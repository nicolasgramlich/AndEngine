/*
 * TitanicsPlayer.c  Copyright (C) 2007 Sylvain "Asle" Chipaux
 * xmp version Copyright (C) 2009 Claudio Matsuoka
 */

/*
 * Titan Trax vol. 1: http://www.youtube.com/watch?v=blgm0EcPUd8
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_titanics (uint8 *, int);
static int depack_titanics (FILE *, FILE *);

struct pw_format pw_titanics = {
	"TIT",
	"Titanics Player",
	0x00,
	test_titanics,
	depack_titanics
};

/* With the help of Xigh :) .. thx */
static int cmplong(const void *a, const void *b)
{
	return *(int *)a == *(int *)b ? 0 : *(int *)a > *(int *)b ? 1 : -1;
}


static int depack_titanics(FILE *in, FILE *out)
{
	uint8 buf[1024];
	long pat_addr[128];
	long pat_addr_ord[128];
	long pat_addr_final[128];
	long max = 0l;
	uint8 pat;
	uint32 smp_addr[15];
	uint16 smp_size[15];
	int i, j, k;

	for (i = 0; i < 128; i++)
		pat_addr[i] = pat_addr_ord[i] = pat_addr_final[i] = 0;

	pw_write_zero(out, 20);			/* write title */

	for (i = 0; i < 15; i++) {
		smp_addr[i] = read32b(in);
		pw_write_zero(out, 22);		/* write name */
		write16b(out, smp_size[i] = read16b(in));
		smp_size[i] *= 2;
		write8(out, read8(in));		/* finetune */
		write8(out, read8(in));		/* volume */
		write16b(out, read16b(in));	/* loop start */
		write16b(out, read16b(in));	/* loop size */
	}
	for (i = 15; i < 31; i++) {
		pw_write_zero(out, 22);		/* write name */
		write16b(out, 0);		/* sample size */
		write8(out, 0);			/* finetune */
		write8(out, 0x40);		/* volume */
		write16b(out, 0);		/* loop start */
		write16b(out, 1);		/* loop size */
	}

	/* pattern list */
	fread(buf, 2, 128, in);
	for (pat = 0; pat < 128; pat++) {
		if (buf[pat * 2] == 0xff)
			break;
		pat_addr_ord[pat] = pat_addr[pat] = readmem16b(buf + pat * 2);
	}

	write8(out, pat);		/* patterns */
	write8(out, 0x7f);		/* write ntk byte */

	/* With the help of Xigh :) .. thx */
	qsort(pat_addr_ord, pat, sizeof(long), cmplong);

	for (j = i = 0; i < pat; i++) {
		pat_addr_final[j++] = pat_addr_ord[i];
		while (pat_addr_ord[i + 1] == pat_addr_ord[i] && i < pat)
			i++;
	}

	memset(buf, 0, 128);

	/* write pattern list */
	for (i = 0; i < pat; i++) {
		for (j = 0; pat_addr[i] != pat_addr_final[j]; j++) ;
		buf[i] = j;
		if (j > max)
			max = j;
	}
	fwrite(buf, 128, 1, out);
	write32b(out, PW_MOD_MAGIC);	/* write M.K. */

	/* pattern data */
	for (i = 0; i <= max; i++) {
		uint8 x, y, c;
		int note;

		fseek(in, pat_addr_final[i], SEEK_SET);

		memset(buf, 0, 1024);
		x = read8(in);

		for (k = 0; k < 64; ) {			/* row number */
			y = read8(in);
			c = (y >> 6) * 4;		/* channel */

			note = y & 0x3f;

			if (note <= 36) {
				buf[k * 16 + c] = ptk_table[note][0];
				buf[k * 16 + c + 1] = ptk_table[note][1];
			}
			buf[k * 16 + c + 2] = read8(in);
			buf[k * 16 + c + 3] = read8(in);

			if (x & 0x80)
				break;

			/* next event */
			x = read8(in);
			k += x & 0x7f;
		}

		fwrite(&buf[0], 1024, 1, out);
	}

	/* sample data */
	for (i = 0; i < 15; i++) {
		if (smp_addr[i]) {
			fseek(in, smp_addr[i], SEEK_SET);
			pw_move_data(out, in, smp_size[i]);
		}
	}

	return 0;
}


static int test_titanics(uint8 *data, int s)
{
	int j, k, l, m, n, o;
	int start = 0, ssize;

	PW_REQUEST_DATA(s, 182);

	/* test samples */
	n = ssize = 0;
	for (k = 0; k < 15; k++) {
		if (data[start + 7 + k * 12] > 0x40)
			return -1;
			
		if (data[start + 6 + k * 12] != 0x00)
			return -1;

		o = readmem32b(data + start + k * 12);
		if (/*o > in_size ||*/ (o < 180 && o != 0))
			return -1;

		j = readmem16b(data + start + k * 12 + 4);	/* size */
		l = readmem16b(data + start + k * 12 + 8);	/* loop start */
		m = readmem16b(data + start + k * 12 + 10);	/* loop size */

		if (l > j || m > (j + 1) || j > 32768)
			return -1;

		if (m == 0)
			return -1;

		if (j == 0 && (l != 0 || m != 1))
			return -1;

		ssize += j;
	}

	if (ssize < 2)
		return -1;

	/* test pattern addresses */
	o = -1;
	for (l = k = 0; k < 256; k += 2) {
		if (readmem16b(data + start + k + 180) == 0xffff) {
			o = 0;
			break;
		}

		j = readmem16b(data + start + k + 180);
		if (j < 180)
			return -1;

		if (j > l)
			l = j;
	}

	if (o == -1)
		return -1;

	/* l is the max addr of the pattern addrs */
	return 0;
}
