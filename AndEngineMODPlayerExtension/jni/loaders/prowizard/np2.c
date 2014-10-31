/*
 * NoisePacker_v2.c   Copyright (C) 1997 Asle / ReDoX
 *                    Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Converts NoisePacked MODs back to ptk
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_np2 (uint8 *, int);
static int depack_np2 (FILE *, FILE *);

struct pw_format pw_np2 = {
	"NP2",
	"Noisepacker v2",
	0x00,
	test_np2,
	depack_np2
};

static int depack_np2(FILE *in, FILE *out)
{
	uint8 tmp[1024];
	uint8 c1, c2, c3, c4;
	uint8 ptable[128];
	int len, nins, npat;
	int max_addr;
	int size, ssize = 0;
	int tsize;
	int trk_addr[128][4];
	int i, j, k;
	int trk_start;

	memset(ptable, 0, 128);
	memset(trk_addr, 0, 128 * 4 * 4);

	c1 = read8(in);			/* read number of samples */
	c2 = read8(in);
	nins = ((c1 << 4) & 0xf0) | ((c2 >> 4) & 0x0f);

	pw_write_zero(out, 20);		/* write title */

	read8(in);
	len = read8(in) / 2;		/* read size of pattern list */
	read16b(in);			/* 2 unknown bytes */
	tsize = read16b(in);		/* read track data size */

	/* read sample descriptions */
	for (i = 0; i < nins; i++) {
		read32b(in);			/* bypass 4 unknown bytes */
		pw_write_zero(out, 22);		/* sample name */
		write16b(out, size = read16b(in));	/* size */
		ssize += size * 2;
		write8(out, read8(in));		/* finetune */
		write8(out, read8(in));		/* volume */
		read32b(in);			/* bypass 4 unknown bytes */
		size = read16b(in);		/* read loop size */
		write16b(out, read16b(in));	/* loop start */
		write16b(out, size);		/* write loop size */
	}

	/* fill up to 31 samples */
	memset(tmp, 0, 30);
	tmp[29] = 0x01;
	for (; i < 31; i++)
		fwrite(tmp, 30, 1, out);

	write8(out, len);		/* write size of pattern list */
	write8(out, 0x7f);		/* write noisetracker byte */

	fseek(in, 2, SEEK_CUR);		/* always $02? */
	fseek(in, 2, SEEK_CUR);		/* unknown */

	/* read pattern table */
	for (npat = i = 0; i < len; i++) {
		ptable[i] = read16b(in) / 8;
		if (ptable[i] > npat)
			npat = ptable[i];
	}
	npat++;

	fwrite(ptable, 128, 1, out);	/* write pattern table */
	write32b(out, PW_MOD_MAGIC);	/* write ptk ID */

	/* read tracks addresses per pattern */
	for (max_addr = i = 0; i < npat; i++) {
		if ((trk_addr[i][0] = read16b(in)) > max_addr)
			max_addr = trk_addr[i][0];
		if ((trk_addr[i][1] = read16b(in)) > max_addr)
			max_addr = trk_addr[i][1];
		if ((trk_addr[i][2] = read16b(in)) > max_addr)
			max_addr = trk_addr[i][2];
		if ((trk_addr[i][3] = read16b(in)) > max_addr)
			max_addr = trk_addr[i][3];
	}
	trk_start = ftell(in);

	/* the track data now ... */
	for (i = 0; i < npat; i++) {
		memset(tmp, 0, 1024);
		for (j = 0; j < 4; j++) {
			fseek(in, trk_start + trk_addr[i][3 - j], SEEK_SET);
			for (k = 0; k < 64; k++) {
				int x = k * 16 + j * 4;

				c1 = read8(in);
				c2 = read8(in);
				c3 = read8(in);
				c4 = (c1 & 0xfe) / 2;

				tmp[x] = ((c1 << 4) & 0x10) | ptk_table[c4][0];
				tmp[x + 1] = ptk_table[c4][1];

				switch (c2 & 0x0f) {
				case 0x08:
					c2 &= 0xf0;
					break;
				case 0x07:
					c2 = (c2 & 0xf0) + 0x0a;
					/* fall through */
				case 0x06:
				case 0x05:
					c3 = c3 > 0x80 ? 0x100 - c3 :
							(c3 << 4) & 0xf0;
					break;
				case 0x0e:
					c3--;
					break;
				case 0x0b:
					c3 = (c3 + 4) / 2;
					break;
				}
				tmp[x + 2] = c2;
				tmp[x + 3] = c3;
			}
		}
		fwrite(tmp, 1024, 1, out);
	}

	/* sample data */
	fseek(in, max_addr + 192 + trk_start, SEEK_SET);
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_np2(uint8 *data, int s)
{
	int j, k, l, o, m, n;
	int start, ssize;

	PW_REQUEST_DATA (s, 1024);

#if 0
	if (i < 15) {
		Test = BAD;
		return;
	}
#endif

	start = 0;

	/* j is the size of the pattern list (*2) */
	j = (data[start + 2] << 8) + data[start + 3];
	if (j & 1 || j == 0)
		return - 1;

	/* test nbr of samples */
	if ((data[start + 1] & 0x0f) != 0x0C)
		return -1;

	/* l is the number of samples */
	l = ((data[start] << 4) & 0xf0) | ((data[start + 1] >> 4) & 0x0f);
	if (l > 0x1f || l == 0)
		return -1;

	/* test volumes */
	for (k = 0; k < l; k++) {
		if (data[start + 15 + k * 16] > 0x40)
			return -1;
	}

	/* test sample sizes */
	ssize = 0;
	for (k = 0; k < l; k++) {
		int x = start + k * 16;

		o = 2 * ((data[x + 12] << 8) + data[x + 13]);
		m = 2 * ((data[x + 20] << 8) + data[x + 21]);
		n = 2 * ((data[x + 22] << 8) + data[x + 23]);

		if (o > 0xffff || m > 0xffff || n > 0xffff)
			return -1;

		if ((m + n) > (o + 2))
			return -1;

		if (n != 0 && m == 0)
			return -1;

		ssize += o;
	}

	if (ssize <= 4)
		return -1;

	l *= 16;
	l += 8 + 4;
	/* l is now the size of the header 'til the end of sample descriptions */

	/* test pattern table */
	n = 0;
	for (k = 0; k < j; k += 2) {
		m = ((data[start + l + k] << 8) + data[start + l + k + 1]);
		if (((m / 8) * 8) != m)
			return -1;
		if (m > n)
			n = m;
	}

	l += j + n + 8;

	/* paske on a que l'address du dernier pattern... */
	/* l is now the size of the header 'til the end of the track list */
	/* n is the highest pattern number (*8) */

	/* test track data size */
	k = (data[start + 6] << 8) + data[start + 7];
	if (k < 192 || ((k / 192) * 192) != k)
		return -1;

	PW_REQUEST_DATA (s, k + l + 16);

	/* test notes */
	j = ((data[start] << 4) & 0xf0) | ((data[start + 1] >> 4) & 0x0f);
	for (m = 0; m < k; m += 3) {
		if (data[start + l + m] > 0x49) {
			printf ("Fail 1 on m = %d\n", m);
			return -1;
		}

		if ((((data[start + l + m] << 4) & 0x10) |
			((data[start + l + m + 1] >> 4) & 0x0f)) > j) {
			printf ("Fail 2 on m = %d", m);
			return -1;
		}

		n = (data[start + l + m + 1] & 0x0F);
		if (n == 0 && data[start + l + m + 2] != 0x00) {
			printf ("Fail 3 on m = %d", m);
			return -1;
		}
	}

	/* ssize is the size of the sample data */
	/* l is the size of the header 'til the track datas */
	/* k is the size of the track datas */

	return 0;
}
