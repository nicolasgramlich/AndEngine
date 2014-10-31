/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/*
 * Based on the GDM (General Digital Music) version 1.0 File Format
 * Specification - Revision 2 by MenTaLguY
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <assert.h>
#include "load.h"
#include "period.h"

#define MAGIC_GDM	MAGIC4('G','D','M',0xfe)
#define MAGIC_GMFS	MAGIC4('G','M','F','S')

static char *fmt[] = {
	"?",
	"MOD",
	"MTM",
	"S3M",
	"669",
	"FAR",
	"ULT",
	"STM",
	"MED",
	"unknown"
};

static int gdm_test(FILE *, char *, const int);
static int gdm_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info gdm_loader = {
	"GDM",
	"Generic Digital Music",
	gdm_test,
	gdm_load
};

static int gdm_test(FILE *f, char *t, const int start)
{
	if (read32b(f) != MAGIC_GDM)
		return -1;

	fseek(f, start + 0x47, SEEK_SET);
	if (read32b(f) != MAGIC_GMFS)
		return -1;

	fseek(f, start + 4, SEEK_SET);
	read_title(f, t, 32);

	return 0;
}



void fix_effect(uint8 *fxt, uint8 *fxp)
{
	switch (*fxt) {
	case 0x00:			/* no effect */
		*fxp = 0;
		break;
	case 0x01:
	case 0x02:
	case 0x03:
	case 0x04:
	case 0x05:
	case 0x06:
	case 0x07:			/* same as protracker */
		break;
	case 0x08:
		*fxt = FX_TREMOR;
		break;
	case 0x09:
	case 0x0a:
	case 0x0b:
	case 0x0c:
	case 0x0d:
	case 0x0e:
	case 0x0f:			/* same as protracker */
		break;
	case 0x10:			/* arpeggio */
		*fxt = FX_ARPEGGIO;
		break;
	case 0x11:			/* set internal flag */
		*fxt = *fxp = 0;
		break;
	case 0x12:
		*fxt = FX_MULTI_RETRIG;
		break;
	case 0x13:
		*fxt = FX_GLOBALVOL;
		break;
	case 0x14:
		*fxt = FX_FINE4_VIBRA;
		break;
	case 0x1e:			/* special misc */
		*fxt = *fxp = 0;
		break;
	case 0x1f:
		*fxt = FX_S3M_BPM;
		break;
	default:
		*fxt = *fxp = 0;
	}
}


