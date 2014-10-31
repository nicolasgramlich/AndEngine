/*
 * NoisePacker_v3.c   Copyright (C) 1998 Asle / ReDoX
 *                    Modified by Claudio Matsuoka
 *
 * Converts NoisePacked MODs back to ptk
 * Last revision : 26/11/1999 by Sylvain "Asle" Chipaux
 *                 reduced to only one FREAD.
 *                 Speed-up and Binary smaller.
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int test_np3(uint8 *, int);
static int depack_np3(FILE *, FILE *);

struct pw_format pw_np3 = {
	"NP3",
	"Noisepacker v3",
	0x00,
	test_np3,
	depack_np3
};

static int depack_np3(FILE *in, FILE *out)
{
	uint8 tmp[1024];
	uint8 c1, c2, c3, c4;
	uint8 ptable[128];
	int len, nins, npat;
	int max_addr, smp_addr = 0;
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
		fread(tmp, 1, 16, in);
		pw_write_zero(out, 22);		/* sample name */
		write16b(out, size = readmem16b(tmp + 6));
		ssize += size * 2;
		write8(out, tmp[0]);		/* write finetune */
		write8(out, tmp[1]);		/* write volume */
		fwrite(tmp + 14, 2, 1, out);	/* write loop start */
		fwrite(tmp + 12, 2, 1, out);	/* write loop size */
	}

	/* fill up to 31 samples */
	memset(tmp, 0, 30);
	tmp[29] = 0x01;
	for (; i < 31; i++)
		fwrite(tmp, 30, 1, out);

	write8(out, len);		/* write size of pattern list */
	write8(out, 0x7f);		/* write noisetracker byte */

	read16b(in);	/* bypass 2 bytes ... seems always the same as in $02 */
	read16b(in);	/* bypass 2 other bytes which meaning is beside me */

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

				if ((c1 = read8(in)) >= 0x80) {
					k += (0x100 - c1) - 1;
					continue;
				}
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
					c3 = 1;
					break;
				case 0x0b:
					c3 = (c3 + 4) / 2;
					break;
				}

				tmp[x + 2] = c2;
				tmp[x + 3] = c3;

				if ((c2 & 0x0f) == 0x0d)
					break;
			}

			if (ftell(in) > smp_addr)
				smp_addr = ftell(in);
		}
		fwrite(tmp, 1024, 1, out);
	}

	if (smp_addr & 1)
		smp_addr++;
	fseek(in, smp_addr, SEEK_SET);
	pw_move_data(out, in, ssize);

	return 0;
}

static int test_np3(uint8 *data, int s)
{
	int j, k, l, m, n, o;
	int start = 0, ssize;

	PW_REQUEST_DATA(s, 10);

	/* size of the pattern table */
	j = readmem16b(data + start + 2);
	if (j & 0x01 || j == 0)
		return -1;

	/* test nbr of samples */
	if ((data[start + 1] & 0x0f) != 0x0C)
		return -1;

	l = ((data[start] << 4) & 0xf0) | ((data[start + 1] >> 4) & 0x0f);
	if (l > 0x1F || l == 0)
		return -1;
	/* l is the number of samples */

	/* test volumes */
	for (k = 0; k < l; k++) {
		if (data[start + 9 + k * 16] > 0x40)
			return -1;
	}

	/* test sample sizes */
	ssize = 0;
	for (k = 0; k < l; k++) {
		o = readmem16b(data + start + k * 16 + 14) * 2;
		m = readmem16b(data + start + k * 16 + 20) * 2;
		n = readmem16b(data + start + k * 16 + 22) * 2;

		if (o > 0xFFFF || m > 0xFFFF || n > 0xFFFF)
			return -1;

		if ((m + n) > (o + 2))
			return -1;

		if (n != 0 && m == 0)
			return -1;

		ssize += o;
	}

	if (ssize <= 4)
		return -1;

	l = l * 16 + 8 + 4;
	/* l is the size of the header 'til the end of sample descriptions */

	/* test pattern table */
	n = 0;
	for (k = 0; k < j; k += 2) {
		m = readmem16b(data + start + l + k);
		if (m & 0x07)
			return -1;
		if (m > n)
			n = m;
	}
	l += j + n + 8;	/* paske on a que l'address du dernier pattern .. */
	/* l is now the size of the header 'til the end of the track list */
	/* j is now available for use :) */
	/* n is the highest pattern number (*8) */

	/* test track data size */
	k = readmem16b(data + start + 6);
	if (k <= 63)
		return -1;

	PW_REQUEST_DATA(s, start + l + k);

	/* test notes */
	/* re-calculate the number of sample */
	/* k is the track data size */
	j = ((data[start] << 4) & 0xf0) | ((data[start + 1] >> 4) & 0x0f);
	for (m = 0; m < k; m++) {
		if (data[start + l + m] & 0x80)
			continue;

		/* si note trop grande et si effet = A */
		if ((data[start + l + m] > 0x49) ||
		    ((data[start + l + m + 1] & 0x0f) == 0x0A))
			return -1;

		/* si effet D et arg > 0x40 */
		if (((data[start + l + m + 1] & 0x0f) == 0x0D)
		    && (data[start + l + m + 2] > 0x40))
			return -1;

		/* sample nbr > ce qui est defini au debut ? */
		if ((((data[start + l + m] << 4) & 0x10) |
		     ((data[start + l + m + 1] >> 4) & 0x0f)) > j)
			return -1;

		/* all is empty ?!? ... cannot be ! */
		if (data[start + l + m] == 0 && data[start + l + m + 1] == 0 &&
		    data[start + l + m + 2] == 0 && m < (k - 3))
			return -1;

		m += 2;
	}

	return 0;
}
