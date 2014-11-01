/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Based on the format description by FreeJack of The Elven Nation
 *
 * The loader recognizes four subformats:
 * - MAS_UTrack_V001: Ultra Tracker version < 1.4
 * - MAS_UTrack_V002: Ultra Tracker version 1.4
 * - MAS_UTrack_V003: Ultra Tracker version 1.5
 * - MAS_UTrack_V004: Ultra Tracker version 1.6
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "period.h"
#include "load.h"


static int ult_test (FILE *, char *, const int);
static int ult_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info ult_loader = {
    "ULT",
    "Ultra Tracker",
    ult_test,
    ult_load
};

static int ult_test(FILE *f, char *t, const int start)
{
    char buf[15];

    if (fread(buf, 1, 15, f) < 15)
	return -1;

    if (memcmp(buf, "MAS_UTrack_V000", 14))
	return -1;

    if (buf[14] < '0' || buf[14] > '4')
	return -1;

    read_title(f, t, 32);

    return 0;
}


#define KEEP_TONEPORTA 32	/* Rows to keep portamento effect */

struct ult_header {
    uint8 magic[15];		/* 'MAS_UTrack_V00x' */
    uint8 name[32];		/* Song name */
    uint8 msgsize;		/* ver < 1.4: zero */
};

struct ult_header2 {
    uint8 order[256];		/* Orders */
    uint8 channels;		/* Number of channels - 1 */
    uint8 patterns;		/* Number of patterns - 1 */
};

struct ult_instrument {
    uint8 name[32];		/* Instrument name */
    uint8 dosname[12];		/* DOS file name */
    uint32 loop_start;		/* Loop start */
    uint32 loopend;		/* Loop end */
    uint32 sizestart;		/* Sample size is sizeend - sizestart */
    uint32 sizeend;
    uint8 volume;		/* Volume (log; ver >= 1.4 linear) */
    uint8 bidiloop;		/* Sample loop flags */
    uint16 finetune;		/* Finetune */
    uint16 c2spd;		/* C2 frequency */
};

struct ult_event {
    /* uint8 note; */
    uint8 ins;
    uint8 fxt;			/* MSN = fxt, LSN = f2t */
    uint8 f2p;			/* Secondary comes first -- little endian! */
    uint8 fxp;
};


static char *verstr[4] = {
    "< 1.4", "1.4", "1.5", "1.6"
};


static int ult_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j, k, ver, cnt;
    struct xxm_event *event;
    struct ult_header ufh;
    struct ult_header2 ufh2;
    struct ult_instrument uih;
    struct ult_event ue;
    int keep_porta1 = 0, keep_porta2 = 0;
    uint8 x8;

    LOAD_INIT();

    fread(&ufh.magic, 15, 1, f);
    fread(&ufh.name, 32, 1, f);
    ufh.msgsize = read8(f);

    ver = ufh.magic[14] - '0';

    strncpy(m->name, (char *)ufh.name, 32);
    ufh.name[0] = 0;
    sprintf(m->type, "ULT V%04d (Ultra Tracker %s)", ver, verstr[ver - 1]);

    MODULE_INFO();

    fseek(f, ufh.msgsize * 32, SEEK_CUR);

    m->xxh->ins = m->xxh->smp = read8(f);
    /* m->xxh->flg |= XXM_FLG_LINEAR; */

    /* Read and convert instruments */

    INSTRUMENT_INIT();

    reportv(ctx, 1, "Instruments    : %d ", m->xxh->ins);

    for (i = 0; i < m->xxh->ins; i++) {
	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);

	fread(&uih.name, 32, 1, f);
	fread(&uih.dosname, 12, 1, f);
	uih.loop_start = read32l(f);
	uih.loopend = read32l(f);
	uih.sizestart = read32l(f);
	uih.sizeend = read32l(f);
	uih.volume = read8(f);
	uih.bidiloop = read8(f);
	uih.finetune = read16l(f);
	uih.c2spd = ver < 4 ? 0 : read16l(f);

	if (ver > 3) {			/* Incorrect in ult_form.txt */
	    uih.c2spd ^= uih.finetune;
	    uih.finetune ^= uih.c2spd;
	    uih.c2spd ^= uih.finetune;
	}
	m->xxs[i].len = uih.sizeend - uih.sizestart;
	m->xxih[i].nsm = !!m->xxs[i].len;
	m->xxs[i].lps = uih.loop_start;
	m->xxs[i].lpe = uih.loopend;

	/* BiDi Loop : (Bidirectional Loop)
	 *
	 * UT takes advantage of the Gus's ability to loop a sample in
	 * several different ways. By setting the Bidi Loop, the sample can
	 * be played forward or backwards, looped or not looped. The Bidi
	 * variable also tracks the sample resolution (8 or 16 bit).
	 *
	 * The following table shows the possible values of the Bidi Loop.
	 * Bidi = 0  : No looping, forward playback,  8bit sample
	 * Bidi = 4  : No Looping, forward playback, 16bit sample
	 * Bidi = 8  : Loop Sample, forward playback, 8bit sample
	 * Bidi = 12 : Loop Sample, forward playback, 16bit sample
	 * Bidi = 24 : Loop Sample, reverse playback 8bit sample
	 * Bidi = 28 : Loop Sample, reverse playback, 16bit sample
	 */

	/* Claudio's note: I'm ignoring reverse playback for samples */

	switch (uih.bidiloop) {
	case 20:		/* Type 20 is in seasons.ult */
	case 4:
	    m->xxs[i].flg = WAVE_16_BITS;
	    m->xxs[i].len <<= 1;
	    break;
	case 8:
	case 24:
	    m->xxs[i].flg = WAVE_LOOPING;
	    break;
	case 12:
	case 28:
	    m->xxs[i].flg = WAVE_16_BITS | WAVE_LOOPING;
	    m->xxs[i].len <<= 1;
	    break;
	}

