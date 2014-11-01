/*
 * FC-M_Packer.c   Copyright (C) 1997 Asle / ReDoX
 *                 Copyright (c) 2006-2007 Claudio Matsuoka
 *
 * Converts back to ptk FC-M packed MODs
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_fcm (uint8 *, int);
static int depack_fcm (FILE *, FILE *);

struct pw_format pw_fcm = {
	"FCM",
	"FC-M Packer",
	0x00,
	test_fcm,
	depack_fcm
};


static int depack_fcm(FILE *in, FILE *out)
{
	uint8 c1;
	uint8 ptable[128];
	uint8 pat_pos;
	uint8 pat_max;
	int i;
	int size, ssize = 0;

	memset(ptable, 0, 128);

	read32b(in);				/* bypass "FC-M" ID */
	read16b(in);				/* version number? */
	read32b(in);				/* bypass "NAME" chunk */
	pw_move_data(out, in, 20);		/* read and write title */
	read32b(in);				/* bypass "INST" chunk */

	/* read and write sample descriptions */
	for (i = 0; i < 31; i++) {
		pw_write_zero(out, 22);		/*sample name */
		write16b(out, size = read16b(in));	/* size */
		ssize += size * 2;
		write8(out, read8(in));		/* finetune */
		write8(out, read8(in));		/* volume */
		write16b(out, read16b(in));	/* loop start */
		size = read16b(in);		/* loop size */
		if (size == 0)
			size = 1;
		write16b(out, size);
	}

	read32b(in);				/* bypass "LONG" chunk */
	write8(out, pat_pos = read8(in));	/* pattern table lenght */
	write8(out, read8(in));			/* NoiseTracker byte */
	read32b(in);				/* bypass "PATT" chunk */

	/* read and write pattern list and get highest patt number */
	for (pat_max = i = 0; i < pat_pos; i++) {
		write8(out, c1 = read8(in));
		if (c1 > pat_max)
			pat_max = c1;
	}
	for (; i < 128; i++)
		write8(out, 0);

	write32b(out, PW_MOD_MAGIC);		/* write ptk ID */
	read32b(in);				/* bypass "SONG" chunk */

	for (i = 0; i <= pat_max; i++)		/* pattern data */
		pw_move_data(out, in, 1024);

	read32b(in);				/* bypass "SAMP" chunk */
	pw_move_data(out, in, ssize);		/* sample data */

	return 0;
}


static int test_fcm (uint8 *data, int s)
{
	int start = 0;
	int j;

	PW_REQUEST_DATA(s, 37 + 8 * 31);

	/* "FC-M" : ID of FC-M packer */
	if (data[0] != 'F' || data[1] != 'C' || data[2] != '-' ||
		data[3] != 'M')
		return -1;

	/* test 1 */
	if (data[start + 4] != 0x01)
		return -1;

	/* test 2 */
	if (data[start + 5] != 0x00)
		return -1;

	/* test 3 */
	for (j = 0; j < 31; j++) {
		if (data[start + 37 + 8 * j] > 0x40)
			return -1;
	}

	return 0;
}
