/*
 * ac1d.c   Copyright (C) 1996-1997 Asle / ReDoX
 *	    Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Converts AC1D packed MODs back to PTK MODs
 * thanks to Gryzor and his ProWizard tool ! ... without it, this prog
 * would not exist !!!
 */

#include <stdlib.h>
#include <string.h>
#include "prowiz.h"

static int depack_AC1D (FILE *, FILE *);
static int test_AC1D (uint8 *, int);

struct pw_format pw_ac1d = {
	"AC1D",
	"AC1D Packer",
	0x00,
	test_AC1D,
	depack_AC1D
};


static int depack_AC1D (FILE *in, FILE *out)
{
	uint8 NO_NOTE = 0xff;
	uint8 c1, c2, c3, c4;
	uint8 npos;
	uint8 ntk_byte;
	uint8 tmp[1024];
	uint8 npat;
	uint8 note, ins, fxt, fxp;
	int size;
	int saddr;
	int ssize = 0;
	int paddr[128];
	int psize[128];
	int tsize1, tsize2, tsize3;
	int i, j, k;

	memset(paddr, 0, 128 * 4);
	memset(psize, 0, 128 * 4);

	npos = read8(in);
	ntk_byte = read8(in);
	read16b(in);			/* bypass ID */
	saddr = read32b(in);		/* sample data address */

	pw_write_zero(out, 20);		/* write title */

	for (i = 0; i < 31; i++) {
		pw_write_zero(out, 22);		/* name */
		write16b(out, size = read16b(in));	/* size */
		ssize += size * 2;
		write8(out, read8(in));		/* finetune */
		write8(out, read8(in));		/* volume */
		write16b(out, read16b(in));	/* loop start */
		write16b(out, read16b(in));	/* loop size */
	}

	/* pattern addresses */
	for (npat = 0; npat < 128; npat++) {
		paddr[npat] = read32b(in);
		if (paddr[npat] == 0)
			break;
	}
	npat--;

	for (i = 0; i < (npat - 1); i++)
		psize[i] = paddr[i + 1] - paddr[i];

	write8(out, npos);		/* write number of pattern pos */
	write8(out, ntk_byte);		/* write "noisetracker" byte */

	fseek(in, 0x300, SEEK_SET);	/* go to pattern table .. */
	pw_move_data(out, in, 128);	/* pattern table */
	
	write32b(out, PW_MOD_MAGIC);	/* M.K. */

	/* pattern data */
	for (i = 0; i < npat; i++) {
		fseek(in, paddr[i], SEEK_SET);
		tsize1 = read32b(in);
		tsize2 = read32b(in);
		tsize3 = read32b(in);

		memset(tmp, 0, 1024);
		for (k = 0; k < 4; k++) {
			for (j = 0; j < 64; j++) {
				int x = j * 16 + k * 4;

				note = ins = fxt = fxp = 0x00;
				c1 = read8(in);
				if (c1 & 0x80) {
					c4 = c1 & 0x7f;
					j += (c4 - 1);
					continue;
				}

				c2 = read8(in);
				ins = ((c1 & 0xc0) >> 2) | ((c2 >> 4) & 0x0f);
				note = c1 & 0x3f;

				if (note == 0x3f)
					note = NO_NOTE;
				else if (note)
					note -= 0x0b;

				if (note == 0)
					note++;

				tmp[x] = ins & 0xf0;

				if (note != NO_NOTE) {
					tmp[x] |= ptk_table[note][0];
					tmp[x + 1] = ptk_table[note][1];
				}

				if ((c2 & 0x0f) == 0x07) {
					fxt = 0x00;
					fxp = 0x00;
					tmp[x + 2] = (ins << 4) & 0xf0;
					continue;
				}

				c3 = read8(in);
				fxt = c2 & 0x0f;
				fxp = c3;
				tmp[x + 2] = ((ins << 4) & 0xf0) | fxt;
				tmp[x + 3] = fxp;
			}
		}
		fwrite(tmp, 1024, 1, out);
	}

	/* sample data */
	fseek(in, saddr, 0);
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_AC1D(uint8 *data, int s)
{
	int j, k;
	int start = 0;

	PW_REQUEST_DATA(s, 896);

	/* test #1 */
	if (data[2] != 0xac || data[3] != 0x1d)
		return -1;

	/* test #2 */
	if (data[start] > 0x7f)
		return -1;

	/* test #4 */
	for (k = 0; k < 31; k++) {
		if (data[start + 10 + 8 * k] > 0x0f)
			return -1;
	}

	/* test #5 */
	for (j = 0; j < 128; j++) {
		if (data[start + 768 + j] > 0x7f)
			return -1;
	}

	return 0;
}
