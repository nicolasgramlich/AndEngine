/*
 * ProRunner2.c   Copyright (C) 1996-1999 Asle / ReDoX
 *                Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Converts ProRunner v2 packed MODs back to Protracker
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_pru2 (uint8 *, int);
static int depack_pru2 (FILE *, FILE *);


struct pw_format pw_pru2 = {
	"PRU2",
	"Prorunner 2.0",
	0x00,
	test_pru2,
	depack_pru2
};


static int depack_pru2(FILE *in, FILE *out)
{
	uint8 header[2048];
	uint8 c1, c2, c3, c4;
	uint8 npat;
	uint8 ptable[128];
	uint8 max = 0;
	uint8 v[4][4];
	int size, ssize = 0;
	int i, j;

	memset(header, 0, 2048);
	memset(ptable, 0, 128);

	pw_write_zero(out, 20);				/* title */

	fseek(in, 8, SEEK_SET);

	for (i = 0; i < 31; i++) {
		pw_write_zero(out, 22);			/*sample name */
		write16b(out, size = read16b(in));	/* size */
		ssize += size * 2;
		write8(out, read8(in));			/* finetune */
		write8(out, read8(in));			/* volume */
		write16b(out, read16b(in));		/* loop start */
		write16b(out, read16b(in));		/* loop size */
	}

	write8(out, npat = read8(in));			/* number of patterns */
	write8(out, read8(in));				/* noisetracker byte */

	for (i = 0; i < 128; i++) {
		write8(out, c1 = read8(in));
		max = (c1 > max) ? c1 : max;
	}

	write32b(out, PW_MOD_MAGIC);

	/* pattern data stuff */
	fseek(in, 770, SEEK_SET);

	for (i = 0; i <= max; i++) {
		for (j = 0; j < 256; j++) {
			c1 = c2 = c3 = c4 = 0;
			header[0] = read8(in);
			if (header[0] == 0x80) {
				write32b(out, 0);
			} else if (header[0] == 0xC0) {
				fwrite(v[0], 4, 1, out);
				c1 = v[0][0];
				c2 = v[0][1];
				c3 = v[0][2];
				c4 = v[0][3];
			} else if (header[0] != 0xC0 && header[0] != 0xC0) {
				header[1] = read8(in);
				header[2] = read8(in);

				c1 = (header[1] & 0x80) >> 3;
				c1 |= ptk_table[(header[0] >> 1)][0];
				c2 = ptk_table[(header[0] >> 1)][1];
				c3 = (header[1] & 0x70) << 1;
				c3 |= (header[0] & 0x01) << 4;
				c3 |= (header[1] & 0x0f);
				c4 = header[2];

				write8(out, c1);
				write8(out, c2);
				write8(out, c3);
				write8(out, c4);
			}

			/* rol previous values */
			v[0][0] = v[1][0];
			v[0][1] = v[1][1];
			v[0][2] = v[1][2];
			v[0][3] = v[1][3];

			v[1][0] = v[2][0];
			v[1][1] = v[2][1];
			v[1][2] = v[2][2];
			v[1][3] = v[2][3];

			v[2][0] = v[3][0];
			v[2][1] = v[3][1];
			v[2][2] = v[3][2];
			v[2][3] = v[3][3];

			v[3][0] = c1;
			v[3][1] = c2;
			v[3][2] = c3;
			v[3][3] = c4;
		}
	}

	/* sample data */
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_pru2 (uint8 *data, int s)
{
	int k;
	int start = 0;

	PW_REQUEST_DATA(s, 12 + 31 * 8);

	if (data[0]!='S' || data[1]!='N' || data[2]!='T' || data[3]!='!')
		return -1;

#if 0
	/* check sample address */
	j = (data[i + 4] << 24) + (data[i + 5] << 16) + (data[i + 6] << 8) +
		data[i + 7];

	PW_REQUEST_DATA (s, j);
#endif

	/* test volumes */
	for (k = 0; k < 31; k++) {
		if (data[start + 11 + k * 8] > 0x40)
			return -1;
	}

	/* test finetunes */
	for (k = 0; k < 31; k++) {
		if (data[start + 10 + k * 8] > 0x0F)
			return -1;
	}

	return 0;
}
