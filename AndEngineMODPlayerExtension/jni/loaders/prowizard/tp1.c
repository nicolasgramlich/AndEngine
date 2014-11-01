/*
 *   TrackerPacker_v1.c   1998 (c) Asle / ReDoX
 *
 * Converts TP1 packed MODs back to PTK MODs
 * thanks to Gryzor and his ProWizard tool ! ... without it, this prog
 * would not exist !!!
 *
*/

#include <string.h>
#include <stdlib.h>

void Depack_TP1 (FILE * in, FILE * out)
{
	uint8 c1 = 0x00, c2 = 0x00, c3 = 0x00, c4 = 0x00;
	uint8 ptk_table[37][2];
	uint8 pnum[128];
	uint8 Pattern[1024];
	uint8 *tmp;
	uint8 note, ins, fxt, fxp;
	uint8 PatMax = 0x00;
	uint8 PatPos;
	long paddr[128];
	long i = 0, j = 0;
	long paddr_tmp[128];
	long tmp_ptr;
	long Start_Pat_Address = 999999l;
	long Whole_Sample_Size = 0;
	long Sample_Data_Address;
	// FILE *in,*out;

#include "ptktable.h"

	if (Save_Status == BAD)
		return;

	memset(paddr, 0, 128 * 4);
	memset(paddr_tmp, 0, 128 * 4);
	memset(pnum, 0, 128);

	// in = fdopen (fd_in, "rb");
	// sprintf ( Depacked_OutName , "%ld.mod" , Cpt_Filename-1 );
	// out = fdopen (fd_out, "w+b");

	/* title */
	fseek (in, 8, 0);	/* SEEK_SET */
	tmp = (uint8 *) malloc (20);
	memset(tmp, 0, 20);
	fread (tmp, 20, 1, in);
	fwrite (tmp, 20, 1, out);
	free (tmp);

	/* sample data address */
	fread (&c1, 1, 1, in);
	fread (&c2, 1, 1, in);
	fread (&c3, 1, 1, in);
	fread (&c4, 1, 1, in);
	Sample_Data_Address =
		(c1 << 24) + (c2 << 16) + (c3 << 8) + c4;
/*printf ( "sample data address : %ld\n" , Sample_Data_Address );*/

	for (i = 0; i < 31; i++) {
		c1 = 0x00;
		for (j = 0; j < 22; j++)	/*sample name */
			fwrite (&c1, 1, 1, out);

		/* read fine */
		fread (&c3, 1, 1, in);

		/* read volume */
		fread (&c4, 1, 1, in);

		/* size */
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		Whole_Sample_Size += (((c1 << 8) + c2) * 2);
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);

		/* write finetune */
		fwrite (&c3, 1, 1, out);

		/* write volume */
		fwrite (&c4, 1, 1, out);

		fread (&c1, 1, 1, in);	/* loop start */
		fread (&c2, 1, 1, in);
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);

		fread (&c1, 1, 1, in);	/* loop size */
		fread (&c2, 1, 1, in);
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);

	}
	/*printf ( "Whole sample size : %ld\n" , Whole_Sample_Size ); */

	/* read size of pattern table */
	fseek (in, 281, 0);
	fread (&PatPos, 1, 1, in);
	PatPos += 0x01;
	fwrite (&PatPos, 1, 1, out);

	/* ntk byte */
	c1 = 0x7f;
	fwrite (&c1, 1, 1, out);

	for (i = 0; i < PatPos; i++) {
		fseek (in, 2, 1);
		fread (&c3, 1, 1, in);
		fread (&c4, 1, 1, in);
		paddr[i] = (c3 << 8) + c4;
		if (Start_Pat_Address > paddr[i])
			Start_Pat_Address = paddr[i];
/*fprintf ( info , "%3ld: %ld\n" , i,paddr[i] );*/
	}

	/* ordering of patterns addresses */

	tmp_ptr = 0;
	for (i = 0; i < PatPos; i++) {
		if (i == 0) {
			pnum[0] = 0x00;
			paddr_tmp[tmp_ptr] = paddr[tmp_ptr];
			tmp_ptr++;
			continue;
		}

		for (j = 0; j < i; j++) {
			if (paddr[i] == paddr[j]) {
				pnum[i] = pnum[j];
				break;
			}
		}
		if (j == i) {
			paddr_tmp[tmp_ptr] = paddr[i];
			pnum[i] = tmp_ptr++;
		}
	}

