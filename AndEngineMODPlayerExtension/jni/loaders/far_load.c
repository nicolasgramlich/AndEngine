/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Based on the Farandole Composer format specifications by Daniel Potter.
 *
 * "(...) this format is for EDITING purposes (storing EVERYTHING you're
 * working on) so it may include information not completely neccessary."
 */


#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include "far.h"

#define MAGIC_FAR	MAGIC4('F','A','R',0xfe)


static int far_test (FILE *, char *, const int);
static int far_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info far_loader = {
    "FAR",
    "Farandole Composer",
    far_test,
    far_load
};

static int far_test(FILE *f, char *t, const int start)
{
    if (read32b(f) != MAGIC_FAR)
	return -1;

    read_title(f, t, 40);

    return 0;
}


#define NONE			0xff
#define FX_FAR_SETVIBRATO	0xfe
#define FX_FAR_VSLIDE_UP	0xfd
#define FX_FAR_VSLIDE_DN	0xfc
#define FX_FAR_RETRIG		0xfb
#define FX_FAR_DELAY		0xfa
#define FX_FAR_PORTA_UP		0xf9
#define FX_FAR_PORTA_DN		0xf8

static uint8 fx[] = {
    NONE,
    FX_FAR_PORTA_UP,		/* 0x1?  Pitch Adjust */
    FX_FAR_PORTA_DN,		/* 0x2?  Pitch Adjust */
    FX_PER_TPORTA,		/* 0x3?  Port to Note -- FIXME */
    FX_FAR_RETRIG,		/* 0x4?  Retrigger */
    FX_FAR_SETVIBRATO,		/* 0x5?  Set VibDepth */
    FX_VIBRATO,			/* 0x6?  Vibrato note */
    FX_FAR_VSLIDE_UP,		/* 0x7?  VolSld Up */
    FX_FAR_VSLIDE_DN,		/* 0x8?  VolSld Dn */
    FX_PER_VIBRATO,		/* 0x9?  Vibrato Sust */
    NONE,			/* 0xa?  Port To Vol */
    NONE,			/* N/A */
    FX_FAR_DELAY,		/* 0xc?  Note Offset */
    NONE,			/* 0xd?  Fine Tempo dn */
    NONE,			/* 0xe?  Fine Tempo up */
    FX_TEMPO			/* 0xf?  Tempo */
};


