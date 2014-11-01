/*
 * Promizer_10c.c   Copyright (C) 1997 Asle / ReDoX
 *                  Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Converts PM10c packed MODs back to PTK MODs
 *
 * claudio's note: Now this one can be *heavily* optimized...
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_p10c(uint8 *, int);
static int depack_p10c(FILE *, FILE *);

struct pw_format pw_p10c = {
	"P10C",
	"Promizer 1.0c",
	0x00,
	test_p10c,
	depack_p10c
};

#define ON  0
#define OFF 1

static int depack_p10c(FILE *in, FILE *out)
{
	uint8 c1, c2, c3;
	short pat_max = 0;
	long tmp_ptr, tmp1, tmp2;
	short refmax = 0;
	uint8 pnum[128];
	uint8 pnum1[128];
	int paddr[128];
	int paddr1[128];
	int paddr2[128];
	short pptr[64][256];
	uint8 NOP = 0x00;	/* number of pattern */
	uint8 *reftab;
	uint8 pat[128][1024];
	int i, j, k, l;
	int size, ssize = 0;
	int psize;
	int SDAV;
	uint8 FLAG = OFF;
	uint8 fin[31];
	uint8 oldins[4];
	short per;

	memset(pnum, 0, 128);
	memset(pnum1, 0, 128);
	memset(pptr, 0, 64 << 8);
	memset(pat, 0, 128 * 1024);
	memset(fin, 0, 31);
	memset(oldins, 0, 4);
	memset(paddr, 0, 128 * 4);
	memset(paddr1, 0, 128 * 4);

	for (i = 0; i < 128; i++)
		paddr2[i] = 9999L;

	pw_write_zero(out, 20);				/* title */

	/* bypass replaycode routine */
	fseek(in, 4460, SEEK_SET);

	for (i = 0; i < 31; i++) {
		pw_write_zero(out, 22);			/*sample name */
		write16b(out, size = read16b(in));	/* size */
		ssize += size * 2;
		write8(out, fin[i] = read8(in));	/* fin */
		write8(out, read8(in));			/* volume */
		write16b(out, read16b(in));		/* loop start */
		write16b(out, read16b(in));		/* loop size */
	}

	write8(out, NOP = read16b(in) / 4);		/* pat table lenght */
	write8(out, 0x7f);				/* NoiseTracker byte */

	for (i = 0; i < 128; i++)
		paddr[i] = read32b(in);

	/* ordering of patterns addresses */

	tmp_ptr = 0;
	for (i = 0; i < NOP; i++) {
		if (i == 0) {
			pnum[0] = 0;
			tmp_ptr++;
			continue;
		}

		for (j = 0; j < i; j++) {
			if (paddr[i] == paddr[j]) {
				pnum[i] = pnum[j];
				break;
			}
		}
		if (j == i)
			pnum[i] = tmp_ptr++;
	}

	pat_max = tmp_ptr - 1;

	/* correct re-order */
	for (i = 0; i < NOP; i++)
		paddr1[i] = paddr[i];

