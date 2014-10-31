/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Loader for Images Music System modules based on the EP replayer.
 *
 * Date: Thu, 19 Apr 2001 19:13:06 +0200
 * From: Michael Doering <mldoering@gmx.net>
 *
 * I just "stumbled" upon something about the Unic.3C format when I was
 * testing replayers for the upcoming UADE 0.21 that might be also
 * interesting to you for xmp. The "Beastbusters" tune is not a UNIC file :)
 * It's actually a different Format, although obviously related, called
 * "Images Music System".
 *
 * I was testing the replayer from the Wanted Team with one of their test
 * tunes, among them also the beastbuster music. When I first listened to
 * it, I knew I have heard it somewhere, a bit different but it was alike.
 * This one had more/richer percussions and there was no strange beep in
 * the bg. ;) After some searching on my HD I found it among the xmp test
 * tunes as a UNIC file.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <ctype.h>
#include <sys/types.h>

#include "load.h"
#include "period.h"

struct ims_instrument {
    uint8 name[20];
    int16 finetune;		/* Causes squeaks in beast-busters1! */
    uint16 size;
    uint8 unknown;
    uint8 volume;
    uint16 loop_start;
    uint16 loop_size;
};

struct ims_header {
    uint8 title[20];
    struct ims_instrument ins[31];
    uint8 len;
    uint8 zero;
    uint8 orders[128];
    uint8 magic[4];
};


static int ims_test (FILE *, char *, const int);
static int ims_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info ims_loader = {
    "IMS",
    "Images Music System",
    ims_test,
    ims_load
};

static int ims_test(FILE *f, char *t, const int start)
{
    int i;
    int smp_size, pat;
    struct ims_header ih;

    smp_size = 0;

    fread(&ih.title, 20, 1, f);

    for (i = 0; i < 31; i++) {
	if (fread(&ih.ins[i].name, 1, 20, f) < 20)
	    return -1;

	ih.ins[i].finetune = (int16)read16b(f);
	ih.ins[i].size = read16b(f);
	ih.ins[i].unknown = read8(f);
	ih.ins[i].volume = read8(f);
	ih.ins[i].loop_start = read16b(f);
	ih.ins[i].loop_size = read16b(f);

	smp_size += ih.ins[i].size * 2;

	if (test_name(ih.ins[i].name, 20) < 0)
	    return -1;

	if (ih.ins[i].volume > 0x40)
	    return -1;

	if (ih.ins[i].size > 0x8000)
	    return -1;

	if (ih.ins[i].loop_start > ih.ins[i].size)
	    return -1;

        if (ih.ins[i].size && ih.ins[i].loop_size > 2 * ih.ins[i].size)
	    return -1;
    }

    if (smp_size < 8)
	return -1;

    ih.len = read8(f);
    ih.zero = read8(f);
    fread (&ih.orders, 128, 1, f);
    fread (&ih.magic, 4, 1, f);
  
    if (ih.zero > 1)		/* not sure what this is */
	return -1;

    if (ih.magic[3] != 0x3c)
	return -1;

    if (ih.len > 0x7f)
	return -1;

    for (pat = i = 0; i < ih.len; i++)
	if (ih.orders[i] > pat)
	    pat = ih.orders[i];
    pat++;

    if (pat > 0x7f || ih.len == 0 || ih.len > 0x7f)
	return -1;
   
    fseek(f, start + 0, SEEK_SET);
    read_title(f, t, 20);

    return 0;
}


