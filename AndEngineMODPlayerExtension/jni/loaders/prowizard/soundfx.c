/*
 *   SoundFX.c   1999 (c) Sylvain "Asle" Chipaux
 *
 * Depacks musics in the SoundFX format and saves in ptk.
 *
*/

#include <string.h>
#include <stdlib.h>

void Depack_SoundFX13 (FILE * in, FILE * out)
{
	uint8 *tmp;
	uint8 c0 = 0x00, c1 = 0x00, c2 = 0x00, c3 = 0x00;
	uint8 Max = 0x00;
	uint8 PatPos;
	long ssize = 0;
	long i = 0, j = 0;
	// FILE *in,*out;

	if (Save_Status == BAD)
		return;

	// in = fdopen (fd_in, "rb");
	// sprintf ( Depacked_OutName , "%ld.mod" , Cpt_Filename-1 );
	// out = fdopen (fd_out, "w+b");

	/* title */
	tmp = (uint8 *) malloc (20);
	memset(tmp, 0, 20);
	fwrite (tmp, 20, 1, out);
	free (tmp);

	/* read and write whole header */
	for (i = 0; i < 15; i++) {
		fseek (in, 0x50 + i * 30, 0);
		/* write name */
		for (j = 0; j < 22; j++) {
			fread (&c1, 1, 1, in);
			fwrite (&c1, 1, 1, out);
		}
		/* size */
		fseek (in, i * 4 + 1, 0);
		fread (&c0, 1, 1, in);
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		c2 /= 2;
		c3 = c1 / 2;
		if ((c3 * 2) != c1)
			c2 += 0x80;
		if (c0 != 0x00)
			c3 += 0x80;
		fseek (in, 0x50 + i * 30 + 24, 0);
		fwrite (&c3, 1, 1, out);
		fwrite (&c2, 1, 1, out);
		ssize += (((c3 << 8) + c2) * 2);
		/* finetune */
		fread (&c1, 1, 1, in);
		fwrite (&c1, 1, 1, out);
		/* volume */
		fread (&c1, 1, 1, in);
		fwrite (&c1, 1, 1, out);
		/* loop start */
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		c2 /= 2;
		c3 = c1 / 2;
		if ((c3 * 2) != c1)
			c2 += 0x80;
		fwrite (&c3, 1, 1, out);
		fwrite (&c2, 1, 1, out);
		/* loop size */
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);
	}
	free (tmp);
	tmp = (uint8 *) malloc (30);
	memset(tmp, 0, 30);
	tmp[29] = 0x01;
	for (i = 0; i < 16; i++)
		fwrite (tmp, 30, 1, out);
	free (tmp);

	/* pattern list size */
	fread (&PatPos, 1, 1, in);
	fwrite (&PatPos, 1, 1, out);

	/* ntk byte */
	fseek (in, 1, 1);
	c1 = 0x7f;
	fwrite (&c1, 1, 1, out);

	/* read and write pattern list */
	Max = 0x00;
	for (i = 0; i < PatPos; i++) {
		fread (&c1, 1, 1, in);
		fwrite (&c1, 1, 1, out);
		if (c1 > Max)
			Max = c1;
	}
	c1 = 0x00;
	while (i != 128) {
		fwrite (&c1, 1, 1, out);
		i += 1;
	}

	/* write ID */
	c1 = 'M';
	c2 = '.';
	c3 = 'K';
	fwrite (&c1, 1, 1, out);
	fwrite (&c2, 1, 1, out);
	fwrite (&c3, 1, 1, out);
	fwrite (&c2, 1, 1, out);


	/* pattern data */
	fseek (in, 0x294, 0);
	tmp = (uint8 *) malloc (1024);
	for (i = 0; i <= Max; i++) {
		memset(tmp, 0, 1024);
		fread (tmp, 1024, 1, in);
		for (j = 0; j < 256; j++) {
			if (tmp[(j * 4)] == 0xff) {
				if (tmp[(j * 4) + 1] != 0xfe)
					printf
						("Volume unknown : (at:%ld) (fx:%x,%x,%x,%x)\n",
						ftell (in)
						, tmp[(j * 4)]
						, tmp[(j * 4) + 1]
						, tmp[(j * 4) + 2]
						, tmp[(j * 4) + 3]);
				tmp[(j * 4)] = 0x00;
				tmp[(j * 4) + 1] = 0x00;
				tmp[(j * 4) + 2] = 0x0C;
				tmp[(j * 4) + 3] = 0x00;
				continue;
			}
			switch (tmp[(j * 4) + 2] & 0x0f) {
			case 1:	/* arpeggio */
				tmp[(j * 4) + 2] &= 0xF0;
				break;
			case 7:	/* slide up */
			case 8:	/* slide down */
				tmp[(j * 4) + 2] -= 0x06;
				break;
			case 3:	/* empty ... same as followings ... but far too much to "printf" it */
			case 6:	/* and Noiseconverter puts 00 instead ... */
				tmp[(j * 4) + 2] &= 0xF0;
				tmp[(j * 4) + 3] = 0x00;
				break;
			case 2:
			case 4:
			case 5:
			case 9:
			case 0x0a:
			case 0x0b:
			case 0x0c:
			case 0x0d:
			case 0x0e:
			case 0x0f:
				printf
					("unsupported effect : (at:%ld) (fx:%d)\n",
					ftell (in),
					tmp[(j * 4) + 2] & 0x0f);
				tmp[(j * 4) + 2] &= 0xF0;
				tmp[(j * 4) + 3] = 0x00;
				break;
			default:
				break;
			}
		}
		fwrite (tmp, 1024, 1, out);
		fflush (stdout);
	}
	free (tmp);
	fflush (stdout);


	/* sample data */
	tmp = (uint8 *) malloc (ssize);
	memset(tmp, 0, ssize);
	fread (tmp, ssize, 1, in);
	fwrite (tmp, ssize, 1, out);
	free (tmp);
	fflush (stdout);


	/* crap */
	Crap ("     Sound FX     ", BAD, BAD, out);

	fflush (in);
	fflush (out);
	// fclose ( in );
	// fclose ( out );

	printf ("done\n"
		"  WARNING: This is only an under devellopment converter !\n"
		"           output could sound strange...\n");
	return;			/* useless ... but */

}

