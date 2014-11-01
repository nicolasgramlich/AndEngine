/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* AMF loader written based on the format specs by Miodrag Vallat.
 *
 * The AMF format is the internal format used by DSMI, the DOS Sound and Music
 * Interface, which is the engine of DMP. As DMP was able to play more and more
 * module formats, the format evolved to support more features. There were 5
 * official formats, numbered from 10 (AMF 1.0) to 14 (AMF 1.4).
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include "period.h"


static int amf_test(FILE *, char *, const int);
static int amf_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info amf_loader = {
	"AMF",
	"DSMI Advanced Module Format",
	amf_test,
	amf_load
};

static int amf_test(FILE * f, char *t, const int start)
{
	char buf[4];
	int ver;

	if (fread(buf, 1, 3, f) < 3)
		return -1;

	if (buf[0] != 'A' || buf[1] != 'M' || buf[2] != 'F')
		return -1;

	ver = read8(f);
	if (ver < 0x0a || ver > 0x0e)
		return -1;

	read_title(f, t, 32);

	return 0;
}


static int amf_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i, j;
	struct xxm_event *event;
	uint8 buf[1024];
	int *trkmap, newtrk;
	int ver;

	LOAD_INIT();

	fread(buf, 1, 3, f);
	ver = read8(f);

	fread(buf, 1, 32, f);
	strncpy(m->name, (char *)buf, 32);
	sprintf(m->type, "DSMI %d.%d", ver / 10, ver % 10);

	m->xxh->ins = read8(f);
	m->xxh->len = read8(f);
	m->xxh->trk = read16l(f);
	m->xxh->chn = read8(f);

	m->xxh->smp = m->xxh->ins;
	m->xxh->pat = m->xxh->len;

	if (ver == 0x0a)
		fread(buf, 1, 16, f);		/* channel remap table */

	if (ver >= 0x0d) {
		fread(buf, 1, 32, f);		/* panning table */
		for (i = 0; i < 32; i++) {
			m->xxc->pan = 0x80 + 2 * (int8)buf[i];
		}
		m->xxh->bpm = read8(f);
		m->xxh->tpo = read8(f);
	} else if (ver >= 0x0b) {
		fread(buf, 1, 16, f);
	}

	MODULE_INFO();
 

	/* Orders */

	for (i = 0; i < m->xxh->len; i++)
		m->xxo[i] = i;

	reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

	m->xxp = calloc(sizeof(struct xxm_pattern *), m->xxh->pat + 1);

	for (i = 0; i < m->xxh->pat; i++) {
		PATTERN_ALLOC(i);
		m->xxp[i]->rows = ver >= 0x0e ? read16l(f) : 64;
		for (j = 0; j < m->xxh->chn; j++) {
			uint16 t = read16l(f);
			m->xxp[i]->info[j].index = t;
		}
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");


	/* Instruments */

	INSTRUMENT_INIT();

	if (V(1))
		report("     Sample name                      Len   LBeg  LEnd  L Vol C2Spd\n");

	for (i = 0; i < m->xxh->ins; i++) {
		uint8 b;
		int c2spd;

		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);

		b = read8(f);
		m->xxih[i].nsm = b ? 1 : 0;

		fread(buf, 1, 32, f);
		copy_adjust(m->xxih[i].name, buf, 32);

		fread(buf, 1, 13, f);	/* sample name */
		read32l(f);		/* sample index */

		m->xxi[i][0].sid = i;
		m->xxi[i][0].pan = 0x80;
		m->xxs[i].len = read32l(f);
		c2spd = read16l(f);
		c2spd_to_note(c2spd, &m->xxi[i][0].xpo, &m->xxi[i][0].fin);
		m->xxi[i][0].vol = read8(f);

		if (ver <= 0x0a) {
			m->xxs[i].lps = read16l(f);
			m->xxs[i].lpe = m->xxs[i].len - 1;
		} else {
			m->xxs[i].lps = read32l(f);
			m->xxs[i].lpe = read32l(f);
		}
		m->xxs[i].flg = m->xxs[i].lps > 0 ? WAVE_LOOPING : 0;

		if (V(1) && (strlen((char *)m->xxih[i].name) || m->xxs[i].len)) {
			report ("[%2X] %-32.32s %05x %05x %05x %c V%02x %5d\n",
				i, m->xxih[i].name, m->xxs[i].len, m->xxs[i].lps,
				m->xxs[i].lpe, m->xxs[i].flg & WAVE_LOOPING ?
				'L' : ' ', m->xxi[i][0].vol, c2spd);
		}
	}
				

	/* Tracks */

	trkmap = calloc(sizeof(int), m->xxh->trk);
	newtrk = 0;

	for (i = 0; i < m->xxh->trk; i++) {		/* read track table */
		uint16 t;
		t = read16l(f);
		trkmap[i] = t - 1;
		if (t > newtrk) newtrk = t;
/*printf("%d -> %d\n", i, t);*/
	}

	for (i = 0; i < m->xxh->pat; i++) {		/* read track table */
		for (j = 0; j < m->xxh->chn; j++) {
			int k = m->xxp[i]->info[j].index - 1;
			if (k < 0)		/* FIXME: what if k is 0? */
				k = 0;
			m->xxp[i]->info[j].index = trkmap[k];
		}
	}

	m->xxh->trk = newtrk;
	free(trkmap);

	reportv(ctx, 0, "Stored tracks  : %d ", m->xxh->trk);
	m->xxt = calloc (sizeof (struct xxm_track *), m->xxh->trk);

	for (i = 0; i < m->xxh->trk; i++) {
		uint8 t1, t2, t3;
		int size;

		m->xxt[i] = calloc(sizeof(struct xxm_track) +
			sizeof(struct xxm_event) * 64 - 1, 1);
		m->xxt[i]->rows = 64;

		size = read24l(f);
/*printf("TRACK %d SIZE %d\n", i, size);*/

		for (j = 0; j < size; j++) {
			t1 = read8(f);			/* row */
			t2 = read8(f);			/* type */
			t3 = read8(f);			/* parameter */
/*printf("track %d row %d: %02x %02x %02x\n", i, t1, t1, t2, t3);*/

			if (t1 == 0xff && t2 == 0xff && t3 == 0xff)
				break;

			event = &m->xxt[i]->event[t1];

			if (t2 < 0x7f) {		/* note */
				if (t2 > 12)
					event->note = t2 + 1 - 12;
				event->vol = t3;
			} else if (t2 == 0x7f) {	/* copy previous */
				memcpy(event, &m->xxt[i]->event[t1 - 1],
					sizeof(struct xxm_event));
			} else if (t2 == 0x80) {	/* instrument */
				event->ins = t3 + 1;
			} else  {			/* effects */
				uint8 fxp, fxt;

				fxp = fxt = 0;

				switch (t2) {
				case 0x81:
					fxt = FX_TEMPO;
					fxp = t3;
					break;
				case 0x82:
					/* Problem with CannonFodder2-Done.AMF
					 * main brass sound doesn't play
					 */
#if 0
					if ((int8)t3 > 0) {
						fxt = FX_VOLSLIDE;
						fxp = t3 << 4;
					} else {
						fxt = FX_VOLSLIDE;
						fxp = -(int8)t3 & 0x0f;
					}
#endif
					break;
				case 0x83:
					event->vol = t3;
					break;
				case 0x84:
					if ((int8)t3 > 0) {
						fxt = FX_PORTA_DN;
						fxp = t3;
					} else {
						fxt = FX_PORTA_UP;
						fxp = -(int8)t3;
					}
					break;
				case 0x85:
					/* porta abs -- unknown */
					break;
				case 0x86:
					fxt = FX_TONEPORTA;
					fxp = t3;
					break;
				case 0x87:
					fxt = FX_TREMOR;
					fxp = t3;
					break;
				case 0x88:
					fxt = FX_ARPEGGIO;
					fxp = t3;
					break;
				case 0x89:
					fxt = FX_VIBRATO;
					fxp = t3;
					break;
				case 0x8a:
					if ((int8)t3 > 0) {
						fxt = FX_TONE_VSLIDE;
						fxp = t3 << 4;
					} else {
						fxt = FX_TONE_VSLIDE;
						fxp = -(int8)t3 & 0x0f;
					}
					break;
				case 0x8b:
					if ((int8)t3 > 0) {
						fxt = FX_VIBRA_VSLIDE;
						fxp = t3 << 4;
					} else {
						fxt = FX_VIBRA_VSLIDE;
						fxp = -(int8)t3 & 0x0f;
					}
					break;
				case 0x8c:
					fxt = FX_BREAK;
					fxp = t3;
					break;
				case 0x8d:
					fxt = FX_JUMP;
					fxp = t3;
					break;
				case 0x8e:
					/* sync -- unknown */
					break;
				case 0x8f:
					fxt = FX_EXTENDED;
					fxp = (EX_RETRIG << 4) | (t3 & 0x0f);
					break;
				case 0x90:
					fxt = FX_OFFSET;
					fxp = t3;
					break;
				case 0x91:
					if ((int8)t3 > 0) {
						fxt = FX_EXTENDED;
						fxp = (EX_F_VSLIDE_UP << 4) |
							(t3 & 0x0f);
					} else {
						fxt = FX_EXTENDED;
						fxp = (EX_F_VSLIDE_DN << 4) |
							(t3 & 0x0f);
					}
					break;
				case 0x92:
					if ((int8)t3 > 0) {
						fxt = FX_PORTA_DN;
						fxp = 0xf0 | (fxp & 0x0f);
					} else {
						fxt = FX_PORTA_UP;
						fxp = 0xf0 | (fxp & 0x0f);
					}
					break;
				case 0x93:
					fxt = FX_EXTENDED;
					fxp = (EX_DELAY << 4) | (t3 & 0x0f);
					break;
				case 0x94:
					fxt = FX_EXTENDED;
					fxp = (EX_CUT << 4) | (t3 & 0x0f);
					break;
				case 0x95:
					fxt = FX_TEMPO;
					if (t3 < 0x21)
						t3 = 0x21;
					fxp = t3;
					break;
				case 0x96:
					if ((int8)t3 > 0) {
						fxt = FX_PORTA_DN;
						fxp = 0xe0 | (fxp & 0x0f);
					} else {
						fxt = FX_PORTA_UP;
						fxp = 0xe0 | (fxp & 0x0f);
					}
					break;
				case 0x97:
					fxt = FX_SETPAN;
					fxp = 0x80 + 2 * (int8)t3;
					break;
				}

				event->fxt = fxt;
				event->fxp = fxp;
			}

		}
		if (V(0) & !(i % 4)) report(".");
	}
	reportv(ctx, 0, "\n");


	/* Samples */

	reportv(ctx, 0, "Stored samples : %d ", m->xxh->smp);

	for (i = 0; i < m->xxh->ins; i++) {
		xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate,
			XMP_SMP_UNS, &m->xxs[m->xxi[i][0].sid], NULL);
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	m->quirk |= XMP_QRK_FINEFX;

	return 0;
}
