/*
 * Digital_Illusion.c   Copyright (C) 1997 Asle / ReDoX
 *			Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Converts DI packed MODs back to PTK MODs
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_di (uint8 *, int);
static int depack_di (FILE *, FILE *);

struct pw_format pw_di = {
	"DI",
	"Digital Illusions",
	0x00,
	test_di,
	depack_di
};


static int depack_di(FILE * in, FILE * out)
{
	uint8 c1, c2, c3;
	uint8 note, ins, fxt, fxp;
	uint8 ptk_tab[5];
	uint8 nins, npat, max;
	uint8 ptable[128];
	uint16 paddr[128];
	uint8 tmp[50];
	int i, k;
	int seq_offs, pat_offs, smp_offs;
	int pos;
	int size, ssize = 0;

	memset(ptable, 0, 128);
	memset(ptk_tab, 0, 5);
	memset(paddr, 0, 128);

	pw_write_zero(out, 20);			/* title */

	nins = read16b(in);
	seq_offs = read32b(in);
	pat_offs = read32b(in);
	smp_offs = read32b(in);

	for (i = 0; i < nins; i++) {
		pw_write_zero(out, 22);			/* name */
		write16b(out, size = read16b(in));	/* size */
		ssize += size * 2;
		write8(out, read8(in));			/* finetune */
		write8(out, read8(in));			/* volume */
		write16b(out, read16b(in));		/* loop start */
		write16b(out, read16b(in));		/* loop size */
	}

	memset(tmp, 0, 50);
	for (i = nins; i < 31; i++)
		fwrite(tmp, 30, 1, out);

	pos = ftell(in);
	fseek (in, seq_offs, 0);

	i = 0;
	do {
		c1 = read8(in);
		ptable[i++] = c1;
	} while (c1 != 0xff);

	ptable[i - 1] = 0;
	write8(out, npat = i - 1);

	write8(out, 0x7f);

	for (max = i = 0; i < 128; i++) {
		write8(out, ptable[i]);
		if (ptable[i] > max)
			max = ptable[i];
	}

	write32b(out, PW_MOD_MAGIC);

	fseek(in, pos, 0);
	for (i = 0; i <= max; i++)
		paddr[i] = read16b(in);

	for (i = 0; i <= max; i++) {
		fseek(in, paddr[i], 0);
		for (k = 0; k < 256; k++) {	/* 256 = 4 voices * 64 rows */
			memset(ptk_tab, 0, 5);
			c1 = read8(in);
			if ((c1 & 0x80) == 0) {
				c2 = read8(in);
				note = ((c1 << 4) & 0x30) | ((c2 >> 4) & 0x0f);
				ptk_tab[0] = ptk_table[note][0];
				ptk_tab[1] = ptk_table[note][1];
				ins = (c1 >> 2) & 0x1f;
				ptk_tab[0] |= (ins & 0xf0);
				ptk_tab[2] = (ins << 4) & 0xf0;
				fxt = c2 & 0x0f;
				ptk_tab[2] |= fxt;
				fxp = 0x00;
				ptk_tab[3] = fxp;
				fwrite (ptk_tab, 4, 1, out);
				continue;
			}
			if (c1 == 0xff) {
				memset(ptk_tab, 0, 5);
				fwrite (ptk_tab, 4, 1, out);
				continue;
			}
			c2 = read8(in);
			c3 = read8(in);
			note = (((c1 << 4) & 0x30) | ((c2 >> 4) & 0x0f));
			ptk_tab[0] = ptk_table[note][0];
			ptk_tab[1] = ptk_table[note][1];
			ins = (c1 >> 2) & 0x1f;
			ptk_tab[0] |= (ins & 0xf0);
			ptk_tab[2] = (ins << 4) & 0xf0;
			fxt = c2 & 0x0f;
			ptk_tab[2] |= fxt;
			fxp = c3;
			ptk_tab[3] = fxp;
			fwrite(ptk_tab, 4, 1, out);
		}
	}

	fseek(in, smp_offs, 0);
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_di (uint8 *data, int s)
{
	int ssize, start = 0;
	int j, k, l, m, n, o;

	PW_REQUEST_DATA (s, 21);

#if 0
	/* test #1 */
	if (i < 17) {
		Test = BAD;
		return;
	}
#endif

	/* test #2  (number of sample) */
	k = readmem16b(data + start);
	if (k > 31)
		return -1;

	/* test #3 (finetunes and whole sample size) */
	/* k = number of samples */
	l = 0;
	for (j = 0; j < k; j++) {
		o = readmem16b(data + start + 14) * 2;
		m = readmem16b(data + start + 18) * 2;
		n = readmem16b(data + start + 20) * 2;

		if (o > 0xffff || m > 0xffff || n > 0xffff)
			return -1;

		if (m + n > o)
			return -1;

		if (data[start + 16 + j * 8] > 0x0f)
			return -1;

		if (data[start + 17 + j * 8] > 0x40)
			return -1;

		/* get total size of samples */
		l += o;
	}
	if (l <= 2)
		return -1;

	/* test #4 (addresses of pattern in file ... ptk_tableible ?) */
	/* k is still the number of sample */

	ssize = k * 8 + 2;

	j = readmem32b(data + start + 2);	/* address of pattern table */
	k = readmem32b(data + start + 6);	/* address of pattern data */
	l = readmem32b(data + start + 10);	/* address of sample data */

	if (k <= j || l <= j || l <= k)
		return -1;

	if (k - j > 128)
		return -1;

#if 0
	if (k > in_size || l > in_size || l > in_size)
		return -1;
#endif

	/* test #4,1 :) */
	if (j < ssize)
		return -1;

#if 0
	/* test #5 */
	if ((k + start) > in_size) {
		Test = BAD;
		return;
	}
#endif

	PW_REQUEST_DATA (s, start + k - 1);

	/* test pattern table reliability */
	for (m = j; m < (k - 1); m++) {
		if (data[start + m] > 0x80)
			return -1;
	}

	/* test #6  ($FF at the end of pattern list ?) */
	if (data[start + k - 1] != 0xFF)
		return -1;

	/* test #7 (addres of sample data > $FFFF ? ) */
	/* l is still the address of the sample data */
	if (l > 65535)
		return -1;

	return 0;
}
