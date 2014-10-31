/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include "stm.h"
#include "period.h"


static int stm_test (FILE *, char *, const int);
static int stm_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info stm_loader = {
    "STM",
    "Scream Tracker 2",
    stm_test,
    stm_load
};

static int stm_test(FILE *f, char *t, const int start)
{
    char buf[8];

    fseek(f, start + 20, SEEK_SET);
    if (fread(buf, 1, 8, f) < 8)
	return -1;
    if (memcmp(buf, "!Scream!", 8) && memcmp(buf, "BMOD2STM", 8))
	return -1;

    read8(f);

    if (read8(f) != STM_TYPE_MODULE)
	return -1;

    if (read8(f) < 1)		/* We don't want STX files */
	return -1;

    fseek(f, start + 0, SEEK_SET);
    read_title(f, t, 20);

    return 0;
}



#define FX_NONE		0xff

/*
 * Skaven's note from http://www.futurecrew.com/skaven/oldies_music.html
 *
 * FYI for the tech-heads: In the old Scream Tracker 2 the Arpeggio command
 * (Jxx), if used in a single row with a 0x value, caused the note to skip
 * the specified amount of halftones upwards halfway through the row. I used
 * this in some songs to give the lead some character. However, when played
 * in ModPlug Tracker, this effect doesn't work the way it did back then.
 */

static uint8 fx[] = {
    FX_NONE,		FX_TEMPO,
    FX_JUMP,		FX_BREAK,
    FX_VOLSLIDE,	FX_PORTA_DN,
    FX_PORTA_UP,	FX_TONEPORTA,
    FX_VIBRATO,		FX_TREMOR,
    FX_ARPEGGIO
};


