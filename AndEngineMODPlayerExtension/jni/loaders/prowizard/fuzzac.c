/*
 * fuzzac.c   Copyright (C) 1997 Asle / ReDoX
 *            Modified by Claudio Matsuoka
 *
 * Converts Fuzzac packed MODs back to PTK MODs
 * thanks to Gryzor and his ProWizard tool ! ... without it, this prog
 * would not exist !!!
 *
 * note: A most worked-up prog ... took some time to finish this !.
 *      there's what lot of my other depacker are missing : the correct
 *      pattern order (most of the time the list is generated badly ..).
 *      Dont know why I did it for this depacker because I've but one
 *      exemple file ! :)
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int test_fuzz (uint8 *, int);
static int depack_fuzz (FILE *, FILE *);

struct pw_format pw_fuzz = {
	"FUZZ",
	"Fuzzac Packer",
	0x00,
	test_fuzz,
	depack_fuzz
};


static int depack_fuzz(FILE *in, FILE *out)
{
	uint8 c1;
	uint8 data[1024];
	uint8 ord[128];
	uint8 tidx[128][16];
	uint8 tidx_real[128][4];
	uint8 track[4][256];
	uint8 status = 1;
	int len, ntrk, npat;
	int size, ssize = 0;
	int lps, lsz;
	int i, j, k, l;

	memset(tidx, 0, 128 * 16);
	memset(tidx_real, 0, 128 * 4);
	memset(ord, 0, 128);

	read32b(in);			/* bypass ID */
	read16b(in);			/* bypass 2 unknown bytes */
	pw_write_zero(out, 20);		/* write title */

	for (i = 0; i < 31; i++) {
		pw_move_data(out, in, 22);	/*sample name */
		fseek(in, 38, SEEK_CUR);
		write16b(out, size = read16b(in));
		ssize += size * 2;
		lps = read16b(in);		/* loop start */
		lsz = read16b(in);		/* loop size */
		write8(out, read8(in));		/* finetune */
		write8(out, read8(in));		/* volume */
		write16b(out, lps);
		write16b(out, lsz > 0 ? lsz : 1);
	}

	write8(out, len = read8(in));	/* size of pattern list */
	ntrk = read8(in);		/* read the number of tracks */
	write8(out, 0x7f);		/* write noisetracker byte */

	/* place file pointer at track number list address */
	fseek(in, 2118, SEEK_SET);

	/* read tracks numbers */
	for (i = 0; i < 4; i++) {
		for (j = 0; j < len; j++)
			fread(&tidx[j][i * 4], 1, 4, in);
	}

	/* sort tracks numbers */
	npat = 0;
	for (i = 0; i < len; i++) {
		if (i == 0) {
			ord[0] = npat++;
			continue;
		}

		for (j = 0; j < i; j++) {
			status = 1;
			for (k = 0; k < 4; k++) {
				if (tidx[j][k * 4] !=
					tidx[i][k * 4]) {
					status = 0;
					break;
				}
			}
			if (status == 1) {
				ord[i] = ord[j];
				break;
			}
		}

		if (status == 0)
			ord[i] = npat++;

		status = 1;
	}

	/* create a list of tracks numbers for the really existing patterns */
	c1 = 0x00;
	for (i = 0; i < len; i++) {
		if (i == 0) {
			tidx_real[c1][0] = tidx[i][0];
			tidx_real[c1][1] = tidx[i][4];
			tidx_real[c1][2] = tidx[i][8];
			tidx_real[c1][3] = tidx[i][12];
			c1++;
			continue;
		}

		for (j = 0; j < i; j++) {
			status = 1;
			if (ord[i] == ord[j]) {
				status = 0;
				break;
			}
		}

		if (status == 0)
			continue;

		tidx_real[c1][0] = tidx[i][0];
		tidx_real[c1][1] = tidx[i][4];
		tidx_real[c1][2] = tidx[i][8];
		tidx_real[c1][3] = tidx[i][12];
		c1++;
		status = 1;
	}

	fwrite(ord, 128, 1, out);	/* write pattern list */
	write32b(out, PW_MOD_MAGIC);	/* write ID */

	/* pattern data */
	l = 2118 + len * 16;

	for (i = 0; i < npat; i++) {
		memset(data, 0, 1024);
		memset(track, 0, 4 << 8);

		fseek(in, l + (tidx_real[i][0] << 8), SEEK_SET);
		fread(track[0], 256, 1, in);

		fseek(in, l + (tidx_real[i][1] << 8), SEEK_SET);
		fread(track[1], 256, 1, in);

		fseek(in, l + (tidx_real[i][2] << 8), SEEK_SET);
		fread(track[2], 256, 1, in);

		fseek(in, l + (tidx_real[i][3] << 8), SEEK_SET);
		fread(track[3], 256, 1, in);

		for (j = 0; j < 64; j++) {
			memcpy(&data[j * 16     ], &track[0][j * 4], 4);
			memcpy(&data[j * 16 + 4 ], &track[1][j * 4], 4);
			memcpy(&data[j * 16 + 8 ], &track[2][j * 4], 4);
			memcpy(&data[j * 16 + 12], &track[3][j * 4], 4);
			data[j * 16 + 15] = track[3][j * 4 + 3];
		}
		fwrite(data, 1024, 1, out);
	}

	/* sample data */
	/* bypass the "SEnd" unidentified ID */
	fseek(in, l + (ntrk << 8) + 4, SEEK_SET);
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_fuzz (uint8 *data, int s)
{
	int j, k;
	int start = 0, ssize = 0;

	if (readmem32b(data) != MAGIC4('M','1','.','0'))
		return -1;

	/* test finetune */
	for (k = 0; k < 31; k++) {
		if (data[start + 72 + k * 68] > 0x0f)
			return -1;
	}

	/* test volumes */
	for (k = 0; k < 31; k++) {
		if (data[start + 73 + k * 68] > 0x40)
			return -1;
	}

	/* test sample sizes */
	for (k = 0; k < 31; k++) {
		j = readmem16b(data + start + k * 68 + 66);
		if (j > 0x8000)
			return -1;
		ssize += j * 2;
	}

	/* test size of pattern list */
	if (data[start + 2114] == 0x00)
		return -1;

	return 0;
}
