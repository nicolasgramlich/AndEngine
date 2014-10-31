/*
 * ProPacker_21.c   Copyright (C) 1997 Sylvain "Asle" Chipaux
 *		    Copyright (C) 2006-2009 Claudio Matsuoka
 *
 * Converts PP21 packed MODs back to PTK MODs
 * thanks to Gryzor and his ProWizard tool ! ... without it, this prog
 * would not exist !!!
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int depack_pp21 (FILE *, FILE *);
static int test_pp21 (uint8 *, int);

struct pw_format pw_pp21 = {
	"PP21",
	"ProPacker 2.1",
	0x00,
	test_pp21,
	depack_pp21
};

static int depack_pp21(FILE *in, FILE *out)
{
	uint8 ptable[128];
	int max = 0;
	uint8 trk[4][128];
	int tptr[512][64];
	uint8 numpat;
	uint8 *tab;
	uint8 buf[1024];
	int i, j;
	int size;
	int ssize = 0;
	int tabsize = 0;		/* Reference Table Size */

	memset(ptable, 0, 128);
	memset(trk, 0, 4 * 128);
	memset(tptr, 0, 512 * 128);

	pw_write_zero(out, 20);			/* title */

	for (i = 0; i < 31; i++) {
		pw_write_zero(out, 22);		/* sample name */
		write16b(out, size = read16b(in));
		ssize += size * 2;
		write8(out, read8(in));		/* finetune */
		write8(out, read8(in));		/* volume */
		write16b(out, read16b(in));	/* loop start */
		write16b(out, read16b(in));	/* loop size */
	}

	write8(out, numpat = read8(in));	/* number of patterns */
	write8(out, read8(in));			/* NoiseTracker restart byte */

	max = 0;
	for (j = 0; j < 4; j++) {
		for (i = 0; i < 128; i++) {
			trk[j][i] = read8(in);
			if (trk[j][i] > max)
				max = trk[j][i];
		}
	}

	/* write pattern table without any optimizing ! */
	for (i = 0; i < numpat; i++)
		write8(out, i);
	pw_write_zero(out, 128 - i);

	write32b(out, PW_MOD_MAGIC);		/* M.K. */


	/* PATTERN DATA code starts here */

	/*printf ("Highest track number : %d\n", max); */
	for (j = 0; j <= max; j++) {
		for (i = 0; i < 64; i++)
			tptr[j][i] = read16b(in);
	}

	/* read "reference table" size */
	tabsize = read32b(in);

	/* read "reference Table" */
	tab = (uint8 *)malloc(tabsize);
	fread(tab, tabsize, 1, in);

	for (i = 0; i < numpat; i++) {
		memset(buf, 0, 1024);
		for (j = 0; j < 64; j++) {
			uint8 *b = buf + j * 16;
			memcpy(b, tab + tptr[trk[0][i]][j] * 4, 4);
			memcpy(b + 4, tab + tptr[trk[1][i]][j] * 4, 4);
			memcpy(b + 8, tab + tptr[trk[2][i]][j] * 4, 4);
			memcpy(b + 12, tab + tptr[trk[3][i]][j] * 4, 4);
		}
		fwrite (buf, 1024, 1, out);
	}

	free (tab);

	/* Now, it's sample data ... though, VERY quickly handled :) */
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_pp21(uint8 *data, int s)
{
	int j, k, l;
	int start = 0;
	int ssize;

	l = 0;
	for (j = 0; j < 31; j++) {
		k = ((data[start + j * 8] << 8) + data[start + 1 + j * 8]) * 2;
		l += k;

		/* finetune > 0x0f ? */
		if (data[start + 2 + 8 * j] > 0x0f)
			return -1;

		/* loop start > size ? */
		if ((((data[start + 4 + j * 8] << 8) + data[start + 5 + j * 8]) * 2) > k)
			return -1;
	}

	if (l <= 2)
		return -1;

	/* test #3   about size of pattern list */
	l = data[start + 248];
	if (l > 127 || l == 0)
		return -1;

	/* get the highest track value */
	k = 0;
	for (j = 0; j < 512; j++) {
		l = data[start + 250 + j];
		if (l > k)
			k = l;
	}
	/* k is the highest track number */
	k += 1;
	k *= 64;

	/* test #4  track data value > $4000 ? */
	/* ssize used as a variable .. set to 0 afterward */
	ssize = 0;
	for (j = 0; j < k; j++) {
		l = (data[start + 762 + j * 2] << 8) + data[start + 763 + j * 2];
		if (l > ssize)
			ssize = l;

		if (l > 0x4000)
			return -1;
	}

	/* test #5  reference table size *4 ? */
	/* ssize is the highest reference number */
	k *= 2;
	l = (data[start + k + 762] << 24) + (data[start + k + 763] << 16) +
			(data[start + k + 764] << 8) + data[start + k + 765];

	if (l != ((ssize + 1) * 4))
		return -1;

	return 0;
}