static int stm_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j;
    struct xxm_event *event;
    struct stm_file_header sfh;
    uint8 b;
    int bmod2stm = 0;

    LOAD_INIT();

    fread(&sfh.name, 20, 1, f);			/* ASCIIZ song name */
    fread(&sfh.magic, 8, 1, f);			/* '!Scream!' */
    sfh.rsvd1 = read8(f);			/* '\x1a' */
    sfh.type = read8(f);			/* 1=song, 2=module */
    sfh.vermaj = read8(f);			/* Major version number */
    sfh.vermin = read8(f);			/* Minor version number */
    sfh.tempo = read8(f);			/* Playback tempo */
    sfh.patterns = read8(f);			/* Number of patterns */
    sfh.gvol = read8(f);			/* Global volume */
    fread(&sfh.rsvd2, 13, 1, f);		/* Reserved */

    for (i = 0; i < 31; i++) {
	fread(&sfh.ins[i].name, 12, 1, f);	/* ASCIIZ instrument name */
	sfh.ins[i].id = read8(f);		/* Id=0 */
	sfh.ins[i].idisk = read8(f);		/* Instrument disk */
	sfh.ins[i].rsvd1 = read16l(f);		/* Reserved */
	sfh.ins[i].length = read16l(f);		/* Sample length */
	sfh.ins[i].loopbeg = read16l(f);	/* Loop begin */
	sfh.ins[i].loopend = read16l(f);	/* Loop end */
	sfh.ins[i].volume = read8(f);		/* Playback volume */
	sfh.ins[i].rsvd2 = read8(f);		/* Reserved */
	sfh.ins[i].c2spd = read16l(f);		/* C4 speed */
	sfh.ins[i].rsvd3 = read32l(f);		/* Reserved */
	sfh.ins[i].paralen = read16l(f);	/* Length in paragraphs */
    }

    if (!strncmp ((char *)sfh.magic, "BMOD2STM", 8))
	bmod2stm = 1;

    m->xxh->pat = sfh.patterns;
    m->xxh->trk = m->xxh->pat * m->xxh->chn;
    m->xxh->tpo = MSN (sfh.tempo);
    m->xxh->ins = 31;
    m->xxh->smp = m->xxh->ins;
    m->c4rate = C4_NTSC_RATE;

    copy_adjust((uint8 *)m->name, sfh.name, 20);

    if (bmod2stm) {
	snprintf(m->type, XMP_NAMESIZE, "!Scream! (BMOD2STM)");
    } else {
	snprintf(m->type, XMP_NAMESIZE, "!Scream! "
			"(Scream Tracker %d.%02d)", sfh.vermaj, sfh.vermin);
    }

    MODULE_INFO();

    INSTRUMENT_INIT();

    reportv(ctx, 1, "     Sample name    Len  LBeg LEnd L Vol C2Spd\n");

    /* Read and convert instruments and samples */
    for (i = 0; i < m->xxh->ins; i++) {
	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);
	m->xxih[i].nsm = !!(m->xxs[i].len = sfh.ins[i].length);
	m->xxs[i].lps = sfh.ins[i].loopbeg;
	m->xxs[i].lpe = sfh.ins[i].loopend;
	if (m->xxs[i].lpe == 0xffff)
	    m->xxs[i].lpe = 0;
	m->xxs[i].flg = m->xxs[i].lpe > 0 ? WAVE_LOOPING : 0;
	m->xxi[i][0].vol = sfh.ins[i].volume;
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].sid = i;

	copy_adjust(m->xxih[i].name, sfh.ins[i].name, 12);

	if ((V(1)) && (strlen((char *) m->xxih[i].name) || (m->xxs[i].len > 1))) {
	    report ("[%2X] %-14.14s %04x %04x %04x %c V%02x %5d\n", i,
		m->xxih[i].name, m->xxs[i].len, m->xxs[i].lps, m->xxs[i].lpe, m->xxs[i].flg
		& WAVE_LOOPING ? 'L' : ' ', m->xxi[i][0].vol, sfh.ins[i].c2spd);
	}

	sfh.ins[i].c2spd = 8363 * sfh.ins[i].c2spd / 8448;
	c2spd_to_note (sfh.ins[i].c2spd, &m->xxi[i][0].xpo, &m->xxi[i][0].fin);
    }

    fread(m->xxo, 1, 128, f);

    for (i = 0; i < 128; i++)
	if (m->xxo[i] >= m->xxh->pat)
	    break;

    m->xxh->len = i;

    reportv(ctx, 0, "Module length  : %d patterns\n", m->xxh->len);

    PATTERN_INIT();

    /* Read and convert patterns */
    reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	m->xxp[i]->rows = 64;
	TRACK_ALLOC (i);
	for (j = 0; j < 64 * m->xxh->chn; j++) {
	    event = &EVENT (i, j % m->xxh->chn, j / m->xxh->chn);
	    b = read8(f);
	    memset (event, 0, sizeof (struct xxm_event));
	    switch (b) {
	    case 251:
	    case 252:
	    case 253:
		break;
	    case 255:
		b = 0;
	    default:
		event->note = b ? 1 + LSN(b) + 12 * (2 + MSN(b)) : 0;
		b = read8(f);
		event->vol = b & 0x07;
		event->ins = (b & 0xf8) >> 3;
		b = read8(f);
		event->vol += (b & 0xf0) >> 1;
		if (event->vol > 0x40)
		    event->vol = 0;
		else
		    event->vol++;
		event->fxt = fx[LSN(b)];
		event->fxp = read8(f);
		switch (event->fxt) {
		case FX_TEMPO:
		    event->fxp = MSN (event->fxp);
		    break;
		case FX_NONE:
		    event->fxp = event->fxt = 0;
		    break;
		}
	    }
	}
	reportv(ctx, 0, ".");
    }

    /* Read samples */
    reportv(ctx, 0, "\nStored samples : %d ", m->xxh->smp);

    for (i = 0; i < m->xxh->ins; i++) {
	xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate, 0,
	    &m->xxs[m->xxi[i][0].sid], NULL);
	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    m->quirk |= XMP_QRK_VSALL | XMP_QUIRK_ST3;

    return 0;
}
