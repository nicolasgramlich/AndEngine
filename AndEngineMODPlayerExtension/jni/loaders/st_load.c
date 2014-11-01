/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Ultimate Soundtracker support based on the module format description
 * written by Michael Schwendt <sidplay@geocities.com>
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <ctype.h>
#include <sys/types.h>
#include <sys/stat.h>

#include "load.h"
#include "mod.h"
#include "period.h"

static int st_test (FILE *, char *, const int);
static int st_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info st_loader = {
    "ST",
    "Soundtracker",
    st_test,
    st_load
};

static int period[] = {
    856, 808, 762, 720, 678, 640, 604, 570, 538, 508, 480, 453,
    428, 404, 381, 360, 339, 320, 302, 285, 269, 254, 240, 226,
    214, 202, 190, 180, 170, 160, 151, 143, 135, 127, 120, 113,
    -1
};

static int st_test(FILE *f, char *t, const int start)
{
    int i, j, k;
    int pat, smp_size;
    struct st_header mh;
    uint8 mod_event[4];
    struct stat st;

    fstat(fileno(f), &st);

    if (st.st_size < 600)
	return -1;

    smp_size = 0;

    fread(mh.name, 1, 20, f);
    if (test_name(mh.name, 20) < 0)
	return -1;

    for (i = 0; i < 15; i++) {
	fread(mh.ins[i].name, 1, 22, f);
	mh.ins[i].size = read16b(f);
	mh.ins[i].finetune = read8(f);
	mh.ins[i].volume = read8(f);
	mh.ins[i].loop_start = read16b(f);
	mh.ins[i].loop_size = read16b(f);
    }
    mh.len = read8(f);
    mh.restart = read8(f);
    fread(mh.order, 1, 128, f);
	
    for (pat = i = 0; i < 128; i++) {
	if (mh.order[i] > 0x7f)
	    return -1;
	if (mh.order[i] > pat)
	    pat = mh.order[i];
    }
    pat++;

    if (pat > 0x7f || mh.len == 0 || mh.len > 0x7f)
	return -1;

    for (i = 0; i < 15; i++) {
	if (test_name(mh.ins[i].name, 22) < 0)
	    return -1;

	if (mh.ins[i].volume > 0x40)
	    return -1;

	if (mh.ins[i].finetune > 0x0f)
	    return -1;

	if (mh.ins[i].size > 0x8000)
	    return -1;

	if ((mh.ins[i].loop_start >> 1) > 0x8000)
	    return -1;

	if (mh.ins[i].loop_size > 0x8000)
	    return -1;

	/* This test fails in atmosfer.mod, disable it 
	 *
	 * if (mh.ins[i].loop_size > 1 && mh.ins[i].loop_size > mh.ins[i].size)
	 *    return -1;
	 */

	if ((mh.ins[i].loop_start >> 1) > mh.ins[i].size)
	    return -1;

	if (mh.ins[i].size && (mh.ins[i].loop_start >> 1) == mh.ins[i].size)
	    return -1;

	if (mh.ins[i].size == 0 && mh.ins[i].loop_start > 0)
	    return -1;

	smp_size += 2 * mh.ins[i].size;
    }

    if (smp_size < 8)
	return -1;

    if (st.st_size < (600 + pat * 1024 + smp_size))
	return -1;

    for (i = 0; i < pat; i++) {
	for (j = 0; j < (64 * 4); j++) {
	    int p;
	
	    fread (mod_event, 1, 4, f);

	    if (MSN(mod_event[0]))	/* sample number > 15 */
		return -1;

	    p = 256 * LSN(mod_event[0]) + mod_event[1];

	    if (p == 0)
		continue;

	    if (p == 162)	/* used in Karsten Obarski's blueberry.mod */
		continue;

	    for (k = 0; period[k] >= 0; k++) {
		if (p == period[k])
		    break;
	    }
	    if (period[k] < 0)
		return -1;
	}
    }

    return 0;
}

