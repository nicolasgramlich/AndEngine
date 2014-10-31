/*
 *   Skyt_Packer.c   Copyright (C) 1997 Asle / ReDoX
 *   Changes for xmp Copyright (C) 2009 Claudio Matsuoka
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int test_skyt (uint8 *, int);
static int depack_skyt (FILE *, FILE *);

struct pw_format pw_skyt = {
	"SKYT",
	"SKYT Packer",
	0x00,
	test_skyt,
	depack_skyt
};


static int depack_skyt(FILE *in, FILE *out)
{
	uint8 c1, c2, c3, c4;
	uint8 ptable[128];
	uint8 pat_pos;
	uint8 pat[1024];
	int i = 0, j = 0, k = 0;
	int trkval[128][4];
	int trk_addr;
	int size, ssize = 0;

	memset(ptable, 0, 128);
	memset(trkval, 0, 128 * 4);

	pw_write_zero(out, 20);			/* write title */

	/* read and write sample descriptions */
	for (i = 0; i < 31; i++) {
		pw_write_zero(out, 22);			/*sample name */
		write16b(out, size = read16b(in));	/* sample size */
		ssize += size * 2;
		write8(out, read8(in));			/* finetune */
		write8(out, read8(in));			/* volume */
		write16b(out, read16b(in));		/* loop start */
		write16b(out, read16b(in));		/* loop size */
	}

	read32b(in);			/* bypass 8 empty bytes */
	read32b(in);
	read32b(in);			/* bypass "SKYT" ID */

	write8(out, pat_pos = read8(in) + 1);	/* pattern table lenght */
	write8(out, 0x7f);			/* write NoiseTracker byte */

	/* read track numbers ... and deduce pattern list */
	for (i = 0; i < pat_pos; i++) {
		for (j = 0; j < 4; j++)
			trkval[i][j] = read16b(in);
	}

	/* write pseudo pattern list */
	for (i = 0; i < 128; i++)
		write8(out, i < pat_pos ? i : 0);

	write32b(out, PW_MOD_MAGIC);		/* write ptk's ID */

	read8(in);				/* bypass $00 unknown byte */

	/* get track address */
	trk_addr = ftell(in);

	/* track data */
	for (i = 0; i < pat_pos; i++) {
		memset(pat, 0, 1024);
		for (j = 0; j < 4; j++) {
			fseek(in, trk_addr + ((trkval[i][j] - 1)<<8), SEEK_SET);
			for (k = 0; k < 64; k++) {
				int x = k * 16 + j * 4;

				c1 = read8(in);
				c2 = read8(in);
				c3 = read8(in);
				c4 = read8(in);

				pat[x] = (c2 & 0xf0) | ptk_table[c1][0];
				pat[x + 1] = ptk_table[c1][1];
				pat[x + 2] = ((c2 << 4) & 0xf0) | c3;
				pat[x + 3] = c4;
			}
		}
		fwrite(pat, 1024, 1, out);
	}

	/* sample data */
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_skyt(uint8 *data, int s)
{
	int start = 0;
	int i;

	PW_REQUEST_DATA(s, 8 * 31 + 12);

	/* test 2 */
	for (i = 0; i < 31; i++) {
		if (data[start + 8 * i + 4] > 0x40)
			return -1;
	}

	if (readmem32b(data + start + 256) != MAGIC4('S','K','Y','T'))
		return -1;

	return 0;
}
