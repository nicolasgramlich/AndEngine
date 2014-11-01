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

#include <assert.h>

#include "load.h"
#include "iff.h"
#include "period.h"

#define MAGIC_D_T_	MAGIC4('D','.','T','.')


static int dt_test(FILE *, char *, const int);
static int dt_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info dt_loader = {
	"DTM",
	"Digital Tracker",
	dt_test,
	dt_load
};

static int dt_test(FILE *f, char *t, const int start)
{
	if (read32b(f) != MAGIC_D_T_)
		return -1;

	read_title(f, t, 0);

	return 0;
}


static int pflag, sflag;
static int realpat;


static void get_d_t_(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int b;

	read16b(f);			/* type */
	read16b(f);			/* 0xff then mono */
	read16b(f);			/* reserved */
	m->xxh->tpo = read16b(f);
	if ((b = read16b(f)) > 0)	/* RAMBO.DTM has bpm 0 */
		m->xxh->bpm = b;
	read32b(f);			/* undocumented */

	fread(m->name, 32, 1, f);
	strcpy(m->type, "DTM (Digital Tracker)");

	MODULE_INFO();
}

static void get_s_q_(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i, maxpat;

	m->xxh->len = read16b(f);
	m->xxh->rst = read16b(f);
	read32b(f);	/* reserved */

	for (maxpat = i = 0; i < 128; i++) {
		m->xxo[i] = read8(f);
		if (m->xxo[i] > maxpat)
			maxpat = m->xxo[i];
	}
	m->xxh->pat = maxpat + 1;
}

static void get_patt(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;

	m->xxh->chn = read16b(f);
	realpat = read16b(f);
	m->xxh->trk = m->xxh->chn * m->xxh->pat;
}

static void get_inst(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i, c2spd;
	uint8 name[30];

	m->xxh->ins = m->xxh->smp = read16b(f);
	reportv(ctx, 0, "Instruments    : %d ", m->xxh->ins);

	INSTRUMENT_INIT();

	reportv(ctx, 1, "\n     Instrument name        Len   LBeg  LSize LS Res Vol Fine C2Spd");
	for (i = 0; i < m->xxh->ins; i++) {
		int fine, replen, flag;

		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);

		read32b(f);		/* reserved */
		m->xxs[i].len = read32b(f);
		m->xxih[i].nsm = !!m->xxs[i].len;
		fine = read8s(f);	/* finetune */
		m->xxi[i][0].vol = read8(f);
		m->xxi[i][0].pan = 0x80;
		m->xxs[i].lps = read32b(f);
		replen = read32b(f);
		m->xxs[i].lpe = m->xxs[i].lps + replen - 1;
		m->xxs[i].flg = replen > 2 ?  WAVE_LOOPING : 0;

		fread(name, 22, 1, f);
		copy_adjust(m->xxih[i].name, name, 22);

		flag = read16b(f);	/* bit 0-7:resol 8:stereo */
		m->xxs[i].flg |= (flag & 0xff) > 8 ? WAVE_16_BITS : 0;

		read32b(f);		/* midi note (0x00300000) */
		c2spd = read32b(f);	/* frequency */
		c2spd_to_note(c2spd, &m->xxi[i][0].xpo, &m->xxi[i][0].fin);

		/* It's strange that we have both c2spd and finetune */
		m->xxi[i][0].fin += fine;

		m->xxi[i][0].sid = i;

		if (strlen((char *)m->xxih[i].name) || m->xxs[i].len > 0) {
			if (V(1))
				report("\n[%2X] %-22.22s %05x%c%05x %05x %c%c %2db V%02x F%+03d %5d",
					i, m->xxih[i].name,
					m->xxs[i].len,
					m->xxs[i].flg & WAVE_16_BITS ? '+' : ' ',
					m->xxs[i].lps,
					replen,
					m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ',
					flag & 0x100 ? 'S' : ' ',
					flag & 0xff,
					m->xxi[i][0].vol,
					fine,
					c2spd);
			else
				report(".");
		}
	}
	reportv(ctx, 0, "\n");
}

static void get_dapt(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int pat, i, j, k;
	struct xxm_event *event;
	static int last_pat;
	int rows;

	if (!pflag) {
		reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);
		pflag = 1;
		last_pat = 0;
		PATTERN_INIT();
	}

	read32b(f);	/* 0xffffffff */
	i = pat = read16b(f);
	rows = read16b(f);

	for (i = last_pat; i <= pat; i++) {
		PATTERN_ALLOC(i);
		m->xxp[i]->rows = rows;
		TRACK_ALLOC(i);
	}
	last_pat = pat + 1;

	for (j = 0; j < rows; j++) {
		for (k = 0; k < m->xxh->chn; k++) {
			uint8 a, b, c, d;

			event = &EVENT(pat, k, j);
			a = read8(f);
			b = read8(f);
			c = read8(f);
			d = read8(f);
			if (a) {
				a--;
				event->note = 12 * (a >> 4) + (a & 0x0f);
			}
			event->vol = (b & 0xfc) >> 2;
			event->ins = ((b & 0x03) << 4) + (c >> 4);
			event->fxt = c & 0xf;
			event->fxp = d;
		}
	}

	reportv(ctx, 0, ".");
}

static void get_dait(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	static int i = 0;

	if (!sflag) {
		reportv(ctx, 0, "\nStored samples : %d ", m->xxh->smp);
		sflag = 1;
		i = 0;
	}

	if (size > 2) {
		xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate,
				XMP_SMP_BIGEND, &m->xxs[m->xxi[i][0].sid], NULL);
		reportv(ctx, 0, ".");
	}

	i++;
}

static int dt_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;

	LOAD_INIT();

	pflag = sflag = 0;
	
	/* IFF chunk IDs */
	iff_register("D.T.", get_d_t_);
	iff_register("S.Q.", get_s_q_);
	iff_register("PATT", get_patt);
	iff_register("INST", get_inst);
	iff_register("DAPT", get_dapt);
	iff_register("DAIT", get_dait);

	/* Load IFF chunks */
	while (!feof(f))
		iff_chunk(ctx, f);

	reportv(ctx, 0, "\n");

	iff_release();

	return 0;
}