#include <string.h>
#include <stdlib.h>

void testSoundFX13 (void)
{
	/* test 1 */
	if (i < 0x3C) {
/*printf ( "#1 (i:%ld)\n" , i );*/
		Test = BAD;
		return;
	}

	/* test 2 */
	/* samples tests */
	start = i - 0x3C;
	for (k = 0; k < 15; k++) {
		/* size */
		j =
			((data[start + k * 4 + 2] << 8) +
			data[start + k * 4 + 3]);
		/* loop start */
		m =
			((data[start + 106 + k * 30] << 8) +
			data[start + 107 + k * 30]);
		/* loop size */
		n =
			(((data[start + 108 +
						 k * 30] << 8) +
				 data[start + 109 +
					k * 30]) * 2);
		/* all sample sizes */

		/* size,loopstart,replen > 64k ? */
		if ((j > 0xFFFF) || (m > 0xFFFF) || (n > 0xFFFF)) {
/*printf ( "#2,0 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		/* replen > size ? */
		if (n > (j + 2)) {
/*printf ( "#2 (Start:%ld) (smp:%ld) (size:%ld) (replen:%ld)\n"
         , start , k+1 , j , n );*/
			Test = BAD;
			return;
		}
		/* loop start > size ? */
		if (m > j) {
/*printf ( "#2,0 (Start:%ld) (smp:%ld) (size:%ld) (lstart:%ld)\n"
         , start , k+1 , j , m );*/
			Test = BAD;
			return;
		}
		/* loop size =0 & loop start != 0 ? */
		if ((m != 0) && (n == 0)) {
/*printf ( "#2,1 (start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		/* size & loopstart !=0 & size=loopstart ? */
		if ((j != 0) && (j == m)) {
/*printf ( "#2,15 (start:%ld) (smp:%ld) (siz:%ld) (lstart:%ld)\n"
         , start,k+1,j,m );*/
			Test = BAD;
			return;
		}
		/* size =0 & loop start !=0 */
		if ((j == 0) && (m != 0)) {
/*printf ( "#2,2 (start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
	}

	/* get real whole sample size */
	ssize = 0;
	for (j = 0; j < 15; j++) {
		k =
			((data[start +
					 j * 4] << 24) +
			(data[start + j * 4 +
					1] << 16) +
			(data[start + j * 4 + 2] << 8) +
			data[start + j * 4 + 3]);
		if (k > 131072) {
/*printf ( "#2,4 (start:%ld) (smp:%ld) (size:%ld)\n"
         , start,j,k );*/
			Test = BAD;
			return;
		}
		ssize += k;
	}

	/* test #3  finetunes & volumes */
	for (k = 0; k < 15; k++) {
		if (data[start + 105 + k * 30] > 0x40) {
/*printf ( "#3 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
	}

	/* test #4  pattern list size */
	l = data[start + 0x212];
	if ((l > 127) || (l == 0)) {
/*printf ( "#4,0 (Start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}
	/* l holds the size of the pattern list */
	k = 0;
	for (j = 0; j < l; j++) {
		if (data[start + 0x214 + j] > k)
			k = data[start + 0x214 + j];
		if (data[start + 0x214 + j] > 127) {
/*printf ( "#4,1 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
	}
	/* k is the number of pattern in the file (-1) */
	k += 1;


	/* test #5 pattern data ... */
	if (((k * 1024) + 0x294 + start) > in_size) {
/*printf ( "#5,0 (Start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}

	Test = GOOD;
}
