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
#include "iff.h"

#define MAGIC_MUSX	MAGIC4('M','U','S','X')
#define MAGIC_MNAM	MAGIC4('M','N','A','M')


static int arch_test (FILE *, char *, const int);
static int arch_load (struct xmp_context *, FILE *, const int);


struct xmp_loader_info arch_loader = {
	"MUSX",
	"Archimedes Tracker",
	arch_test,
	arch_load
};

#if 0
static uint8 convert_vol(uint8 vol) {
/*	return pow(2,6.0-(255.0-vol)/32)+.5; */
	return vol_table[vol];
}
#endif

static int arch_test(FILE *f, char *t, const int start)
{
	if (read32b(f) != MAGIC_MUSX)
		return -1;

	read32l(f);

	while (!feof(f)) {
		uint32 id = read32b(f);
		uint32 len = read32l(f);

		if (id == MAGIC_MNAM) {
			read_title(f, t, 32);
			return 0;
		}

		fseek(f, len, SEEK_CUR);
	}

	read_title(f, t, 0);

	return 0;
}


static int year, month, day;
static int pflag, sflag, max_ins;
static uint8 ster[8], rows[64];

static void fix_effect(struct xxm_event *e)
{
	switch (e->fxt) {
	case 0x00:			/* 00 xy Normal play or Arpeggio */
		e->fxt = FX_ARPEGGIO;
		/* x: first halfnote to add
		   y: second halftone to subtract */
		break;
	case 0x01:			/* 01 xx Slide Up */
		e->fxt = FX_PORTA_UP;
		break;
	case 0x02:			/* 02 xx Slide Down */
		e->fxt = FX_PORTA_DN;
		break;
	case 0x0b:			/* 0B xx Break Pattern */
		e->fxt = FX_BREAK;
		break;
	case 0x0e:			/* 0E xy Set Stereo */
		e->fxt = e->fxp = 0;
		/* y: stereo position (1-7,ignored). 1=left 4=center 7=right */
		break;
	case 0x10:			/* 10 xx Volume Slide Up */
		e->fxt = FX_VOLSLIDE_UP;
		break;
	case 0x11:			/* 11 xx Volume Slide Down */
		e->fxt = FX_VOLSLIDE_DN;
		break;
	case 0x13:			/* 13 xx Position Jump */
		e->fxt = FX_JUMP;
		break;
	case 0x15:			/* 15 xy Line Jump. (not in manual) */
		e->fxt = e->fxp = 0;
		/* Jump to line 10*x+y in same pattern. (10*x+y>63 ignored) */
		break;
	case 0x1c:			/* 1C xy Set Speed */
		e->fxt = FX_TEMPO;
		break;
	case 0x1f:			/* 1F xx Set Volume */
		e->fxt = FX_VOLSET;
		/* all volumes are logarithmic */
		/* e->fxp = convert_vol (e->fxp); */
		break;
	default:
		e->fxt = e->fxp = 0;
	}
}

static void get_tinf(struct xmp_context *ctx, int size, FILE *f)
{
	int x;

	x = read8(f);
	year = ((x & 0xf0) >> 4) * 10 + (x & 0x0f);
	x = read8(f);
	year += ((x & 0xf0) >> 4) * 1000 + (x & 0x0f) * 100;

	x = read8(f);
	month = ((x & 0xf0) >> 4) * 10 + (x & 0x0f);

	x = read8(f);
	day = ((x & 0xf0) >> 4) * 10 + (x & 0x0f);
}

static void get_mvox(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;

	m->xxh->chn = read32l(f);
}

static void get_ster(struct xmp_context *ctx, int size, FILE *f)
{
	fread(ster, 1, 8, f);
}

static void get_mnam(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;

	fread(m->name, 1, 32, f);
}

static void get_anam(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;

	fread(m->author, 1, 32, f);
}

static void get_mlen(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;

	m->xxh->len = read32l(f);
}

static void get_pnum(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;

	m->xxh->pat = read32l(f);
}

static void get_plen(struct xmp_context *ctx, int size, FILE *f)
{
	fread(rows, 1, 64, f);
}

static void get_sequ(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;

	fread(m->xxo, 1, 128, f);

	strcpy(m->type, "MUSX (Archimedes Tracker)");

	MODULE_INFO();
	reportv(ctx, 0, "Creation date  : %02d/%02d/%04d\n", day, month, year);
}

