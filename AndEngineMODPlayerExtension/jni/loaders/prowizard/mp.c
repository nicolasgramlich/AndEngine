/*
 * Module_Protector.c   Copyright (C) 1997 Asle / ReDoX
 *                      Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Converts MP packed MODs back to PTK MODs
 */

#include <stdlib.h>
#include <string.h>
#include "prowiz.h"

#define MAGIC_TRK1	MAGIC4('T','R','K','1')


static int depack_mp (FILE *, FILE *);
static int test_mp_id (uint8 *, int);
static int test_mp_noid (uint8 *, int);

struct pw_format pw_mp_id = {
	"MP",
	"Module Protector",
	0x00,
	test_mp_id,
	depack_mp
};

struct pw_format pw_mp_noid = {
	"MP",
	"Module Protector noID",
	0x00,
	test_mp_noid,
	depack_mp
};


static int depack_mp(FILE *in, FILE *out)
{
	uint8 c1;
	uint8 ptable[128];
	uint8 max;
	int i;
	int size, ssize = 0;

	memset(ptable, 0, 128);

	pw_write_zero(out, 20);				/* title */

	if (read32b(in) != MAGIC_TRK1)			/* TRK1 */
		fseek(in, -4, SEEK_CUR);

	for (i = 0; i < 31; i++) {
		pw_write_zero(out, 22);			/* sample name */
		write16b(out, size = read16b(in));	/* size */
		ssize += size * 2;
		write8(out, read8(in));			/* finetune */
		write8(out, read8(in));			/* volume */
		write16b(out, read16b(in));		/* loop start */
		write16b(out, read16b(in));		/* loop size */
	}

	write8(out, read8(in));		/* pattern table length */
	write8(out, read8(in));		/* NoiseTracker restart byte */

	for (max = i = 0; i < 128; i++) {
		write8(out, c1 = read8(in));
		if (c1 > max)
			max = c1;
	}
	max++;

	write32b(out, PW_MOD_MAGIC);		/* M.K. */

	if (read32b(in) != 0)			/* bypass unknown empty bytes */
		fseek (in, -4, SEEK_CUR);

	pw_move_data(out, in, 1024 * max);	/* pattern data */
	pw_move_data(out, in, ssize);		/* sample data */

	return 0;
}


static int test_mp_noid(uint8 *data, int s)
{
	int start, ssize;
	int j, k, l, m, n;

	start = 0;

#if 0
	if (i < 3) {
		Test = BAD;
		return;
	}
#endif

	/* test #2 */
	l = 0;
	for (j = 0; j < 31; j++) {
		int x = start + 8 * j;

		k = readmem16b(data + x) * 2;		/* size */
		m = readmem16b(data + x + 4) * 2;	/* loop start */
		n = readmem16b(data + x + 6) * 2;	/* loop size */
		l += k;

		/* finetune > 0x0f ? */
		if (data[x + 2] > 0x0f)
			return -1;

		/* loop start+replen > size ? */
		if (n != 2 && (m + n) > k)
			return -1;

		/* loop size > size ? */
		if (n > (k + 2))
			return -1;

		/* loop start != 0 and loop size = 0 */
		if (m != 0 && n <= 2)
			return -1;

		/* when size!=0  loopsize==0 ? */
		if (k != 0 && n == 0)
			return -1;
	}

	if (l <= 2)
		return -1;

	/* test #3 */
	l = data[start + 248];
	if (l > 0x7f || l == 0x00)
		return -1;

	/* test #4 */
	/* l contains the size of the pattern list */
	k = 0;
	for (j = 0; j < 128; j++) {
		if (data[start + 250 + j] > k)
			k = data[start + 250 + j];
		if (data[start + 250 + j] > 0x7f)
			return -1;
		if (j > l + 3) {
			if (data[start + 250 + j] != 0x00)
				return -1;
		}
	}
	k++;

	/* test #5  ptk notes .. gosh ! (testing all patterns !) */
	/* k contains the number of pattern saved */
	for (j = 0; j < (256 * k); j++) {
		int x = start + j * 4;

		l = data[x + 378];
		if (l > 19 && l != 74)		/* MadeInCroatia has l == 74 */
			return -1;

		ssize = data[x + 378] & 0x0f;
		ssize *= 256;
		ssize += data[x + 379];

		if (ssize > 0 && ssize < 0x71)
			return -1;
	}

	/* test #6  (loopStart+LoopSize > Sample ? ) */
	for (j = 0; j < 31; j++) {
		int x = start + j * 8;

		k = readmem16b(data + x) * 2;
		l = (readmem16b(data + x + 4) + readmem16b(data + x + 6)) * 2;

		if (l > (k + 2))
			return -1;
	}

	return 0;
}


static int test_mp_id(uint8 *data, int s)
{
	int j, l, k;
	int start = 0;

	/* "TRK1" Module Protector */
	if (readmem32b(data) != MAGIC_TRK1)
		return -1;

	/* test #1 */
	for (j = 0; j < 31; j++) {
		if (data[start + 6 + 8 * j] > 0x0f)
			return -1;
	}

	/* test #2 */
	l = data[start + 252];
	if (l > 0x7f || l == 0x00)
		return -1;

	/* test #4 */
	k = 0;
	for (j = 0; j < 128; j++) {
		if (data[start + 254 + j] > k)
			k = data[start + 254 + j];
		if (data[start + 254 + j] > 0x7f)
			return -1;
	}
	k++;

	/* test #5  ptk notes .. gosh ! (testing all patterns !) */
	/* k contains the number of pattern saved */
	for (j = 0; j < (256 * k); j++) {
		l = data[start + 382 + j * 4];
		if (l > 19)
			return -1;
	}

	return 0;
}