static int gdm_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	struct xxm_event *event;
	int vermaj, vermin, tvmaj, tvmin, tracker;
	int origfmt, ord_ofs, pat_ofs, ins_ofs, smp_ofs;
	uint8 buffer[32], panmap[32];
	int i;

	LOAD_INIT();

	read32b(f);		/* skip magic */
	fread(m->name, 1, 32, f);
	fread(m->author, 1, 32, f);

	fseek(f, 7, SEEK_CUR);

	vermaj = read8(f);
	vermin = read8(f);
	tracker = read16l(f);
	tvmaj = read8(f);
	tvmin = read8(f);

	if (tracker == 0) {
		sprintf(m->type, "GDM %d.%02d, (2GDM %d.%02d)",
					vermaj, vermin, tvmaj, tvmin);
	} else {
		sprintf(m->type, "GDM %d.%02d (unknown tracker %d.%02d)",
					vermaj, vermin, tvmaj, tvmin);
	}

	fread(panmap, 32, 1, f);
	for (i = 0; i < 32; i++) {
		if (panmap[i] != 0xff)
			m->xxh->chn = i + 1;
		if (panmap[i] == 16)
			panmap[i] = 8;
		m->xxc[i].pan = 0x80 + (panmap[i] - 8) * 16;
	}

	m->xxh->gvl = read8(f);
	m->xxh->tpo = read8(f);
	m->xxh->bpm = read8(f);
	origfmt = read16l(f);
	ord_ofs = read32l(f);
	m->xxh->len = read8(f) + 1;
	pat_ofs = read32l(f);
	m->xxh->pat = read8(f) + 1;
	ins_ofs = read32l(f);
	smp_ofs = read32l(f);
	m->xxh->ins = m->xxh->smp = read8(f) + 1;
	m->xxh->trk = m->xxh->pat * m->xxh->chn;
	
	MODULE_INFO();

	if (origfmt > 9)
		origfmt = 9;
	reportv(ctx, 0, "Orig format    : %s\n", fmt[origfmt]);

	fseek(f, start + ord_ofs, SEEK_SET);

	for (i = 0; i < m->xxh->len; i++)
		m->xxo[i] = read8(f);

	/* Read instrument data */

	fseek(f, start + ins_ofs, SEEK_SET);

	INSTRUMENT_INIT();

	reportv(ctx, 1, "     Name                             Len   LBeg  LEnd  L Vol Pan C4Spd\n");

	for (i = 0; i < m->xxh->ins; i++) {
		int flg, c4spd, vol, pan;

		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);
		fread(buffer, 32, 1, f);
		copy_adjust(m->xxih[i].name, buffer, 32);
		fseek(f, 12, SEEK_CUR);		/* skip filename */
		read8(f);			/* skip EMS handle */
		m->xxs[i].len = read32l(f);
		m->xxs[i].lps = read32l(f);
		m->xxs[i].lpe = read32l(f);
		flg = read8(f);
		c4spd = read16l(f);
		vol = read8(f);
		pan = read8(f);
		
		m->xxi[i][0].vol = vol > 0x40 ? 0x40 : vol;
		m->xxi[i][0].pan = pan > 15 ? 0x80 : 0x80 + (pan - 8) * 16;
		c2spd_to_note(c4spd, &m->xxi[i][0].xpo, &m->xxi[i][0].fin);

		m->xxih[i].nsm = !!(m->xxs[i].len);
		m->xxi[i][0].sid = i;
		m->xxs[i].flg = 0;

		if (flg & 0x01)
			m->xxs[i].flg |= WAVE_LOOPING;
		if (flg & 0x02)
			m->xxs[i].flg |= WAVE_16_BITS;

		if (V(1) && (strlen((char*)m->xxih[i].name) || (m->xxs[i].len > 1))) {
			report("[%2X] %-32.32s %05x%c%05x %05x %c V%02x P%02x %5d\n",
				i, m->xxih[i].name,
				m->xxs[i].len,
				m->xxs[i].flg & WAVE_16_BITS ? '+' : ' ',
				m->xxs[i].lps,
				m->xxs[i].lpe,
				m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ',
				m->xxi[i][0].vol,
				m->xxi[i][0].pan,
				c4spd);
		}
	}

	/* Read and convert patterns */

	fseek(f, start + pat_ofs, SEEK_SET);

	PATTERN_INIT();

	reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

	for (i = 0; i < m->xxh->pat; i++) {
		int len, c, r, k;

		PATTERN_ALLOC(i);
		m->xxp[i]->rows = 64;
		TRACK_ALLOC(i);

		len = read16l(f);
		len -= 2;

		for (r = 0; len > 0; ) {
			c = read8(f);
			len--;

			if (c == 0) {
				r++;
				continue;
			}

			assert((c & 0x1f) < m->xxh->chn);
			event = &EVENT (i, c & 0x1f, r);

			if (c & 0x20) {		/* note and sample follows */
				k = read8(f);
				event->note = 12 * MSN(k & 0x7f) + LSN(k);
				event->ins = read8(f);
				len -= 2;
			}

			if (c & 0x40) {		/* effect(s) follow */
				do {
					k = read8(f);
					len--;
					switch ((k & 0xc0) >> 6) {
					case 0:
						event->fxt = k & 0x1f;
						event->fxp = read8(f);
						len--;
						fix_effect(&event->fxt, &event->fxp);
						break;
					case 1:
						event->f2t = k & 0x1f;
						event->f2p = read8(f);
						len--;
						fix_effect(&event->f2t, &event->f2p);
						break;
					case 2:
						read8(f);
						len--;
					}
				} while (k & 0x20);
			}
		}
		
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	/* Read samples */

	fseek(f, start + smp_ofs, SEEK_SET);

	reportv(ctx, 0, "Stored samples : %d ", m->xxh->smp);
	for (i = 0; i < m->xxh->ins; i++) {
		xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate,
				XMP_SMP_UNS, &m->xxs[m->xxi[i][0].sid], NULL);
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	return 0;
}
