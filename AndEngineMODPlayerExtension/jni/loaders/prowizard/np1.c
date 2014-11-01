/*
 * NoisePacker_v1.c   Copyright (C) 1997 Asle / ReDoX
 *                    Modified by Claudio Matsuoka
 *
 * Converts NoisePacked MODs back to ptk
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_np1(uint8 *, int);
static int depack_np1(FILE *, FILE *);

struct pw_format pw_np1 = {
	"NP1",
	"NoisePacker v1",
	0x00,
	test_np1,
	depack_np1
};

static int depack_np1(FILE *in, FILE *out)
{
	uint8 tmp[1024];
	uint8 c1, c2, c3, c4;
	uint8 len;
	uint8 nins;
	uint8 ptable[128];
	uint8 npat = 0x00;
	int max_addr;
	int size, ssize = 0;
	int tsize;
	int taddr[128][4];
	int i = 0, j = 0, k;
	int tdata;

	memset(ptable, 0, 128);
	memset(taddr, 0, 128 * 4 * 4);

	/* read number of sample */
	c1 = read8(in);
	c2 = read8(in);
	nins = ((c1 << 4) & 0xf0) | ((c2 >> 4) & 0x0f);

	/* write title */
	pw_write_zero(out, 20);

	len = read16b(in) / 2;		/* size of pattern list */
	read16b(in);			/* 2 unknown bytes */
	tsize = read16b(in);		/* read track data size */

	/* read sample descriptions */
	for (i = 0; i < nins; i++) {
		read32b(in);			/* bypass 4 unknown bytes */
		pw_write_zero(out, 22);		/* sample name */
		write16b(out, size = read16b(in));	/* sample size */
		ssize += size * 2;
		write8(out, read8(in));		/* finetune */
		write8(out, read8(in));		/* volume */
		read32b(in);			/* bypass 4 unknown bytes */
		size = read16b(in);		/* read loop size */
		write16b(out, read16b(in) / 2);	/* loop start */
		write16b(out, size);		/* write loop size */
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
	npat = 0;
	for (i = 0; i < len; i++) {
		ptable[i] = read16b(in);
		if (ptable[i] > npat)
			npat = ptable[i];
	}
	npat++;

	fwrite(ptable, 128, 1, out);		/* write pattern table */
	write32b(out, PW_MOD_MAGIC);		/* write ptk ID */

	/* read tracks addresses per pattern */
	max_addr = 0;
	for (i = 0; i < npat; i++) {
		if ((taddr[i][0] = read16b(in)) > max_addr)
			max_addr = taddr[i][0];
		if ((taddr[i][1] = read16b(in)) > max_addr)
			max_addr = taddr[i][1];
		if ((taddr[i][2] = read16b(in)) > max_addr)
			max_addr = taddr[i][2];
		if ((taddr[i][3] = read16b(in)) > max_addr)
			max_addr = taddr[i][3];
	}
	tdata = ftell(in);

	/* the track data now ... */
	for (i = 0; i < npat; i++) {
		memset(tmp, 0, 1024);
		for (j = 0; j < 4; j++) {
			fseek(in, tdata + taddr[i][3 - j], 0);
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
				case 0x0B:
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
	fseek(in, max_addr + 192 + tdata, 0);
	pw_move_data(out, in, ssize);

	return 0;
}

static int test_np1(uint8 *data, int s)
{
	int j, k, l, m, n, o;
	int start = 0, ssize;

	/* size of the pattern table */
	j = readmem16b(data + start + 2);
	if (j % 2 || j == 0)
		return -1;

	/* test nbr of samples */
	if ((data[start + 1] & 0x0f) != 0x0C)
		return -1;

	l = ((data[start] << 4) & 0xf0) | ((data[start + 1] >> 4) & 0x0f);
	if (l > 0x1F || l == 0)
		return -1;
	/* l is the number of samples */

	PW_REQUEST_DATA(s, start + 15 + l * 16);

	/* test volumes */
	for (k = 0; k < l; k++) {
		if (data[start + 15 + k * 16] > 0x40)
			return -1;
	}

	/* test sample sizes */
	ssize = 0;
	for (k = 0; k < l; k++) {
		o = readmem16b(data + start + k * 16 + 12) * 2;
		m = readmem16b(data + start + k * 16 + 20) * 2;
		n = readmem16b(data + start + k * 16 + 22);

		if (o > 0xFFFF || m > 0xFFFF || n > 0xFFFF)
			return -1;

		if (m + n > o + 2)
			return -1;

		if (n != 0 && m == 0)
			return -1;

		ssize += o;
	}

	if (ssize <= 4)
		return -1;

	l = l * 16 + 8 + 4;
	/* l is the size of the header til the end of sample descriptions */

	/* test pattern table */
	n = 0;
	for (k = 0; k < j; k += 2) {
		m = readmem16b(data + start + l + k);
		if (m % 8)
			return -1;
		if (m > n)
			n = m;
	}

	l += j + n + 8;	/* paske on a que l'address du dernier pattern .. */
	/* l is now the size of the header 'til the end of the track list */

	/* test track data size */
	k = readmem16b(data + start + 6);
	if (k < 192 || k % 192)
		return -1;

	PW_REQUEST_DATA(s, start + l + k);

	/* test notes */
	for (m = 0; m < k; m += 3) {
		if (data[start + l + m] > 0x49)
			return -1;
	}

	return 0;
}