/*
  for ( i=0 ; i<PatPos ; i++ )
  {
    fprintf ( info , "%x, %ld\n" , pnum[i],paddr_tmp[i] );
  }
*/


	PatMax = tmp_ptr;
	/*printf ( "Highest pattern number : %d\n" , PatMax-1 ); */


	/* write pattern list */
	fwrite (pnum, 128, 1, out);


	/* ID string */
	c1 = 'M';
	c2 = '.';
	c3 = 'K';
	fwrite (&c1, 1, 1, out);
	fwrite (&c2, 1, 1, out);
	fwrite (&c3, 1, 1, out);
	fwrite (&c2, 1, 1, out);

	/*printf ( "address of the first pattern : %ld\n" , Start_Pat_Address ); */
	fseek (in, Start_Pat_Address, 0);

	/* pattern datas */

	j = 0;
	/*printf ( "converting pattern data " ); */
	for (i = 0; i < PatMax; i++) {
/*fprintf ( info , "\npattern %ld: (at: %ld)\n\n" , i,paddr_tmp[i] );*/
		fseek (in, paddr_tmp[i], 0);
		memset(Pattern, 0, 1024);
		for (j = 0; j < 256; j++) {
			fread (&c1, 1, 1, in);
/*fprintf ( info , "%ld: %2x," , k , c1 );*/
			if (c1 == 0xC0) {
/*fprintf ( info , " <--- empty\n" );*/
				continue;
			}
			if ((c1 & 0xC0) == 0x80) {
				fread (&c2, 1, 1, in);
/*fprintf ( info , "%2x ,\n" , c2 );*/
				fxt = (c1 >> 2) & 0x0f;
				fxp = c2;
				Pattern[j * 4 + 2] = fxt;
				Pattern[j * 4 + 3] = fxp;
				continue;
			}
			fread (&c2, 1, 1, in);
			fread (&c3, 1, 1, in);
/*fprintf ( info , "%2x, %2x\n" , c2 , c3 );*/

			ins = ((c2 >> 4) & 0x0f) | ((c1 << 4) & 0x10);
			note = c1 & 0xFE;
			fxt = c2 & 0x0F;
			fxp = c3;

			Pattern[j * 4] = ins & 0xf0;
			Pattern[j * 4] |= ptk_table[(note / 2)][0];
			Pattern[j * 4 + 1] = ptk_table[(note / 2)][1];
			Pattern[j * 4 + 2] = (ins << 4) & 0xf0;
			Pattern[j * 4 + 2] |= fxt;
			Pattern[j * 4 + 3] = fxp;
		}
		fwrite (Pattern, 1024, 1, out);
		/*printf ( "." ); */
	}
	/*printf ( "\n" ); */

	/* Sample data */
	fseek (in, Sample_Data_Address, 0);	/* SEEK_SET */
	tmp = (uint8 *) malloc (Whole_Sample_Size);
	fread (tmp, Whole_Sample_Size, 1, in);
	fwrite (tmp, Whole_Sample_Size, 1, out);
	free (tmp);


	Crap ("TP1:Tracker Packer 1", BAD, BAD, out);

	fflush (in);
	fflush (out);

	printf ("done\n");
	return;			/* useless ... but */
}


void testTP1 (void)
{
	start = i;

	/* size of the module */
	ssize =
		((data[start + 4] << 24) +
		(data[start + 5] << 16) +
		(data[start + 6] << 8) +
		data[start + 7]);
	if ((ssize < 794) || (ssize > 2129178l)) {
		Test = BAD;
		return;
	}

	/* test finetunes */
	for (k = 0; k < 31; k++) {
		if (data[start + 32 + k * 8] > 0x0f) {
			Test = BAD;
			return;
		}
	}

	/* test volumes */
	for (k = 0; k < 31; k++) {
		if (data[start + 33 + k * 8] > 0x40) {
			Test = BAD;
			return;
		}
	}

	/* sample data address */
	l = ((data[start + 28] << 24) +
		(data[start + 29] << 16) +
		(data[start + 30] << 8) +
		data[start + 31]);
	if ((l == 0) || (l > ssize)) {
		Test = BAD;
		return;
	}

	/* test sample sizes */
	for (k = 0; k < 31; k++) {
		j =
			(data[start + k * 8 + 34] << 8) +
			data[start + k * 8 + 35];
		m =
			(data[start + k * 8 + 36] << 8) +
			data[start + k * 8 + 37];
		n =
			(data[start + k * 8 + 38] << 8) +
			data[start + k * 8 + 39];
		j *= 2;
		m *= 2;
		n *= 2;
		if ((j > 0xFFFF) || (m > 0xFFFF) || (n > 0xFFFF)) {
/*printf ( "#5 Start:%ld\n" , start );*/
			Test = BAD;
			return;
		}
		if ((m + n) > (j + 2)) {
/*printf ( "#5,1 Start:%ld\n" , start );*/
			Test = BAD;
			return;
		}
		if ((m != 0) && (n <= 2)) {
/*printf ( "#5,2 Start:%ld\n" , start );*/
			Test = BAD;
			return;
		}
	}

	/* pattern list size */
	l = data[start + 281];
	if ((l == 0) || (l > 128)) {
		Test = BAD;
		return;
	}

	/* ssize is the size of the module :) */
	Test = GOOD;
}
