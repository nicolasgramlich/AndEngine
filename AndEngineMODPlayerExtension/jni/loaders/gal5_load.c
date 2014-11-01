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

/* Galaxy Music System 5.0 module file loader
 *
 * Based on the format description by Dr.Eggman
 * (http://www.jazz2online.com/J2Ov2/articles/view.php?articleID=288)
 * and Jazz Jackrabbit modules by Alexander Brandon from Lori Central
 * (http://www.loricentral.com/jj2music.html)
 */

static int gal5_test(FILE *, char *, const int);
static int gal5_load(struct xmp_context *, FILE *, const int);

struct xmp_loader_info gal5_loader = {
	"GAL5",
	"Galaxy Music System 5.0",
	gal5_test,
	gal5_load
};

static uint8 chn_pan[64];

static int gal5_test(FILE *f, char *t, const int start)
{
        if (read32b(f) != MAGIC4('R', 'I', 'F', 'F'))
		return -1;

	read32b(f);

	if (read32b(f) != MAGIC4('A', 'M', ' ', ' '))
		return -1;

	if (read32b(f) != MAGIC4('I', 'N', 'I', 'T'))
		return -1;

	read_title(f, t, 0);

	return 0;
}

static void get_init(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	char buf[64];
	int flags;
	
	fread(buf, 1, 64, f);
	strncpy(m->name, buf, 64);
	strcpy(m->type, "Galaxy Music System 5.0");
	flags = read8(f);	/* bit 0: Amiga period */
	if (~flags & 0x01)
		m->xxh->flg = XXM_FLG_LINEAR;
	m->xxh->chn = read8(f);
	m->xxh->tpo = read8(f);
	m->xxh->bpm = read8(f);
	read16l(f);		/* unknown - 0x01c5 */
	read16l(f);		/* unknown - 0xff00 */
	read8(f);		/* unknown - 0x80 */
	fread(chn_pan, 1, 64, f);
}

static void get_ordr(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i;

	m->xxh->len = read8(f) + 1;
	/* Don't follow Dr.Eggman's specs here */

	for (i = 0; i < m->xxh->len; i++)
		m->xxo[i] = read8(f);
}

static void get_patt_cnt(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i;

	i = read8(f) + 1;	/* pattern number */

	if (i > m->xxh->pat)
		m->xxh->pat = i;
}

static void get_inst_cnt(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i;

	read32b(f);		/* 42 01 00 00 */
	read8(f);		/* 00 */
	i = read8(f) + 1;	/* instrument number */

	if (i > m->xxh->ins)
		m->xxh->ins = i;
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
	int i, srate, finetune, flags;

	read32b(f);		/* 42 01 00 00 */
	read8(f);		/* 00 */
	i = read8(f);		/* instrument number */
	
	if (V(1) && i == 0) {
	    report("\n     Instrument name                  Smp Len   LBeg  LEnd  L Vol Flag C2Spd");
	}

	fread(&m->xxih[i].name, 1, 28, f);
	str_adj((char *)m->xxih[i].name);

	fseek(f, 290, SEEK_CUR);	/* Sample/note map, envelopes */
	m->xxih[i].nsm = read16l(f);

	reportv(ctx, 1, "\n[%2X] %-28.28s  %2d ", i, m->xxih[i].name,
							m->xxih[i].nsm);

	if (m->xxih[i].nsm == 0)
		return;

	m->xxi[i] = calloc(sizeof(struct xxm_instrument), m->xxih[i].nsm);

	/* FIXME: Currently reading only the first sample */

	read32b(f);	/* RIFF */
	read32b(f);	/* size */
	read32b(f);	/* AS   */
	read32b(f);	/* SAMP */
	read32b(f);	/* size */
	read32b(f);	/* unknown - usually 0x40000000 */

	fread(&m->xxs[i].name, 1, 28, f);
	str_adj((char *)m->xxs[i].name);

	read32b(f);	/* unknown - 0x0000 */
	read8(f);	/* unknown - 0x00 */

	m->xxi[i][0].sid = i;
	m->xxih[i].vol = read8(f);
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].vol = (read16l(f) + 1) / 512;
	flags = read16l(f);
	read16l(f);			/* unknown - 0x0080 */
	m->xxs[i].len = read32l(f);
	m->xxs[i].lps = read32l(f);
	m->xxs[i].lpe = read32l(f);

	m->xxs[i].flg = 0;
	if (flags & 0x04)
		m->xxs[i].flg |= WAVE_16_BITS;
	if (flags & 0x08)
		m->xxs[i].flg |= WAVE_LOOPING;
	if (flags & 0x10)
		m->xxs[i].flg |= WAVE_LOOPING | WAVE_BIDIR_LOOP;
	if (~flags & 0x80)
		m->xxs[i].flg |= WAVE_UNSIGNED;

	srate = read32l(f);
	finetune = 0;
	c2spd_to_note(srate, &m->xxi[i][0].xpo, &m->xxi[i][0].fin);
	m->xxi[i][0].fin += finetune;

	read32l(f);			/* 0x00000000 */
	read32l(f);			/* unknown */

	reportv(ctx, 1, "[%x] %05x%c%05x %05x %c V%02x %04x %5d ",
		0, m->xxs[i].len,
		m->xxs[i].flg & WAVE_16_BITS ? '+' : ' ',
		m->xxs[i].lps,
		m->xxs[i].lpe,
		m->xxs[i].flg & WAVE_BIDIR_LOOP ? 'B' : 
			m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ',
		m->xxi[i][0].vol, flags, srate);

	if (m->xxs[i].len > 1) {
		xmp_drv_loadpatch(ctx, f, i, m->c4rate, 0, &m->xxs[i], NULL);
		reportv(ctx, 0, ".");
	}
}

static int gal5_load(struct xmp_context *ctx, FILE *f, const int start)
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

	iff_register("INIT", get_init);		/* Galaxy 5.0 */
	iff_register("ORDR", get_ordr);
	iff_register("PATT", get_patt_cnt);
	iff_register("INST", get_inst_cnt);
	iff_setflag(IFF_LITTLE_ENDIAN);
	iff_setflag(IFF_SKIP_EMBEDDED);
	iff_setflag(IFF_CHUNK_ALIGN2);

	/* Load IFF chunks */
	while (!feof(f))
		iff_chunk(ctx, f);

	iff_release();

	m->xxh->trk = m->xxh->pat * m->xxh->chn;
	m->xxh->smp = m->xxh->ins;

	MODULE_INFO();
	INSTRUMENT_INIT();
	PATTERN_INIT();

	if (V(0)) {
	    report("Stored patterns: %d\n", m->xxh->pat);
	    report("Stored samples : %d ", m->xxh->smp);
	}

	fseek(f, start + offset, SEEK_SET);

	iff_register("PATT", get_patt);
	iff_register("INST", get_inst);
	iff_setflag(IFF_LITTLE_ENDIAN);
	iff_setflag(IFF_SKIP_EMBEDDED);
	iff_setflag(IFF_CHUNK_ALIGN2);

	/* Load IFF chunks */
	while (!feof (f))
		iff_chunk(ctx, f);

	iff_release();

	reportv(ctx, 0, "\n");

	for (i = 0; i < m->xxh->chn; i++)
		m->xxc[i].pan = chn_pan[i] * 2;

	return 0;
}
