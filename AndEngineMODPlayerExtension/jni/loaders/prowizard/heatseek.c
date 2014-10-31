/*
 * Heatseeker_mc1.0.c   Copyright (C) 1997 Asle / ReDoX
 *			Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Converts back to ptk Heatseeker packed MODs
 *
 * Asle's note: There's a good job ! .. gosh !.
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int test_crb (uint8 *, int);
static int depack_crb (FILE *, FILE *);

struct pw_format pw_crb = {
	"CRB",
	"Heatseeker 1.0",
	0x00,
	test_crb,
	depack_crb
};


static int depack_crb (FILE *in, FILE *out)
{
	uint8 c1, c2, c3, c4;
	uint8 ptable[128];
	uint8 pat_pos, pat_max;
	uint8 pat[1024];
	int taddr[512];
	int i, j, k, l, m;
	int size, ssize = 0;

	memset(ptable, 0, 128);
	memset(taddr, 0, 512 * 4);

	pw_write_zero(out, 20);				/* write title */

	/* read and write sample descriptions */
	for (i = 0; i < 31; i++) {
		pw_write_zero(out, 22);			/*sample name */
		write16b(out, size = read16b(in));	/* size */
		ssize += size * 2;
		write8(out, read8(in));			/* finetune */
		write8(out, read8(in));			/* volume */
		write16b(out, read16b(in));		/* loop start */
		size = read16b(in);			/* loop size */
		write16b(out, size ? size : 1);
	}

	write8(out, pat_pos = read8(in));		/* pat table length */
	write8(out, read8(in)); 			/* NoiseTracker byte */

	/* read and write pattern list and get highest patt number */
	for (pat_max = i = 0; i < 128; i++) {
		write8(out, c1 = read8(in));
		if (c1 > pat_max)
			pat_max = c1;
	}
	pat_max++;

	/* write ptk's ID */
	write32b(out, PW_MOD_MAGIC);

	/* pattern data */
	for (i = 0; i < pat_max; i++) {
		memset(pat, 0, 1024);
		for (j = 0; j < 4; j++) {
			taddr[i * 4 + j] = ftell(in);
			for (k = 0; k < 64; k++) {
				int y = k * 16 + j * 4;

				c1 = read8(in);
				if (c1 == 0x80) {
					c2 = read8(in);
					c3 = read8(in);
					c4 = read8(in);
					k += c4;
					continue;
				}
				if (c1 == 0xc0) {
					c2 = read8(in);
					c3 = read8(in);
					c4 = read8(in);
					l = ftell(in);
					fseek(in, taddr[((c3 << 8) + c4) / 4],
								SEEK_SET);
					for (m = 0; m < 64; m++) {
						int x = m * 16 + j * 4;

						c1 = read8(in);
						if (c1 == 0x80) {
							c2 = read8(in);
							c3 = read8(in);
							c4 = read8(in);
							m += c4;
							continue;
						}
						pat[x] = c1;
						pat[x + 1] = read8(in);
						pat[x + 2] = read8(in);
						pat[x + 3] = read8(in);
					}
					fseek (in, l, 0);	/* SEEK_SET */
					k += 100;
					continue;
				}
				pat[y] = c1;
				pat[y + 1] = read8(in);
				pat[y + 2] = read8(in);
				pat[y + 3] = read8(in);
			}
		}
		fwrite (pat, 1024, 1, out);
	}

	/* sample data */
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_crb (uint8 *data, int s)
{
	int j, k, l, m, n, o;
	int start = 0, ssize;

	PW_REQUEST_DATA (s, 378);

	/* size of the pattern table */
	if (data[start + 248] > 0x7f || data[start + 248] == 0x00)
		return -1;

	/* test noisetracker byte */
	if (data[start + 249] != 0x7f)
		return -1;

	/* test samples */
	ssize = 0;
	for (k = 0; k < 31; k++) {
		if (data[start + 2 + k * 8] > 0x0f)
			return -1;

		/* test volumes */
		if (data[start + 3 + k * 8] > 0x40)
			return -1;

		j = readmem16b(data + start + k * 8) * 2;	/* size */
		m = readmem16b(data + start + k * 8 + 4) * 2;	/* loop start */
		n = readmem16b(data + start + k * 8 + 6) * 2;	/* loop size */

		if (j > 0xffff || m > 0xffff || n > 0xffff)
			return -1;

		/* n != 2 test added by claudio -- asle, please check! */
		if (n != 0 && n != 2 && (m + n) > j)
			return -1;

		if (m != 0 && n <= 2)
			return -1;

		ssize += j;
	}

/* printf ("3\n"); */
	if (ssize <= 4)
		return -1;

	/* test pattern table */
	l = 0;
	for (j = 0; j < 128; j++) {
		if (data[start + 250 + j] > 0x7f)
			return -1;
		if (data[start + 250 + j] > l)
			l = data[start + 250 + j];
	}

	/* FIXME */
	PW_REQUEST_DATA (s, 379 + 4 * l * 4 * 64);

	/* test notes */
	k = 0;
	j = 0;
	for (m = 0; m <= l; m++) {
		for (n = 0; n < 4; n++) {
			for (o = 0; o < 64; o++) {
				switch (data[start + 378 + j] & 0xC0) {
				case 0x00:
					if ((data[start + 378 + j] & 0x0F) > 0x03)
						return -1;
					k += 4;
					j += 4;
					break;
				case 0x80:
					if (data[start + 379 + j] != 0x00)
						return -1;
					o += data[start + 381 + j];
					j += 4;
					k += 4;
					break;
				case 0xC0:
					if (data[start + 379 + j] != 0x00)
						return -1;
					o = 100;
					j += 4;
					k += 4;
					break;
				default:
					break;
				}
			}
		}
	}

	/* k is the size of the pattern data */
	/* ssize is the size of the sample data */

	return 0;
}