static int st_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    struct xmp_options *o = &ctx->o;
    int i, j;
    int smp_size, pat_size;
    struct xxm_event ev, *event;
    struct st_header mh;
    uint8 mod_event[4];
    int ust = 1, nt = 0, serr = 0;
    /* int lps_mult = m->fetch & XMP_CTL_FIXLOOP ? 1 : 2; */
    char *modtype;
    int fxused;
    int pos;

    LOAD_INIT();

    m->xxh->ins = 15;
    m->xxh->smp = m->xxh->ins;
    smp_size = 0;
    pat_size = 0;

    fread(mh.name, 1, 20, f);
    for (i = 0; i < 15; i++) {
	fread(mh.ins[i].name, 1, 22, f);
	mh.ins[i].size = read16b(f);
	mh.ins[i].finetune = read8(f);
	mh.ins[i].volume = read8(f);
	mh.ins[i].loop_start = read16b(f);
	mh.ins[i].loop_size = read16b(f);
    }
    mh.len = read8(f);
    mh.restart = read8(f);
    fread(mh.order, 1, 128, f);
	
    m->xxh->len = mh.len;
    m->xxh->rst = mh.restart;

    /* UST: The byte at module offset 471 is BPM, not the song restart
     *      The default for UST modules is 0x78 = 120 BPM = 48 Hz.
     */
    if (m->xxh->rst < 0x40)	/* should be 0x20 */
	ust = 0;

    memcpy (m->xxo, mh.order, 128);

    for (i = 0; i < 128; i++)
	if (m->xxo[i] > m->xxh->pat)
	    m->xxh->pat = m->xxo[i];
    m->xxh->pat++;

    pat_size = 256 * m->xxh->chn * m->xxh->pat;

    for (i = 0; i < m->xxh->ins; i++) {
	/* UST: Volume word does not contain a "Finetuning" value in its
	 * high-byte.
	 */
	if (mh.ins[i].finetune)
	    ust = 0;

	if (mh.ins[i].size == 0 && mh.ins[i].loop_size == 1)
	    nt = 1;

	/* UST: Maximum sample length is 9999 bytes decimal, but 1387 words
	 * hexadecimal. Longest samples on original sample disk ST-01 were
	 * 9900 bytes.
	 */
	if (mh.ins[i].size > 0x1387 || mh.ins[i].loop_start > 9999
		|| mh.ins[i].loop_size > 0x1387)
	    ust = 0;

	smp_size += 2 * mh.ins[i].size;
    }

    INSTRUMENT_INIT();

    for (i = 0; i < m->xxh->ins; i++) {
	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);
	m->xxs[i].len = 2 * mh.ins[i].size;
	m->xxs[i].lps = mh.ins[i].loop_start;
	m->xxs[i].lpe = m->xxs[i].lps + 2 * mh.ins[i].loop_size;
	m->xxs[i].flg = mh.ins[i].loop_size > 1 ? WAVE_LOOPING : 0;
	m->xxi[i][0].fin = (int8)(mh.ins[i].finetune << 4);
	m->xxi[i][0].vol = mh.ins[i].volume;
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].sid = i;
	m->xxih[i].nsm = !!(m->xxs[i].len);
	strncpy((char *)m->xxih[i].name, (char *)mh.ins[i].name, 22);
	str_adj((char *)m->xxih[i].name);
    }

    m->xxh->trk = m->xxh->chn * m->xxh->pat;

    strncpy (m->name, (char *) mh.name, 20);

    /* Scan patterns for tracker detection */
    fxused = 0;
    pos = ftell(f);

    for (i = 0; i < m->xxh->pat; i++) {
	for (j = 0; j < (64 * m->xxh->chn); j++) {
	    fread (mod_event, 1, 4, f);

	    cvt_pt_event (&ev, mod_event);

	    if (ev.fxt)
		fxused |= 1 << ev.fxt;
	    else if (ev.fxp)
		fxused |= 1;
	    
	    /* UST: Only effects 1 (arpeggio) and 2 (pitchbend) are
	     * available.
	     */
	    if (ev.fxt && ev.fxt != 1 && ev.fxt != 2)
		ust = 0;

	    /* Karsten Obarski's sleepwalk mod uses arpeggio 30 and 40 */
	    if (ev.fxt == 1) {		/* unlikely arpeggio */
		if (ev.fxp == 0x00)
		    ust = 0;
		/*if ((ev.fxp & 0x0f) == 0 || (ev.fxp & 0xf0) == 0)
		    ust = 0;*/
	    }

	    if (ev.fxt == 2) {		/* bend up and down at same time? */
		if ((ev.fxp & 0x0f) != 0 && (ev.fxp & 0xf0) != 0)
		    ust = 0;
	    }
	}
    }

    if (fxused & ~0x0006)
	ust = 0;

    if (ust)
	modtype = "Ultimate Soundtracker";
    else if ((fxused & ~0xd007) == 0)
	modtype = "Soundtracker IX";	/* or MasterSoundtracker? */
    else if ((fxused & ~0xf807) == 0)
	modtype = "D.O.C. Soundtracker";
    else if ((fxused & ~0xfc07) == 0)
	modtype = "Soundtracker 2.3/2.4";
    else if ((fxused & ~0xfc3f) == 0)
	modtype = "Noisetracker 1.0/1.2";
    else if ((fxused & ~0xfcbf) == 0)
	modtype = "Noisetracker 2.0";
    else
	modtype = "unknown tracker";

    if (ust)
	snprintf(m->type, XMP_NAMESIZE, "UST (%s)", modtype);
    else
	snprintf(m->type, XMP_NAMESIZE, "ST (%s)", modtype);

    MODULE_INFO();

    if (serr && V(2))
	report ("File size error: %d\n", serr);

    fseek(f, start + pos, SEEK_SET);

    PATTERN_INIT();

    /* Load and convert patterns */

    reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	m->xxp[i]->rows = 64;
	TRACK_ALLOC (i);
	for (j = 0; j < (64 * m->xxh->chn); j++) {
	    event = &EVENT (i, j % m->xxh->chn, j / m->xxh->chn);
	    fread (mod_event, 1, 4, f);

	    cvt_pt_event(event, mod_event);
	}
	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    reportv(ctx, 1, "     Instrument name        Len  LBeg LEnd L Vol Fin\n");

    for (i = 0; (V(1)) && (i < m->xxh->ins); i++) {
	if (*m->xxih[i].name || m->xxs[i].len > 2) {
	    report ("[%2X] %-22.22s %04x %04x %04x %c V%02x %+d\n",
		i, m->xxih[i].name, m->xxs[i].len, m->xxs[i].lps,
		m->xxs[i].lpe, mh.ins[i].loop_size > 1 ? 'L' : ' ',
		m->xxi[i][0].vol, m->xxi[i][0].fin >> 4);
	}
    }

    m->xxh->flg |= XXM_FLG_MODRNG;

    /* Perform the necessary conversions for Ultimate Soundtracker */
    if (ust) {
	/* Fix restart & bpm */
	m->xxh->bpm = m->xxh->rst;
	m->xxh->rst = 0;

	/* Fix sample loops */
	for (i = 0; i < m->xxh->ins; i++) {
	    /* FIXME */	
	}

	/* Fix effects (arpeggio and pitchbending) */
	for (i = 0; i < m->xxh->pat; i++) {
	    for (j = 0; j < (64 * m->xxh->chn); j++) {
		event = &EVENT(i, j % m->xxh->chn, j / m->xxh->chn);
		if (event->fxt == 1)
		    event->fxt = 0;
		else if (event->fxt == 2 && (event->fxp & 0xf0) == 0)
		    event->fxt = 1;
		else if (event->fxt == 2 && (event->fxp & 0x0f) == 0)
		    event->fxp >>= 4;
	    }
	}
    } else {
	if (m->xxh->rst >= m->xxh->len)
	    m->xxh->rst = 0;
    }

    if (o->skipsmp)
	return 0;

    /* Load samples */

    reportv(ctx, 0, "Stored samples : %d ", m->xxh->smp);

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
