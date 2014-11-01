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

#include <unistd.h>
#include <limits.h>
#include "load.h"
#include "iff.h"
#include "period.h"

/* Galaxy Music System 4.0 module file loader
 *
 * Based on modules converted using mod2j2b.exe
 */

static int gal4_test(FILE *, char *, const int);
static int gal4_load(struct xmp_context *, FILE *, const int);

struct xmp_loader_info gal4_loader = {
	"GAL4",
	"Galaxy Music System 4.0",
	gal4_test,
	gal4_load
};

static int gal4_test(FILE *f, char *t, const int start)
{
        if (read32b(f) != MAGIC4('R', 'I', 'F', 'F'))
		return -1;

	read32b(f);

	if (read32b(f) != MAGIC4('A', 'M', 'F', 'F'))
		return -1;

	if (read32b(f) != MAGIC4('M', 'A', 'I', 'N'))
		return -1;

	read_title(f, t, 0);

	return 0;
}

static int snum;

static void get_main(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	char buf[64];
	int flags;
	
	fread(buf, 1, 64, f);
	strncpy(m->name, buf, 64);
	strcpy(m->type, "Galaxy Music System 4.0");

	flags = read8(f);
	if (~flags & 0x01)
		m->xxh->flg = XXM_FLG_LINEAR;
	m->xxh->chn = read8(f);
	m->xxh->tpo = read8(f);
	m->xxh->bpm = read8(f);
	read16l(f);		/* unknown - 0x01c5 */
	read16l(f);		/* unknown - 0xff00 */
	read8(f);		/* unknown - 0x80 */
}

static void get_ordr(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i;

	m->xxh->len = read8(f);

	for (i = 0; i < m->xxh->len; i++)
		m->xxo[i] = read8(f);
}

static void get_patt_cnt(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i;

	i = read8(f) + 1;		/* pattern number */

	if (i > m->xxh->pat)
		m->xxh->pat = i;
}

static void get_inst_cnt(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i;

	read8(f);			/* 00 */
	i = read8(f) + 1;		/* instrument number */
	
	if (i > m->xxh->ins)
		m->xxh->ins = i;

	fseek(f, 28, SEEK_CUR);		/* skip name */

	m->xxh->smp += read8(f);
}

static void get_patt(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	struct xxm_event *event, dummy;
	int i, len, chan;
	int rows, r;
	uint8 flag;
	
	i = read8(f);	/* pattern number */
	len = read32l(f);
	
	rows = read8(f) + 1;

	PATTERN_ALLOC(i);
	m->xxp[i]->rows = rows;
	TRACK_ALLOC(i);

	for (r = 0; r < rows; ) {
		if ((flag = read8(f)) == 0) {
			r++;
			continue;
		}

		chan = flag & 0x1f;

		event = chan < m->xxh->chn ? &EVENT(i, chan, r) : &dummy;

		if (flag & 0x80) {
			uint8 fxp = read8(f);
			uint8 fxt = read8(f);

			switch (fxt) {
			case 0x14:		/* speed */
				fxt = FX_S3M_TEMPO;
				break;
			default:
				if (fxt > 0x0f) {
					printf("unknown effect %02x %02x\n", fxt, fxp);
					fxt = fxp = 0;
				}
			}

			event->fxt = fxt;
			event->fxp = fxp;
		}

		if (flag & 0x40) {
			event->ins = read8(f);
			event->note = read8(f);

			if (event->note == 128) {
				event->note = XMP_KEY_OFF;
			} else if (event->note > 12) {
				event->note -= 12;
			} else {
				event->note = 0;
			}
		}

		if (flag & 0x20) {
			event->vol = 1 + read8(f) / 2;
		}
	}
}

