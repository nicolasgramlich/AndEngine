/*
 *   Promizer_20.c   1997 (c) Asle / ReDoX
 *
 * Converts PM20 packed MODs back to PTK MODs
 *
*/

#include <string.h>
#include <stdlib.h>

#define ON  0
#define OFF 1
#define AFTER_REPLAY_CODE   5198
#define SAMPLE_DESC         5458
#define ADDRESS_SAMPLE_DATA 5706
#define ADDRESS_REF_TABLE   5710
#define PATTERN_DATA        5714

void Depack_PM20 (FILE * in, FILE * out)
{
	uint8 c1 = 0x00, c2 = 0x00, c3 = 0x00, c4 = 0x00;
	short pat_max = 0;
	long tmp_ptr, tmp1, tmp2;
	short refmax = 0;
	uint8 pnum[128];
	uint8 pnum_tmp[128];
	long paddr[128];
	long paddr_tmp[128];
	long paddr_tmp2[128];
	short pptr[64][256];
	uint8 NOP = 0x00;	/* number of pattern */
	uint8 *reftab;
	uint8 *sdata;
	uint8 Pattern[128][1024];
	long i = 0, j = 0, k = 0;
	long ssize = 0;
	long psize = 0l;
	long SDAV = 0l;
	uint8 FLAG = OFF;
	uint8 ptk_table[37][2];
	uint8 note, ins;
	// FILE *in,*out;

	if (Save_Status == BAD)
		return;

#include "ptktable.h"

	// in = fdopen (fd_in, "rb");
	// sprintf ( Depacked_OutName , "%ld.mod" , Cpt_Filename-1 );
	// out = fdopen (fd_out, "w+b");

	memset(pnum, 0, 128);
	memset(pnum_tmp, 0, 128);
	memset(pptr, 0, 64 << 8);
	memset(Pattern, 0, 128 * 1024);
	memset(paddr, 0, 128 * 4);
	memset(paddr_tmp, 0, 128 * 4);
	for (i = 0; i < 128; i++)
		paddr_tmp2[i] = 9999l;

	for (i = 0; i < 20; i++)	/* title */
		fwrite (&c1, 1, 1, out);

	/* bypass replaycode routine */
	fseek (in, SAMPLE_DESC, 0);	/* SEEK_SET */

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
		c1 /= 2;
		fwrite (&c1, 1, 1, out);
		fread (&c1, 1, 1, in);	/* volume */
		fwrite (&c1, 1, 1, out);
		fread (&c1, 1, 1, in);	/* loop start */
		fread (&c2, 1, 1, in);
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);
		fread (&c1, 1, 1, in);	/* loop size */
		fread (&c2, 1, 1, in);
		if ((c1 == 0x00) && (c2 == 0x00))
			c2 = 0x01;
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);
	}

	/* read REAL number of pattern */
	fseek (in, AFTER_REPLAY_CODE + 1, 0);	/* SEEK_SET */
	fread (&NOP, 1, 1, in);

	/*printf ( "REAL Number of patterns : %d\n" , NOP ); */

	/* read "used" size of pattern table */
	fseek (in, 1, 1);	/* SEEK_CUR */
	fread (&c1, 1, 1, in);
	c4 = c1 / 2;
	/*printf ( "Number of pattern in pattern list : %d\n" , c4 ); */

	/* write size of pattern list */
	fwrite (&c4, 1, 1, out);

	/* NoiseTracker restart byte */
	c1 = 0x7f;
	fwrite (&c1, 1, 1, out);

	for (i = 0; i < 128; i++) {
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		paddr[i] = (c1 << 8) + c2;
	}

	/* ordering of patterns addresses */
	/* c4 contains the size of the pattern list .. */
	tmp_ptr = 0;
	for (i = 0; i < c4; i++) {
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

	/* correct re-order */
  /********************/
	for (i = 0; i < c4; i++)
		paddr_tmp[i] = paddr[i];

      restart:
	for (i = 0; i < c4; i++) {
		for (j = 0; j < i; j++) {
			if (paddr_tmp[i] < paddr_tmp[j]) {
				tmp2 = pnum[j];
				pnum[j] = pnum[i];
				pnum[i] = tmp2;
				tmp1 = paddr_tmp[j];
				paddr_tmp[j] = paddr_tmp[i];
				paddr_tmp[i] = tmp1;
				goto restart;
			}
		}
	}

	j = 0;
	for (i = 0; i < c4; i++) {
		if (i == 0) {
			paddr_tmp2[j] = paddr_tmp[i];
			continue;
		}

		if (paddr_tmp[i] == paddr_tmp2[j])
			continue;
		paddr_tmp2[++j] = paddr_tmp[i];
	}

	for (c1 = 0x00; c1 < c4; c1++) {
		for (c2 = 0x00; c2 < c4; c2++)
			if (paddr[c1] == paddr_tmp2[c2]) {
				pnum_tmp[c1] = c2;
			}
	}

	for (i = 0; i < c4; i++)
		pnum[i] = pnum_tmp[i];

	/* write pattern table */
	for (c1 = 0x00; c1 < 128; c1++) {
		fwrite (&pnum[c1], 1, 1, out);
	}

	c1 = 'M';
	c2 = '.';
	c3 = 'K';

	fwrite (&c1, 1, 1, out);
	fwrite (&c2, 1, 1, out);
	fwrite (&c3, 1, 1, out);
	fwrite (&c2, 1, 1, out);


	/* a little pre-calc code ... no other way to deal with these unknown
	   pattern data sizes ! :( */
	/* so, first, we get the pattern data size .. */
	fseek (in, ADDRESS_REF_TABLE, 0);	/* SEEK_SET */
	fread (&c1, 1, 1, in);
	fread (&c2, 1, 1, in);
	fread (&c3, 1, 1, in);
	fread (&c4, 1, 1, in);
	j = (c1 << 24) + (c2 << 16) + (c3 << 8) + c4;
	psize = (AFTER_REPLAY_CODE + j) - PATTERN_DATA;
	/*printf ( "Pattern data size : %ld\n" , psize ); */

	/* go back to pattern data starting address */
	fseek (in, 5226, 0);	/* SEEK_SET */
	/* now, reading all pattern data to get the max value of note */
	for (j = 0; j < psize; j += 2) {
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		if (((c1 << 8) + c2) > refmax)
			refmax = (c1 << 8) + c2;
	}
/*
  printf ( "* refmax = %d\n" , refmax );
  printf ( "* where : %ld\n" , ftell ( in ) );
*/
	/* read "reference Table" */
	fseek (in, ADDRESS_REF_TABLE, 0);	/* SEEK_SET */
	fread (&c1, 1, 1, in);
	fread (&c2, 1, 1, in);
	fread (&c3, 1, 1, in);
	fread (&c4, 1, 1, in);
	j = (c1 << 24) + (c2 << 16) + (c3 << 8) + c4;
	fseek (in, AFTER_REPLAY_CODE + j, 0);	/* SEEK_SET */
	/*printf ( "address of 'reference table' : %ld\n" , ftell (in ) ); */
	refmax += 1;		/* coz 1st value is 0 ! */
	i = refmax * 4;	/* coz each block is 4 bytes long */
	reftab = (uint8 *) malloc (i);
	fread (reftab, i, 1, in);

	/* go back to pattern data starting address */
	fseek (in, PATTERN_DATA, 0);	/* SEEK_SET */
	/*printf ( "Highest pattern number : %d\n" , pat_max ); */


	k = 0;
	for (j = 0; j <= pat_max; j++) {
		for (i = 0; i < 64; i++) {
			/* VOICE #1 */

			fread (&c1, 1, 1, in);
			k += 1;
			fread (&c2, 1, 1, in);
			k += 1;
			ins = reftab[((c1 << 8) + c2) * 4];
			ins = ins >> 2;
			note = reftab[((c1 << 8) + c2) * 4 + 1];

			Pattern[j][i * 16] = (ins & 0xf0);
			Pattern[j][i * 16] |= ptk_table[(note / 2)][0];
			Pattern[j][i * 16 + 1] = ptk_table[(note / 2)][1];
			Pattern[j][i * 16 + 2] =
				reftab[((c1 << 8) + c2) * 4 + 2];
			Pattern[j][i * 16 + 2] |= ((ins << 4) & 0xf0);
			Pattern[j][i * 16 + 3] =
				reftab[((c1 << 8) + c2) * 4 + 3];

			if (((Pattern[j][i * 16 + 2] & 0x0f) == 0x0d) ||
				((Pattern[j][i * 16 + 2] & 0x0f) == 0x0b)) {
				FLAG = ON;
			}

			/* VOICE #2 */

			fread (&c1, 1, 1, in);
			k += 1;
			fread (&c2, 1, 1, in);
			k += 1;
			ins = reftab[((c1 << 8) + c2) * 4];
			ins = ins >> 2;
			note = reftab[((c1 << 8) + c2) * 4 + 1];

			Pattern[j][i * 16 + 4] = (ins & 0xf0);
			Pattern[j][i * 16 + 4] |= ptk_table[(note / 2)][0];
			Pattern[j][i * 16 + 5] = ptk_table[(note / 2)][1];
			Pattern[j][i * 16 + 6] =
				reftab[((c1 << 8) + c2) * 4 + 2];
			Pattern[j][i * 16 + 6] |= ((ins << 4) & 0xf0);
			Pattern[j][i * 16 + 7] =
				reftab[((c1 << 8) + c2) * 4 + 3];

			if (((Pattern[j][i * 16 + 6] & 0x0f) == 0x0d) ||
				((Pattern[j][i * 16 + 6] & 0x0f) == 0x0b)) {
				FLAG = ON;
			}

			/* VOICE #3 */

			fread (&c1, 1, 1, in);
			k += 1;
			fread (&c2, 1, 1, in);
			k += 1;
			ins = reftab[((c1 << 8) + c2) * 4];
			ins = ins >> 2;
			note = reftab[((c1 << 8) + c2) * 4 + 1];

			Pattern[j][i * 16 + 8] = (ins & 0xf0);
			if (note != 0)
				Pattern[j][i * 16 + 8] |= ptk_table[(note / 2)][0];
			Pattern[j][i * 16 + 9] = ptk_table[(note / 2)][1];
			Pattern[j][i * 16 + 10] =
				reftab[((c1 << 8) + c2) * 4 + 2];
			Pattern[j][i * 16 + 10] |= ((ins << 4) & 0xf0);
			Pattern[j][i * 16 + 11] =
				reftab[((c1 << 8) + c2) * 4 + 3];

			if (((Pattern[j][i * 16 + 10] & 0x0f) == 0x0d) ||
				((Pattern[j][i * 16 + 10] & 0x0f) == 0x0b)) {
				FLAG = ON;
			}

			/* VOICE #4 */

			fread (&c1, 1, 1, in);
			k += 1;
			fread (&c2, 1, 1, in);
			k += 1;
			ins = reftab[((c1 << 8) + c2) * 4];
			ins = ins >> 2;
			note = reftab[((c1 << 8) + c2) * 4 + 1];

			Pattern[j][i * 16 + 12] = (ins & 0xf0);
			Pattern[j][i * 16 + 12] |= ptk_table[(note / 2)][0];
			Pattern[j][i * 16 + 13] = ptk_table[(note / 2)][1];
			Pattern[j][i * 16 + 14] =
				reftab[((c1 << 8) + c2) * 4 + 2];
			Pattern[j][i * 16 + 14] |= ((ins << 4) & 0xf0);
			Pattern[j][i * 16 + 15] =
				reftab[((c1 << 8) + c2) * 4 + 3];

			if (((Pattern[j][i * 16 + 14] & 0x0f) == 0x0d) ||
				((Pattern[j][i * 16 + 14] & 0x0f) == 0x0b)) {
				FLAG = ON;
			}

			if (FLAG == ON) {
				FLAG = OFF;
				break;
			}
		}
		fwrite (Pattern[j], 1024, 1, out);
	}

	free (reftab);

	/* get address of sample data .. and go there */
	fseek (in, ADDRESS_SAMPLE_DATA, 0);	/* SEEK_SET */
	fread (&c1, 1, 1, in);
	fread (&c2, 1, 1, in);
	fread (&c3, 1, 1, in);
	fread (&c4, 1, 1, in);
	SDAV = (c1 << 24) + (c2 << 16) + (c3 << 8) + c4;
	fseek (in, AFTER_REPLAY_CODE + SDAV, 0);	/* SEEK_SET */


	/* read and save sample data */
	/*printf ( "out: where before saving sample data : %ld\n" , ftell ( out ) ); */
	/*printf ( "Total sample size : %ld\n" , ssize ); */
	sdata = (uint8 *) malloc (ssize);
	fread (sdata, ssize, 1, in);
	fwrite (sdata, ssize, 1, out);
	free (sdata);


	Crap ("PM20:Promizer 2.0", BAD, BAD, out);

	fflush (in);
	fflush (out);

	printf ("done\n");
	return;			/* useless ... but */
}


void testPM2 (void)
{
	start = i;
	/* test 1 */
	if ((start + 5714) > in_size) {
		Test = BAD;
		return;
	}

	/* test 2 */
	if (data[start + 5094] != 0x03)
		/* not sure in fact ... */
		/* well, it IS the frequency table, it always seem */
		/* to be the 'standard one .. so here, there is 0358h */
	{
		Test = BAD;
		return;
	}

	/* test 3 */
	if (data[start + 5461] > 0x40)
		/* testing a volume */
	{
		Test = BAD;
		return;
	}

	Test = GOOD;
}
