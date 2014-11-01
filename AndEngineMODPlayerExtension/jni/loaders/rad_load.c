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

static int rad_test(FILE *, char *, const int);
static int rad_load(struct xmp_context *, FILE *, const int);

struct xmp_loader_info rad_loader = {
	"RAD",
	"Reality Adlib Tracker",
	rad_test,
	rad_load
};

static int rad_test(FILE *f, char *t, const int start)
{
	char buf[16];

	if (fread(buf, 1, 16, f) < 16)
		return -1;

	if (memcmp(buf, "RAD by REALiTY!!", 16))
		return -1;

	read_title(f, t, 0);

	return 0;
}

struct rad_instrument {
	uint8 num;		/* Number of instruments that follows */
	uint8 reg[11];		/* Adlib registers */
};


static int rad_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	struct xxm_event *event;
	int i, j;
	uint8 sid[11];
	uint16 ppat[32];
	uint8 b, r, c;
	uint8 version;		/* Version in BCD */
	uint8 flags;		/* bit 7=descr,6=slow-time,4..0=tempo */

	LOAD_INIT();

	fseek(f, 16, SEEK_SET);		/* skip magic */
	version = read8(f);
	flags = read8(f);

	m->xxh->chn = 9;
	m->xxh->bpm = 125;
	m->xxh->tpo = flags & 0x1f;
	m->xxh->flg = XXM_FLG_LINEAR;
	/* FIXME: tempo setting in RAD modules */
	if (m->xxh->tpo <= 2)
		m->xxh->tpo = 6;
	m->xxh->smp = 0;

	sprintf(m->type, "RAD %d.%d (Reality Adlib Tracker)",
				MSN(version), LSN(version));

	MODULE_INFO();

	/* Read description */
	if (flags & 0x80) {
		reportv(ctx, 1, "|");
		while ((b = read8(f)) != 0) {
			if (V(1)) {
				if (b == 1) {
					report("\n|");
				} else if (b < 0x20) {
					for (i = 0; i < b; i++)
						report(" ");
				} else if (b < 0x80) {
					report("%c", b);
				} else {
					report(".");
				}
			}
		}
		reportv(ctx, 1, "\n");
	}

	reportv(ctx, 1,
"               Modulator                       Carrier             Common\n"
"     Char Fr LS OL At De Su Re WS   Char Fr LS OL At De Su Re WS   Fbk Alg\n"
		);

	/* Read instruments */
	m->xxh->ins = 0;

	while ((b = read8(f)) != 0) {
		m->xxh->ins = b;

		fread(sid, 1, 11, f);
		xmp_cvt_hsc2sbi((char *)sid);
		if (V(1)) {
			report("[%2X] ", b - 1);

			report("%c%c%c%c %2d ",
			       sid[0] & 0x80 ? 'a' : '-',
			       sid[0] & 0x40 ? 'v' : '-',
			       sid[0] & 0x20 ? 's' : '-',
			       sid[0] & 0x10 ? 'e' : '-', sid[0] & 0x0f);
			report("%2d %2d ", sid[2] >> 6, sid[2] & 0x3f);
			report("%2d %2d ", sid[4] >> 4, sid[4] & 0x0f);
			report("%2d %2d ", sid[6] >> 4, sid[6] & 0x0f);
			report("%2d   ", sid[8]);

			report("%c%c%c%c %2d ",
			       sid[1] & 0x80 ? 'a' : '-',
			       sid[1] & 0x40 ? 'v' : '-',
			       sid[1] & 0x20 ? 's' : '-',
			       sid[1] & 0x10 ? 'e' : '-', sid[1] & 0x0f);
			report("%2d %2d ", sid[3] >> 6, sid[3] & 0x3f);
			report("%2d %2d ", sid[5] >> 4, sid[5] & 0x0f);
			report("%2d %2d ", sid[7] >> 4, sid[7] & 0x0f);
			report("%2d   ", sid[9]);

			report("%2d  %2d\n", sid[10] >> 1, sid[10] & 0x01);
		}
		xmp_drv_loadpatch(ctx, f, b - 1, 0, 0, NULL, (char *)sid);
	}

	INSTRUMENT_INIT();

	for (i = 0; i < m->xxh->ins; i++) {
		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);
		m->xxih[i].nsm = 1;
		m->xxi[i][0].vol = 0x40;
		m->xxi[i][0].pan = 0x80;
		m->xxi[i][0].xpo = -1;
		m->xxi[i][0].sid = i;
	}

	/* Read orders */
	m->xxh->len = read8(f);

	for (j = i = 0; i < m->xxh->len; i++) {
		b = read8(f);
		if (b < 0x80)
			m->xxo[j++] = b;
	}

	/* Read pattern pointers */
	for (m->xxh->pat = i = 0; i < 32; i++) {
		ppat[i] = read16l(f);
		if (ppat[i])
			m->xxh->pat++;
	}
	m->xxh->trk = m->xxh->pat * m->xxh->chn;

	if (V(0)) {
		report("Module length  : %d patterns\n", m->xxh->len);
		report("Instruments    : %d\n", m->xxh->ins);
		report("Stored patterns: %d ", m->xxh->pat);
	}
	PATTERN_INIT();

	/* Read and convert patterns */
	for (i = 0; i < m->xxh->pat; i++) {
		PATTERN_ALLOC(i);
		m->xxp[i]->rows = 64;
		TRACK_ALLOC(i);

		if (ppat[i] == 0)
			continue;

		fseek(f, start + ppat[i], SEEK_SET);

		do {
			r = read8(f);		/* Row number */

			if ((r & 0x7f) >= 64)
				report("** Whoops! row = %d\n", r);

			do {
				c = read8(f);	/* Channel number */

				if ((c & 0x7f) >= m->xxh->chn)
					report("** Whoops! channel = %d\n", c);

				event = &EVENT(i, c & 0x7f, r & 0x7f);

				b = read8(f);	/* Note + octave + inst */
				event->ins = (b & 0x80) >> 3;
				event->note = LSN(b);

				if (event->note == 15)
					event->note = XMP_KEY_OFF;
				else if (event->note)
					event->note += 14 +
						12 * ((b & 0x70) >> 4);

				b = read8(f);	/* Instrument + effect */
				event->ins |= MSN(b);
				event->fxt = LSN(b);
				if (event->fxt) {
					b = read8(f);	/* Effect parameter */
					event->fxp = b;

					/* FIXME: tempo setting */
					if (event->fxt == 0x0f
					    && event->fxp <= 2)
						event->fxp = 6;
				}
			} while (~c & 0x80);
		} while (~r & 0x80);

		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	for (i = 0; i < m->xxh->chn; i++) {
		m->xxc[i].pan = 0x80;
		m->xxc[i].flg = XXM_CHANNEL_FM;
	}

	return 0;
}
