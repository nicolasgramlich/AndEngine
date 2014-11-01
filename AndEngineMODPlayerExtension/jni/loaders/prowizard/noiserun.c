/*
 *   NoiseRunner.c   1997 (c) Asle / ReDoX
 *
 * NoiseRunner to Protracker.
 *
 * NOTE: some lines will work ONLY on IBM-PC !!!. Check the lines
 *      with the warning note to get this code working on 68k machines.
 *
*/

#include <string.h>
#include <stdlib.h>

#define PAT_DATA_ADDRESS 0x43C

void Depack_Noiserunner (FILE * in, FILE * out)
{
	uint8 tmp[1025];
	uint8 c1 = 0x00, c2 = 0x00, c3 = 0x00, c4 = 0x00, c5 = 0x00, c6 =
		0x00;
	uint8 ptable[128];
	uint8 PatPos;
	uint8 ptk_table[37][2];
	uint8 Max = 0x00;
	uint8 note, ins, fxt, fxp;
	uint8 *sdata;
	uint8 *address;
	uint8 fine, vol;
	uint8 Pattern[1025];
	long i = 0, j = 0, l = 0;
	long ssize = 0;
	// FILE *in,*out;

	if (Save_Status == BAD)
		return;

#include "ptktable.h"

	memset(tmp, 0, 1025);
	memset(ptable, 0, 128);
	memset(Pattern, 0, 1025);

	// in = fdopen (fd_in, "rb");
	// sprintf ( Depacked_OutName , "%ld.mod" , Cpt_Filename-1 );
	// out = fdopen (fd_out, "w+b");

	/* title */
	fwrite (tmp, 20, 1, out);

	/* 31 samples */
	/*printf ( "Converting sample headers ... " ); */
	for (i = 0; i < 31; i++) {
		/* sample name */
		fwrite (tmp, 22, 1, out);

		/* bypass $00 */
		fseek (in, 1, 1);

		/* read volume */
		fread (&vol, 1, 1, in);

		/* read sample address */
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		fread (&c3, 1, 1, in);
		fread (&c4, 1, 1, in);
		j =
			(c1 << 24) + (c2 << 16) +
			(c3 << 8) + c4;

		/* read and write sample size */
		fread (&c5, 1, 1, in);
		fread (&c6, 1, 1, in);
		fwrite (&c5, 1, 1, out);
		fwrite (&c6, 1, 1, out);
		ssize += (((c5 << 8) + c6) * 2);

		/* read loop start address */
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		fread (&c3, 1, 1, in);
		fread (&c4, 1, 1, in);
		l =
			(c1 << 24) + (c2 << 16) +
			(c3 << 8) + c4;

		/* calculate loop start value */
		j = l - j;

		/* read loop size */
		fread (&c5, 1, 1, in);
		fread (&c6, 1, 1, in);

		/* read finetune ?!? */
		fread (&c1, 1, 1, in);
		fread (&c2, 1, 1, in);
		if (c1 > 0xf0) {
			if ((c1 == 0xFB) && (c2 == 0xC8))
				fine = 0x0f;
			if ((c1 == 0xFC) && (c2 == 0x10))
				fine = 0x0E;
			if ((c1 == 0xFC) && (c2 == 0x58))
				fine = 0x0D;
			if ((c1 == 0xFC) && (c2 == 0xA0))
				fine = 0x0C;
			if ((c1 == 0xFC) && (c2 == 0xE8))
				fine = 0x0B;
			if ((c1 == 0xFD) && (c2 == 0x30))
				fine = 0x0A;
			if ((c1 == 0xFD) && (c2 == 0x78))
				fine = 0x09;
			if ((c1 == 0xFD) && (c2 == 0xC0))
				fine = 0x08;
			if ((c1 == 0xFE) && (c2 == 0x08))
				fine = 0x07;
			if ((c1 == 0xFE) && (c2 == 0x50))
				fine = 0x06;
			if ((c1 == 0xFE) && (c2 == 0x98))
				fine = 0x05;
			if ((c1 == 0xFE) && (c2 == 0xE0))
				fine = 0x04;
			if ((c1 == 0xFF) && (c2 == 0x28))
				fine = 0x03;
			if ((c1 == 0xFF) && (c2 == 0x70))
				fine = 0x02;
			if ((c1 == 0xFF) && (c2 == 0xB8))
				fine = 0x01;
		} else
			fine = 0x00;

		/* write fine */
		fwrite (&fine, 1, 1, out);

		/* write vol */
		fwrite (&vol, 1, 1, out);

		/* write loop start */
		/* WARNING !!! WORKS ONLY ON PC !!!       */
		/* 68k machines code : c1 = *(address+2); */
		/* 68k machines code : c2 = *(address+3); */
		j /= 2;
		address = (uint8 *) & j;
		c1 = *(address + 1);
		c2 = *address;
		fwrite (&c1, 1, 1, out);
		fwrite (&c2, 1, 1, out);

		/* write loop size */
		fwrite (&c5, 1, 1, out);
		fwrite (&c6, 1, 1, out);
	}
	/*printf ( "ok\n" ); */
	/*printf ( "Whole sample size : %ld\n" , ssize ); */

	/* size of pattern list */
	fseek (in, 950, 0);
	fread (&PatPos, 1, 1, in);
	fwrite (&PatPos, 1, 1, out);

	/* ntk byte */
	fread (&c1, 1, 1, in);
	fwrite (&c1, 1, 1, out);

	/* pattern table */
	Max = 0x00;
	fread (ptable, 128, 1, in);
	fwrite (ptable, 128, 1, out);
	for (i = 0; i < 128; i++) {
		if (ptable[i] > Max)
			Max = ptable[i];
	}
	Max += 1;		/* starts at $00 */
	/*printf ( "number of pattern : %d\n" , Max ); */

	/* write Protracker's ID */
	c1 = 'M';
	c2 = '.';
	c3 = 'K';
	fwrite (&c1, 1, 1, out);
	fwrite (&c2, 1, 1, out);
	fwrite (&c3, 1, 1, out);
	fwrite (&c2, 1, 1, out);


	/* pattern data */
	fseek (in, PAT_DATA_ADDRESS, 0);	/* SEEK_SET */
	for (i = 0; i < Max; i++) {
		memset(Pattern, 0, 1025);
		fread (tmp, 1024, 1, in);
		for (j = 0; j < 256; j++) {
			ins = (tmp[j * 4 + 3] >> 3) & 0x1f;
			note = tmp[j * 4 + 2];
			fxt = tmp[j * 4];
			fxp = tmp[j * 4 + 1];
			switch (fxt) {
			case 0x00:	/* tone portamento */
				fxt = 0x03;
				break;

			case 0x04:	/* slide up */
				fxt = 0x01;
				break;

			case 0x08:	/* slide down */
				fxt = 0x02;
				break;

			case 0x0C:	/* no fxt */
				fxt = 0x00;
				break;

			case 0x10:	/* set vibrato */
				fxt = 0x04;
				break;

			case 0x14:	/* portamento + volume slide */
				fxt = 0x05;
				break;

			case 0x18:	/* vibrato + volume slide */
				fxt = 0x06;
				break;

			case 0x20:	/* set panning ?!?!? not PTK ! Heh, Gryzor ... */
				fxt = 0x08;
				break;

			case 0x24:	/* sample offset */
				fxt = 0x09;
				break;

			case 0x28:	/* volume slide */
				fxt = 0x0A;
				break;

			case 0x30:	/* set volume */
				fxt = 0x0C;
				break;

			case 0x34:	/* pattern break */
				fxt = 0x0D;
				break;

			case 0x38:	/* extended command */
				fxt = 0x0E;
				break;

			case 0x3C:	/* set speed */
				fxt = 0x0F;
				break;

			default:
				/*printf ( "%x : at %x\n" , fxt , i*1024 + j*4 + 1084 ); */
				fxt = 0x00;
				break;
			}
			Pattern[j * 4] = (ins & 0xf0);
			Pattern[j * 4] |= ptk_table[(note / 2)][0];
			Pattern[j * 4 + 1] = ptk_table[(note / 2)][1];
			Pattern[j * 4 + 2] = ((ins << 4) & 0xf0);
			Pattern[j * 4 + 2] |= fxt;
			Pattern[j * 4 + 3] = fxp;
		}
		fwrite (Pattern, 1024, 1, out);
/*    printf ( "pattern %ld written\n" , i );*/
	}

	/* sample data */
	sdata = (uint8 *) malloc (ssize);
	memset(sdata, 0, ssize);
	fread (sdata, ssize, 1, in);
	fwrite (sdata, ssize, 1, out);
	free (sdata);


	Crap ("NR:NoiseRunner", BAD, BAD, out);

	fflush (in);
	fflush (out);

	printf ("done\n");
	return;			/* useless ... but */
}


