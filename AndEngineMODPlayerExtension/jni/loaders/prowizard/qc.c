/*
 * QuadraComposer.c   Copyright (C) 1999 Asle / ReDoX
 *                    Modified by Claudio Matsuoka
 *
 * Converts QC MODs back to PTK MODs
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_emod (uint8 *, int);
static int depack_emod (FILE *, FILE *);

struct pw_format pw_emod = {
	"EMOD",
	"QuadraComposer",
	0x00,
	test_emod,
	depack_emod
};

static int depack_emod (FILE *in, FILE *out)
{
	uint8 c1, c2, c3, c4, c5;
	uint8 pat_pos;
	uint8 pat_max = 0x00;
	uint8 Real_pat_max = 0x00;
	uint8 *tmp;
	uint8 Row[16];
	uint8 Pattern[1024];
	uint8 nins = 0x00;
	uint8 Realnins = 0x00;
	uint8 nrow[128];
	long iaddr[32];
	long isize[32];
	long paddr[128];
	long i = 0, j = 0, k = 0;

	memset(iaddr, 0, 32 * 4);
	memset(isize, 0, 32 * 4);
	memset(paddr, 0, 128 * 4);
	memset(nrow, 0, 128);

	/* bypass ID's and chunk sizes */
	fseek (in, 22, 0);

	/* read and write title */
	for (i = 0; i < 20; i++) {
		fread (&c1, 1, 1, in);
		fwrite (&c1, 1, 1, out);
	}

	/* bypass composer and tempo */
	fseek (in, 21, 1);

	/* read number of samples */
	fread (&nins, 1, 1, in);

	/* write empty 930 sample header */
	tmp = (uint8 *) malloc (930);
	memset(tmp, 0, 930);
	/* puts some $01 for replen */
	for (i = 0; i < 31; i++)
		tmp[i * 30 + 29] = 0x01;
	fwrite (tmp, 930, 1, out);
	free (tmp);

	/* read and write sample descriptions */
/*printf ( "sample number:" );*/
	for (i = 0; i < nins; i++) {
		/* read sample number byte */
		fread (&c5, 1, 1, in);
		if (c5 > Realnins)
			Realnins = c5;
/*printf ( "%d," , c5 );*/
		fseek (out, 20 + (c5 - 1) * 30, 0);

		/* read volume */
		fread (&c4, 1, 1, in);

		/* read size (/2 like ptk) */
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		isize[c5] = (((c1 << 8) + c2) * 2);

		/* read/write sample name */
		for (j = 0; j < 20; j++) {
			fread (&c3, 1, 1, in);
			fwrite (&c3, 1, 1, out);
		}
		/* fill to 22 with $00 */
		c3 = 0x00;
		fwrite (&c3, 1, 1, out);
		fwrite (&c3, 1, 1, out);

		/* write size */
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);

		/* bypass control byte */
		fseek (in, 1, 1);

		/* read/write finetune */
		fread (&c1, 1, 1, in);
		fwrite (&c1, 1, 1, out);

		/* write volume */
		fwrite (&c4, 1, 1, out);

		/* read/write loops (start & len) */
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		if ((c1 == 0x00) && (c2 == 0x00))
			c2 = 0x01;
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);

		/* read address of this sample in the file */
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		fread (&c3, 1, 1, in);
		fread (&c4, 1, 1, in);
		iaddr[c5] = ((c1 << 24) +
			(c2 << 16) + (c3 << 8) + (c4));
	}
/*printf ( "\n" );*/
	fseek (out, 0, 2);

	/* patterns now */
	/* bypass "pad" ?!? */
	fread (&c1, 1, 1, in);
	if (c1 != 0x00)
		fseek (in, -1, 1);

	/* read number of pattern */
	fread (&pat_max, 1, 1, in);
/*  printf ( "\npat_max : %d (at %x)\n" , Pat_Max , ftell ( in ) );*/

	/* read patterns info */
