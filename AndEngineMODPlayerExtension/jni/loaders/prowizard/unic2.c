/*
 * Unic_Tracker_2.c   Copyright (C) 1997 Asle / ReDoX
 *                    Copyright 2006-2007 Claudio Matsuoka
 *
 * Convert Unic Tracker 2 MODs to Protracker
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_unic2 (uint8 *, int);
static int depack_unic2 (FILE *, FILE *);

struct pw_format pw_unic2 = {
	"UNIC2",
	"Unic Tracker 2",
	0x00,
	test_unic2,
	depack_unic2
};


#define ON 1
#define OFF 2

static int depack_unic2(FILE *in, FILE *out)
{
	uint8 c1, c2, c3, c4;
	uint8 npat, maxpat;
	uint8 ins, note, fxt, fxp;
	uint8 fine = 0;
	uint8 tmp[1025];
	uint8 loop_status = OFF;	/* standard /2 */
	int i, j, k, l;
	int ssize = 0;

	pw_write_zero(out, 20);		/* title */

	for (i = 0; i < 31; i++) {
		pw_move_data(out, in, 20);	/* sample name */
		write8(out, 0);
		write8(out, 0);

		/* fine on ? */
		c1 = read8(in);
		c2 = read8(in);
		j = (c1 << 8) + c2;
		if (j != 0) {
			if (j < 256)
				fine = 0x10 - c2;
			else
				fine = 0x100 - c2;
		} else {
			fine = 0;
		}

		/* smp size */
		write16b(out, l = read16b(in));
		ssize += l * 2;

		read8(in);
		write8(out, fine);		/* fine */
		write8(out, read8(in));		/* vol */

		j = read16b(in);		/* loop start */
		k = read16b(in);		/* loop size */

		if ((((j * 2) + k) <= l) && (j != 0)) {
			loop_status = ON;
			j *= 2;
		}

		write16b(out, j);
		write16b(out, k);
	}

	write8(out, npat = read8(in));		/* number of pattern */
	write8(out, 0x7f);			/* noisetracker byte */
	read8(in);

	fread(tmp, 128, 1, in);
	fwrite(tmp, 128, 1, out);		/* pat table */

	/* get highest pattern number */
	for (maxpat = i = 0; i < 128; i++) {
		if (tmp[i] > maxpat)
			maxpat = tmp[i];
	}
	maxpat++;		/* coz first is $00 */

	write32b(out, PW_MOD_MAGIC);

	/* pattern data */
	for (i = 0; i < maxpat; i++) {
		for (j = 0; j < 256; j++) {
			c1 = read8(in);
			c2 = read8(in);
			c3 = read8(in);

			ins = ((c1 >> 2) & 0x10) | ((c2 >> 4) & 0x0f);
			note = c1 & 0x3f;
			fxt = c2 & 0x0f;
			fxp = c3;

			if (fxt == 0x0d) {	/* pattern break */
				c4 = fxp % 10;
				c3 = fxp / 10;
				fxp = 16 * c3 + c4;
			}

			tmp[j * 4] = (ins & 0xf0);
			tmp[j * 4] |= ptk_table[note][0];
			tmp[j * 4 + 1] = ptk_table[note][1];
			tmp[j * 4 + 2] = ((ins << 4) & 0xf0) | fxt;
			tmp[j * 4 + 3] = fxp;
		}
		fwrite(tmp, 1024, 1, out);
	}

	/* sample data */
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_unic2 (uint8 *data, int s)
{
	int j, k, l, m, n, o;
	int start = 0, ssize;

	/* test 1 */
	PW_REQUEST_DATA (s, 1084);

	/* test #2 ID = $00000000 ? */
	if (readmem32b(data + start + 1080) == 0x00000000)
		return -1;

	/* test 2,5 :) */
	o = 0;
	ssize = 0;
	for (k = 0; k < 31; k++) {
		int x = start + k * 30;

		j = readmem16b(data + x + 22) * 2;
		m = readmem16b(data + x + 26) * 2;
		n = readmem16b(data + x + 28) * 2;
		ssize += j;

		if (j + 2 < m + n)
			return -1;

		if (j > 0xffff || m > 0xffff || n > 0xffff)
			return -1;

		if (data[x + 25] > 0x40)
			return -1;

		if (readmem16b(data + x + 20) && j == 0)
			return -1;

		if (data[x + 25] != 0 && j == 0)
			return -1;

		/* get the highest !0 sample */
		if (j != 0)
			o = j + 1;
	}

	if (ssize <= 2)
		return -1;

	/* test #4  pattern list size */
	l = data[start + 930];
	if (l > 127 || l == 0)
		return -1;
	/* l holds the size of the pattern list */

	k = 0;
	for (j = 0; j < l; j++) {
		if (data[start + 932 + j] > k)
			k = data[start + 932 + j];
		if (data[start + 932 + j] > 127)
			return -1;
	}
	/* k holds the highest pattern number */

	/* test last patterns of the pattern list = 0 ? */
	j += 2;		/* just to be sure .. */
	while (j != 128) {
		if (data[start + 932 + j] != 0)
			return -1;
		j += 1;
	}
	/* k is the number of pattern in the file (-1) */
	k += 1;

#if 0
	/* test #5 pattern data ... */
	if (((k * 768) + 1060 + start) > in_size) {
/*printf ( "#5,0 (Start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}
#endif

	PW_REQUEST_DATA (s, 1060 + k * 256 * 3 + 2);

	for (j = 0; j < (k << 8); j++) {
		int y = start + 1060 + j * 3;

		/* relative note number + last bit of sample > $34 ? */
		if (data[y] > 0x74)
			return -1;
		if ((data[y] & 0x3F) > 0x24)
			return -1;
		if ((data[y + 1] & 0x0F) == 0x0C && data[y + 2] > 0x40)
			return -1;
		if ((data[y + 1] & 0x0F) == 0x0B && data[y + 2] > 0x7F)
			return -1;
		if ((data[y + 1] & 0x0F) == 0x0D && data[y + 2] > 0x40)
			return -1;

		n = ((data[y] >> 2) & 0x30) | ((data[y + 2] >> 4) & 0x0f);

		if (n > o)
			return -1;
	}

	return 0;
}
