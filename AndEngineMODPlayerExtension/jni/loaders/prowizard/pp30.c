/*
 *   ProPacker_30.c   1997 (c) Asle / ReDoX
 *
 * Converts PP30 packed MODs back to PTK MODs
 * thanks to Gryzor and his ProWizard tool ! ... without it, this prog
 * would not exist !!!
 *
*/

#include <string.h>
#include <stdlib.h>

void Depack_PP30 (FILE * in, FILE * out)
{
	uint8 c1 = 0x00, c2 = 0x00, c3 = 0x00, c4 = 0x00;
	uint8 ptable[128];
	short Max = 0;
	uint8 Tracks_Numbers[4][128];
	short Tracks_PrePointers[512][64];
	uint8 NOP = 0x00;	/* number of pattern */
	uint8 *reftab;
	uint8 Pattern[1024];
	long i = 0, j = 0;
	long ssize = 0;
	long RTS = 0;		/* Reference Table Size */
	// FILE *in,*out;

	if (Save_Status == BAD)
		return;

	memset(ptable, 0, 128);
	memset(Tracks_Numbers, 0, 4 * 128);
	memset(Tracks_PrePointers, 0, 512 * 64);

	// in = fdopen (fd_in, "rb");
	// sprintf ( Depacked_OutName , "%ld.mod" , Cpt_Filename-1 );
	// out = fdopen (fd_out, "w+b");

	for (i = 0; i < 20; i++)	/* title */
		fwrite (&c1, 1, 1, out);

	for (i = 0; i < 31; i++) {
		c1 = 0x00;
		for (j = 0; j < 22; j++)	/*sample name */
			fwrite (&c1, 1, 1, out);

		fread (&c1, 1, 1, in);	/* size */
		fread (&c2, 1, 1, in);
		ssize += (((c1 << 8) + c2) * 2);
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);
		fread (&c1, 1, 1, in);	/* finetune */
		fwrite (&c1, 1, 1, out);
		fread (&c1, 1, 1, in);	/* volume */
		fwrite (&c1, 1, 1, out);
		fread (&c1, 1, 1, in);	/* loop start */
		fread (&c2, 1, 1, in);
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);
		fread (&c1, 1, 1, in);	/* loop size */
		fread (&c2, 1, 1, in);
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);
	}

	/* pattern table lenght */
	fread (&NOP, 1, 1, in);
	fwrite (&NOP, 1, 1, out);

	/*printf ( "Number of patterns : %d\n" , NOP ); */

	/* NoiseTracker restart byte */
	fread (&c1, 1, 1, in);
	fwrite (&c1, 1, 1, out);

	Max = 0;
	for (j = 0; j < 4; j++) {
		for (i = 0; i < 128; i++) {
			fread (&c1, 1, 1, in);
			Tracks_Numbers[j][i] = c1;
			if (Tracks_Numbers[j][i] > Max)
				Max = Tracks_Numbers[j][i];
		}
	}

	/* write pattern table without any optimizing ! */
	for (c1 = 0x00; c1 < NOP; c1++)
		fwrite (&c1, 1, 1, out);
	c4 = 0x00;
	for (; c1 < 128; c1++)
		fwrite (&c4, 1, 1, out);

	c1 = 'M';
	c2 = '.';
	c3 = 'K';

	fwrite (&c1, 1, 1, out);
	fwrite (&c2, 1, 1, out);
	fwrite (&c3, 1, 1, out);
	fwrite (&c2, 1, 1, out);



	/* PATTERN DATA code starts here */

	/*printf ( "Highest track number : %d\n" , Max ); */
	for (j = 0; j <= Max; j++) {
		for (i = 0; i < 64; i++) {
			fread (&c1, 1, 1, in);
			fread (&c2, 1, 1, in);
			Tracks_PrePointers[j][i] = ((c1 << 8) + c2) / 4;
		}
	}

	/* read "reference table" size */
	fread (&c1, 1, 1, in);
	fread (&c2, 1, 1, in);
	fread (&c3, 1, 1, in);
	fread (&c4, 1, 1, in);

	RTS = (c1 << 24) + (c2 << 16) + (c3 << 8) + c4;


	/* read "reference Table" */
	reftab = (uint8 *) malloc (RTS);
	fread (reftab, RTS, 1, in);

	/* NOW, the real shit takes place :) */
	for (i = 0; i < NOP; i++) {
		memset(Pattern, 0, 1024);
		for (j = 0; j < 64; j++) {

			Pattern[j * 16] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[0][i]][j] * 4];
			Pattern[j * 16 + 1] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[0][i]][j] * 4 + 1];
			Pattern[j * 16 + 2] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[0][i]][j] * 4 + 2];
			Pattern[j * 16 + 3] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[0][i]][j] * 4 + 3];

			Pattern[j * 16 + 4] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[1][i]][j] * 4];
			Pattern[j * 16 + 5] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[1][i]][j] * 4 + 1];
			Pattern[j * 16 + 6] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[1][i]][j] * 4 + 2];
			Pattern[j * 16 + 7] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[1][i]][j] * 4 + 3];

			Pattern[j * 16 + 8] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[2][i]][j] * 4];
			Pattern[j * 16 + 9] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[2][i]][j] * 4 + 1];
			Pattern[j * 16 + 10] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[2][i]][j] * 4 + 2];
			Pattern[j * 16 + 11] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[2][i]][j] * 4 + 3];

			Pattern[j * 16 + 12] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[3][i]][j] * 4];
			Pattern[j * 16 + 13] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[3][i]][j] * 4 + 1];
			Pattern[j * 16 + 14] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[3][i]][j] * 4 + 2];
			Pattern[j * 16 + 15] =
				reftab[Tracks_PrePointers
				[Tracks_Numbers[3][i]][j] * 4 + 3];


		}
		fwrite (Pattern, 1024, 1, out);
	}

	free (reftab);


	/* Now, it's sample data ... though, VERY quickly handled :) */
	/* thx GCC ! (GNU C COMPILER). */

	/*printf ( "Total sample size : %ld\n" , ssize ); */
	reftab = (uint8 *) malloc (ssize);
	fread (reftab, ssize, 1, in);
	fwrite (reftab, ssize, 1, out);
	free (reftab);


	Crap ("PP30:ProPacker v3.0", BAD, BAD, out);

	fflush (in);
	fflush (out);

	printf ("done\n");
	return;			/* useless ... but */
}


