/*
 * gmc.c    Copyright (C) 1997 Sylvain "Asle" Chipaux
 *          Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Depacks musics in the Game Music Creator format and saves in ptk.
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int depack_GMC(FILE *, FILE *);
static int test_GMC(uint8 *, int);

struct pw_format pw_gmc = {
	"GMC",
	"Game Music Creator",
	0x00,
	test_GMC,
	depack_GMC
};

static int depack_GMC(FILE *in, FILE *out)
{
	uint8 tmp[1024];
	uint8 ptable[128];
	uint8 max;
	uint8 PatPos;
	uint16 len, looplen;
	long ssize = 0;
	long i = 0, j = 0;

	memset(ptable, 0, 128);

	pw_write_zero(out, 20);			/* title */

	for (i = 0; i < 15; i++) {
		pw_write_zero(out, 22);		/* name */
		read32b(in);			/* bypass 4 address bytes */
		write16b(out, len = read16b(in));	/* size */
		ssize += len * 2;
		read8(in);
		write8(out, 0);			/* finetune */
		write8(out, read8(in));		/* volume */
		read32b(in);			/* bypass 4 address bytes */

		looplen = read16b(in);		/* loop size */
		write16b(out, looplen > 2 ? len - looplen : 0);
		write16b(out, looplen <= 2 ? 1 : looplen);
		read16b(in);	/* always zero? */
	}

	memset(tmp, 0, 30);
	tmp[29] = 0x01;
	for (i = 0; i < 16; i++)
		fwrite(tmp, 30, 1, out);

	fseek(in, 0xf3, 0);
	write8(out, PatPos = read8(in));	/* pattern list size */
	write8(out, 0x7f);			/* ntk byte */

	/* read and write size of pattern list */
	/*printf ( "Creating the pattern table ... " ); */
	for (i = 0; i < 100; i++)
		ptable[i] = read16b(in) / 1024;
	fwrite(ptable, 128, 1, out);

	/* get number of pattern */
	for (max = i = 0; i < 128; i++) {
		if (ptable[i] > max)
			max = ptable[i];
	}

	/* write ID */
	write32b(out, PW_MOD_MAGIC);

	/* pattern data */
	fseek(in, 444, SEEK_SET);
	for (i = 0; i <= max; i++) {
		memset(tmp, 0, 1024);
		fread(tmp, 1024, 1, in);
		for (j = 0; j < 256; j++) {
			switch (tmp[(j * 4) + 2] & 0x0f) {
			case 3:	/* replace by C */
				tmp[(j * 4) + 2] += 0x09;
				break;
			case 4:	/* replace by D */
				tmp[(j * 4) + 2] += 0x09;
				break;
			case 5:	/* replace by B */
				tmp[(j * 4) + 2] += 0x06;
				break;
			case 6:	/* replace by E0 */
				tmp[(j * 4) + 2] += 0x08;
				break;
			case 7:	/* replace by E0 */
				tmp[(j * 4) + 2] += 0x07;
				break;
			case 8:	/* replace by F */
				tmp[(j * 4) + 2] += 0x07;
				break;
			default:
				break;
			}
		}
		fwrite(tmp, 1024, 1, out);
	}

	/* sample data */
	pw_move_data(out, in, ssize);

	return 0;
}

static int test_GMC(uint8 *data, int s)
{
	int j, k, l, m, n, o;
	int start = 0;

	PW_REQUEST_DATA(s, 1024);

#if 0
	/* test #1 */
	if (i < 7) {
/*printf ( "#1\n" );*/
		return -1;
	}
	start = i - 7;
#endif

	/* samples descriptions */
	m = 0;
	j = 0;
	for (k = 0; k < 15; k++) {
		o = (data[start + 16 * k + 4] << 8) + data[start + 16 * k + 5];
		n = (data[start + 16 * k + 12] << 8) +
		    data[start + 16 * k + 13];
		o *= 2;

		/* volumes */
		if (data[start + 7 + (16 * k)] > 0x40)
			return -1;

		/* size */
		if (o > 0xFFFF)
			return -1;

		if (n > o)
			return -1;

		m += o;
		if (o != 0)
			j = k + 1;
	}
	if (m <= 4)
		return -1;
	/* j is the highest not null sample */

	/* pattern table size */
	if (data[start + 243] > 0x64 || data[start + 243] == 0x00)
		return -1;

	/* pattern order table */
	l = 0;
	for (n = 0; n < 100; n++) {
		k = readmem16b(data + start + 244 + n * 2);
		if (k & 0x03ff)
			return -1;
		l = ((k >> 10) > l) ? k >> 10 : l;
	}
	l++;

	/* l is the number of pattern */
	if (l == 1 || l > 0x64)
		return -1;

	PW_REQUEST_DATA(s, 444 + k * 1024 + n * 4 + 3);

	/* test pattern data */
	o = data[start + 243];
	for (k = 0; k < l; k++) {
		for (n = 0; n < 256; n++) {
			int offset = start + 444 + k * 1024 + n * 4;
			uint8 *d = &data[offset];

			if (offset > (PW_TEST_CHUNK - 4))
				return -1;
				
			/* First test fails with Jumping Jackson */
			if (/*d[0] > 0x03 ||*/ (d[2] & 0x0f) >= 0x90)
				return -1;
#if 0
			/* Test fails with Jumping Jackson */
			if (((d[2] & 0xf0) >> 4) > j)
				return -1;
#endif
			if ((d[2] & 0x0f) == 3 && d[3] > 0x40)
				return -1;

			if ((d[2] & 0x0f) == 4 && d[3] > 0x63)
				return -1;

			if ((d[2] & 0x0f) == 5 && d[3] > (o + 1))
				return -1;

			if ((d[2] & 0x0f) == 6 && d[3] >= 0x02)
				return -1;

			if ((d[2] & 0x0f) == 7 && d[3] >= 0x02)
				return -1;
		}
	}

	return 0;
}