static int far_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j, vib = 0;
    struct xxm_event *event;
    struct far_header ffh;
    struct far_header2 ffh2;
    struct far_instrument fih;
    uint8 sample_map[8];

    LOAD_INIT();

    read32b(f);				/* File magic: 'FAR\xfe' */
    fread(&ffh.name, 40, 1, f);		/* Song name */
    fread(&ffh.crlf, 3, 1, f);		/* 0x0d 0x0a 0x1A */
    ffh.headersize = read16l(f);	/* Remaining header size in bytes */
    ffh.version = read8(f);		/* Version MSN=major, LSN=minor */
    fread(&ffh.ch_on, 16, 1, f);	/* Channel on/off switches */
    fseek(f, 9, SEEK_CUR);		/* Current editing values */
    ffh.tempo = read8(f);		/* Default tempo */
    fread(&ffh.pan, 16, 1, f);		/* Channel pan definitions */
    read32l(f);				/* Grid, mode (for editor) */
    ffh.textlen = read16l(f);		/* Length of embedded text */

    fseek(f, ffh.textlen, SEEK_CUR);	/* Skip song text */

    fread(&ffh2.order, 256, 1, f);	/* Orders */
    ffh2.patterns = read8(f);		/* Number of stored patterns (?) */
    ffh2.songlen = read8(f);		/* Song length in patterns */
    ffh2.restart = read8(f);		/* Restart pos */
    for (i = 0; i < 256; i++)
	ffh2.patsize[i] = read16l(f);	/* Size of each pattern in bytes */

    m->xxh->chn = 16;
    /*m->xxh->pat=ffh2.patterns; (Error in specs? --claudio) */
    m->xxh->len = ffh2.songlen;
    m->xxh->tpo = 6;
    m->xxh->bpm = 8 * 60 / ffh.tempo;
    memcpy (m->xxo, ffh2.order, m->xxh->len);

    for (m->xxh->pat = i = 0; i < 256; i++) {
	if (ffh2.patsize[i])
	    m->xxh->pat = i + 1;
    }

    m->xxh->trk = m->xxh->chn * m->xxh->pat;

    strncpy(m->name, (char *)ffh.name, 40);
    sprintf(m->type, "FAR (Farandole Composer %d.%d)",
				MSN(ffh.version), LSN(ffh.version));

    MODULE_INFO();

    PATTERN_INIT();

    /* Read and convert patterns */
    if (V(0)) {
	report("Comment bytes  : %d\n", ffh.textlen);
	report("Stored patterns: %d ", m->xxh->pat);
    }

    for (i = 0; i < m->xxh->pat; i++) {
	uint8 brk, note, ins, vol, fxb;

	PATTERN_ALLOC(i);
	if (!ffh2.patsize[i])
	    continue;
	m->xxp[i]->rows = (ffh2.patsize[i] - 2) / 64;
	TRACK_ALLOC(i);

	brk = read8(f) + 1;
	read8(f);

	for (j = 0; j < m->xxp[i]->rows * m->xxh->chn; j++) {
	    event = &EVENT(i, j % m->xxh->chn, j / m->xxh->chn);

	    if ((j % m->xxh->chn) == 0 && (j / m->xxh->chn) == brk)
		event->f2t = FX_BREAK;
	
	    note = read8(f);
	    ins = read8(f);
	    vol = read8(f);
	    fxb = read8(f);

	    if (note)
		event->note = note + 36;
	    if (event->note || ins)
		event->ins = ins + 1;

	    vol = 16 * LSN(vol) + MSN(vol);

	    if (vol)
		event->vol = vol - 0x10;	/* ? */

	    event->fxt = fx[MSN(fxb)];
	    event->fxp = LSN(fxb);

	    switch (event->fxt) {
	    case NONE:
	        event->fxt = event->fxp = 0;
		break;
	    case FX_FAR_PORTA_UP:
		event->fxt = FX_EXTENDED;
		event->fxp |= (EX_F_PORTA_UP << 4);
		break;
	    case FX_FAR_PORTA_DN:
		event->fxt = FX_EXTENDED;
		event->fxp |= (EX_F_PORTA_DN << 4);
		break;
	    case FX_FAR_RETRIG:
		event->fxt = FX_EXTENDED;
		event->fxp |= (EX_RETRIG << 4);
		break;
	    case FX_FAR_DELAY:
		event->fxt = FX_EXTENDED;
		event->fxp |= (EX_DELAY << 4);
		break;
	    case FX_FAR_SETVIBRATO:
		vib = event->fxp & 0x0f;
		event->fxt = event->fxp = 0;
		break;
	    case FX_VIBRATO:
		event->fxp = (event->fxp << 4) + vib;
		break;
	    case FX_PER_VIBRATO:
		event->fxp = (event->fxp << 4) + vib;
		break;
	    case FX_FAR_VSLIDE_UP:	/* Fine volume slide up */
		event->fxt = FX_EXTENDED;
		event->fxp |= (EX_F_VSLIDE_UP << 4);
		break;
	    case FX_FAR_VSLIDE_DN:	/* Fine volume slide down */
		event->fxt = FX_EXTENDED;
		event->fxp |= (EX_F_VSLIDE_DN << 4);
		break;
	    case FX_TEMPO:
		event->fxp = 8 * 60 / event->fxp;
		break;
	    }
	}
	reportv(ctx, 0, ".");
    }

    m->xxh->ins = -1;
    fread(sample_map, 1, 8, f);
    for (i = 0; i < 64; i++) {
	if (sample_map[i / 8] & (1 << (i % 8)))
		m->xxh->ins = i;
    }
    m->xxh->ins++;

    m->xxh->smp = m->xxh->ins;

    INSTRUMENT_INIT();

    /* Read and convert instruments and samples */
    reportv(ctx, 0, "\nInstruments    : %d ", m->xxh->ins);

    for (i = 0; i < m->xxh->ins; i++) {
	if (!(sample_map[i / 8] & (1 << (i % 8))))
		continue;

	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);

	fread(&fih.name, 32, 1, f);	/* Instrument name */
	fih.length = read32l(f);	/* Length of sample (up to 64Kb) */
	fih.finetune = read8(f);	/* Finetune (unsuported) */
	fih.volume = read8(f);		/* Volume (unsuported?) */
	fih.loop_start = read32l(f);	/* Loop start */
	fih.loopend = read32l(f);	/* Loop end */
	fih.sampletype = read8(f);	/* 1=16 bit sample */
	fih.loopmode = read8(f);

	fih.length &= 0xffff;
	fih.loop_start &= 0xffff;
	fih.loopend &= 0xffff;
	m->xxih[i].nsm = !!(m->xxs[i].len = fih.length);
	m->xxs[i].lps = fih.loop_start;
	m->xxs[i].lpe = fih.loopend;
	m->xxs[i].flg = fih.sampletype ? WAVE_16_BITS : 0;
	m->xxs[i].flg |= fih.loopmode ? WAVE_LOOPING : 0;
	m->xxi[i][0].vol = 0xff; /* fih.volume; */
	m->xxi[i][0].sid = i;

	copy_adjust(m->xxih[i].name, fih.name, 32);

	if (V(1) && (strlen((char *)m->xxih[i].name) || m->xxs[i].len) && m->xxs[i].lps != 0xffff) {
	    report ("\n[%2X] %-32.32s %04x %04x %04x %c V%02x ",
			i, m->xxih[i].name,
			m->xxs[i].len, m->xxs[i].lps, m->xxs[i].lpe,
			fih.loopmode ? 'L' : ' ', m->xxi[i][0].vol);
	    reportv(ctx, 0, ".");
	}
	xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate, 0, &m->xxs[i], NULL);
    }
    reportv(ctx, 0, "\n");

    m->volbase = 0xff;

    return 0;
}
