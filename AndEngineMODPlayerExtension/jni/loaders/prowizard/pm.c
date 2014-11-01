/*
 *   PowerMusic.c   1996 (c) Asle / ReDoX
 *
 * Converts back to ptk Optimod's power music files
 *
*/

#include <string.h>
#include <stdlib.h>

void Depack_PM (FILE * in, FILE * out)
{
	uint8 Header[2048];
	signed char *tmp;
	signed char *ins_Data;
	uint8 c1 = 0x00, c2 = 0x00, c3 = 0x00;
	uint8 npat = 0x00;
	uint8 ptable[128];
	uint8 Max = 0x00;
	long ssize = 0;
	long i = 0;
	// FILE *in,*out;

	if (Save_Status == BAD)
		return;

	memset(Header, 0, 2048);
	memset(ptable, 0, 128);

	// in = fdopen (fd_in, "rb");
	// sprintf ( Depacked_OutName , "%ld.mod" , Cpt_Filename-1 );
	// out = fdopen (fd_out, "w+b");

	/* read and write whole header */
	fseek (in, 0, SEEK_SET);
	fread (Header, 950, 1, in);
	fwrite (Header, 950, 1, out);

	/* get whole sample size */
	for (i = 0; i < 31; i++)
		ssize +=
			(((Header[42 + i * 30] << 8) + Header[43 +
			     i * 30]) * 2);
	/*printf ( "Whole sanple size : %ld\n" , ssize ); */

	/* read and write size of pattern list */
	fread (&npat, 1, 1, in);
	fwrite (&npat, 1, 1, out);
	/*printf ( "Size of pattern list : %d\n" , npat ); */

	memset(Header, 0, 2048);

	/* read and write ntk byte and pattern list */
	fread (Header, 129, 1, in);
	Header[0] = 0x7f;
	fwrite (Header, 129, 1, out);

	/* write ID */
	c1 = 'M';
	c2 = '.';
	c3 = 'K';
	fwrite (&c1, 1, 1, out);
	fwrite (&c2, 1, 1, out);
	fwrite (&c3, 1, 1, out);
	fwrite (&c2, 1, 1, out);

	/* get number of pattern */
	Max = 0x00;
	for (i = 1; i < 129; i++) {
		if (Header[i] > Max)
			Max = Header[i];
	}
	Max += 1;
	/*printf ( "Number of pattern : %d\n" , Max ); */
	/* pattern data */
	fseek (in, 1084, SEEK_SET);
	tmp = (uint8 *) malloc (Max * 1024);
	memset(tmp, 0, Max * 1024);
	fread (tmp, Max * 1024, 1, in);
	fwrite (tmp, Max * 1024, 1, out);
	free (tmp);

	/* sample data */
	tmp = (signed char *) malloc (ssize);
	ins_Data = (signed char *) malloc (ssize);
	memset(tmp, 0, ssize);
	memset(ins_Data, 0, ssize);
	fread (tmp, ssize, 1, in);
	ins_Data[0] = tmp[0];
	for (i = 1; i < ssize - 1; i++) {
		ins_Data[i] = ins_Data[i - 1] + tmp[i];
	}
	fwrite (ins_Data, ssize, 1, out);
	free (tmp);
	free (ins_Data);


	/* crap */
	Crap ("PM:Power Music", BAD, BAD, out);

	fflush (in);
	fflush (out);

	printf ("done\n");
	return;			/* useless ... but */
}


/* Power Music */
int testPM (void)
{
	if ((data[i] != '!') ||
           (data[i + 1] != 'P') ||
           (data[i + 2] != 'M') ||
           (data[i + 3] != '!'))
		return BAD;

	/* test 1 */
	if (i < 1080)
		return BAD;

	/* test 2 */
	start = i - 1080;
	for (j = 0; j < 31; j++) {
		if (data[start + 45 + 30 * j] > 0x40)
			return BAD;
	}

	/* test 3 */
	if (data[start + 951] != 0xFF)
		return BAD;

	return GOOD;
}