/* TODO: Add logarithmic volume support */
	m->xxi[i][0].vol = uih.volume;
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].sid = i;

	copy_adjust(m->xxih[i].name, uih.name, 24);

	if ((V(1)) && (strlen((char *) uih.name) || m->xxs[i].len)) {
	    report ("\n[%2X] %-32.32s %05x%c%05x %05x %c V%02x F%04x %5d",
		i, uih.name, m->xxs[i].len,
		m->xxs[i].flg & WAVE_16_BITS ? '+' : ' ',
		m->xxs[i].lps, m->xxs[i].lpe,
		m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ',
		m->xxi[i][0].vol, uih.finetune, uih.c2spd);
	}

	if (ver > 3)
	    c2spd_to_note(uih.c2spd, &m->xxi[i][0].xpo, &m->xxi[i][0].fin);
    }

    reportv(ctx, 1, "\n");

    fread(&ufh2.order, 256, 1, f);
    ufh2.channels = read8(f);
    ufh2.patterns = read8(f);

    for (i = 0; i < 256; i++) {
	if (ufh2.order[i] == 0xff)
	    break;
	m->xxo[i] = ufh2.order[i];
    }
    m->xxh->len = i;
    m->xxh->chn = ufh2.channels + 1;
    m->xxh->pat = ufh2.patterns + 1;
    m->xxh->tpo = 6;
    m->xxh->bpm = 125;
    m->xxh->trk = m->xxh->chn * m->xxh->pat;

    for (i = 0; i < m->xxh->chn; i++) {
	if (ver >= 3) {
	    x8 = read8(f);
	    m->xxc[i].pan = 255 * x8 / 15;
	} else {
	    m->xxc[i].pan = (((i + 1) / 2) % 2) * 0xff;	/* ??? */
	}
    }

    PATTERN_INIT();

    /* Read and convert patterns */

    if (V(0))
	report ("Stored patterns: %d ", m->xxh->pat);

    /* Events are stored by channel */
    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	m->xxp[i]->rows = 64;
	TRACK_ALLOC (i);
    }

    for (i = 0; i < m->xxh->chn; i++) {
	for (j = 0; j < 64 * m->xxh->pat; ) {
	    cnt = 1;
	    x8 = read8(f);		/* Read note or repeat code (0xfc) */
	    if (x8 == 0xfc) {
		cnt = read8(f);			/* Read repeat count */
		x8 = read8(f);			/* Read note */
	    }
	    fread(&ue, 4, 1, f);		/* Read rest of the event */

	    if (cnt == 0)
		cnt++;

	    for (k = 0; k < cnt; k++, j++) {
		event = &EVENT (j >> 6, i , j & 0x3f);
		memset(event, 0, sizeof (struct xxm_event));
		if (x8)
		    event->note = x8 + 24;
		event->ins = ue.ins;
		event->fxt = MSN (ue.fxt);
		event->f2t = LSN (ue.fxt);
		event->fxp = ue.fxp;
		event->f2p = ue.f2p;

		switch (event->fxt) {
		case 0x00:		/* <mumble> */
		    if (event->fxp)
			keep_porta1 = 0;
		    if (keep_porta1) {
			event->fxt = 0x03;
			keep_porta1--;
		    }
		    break;
		case 0x03:		/* Portamento kludge */
		    keep_porta1 = KEEP_TONEPORTA;
		    break;
		case 0x05:		/* 'Special' effect */
		case 0x06:		/* Reserved */
		    event->fxt = event->fxp = 0;
		    break;
		case 0x0b:		/* Pan */
		    event->fxt = FX_SETPAN;
		    event->fxp <<= 4;
		    break;
		case 0x09:		/* Sample offset */
/* TODO: fine sample offset */
		    event->fxp <<= 2;
		    break;
		}

		switch (event->f2t) {
		case 0x00:		/* <mumble> */
		    if (event->f2p)
			keep_porta2 = 0;
		    if (keep_porta2) {
			event->f2t = 0x03;
			keep_porta2--;
		    }
		    break;
		case 0x03:		/* Portamento kludge */
		    keep_porta2 = KEEP_TONEPORTA;
		    break;
		case 0x05:		/* 'Special' effect */
		case 0x06:		/* Reserved */
		    event->f2t = event->f2p = 0;
		    break;
		case 0x0b:		/* Pan */
		    event->f2t = FX_SETPAN;
		    event->f2p <<= 4;
		    break;
		case 0x09:		/* Sample offset */
/* TODO: fine sample offset */
		    event->f2p <<= 2;
		    break;
		}

	    }
	    if (V(0) && (j % (64 * m->xxh->chn) == 0))
		report (".");
	}
    }

    reportv(ctx, 0, "\nStored samples : %d ", m->xxh->smp);

    for (i = 0; i < m->xxh->ins; i++) {
	if (!m->xxs[i].len)
	    continue;
	xmp_drv_loadpatch(ctx, f, i, m->c4rate, 0, &m->xxs[i], NULL);
	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    m->volbase = 0x100;

    return 0;
}