static void get_inst(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i, j;
	int srate, finetune, flags;
	int val, vwf, vra, vde, vsw, fade;
	uint8 buf[30];

	read8(f);		/* 00 */
	i = read8(f);		/* instrument number */

	if (V(1) && i == 0) {
	    report("\n     Instrument name                  Smp Len   LBeg  LEnd  L Vol Pan C2Spd");
	}

	fread(&m->xxih[i].name, 1, 28, f);
	str_adj((char *)m->xxih[i].name);

	m->xxih[i].nsm = read8(f);
	fseek(f, 12, SEEK_CUR);		/* Sample map - 1st octave */
	fread(&m->xxim[i].ins, 1, 96, f);

	fseek(f, 11, SEEK_CUR);		/* unknown */
	vwf = read8(f);			/* vibrato waveform */
	vsw = read8(f);			/* vibrato sweep */
	read8(f);			/* unknown */
	read8(f);			/* unknown */
	vde = read8(f) / 4;		/* vibrato depth */
	vra = read16l(f) / 16;		/* vibrato speed */
	read8(f);			/* unknown */

	val = read8(f);			/* PV envelopes flags */
	if (LSN(val) & 0x01)
		m->xxih[i].aei.flg |= XXM_ENV_ON;
	if (LSN(val) & 0x02)
		m->xxih[i].aei.flg |= XXM_ENV_SUS;
	if (LSN(val) & 0x04)
		m->xxih[i].aei.flg |= XXM_ENV_LOOP;
	if (MSN(val) & 0x01)
		m->xxih[i].pei.flg |= XXM_ENV_ON;
	if (MSN(val) & 0x02)
		m->xxih[i].pei.flg |= XXM_ENV_SUS;
	if (MSN(val) & 0x04)
		m->xxih[i].pei.flg |= XXM_ENV_LOOP;

	val = read8(f);			/* PV envelopes points */
	m->xxih[i].aei.npt = LSN(val) + 1;
	m->xxih[i].pei.npt = MSN(val) + 1;

	val = read8(f);			/* PV envelopes sustain point */
	m->xxih[i].aei.sus = LSN(val);
	m->xxih[i].pei.sus = MSN(val);

	val = read8(f);			/* PV envelopes loop start */
	m->xxih[i].aei.lps = LSN(val);
	m->xxih[i].pei.lps = MSN(val);

	read8(f);			/* PV envelopes loop end */
	m->xxih[i].aei.lpe = LSN(val);
	m->xxih[i].pei.lpe = MSN(val);

	if (m->xxih[i].aei.npt)
		m->xxae[i] = calloc(4, m->xxih[i].aei.npt);
	else
		m->xxih[i].aei.flg &= ~XXM_ENV_ON;

	if (m->xxih[i].pei.npt)
		m->xxpe[i] = calloc(4, m->xxih[i].pei.npt);
	else
		m->xxih[i].pei.flg &= ~XXM_ENV_ON;

	fread(buf, 1, 30, f);		/* volume envelope points */;
	for (j = 0; j < m->xxih[i].aei.npt; j++) {
		m->xxae[i][j * 2] = readmem16l(buf + j * 3) / 16;
		m->xxae[i][j * 2 + 1] = buf[j * 3 + 2];
	}

	fread(buf, 1, 30, f);		/* pan envelope points */;
	for (j = 0; j < m->xxih[i].pei.npt; j++) {
		m->xxpe[i][j * 2] = readmem16l(buf + j * 3) / 16;
		m->xxpe[i][j * 2 + 1] = buf[j * 3 + 2];
	}

	fade = read8(f);		/* fadeout - 0x80->0x02 0x310->0x0c */
	read8(f);			/* unknown */

	reportv(ctx, 1, "\n[%2X] %-28.28s  %2d ",
			i, m->xxih[i].name, m->xxih[i].nsm);

	if (m->xxih[i].nsm == 0)
		return;

	m->xxi[i] = calloc(sizeof(struct xxm_instrument), m->xxih[i].nsm);

	for (j = 0; j < m->xxih[i].nsm; j++, snum++) {
		read32b(f);	/* SAMP */
		read32b(f);	/* size */
	
		fread(&m->xxs[snum].name, 1, 28, f);
		str_adj((char *)m->xxs[snum].name);
	
		m->xxi[i][j].pan = read8(f) * 4;
		if (m->xxi[i][j].pan == 0)	/* not sure about this */
			m->xxi[i][j].pan = 0x80;
		
		m->xxi[i][j].vol = read8(f);
		flags = read8(f);
		read8(f);	/* unknown - 0x80 */

		m->xxi[i][j].vwf = vwf;
		m->xxi[i][j].vde = vde;
		m->xxi[i][j].vra = vra;
		m->xxi[i][j].vsw = vsw;
		m->xxi[i][j].sid = snum;
	
		m->xxs[snum].len = read32l(f);
		m->xxs[snum].lps = read32l(f);
		m->xxs[snum].lpe = read32l(f);
	
		m->xxs[snum].flg = 0;
		if (flags & 0x04)
			m->xxs[snum].flg |= WAVE_16_BITS;
		if (flags & 0x08)
			m->xxs[snum].flg |= WAVE_LOOPING;
		if (flags & 0x10)
			m->xxs[snum].flg |= WAVE_BIDIR_LOOP;
		/* if (flags & 0x80)
			m->xxs[snum].flg |= ? */
	
		if (m->xxs[snum].flg & WAVE_16_BITS) {
			m->xxs[snum].len <<= 1;
			m->xxs[snum].lps <<= 1;
			m->xxs[snum].lpe <<= 1;
		}
	
		srate = read32l(f);
		finetune = 0;
		c2spd_to_note(srate, &m->xxi[i][j].xpo, &m->xxi[i][j].fin);
		m->xxi[i][j].fin += finetune;
	
		read32l(f);			/* 0x00000000 */
		read32l(f);			/* unknown */
	
		if (j > 0)
			reportv(ctx, 1, "\n                                      ");
	
		reportv(ctx, 1, "[%X] %05x%c%05x %05x %c V%02x P%02x %5d ",
			j, m->xxs[snum].len,
			m->xxs[snum].flg & WAVE_16_BITS ? '+' : ' ',
			m->xxs[snum].lps,
			m->xxs[snum].lpe,
			m->xxs[snum].flg & WAVE_BIDIR_LOOP ? 'B' : 
				m->xxs[snum].flg & WAVE_LOOPING ? 'L' : ' ',
			m->xxi[i][j].vol,
			m->xxi[i][j].pan,
			srate);
	
		if (m->xxs[snum].len > 1) {
			xmp_drv_loadpatch(ctx, f, snum, m->c4rate, 0, &m->xxs[snum], NULL);
			reportv(ctx, 0, ".");
		}
	}
}

