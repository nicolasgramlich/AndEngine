/*
 * EurekaPacker.c   Copyright (C) 1997 Asle / ReDoX
 *		    Modified by Claudio Matsuoka
 *
 * Converts MODs packed with Eureka packer back to ptk
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_eu (uint8 *, int);
static int depack_eu (FILE *, FILE *);

struct pw_format pw_eu = {
	"EU",
	"Eureka Packer",
	0x00,
	test_eu,
	depack_eu
};

static int depack_eu(FILE *in, FILE *out)
{
	uint8 tmp[1080];
	uint8 c1;
	int npat, smp_addr;
	int ssize = 0;
	int trk_addr[128][4];
	int i, j, k;

	/* read header ... same as ptk */
	fread(tmp, 1080, 1, in);
	fwrite(tmp, 1080, 1, out);

	/* now, let's sort out that a bit :) */
	/* first, the whole sample size */
	for (i = 0; i < 31; i++)
		ssize += 2 * readmem16b(tmp + i * 30 + 42);

	/* now, the pattern list .. and the max */
	for (npat = i = 0; i < 128; i++) {
		if (tmp[952 + i] > npat)
			npat = tmp[952 + i];
	}
	npat++;

	write32b(out, PW_MOD_MAGIC);		/* write ptk ID */
	smp_addr = read32b(in);			/* read sample data address */

	/* read tracks addresses */
	for (i = 0; i < npat; i++) {
		for (j = 0; j < 4; j++)
			trk_addr[i][j] = read16b(in);
	}

	/* the track data now ... */
	for (i = 0; i < npat; i++) {
		memset(tmp, 0, 1024);
		for (j = 0; j < 4; j++) {
			fseek(in, trk_addr[i][j], SEEK_SET);
			for (k = 0; k < 64; k++) {
				uint8 *x = &tmp[k * 16 + j * 4];
				c1 = read8(in);
				if ((c1 & 0xc0) == 0x00) {
					*x++ = c1;
					*x++ = read8(in);
					*x++ = read8(in);
					*x++ = read8(in);
					continue;
				}
				if ((c1 & 0xc0) == 0xc0) {
					k += (c1 & 0x3f);
					continue;
				}
				if ((c1 & 0xc0) == 0x40) {
					x += 2;
					*x++ = c1 & 0x0f;
					*x++ = read8(in);
					continue;
				}
				if ((c1 & 0xc0) == 0x80) {
					*x++ = read8(in);
					*x++ = read8(in);
					*x++ = (c1 << 4) & 0xf0;
					continue;
				}
			}
		}
		fwrite(tmp, 1024, 1, out);
	}

	fseek(in, smp_addr, SEEK_SET);
	pw_move_data(out, in, ssize);

	return 0;
}

static int test_eu (uint8 *data, int s)
{
	int j, k, l, m, n, o;
	int start = 0;

	PW_REQUEST_DATA (s, 1084);

	/* test 2 */
	j = data[start + 950];
	if (j == 0 || j > 127)
		return -1;

	/* test #3  finetunes & volumes */
	for (k = 0; k < 31; k++) {
		o = (data[start + 42 + k * 30] << 8) +
			data[start + 43 + k * 30];
		m = (data[start + 46 + k * 30] << 8) +
			data[start + 47 + k * 30];
		n = (data[start + 48 + k * 30] << 8) +
			data[start + 49 + k * 30];
		o *= 2;
		m *= 2;
		n *= 2;
		if (o > 0xffff || m > 0xffff || n > 0xffff)
			return -1;

		if ((m + n) > (o + 2))
			return -1;

		if (data[start + 44 + k * 30] > 0x0f ||
			data[start + 45 + k * 30] > 0x40)
			return -1;
	}


	/* test 4 */
	l = (data[start + 1080] << 24) + (data[start + 1081] << 16)
		+ (data[start + 1082] << 8) + data[start + 1083];

#if 0
	if ((l + start) > in_size)
		return -1;
#endif

	if (l < 1084)
		return -1;

	m = 0;
	/* pattern list */
	for (k = 0; k < j; k++) {
		n = data[start + 952 + k];
		if (n > m)
			m = n;
		if (n > 127)
			return -1;
	}
	k += 2;		/* to be sure .. */

	while (k != 128) {
		if (data[start + 952 + k] != 0)
			return -1;
		k += 1;
	}
	m += 1;
	/* m is the highest pattern number */


	/* test #5 */
	/* j is still the size if the pattern table */
	/* l is still the address of the sample data */
	/* m is the highest pattern number */
	n = 0;
	j = 999999L;

	PW_REQUEST_DATA (s, start + (m * 4) * 2 + 1085);

	for (k = 0; k < (m * 4); k++) {
		o = (data[start + k * 2 + 1084] << 8) +
			data[start + k * 2 + 1085];
		if (o > l || o < 1084)
			return -1;
		if (o > n)
			n = o;
		if (o < j)
			j = o;
	}
	/* o is the highest track address */
	/* j is the lowest track address */

	/* test track datas */
	/* last track wont be tested ... */
	for (k = j; k < o; k++) {
		if ((data[start + k] & 0xC0) == 0xC0)
			continue;
		if ((data[start + k] & 0xC0) == 0x80) {
			k += 2;
			continue;
		}
		if ((data[start + k] & 0xC0) == 0x40) {
			if ((data[start + k] & 0x3F) == 0x00 &&
				data[start + k + 1] == 0x00)
				return -1;
			k += 1;
			continue;
		}
		if ((data[start + k] & 0xC0) == 0x00) {
			if (data[start + k] > 0x13)
				return -1;
			k += 3;
			continue;
		}
	}

	return 0;
}
