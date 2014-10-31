/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/*
 * From http://amp.dascene.net/faq.php:
 * [Face The Music is an] Amiga tracker created by Jörg W. Schmidt in 1990
 * for Maxon Computer GmbH. Face the Music delivers: 8 channels, line-based
 * editor with S.E.L. (Sound Effect Language).
 */
#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"

struct ftm_instrument {
	uint8 name[30];		/* Instrument name */
	uint8 unknown[2];
};

struct ftm_header {
	uint8 id[4];		/* "FTMN" ID string */
	uint8 ver;		/* Version ?!? (0x03) */
	uint8 nos;		/* Number of samples (?) */
	uint8 unknown[10];
	uint8 title[32];	/* Module title */
	uint8 author[32];	/* Module author */
	uint8 unknown2[2];
};

int ftm_load(FILE * f)
{
	int i, j, k;
	struct xxm_event *event;
	struct ftm_header fh;
	struct ftm_instrument si;
	uint8 b1, b2, b3;

	LOAD_INIT();

	fread(&fh.id, 4, 1, f);
	if (memcmp(fh.id, "FTMN", 4))
		return -1;

	fh.ver = read8(f);
	fh.nos = read8(f);
	read16b(f);
	read32b(f);
	read32b(f);
	fread(&fh.title, 32, 1, f);
	fread(&fh.author, 32, 1, f);
	read16b(f);

	//m->xxh->len = fh.len;
	//m->xxh->pat = fh.pat;
	m->xxh->ins = fh.nos;
	m->xxh->smp = m->xxh->ins;
	m->xxh->trk = m->xxh->pat * m->xxh->chn;
	for (i = 0; i < m->xxh->len; i++)
		m->xxo[i] = fh.order[i];

	sprintf(m->type, "Face The Music");
	MODULE_INFO();
	PATTERN_INIT();

	/* Load and convert patterns */
	if (V(0))
		report("Stored patterns: %d ", m->xxh->pat);
	for (i = 0; i < m->xxh->pat; i++) {
		PATTERN_ALLOC(i);
		m->xxp[i]->rows = 64;
		TRACK_ALLOC(i);
		for (j = 0; j < 4; j++) {
		}

		reportv(ctx, 0, ".");
	}

	INSTRUMENT_INIT();
	reportv(ctx, 0, "\nStored samples : %d ", m->xxh->smp);

	for (i = 0; i < m->xxh->smp; i++) {
		reportv(ctx, 0, ".");
	}

	reportv(ctx, 0, "\n");
	m->xxh->flg |= XXM_FLG_MODRNG;

	return 0;
}
