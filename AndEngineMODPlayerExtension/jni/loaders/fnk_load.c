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

#include <sys/types.h>
#include <sys/stat.h>
#include "load.h"

#define MAGIC_Funk	MAGIC4('F','u','n','k')


static int fnk_test (FILE *, char *, const int);
static int fnk_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info fnk_loader = {
    "FNK",
    "Funktracker",
    fnk_test,
    fnk_load
};

static int fnk_test(FILE *f, char *t, const int start)
{
    uint8 a, b;
    int size;
    struct stat st;

    if (read32b(f) != MAGIC_Funk)
	return -1;

    read8(f); 
    a = read8(f);
    b = read8(f); 
    read8(f); 

    if ((a >> 1) < 10)			/* creation year (-1980) */
	return -1;

    if (MSN(b) > 7 || LSN(b) > 9)	/* CPU and card */
	return -1;

    size = read32l(f);
    if (size < 1024)
	return -1;

    fstat(fileno(f), &st);
    if (size != st.st_size)
	return -1;

    read_title(f, t, 0);

    return 0;
}


struct fnk_instrument {
    uint8 name[19];		/* ASCIIZ instrument name */
    uint32 loop_start;		/* Instrument loop start */
    uint32 length;		/* Instrument length */
    uint8 volume;		/* Volume (0-255) */
    uint8 pan;			/* Pan (0-255) */
    uint8 shifter;		/* Portamento and offset shift */
    uint8 waveform;		/* Vibrato and tremolo waveforms */
    uint8 retrig;		/* Retrig and arpeggio speed */
};

struct fnk_header {
    uint8 marker[4];		/* 'Funk' */
    uint8 info[4];		/* */
    uint32 filesize;		/* File size */
    uint8 fmt[4];		/* F2xx, Fkxx or Fvxx */
    uint8 loop;			/* Loop order number */
    uint8 order[256];		/* Order list */
    uint8 pbrk[128];		/* Break list for patterns */
    struct fnk_instrument fih[64];	/* Instruments */
};


