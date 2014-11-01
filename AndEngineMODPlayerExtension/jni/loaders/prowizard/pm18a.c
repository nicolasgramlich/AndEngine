/*
 * Promizer_18a.c   Copyright (C) 1997 Asle / ReDoX
 *		    Modified by Claudio Matsuoka
 *
 * Converts PM18a packed MODs back to PTK MODs
 * thanks to Gryzor and his ProWizard tool ! ... without it, this prog
 * would not exist !!!
 *
 * claudio's note: this code asks for heavy optimization. maybe later
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_p18a(uint8 *, int);
static int depack_p18a(FILE *, FILE *);

struct pw_format pw_p18a = {
	"P18A",
	"Promizer 1.8a",
	0x00,
	test_p18a,
	depack_p18a
};

#define ON  0
#define OFF 1

static int depack_p18a(FILE *in, FILE *out)
{
	uint8 c3;
	short pat_max;
	int tmp_ptr;
	int refmax;
	uint8 pnum[128];
	int paddr[128];
	short pptr[64][256];
	uint8 NOP = 0x00;	/* number of pattern */
	uint8 *reftab;
	uint8 pat[128][1024];
	int i = 0, j = 0, k = 0, l = 0;
	int size, ssize;
	int psize;
	int SDAV;
	uint8 FLAG = OFF;
	uint8 fin[31];
	uint8 oins[4];
	short per;

	memset(pnum, 0, 128);
	memset(pptr, 0, 64 << 8);
	memset(pat, 0, 128 * 1024);
	memset(fin, 0, 31);
	memset(oins, 0, 4);
	memset(paddr, 0, 128 * 4);

	pw_write_zero(out, 20);			/* title */

	/* bypass replaycode routine */
	fseek(in, 4464, 0);	/* SEEK_SET */

	ssize = 0;
	for (i = 0; i < 31; i++) {
		pw_write_zero(out, 22);		/* sample name */
		write16b(out, size = read16b(in));
		ssize += size * 2;
		write8(out, fin[i] = read8(in)); /* finetun_table */
		write8(out, read8(in));		/* volume */
		write16b(out, read16b(in));	/* loop start */
		write16b(out, read16b(in));	/* loop size */
	}

	write8(out, NOP = read16b(in) / 4);	/* pattern table length */
	write8(out, 0x7f);			/* NoiseTracker restart byte */

	for (i = 0; i < 128; i++)
		paddr[i] = read32b(in);

	/* ordering of patterns addresses */

	tmp_ptr = 0;
	for (i = 0; i < NOP; i++) {
		if (i == 0) {
			pnum[0] = 0x00;
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

	fwrite(pnum, 128, 1, out);		/* pattern table */
	write32b(out, PW_MOD_MAGIC);		/* M.K. */


	/* a little pre-calc code ... no other way to deal with these unknown
	 * pattern data sizes ! :(
	 */
	fseek(in, 4460, SEEK_SET);
	psize = read32b(in);
	fseek(in, 5226, SEEK_SET);	/* back to pattern data start */

	/* now, reading all pattern data to get the max value of note */
	refmax = 0;
	for (j = 0; j < psize; j += 2) {
		int x = read16b(in);
		if (x > refmax)
			refmax = x;
	}

	/* read "reference table" */
	refmax += 1;			/* 1st value is 0 ! */
	i = refmax * 4;			/* each block is 4 bytes long */
	reftab = (uint8 *) malloc(i);
	fread(reftab, i, 1, in);
	fseek(in, 5226, SEEK_SET);	/* back to pattern data start */

	k = 0;
	for (j = 0; j <= pat_max; j++) {
		fseek(in, paddr[j] + 5226, 0);
		for (i = 0; i < 64; i++) {
			/* VOICE #1 */
			int x = read16b(in) * 4;
			int y = i * 16;

			k += 2;
			pat[j][y] = reftab[x];
			pat[j][y + 1] = reftab[x + 1];
			pat[j][y + 2] = reftab[x + 2];
			pat[j][y + 3] = reftab[x + 3];

			c3 = ((pat[j][y + 2] >> 4) & 0x0f) | (pat[j][y] & 0xf0);

			if (c3)
				oins[0] = c3;

			per = ((pat[j][y] & 0x0f) << 8) + pat[j][y + 1];

			if (per && fin[oins[0] - 1]) {
				for (l = 0; l < 36; l++)
					if (tun_table[fin[oins[0] - 1]][l] == per) {
						pat[j][y] &= 0xf0;
						pat[j][y] |= ptk_table[l + 1][0];
						pat[j][y + 1] = ptk_table[l + 1][1];
						break;
					}
			}

			if (((pat[j][y + 2] & 0x0f) == 0x0d) ||
			    ((pat[j][y + 2] & 0x0f) == 0x0b)) {
				FLAG = ON;
			}

			/* VOICE #2 */

			x = read16b(in) * 4;
			k += 2;
			pat[j][y + 4] = reftab[x];
			pat[j][y + 5] = reftab[x + 1];
			pat[j][y + 6] = reftab[x + 2];
			pat[j][y + 7] = reftab[x + 3];
			c3 = ((pat[j][y + 6] >> 4) & 0x0f) |
			    (pat[j][y + 4] & 0xf0);

			if (c3)
				oins[1] = c3;

			per = ((pat[j][y + 4] & 0x0f) << 8) + pat[j][y + 5];
			if ((per != 0) && (fin[oins[1] - 1] != 0x00)) {
				for (l = 0; l < 36; l++)
					if (tun_table[fin[oins[1] - 1]][l] == per) {
						pat[j][y + 4] &= 0xf0;
						pat[j][y + 4] |= ptk_table[l + 1][0];
						pat[j][y + 5] = ptk_table[l + 1][1];
						break;
					}
			}

			if (((pat[j][y + 6] & 0x0f) == 0x0d) ||
			    ((pat[j][y + 6] & 0x0f) == 0x0b)) {
				FLAG = ON;
			}

			/* VOICE #3 */

			x = read16b(in) * 4;
			k += 2;
			pat[j][y + 8] = reftab[x];
			pat[j][y + 9] = reftab[x + 1];
			pat[j][y + 10] = reftab[x + 2];
			pat[j][y + 11] = reftab[x + 3];
			c3 = ((pat[j][y + 10] >> 4) & 0x0f) |
			    (pat[j][y + 8] & 0xf0);

			if (c3)
				oins[2] = c3;

			per = ((pat[j][y + 8] & 0x0f) << 8) + pat[j][y + 9];
			if ((per != 0)
			    && (fin[oins[2] - 1] != 0x00)) {
				for (l = 0; l < 36; l++)
					if (tun_table[fin[oins[2] - 1]][l] == per) {
						pat[j][y + 8] &= 0xf0;
						pat[j][y + 8] |=
						    ptk_table[l + 1][0];
						pat[j][y + 9] =
						    ptk_table[l + 1][1];
						break;
					}
			}

			if (((pat[j][y + 10] & 0x0f) == 0x0d) ||
			    ((pat[j][y + 10] & 0x0f) == 0x0b)) {
				FLAG = ON;
			}

			/* VOICE #4 */

			x = read16b(in) * 4;
			k += 2;
			pat[j][y + 12] = reftab[x];
			pat[j][y + 13] = reftab[x + 1];
			pat[j][y + 14] = reftab[x + 2];
			pat[j][y + 15] = reftab[x + 3];
			c3 = ((pat[j][y + 14] >> 4) & 0x0f) | (pat[j][y + 12] & 0xf0);
			if (c3 != 0x00)
				oins[3] = c3;

			per = ((pat[j][y + 12] & 0x0f) << 8) + pat[j][y + 13];
			if ((per != 0)
			    && (fin[oins[3] - 1] != 0x00)) {
				for (l = 0; l < 36; l++)
					if (tun_table[fin[oins[3] - 1]][l] == per) {
						pat[j][y + 12] &= 0xf0;
						pat[j][y + 12] |= ptk_table[l + 1][0];
						pat[j][y + 13] = ptk_table[l + 1][1];
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

	/* printf ( "Highest value in pattern data : %d\n" , refmax ); */

	free(reftab);

	fseek(in, 4456, SEEK_SET);
	SDAV = read32b(in);
	fseek(in, 4460 + SDAV, SEEK_SET);

	/* Now, it's sample data ... though, VERY quickly handled :) */
	pw_move_data(out, in, ssize);

	return 0;
}

static int test_p18a(uint8 * data, int s)
{
	int i = 0, j, k;
	int start = 0;

	/* test 1 */
	PW_REQUEST_DATA(s, 22);

	if (data[i] != 0x60 || data[i + 1] != 0x38 || data[i + 2] != 0x60 ||
	    data[i + 3] != 0x00 || data[i + 4] != 0x00 ||
	    data[i + 5] != 0xa0 || data[i + 6] != 0x60 ||
	    data[i + 7] != 0x00 || data[i + 8] != 0x01 ||
	    data[i + 9] != 0x3e || data[i + 10] != 0x60 ||
	    data[i + 11] != 0x00 || data[i + 12] != 0x01 ||
	    data[i + 13] != 0x0c || data[i + 14] != 0x48 ||
	    data[i + 15] != 0xe7)
		return -1;

	/* test 2 */
	if (data[start + 21] != 0xd2)
		return -1;

	/* test 3 */
	PW_REQUEST_DATA(s, 4460);
	j = readmem32b(data + start + 4456);

#if 0
	if ((start + j + 4456) > in_size) {
		Test = BAD;
		return;
	}
#endif

	/* test 4 */
	PW_REQUEST_DATA(s, 4714);
	k = readmem16b(data + start + 4712);
	if (k & 0x03)
		return -1;

	/* test 5 */
	if (data[start + 36] != 0x11)
		return -1;

	/* test 6 */
	if (data[start + 37] != 0x00)
		return -1;

	return 0;
}