static int ims_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j;
    int smp_size;
    struct xxm_event *event;
    struct ims_header ih;
    uint8 ims_event[3];
    int xpo = 21;		/* Tuned against UADE */

    LOAD_INIT();

    m->xxh->ins = 31;
    m->xxh->smp = m->xxh->ins;
    smp_size = 0;

    fread (&ih.title, 20, 1, f);

    for (i = 0; i < 31; i++) {
	fread (&ih.ins[i].name, 20, 1, f);
	ih.ins[i].finetune = (int16)read16b(f);
	ih.ins[i].size = read16b(f);
	ih.ins[i].unknown = read8(f);
	ih.ins[i].volume = read8(f);
	ih.ins[i].loop_start = read16b(f);
	ih.ins[i].loop_size = read16b(f);

	smp_size += ih.ins[i].size * 2;
    }

    ih.len = read8(f);
    ih.zero = read8(f);
    fread (&ih.orders, 128, 1, f);
    fread (&ih.magic, 4, 1, f);
  
    m->xxh->len = ih.len;
    memcpy (m->xxo, ih.orders, m->xxh->len);

    for (i = 0; i < m->xxh->len; i++)
	if (m->xxo[i] > m->xxh->pat)
	    m->xxh->pat = m->xxo[i];

    m->xxh->pat++;
    m->xxh->trk = m->xxh->chn * m->xxh->pat;

    strncpy(m->name, (char *)ih.title, 20);
    sprintf(m->type, "IMS (Images Music System)");

    MODULE_INFO();

    INSTRUMENT_INIT();

    for (i = 0; i < m->xxh->ins; i++) {
	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);
	m->xxs[i].len = 2 * ih.ins[i].size;
	m->xxs[i].lpe = m->xxs[i].lps + 2 * ih.ins[i].loop_size;
	m->xxs[i].flg = ih.ins[i].loop_size > 1 ? WAVE_LOOPING : 0;
	m->xxi[i][0].fin = 0; /* ih.ins[i].finetune; */
	m->xxi[i][0].vol = ih.ins[i].volume;
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].sid = i;
	m->xxih[i].nsm = !!(m->xxs[i].len);
	m->xxih[i].rls = 0xfff;

	copy_adjust(m->xxih[i].name, ih.ins[i].name, 20);

	if (V(1) &&
		(strlen((char *) m->xxih[i].name) || (m->xxs[i].len > 2))) {
	    report ("[%2X] %-20.20s %04x %04x %04x %c V%02x %+d\n",
		i, m->xxih[i].name, m->xxs[i].len, m->xxs[i].lps,
		m->xxs[i].lpe, ih.ins[i].loop_size > 1 ? 'L' : ' ',
		m->xxi[i][0].vol, m->xxi[i][0].fin >> 4);
	}
    }

    PATTERN_INIT();

    /* Load and convert patterns */
    reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC(i);
	m->xxp[i]->rows = 64;
	TRACK_ALLOC(i);
	for (j = 0; j < 0x100; j++) {
	    event = &EVENT (i, j & 0x3, j >> 2);
	    fread (ims_event, 1, 3, f);

	    /* Event format:
	     *
	     * 0000 0000  0000 0000  0000 0000
	     *  |\     /  \  / \  /  \       /
	     *  | note    ins   fx   parameter
	     * ins
	     *
	     * 0x3f is a blank note.
	     */
	    event->note = ims_event[0] & 0x3f;
	    if (event->note != 0x00 && event->note != 0x3f)
		event->note += xpo;
	    else
		event->note = 0;
	    event->ins = ((ims_event[0] & 0x40) >> 2) | MSN(ims_event[1]);
	    event->fxt = LSN(ims_event[1]);
	    event->fxp = ims_event[2];

	    disable_continue_fx (event);

	    /* According to Asle:
	     * ``Just note that pattern break effect command (D**) uses
	     * HEX value in UNIC format (while it is DEC values in PTK).
	     * Thus, it has to be converted!''
	     *
	     * Is this valid for IMS as well? --claudio
	     */
	    if (event->fxt == 0x0d)
		 event->fxp = (event->fxp / 10) << 4 | (event->fxp % 10);
	}
	reportv(ctx, 0, ".");
    }

    m->xxh->flg |= XXM_FLG_MODRNG;

    /* Load samples */

    reportv(ctx, 0, "\nStored samples : %d ", m->xxh->smp);
    for (i = 0; i < m->xxh->smp; i++) {
	if (!m->xxs[i].len)
	    continue;
	xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate, 0,
	    &m->xxs[m->xxi[i][0].sid], NULL);
	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    return 0;
}
