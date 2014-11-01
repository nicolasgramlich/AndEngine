/*
 * STIM_Packer.c   Copyright (C) 1998 Sylvain "Asle" Chipaux
 *                 Modified by Claudio Matsuoka
 *
 * STIM Packer to Protracker.
 ********************************************************
 * 13 april 1999 : Update
 *   - no more open() of input file ... so no more fread() !.
 *     It speeds-up the process quite a bit :).
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_stim (uint8 *, int);
static int depack_stim (uint8 *, FILE *);

struct pw_format pw_stim = {
	"STIM",
	"Slamtilt",
	0x00,
	test_stim,
	depack_stim,
	NULL
};

static int depack_stim (uint8 *data, FILE * out)
{
	uint8 tmp[1025];
	uint8 c1, c2, c3, c4;
	uint8 ptable[128];
	uint8 max = 0x00;
	uint8 note, ins, fxt, fxp;
	uint8 pat[1025];
	short taddr[4];
	int i = 0, j = 0, k = 0;
	int ssize = 0;
	int iaddr = 0;
	int paddr[64];
	int idata_addr[31];
	int isize[31];
	int start = 0;
	int w = start;	/* main pointer to prevent fread() */

	memset(tmp, 0, 1025);
	memset(ptable, 0, 128);
	memset(pat, 0, 1025);
	memset(paddr, 0, 64 * 4);
	memset(idata_addr, 0, 31 * 4);
	memset(isize, 0, 31 * 4);

	/* write title */
	for (i = 0; i < 20; i++)
		fwrite (&c1, 1, 1, out);

	/* bypass ID */
	w += 4;

	/* read $ of sample description */
	c1 = data[w++];
	c2 = data[w++];
	c3 = data[w++];
	c4 = data[w];
	iaddr = (c1 << 24) + (c2 << 16) + (c3 << 8) + c4;
	/*printf ( "iaddr : %ld\n" , iaddr ); */

	/* convert and write header */
	for (i = 0; i < 31; i++) {
		w = start + iaddr + i * 4;
		c1 = data[w++];
		c2 = data[w++];
		c3 = data[w++];
		c4 = data[w++];
		idata_addr[i] = (c1 << 24) + (c2 << 16) + (c3 << 8) + c4;
		idata_addr[i] += iaddr;
		w = start + idata_addr[i];
		idata_addr[i] += 8;

		/* write sample name */
		fwrite (tmp, 22, 1, out);

		/* sample size */
		c1 = data[w++];
		c2 = data[w++];
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);
		isize[i] = (((c1 << 8) + c2) * 2);
		ssize += (((c1 << 8) + c2) * 2);

		/* finetune */
		fwrite (&data[w++], 1, 1, out);

		/* volume */
		fwrite (&data[w++], 1, 1, out);

		/* loop start */
		fwrite (&data[w++], 1, 1, out);
		fwrite (&data[w++], 1, 1, out);

		/* loop size */
		fwrite (&data[w++], 1, 1, out);
		fwrite (&data[w++], 1, 1, out);
	}

	/* size of the pattern list */
	w = start + 19;
	fwrite (&data[w++], 1, 1, out);
	c1 = 0x7f;
	fwrite (&c1, 1, 1, out);

	/* pattern table */
	w += 1;
	max = data[w++];
	for (i = 0; i < 128; i++)
		ptable[i] = data[w++];
	fwrite (&data[w - 128], 128, 1, out);

	/*printf ( "number of pattern : %d\n" , max ); */

	/* write Protracker's ID */
	c1 = 'M';
	c2 = '.';
	c3 = 'K';
	fwrite (&c1, 1, 1, out);
	fwrite (&c2, 1, 1, out);
	fwrite (&c3, 1, 1, out);
	fwrite (&c2, 1, 1, out);

	/* read pattern addresses */
	for (i = 0; i < 64; i++) {
		c1 = data[w++];
		c2 = data[w++];
		c3 = data[w++];
		c4 = data[w++];
		paddr[i] = (c1 << 24) + (c2 << 16) + (c3 << 8) + c4;
		paddr[i] += 0x0c;
	}

	/* pattern data */
	for (i = 0; i < max; i++) {
		w = start + paddr[i];
		for (k = 0; k < 4; k++) {
			c1 = data[w++];
			c2 = data[w++];
			taddr[k] = (c1 << 8) + c2;
		}

		memset(pat, 0, 1025);
		for (k = 0; k < 4; k++) {
			w = start + paddr[i] + taddr[k];
			for (j = 0; j < 64; j++) {
				c1 = data[w++];
				if ((c1 & 0x80) == 0x80) {
					j += (c1 & 0x7F);
					continue;
				}
				c2 = data[w++];
				c3 = data[w++];

				ins = c1 & 0x1F;
				note = c2 & 0x3F;
				fxt = ((c1 >> 5) & 0x03);
				c4 = ((c2 >> 4) & 0x0C);
				fxt |= c4;
				fxp = c3;

				pat[j * 16 + k * 4] = (ins & 0xf0);

				if (note != 0) {
					pat[j * 16 + k * 4] |=
						ptk_table[note - 1][0];
					pat[j * 16 + k * 4 + 1] =
						ptk_table[note - 1][1];
				}

				pat[j * 16 + k * 4 + 2] = (ins << 4) & 0xf0;
				pat[j * 16 + k * 4 + 2] |= fxt;
				pat[j * 16 + k * 4 + 3] = fxp;
			}
		}
		fwrite (pat, 1024, 1, out);
	}

	/* sample data */
	for (i = 0; i < 31; i++) {
		w = start + idata_addr[i];
		fwrite (&data[w], isize[i], 1, out);
	}

	return 0;
}


static int test_stim (uint8 *data, int s)
{
	int j, k, l;
	int start = 0;

	PW_REQUEST_DATA (s, 150);

	if (data[0]!='S' || data[1]!= 'T' || data[2]!='I' || data[3]!='M')
		return -1;

	/*  */
	j = ((data[start + 4] << 24) + (data[start + 5] << 16) +
		(data[start + 6] << 8) + data[start + 7]);
	if (j < 406)
		return -1;

	/* size of the pattern list */
	k = ((data[start + 18] << 8) + data[start + 19]);
	if (k > 128)
		return -1;

	/* nbr of pattern saved */
	k = ((data[start + 18] << 8) + data[start + 19]);
	if (k > 64 || k == 0)
		return -1;

	/* pattern list */
	for (l = 0; l < 128; l++) {
		if (data[start + 22 + l] > k)
			return -1;
	}

#if 0
	/* test sample sizes */
	ssize = 0;
	for (l = 0; l < 31; l++) {
		/* addresse de la table */
		o = start + j + l * 4;

		/* address du sample */
		k = ((data[o] << 24) + (data[o + 1] << 16) +
			(data[o + 2] << 8) + data[o + 3]);

		/* taille du smp */
		m = ((data[o + k - l * 4] << 8) + data[o + k + 1 - l * 4]) * 2;

		ssize += m;
	}

	if (ssize <= 4)
		return -1;
#endif

	/* ssize is the size of the sample data */
	/* j is the address of the sample desc */

	return 0;
}