static int gal4_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i, offset;

	LOAD_INIT();

	read32b(f);	/* Skip RIFF */
	read32b(f);	/* Skip size */
	read32b(f);	/* Skip AM   */

	offset = ftell(f);

	m->xxh->smp = m->xxh->ins = 0;

	iff_register("MAIN", get_main);
	iff_register("ORDR", get_ordr);
	iff_register("PATT", get_patt_cnt);
	iff_register("INST", get_inst_cnt);
	iff_setflag(IFF_LITTLE_ENDIAN);
	iff_setflag(IFF_CHUNK_TRUNC4);

	/* Load IFF chunks */
	while (!feof(f))
		iff_chunk(ctx, f);

	iff_release();

	m->xxh->trk = m->xxh->pat * m->xxh->chn;

	MODULE_INFO();
	INSTRUMENT_INIT();
	PATTERN_INIT();

	if (V(0)) {
	    report("Stored patterns: %d\n", m->xxh->pat);
	    report("Stored samples : %d ", m->xxh->smp);
	}

	fseek(f, start + offset, SEEK_SET);
	snum = 0;

	iff_register("PATT", get_patt);
	iff_register("INST", get_inst);
	iff_setflag(IFF_LITTLE_ENDIAN);
	iff_setflag(IFF_CHUNK_TRUNC4);

	/* Load IFF chunks */
	while (!feof (f))
		iff_chunk(ctx, f);

	iff_release();

	reportv(ctx, 0, "\n");

	for (i = 0; i < m->xxh->chn; i++)
		m->xxc[i].pan = 0x80;

	return 0;
}
