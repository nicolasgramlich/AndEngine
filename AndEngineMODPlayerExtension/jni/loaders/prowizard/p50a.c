/*
 * The_Player_5.0a.c	Copyright (C) 1998 Sylvain "Asle" Chipaux
 *			Copyright (C) 2006-2008 Claudio Matsuoka
 *
 * The Player 5.0a to Protracker.
 *
 * note: It's a REAL mess !. It's VERY badly coded, I know. Just dont forget
 *      it was mainly done to test the description I made of P50a format. I
 *      certainly wont dare to beat Gryzor on the ground :). His Prowiz IS
 *      the converter to use !!!. Though, using the official depacker could
 *      be a good idea too :).
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int test_p50a (uint8 *, int);
static int depack_p50a (FILE *, FILE *);

struct pw_format pw_p50a = {
	"P50A",
	"The Player 5.0a",
	0x00,
	test_p50a,
	depack_p50a
};

#define ON 1
#define OFF 2

static int depack_p50a(FILE *in, FILE *out)
{
    uint8 c1, c2, c3, c4, c5, c6;
    int max_row;
    signed char *smp_buffer;
    uint8 PatPos = 0x00;
    uint8 npat = 0x00;
    uint8 nins = 0x00;
    uint8 tdata[512][256];
    uint8 ptable[128];
    uint8 GLOBAL_DELTA = OFF;
    int isize[31];
    int taddr[128][4];
    int tdata_addr = 0;
    int sdata_addr = 0;
    int ssize = 0;
    int i = 0, j, k, l, a, b;
    int smp_size[31];
    int saddr[32];
    int val;
    uint8 buf[1024];

    memset(taddr, 0, 128 * 4 * 4);
    memset(tdata, 0, 512 << 8);
    memset(ptable, 0, 128);
    memset(smp_size, 0, 31 * 4);
    memset(saddr, 0, 32 * 4);
    memset(isize, 0, 31 * sizeof(int));

    sdata_addr = read16b(in);		/* read sample data address */
    npat = read8(in);			/* read real number of patterns */
    nins = read8(in);			/* read number of samples */

    if (nins & 0x80) {
	/*printf ( "Samples are saved as delta values !\n" ); */
	GLOBAL_DELTA = ON;
    }
    nins &= 0x3f;

    pw_write_zero(out, 20);		/* write title */

    /* sample headers */
    for (i = 0; i < nins; i++) {
	pw_write_zero(out, 22);		/* name */

	j = isize[i] = read16b(in);	/* sample size */

	if (j > 0xff00) {
	    smp_size[i] = smp_size[0xffff - j];
	    isize[i] = isize[0xffff - j];
	    saddr[i + 1] = saddr[0xffff - j + 1];
	} else {
	    saddr[i + 1] = saddr[i] + smp_size[i - 1];
	    smp_size[i] = j * 2;
	    ssize += smp_size[i];
	}
	j = smp_size[i] / 2;

	write16b(out, isize[i]);

	write8(out, read8(in));		/* finetune */
	write8(out, read8(in));		/* volume */
	val = read16b(in);		/* loop start */

	if (val == 0xffff) {
	    write16b(out, 0x0000);	/* loop start */
	    write16b(out, 0x0001);	/* loop size */
	} else {
	    write16b(out, val);		/* loop start */
	    write16b(out, j - val);	/* loop size */
	}
    }

    /* go up to 31 samples */
    memset(buf, 0, 30);
    buf[29] = 0x01;
    for (; i < 31; i++)
	fwrite(buf, 30, 1, out);

    /* read tracks addresses per pattern */
    for (i = 0; i < npat; i++) {
	for (j = 0; j < 4; j++)
	    taddr[i][j] = read16b(in);
    }

    /* pattern table */
    for (PatPos = 0; PatPos < 128; PatPos++) {
	c1 = read8(in);
	if (c1 == 0xff)
	    break;
	ptable[PatPos] = c1 / 2;
    }
    write8(out, PatPos);		/* write size of pattern list */
    write8(out, 0x7f);			/* write noisetracker byte */
    fwrite(ptable, 128, 1, out);	/* write pattern table */
    write32b(out, PW_MOD_MAGIC);	/* M.K. */

    tdata_addr = ftell(in);

    /* rewrite the track data */

    for (i = 0; i < npat; i++) {
	max_row = 63;
	for (j = 0; j < 4; j++) {
	    fseek(in, taddr[i][j] + tdata_addr, SEEK_SET);
	    for (k = 0; k <= max_row; k++) {
		uint8 *x = &tdata[i * 4 + j][k * 4];
		c1 = read8(in);
		c2 = read8(in);
		c3 = read8(in);

		if ((c1 & 0x80) == 0x80 && c1 != 0x80) {
		    c4 = read8(in);
		    c1 = 0xff - c1;

		    *x++ = ((c1 << 4) & 0x10) | ptk_table[c1 / 2][0];
		    *x++ = ptk_table[c1 / 2][1];

		    c6 = c2 & 0x0f;
		    if (c6 == 0x08)
			c2 -= 0x08;

		    *x++ = c2;

		    if (c6 == 0x05 || c6 == 0x06 || c6 == 0x0a)
			c3 = c3 > 0x7f ? (0x100 - c3) << 4 : c3;

		    *x++ = c3;

		    if (c6 == 0x0d) {		/* pattern break */
			max_row = k;
			break;
		    }
		    if (c6 == 0x0b) {		/* pattern jump */
			max_row = k;
			break;
		    }
		    if (c4 < 0x80) {		/* skip rows */
			k += c4;
			continue;
		    }
		    c4 = 0x100 - c4;

		    for (l = 0; l < c4; l++) {
			k += 1;
			x = &tdata[i * 4 + j][k * 4];

			*x++ = ((c1 << 4) & 0x10) | ptk_table[c1 / 2][0];
			*x++ = ptk_table[c1 / 2][1];

			c6 = c2 & 0x0f;
			if (c6 == 0x08)
			    c2 -= 0x08;

			*x++ = c2;

			if (c6 == 0x05 || c6 == 0x06 || c6 == 0x0a)
			    c3 = c3 > 0x7f ? (0x100 - c3) << 4 : c3;

			*x++ = c3;
		    }
		    continue;
		}

		if (c1 == 0x80) {
		    c4 = read8(in);
		    a = ftell(in);
		    c5 = c2;
		    fseek(in, -((c3 << 8) + c4), SEEK_CUR);
		    for (l = 0; l <= c5; l++, k++) {
			x = &tdata[i * 4 + j][k * 4];

			c1 = read8(in);
			c2 = read8(in);
			c3 = read8(in);

			if ((c1 & 0x80) == 0x80 && c1 != 0x80) {
			    c4 = read8(in);
			    c1 = 0xff - c1;
			    *x++ = ((c1 << 4) & 0x10) | ptk_table[c1 / 2][0];
			    *x++ = ptk_table[c1 / 2][1];

			    c6 = c2 & 0x0f;
			    if (c6 == 0x08)
				c2 -= 0x08;

			    *x++ = c2;

			    if (c6 == 0x05 || c6 == 0x06 || c6 == 0x0a)
				c3 = c3 > 0x7f ? (0x100 - c3) << 4 : c3;

			    *x++ = c3;

			    if (c6 == 0x0d) {	/* pattern break */
				max_row = k;
				k = l = 9999l;
				continue;
			    }
			    if (c6 == 0x0b) {	/* pattern jump */
				max_row = k;
				k = l = 9999l;
				continue;
			    }
			    if (c4 < 0x80) {	/* skip rows */
				k += c4;
				continue;
			    }
			    c4 = 0x100 - c4;

			    for (b = 0; b < c4; b++) {
				k += 1;
				x = &tdata[i * 4 + j][k * 4];

				*x++ = ((c1 << 4) & 0x10) |
						ptk_table[c1 / 2][0];
				*x++ = ptk_table[c1 / 2][1];

				c6 = c2 & 0x0f;
				if (c6 == 0x08)
				    c2 -= 0x08;

				*x++ = c2;

				if (c6 == 0x05 || c6 == 0x06 || c6 == 0x0a)
				    c3 = c3 > 0x7f ? (0x100 - c3) << 4 : c3;
				*x++ = c3;
			    }
			}

			x = &tdata[i * 4 + j][k * 4];

			*x++ = ((c1 << 4) & 0x10) | ptk_table[c1 / 2][0];
			*x++ = ptk_table[c1 / 2][1];

			c6 = c2 & 0x0f;
			if (c6 == 0x08)
			    c2 -= 0x08;

			*x++ = c2;

			if (c6 == 0x05 || c6 == 0x06 || c6 == 0x0a)
			    c3 = c3 > 0x7f ? (0x100 - c3) << 4 : c3;

			*x++ = c3;
		    }
		    fseek(in, a, SEEK_SET);
		    k -= 1;
		    continue;
		}

		x = &tdata[i * 4 + j][k * 4];

		*x++ = ((c1 << 4) & 0x10) | ptk_table[c1 / 2][0];
		*x++ = ptk_table[c1 / 2][1];

		c6 = c2 & 0x0f;
		if (c6 == 0x08)
		    c2 -= 0x08;

		*x++ = c2;

		if (c6 == 0x05 || c6 == 0x06 || c6 == 0x0a)
		    c3 = c3 > 0x7f ? (0x100 - c3) << 4 : c3;

		*x++ = c3;

		if (c6 == 0x0d) {	/* pattern break */
		    max_row = k;
		    break;
		}
		if (c6 == 0x0b) {	/* pattern jump */
		    max_row = k;
		    break;
		}
	    }
	}
    }

    /* write pattern data */

    for (i = 0; i < npat; i++) {
	memset(buf, 0, 1024);
	for (j = 0; j < 64; j++) {
	    for (k = 0; k < 4; k++)
		memcpy(&buf[j * 16 + k * 4], &tdata[k + i * 4][j * 4], 4);
	}
	fwrite(buf, 1024, 1, out);
    }

    /* go to sample data address */
    fseek(in, sdata_addr, SEEK_SET);

    /* read and write sample data */
    for (i = 0; i < nins; i++) {
	fseek(in, sdata_addr + saddr[i + 1], SEEK_SET);
	smp_buffer = malloc(smp_size[i]);
	memset(smp_buffer, 0, smp_size[i]);
	fread(smp_buffer, smp_size[i], 1, in);
	if (GLOBAL_DELTA == ON) {
	    c1 = smp_buffer[0];
	    for (j = 1; j < smp_size[i]; j++) {
		c2 = smp_buffer[j];
		c3 = c1 - c2;
		smp_buffer[j] = c3;
		c1 = c3;
	    }
	}
	fwrite(smp_buffer, smp_size[i], 1, out);
	free(smp_buffer);
    }

    if (GLOBAL_DELTA == ON)
	pw_p50a.flags |= PW_DELTA;

    return 0;
}


