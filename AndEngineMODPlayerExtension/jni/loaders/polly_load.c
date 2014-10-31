/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */


/*
 * Polly Tracker is a tracker for the Commodore 64 written by Aleksi Eeben.
 * See http://aleksieeben.blogspot.com/2007/05/polly-tracker.html for more
 * information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"

static int polly_test(FILE *, char *, const int);
static int polly_load(struct xmp_context *, FILE *, const int);

struct xmp_loader_info polly_loader = {
	"POLLY",
	"Polly Tracer",
	polly_test,
	polly_load
};

#define NUM_PAT 0x1f
#define PAT_SIZE (64 * 4)
#define ORD_OFS (NUM_PAT * PAT_SIZE)
#define SMP_OFS (NUM_PAT * PAT_SIZE + 256)


static void decode_rle(uint8 *out, FILE *f, int size)
{
	int i;

	for (i = 0; i < size; ) {
		int x = read8(f);

		if (feof(f))
			return;

		if (x == 0xae) {
			int n,v;
			switch (n = read8(f)) {
			case 0x01:
				out[i++] = 0xae;
				break;
			default:
				v = read8(f);
				while (n-- && i < size)
					out[i++] = v;
			}
		} else {
			out[i++] = x;
		}
	}
}

static int polly_test(FILE *f, char *t, const int start)
{
	int i;
	uint8 *buf;

	if (read8(f) != 0xae)
		return -1;

	if ((buf = malloc(0x10000)) == NULL)
		return -1;

	decode_rle(buf, f, 0x10000);

	for (i = 0; i < 128; i++) {
		if (buf[ORD_OFS + i] != 0 && buf[ORD_OFS] < 0xe0) {
			free(buf);
			return -1;
		}
	}

	if (t)
		memcpy(t, buf + ORD_OFS + 160, 16);

	free(buf);

	return 0;
}

static int polly_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	struct xxm_event *event;
	uint8 *buf;
	int i, j, k;

	LOAD_INIT();

	read8(f);			/* skip 0xae */
	/*
	 * File is RLE-encoded, escape is 0xAE (Aleksi Eeben's initials).
	 * Actual 0xAE is encoded as 0xAE 0x01
	 */
	if ((buf = calloc(1, 0x10000)) == NULL)
		return -1;

	decode_rle(buf, f, 0x10000);

	for (i = 0; buf[ORD_OFS + i] != 0 && i < 128; i++)
		m->xxo[i] = buf[ORD_OFS + i] - 0xe0;
	m->xxh->len = i;

	memcpy(m->name, buf + ORD_OFS + 160, 16);
	memcpy(m->author, buf + ORD_OFS + 176, 16);
	sprintf(m->type, "Polly Tracker");
	MODULE_INFO();

	m->xxh->tpo = 0x03;
	m->xxh->bpm = 0x7d * buf[ORD_OFS + 193] / 0x88;
#if 0
	for (i = 0; i < 1024; i++) {
		if ((i % 16) == 0) printf("\n");
		printf("%02x ", buf[ORD_OFS + i]);
	}
#endif

	m->xxh->pat = 0;
	for (i = 0; i < m->xxh->len; i++) {
		if (m->xxo[i] > m->xxh->pat)
			m->xxh->pat = m->xxo[i];
	}
	m->xxh->pat++;
	
	m->xxh->chn = 4;
	m->xxh->trk = m->xxh->pat * m->xxh->chn;

	PATTERN_INIT();

	reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

	for (i = 0; i < m->xxh->pat; i++) {
		PATTERN_ALLOC(i);
		m->xxp[i]->rows = 64;
		TRACK_ALLOC(i);

		for (j = 0; j < 64; j++) {
			for (k = 0; k < 4; k++) {
				uint8 x = buf[i * PAT_SIZE + j * 4 + k];
				event = &EVENT(i, k, j);
				if (x == 0xf0) {
					event->fxt = FX_BREAK;
					event->fxp = 0;
					continue;
				}
				event->note = LSN(x);
				if (event->note)
					event->note += 36;
				event->ins = MSN(x);
			}
		}
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");


	m->xxh->ins = m->xxh->smp = 15;
	INSTRUMENT_INIT();

	reportv(ctx, 1, "     Len  LBeg LEnd L Vol\n");

	for (i = 0; i < 15; i++) {
		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);
		m->xxs[i].len = buf[ORD_OFS + 129 + i] < 0x10 ? 0 :
					256 * buf[ORD_OFS + 145 + i];
		m->xxi[i][0].fin = 0;
		m->xxi[i][0].vol = 0x40;
		m->xxs[i].lps = 0;
		m->xxs[i].lpe = 0;
		m->xxs[i].flg = 0;
		m->xxi[i][0].pan = 0x80;
		m->xxi[i][0].sid = i;
		m->xxih[i].nsm = !!(m->xxs[i].len);
		m->xxih[i].rls = 0xfff;

		if (V(1) && m->xxs[i].len > 0) {
                	report("[%2X] %04x %04x %04x %c V%02x\n",
                       		i, m->xxs[i].len, m->xxs[i].lps,
                        	m->xxs[i].lpe, ' ', m->xxi[i][0].vol);
		}
	}

	/* Convert samples from 6 to 8 bits */
	for (i = SMP_OFS; i < 0x10000; i++)
		buf[i] = buf[i] << 2;

	/* Read samples */
	reportv(ctx, 0, "Loading samples: %d ", m->xxh->ins);

	for (i = 0; i < m->xxh->ins; i++) {
		if (m->xxs[i].len == 0)
			continue;
		xmp_drv_loadpatch(ctx, NULL, m->xxi[i][0].sid, m->c4rate,
				XMP_SMP_NOLOAD | XMP_SMP_UNS,
				&m->xxs[m->xxi[i][0].sid],
				(char*)buf + ORD_OFS + 256 +
					256 * (buf[ORD_OFS + 129 + i] - 0x10));
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	free(buf);

	/* make it mono */
	for (i = 0; i < m->xxh->chn; i++)
		m->xxc[i].pan = 0x80;

	m->xxh->flg |= XXM_FLG_MODRNG;

	return 0;
}
