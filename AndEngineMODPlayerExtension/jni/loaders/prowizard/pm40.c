/*
 *   Promizer_40.c   1997 (c) Asle / ReDoX
 *
 * Converts PM40 packed MODs back to PTK MODs
 *
*/

#include <string.h>
#include <stdlib.h>

#define ON  0
#define OFF 1
#define SAMPLE_DESC         264
#define ADDRESS_SAMPLE_DATA 512
#define ADDRESS_REF_TABLE   516
#define PATTERN_DATA        520

void Depack_PM40 (FILE * in, FILE * out)
{
	uint8 c1 = 0x00, c2 = 0x00, c3 = 0x00, c4 = 0x00;
	uint8 PatPos = 0x00;
	short pat_max = 0;
	long tmp_ptr, tmp1, tmp2;
	short refmax = 0;
	uint8 pnum[128];
	uint8 pnum_tmp[128];
	long paddr[128];
	long paddr_tmp[128];
	long paddr_tmp2[128];
	short pptr[64][256];
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

	/* write title */
	for (i = 0; i < 20; i++)	/* title */
		fwrite (&c1, 1, 1, out);

	/* read and write sample headers */
	/*printf ( "Converting sample headers ... " ); */
	fseek (in, SAMPLE_DESC, 0);
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
	/*printf ( "ok\n" ); */

	/* read and write the size of the pattern list */
	fseek (in, 7, 0);	/* SEEK_SET */
	fread (&PatPos, 1, 1, in);
	fwrite (&PatPos, 1, 1, out);

	/* NoiseTracker restart byte */
	c1 = 0x7f;
	fwrite (&c1, 1, 1, out);


	/* pattern addresses */
	fseek (in, 8, 0);	/* SEEK_SET */
	for (i = 0; i < 128; i++) {
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		paddr[i] = (c1 << 8) + c2;
	}

	/* ordering of patterns addresses */
	/* PatPos contains the size of the pattern list .. */
	/*printf ( "Creating pattern list ... " ); */
	tmp_ptr = 0;
	for (i = 0; i < PatPos; i++) {
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
	/*printf ( "ok\n" ); */

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
	psize = (8 + j) - PATTERN_DATA;
/*  printf ( "Pattern data size : %ld\n" , psize );*/

	/* go back to pattern data starting address */
	fseek (in, PATTERN_DATA, 0);	/* SEEK_SET */
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
	fseek (in, 8 + j, 0);	/* SEEK_SET */
/*  printf ( "address of 'reference table' : %ld\n" , ftell (in ) );*/
	refmax += 1;		/* coz 1st value is 0 and will be empty in this table */
	i = refmax * 4;	/* coz each block is 4 bytes long */
	reftab = (uint8 *) malloc (i);
	memset(reftab, 0, i);
	fread (&reftab[4], i, 1, in);

	/* go back to pattern data starting address */
	fseek (in, PATTERN_DATA, 0);	/* SEEK_SET */
/*  printf ( "Highest pattern number : %d\n" , pat_max );*/

	/*printf ( "Computing the pattern datas " ); */
	k = 0;
	for (j = 0; j <= pat_max; j++) {
		for (i = 0; i < 64; i++) {
			/* VOICE #1 */

			fread (&c1, 1, 1, in);
			k += 1;
			fread (&c2, 1, 1, in);
			k += 1;
			ins = reftab[((c1 << 8) + c2) * 4];
			note = reftab[((c1 << 8) + c2) * 4 + 1];

			Pattern[j][i * 16] = (ins & 0xf0);
			Pattern[j][i * 16] |= ptk_table[note][0];
			Pattern[j][i * 16 + 1] = ptk_table[note][1];
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
			note = reftab[((c1 << 8) + c2) * 4 + 1];

			Pattern[j][i * 16 + 4] = (ins & 0xf0);
			Pattern[j][i * 16 + 4] |= ptk_table[note][0];
			Pattern[j][i * 16 + 5] = ptk_table[note][1];
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
			note = reftab[((c1 << 8) + c2) * 4 + 1];

			Pattern[j][i * 16 + 8] = (ins & 0xf0);
			Pattern[j][i * 16 + 8] |= ptk_table[note][0];
			Pattern[j][i * 16 + 9] = ptk_table[note][1];
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
			note = reftab[((c1 << 8) + c2) * 4 + 1];

			Pattern[j][i * 16 + 12] = (ins & 0xf0);
			Pattern[j][i * 16 + 12] |= ptk_table[note][0];
			Pattern[j][i * 16 + 13] = ptk_table[note][1];
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
		/*printf ( "." ); */
	}
	free (reftab);
	/*printf ( " ok\n" ); */


	/* get address of sample data .. and go there */
	/*printf ( "Saving sample datas ... " ); */
	fseek (in, ADDRESS_SAMPLE_DATA, 0);	/* SEEK_SET */
	fread (&c1, 1, 1, in);
	fread (&c2, 1, 1, in);
	fread (&c3, 1, 1, in);
	fread (&c4, 1, 1, in);
	SDAV = (c1 << 24) + (c2 << 16) + (c3 << 8) + c4;
	fseek (in, 4 + SDAV, 0);	/* SEEK_SET */


	/* read and save sample data */
/*  printf ( "out: where before saving sample data : %ld\n" , ftell ( out ) );*/
/*  printf ( "Whole sample size : %ld\n" , ssize );*/
	sdata = (uint8 *) malloc (ssize);
	fread (sdata, ssize, 1, in);
	fwrite (sdata, ssize, 1, out);
	free (sdata);
	/*printf ( " ok\n" ); */

	Crap ("PM40:Promizer 4.0", BAD, BAD, out);

	fflush (in);
	fflush (out);

	printf ("done\n");
	return;			/* useless ... but */
}


void testPM40 (void)
{
	start = i;

	/* size of the pattern list */
	j = data[start + 7];
	if (j > 0x7f) {
/*printf ( "#1 Start:%ld\n" , start );*/
		Test = BAD;
		return;
	}
	/* j is the size of the pattern list */

	/* finetune */
	for (k = 0; k < 31; k++) {
		if (data[start + k * 8 + 266] > 0x0f) {
/*printf ( "#2 Start:%ld\n" , start );*/
			Test = BAD;
			return;
		}
	}

	/* volume */
	for (k = 0; k < 31; k++) {
		if (data[start + k * 8 + 267] > 0x40) {
/*printf ( "#3 Start:%ld\n" , start );*/
			Test = BAD;
			return;
		}
	}

	/* sample data address */
	l = ((data[start + 512] << 24) +
		(data[start + 513] << 16) +
		(data[start + 514] << 8) +
		data[start + 515]);
	if ((l <= 520) || (l > 2500000l)) {
/*printf ( "#4 Start:%ld\n" , start );*/
		Test = BAD;
		return;
	}

	/* l is the sample data address */
	Test = GOOD;
}