void testPP30 (void)
{
	/* test #1 */
	if (i < 3) {
/*printf ( "#1 (i:%ld)\n" , i );*/
		Test = BAD;
		return;
	}

	/* test #2 */
	start = i - 3;
	l = 0;
	for (j = 0; j < 31; j++) {
		k =
			(((data[start + j * 8] << 8) +
				 data[start + j * 8 +
					1]) * 2);
		l += k;
		/* finetune > 0x0f ? */
		if (data[start + 8 * j + 2] > 0x0f) {
/*printf ( "#2 (start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		/* volume > 0x40 ? */
		if (data[start + 8 * j + 3] > 0x40) {
/*printf ( "#2,0 (start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		/* loop start > size ? */
		if ((((data[start + 4 + j * 8] << 8) +
					data[start + 5 +
						j * 8]) * 2) > k) {
			Test = BAD;
/*printf ( "#2,1 (start:%ld)\n" , start );*/
			return;
		}
	}
	if (l <= 2) {
/*printf ( "#2,2 (start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}

	/* test #3   about size of pattern list */
	l = data[start + 248];
	if ((l > 127) || (l == 0)) {
/*printf ( "#3 (start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}

	/* get the highest track value */
	k = 0;
	for (j = 0; j < 512; j++) {
		l = data[start + 250 + j];
		if (l > k)
			k = l;
	}
	/* k is the highest track number */
	k += 1;
	k *= 64;

	/* test #4  track data value *4 ? */
	/* ssize used as a variable .. set to 0 afterward */
	ssize = 0;
	if (((k * 2) + start) > in_size) {
		Test = BAD;
		ssize = 0;
		return;
	}
	for (j = 0; j < k; j++) {
		l =
			(data[start + 762 + j * 2] << 8) +
			data[start + 763 + j * 2];
		if (l > ssize)
			ssize = l;
		if (((l * 4) / 4) != l) {
/*printf ( "#4 (start:%ld)(where:%ld)\n" , start,start+j*2+762 );*/
			Test = BAD;
			ssize = 0;
			return;
		}
	}

	/* test #5  reference table size *4 ? */
	/* ssize is the highest reference number */
	k *= 2;
	ssize /= 4;
	l = (data[start + k + 762] << 24)
		+ (data[start + k + 763] << 16)
		+ (data[start + k + 764] << 8)
		+ data[start + k + 765];
	if (l > 65535) {
		Test = BAD;
		ssize = 0;
		return;
	}
	if (l != ((ssize + 1) * 4)) {
/*printf ( "#5 (start:%ld)(where:%ld)\n" , start,(start+k+762) );*/
		Test = BAD;
		ssize = 0;
		return;
	}

	/* test #6  data in reference table ... */
	for (j = 0; j < (l / 4); j++) {
		/* volume > 41 ? */
		if (((data[start + k + 766 + j * 4 +
						2] & 0x0f) == 0x0c)
			&& (data[start + k + 766 + j * 4 +
					3] > 0x41)) {
/*printf ( "#6 (vol > 40 at : %ld)\n" , start+k+766+j*4+2 );*/
			Test = BAD;
			ssize = 0;
			return;
		}
		/* break > 40 ? */
		if (((data[start + k + 766 + j * 4 +
						2] & 0x0f) == 0x0d)
			&& (data[start + k + 766 + j * 4 +
					3] > 0x40)) {
/*printf ( "#6,1\n" );*/
			Test = BAD;
			ssize = 0;
			return;
		}
		/* jump > 128 */
		if (((data[start + k + 766 + j * 4 +
						2] & 0x0f) == 0x0b)
			&& (data[start + k + 766 + j * 4 +
					3] > 0x7f)) {
/*printf ( "#6,2\n" );*/
			Test = BAD;
			ssize = 0;
			return;
		}
		/* smp > 1f ? */
		if ((data[start + k + 766 +
					j * 4] & 0xf0) > 0x10) {
/*printf ( "#6,3\n" );*/
			Test = BAD;
			ssize = 0;
			return;
		}
	}

	ssize = 0;
	Test = GOOD;
}
