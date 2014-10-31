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
#include "period.h"


static int gtk_test(FILE *, char *, const int);
static int gtk_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info gtk_loader = {
	"GTK",
	"Graoumf Tracker",
	gtk_test,
	gtk_load
};

static int gtk_test(FILE * f, char *t, const int start)
{
	char buf[4];

	if (fread(buf, 1, 4, f) < 4)
		return -1;

	if (memcmp(buf, "GTK", 3) || buf[3] > 4)
		return -1;

	read_title(f, t, 32);

	return 0;
}

static int gtk_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	struct xxm_event *event;
	int i, j, k;
	uint8 buffer[40];
	int rows, bits, c2spd, size;
	int ver, patmax;

	LOAD_INIT();

	fread(buffer, 4, 1, f);
	ver = buffer[3];
	fread(m->name, 32, 1, f);
	sprintf(m->type, "GTK v%d (Graoumf Tracker)", ver);
	fseek(f, 160, SEEK_CUR);	/* skip comments */

	m->xxh->ins = read16b(f);
	m->xxh->smp = m->xxh->ins;
	rows = read16b(f);
	m->xxh->chn = read16b(f);
	m->xxh->len = read16b(f);
	m->xxh->rst = read16b(f);

	MODULE_INFO();

	reportv(ctx, 0, "Instruments    : %d ", m->xxh->ins);
	reportv(ctx, 1, "\n     Name                          Len   LBeg  LSiz  L Vol Fin  C2spd");

	INSTRUMENT_INIT();
	for (i = 0; i < m->xxh->ins; i++) {
		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);
		fread(buffer, 28, 1, f);
		copy_adjust(m->xxih[i].name, buffer, 28);

		if (ver == 1) {
			read32b(f);
			m->xxs[i].len = read32b(f);
			m->xxs[i].lps = read32b(f);
			size = read32b(f);
			m->xxs[i].lpe = m->xxs[i].lps + size - 1;
			read16b(f);
			read16b(f);
			m->xxi[i][0].vol = 0x40;
			m->xxi[i][0].pan = 0x80;
			bits = 1;
			c2spd = 8363;
		} else {
			fseek(f, 14, SEEK_CUR);
			read16b(f);		/* autobal */
			bits = read16b(f);	/* 1 = 8 bits, 2 = 16 bits */
			c2spd = read16b(f);
			c2spd_to_note(c2spd, &m->xxi[i][0].xpo, &m->xxi[i][0].fin);
			m->xxs[i].len = read32b(f);
			m->xxs[i].lps = read32b(f);
			size = read32b(f);
			m->xxs[i].lpe = m->xxs[i].lps + size - 1;
			m->xxi[i][0].vol = read16b(f) / 4;
			read8(f);
			m->xxi[i][0].fin = read8s(f);
		}

		m->xxih[i].nsm = !!m->xxs[i].len;
		m->xxi[i][0].sid = i;
		m->xxs[i].flg = size > 2 ? WAVE_LOOPING : 0;
		m->xxs[i].flg |= bits > 1 ? WAVE_16_BITS : 0;

		if (strlen((char*)m->xxih[i].name) || (m->xxs[i].len > 1)) {
			if (V(1)) {
				report("\n[%2X] %-28.28s  %05x%c%05x %05x %c "
						"V%02x F%+03d %5d", i,
			 		m->xxih[i].name,
					m->xxs[i].len,
					bits > 1 ? '+' : ' ',
					m->xxs[i].lps,
					size,
					m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ',
					m->xxi[i][0].vol, m->xxi[i][0].fin,
					c2spd);
			} else {
				report(".");
			}
		}
	}
	reportv(ctx, 0, "\n");

	for (i = 0; i < 256; i++)
		m->xxo[i] = read16b(f);

	for (patmax = i = 0; i < m->xxh->len; i++) {
		if (m->xxo[i] > patmax)
			patmax = m->xxo[i];
	}

	m->xxh->pat = patmax + 1;
	m->xxh->trk = m->xxh->pat * m->xxh->chn;

	PATTERN_INIT();

	/* Read and convert patterns */
	reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

	for (i = 0; i < m->xxh->pat; i++) {
		PATTERN_ALLOC(i);
		m->xxp[i]->rows = rows;
		TRACK_ALLOC(i);

		for (j = 0; j < m->xxp[i]->rows; j++) {
			for (k = 0; k < m->xxh->chn; k++) {
				event = &EVENT (i, k, j);

				event->note = read8(f);
				event->ins = read8(f);
				event->fxt = read8(f);
				event->fxp = read8(f);
				if (ver >= 4) {
					event->vol = read8(f);
				}

				/* Ignore extended effects */
				if (event->fxt > 0x0f || event->fxt == 0x0e ||
						event->fxt == 0x0c) {
					event->fxt = 0;
					event->fxp = 0;
				}
			}
		}
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	/* Read samples */
	reportv(ctx, 0, "Stored samples : %d ", m->xxh->smp);
	for (i = 0; i < m->xxh->ins; i++) {
		if (m->xxs[i].len == 0)
			continue;
		xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate, 0,
						&m->xxs[m->xxi[i][0].sid], NULL);
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	return 0;
}