static void get_patt(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	static int i = 0;
	int j, k;
	struct xxm_event *event;

	if (!pflag) {
		reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);
		pflag = 1;
		i = 0;
		m->xxh->trk = m->xxh->pat * m->xxh->chn;
		PATTERN_INIT();
	}

	PATTERN_ALLOC(i);
	m->xxp[i]->rows = rows[i];
	TRACK_ALLOC(i);

	for (j = 0; j < rows[i]; j++) {
		for (k = 0; k < m->xxh->chn; k++) {
			event = &EVENT(i, k, j);

			event->fxp = read8(f);
			event->fxt = read8(f);
			event->ins = read8(f);
			event->note = read8(f);

			if (event->note)
				event->note += 36;

			fix_effect(event);
		}
	}

	i++;
	reportv(ctx, 0, ".");
}

static void get_samp(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	static int i = 0;

	if (!sflag) {
		m->xxh->smp = m->xxh->ins = 36;
		INSTRUMENT_INIT();
		reportv(ctx, 0, "\nInstruments    : %d ", m->xxh->ins);
	        reportv(ctx, 1, "\n     Instrument name      Len   LBeg  LEnd  L Vol");
		sflag = 1;
		max_ins = 0;
		i = 0;
	}

	m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);
	read32l(f);	/* SNAM */
	read32l(f);
	fread(m->xxih[i].name, 1, 20, f);
	read32l(f);	/* SVOL */
	read32l(f);
	/* m->xxi[i][0].vol = convert_vol(read32l(f)); */
	m->xxi[i][0].vol = read32l(f);
	read32l(f);	/* SLEN */
	read32l(f);
	m->xxs[i].len = read32l(f);
	read32l(f);	/* ROFS */
	read32l(f);
	m->xxs[i].lps = read32l(f);
	read32l(f);	/* RLEN */
	read32l(f);
	m->xxs[i].lpe = read32l(f);

	read32l(f);	/* SDAT */
	read32l(f);
	read32l(f);	/* 0x00000000 */

	m->xxih[i].nsm = 1;
	m->xxi[i][0].sid = i;
	m->xxi[i][0].pan = 0x80;

	m->vol_table = arch_vol_table;
	m->volbase = 0xff;

	if (m->xxs[i].lpe > 2) {
		m->xxs[i].flg = WAVE_LOOPING;
		m->xxs[i].lpe = m->xxs[i].lps + m->xxs[i].lpe;
	} else if (m->xxs[i].lpe == 2 && m->xxs[i].lps > 0) {
		/* non-zero repeat offset and repeat length of 2
		 * means loop to end of sample */
		m->xxs[i].flg = WAVE_LOOPING;
		m->xxs[i].lpe = m->xxs[i].len;
	}

	xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate, XMP_SMP_VIDC,
					&m->xxs[m->xxi[i][0].sid], NULL);

	if (strlen((char *)m->xxih[i].name) || m->xxs[i].len > 0) {
		if (V(1))
			report("\n[%2X] %-20.20s %05x %05x %05x %c V%02x",
				i, m->xxih[i].name,
				m->xxs[i].len,
				m->xxs[i].lps,
				m->xxs[i].lpe,
				m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ',
				m->xxi[i][0].vol);
		else
			reportv(ctx, 0, ".");
	}

	i++;
	max_ins++;
}

static int arch_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i;

	LOAD_INIT();

	read32b(f);	/* MUSX */
	read32b(f);

	pflag = sflag = 0;

	/* IFF chunk IDs */
	iff_register("TINF", get_tinf);
	iff_register("MVOX", get_mvox);
	iff_register("STER", get_ster);
	iff_register("MNAM", get_mnam);
	iff_register("ANAM", get_anam);
	iff_register("MLEN", get_mlen);
	iff_register("PNUM", get_pnum);
	iff_register("PLEN", get_plen);
	iff_register("SEQU", get_sequ);
	iff_register("PATT", get_patt);
	iff_register("SAMP", get_samp);

	iff_setflag(IFF_LITTLE_ENDIAN);

	/* Load IFF chunks */
	while (!feof(f))
		iff_chunk(ctx, f);

	reportv(ctx, 0, "\n");

	iff_release();

	for (i = 0; i < m->xxh->chn; i++)
		m->xxc[i].pan = (((i + 3) / 2) % 2) * 0xff;

	return 0;
}

