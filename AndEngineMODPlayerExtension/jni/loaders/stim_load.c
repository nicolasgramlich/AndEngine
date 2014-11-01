/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Loader for Slamtilt modules based on the format description
 * written by Sylvain Chipaux (Asle/ReDoX). Get the Slamtilt demo
 * from game/demo in Aminet.
 */

/* Tested with the Slamtilt modules sent by Sipos Attila */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"

#define MAGIC_STIM	MAGIC4('S','T','I','M')

static int stim_test(FILE *, char *, const int);
static int stim_load(struct xmp_context *, FILE *, const int);

struct xmp_loader_info stim_loader = {
	"STIM",
	"Slamtilt",
	stim_test,
	stim_load
};

static int stim_test(FILE *f, char *t, const int start)
{
	if (read32b(f) != MAGIC_STIM)
		return -1;

	read_title(f, t, 0);

	return 0;
}

struct stim_instrument {
	uint16 size;		/* Lenght of the sample (/2) */
	uint8 finetune;		/* Finetune (as ptk) */
	uint8 volume;		/* Volume (as ptk) */
	uint16 loop_start;	/* Loop start (/2) */
	uint16 loop_size;	/* Loop lenght (/2) */
};

struct stim_header {
	uint32 id;		/* "STIM" ID string */
	uint32 smpaddr;		/* Address of the sample descriptions */
	uint32 unknown[2];
	uint16 nos;		/* Number of samples (?) */
	uint16 len;		/* Size of pattern list */
	uint16 pat;		/* Number of patterns saved */
	uint8 order[128];	/* Pattern list */
	uint32 pataddr[64];	/* Pattern addresses (add 0xc) */
};

static int stim_load(struct xmp_context *ctx, FILE * f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i, j, k;
	struct xxm_event *event;
	struct stim_header sh;
	struct stim_instrument si;
	uint8 b1, b2, b3;

	LOAD_INIT();

	sh.id = read32b(f);
	sh.smpaddr = read32b(f);
	read32b(f);
	read32b(f);
	sh.nos = read16b(f);
	sh.len = read16b(f);
	sh.pat = read16b(f);
	fread(&sh.order, 128, 1, f);
	for (i = 0; i < 64; i++)
		sh.pataddr[i] = read32b(f) + 0x0c;

	m->xxh->len = sh.len;
	m->xxh->pat = sh.pat;
	m->xxh->ins = sh.nos;
	m->xxh->smp = m->xxh->ins;
	m->xxh->trk = m->xxh->pat * m->xxh->chn;

	for (i = 0; i < m->xxh->len; i++)
		m->xxo[i] = sh.order[i];

	sprintf(m->type, "STIM (Slamtilt)");

	MODULE_INFO();

	PATTERN_INIT();

	/* Load and convert patterns */
	reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

	for (i = 0; i < m->xxh->pat; i++) {
		PATTERN_ALLOC(i);
		m->xxp[i]->rows = 64;
		TRACK_ALLOC(i);

		fseek(f, start + sh.pataddr[i] + 8, SEEK_SET);

		for (j = 0; j < 4; j++) {
			for (k = 0; k < 64; k++) {
				event = &EVENT(i, j, k);
				b1 = read8(f);

				if (b1 & 0x80) {
					k += b1 & 0x7f;
					continue;
				}

				/* STIM event format:
				 *
				 *     __ Fx __
				 *    /        \
				 *   ||        ||
				 *  0000 0000  0000 0000  0000 0000
				 *  |  |    |    |     |  |       |
				 *  |   \  /      \   /    \     /
				 *  |    smp      note      Fx Val
				 *  |
				 *  Description bit set to 0.
				 */

				b2 = read8(f);
				b3 = read8(f);

				if ((event->note = b2 & 0x3f) != 0)
					event->note += 35;
				event->ins = b1 & 0x1f;
				event->fxt = ((b2 >> 4) & 0x0c) | (b1 >> 5);
				event->fxp = b3;

				disable_continue_fx(event);
			}
		}
		reportv(ctx, 0, ".");
	}

	INSTRUMENT_INIT();

	reportv(ctx, 0, "\nStored samples : %d ", m->xxh->smp);

	fseek(f, start + sh.smpaddr + m->xxh->smp * 4, SEEK_SET);

	for (i = 0; i < m->xxh->smp; i++) {
		si.size = read16b(f);
		si.finetune = read8(f);
		si.volume = read8(f);
		si.loop_start = read16b(f);
		si.loop_size = read16b(f);

		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);
		m->xxs[i].len = 2 * si.size;
		m->xxs[i].lps = 2 * si.loop_start;
		m->xxs[i].lpe = m->xxs[i].lps + 2 * si.loop_size;
		m->xxs[i].flg = si.loop_size > 1 ? WAVE_LOOPING : 0;
		m->xxi[i][0].fin = (int8) (si.finetune << 4);
		m->xxi[i][0].vol = si.volume;
		m->xxi[i][0].pan = 0x80;
		m->xxi[i][0].sid = i;
		m->xxih[i].nsm = !!(m->xxs[i].len);
		m->xxih[i].rls = 0xfff;

		if (V(1) && m->xxs[i].len > 2) {
			report("\n[%2X] %04x %04x %04x %c V%02x %+d ",
			       i, m->xxs[i].len, m->xxs[i].lps,
			       m->xxs[i].lpe, si.loop_size > 1 ? 'L' : ' ',
			       m->xxi[i][0].vol, m->xxi[i][0].fin >> 4);
		}

		if (!m->xxs[i].len)
			continue;
		xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate, 0,
				  &m->xxs[m->xxi[i][0].sid], NULL);
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	m->xxh->flg |= XXM_FLG_MODRNG;

	return 0;
}
