/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * Pattern unpacking code by Teijo Kinnunen, 1990
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/*
 * MED 2.00 is in Fish disk #349 and has a couple of demo modules, get it
 * from ftp://ftp.funet.fi/pub/amiga/fish/301-400/ff349
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <assert.h>
#include "load.h"

#define MAGIC_MED3	MAGIC4('M','E','D',3)


static int med3_test(FILE *, char *, const int);
static int med3_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info med3_loader = {
	"MED3",
	"MED 2.00",
	med3_test,
	med3_load
};

static int med3_test(FILE *f, char *t, const int start)
{
	if (read32b(f) !=  MAGIC_MED3)
		return -1;

	read_title(f, t, 0);

	return 0;
}


#define MASK		0x80000000

#define M0F_LINEMSK0F	0x01
#define M0F_LINEMSK1F	0x02
#define M0F_FXMSK0F	0x04
#define M0F_FXMSK1F	0x08
#define M0F_LINEMSK00	0x10
#define M0F_LINEMSK10	0x20
#define M0F_FXMSK00	0x40
#define M0F_FXMSK10	0x80



/*
 * From the MED 2.00 file loading/saving routines by Teijo Kinnunen, 1990
 */

static uint8 get_nibble(uint8 *mem, uint16 *nbnum)
{
	uint8 *mloc = mem + (*nbnum / 2),res;

	if(*nbnum & 0x1)
		res = *mloc & 0x0f;
	else
		res = *mloc >> 4;
	(*nbnum)++;

	return res;
}

static uint16 get_nibbles(uint8 *mem,uint16 *nbnum,uint8 nbs)
{
	uint16 res = 0;

	while (nbs--) {
		res <<= 4;
		res |= get_nibble(mem,nbnum);
	}

	return res;
}

static void unpack_block(struct xmp_context *ctx, uint16 bnum, uint8 *from)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	struct xxm_event *event;
	uint32 linemsk0 = *((uint32 *)from), linemsk1 = *((uint32 *)from + 1);
	uint32 fxmsk0 = *((uint32 *)from + 2), fxmsk1 = *((uint32 *)from + 3);
	uint32 *lmptr = &linemsk0, *fxptr = &fxmsk0;
	uint16 fromn = 0, lmsk;
	uint8 *fromst = from + 16, bcnt, *tmpto;
	uint8 *patbuf, *to;
	int i, j, trkn = m->xxh->chn;

	from += 16;
	patbuf = to = calloc(3, 4 * 64);
	assert(to);

	for (i = 0; i < 64; i++) {
		if (i == 32) {
			lmptr = &linemsk1;
			fxptr = &fxmsk1;
		}

		if (*lmptr & MASK) {
			lmsk = get_nibbles(fromst, &fromn, (uint8)(trkn / 4));
			lmsk <<= (16 - trkn);
			tmpto = to;

			for (bcnt = 0; bcnt < trkn; bcnt++) {
				if (lmsk & 0x8000) {
					*tmpto = (uint8)get_nibbles(fromst,
						&fromn,2);
					*(tmpto + 1) = (get_nibble(fromst,
							&fromn) << 4);
				}
				lmsk <<= 1;
				tmpto += 3;
			}
		}

		if (*fxptr & MASK) {
			lmsk = get_nibbles(fromst,&fromn,(uint8)(trkn / 4));
			lmsk <<= (16 - trkn);
			tmpto = to;

			for (bcnt = 0; bcnt < trkn; bcnt++) {
				if (lmsk & 0x8000) {
					*(tmpto+1) |= get_nibble(fromst,
							&fromn);
					*(tmpto+2) = (uint8)get_nibbles(fromst,
							&fromn,2);
				}
				lmsk <<= 1;
				tmpto += 3;
			}
		}
		to += 3 * trkn;
		*lmptr <<= 1;
		*fxptr <<= 1;
	}

	for (i = 0; i < 64; i++) {
		for (j = 0; j < 4; j++) {
			event = &EVENT(bnum, j, i);

			event->note = patbuf[i * 12 + j * 3 + 0];
			if (event->note)
				event->note += 36;
			event->ins  = patbuf[i * 12 + j * 3 + 1] >> 4;
			if (event->ins)
				event->ins++;
			event->fxt  = patbuf[i * 12 + j * 3 + 1] & 0x0f;
			event->fxp  = patbuf[i * 12 + j * 3 + 2];

			switch (event->fxt) {
			case 0x00:	/* arpeggio */
			case 0x01:	/* slide up */
			case 0x02:	/* slide down */
				break;
			case 0x03:	/* vibrato */
				event->fxt = FX_VIBRATO;
				break;
			case 0x0c:	/* set volume (BCD) */
				event->fxp = MSN(event->fxp) * 10 +
							LSN(event->fxp);
				break;
			case 0x0d:	/* volume slides */
				event->fxt = FX_VOLSLIDE;
				break;
			case 0x0f:	/* tempo/break */
				if (event->fxp == 0)
					event->fxt = FX_BREAK;
				if (event->fxp == 0xff) {
					event->fxp = event->fxt = 0;
					event->vol = 1;
				} else if (event->fxp == 0xfe) {
					event->fxp = event->fxt = 0;
				} else if (event->fxp == 0xf1) {
					event->fxt = FX_EXTENDED;
					event->fxp = (EX_RETRIG << 4) | 3;
				} else if (event->fxp == 0xf2) {
					event->fxt = FX_EXTENDED;
					event->fxp = (EX_CUT << 4) | 3;
				} else if (event->fxp == 0xf3) {
					event->fxt = FX_EXTENDED;
					event->fxp = (EX_DELAY << 4) | 3;
				} else if (event->fxp > 10) {
					event->fxt = FX_S3M_BPM;
					event->fxp = 125 * event->fxp / 33;
				}
				break;
			default:
				event->fxp = event->fxt = 0;
			}
		}
	}

	free(patbuf);
}