static int fnk_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j;
    int day, month, year;
    struct xxm_event *event;
    struct fnk_header ffh;
    uint8 ev[3];

    LOAD_INIT();

    fread(&ffh.marker, 4, 1, f);
    fread(&ffh.info, 4, 1, f);
    ffh.filesize = read32l(f);
    fread(&ffh.fmt, 4, 1, f);
    ffh.loop = read8(f);
    fread(&ffh.order, 256, 1, f);
    fread(&ffh.pbrk, 128, 1, f);

    for (i = 0; i < 64; i++) {
	fread(&ffh.fih[i].name, 19, 1, f);
	ffh.fih[i].loop_start = read32l(f);
	ffh.fih[i].length = read32l(f);
	ffh.fih[i].volume = read8(f);
	ffh.fih[i].pan = read8(f);
	ffh.fih[i].shifter = read8(f);
	ffh.fih[i].waveform = read8(f);
	ffh.fih[i].retrig = read8(f);
    }

    day = ffh.info[0] & 0x1f;
    month = ((ffh.info[1] & 0x01) << 3) | ((ffh.info[0] & 0xe0) >> 5);
    year = 1980 + ((ffh.info[1] & 0xfe) >> 1);

    m->xxh->smp = m->xxh->ins = 64;

    for (i = 0; i < 256 && ffh.order[i] != 0xff; i++) {
	if (ffh.order[i] > m->xxh->pat)
	    m->xxh->pat = ffh.order[i];
    }
    m->xxh->pat++;

    m->xxh->len = i;
    memcpy (m->xxo, ffh.order, m->xxh->len);

    m->xxh->tpo = 4;
    m->xxh->bpm = 125;
    m->xxh->chn = 0;

    /*
     * If an R1 fmt (funktype = Fk** or Fv**), then ignore byte 3. It's
     * unreliable. It used to store the (GUS) sample memory requirement.
     */
    if (ffh.fmt[0] == 'F' && ffh.fmt[1] == '2') {
	if (((int8)ffh.info[3] >> 1) & 0x40)
	    m->xxh->bpm -= (ffh.info[3] >> 1) & 0x3f;
	else
	    m->xxh->bpm += (ffh.info[3] >> 1) & 0x3f;

	strcpy(m->type, "FNK R2 (FunktrackerGOLD)");
    } else if (ffh.fmt[0] == 'F' && (ffh.fmt[1] == 'v' || ffh.fmt[1] == 'k')) {
	strcpy(m->type, "FNK R1 (Funktracker)");
    } else {
	m->xxh->chn = 8;
	strcpy(m->type, "FNK R0 (Funktracker DOS32)");
    }

    if (m->xxh->chn == 0) {
	m->xxh->chn = (ffh.fmt[2] < '0') || (ffh.fmt[2] > '9') ||
		(ffh.fmt[3] < '0') || (ffh.fmt[3] > '9') ? 8 :
		(ffh.fmt[2] - '0') * 10 + ffh.fmt[3] - '0';
    }

    m->xxh->bpm = 4 * m->xxh->bpm / 5;
    m->xxh->trk = m->xxh->chn * m->xxh->pat;
    /* FNK allows mode per instrument but we don't, so use linear like 669 */
    m->xxh->flg |= XXM_FLG_LINEAR;

    MODULE_INFO();

    reportv(ctx, 0, "Creation date  : %02d/%02d/%04d\n", day, month, year);

    INSTRUMENT_INIT();

    /* Convert instruments */
    for (i = 0; i < m->xxh->ins; i++) {
	m->xxi[i] = calloc(sizeof (struct xxm_instrument), 1);
	m->xxih[i].nsm = !!(m->xxs[i].len = ffh.fih[i].length);
	m->xxs[i].lps = ffh.fih[i].loop_start;
	if (m->xxs[i].lps == -1)
	    m->xxs[i].lps = 0;
	m->xxs[i].lpe = ffh.fih[i].length;
	m->xxs[i].flg = ffh.fih[i].loop_start != -1 ? WAVE_LOOPING : 0;
	m->xxi[i][0].vol = ffh.fih[i].volume;
	m->xxi[i][0].pan = ffh.fih[i].pan;
	m->xxi[i][0].sid = i;

	copy_adjust(m->xxih[i].name, ffh.fih[i].name, 19);

	if ((V(1)) && (strlen((char *)m->xxih[i].name) || m->xxs[i].len > 2))
	    report ("[%2X] %-20.20s %04x %04x %04x %c V%02x P%02x\n", i,
		m->xxih[i].name,
		m->xxs[i].len, m->xxs[i].lps, m->xxs[i].lpe,
		m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ',
		m->xxi[i][0].vol, m->xxi[i][0].pan);
    }

    PATTERN_INIT();

    /* Read and convert patterns */
    reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	m->xxp[i]->rows = 64;
	TRACK_ALLOC (i);

	EVENT(i, 1, ffh.pbrk[i]).f2t = FX_BREAK;

	for (j = 0; j < 64 * m->xxh->chn; j++) {
	    event = &EVENT(i, j % m->xxh->chn, j / m->xxh->chn);
	    fread(&ev, 1, 3, f);

	    switch (ev[0] >> 2) {
	    case 0x3f:
	    case 0x3e:
	    case 0x3d:
		break;
	    default:
		event->note = 25 + (ev[0] >> 2);
		event->ins = 1 + MSN(ev[1]) + ((ev[0] & 0x03) << 4);
		event->vol = ffh.fih[event->ins - 1].volume;
	    }

	    switch (LSN(ev[1])) {
	    case 0x00:
		event->fxt = FX_PER_PORTA_UP;
		event->fxp = ev[2];
		break;
	    case 0x01:
		event->fxt = FX_PER_PORTA_DN;
		event->fxp = ev[2];
		break;
	    case 0x02:
		event->fxt = FX_PER_TPORTA;
		event->fxp = ev[2];
		break;
	    case 0x03:
		event->fxt = FX_PER_VIBRATO;
		event->fxp = ev[2];
		break;
	    case 0x06:
		event->fxt = FX_PER_VSLD_UP;
		event->fxp = ev[2] << 1;
		break;
	    case 0x07:
		event->fxt = FX_PER_VSLD_DN;
		event->fxp = ev[2] << 1;
		break;
	    case 0x0b:
		event->fxt = FX_ARPEGGIO;
		event->fxp = ev[2];
		break;
	    case 0x0d:
		event->fxt = FX_VOLSET;
		event->fxp = ev[2];
		break;
	    case 0x0e:
		if (ev[2] == 0x0a || ev[2] == 0x0b || ev[2] == 0x0c) {
		    event->fxt = FX_PER_CANCEL;
		    break;
		}

		switch (MSN(ev[2])) {
		case 0x1:
		    event->fxt = FX_EXTENDED;
		    event->fxp = (EX_CUT << 4) | LSN(ev[2]);
		    break;
		case 0x2:
		    event->fxt = FX_EXTENDED;
		    event->fxp = (EX_DELAY << 4) | LSN(ev[2]);
		    break;
		case 0xd:
		    event->fxt = FX_EXTENDED;
		    event->fxp = (EX_RETRIG << 4) | LSN(ev[2]);
		    break;
		case 0xe:
		    event->fxt = FX_SETPAN;
		    event->fxp = 8 + (LSN(ev[2]) << 4);	
		    break;
		case 0xf:
		    event->fxt = FX_TEMPO;
		    event->fxp = LSN(ev[2]);	
		    break;
		}
	    }
	}
	reportv(ctx, 0, ".");
    }

    /* Read samples */
    reportv(ctx, 0, "\nStored samples : %d ", m->xxh->smp);

    for (i = 0; i < m->xxh->ins; i++) {
	if (m->xxs[i].len <= 2)
	    continue;

	xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate, 0,
							&m->xxs[i], NULL);

	reportv(ctx, 0, ".");
    }

    reportv(ctx, 0, "\n");

    for (i = 0; i < m->xxh->chn; i++)
	m->xxc[i].pan = 0x80;

    m->volbase = 0xff;
    m->quirk = XMP_QRK_VSALL;

    return 0;
}
