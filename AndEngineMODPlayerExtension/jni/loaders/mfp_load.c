/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/*
 * A module packer created by Shaun Southern. Samples are stored in a
 * separate file. File prefixes are mfp for song and smp for samples. For
 * more information see http://www.exotica.org.uk/wiki/Magnetic_Fields_Packer
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <limits.h>
#include <unistd.h>

static int mfp_test(FILE *, char *, const int);
static int mfp_load(struct xmp_context *, FILE *, const int);

struct xmp_loader_info mfp_loader = {
	"MFP",
	"Magnetic Fields Packer",
	mfp_test,
	mfp_load
};

static int mfp_test(FILE *f, char *t, const int start)
{
	uint8 buf[384];
	int i, len, lps, lsz;

	if (fread(buf, 1, 384, f) < 384)
		return -1;

	/* check restart byte */
	if (buf[249] != 0x7f)
		return -1;

	for (i = 0; i < 31; i++) {
		/* check size */
		len = readmem16b(buf + i * 8);
		if (len > 0x7fff)
			return -1;

		/* check finetune */
		if (buf[i * 8 + 2] & 0xf0)
			return -1;

		/* check volume */
		if (buf[i * 8 + 3] > 0x40)
			return -1;

		/* check loop start */
		lps = readmem16b(buf + i * 8 + 4);
		if (lps > len)
			return -1;

		/* check loop size */
		lsz = readmem16b(buf + i * 8 + 6);
		if (lps + lsz - 1 > len)
			return -1;

		if (len > 0 && lsz == 0)
			return -1;
	}

	if (buf[248] != readmem16b(buf + 378))
		return -1;

	if (readmem16b(buf + 378) != readmem16b(buf + 380))
		return -1;

	return 0;
}

static int mfp_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i, j, k, x, y;
	struct xxm_event *event;
	struct stat st;
	char smp_filename[PATH_MAX];
	FILE *s;
	int size1, size2;
	int pat_addr, pat_table[128][4];
	uint8 buf[1024], mod_event[4];
	int row;

	LOAD_INIT();

	sprintf(m->type, "Magnetic Fields Packer");
	MODULE_INFO();

	m->xxh->chn = 4;

	m->xxh->ins = m->xxh->smp = 31;
	INSTRUMENT_INIT();

	reportv(ctx, 1, "     Len  LBeg LEnd L Vol Fin\n");

	for (i = 0; i < 31; i++) {
		int loop_size;

		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);
		
		m->xxs[i].len = 2 * read16b(f);
		m->xxi[i][0].fin = (int8)(read8(f) << 4);
		m->xxi[i][0].vol = read8(f);
		m->xxs[i].lps = 2 * read16b(f);
		loop_size = read16b(f);

		m->xxs[i].lpe = m->xxs[i].lps + 2 * loop_size;
		m->xxs[i].flg = loop_size > 1 ? WAVE_LOOPING : 0;
		m->xxi[i][0].pan = 0x80;
		m->xxi[i][0].sid = i;
		m->xxih[i].nsm = !!(m->xxs[i].len);
		m->xxih[i].rls = 0xfff;

		if (V(1) && m->xxs[i].len > 2) {
                	report("[%2X] %04x %04x %04x %c V%02x %+d %c\n",
                       		i, m->xxs[i].len, m->xxs[i].lps,
                        	m->xxs[i].lpe,
				loop_size > 1 ? 'L' : ' ',
                        	m->xxi[i][0].vol, m->xxi[i][0].fin >> 4,
                        	m->xxs[i].flg & WAVE_PTKLOOP ? '!' : ' ');
		}
	}

	m->xxh->len = m->xxh->pat = read8(f);
	read8(f);		/* restart */

	for (i = 0; i < 128; i++)
		m->xxo[i] = read8(f);

#if 0
	for (i = 0; i < 128; i++) {
		m->xxo[i] = read8(f);
		if (m->xxo[i] > m->xxh->pat)
			m->xxh->pat = m->xxo[i];
	}
	m->xxh->pat++;
#endif

	m->xxh->trk = m->xxh->pat * m->xxh->chn;

	/* Read and convert patterns */

	PATTERN_INIT();

	size1 = read16b(f);
	size2 = read16b(f);

	for (i = 0; i < size1; i++) {		/* Read pattern table */
		for (j = 0; j < 4; j++) {
			pat_table[i][j] = read16b(f);
		}
	}

	reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

	pat_addr = ftell(f);

	for (i = 0; i < m->xxh->pat; i++) {
		PATTERN_ALLOC(i);
		m->xxp[i]->rows = 64;
		TRACK_ALLOC(i);

		for (j = 0; j < 4; j++) {
			fseek(f, pat_addr + pat_table[i][j], SEEK_SET);

			fread(buf, 1, 1024, f);

			for (row = k = 0; k < 4; k++) {
				for (x = 0; x < 4; x++) {
					for (y = 0; y < 4; y++, row++) {
						event = &EVENT(i, j, row);
						memcpy(mod_event, &buf[buf[buf[buf[k] + x] + y] * 2], 4);
						cvt_pt_event(event, mod_event);
					}
				}
			}
		}
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	/* Read samples */
	reportv(ctx, 0, "Loading samples: %d ", m->xxh->ins);

	/* first check smp.filename */
	m->basename[0] = 's';
	m->basename[1] = 'm';
	m->basename[2] = 'p';
	snprintf(smp_filename, PATH_MAX, "%s%s", m->dirname, m->basename);
	if (stat(smp_filename, &st) < 0) {
		/* handle .set filenames like in Kid Chaos*/
		char *x;
		if ((x = strchr(smp_filename, '-')))
			strcpy(x, ".set");
		if (stat(smp_filename, &st) < 0) {
			report("sample file %s is missing!\n", smp_filename);
			return 0;
		}
	}
	if ((s = fopen(smp_filename, "rb")) == NULL) {
		report("can't open sample file %s!\n", smp_filename);
		return 0;
	}

	for (i = 0; i < m->xxh->ins; i++) {
		xmp_drv_loadpatch(ctx, s, m->xxi[i][0].sid, m->c4rate, 0,
				  &m->xxs[m->xxi[i][0].sid], NULL);
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	fclose(s);

	m->xxh->flg |= XXM_FLG_MODRNG;

	return 0;
}