/*printf ( "pattern numbers:" );*/
	for (i = 0; i < pat_max; i++) {
		/* read pattern number */
		fread (&c5, 1, 1, in);
/*printf ("%d," , c5);*/
		/* read number of rows for each pattern */
		fread (&nrow[c5], 1, 1, in);

		/* bypass pattern name */
		fseek (in, 20, 1);

		/* read pattern address */
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		fread (&c3, 1, 1, in);
		fread (&c4, 1, 1, in);
		paddr[c5] = ((c1 << 24) + (c2 << 16) + (c3 << 8) + (c4));
	}

	/* pattern list */
	/* bypass "pad" ?!? */
	fread (&c1, 1, 1, in);
	if (c1 != 0x00)
		fseek (in, -1, 1);

	/* read/write number of position */
	fread (&pat_pos, 1, 1, in);
	fwrite (&pat_pos, 1, 1, out);

	/* write noisetracker byte */
	c1 = 0x7f;
	fwrite (&c1, 1, 1, out);

	/* read/write pattern list */
	for (i = 0; i < pat_pos; i++) {
		fread (&c1, 1, 1, in);
		fwrite (&c1, 1, 1, out);
		if (c1 > Real_pat_max)
			Real_pat_max = c1;
	}
	/* fill up to 128 */
	c2 = 0x00;
	while (i < 128) {
		fwrite (&c2, 1, 1, out);
		i++;
	}

	/* write ptk's ID */
	c1 = 'M';
	c2 = '.';
	c3 = 'K';
	fwrite (&c1, 1, 1, out);
	fwrite (&c2, 1, 1, out);
	fwrite (&c3, 1, 1, out);
	fwrite (&c2, 1, 1, out);


	/* pattern data */
	for (i = 0; i <= Real_pat_max; i++) {
		memset(Pattern, 0, 1024);
		if (paddr[i] == 0l) {
			fwrite (Pattern, 1024, 1, out);
			printf ("-");
			continue;
		}
		fseek (in, paddr[i], 0);
		for (j = 0; j <= nrow[i]; j++) {
			memset(Row, 0, 16);
			fread (Row, 16, 1, in);
			for (k = 0; k < 4; k++) {
				/* fxt */
				Pattern[j * 16 + k * 4 + 2] = Row[k * 4 + 2];

				/* fxt args */
				switch (Pattern[j * 16 + k * 4 + 2]) {
				case 0x09:
					/*printf ( "#" ); */
					Pattern[j * 16 + k * 4 + 3] =
						(Row[k * 4 + 3] * 2);
					break;
				case 0x0b:
					/*printf ( "!" ); */
					c4 = Row[k * 4 + 3] % 10;
					c3 = Row[k * 4 + 3] / 10;
					Pattern[j * 16 + k * 4 + 3] = 16;
					Pattern[j * 16 + k * 4 + 3] *= c3;
					Pattern[j * 16 + k * 4 + 3] += c4;
					break;
				case 0x0E:
					if ((Row[k * 4 + 3] & 0xf0) == 0xf0)
						Pattern[j * 16 + k * 4 + 3] =
							(Row[k * 4 + 3] -
							0x10);
					break;
				default:
					Pattern[j * 16 + k * 4 + 3] =
						Row[k * 4 + 3];
				}

				/* smp nbr (4 lower bits) */
				Pattern[j * 16 + k * 4 + 2] |=
					((Row[k * 4] << 4) & 0xf0);
				/* notes */
				c1 = Row[k * 4 + 1];
				if (c1 != 0xff) {
					Pattern[j * 16 + k * 4] = ptk_table[c1][0];
					Pattern[j * 16 + k * 4 + 1] =
						ptk_table[c1][1];
				}
				/* smp nbr (4 higher bits) */
				Pattern[j * 16 + k * 4] |=
					(Row[k * 4] & 0xf0);
			}
		}
		fwrite (Pattern, 1024, 1, out);
	}

	/* sample data */
	for (i = 1; i <= Realnins; i++) {
		if (isize[i] == 0) {
			continue;
		}
		fseek (in, iaddr[i], 0);
		tmp = (uint8 *) malloc (isize[i]);
		fread (tmp, isize[i], 1, in);
		fwrite (tmp, isize[i], 1, out);
		free (tmp);
	}

	return 0;
}


static int test_emod (uint8 *data, int s)
{
	int start = 0;
	int l;

#if 0
	/* test #1 */
	if (i < 8) {
		Test = BAD;
		return;
	}
	start = i - 8;
#endif

	/* test #2 "FORM" & "EMIC" */
	if (data[start] != 'F' ||
		data[start + 1] != 'O' ||
		data[start + 2] != 'R' ||
		data[start + 3] != 'M' ||
		data[start + 12] != 'E' ||
		data[start + 13] != 'M' ||
		data[start + 14] != 'I' ||
		data[start + 15] != 'C')
		return -1;

	/* test number of samples */
	l = data[start + 63];
	if (l == 0x00 || l > 0x20)
		return -1;

	return 0;
}