static int med3_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i, j;
	uint32 mask;
	int transp, sliding;

	LOAD_INIT();

	read32b(f);

	strcpy(m->type, "MED3 (MED 2.00)");

	m->xxh->ins = m->xxh->smp = 32;
	INSTRUMENT_INIT();

	/* read instrument names */
	for (i = 0; i < 32; i++) {
		uint8 c, buf[40];
		for (j = 0; j < 40; j++) {
			c = read8(f);
			buf[j] = c;
			if (c == 0)
				break;
		}
		copy_adjust(m->xxih[i].name, buf, 32);
		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);
	}

	/* read instrument volumes */
	mask = read32b(f);
	for (i = 0; i < 32; i++, mask <<= 1) {
		m->xxi[i][0].vol = mask & MASK ? read8(f) : 0;
		m->xxi[i][0].pan = 0x80;
		m->xxi[i][0].fin = 0;
		m->xxi[i][0].sid = i;
	}

	/* read instrument loops */
	mask = read32b(f);
	for (i = 0; i < 32; i++, mask <<= 1) {
		m->xxs[i].lps = mask & MASK ? read16b(f) : 0;
	}

	/* read instrument loop length */
	mask = read32b(f);
	for (i = 0; i < 32; i++, mask <<= 1) {
		uint32 lsiz = mask & MASK ? read16b(f) : 0;
		m->xxs[i].len = m->xxs[i].lps + lsiz;
		m->xxs[i].lpe = m->xxs[i].lps + lsiz;
		m->xxs[i].flg = lsiz > 1 ? WAVE_LOOPING : 0;
	}

	m->xxh->chn = 4;
	m->xxh->pat = read16b(f);
	m->xxh->trk = m->xxh->chn * m->xxh->pat;

	m->xxh->len = read16b(f);
	fread(m->xxo, 1, m->xxh->len, f);
	m->xxh->tpo = read16b(f);
	if (m->xxh->tpo > 10) {
		m->xxh->bpm = 125 * m->xxh->tpo / 33;
		m->xxh->tpo = 6;
	}
	transp = read8s(f);
	read8(f);			/* flags */
	sliding = read16b(f);		/* sliding */
	read32b(f);			/* jumping mask */
	fseek(f, 16, SEEK_CUR);		/* rgb */

	/* read midi channels */
	mask = read32b(f);
	for (i = 0; i < 32; i++, mask <<= 1) {
		if (mask & MASK)
			read8(f);
	}

	/* read midi programs */
	mask = read32b(f);
	for (i = 0; i < 32; i++, mask <<= 1) {
		if (mask & MASK)
			read8(f);
	}
	
	MODULE_INFO();

	reportv(ctx, 0, "Sliding        : %d\n", sliding);
	reportv(ctx, 0, "Play transpose : %d semitones\n", transp);

	if (sliding == 6)
		m->quirk |= XMP_QRK_VSALL | XMP_QRK_PBALL;

	for (i = 0; i < 32; i++)
		m->xxi[i][0].xpo = transp;

	PATTERN_INIT();

	/* Load and convert patterns */
	reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

	for (i = 0; i < m->xxh->pat; i++) {
		uint32 *conv;
		uint8 b, tracks;
		uint16 convsz;

		PATTERN_ALLOC(i);
		m->xxp[i]->rows = 64;
		TRACK_ALLOC(i);

		tracks = read8(f);

		b = read8(f);
		convsz = read16b(f);
		conv = calloc(1, convsz + 16);
		assert(conv);

                if (b & M0F_LINEMSK00)
			*conv = 0L;
                else if (b & M0F_LINEMSK0F)
			*conv = 0xffffffff;
                else
			*conv = read32b(f);

                if (b & M0F_LINEMSK10)
			*(conv + 1) = 0L;
                else if (b & M0F_LINEMSK1F)
			*(conv + 1) = 0xffffffff;
                else
			*(conv + 1) = read32b(f);

                if (b & M0F_FXMSK00)
			*(conv + 2) = 0L;
                else if (b & M0F_FXMSK0F)
			*(conv + 2) = 0xffffffff;
                else
			*(conv + 2) = read32b(f);

                if (b & M0F_FXMSK10)
			*(conv + 3) = 0L;
                else if (b & M0F_FXMSK1F)
			*(conv + 3) = 0xffffffff;
                else
			*(conv + 3) = read32b(f);

		fread(conv + 4, 1, convsz, f);

                unpack_block(ctx, i, (uint8 *)conv);

		free(conv);

		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	/* Load samples */

	reportv(ctx, 0, "Instruments    : %d ", m->xxh->ins);
	reportv(ctx, 1, "\n     Instrument name                  Len  LBeg LEnd L Vol");

	mask = read32b(f);
	for (i = 0; i < 32; i++, mask <<= 1) {
		if (~mask & MASK)
			continue;

		m->xxs[i].len = read32b(f);
		if (read16b(f))		/* type */
			continue;

		m->xxih[i].nsm = !!(m->xxs[i].len);

		reportv(ctx, 1, "\n[%2X] %-32.32s %04x %04x %04x %c V%02x ",
			i, m->xxih[i].name, m->xxs[i].len, m->xxs[i].lps,
			m->xxs[i].lpe,
			m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ',
			m->xxi[i][0].vol);

		xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate, 0,
				  &m->xxs[m->xxi[i][0].sid], NULL);
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	return 0;
}