static int test_p50a(uint8 *data, int s)
{
	int j, k, l, m, n, o;
	int start = 0, ssize;

	/* number of pattern (real) */
	/* m is the real number of pattern */
	m = data[start + 2];
	if (m > 0x7f || m == 0)
		return -1;

	/* number of sample */
	/* k is the number of sample */
	k = data[start + 3];
	if ((k & 0x80) == 0x80)
		return -1;
	if (((k & 0x3f) > 0x1F) || ((k & 0x3F) == 0))
		return -1;
	k &= 0x3F;

	for (l = 0; l < k; l++) {
		/* test volumes */
		if (data[start + 7 + l * 6] > 0x40)
			return -1;
		/* test finetunes */
		if (data[start + 6 + l * 6] > 0x0F)
			return -1;
	}

	/* test sample sizes and loop start */
	ssize = 0;
	for (n = 0; n < k; n++) {
		o = ((data[start + 4 + n * 6] << 8) + data[start + 5 + n * 6]);
		if ((o < 0xffdf && o > 0x8000) || o == 0)
			return -1;
		if (o < 0xff00)
			ssize += o * 2;

		j = readmem16b(data + start + 8 + n * 6);
		if (j != 0xffff && j >= o)
			return -1;

		if (o > 0xffdf) {
			if (0xffff - o > k)
				return -1;
		}
	}

	/* test sample data address */
	/* j is the address of the sample data */
	j = readmem16b(data + start);
	if (j < k * 6 + 4 + m * 8)
		return -1;

	/* test track table */
	for (l = 0; l < m * 4; l++) {
		o = readmem16b(data + start + 4 + k * 6 + l * 2);
		if (o + k * 6 + 4 + m * 8 > j)
			return -1;
	}

	/* test pattern table */
	l = 0;
	o = 0;

	PW_REQUEST_DATA(s, start + k * 6 + 4 + m * 8 + 128);

	while (data[start + k * 6 + 4 + m * 8 + l] != 0xff && l < 128) {

		if (data[start + k * 6 + 4 + m * 8 + l] & 0x01)
			return -1;

		if (data[start + k * 6 + 4 + m * 8 + l] > m * 2)
			return -1;

		if (data[start + k * 6 + 4 + m * 8 + l] > o)
			o = data[start + k * 6 + 4 + m * 8 + l];
		l++;
	}

	/* are we beside the sample data address ? */
	if (k * 6 + 4 + m * 8 + l > j)
		return -1;

	if (l == 0 || l == 128)
		return -1;

	o /= 2;
	o += 1;
	/* o is the highest number of pattern */

	/* test notes ... pfiew */

	PW_REQUEST_DATA(s, start + j + 1);

	l += 1;
	for (n = k * 6 + 4 + m * 8 + l; n < j; n++) {
		if (~data[start + n] & 0x80) {
			if (data[start + n] > 0x49)
				return -1;

			if ((((data[start + n] << 4) & 0x10) |
				((data[start + n + 1] >> 4) & 0x0F)) > k)
				return -1;
			n += 2;
			continue;
		}

		if (data[start + n] == 0x80) {
			/* too many lines to repeat ? */
			if (data[start + n + 1] > 0x40)
				return -1;
			if (readmem16b(data + start + n + 2) <
			    (data[start + n + 1] * 3))
				return -1;
		}
		n += 3;
	}

	/* ssize is the whole sample data size */
	/* j is the address of the sample data */

	return 0;
}
