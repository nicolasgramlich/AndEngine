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

#define MAGIC_DskT	MAGIC4('D','s','k','T')
#define MAGIC_DskS	MAGIC4('D','s','k','S')


static int dtt_test(FILE *, char *, const int);
static int dtt_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info dtt_loader = {
	"DTT",
	"Desktop Tracker",
	dtt_test,
	dtt_load
};

static int dtt_test(FILE *f, char *t, const int start)
{
	if (read32b(f) != MAGIC_DskT)
		return -1;

	read_title(f, t, 64);

	return 0;
}

static int dtt_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	struct xxm_event *event;
	int i, j, k;
	int n;
	uint8 buf[100];
	uint32 flags;
	uint32 pofs[256];
	uint8 plen[256];
	int sdata[64];

	LOAD_INIT();

	read32b(f);

	strcpy(m->type, "Desktop Tracker");

	fread(buf, 1, 64, f);
	strncpy(m->name, (char *)buf, XMP_NAMESIZE);
	fread(buf, 1, 64, f);
	strncpy(m->author, (char *)buf, XMP_NAMESIZE);
	
	flags = read32l(f);
	m->xxh->chn = read32l(f);
	m->xxh->len = read32l(f);
	fread(buf, 1, 8, f);
	m->xxh->tpo = read32l(f);
	m->xxh->rst = read32l(f);
	m->xxh->pat = read32l(f);
	m->xxh->ins = m->xxh->smp = read32l(f);
	m->xxh->trk = m->xxh->pat * m->xxh->chn;
	
	fread(m->xxo, 1, (m->xxh->len + 3) & ~3L, f);

	MODULE_INFO();

	for (i = 0; i < m->xxh->pat; i++) {
		int x = read32l(f);
		if (i < 256)
			pofs[i] = x;
	}

	n = (m->xxh->pat + 3) & ~3L;
	for (i = 0; i < n; i++) {
		int x = read8(f);
		if (i < 256)
			plen[i] = x;
	}

	INSTRUMENT_INIT();

	/* Read instrument names */
	reportv(ctx, 1, "     Name                              Len  LBeg LEnd L Vol\n");
	for (i = 0; i < m->xxh->ins; i++) {
		int c2spd, looplen;

		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);
		read8(f);			/* note */
		m->xxi[i][0].vol = read8(f) >> 1;
		m->xxi[i][0].pan = 0x80;
		read16l(f);			/* not used */
		c2spd = read32l(f);		/* period? */
		read32l(f);			/* sustain start */
		read32l(f);			/* sustain length */
		m->xxs[i].lps = read32l(f);
		looplen = read32l(f);
		m->xxs[i].flg = looplen > 0 ? WAVE_LOOPING : 0;
		m->xxs[i].lpe = m->xxs[i].lps + looplen;
		m->xxs[i].len = read32l(f);
		fread(buf, 1, 32, f);
		copy_adjust(m->xxih[i].name, (uint8 *)buf, 32);
		sdata[i] = read32l(f);

		m->xxih[i].nsm = !!(m->xxs[i].len);
		m->xxi[i][0].sid = i;

		if (V(1) && (strlen((char*)m->xxih[i].name) || (m->xxs[i].len > 1))) {
			report("[%2X] %-32.32s  %04x %04x %04x %c V%02x\n",
				i, m->xxih[i].name, m->xxs[i].len,
				m->xxs[i].lps, m->xxs[i].lpe,
				m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ',
				m->xxi[i][0].vol, c2spd);
		}
	}

	PATTERN_INIT();

	/* Read and convert patterns */
	reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

	for (i = 0; i < m->xxh->pat; i++) {
		PATTERN_ALLOC(i);
		m->xxp[i]->rows = plen[i];
		TRACK_ALLOC(i);

		fseek(f, start + pofs[i], SEEK_SET);

		for (j = 0; j < m->xxp[i]->rows; j++) {
			for (k = 0; k < m->xxh->chn; k++) {
				uint32 x;

				event = &EVENT (i, k, j);
				x = read32l(f);

				event->ins  = (x & 0x0000003f);
				event->note = (x & 0x00000fc0) >> 6;
				event->fxt  = (x & 0x0001f000) >> 12;

				if (event->note)
					event->note += 36;

				/* sorry, we only have room for two effects */
				if (x & (0x1f << 17)) {
					event->f2p = (x & 0x003e0000) >> 17;
					x = read32l(f);
					event->fxp = (x & 0x000000ff);
					event->f2p = (x & 0x0000ff00) >> 8;
				} else {
					event->fxp = (x & 0xfc000000) >> 18;
				}
			}
		}
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	/* Read samples */
	reportv(ctx, 0, "Stored samples : %d ", m->xxh->smp);
	for (i = 0; i < m->xxh->ins; i++) {
		fseek(f, start + sdata[i], SEEK_SET);
		xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate,
				XMP_SMP_VIDC, &m->xxs[m->xxi[i][0].sid], NULL);
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	return 0;
}
