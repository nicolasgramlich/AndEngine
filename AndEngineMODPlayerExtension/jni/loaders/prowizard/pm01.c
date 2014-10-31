/*
 *   Promizer_0.1_Packer.c   1997 (c) Asle / ReDoX
 *
 * Converts back to ptk Promizer 0.1 packed MODs
 *
*/

#include <string.h>
#include <stdlib.h>

void Depack_PM01 (FILE * in, FILE * out)
{
	uint8 c1 = 0x00, c2 = 0x00, c3 = 0x00, c4 = 0x00;
	uint8 ptable[128];
	uint8 pat_pos;
	uint8 pat_max;
	uint8 ptk_table[37][2];
	uint8 *tmp;
	uint8 *PatternData;
	uint8 fin[31];
	uint8 Old_ins_Nbr[4];
	long i = 0, j = 0, k = 0, l = 0;
	long ssize = 0;
	long Pattern_Address[128];
	// FILE *in,*out;

#include "tuning.h"
#include "ptktable.h"

	if (Save_Status == BAD)
		return;

	memset(ptable, 0, 128);
	memset(Pattern_Address, 0, 128 * 4);
	memset(fin, 0, 31);
	memset(Old_ins_Nbr, 0, 4);

	// in = fdopen (fd_in, "rb");
	// sprintf ( Depacked_OutName , "%ld.mod" , Cpt_Filename-1 );
	// out = fdopen (fd_out, "w+b");

	/* write title */
	for (i = 0; i < 20; i++)	/* title */
		fwrite (&c1, 1, 1, out);

	/* read and write sample descriptions */
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
		fin[i] = c1;
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
	/*printf ( "Whole sample size : %ld\n" , ssize ); */

	/* pattern table lenght */
	fread (&c1, 1, 1, in);
	fread (&c2, 1, 1, in);
	pat_pos = ((c1 << 8) + c2) / 4;
	fwrite (&pat_pos, 1, 1, out);
	/*printf ( "Size of pattern list : %d\n" , pat_pos ); */

	/* write NoiseTracker byte */
	c1 = 0x7f;
	fwrite (&c1, 1, 1, out);

	/* read pattern address list */
	for (i = 0; i < 128; i++) {
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		fread (&c3, 1, 1, in);
		fread (&c4, 1, 1, in);
		Pattern_Address[i] =
			(c1 << 24) + (c2 << 16) +
			(c3 << 8) + c4;
	}

	/* deduce pattern list and write it */
	pat_max = 0x00;
	for (i = 0; i < 128; i++) {
		ptable[i] = Pattern_Address[i] / 1024;
		fwrite (&ptable[i], 1, 1, out);
		if (ptable[i] > pat_max)
			pat_max = ptable[i];
	}
	pat_max += 1;
	/*printf ( "Number of pattern : %d\n" , pat_max ); */

	/* write ptk's ID */
	c1 = 'M';
	c2 = '.';
	c3 = 'K';
	fwrite (&c1, 1, 1, out);
	fwrite (&c2, 1, 1, out);
	fwrite (&c3, 1, 1, out);
	fwrite (&c2, 1, 1, out);

	/* get pattern data size */
	fread (&c1, 1, 1, in);
	fread (&c2, 1, 1, in);
	fread (&c3, 1, 1, in);
	fread (&c4, 1, 1, in);
	j = (c1 << 24) + (c2 << 16) + (c3 << 8) + c4;
	/*printf ( "Size of the pattern data : %ld\n" , j ); */

	/* read and XOR pattern data */
	tmp = (uint8 *) malloc (j);
	PatternData = (uint8 *) malloc (j);
	memset(tmp, 0, j);
	fread (tmp, j, 1, in);
	for (k = 0; k < j; k++) {
		if (k % 4 == 3) {
			PatternData[k] =
				((240 - (tmp[k] & 0xf0)) +
				(tmp[k] & 0x0f));
			continue;
		}
		PatternData[k] = 255 - tmp[k];
	}

	/* all right, now, let's take care of these 'finetuned' value ... pfff */
	Old_ins_Nbr[0] = Old_ins_Nbr[1] = Old_ins_Nbr[2] = Old_ins_Nbr[3] =
		0x1f;
	memset(tmp, 0, j);
	for (i = 0; i < j / 4; i++) {
		c1 = PatternData[i * 4] & 0x0f;
		c2 = PatternData[i * 4 + 1];
		k = (c1 << 8) + c2;
		c3 =
			(PatternData[i * 4] & 0xf0) | ((PatternData[i * 4 +
					   2] >> 4) & 0x0f);
		if (c3 == 0)
			c3 = Old_ins_Nbr[i % 4];
		else
			Old_ins_Nbr[i % 4] = c3;
		if ((k != 0) && (fin[c3 - 1] != 0x00)) {
/*fprintf ( info , "! (at %ld)(smp:%x)(pitch:%ld)\n" , (i*4)+382 , c3 , k );*/
			for (l = 0; l < 36; l++) {
				if (k == tun_table[fin[c3 - 1]][l]) {
					tmp[i * 4] = ptk_table[l + 1][0];
					tmp[i * 4 + 1] = ptk_table[l + 1][1];
				}
			}
		} else {
			tmp[i * 4] = PatternData[i * 4] & 0x0f;
			tmp[i * 4 + 1] = PatternData[i * 4 + 1];
		}
		tmp[i * 4] |= (PatternData[i * 4] & 0xf0);
		tmp[i * 4 + 2] = PatternData[i * 4 + 2];
		tmp[i * 4 + 3] = PatternData[i * 4 + 3];
	}
	fwrite (tmp, j, 1, out);
	free (tmp);
	free (PatternData);

	/* sample data */
	tmp = (uint8 *) malloc (ssize);
	fread (tmp, ssize, 1, in);
	fwrite (tmp, ssize, 1, out);
	free (tmp);

	/* crap */
	Crap ("PM01:Promizer 0.1", BAD, BAD, out);

	fflush (in);
	fflush (out);

	printf ("done\n");
	return;			/* useless ... but */
}

