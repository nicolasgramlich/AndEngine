/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Loader for Soundtracker 2.6/Ice Tracker modules */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"

#define MAGIC_MTN_	MAGIC4('M','T','N',0)
#define MAGIC_IT10	MAGIC4('I','T','1','0')


static int ice_test (FILE *, char *, const int);
static int ice_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info ice_loader = {
    "MTN",
    "Soundtracker 2.6/Ice Tracker",
    ice_test,
    ice_load
};

static int ice_test(FILE *f, char *t, const int start)
{
    uint32 magic;

    fseek(f, start + 1464, SEEK_SET);
    magic = read32b(f);
    if (magic != MAGIC_MTN_ && magic != MAGIC_IT10)
	return -1;

    fseek(f, start + 0, SEEK_SET);
    read_title(f, t, 28);

    return 0;
}


struct ice_ins {
    char name[22];		/* Instrument name */
    uint16 len;			/* Sample length / 2 */
    uint8 finetune;		/* Finetune */
    uint8 volume;		/* Volume (0-63) */
    uint16 loop_start;		/* Sample loop start in file */
    uint16 loop_size;		/* Loop size / 2 */
};

struct ice_header {
    char title[20];
    struct ice_ins ins[31];	/* Instruments */
    uint8 len;			/* Size of the pattern list */
    uint8 trk;			/* Number of tracks */
    uint8 ord[128][4];
    uint32 magic;		/* 'MTN\0', 'IT10' */
};


static int ice_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j;
    struct xxm_event *event;
    struct ice_header ih;
    uint8 ev[4];

    LOAD_INIT();

    fread(&ih.title, 20, 1, f);
    for (i = 0; i < 31; i++) {
	fread(&ih.ins[i].name, 22, 1, f);
	ih.ins[i].len = read16b(f);
	ih.ins[i].finetune = read8(f);
	ih.ins[i].volume = read8(f);
	ih.ins[i].loop_start = read16b(f);
	ih.ins[i].loop_size = read16b(f);
    }
    ih.len = read8(f);
    ih.trk = read8(f);
    fread(&ih.ord, 128 * 4, 1, f);
    ih.magic = read32b(f);

    if (ih.magic == MAGIC_IT10)
        strcpy(m->type, "IT10 (Ice Tracker)");
    else if (ih.magic == MAGIC_MTN_)
        strcpy(m->type, "MTN (Soundtracker 2.6)");
    else
	return -1;

    m->xxh->ins = 31;
    m->xxh->smp = m->xxh->ins;
    m->xxh->pat = ih.len;
    m->xxh->len = ih.len;
    m->xxh->trk = ih.trk;

    strncpy (m->name, (char *) ih.title, 20);
    MODULE_INFO();

    INSTRUMENT_INIT();

    reportv(ctx, 1, "     Instrument name        Len  LBeg LEnd L Vl Ft\n");

    for (i = 0; i < m->xxh->ins; i++) {
	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);
	m->xxih[i].nsm = !!(m->xxs[i].len = 2 * ih.ins[i].len);
	m->xxs[i].lps = 2 * ih.ins[i].loop_start;
	m->xxs[i].lpe = m->xxs[i].lps + 2 * ih.ins[i].loop_size;
	m->xxs[i].flg = ih.ins[i].loop_size > 1 ? WAVE_LOOPING : 0;
	m->xxi[i][0].vol = ih.ins[i].volume;
	m->xxi[i][0].fin = ((int16)ih.ins[i].finetune / 0x48) << 4;
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].sid = i;
	if (V(1) && m->xxs[i].len > 2)
	    report ("[%2X] %-22.22s %04x %04x %04x %c %02x %+01x\n",
		i, ih.ins[i].name, m->xxs[i].len, m->xxs[i].lps, m->xxs[i].lpe,
		m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ', m->xxi[i][0].vol,
		m->xxi[i][0].fin >> 4);
    }

    PATTERN_INIT();

    if (V(0))
	report ("Stored patterns: %d ", m->xxh->pat);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	m->xxp[i]->rows = 64;
	for (j = 0; j < m->xxh->chn; j++) {
	    m->xxp[i]->info[j].index =  ih.ord[i][j];
	}
	m->xxo[i] = i;

	reportv(ctx, 0, ".");
    }

    reportv(ctx, 0, "\nStored tracks  : %d ", m->xxh->trk);

    for (i = 0; i < m->xxh->trk; i++) {
	m->xxt[i] = calloc (sizeof (struct xxm_track) + sizeof
		(struct xxm_event) * 64, 1);
	m->xxt[i]->rows = 64;
	for (j = 0; j < m->xxt[i]->rows; j++) {
		event = &m->xxt[i]->event[j];
		fread (ev, 1, 4, f);
		cvt_pt_event (event, ev);
	}

	if (V(0) && !(i % m->xxh->chn))
	    report (".");
    }

    m->xxh->flg |= XXM_FLG_MODRNG;

    /* Read samples */

    reportv(ctx, 0, "\nStored samples : %d ", m->xxh->smp);

    for (i = 0; i < m->xxh->ins; i++) {
	if (m->xxs[i].len <= 4)
	    continue;
	xmp_drv_loadpatch(ctx, f, i, m->c4rate, 0, &m->xxs[i], NULL);
	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    return 0;
}

