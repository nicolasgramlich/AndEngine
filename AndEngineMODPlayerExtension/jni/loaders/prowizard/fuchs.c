/*
 * FuchsTracker.c   Copyright (C) 1999 Sylvain "Asle" Chipaux
 *                  Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Depacks Fuchs Tracker modules
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_fuchs (uint8 *, int);
static int depack_fuchs (FILE *, FILE *);

struct pw_format pw_fchs = {
	"FCHS",
	"Fuchs Tracker",
	0x00,
	test_fuchs,
	depack_fuchs
};


static int depack_fuchs(FILE *in, FILE *out)
{
	uint8 *tmp;
	uint8 c1;
	uint8 pmax;
	int ssize = 0;
	int SampleSizes[16];
	int LoopStart[16];
	int i, j;

	memset(SampleSizes, 0, 16 * 4);
	memset(LoopStart, 0, 16 * 4);

	pw_write_zero(out, 1080);		/* write ptk header */
	fseek(out, 0, SEEK_SET);
	pw_move_data(out, in, 10);		/* read/write title */
	ssize = read32b(in);			/* read all sample data size */

	/* read/write sample sizes */
	for (i = 0; i < 16; i++) {
		fseek(out, 42 + i * 30, SEEK_SET);
		write16b(out, (SampleSizes[i] = read16b(in)) / 2);
	}

	/* read/write volumes */
	for (i = 0; i < 16; i++) {
		fseek(out, 45 + i * 30, SEEK_SET);
		fseek(in, 1, SEEK_CUR);
		write8(out, read8(in));
	}

	/* read/write loop start */
	for (i = 0; i < 16; i++) {
		fseek(out, 46 + i * 30, SEEK_SET);
		write8(out, (LoopStart[i] = read16b(in)) / 2);
	}

	/* write replen */
	for (i = 0; i < 16; i++) {
		fseek(out, 48 + i * 30, SEEK_SET);
		j = SampleSizes[i] - LoopStart[i];
		if ((j == 0) || (LoopStart[i] == 0))
			write16b(out, 0x0001);
		else
			write16b(out, j / 2);
	}

	/* fill replens up to 31st sample wiz $0001 */
	for (i = 16; i < 31; i++) {
		fseek(out, 48 + i * 30, SEEK_SET);
		write16b(out, 0x0001);
	}

	/* that's it for the samples ! */
	/* now, the pattern list */

	/* read number of pattern to play */
	fseek(out, 950, SEEK_SET);
	/* bypass empty byte (saved wiz a WORD ..) */
	fseek(in, 1, SEEK_CUR);
	write8(out, read8(in));

	/* write ntk byte */
	write8(out, 0x7f);

	/* read/write pattern list */
	for (pmax = i = 0; i < 40; i++) {
		fseek(in, 1, SEEK_CUR);
		write8(out, c1 = read8(in));
		if (c1 > pmax)
			pmax = c1;
	}

	/* write ptk's ID */
	fseek(out, 0, SEEK_END);
	write32b(out, PW_MOD_MAGIC);

	/* now, the pattern data */

	/* bypass the "SONG" ID */
	fseek(in, 4, 1);

	/* read pattern data size */
	j = read32b(in);

	/* read pattern data */
	tmp = (uint8 *)malloc(j);
	fread(tmp, j, 1, in);

	/* convert shits */
	for (i = 0; i < j; i += 4) {
		/* convert fx C arg back to hex value */
		if ((tmp[i + 2] & 0x0f) == 0x0c) {
			c1 = tmp[i + 3];
			if (c1 <= 9) {
				tmp[i + 3] = c1;
				continue;
			}
			if ((c1 >= 16) && (c1 <= 25)) {
				tmp[i + 3] = (c1 - 6);
				continue;
			}
			if ((c1 >= 32) && (c1 <= 41)) {
				tmp[i + 3] = (c1 - 12);
				continue;
			}
			if ((c1 >= 48) && (c1 <= 57)) {
				tmp[i + 3] = (c1 - 18);
				continue;
			}
			if ((c1 >= 64) && (c1 <= 73)) {
				tmp[i + 3] = (c1 - 24);
				continue;
			}
			if ((c1 >= 80) && (c1 <= 89)) {
				tmp[i + 3] = (c1 - 30);
				continue;
			}
			if ((c1 >= 96) && (c1 <= 100)) {
				tmp[i + 3] = (c1 - 36);
				continue;
			}
		}
	}

	/* write pattern data */
	fwrite(tmp, j, 1, out);
	free(tmp);

	/* read/write sample data */
	fseek (in, 4, SEEK_CUR);	/* bypass "INST" Id */
	for (i = 0; i < 16; i++) {
		if (SampleSizes[i] != 0)
			pw_move_data(out, in, SampleSizes[i]);
	}

	return 0;
}


static int test_fuchs (uint8 *data, int s)
{
	int start = 0;
	int j, k, m, n, o;

#if 0
	/* test #1 */
	if (i < 192) {
		Test = BAD;
		return;
	}
	start = i - 192;
#endif

	if (readmem32b(data + 192) != 0x534f4e47)	/* SONG */
		return -1;

	/* all sample size */
	j = ((data[start + 10] << 24) + (data[start + 11] << 16) +
		(data[start + 12] << 8) + data[start + 13]);

	if (j <= 2 || j >= (65535 * 16))
		return -1;

	/* samples descriptions */
	m = 0;
	for (k = 0; k < 16; k++) {
		/* size */
		o = (data[start + k * 2 + 14] << 8) + data[start + k * 2 + 15];
		/* loop start */
		n = (data[start + k * 2 + 78] << 8) + data[start + k * 2 + 79];

		/* volumes */
		if (data[start + 46 + k * 2] > 0x40)
			return -1;

		/* size < loop start ? */
		if (o < n)
			return -1;

		m += o;
	}

	/* m is the size of all samples (in descriptions) */
	/* j is the sample data sizes (header) */
	/* size<2  or  size > header sample size ? */
	if (m <= 2 || m > j)
		return -1;

	/* get highest pattern number in pattern list */
	k = 0;
	for (j = 0; j < 40; j++) {
		n = data[start + j * 2 + 113];
		if (n > 40)
			return -1;
		if (n > k)
			k = n;
	}

	/* m is the size of all samples (in descriptions) */
	/* k is the highest pattern data -1 */

#if 0
	/* input file not long enough ? */
	k += 1;
	k *= 1024;
	PW_REQUEST_DATA (s, k + 200);
#endif

	/* m is the size of all samples (in descriptions) */
	/* k is the pattern data size */

	return 0;
}