#include <string.h>
#include <stdlib.h>

void testPM01 (void)
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
				 data[start + 1 +
					j * 8]) * 2);
		l += k;
		/* finetune > 0x0f ? */
		if (data[start + 2 + 8 * j] > 0x0f) {
/*printf ( "#2 (start:%ld)\n" , start );*/
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
	l =
		(data[start + 248] << 8) +
		data[start + 249];
	k = l / 4;
	if ((k * 4) != l) {
/*printf ( "#3 (start:%ld)(l:%ld)(k:%ld)\n" , start,l,k );*/
		Test = BAD;
		return;
	}
	if (k > 127) {
/*printf ( "#3,1 (start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}
	if (l == 0) {
/*printf ( "#3,2 (start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}

	/* test #4  size of all the pattern data */
	/* k contains the size of the pattern list */
	l = (data[start + 762] << 24)
		+ (data[start + 743] << 16)
		+ (data[start + 764] << 8)
		+ data[start + 765];
	if ((l < 1024) || (l > 131072)) {
/*printf ( "#4 (start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}

	/* test #5  first pattern address != $00000000 ? */
	l = (data[start + 250] << 24)
		+ (data[start + 251] << 16)
		+ (data[start + 252] << 8)
		+ data[start + 253];
	if (l != 0) {
/*printf ( "#5 (start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}

	/* test #6  pattern addresses */
	/* k is still ths size of the pattern list */
	for (j = 0; j < k; j++) {
		l =
			(data[start + 250 +
				 j * 4] << 24) +
			(data[start + 251 +
				j * 4] << 16) +
			(data[start + 252 + j * 4] << 8)
			+ data[start + 253 + j * 4];
		if (l > 131072) {
/*printf ( "#6 (start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		if (((l / 1024) * 1024) != l) {
			Test = BAD;
			return;
		}
	}

	/* test #7  last patterns in pattern table != $00000000 ? */
	j += 4;		/* just to be sure */
	while (j != 128) {
		l =
			(data[start + 250 +
				 j * 4] << 24) +
			(data[start + 251 +
				j * 4] << 16) +
			(data[start + 252 + j * 4] << 8)
			+ data[start + 253 + j * 4];
		if (l != 0) {
/*printf ( "#7 (start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		j += 1;
	}

	Test = GOOD;
}
