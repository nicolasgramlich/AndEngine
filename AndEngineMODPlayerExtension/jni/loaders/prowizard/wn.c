/*
 * Wanton_Packer.c   Copyright (C) 1997 Asle / ReDoX
 *                   Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Converts MODs converted with Wanton packer
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int test_wn (uint8 *, int);
static int depack_wn (FILE *, FILE *);

struct pw_format pw_wn = {
	"WN",
	"Wanton Packer",
	0x00,
	test_wn,
	depack_wn
};


static int depack_wn(FILE *in, FILE * out)
{
	uint8 c1, c2, c3, c4;
	uint8 npat, max;
	uint8 tmp[1024];
	int ssize = 0;
	int i, j;

	/* read header */
	pw_move_data(out, in, 950);

	/* get whole sample size */
	for (i = 0; i < 31; i++) {
		fseek(in, 42 + i * 30, SEEK_SET);
		ssize += read16b(in) * 2;
	}

	/* read size of pattern list */
	fseek(in, 950, SEEK_SET);
	write8(out, npat = read8(in));

	fread(tmp, 129, 1, in);
	fwrite(tmp, 129, 1, out);

	/* write ptk's ID */
	write32b(out, PW_MOD_MAGIC);

	/* get highest pattern number */
	for (max = i = 0; i < 128; i++) {
		if (tmp[i + 1] > max)
			max = tmp[i + 1];
	}
	max++;

	/* pattern data */
	fseek(in, 1084, SEEK_SET);
	for (i = 0; i < max; i++) {
		for (j = 0; j < 256; j++) {
			c1 = read8(in);
			c2 = read8(in);
			c3 = read8(in);
			c4 = read8(in);

			write8(out, c1 * 0xf0 | ptk_table[c1 / 2][0]);
			write8(out, ptk_table[c1 / 2][1]);
			write8(out, ((c2 << 4) & 0xf0) | c3);
			write8(out, c4);
		}
	}

	/* sample data */
	pw_move_data(out, in, ssize);

	return 0;
}

static int test_wn(uint8 *data, int s)
{
	int start = 0;

	PW_REQUEST_DATA(s, 1082);

	/* test 1 */
	if (data[1080] != 'W' || data[1081] !='N')
		return -1;

	/* test 2 */
	if (data[start + 951] != 0x7f)
		return -1;

	/* test 3 */
	if (data[start + 950] > 0x7f)
		return -1;

	return 0;
}