void testNoiserunner (void)
{
	/* test 1 */
	if (i < 1080) {
		Test = BAD;
		return;
	}

	/* test 2 */
	start = i - 1080;
	for (k = 0; k < 31; k++) {
		j =
			(((data[start + 6 + k * 16] << 8) +
				 data[start + 7 +
					k * 16]) * 2);
		if (j > 0xFFFF) {
/*printf ( "#2 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		ssize += j;
	}
	if (ssize == 0) {
		Test = BAD;
		return;
	}
	/* ssize is the size of all the pattern data */

	/* test #3 volumes */
	for (k = 0; k < 31; k++) {
		if (data[start + 1 + k * 16] > 0x40) {
/*printf ( "#3 (Start:%ld)\n" , start );*/
			Test = BAD;
			ssize = 0;
			return;
		}
	}

	/* test #4  pattern list size */
	l = data[start + 950];
	if ((l > 127) || (l == 0)) {
/*printf ( "#4,0 (Start:%ld)\n" , start );*/
		Test = BAD;
		ssize = 0;
		return;
	}
	/* l holds the size of the pattern list */
	k = 0;
	for (j = 0; j < l; j++) {
		if (data[start + 952 + j] > k)
			k = data[start + 952 + j];
		if (data[start + 952 + j] > 127) {
/*printf ( "#4,1 (Start:%ld)\n" , start );*/
			Test = BAD;
			ssize = 0;
			return;
		}
	}
	/* k holds the highest pattern number */
	/* test last patterns of the pattern list = 0 ? */
	while (j != 128) {
		if (data[start + 952 + j] != 0) {
/*printf ( "#4,2 (Start:%ld)\n" , start );*/
			Test = BAD;
			ssize = 0;
			return;
		}
		j += 1;
	}
	/* k is the number of pattern in the file (-1) */
	k += 1;


	/* test #5 pattern data ... */
	for (j = 0; j < (k << 8); j++) {
		/* note > 48h ? */
		if (data[start + 1086 + j * 4] > 0x48) {
/*printf ( "#5.1 (Start:%ld)\n" , start );*/
			Test = BAD;
			ssize = 0;
			return;
		}
		l = data[start + 1087 + j * 4];
		if (((l / 8) * 8) != l) {
/*printf ( "#5,2 (Start:%ld)\n" , start );*/
			Test = BAD;
			ssize = 0;
			return;
		}
		l = data[start + 1084 + j * 4];
		if (((l / 4) * 4) != l) {
/*printf ( "#5,2 (Start:%ld)\n" , start );*/
			Test = BAD;
			ssize = 0;
			return;
		}
	}

	Test = GOOD;
}
