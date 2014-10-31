/*
 * Kefrens_Sound_Machine.c   Copyright (C) 1997 Sylvain "Asle" Chipaux
 *                           Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Depacks musics in the Kefrens Sound Machine format and saves in ptk.
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int test_ksm (uint8 *, int);
static int depack_ksm (FILE *, FILE *);

struct pw_format pw_ksm = {
	"KSM",
	"Kefrens Sound Machine",
	0x00,
	test_ksm,
	depack_ksm
};


#define ON  1
#define OFF 2

static int depack_ksm (FILE *in, FILE *out)
{
	uint8 tmp[1024];
	uint8 c1, c5;
	uint8 plist[128];
	uint8 trknum[128][4];
	uint8 real_tnum[128][4];
	uint8 tdata[4][192];
	uint8 Max;
	uint8 PatPos;
	uint8 Status = ON;
	int ssize = 0;
	int i, j, k;

	memset(plist, 0, 128);
	memset(trknum, 0, 128 * 4);
	memset(real_tnum, 0, 128 * 4);

	/* title */
	fseek(in, 2, SEEK_SET);
	pw_move_data(out, in, 13);
	pw_write_zero(out, 7);

	/* read and write whole header */
	/*printf ( "Converting sample headers ... " ); */
	fseek(in, 32, SEEK_SET);
	for (i = 0; i < 15; i++) {
		pw_write_zero(out, 22);		/* write name */
		fseek(in, 20, SEEK_CUR);	/* 16 unknown/4 addr bytes */
		write16b(out, (k = read16b(in)) / 2); /* size */
		ssize += k;
		write8(out, 0);			/* finetune */
		write8(out, read8(in));		/* volume */
		read8(in);			/* bypass 1 unknown byte */
		write16b(out, (j = read16b(in)) / 2);	/* loop start */
		j = k - j;
		write16b(out, j != k ? j / 2 : 1);	/* loop size */
		fseek(in, 6, SEEK_CUR);		/* bypass 6 unknown bytes */
	}

	memset(tmp, 0, 30);
	tmp[29] = 0x01;
	for (i = 0; i < 16; i++)
		fwrite(tmp, 30, 1, out);

	/* pattern list */
	fseek (in, 512, 0);
	for (Max = PatPos = 0; PatPos < 128; PatPos++) {
		fread(&trknum[PatPos][0], 1, 1, in);
		fread(&trknum[PatPos][1], 1, 1, in);
		fread(&trknum[PatPos][2], 1, 1, in);
		fread(&trknum[PatPos][3], 1, 1, in);
		if (trknum[PatPos][0] == 0xFF)
			break;
		if (trknum[PatPos][0] > Max)
			Max = trknum[PatPos][0];
		if (trknum[PatPos][1] > Max)
			Max = trknum[PatPos][1];
		if (trknum[PatPos][2] > Max)
			Max = trknum[PatPos][2];
		if (trknum[PatPos][3] > Max)
			Max = trknum[PatPos][3];
	}

	write8(out, PatPos);		/* write patpos */
	write8(out, 0x7f);		/* ntk byte */

	/* sort tracks numbers */
	c5 = 0x00;
	for (i = 0; i < PatPos; i++) {
		if (i == 0) {
			plist[0] = c5;
			c5 += 0x01;
			continue;
		}
		for (j = 0; j < i; j++) {
			Status = ON;
			for (k = 0; k < 4; k++) {
				if (trknum[j][k] !=
					trknum[i][k]) {
					Status = OFF;
					break;
				}
			}
			if (Status == ON) {
				plist[i] = plist[j];
				break;
			}
		}
		if (Status == OFF) {
			plist[i] = c5;
			c5 += 0x01;
		}
		Status = ON;
	}
	/* c5 is the Max pattern number */

	/* create real list of tracks numbers for really existing patterns */
	c1 = 0x00;
	for (i = 0; i < PatPos; i++) {
		if (i == 0) {
			real_tnum[c1][0] = trknum[i][0];
			real_tnum[c1][1] = trknum[i][1];
			real_tnum[c1][2] = trknum[i][2];
			real_tnum[c1][3] = trknum[i][3];
			c1 += 0x01;
			continue;
		}
		for (j = 0; j < i; j++) {
			Status = ON;
			if (plist[i] == plist[j]) {
				Status = OFF;
				break;
			}
		}
		if (Status == OFF)
			continue;
		real_tnum[c1][0] = trknum[i][0];
		real_tnum[c1][1] = trknum[i][1];
		real_tnum[c1][2] = trknum[i][2];
		real_tnum[c1][3] = trknum[i][3];
		c1 += 0x01;
		Status = ON;
	}

	fwrite(plist, 128, 1, out);	/* write pattern list */
	write32b(out, PW_MOD_MAGIC);	/* write ID */

	/* pattern data */
	for (i = 0; i < c5; i++) {
		memset(tmp, 0, 1024);
		memset(tdata, 0, 192 * 4);
		fseek(in, 1536 + 192 * real_tnum[i][0], SEEK_SET);
		fread(tdata[0], 192, 1, in);
		fseek(in, 1536 + 192 * real_tnum[i][1], SEEK_SET);
		fread(tdata[1], 192, 1, in);
		fseek(in, 1536 + 192 * real_tnum[i][2], SEEK_SET);
		fread(tdata[2], 192, 1, in);
		fseek(in, 1536 + 192 * real_tnum[i][3], SEEK_SET);
		fread(tdata[3], 192, 1, in);

		for (j = 0; j < 64; j++) {
			int x = j * 16;

			tmp[x] = ptk_table[tdata[0][j * 3]][0];
			tmp[x + 1] = ptk_table[tdata[0][j * 3]][1];
			if ((tdata[0][j * 3 + 1] & 0x0f) == 0x0d)
				tdata[0][j * 3 + 1] -= 0x03;
			tmp[x + 2] = tdata[0][j * 3 + 1];
			tmp[x + 3] = tdata[0][j * 3 + 2];

			tmp[x + 4] = ptk_table[tdata[1][j * 3]][0];
			tmp[x + 5] = ptk_table[tdata[1][j * 3]][1];
			if ((tdata[1][j * 3 + 1] & 0x0f) == 0x0d)
				tdata[1][j * 3 + 1] -= 0x03;
			tmp[x + 6] = tdata[1][j * 3 + 1];
			tmp[x + 7] = tdata[1][j * 3 + 2];

			tmp[x + 8] = ptk_table[tdata[2][j * 3]][0];
			tmp[x + 9] = ptk_table[tdata[2][j * 3]][1];
			if ((tdata[2][j * 3 + 1] & 0x0f) == 0x0d)
				tdata[2][j * 3 + 1] -= 0x03;
			tmp[x + 10] = tdata[2][j * 3 + 1];
			tmp[x + 11] = tdata[2][j * 3 + 2];

			tmp[x + 12] = ptk_table[tdata[3][j * 3]][0];
			tmp[x + 13] = ptk_table[tdata[3][j * 3]][1];
			if ((tdata[3][j * 3 + 1] & 0x0f) == 0x0d)
				tdata[3][j * 3 + 1] -= 0x03;
			tmp[x + 14] = tdata[3][j * 3 + 1];
			tmp[x + 15] = tdata[3][j * 3 + 2];
		}

		fwrite(tmp, 1024, 1, out);
	}

	/* sample data */
	fseek(in, 1536 + (192 * (Max + 1)), SEEK_SET);
	pw_move_data(out, in, ssize);

	return 0;
}

static int test_ksm (uint8 *data, int s)
{
	int j, k, l;
	int start = 0;

	PW_REQUEST_DATA (s, 1536);

	if (data[start] != 'M' || data[start + 1] != '.')
		return -1;

	/* test "a" */
	if (data[start + 15] != 'a')
		return -1;

	/* test volumes */
	for (k = 0; k < 15; k++) {
		if (data[start + 54 + k * 32] > 0x40)
			return -1;
	}


	/* test tracks data */
	/* first, get the highest track number .. */
	j = 0;
	for (k = 0; k < 1024; k++) {
		if (data[start + k + 512] == 0xFF)
			break;
		if (data[start + k + 512] > j)
			j = data[start + k + 512];
	}

	if (k == 1024)
		return -1;

	if (j == 0)
		return -1;

	PW_REQUEST_DATA (s, start + 1536 + j * 192 + 63 * 3);

	/* so, now, j is the highest track number (first is 00h !!) */
	/* real test on tracks data starts now */
	for (k = 0; k <= j; k++) {
		for (l = 0; l < 64; l++) {
			if (data[start + 1536 + k * 192 + l * 3] > 0x24)
				return -1;
		}
	}

	/* j is still the highest track number */

	return 0;
}
