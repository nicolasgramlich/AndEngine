/*
 *   ProPacker_v1.0   1997 (c) Asle / ReDoX
 *
 * Converts back to ptk ProPacker v1 MODs
 *
*/

#include <string.h>
#include <stdlib.h>

void Depack_PP10 (FILE * in, FILE * out)
{
	uint8 c1 = 0x00, c2 = 0x00, c3 = 0x00, c4 = 0x00;
	uint8 Tracks_Numbers[4][128];
	uint8 pat_pos;
	uint8 *tmp;
	uint8 Pattern[1024];
	short Max;
	long i = 0, j = 0, k = 0;
	long ssize = 0;
	// FILE *in,*out;

	if (Save_Status == BAD)
		return;

	memset(Tracks_Numbers, 0, 128 * 4);

	// in = fdopen (fd_in, "rb");
	// sprintf ( Depacked_OutName , "%ld.mod" , Cpt_Filename-1 );
	// out = fdopen (fd_out, "w+b");

	/* write title */
	for (i = 0; i < 20; i++)
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

	/* read and write pattern table lenght */
	fread (&pat_pos, 1, 1, in);
	fwrite (&pat_pos, 1, 1, out);
	/*printf ( "Size of pattern list : %d\n" , pat_pos ); */

	/* read and write NoiseTracker byte */
	fread (&c1, 1, 1, in);
	fwrite (&c1, 1, 1, out);

	/* read track list and get highest track number */
	Max = 0;
	for (j = 0; j < 4; j++) {
		for (i = 0; i < 128; i++) {
			fread (&Tracks_Numbers[j][i], 1, 1, in);
			if (Tracks_Numbers[j][i] > Max)
				Max = Tracks_Numbers[j][i];
		}
	}
	/*printf ( "highest track number : %d\n" , Max+1 ); */

	/* write pattern table "as is" ... */
	for (c1 = 0x00; c1 < pat_pos; c1++)
		fwrite (&c1, 1, 1, out);
	c2 = 0x00;
	for (; c1 < 128; c1++)
		fwrite (&c2, 1, 1, out);

	/* write ptk's ID */
	c1 = 'M';
	c2 = '.';
	c3 = 'K';
	fwrite (&c1, 1, 1, out);
	fwrite (&c2, 1, 1, out);
	fwrite (&c3, 1, 1, out);
	fwrite (&c2, 1, 1, out);

	/* track/pattern data */

	for (i = 0; i < pat_pos; i++) {
/*fprintf ( info , "\n\n\nPattern %ld :\n" , i );*/
		memset(Pattern, 0, 1024);
		for (j = 0; j < 4; j++) {
			fseek (in, 762 + (Tracks_Numbers[j][i] << 8), 0);	/* SEEK_SET */
/*fprintf ( info , "Voice %ld :\n" , j );*/
			for (k = 0; k < 64; k++) {
				fread (&c1, 1, 1, in);
				fread (&c2, 1, 1, in);
				fread (&c3, 1, 1, in);
				fread (&c4, 1, 1, in);
/*fprintf ( info , "%2x , %2x , %2x  (%ld)\n" , c2 , c3 , c4 ,ftell (in));*/
				Pattern[k * 16 + j * 4] = c1;
				Pattern[k * 16 + j * 4 + 1] = c2;
				Pattern[k * 16 + j * 4 + 2] = c3;
				Pattern[k * 16 + j * 4 + 3] = c4;
			}
		}
		fwrite (Pattern, 1024, 1, out);
		/*printf ( "+" ); */
	}
	/*printf ( "\n" ); */


	/* now, lets put file pointer at the beginning of the sample datas */
	fseek (in, 762 + ((Max + 1) << 8), 0);	/* SEEK_SET */

	/* sample data */
	tmp = (uint8 *) malloc (ssize);
	fread (tmp, ssize, 1, in);
	fwrite (tmp, ssize, 1, out);
	free (tmp);

	/* crap */
	Crap ("PP10:ProPacker v1.0", BAD, BAD, out);

	fflush (in);
	fflush (out);

	printf ("done\n");
	return;			/* useless ... but */
}


void testPP10 (void)
{
	/* test #1 */
	if (i < 3) {
/*printf ( "#1 (i:%ld)\n" , i );*/
		Test = BAD;
		return;
	}
	start = i - 3;

	/* noisetracker byte */
	if (data[start + 249] > 0x7f) {
		Test = BAD;
/*printf ( "#1,1 (start:%ld)\n" , start );*/
		return;
	}

	/* test #2 */
	ssize = 0;
	for (j = 0; j < 31; j++) {
		k =
			(((data[start + j * 8] << 8) +
				 data[start + 1 +
					j * 8]) * 2);
		l =
			(((data[start + j * 8 + 4] << 8) +
				 data[start + 5 +
					j * 8]) * 2);
		/* loop size */
		m =
			(((data[start + j * 8 + 6] << 8) +
				 data[start + 7 +
					j * 8]) * 2);
		if (m == 0) {
/*printf ( "#1,98 (start:%ld) (k:%ld) (l:%ld) (m:%ld)\n" , start,k,l,m );*/
			Test = BAD;
			return;
		}
		if ((l != 0) && (m <= 2)) {
/*printf ( "#1,99 (start:%ld) (k:%ld) (l:%ld) (m:%ld)\n" , start,k,l,m );*/
			Test = BAD;
			return;
		}
		if ((l + m) > (k + 2)) {
/*printf ( "#2,0 (start:%ld) (k:%ld) (l:%ld) (m:%ld)\n" , start,k,l,m );*/
			Test = BAD;
			return;
		}
		if ((l != 0) && (m == 0)) {
/*printf ( "#2,01 (start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		ssize += k;
		/* finetune > 0x0f ? */
		if (data[start + 2 + 8 * j] > 0x0f) {
/*printf ( "#2 (start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		/* volume > 0x40 ? */
		if (data[start + 3 + 8 * j] > 0x40) {
/*printf ( "#2,1 (start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		/* loop start > size ? */
		if ((((data[start + 4 + j * 8] << 8) +
					data[start + 5 +
						j * 8]) * 2) > k) {
			Test = BAD;
/*printf ( "#2,2 (start:%ld)\n" , start );*/
			return;
		}
		/* size > 0xffff ? */
		if (k > 0xFFFF) {
			Test = BAD;
/*printf ( "#2,3 (start:%ld)\n" , start );*/
			return;
		}
	}
	if (ssize <= 2) {
/*printf ( "#2,4 (start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}
	/* ssize = whole sample size */

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

	/* track data test */
	for (j = 0; j < k; j++) {
		if (data[start + 762 + j * 4] > 0x13) {
			Test = BAD;
			ssize = 0;
/*printf ( "#3,1 (start:%ld)\n" , start );*/
			return;
		}
	}
	k *= 4;

	/* ssize is the sample data size */
	/* k is the track data size */
	Test = GOOD;
}
