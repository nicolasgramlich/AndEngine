/*
 * Kris_tracker.c   Copyright (C) 1997 Asle / ReDoX
 *                  Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Kris Tracker to Protracker.
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_kris (uint8 *, int);
static int depack_kris (FILE *, FILE *);

struct pw_format pw_kris = {
	"KRIS",
	"ChipTracker",
	0x00,
	test_kris,
	depack_kris
};


static int depack_kris(FILE *in, FILE *out)
{
	uint8 tmp[1024];
	uint8 c3;
	uint8 npat, max;
	uint8 ptable[128];
	uint8 note, ins, fxt, fxp;
	uint8 tdata[512][256];
	short taddr[128][4];
	short maxtaddr = 0;
	int i, j, k;
	int size, ssize = 0;

	memset(tmp, 0, 1024);
	memset(ptable, 0, 128);
	memset(taddr, 0, 128 * 4 * 2);
	memset(tdata, 0, 512 << 8);

	pw_move_data(out, in, 20);			/* title */
	fseek(in, 2, SEEK_CUR);

	/* 31 samples */
	for (i = 0; i < 31; i++) {
		/* sample name */
		fread(tmp, 22, 1, in);
		if (tmp[0] == 0x01)
			tmp[0] = 0x00;
		fwrite(tmp, 22, 1, out);

		write16b(out, size = read16b(in));	/* size */
		ssize += size * 2;
		write8(out, read8(in));			/* fine */
		write8(out, read8(in));			/* volume */
		write16b(out, read16b(in) / 2);		/* loop start */
		write16b(out, read16b(in));		/* loop size */
	}

	read32b(in);			/* bypass ID "KRIS" */
	write8(out, npat = read8(in));	/* number of pattern in pattern list */
	write8(out, read8(in));		/* Noisetracker restart byte */

	/* pattern table (read,count and write) */
	c3 = 0;
	k = 0;
	for (i = 0; i < 128; i++, k++) {
		for (j = 0; j < 4; j++) {
			taddr[k][j] = read16b(in);
			if (taddr[k][j] > maxtaddr)
				maxtaddr = taddr[k][j];
		}

		for (j = 0; j < k; j++) {
			if (!memcmp(taddr[j], taddr[k], 4)) {
				ptable[i] = ptable[j];
				k--;
				break;
			}
		}

		if (k == j)
			ptable[i] = c3++;

		write8(out, ptable[i]);
	}

	max = c3 - 1;
	write32b(out, PW_MOD_MAGIC);	/* ptk ID */
	read16b(in);			/* bypass two unknown bytes */

	/* Track data ... */
	for (i = 0; i <= (maxtaddr / 256); i += 1) {
		memset(tmp, 0, 1024);
		fread(tmp, 256, 1, in);

		for (j = 0; j < 64 * 4; j += 4) {
			note = tmp[j];
			ins = tmp[j + 1];
			fxt = tmp[j + 2] & 0x0f;
			fxp = tmp[j + 3];

			tdata[i][j] = ins & 0xf0;

			if (note != 0xa8) {
				tdata[i][j] |= ptk_table[(note / 2) - 35][0];
				tdata[i][j + 1] = ptk_table[(note / 2) - 35][1];
			}
			tdata[i][j + 2] = ((ins << 4) & 0xf0) | (fxt & 0x0f);
			tdata[i][j + 3] = fxp;
		}
	}

	for (i = 0; i <= max; i++) {
		memset(tmp, 0, 1024);
		for (j = 0; j < 64 * 4; j += 4) {
			uint8 *p = &tmp[j * 4];

			memcpy(p, &tdata[taddr[i][0] / 256][j], 4);
			memcpy(p + 4, &tdata[taddr[i][1] / 256][j], 4);
			memcpy(p + 8, &tdata[taddr[i][2] / 256][j], 4);
			memcpy(p + 12, &tdata[taddr[i][3] / 256][j], 4);
		}
		fwrite(tmp, 1024, 1, out);
	}

	/* sample data */
	pw_move_data(out, in, ssize);

	return 0;
}

static int test_kris (uint8 *data, int s)
{
	int j;
	int start = 0;

	/* test 1 */
	PW_REQUEST_DATA (s, 1024);

	if (readmem32b(data + 952) != MAGIC4('K','R','I','S'))
		return -1;

	/* test 2 */
	for (j = 0; j < 31; j++) {
		if (data[47 + j * 30] > 0x40)
			return -1;
		if (data[46 + j * 30] > 0x0f)
			return -1;
	}

	/* test volumes */
	for (j = 0; j < 31; j++) {
		if (data[start + j * 30 + 47] > 0x40)
			return -1;
	}

	return 0;
}