restart:
	for (i = 0; i < NOP; i++) {
		for (j = 0; j < i; j++) {
			if (paddr1[i] < paddr1[j]) {
				tmp2 = pnum[j];
				pnum[j] = pnum[i];
				pnum[i] = tmp2;
				tmp1 = paddr1[j];
				paddr1[j] = paddr1[i];
				paddr1[i] = tmp1;
				goto restart;
			}
		}
	}

	for (j = i = 0; i < NOP; i++) {
		if (i == 0) {
			paddr2[j] = paddr1[i];
			continue;
		}

		if (paddr1[i] == paddr2[j])
			continue;
		paddr2[++j] = paddr1[i];
	}

	for (c1 = 0; c1 < NOP; c1++) {
		for (c2 = 0; c2 < NOP; c2++) {
			if (paddr[c1] == paddr2[c2])
				pnum1[c1] = c2;
		}
	}

	for (i = 0; i < NOP; i++)
		pnum[i] = pnum1[i];

	/* write pattern table */
	for (c1 = 0x00; c1 < 128; c1++)
		fwrite(&pnum[c1], 1, 1, out);

	write32b(out, PW_MOD_MAGIC);

	/* a little pre-calc code ... no other way to deal with these unknown
	 * pattern data sizes ! :(
	 */
	fseek(in, 4456, SEEK_SET);
	psize = read32b(in);

	/* go back to pattern data starting address */
	fseek(in, 5222, SEEK_SET);
	/* now, reading all pattern data to get the max value of note */
	for (j = 0; j < psize; j += 2) {
		int x;
		if ((x = read16b(in)) > refmax)
			refmax = x;
	}

	/* read "reference Table" */
	refmax += 1;		/* coz 1st value is 0 ! */
	i = refmax * 4;		/* coz each block is 4 bytes long */
	reftab = (uint8 *) malloc(i);
	fread(reftab, i, 1, in);

	/* go back to pattern data starting address */
	fseek(in, 5222, SEEK_SET);

	for (k = j = 0; j <= pat_max; j++) {
		for (i = 0; i < 64; i++) {
			int x, y = i * 16;

			/* VOICE #1 */

			x = read16b(in);
			k += 2;
			pat[j][y + 0] = reftab[x * 4];
			pat[j][y + 1] = reftab[x * 4 + 1];
			pat[j][y + 2] = reftab[x * 4 + 2];
			pat[j][y + 3] = reftab[x * 4 + 3];

			c3 = ((pat[j][y + 2] >> 4) & 0x0f) | (pat[j][y] & 0xf0);

			if (c3 != 0) {
				oldins[0] = c3;
			}
			per = ((pat[j][y] & 0x0f) << 8) + pat[j][y + 1];

			if ((per != 0) && (fin[oldins[0] - 1] != 0)) {
				for (l = 0; l < 36; l++) {
					if (tun_table[fin[oldins[0] - 1]][l] ==
					    per) {
						pat[j][y] &= 0xf0;
						pat[j][y] |=
						    ptk_table[l + 1][0];
						pat[j][y + 1] =
						    ptk_table[l + 1][1];
						break;
					}
				}
			}

			if (((pat[j][y + 2] & 0x0f) == 0x0d) ||
			    ((pat[j][y + 2] & 0x0f) == 0x0b)) {
				FLAG = ON;
			}

			/* VOICE #2 */

			x = read16b(in);
			k += 2;
			pat[j][y + 4] = reftab[x * 4];
			pat[j][y + 5] = reftab[x * 4 + 1];
			pat[j][y + 6] = reftab[x * 4 + 2];
			pat[j][y + 7] = reftab[x * 4 + 3];

			c3 = ((pat[j][y + 6] >> 4) & 0x0f) |
					(pat[j][y + 4] & 0xf0);

			if (c3 != 0) {
				oldins[1] = c3;
			}
			per = ((pat[j][y + 4] & 0x0f) << 8) + pat[j][y + 5];

			if ((per != 0) && (fin[oldins[1] - 1] != 0x00)) {
				for (l = 0; l < 36; l++) {
					if (tun_table[fin[oldins[1] - 1]][l] ==
					    per) {
						pat[j][y + 4] &= 0xf0;
						pat[j][y + 4] |=
						    ptk_table[l + 1][0];
						pat[j][y + 5] =
						    ptk_table[l + 1][1];
						break;
					}
				}
			}

			if (((pat[j][y + 6] & 0x0f) == 0x0d) ||
			    ((pat[j][y + 6] & 0x0f) == 0x0b)) {
				FLAG = ON;
			}

			/* VOICE #3 */

			x = read16b(in);
			k += 2;
			pat[j][y + 8] = reftab[x * 4];
			pat[j][y + 9] = reftab[x * 4 + 1];
			pat[j][y + 10] = reftab[x * 4 + 2];
			pat[j][y + 11] = reftab[x * 4 + 3];

			c3 = ((pat[j][y + 10] >> 4) & 0x0f) |
					(pat[j][y + 8] & 0xf0);

			if (c3 != 0) {
				oldins[2] = c3;
			}
			per = ((pat[j][y + 8] & 0x0f) << 8) + pat[j][y + 9];

			if ((per != 0) && (fin[oldins[2] - 1] != 0x00)) {
				for (l = 0; l < 36; l++) {
					if (tun_table[fin[oldins[2] - 1]][l] ==
					    per) {
						pat[j][y + 8] &= 0xf0;
						pat[j][y + 8] |=
						    ptk_table[l + 1][0];
						pat[j][y + 9] =
						    ptk_table[l + 1][1];
						break;
					}
				}
			}

			if (((pat[j][y + 10] & 0x0f) == 0x0d) ||
			    ((pat[j][y + 10] & 0x0f) == 0x0b)) {
				FLAG = ON;
			}

			/* VOICE #4 */

			x = read16b(in);
			k += 2;
			pat[j][y + 12] = reftab[x * 4];
			pat[j][y + 13] = reftab[x * 4 + 1];
			pat[j][y + 14] = reftab[x * 4 + 2];
			pat[j][y + 15] = reftab[x * 4 + 3];

			c3 = ((pat[j][y + 14] >> 4) & 0x0f) |
					(pat[j][y + 12] & 0xf0);

			if (c3 != 0) {
				oldins[3] = c3;
			}
			per = ((pat[j][y + 12] & 0x0f) << 8) + pat[j][y + 13];

			if ((per != 0) && (fin[oldins[3] - 1] != 0x00)) {
				for (l = 0; l < 36; l++)
					if (tun_table[fin[oldins[3] - 1]][l] ==
					    per) {
						pat[j][y + 12] &= 0xf0;
						pat[j][y + 12] |=
						    ptk_table[l + 1][0];
						pat[j][y + 13] =
						    ptk_table[l + 1][1];
						break;
					}
			}

			if (((pat[j][y + 14] & 0x0f) == 0x0d) ||
			    ((pat[j][y + 14] & 0x0f) == 0x0b)) {
				FLAG = ON;
			}

			if (FLAG == ON) {
				FLAG = OFF;
				break;
			}
		}
		fwrite(pat[j], 1024, 1, out);
	}

	free(reftab);

	fseek(in, 4452, SEEK_SET);
	SDAV = read32b(in);
	fseek(in, 4456 + SDAV, SEEK_SET);

	pw_move_data(out, in, ssize);

	return 0;
}

static int test_p10c(uint8 *data, int s)
{
	int start = 0;
	uint8 magic[] = {
		0x60, 0x38, 0x60, 0x00, 0x00, 0xa0, 0x60, 0x00,
		0x01, 0x3e, 0x60, 0x00, 0x01, 0x0c, 0x48, 0xe7
	};

	/* test 1 */
	PW_REQUEST_DATA(s, 22);

	if (memcmp(data + start, magic, 16) != 0)
		return -1;

	/* test 2 */
	if (data[start + 21] != 0xce)
		return -1;

	PW_REQUEST_DATA(s, 4714);

#if 0
	/* test 3 */
	j = readmem32b(data + start + 4452);
	if ((start + j + 4452) > in_size)
		return -1;
#endif

	/* test 4 */
	if (readmem16b(data + start + 4712) & 0x03)
		return -1;

	/* test 5 */
	if (data[start + 36] != 0x10)
		return -1;

	/* test 6 */
	if (data[start + 37] != 0xfc)
		return -1;

	return 0;
}
