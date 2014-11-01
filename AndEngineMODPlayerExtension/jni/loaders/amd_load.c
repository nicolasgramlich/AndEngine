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


static int amd_test (FILE *, char *, const int);
static int amd_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info amd_loader = {
    "AMD",
    "Amusic Adlib Tracker",
    amd_test,
    amd_load
};

static int amd_test(FILE *f, char *t, const int start)
{
    char buf[9];

    fseek(f, start + 1062, SEEK_SET);
    if (fread(buf, 1, 9, f) < 9)
	return -1;

    if (memcmp(buf, "<o", 2) || memcmp(buf + 6, "RoR", 3))
	return -1;

    fseek(f, start + 0, SEEK_SET);
    read_title(f, t, 24);

    return 0;
}



struct amd_instrument {
    uint8 name[23];		/* Instrument name */
    uint8 reg[11];		/* Adlib registers */
};


struct amd_file_header {
    uint8 name[24];		/* ASCIIZ song name */
    uint8 author[24];		/* ASCIIZ author name */
    struct amd_instrument ins[26];	/* Instruments */
    uint8 len;			/* Song length */
    uint8 pat;			/* Index of last pattern */
    uint8 order[128];		/* Orders */
    uint8 magic[9];		/* 3c 6f ef 51 55 ee 52 6f 52 */
    uint8 version;		/* 0x10=normal module, 0x11=packed */
};

static int reg_xlat[] = { 0, 5, 1, 6, 2, 7, 3, 8, 4, 9, 10 };


static int amd_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int r, i, j, tmode = 1;
    struct amd_file_header afh;
    struct xxm_event *event;
    char regs[11];
    uint16 w;
    uint8 b;

    LOAD_INIT();

    fread(&afh.name, 24, 1, f);
    fread(&afh.author, 24, 1, f);
    for (i = 0; i < 26; i++) {
	fread(&afh.ins[i].name, 23, 1, f);
	fread(&afh.ins[i].reg, 11, 1, f);
    }
    afh.len = read8(f);
    afh.pat = read8(f);
    fread(&afh.order, 128, 1, f);
    fread(&afh.magic, 9, 1, f);
    afh.version = read8(f);

    m->xxh->chn = 9;
    m->xxh->bpm = 125;
    m->xxh->tpo = 6;
    m->xxh->len = afh.len;
    m->xxh->pat = afh.pat + 1;
    m->xxh->ins = 26;
    m->xxh->smp = 0;
    memcpy (m->xxo, afh.order, m->xxh->len);

    strcpy(m->type, "Amusic");
    strncpy(m->name, (char *)afh.name, 24);
    strncpy(m->author, (char *)afh.author, 24);

    MODULE_INFO();

    if (V(0))
	report ("Instruments    : %d ", m->xxh->ins);

    INSTRUMENT_INIT();

    /* Load instruments */
    for (i = 0; i < m->xxh->ins; i++) {
	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);

	copy_adjust(m->xxih[i].name, afh.ins[i].name, 23);

	m->xxih[i].nsm = 1;
	m->xxi[i][0].vol = 0x40;
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].sid = i;
	m->xxi[i][0].xpo = -1;

	for (j = 0; j < 11; j++)
	    regs[j] = afh.ins[i].reg[reg_xlat[j]];

	if (V(1)) {
	    report ("\n[%2X] %-23.23s ", i, m->xxih[i].name);
	    if (regs[0] | regs[1] | regs[2] | regs[3] | regs[4] | regs[5] | regs[6]
		| regs[7] | regs[8] | regs[9] | regs[10]) {
		for (j = 0; j < 11; j++)
		    report ("%02x ", (uint8) regs[j]);
	    }
	}
	if (V(0) == 1)
	    report (".");
	xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, 0, 0, NULL, regs);
    }
    if (V(0))
	report ("\n");

    if (!afh.version) {
	report (
	    "Aborting: Unpacked modules not supported. Please contact the authors.\n");
	return -1;
    }
    if (V(0))
	report ("Stored patterns: %d ", m->xxh->pat);
    m->xxp = calloc (sizeof (struct xxm_pattern *), m->xxh->pat + 1);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	for (j = 0; j < 9; j++) {
	    w = read16l(f);
	    m->xxp[i]->info[j].index = w;
	    if (w > m->xxh->trk)
		m->xxh->trk = w;
	}
	m->xxp[i]->rows = 64;
	if (V(0))
	    report (".");
    }
    m->xxh->trk++;

    w = read16l(f);
    if (V(0))
	report ("\nStored tracks  : %d ", w);
    m->xxt = calloc (sizeof (struct xxm_track *), m->xxh->trk);
    m->xxh->trk = w;

    for (i = 0; i < m->xxh->trk; i++) {
	w = read16l(f);
	m->xxt[w] = calloc (sizeof (struct xxm_track) +
	    sizeof (struct xxm_event) * 64, 1);
	m->xxt[w]->rows = 64;
	for (r = 0; r < 64; r++) {
	    event = &m->xxt[w]->event[r];
	    b = read8(f);		/* Effect parameter */
	    if (b & 0x80) {
		r += (b & 0x7f) - 1;
		continue;
	    }
	    event->fxp = b;
	    b = read8(f);		/* Instrument + effect type */
	    event->ins = MSN (b);
	    switch (b = LSN (b)) {
	    case 0:		/* Arpeggio */
		break;
	    case 4:		/* Set volume */
		b = FX_VOLSET;
		break;
	    case 1:		/* Slide up */
	    case 2:		/* Slide down */
	    case 3:		/* Modulator/carrier intensity */
	    case 8:		/* Tone portamento */
	    case 9:		/* Tremolo/vibrato */
		event->fxp = b = 0;
		break;
	    case 5:		/* Pattern jump */
		b = FX_JUMP;
		break;
	    case 6:		/* Pattern break */
		b = FX_BREAK;
		break;
	    case 7:		/* Speed */
		if (!event->fxp)
		    tmode = 3;
		if (event->fxp > 31) {
		    event->fxp = b = 0;
		    break;
		}
		event->fxp *= tmode;
		b = FX_TEMPO;
		break;
	    }
	    event->fxt = b;
	    b = read8(f);	/* Note + octave + instrument */
	    event->ins |= (b & 1) << 4;
	    if ((event->note = MSN (b)))
		event->note += (1 + ((b & 0xe) >> 1)) * 12;
	}
	if (V(0) && !(i % 9))
	    report (".");
    }
    if (V(0))
	report ("\n");

    for (i = 0; i < m->xxh->chn; i++) {
	m->xxc[i].pan = 0x80;
	m->xxc[i].flg = XXM_CHANNEL_FM;
    }
    return 0;
}
