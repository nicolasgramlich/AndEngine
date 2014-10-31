/*
 * Unic_Tracker.c   Copyright (C) 1997 Asle / ReDoX
 *		    Copyright (C) 2006-2007 Claudio Matsuoka
 * 
 * Unic tracked MODs to Protracker
 * both with or without ID Unic files will be converted
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

#define MAGIC_UNIC	MAGIC4('U','N','I','C')
#define MAGIC_M_K_	MAGIC4('M','.','K','.')
#define MAGIC_0000	MAGIC4(0x0,0x0,0x0,0x0)


static int test_unic_id (uint8 *, int);
static int test_unic_noid (uint8 *, int);
static int test_unic_emptyid (uint8 *, int);
static int depack_unic (FILE *, FILE *);

struct pw_format pw_unic_id = {
	"UNIC",
	"UNIC Tracker",
	0x00,
	test_unic_id,
	depack_unic
};

struct pw_format pw_unic_noid = {
	"UNIC",
	"UNIC Tracker noid",
	0x00,
	test_unic_noid,
	depack_unic
};

struct pw_format pw_unic_emptyid = {
	"UNIC",
	"UNIC Tracker id0",
	0x00,
	test_unic_emptyid,
	depack_unic
};


#define ON 1
#define OFF 2

static int depack_unic (FILE *in, FILE *out)
{
	uint8 c1, c2, c3, c4;
	uint8 npat;
	uint8 max = 0;
	uint8 ins, note, fxt, fxp;
	uint8 fine;
	uint8 tmp[1025];
	uint8 loop_status = OFF;	/* standard /2 */
	int i = 0, j = 0, k = 0, l = 0;
	int ssize = 0;
	uint32 id;

	pw_move_data(out, in, 20);		/* title */

	for (i = 0; i < 31; i++) {
		pw_move_data(out, in, 20);	/* sample name */
		write8(out, 0);
		write8(out, 0);

		/* fine on ? */
		c1 = read8(in);
		c2 = read8(in);
		j = (c1 << 8) + c2;
		if (j != 0) {
			if (j < 256)
				fine = 0x10 - c2;
			else
				fine = 0x100 - c2;
		} else {
			fine = 0;
		}

		/* smp size */
		write16b(out, l = read16b(in));
		ssize += l * 2;

		read8(in);
		write8(out, fine);		/* fine */
		write8(out, read8(in));		/* vol */
		j = read16b(in);		/* loop start */
		k = read16b(in);		/* loop size */

		if ((((j * 2) + k) <= l) && (j != 0)) {
			loop_status = ON;
			j *= 2;
		}

		write16b(out, j);
		write16b(out, k);
	}

	write8(out, npat = read8(in));		/* number of pattern */
	write8(out, 0x7f);			/* noisetracker byte */
	read8(in);

	fread(tmp, 128, 1, in);			/* pat table */
	fwrite(tmp, 128, 1, out);

	/* get highest pattern number */
	for (i = 0; i < 128; i++) {
		if (tmp[i] > max)
			max = tmp[i];
	}
	max++;		/* coz first is $00 */

	write32b(out, PW_MOD_MAGIC);

	/* verify UNIC ID */
	fseek(in, 1080, SEEK_SET);
	id = read32b(in);

	if (id && id != MAGIC_M_K_ && id != MAGIC_UNIC)
		fseek(in, -4, SEEK_CUR);

	/* pattern data */
	for (i = 0; i < max; i++) {
		for (j = 0; j < 256; j++) {
			c1 = read8(in);
			c2 = read8(in);
			c3 = read8(in);

			ins = ((c1 >> 2) & 0x10) | ((c2 >> 4) & 0x0f);
			note = c1 & 0x3f;
			fxt = c2 & 0x0f;
			fxp = c3;

			if (fxt == 0x0d) {	/* pattern break */
				c3 = fxp / 10;
				c4 = fxp % 10;
				fxp = 16 * c3 + c4;
			}

			tmp[j * 4] = (ins & 0xf0) | ptk_table[note][0];
			tmp[j * 4 + 1] = ptk_table[note][1];
			tmp[j * 4 + 2] = ((ins << 4) & 0xf0) | fxt;
			tmp[j * 4 + 3] = fxp;
		}
		fwrite(tmp, 1024, 1, out);
	}

	/* sample data */
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_unic_id (uint8 *data, int s)
{
	int j, k, l, n;
	int start = 0, ssize;

	/* test 1 */
	PW_REQUEST_DATA(s, 1084);

	if (readmem32b(data + start + 1080) != MAGIC_M_K_)
		return -1;

	/* test 2 */
	ssize = 0;
	for (k = 0; k < 31; k++) {
		int x = start + k * 30;

		j = readmem16b(data + x + 42) * 2;
		ssize += j;
		n = (readmem16b(data + x + 46) + readmem16b(data + x + 48)) * 2;

		if ((j + 2) < n)
			return -1;
	}

	if (ssize <= 2)
		return -1;

	/* test #3  finetunes & volumes */
	for (k = 0; k < 31; k++) {
		int x = start + k * 30;

		if (data[x + 40] > 0x0f || data[x + 44] || data[x + 45] > 0x40)
			return -1;
	}

	/* test #4  pattern list size */
	l = data[start + 950];
	if (l > 127 || l == 0)
		return -1;
	/* l holds the size of the pattern list */

	k = 0;
	for (j = 0; j < l; j++) {
		if (data[start + 952 + j] > k)
			k = data[start + 952 + j];
		if (data[start + 952 + j] > 127)
			return -1;
	}
	/* k holds the highest pattern number */

	/* test last patterns of the pattern list = 0 ? */
	while (j != 128) {
		if (data[start + 952 + j] != 0)
			return -1;
		j++;
	}
	/* k is the number of pattern in the file (-1) */
	k++;

	PW_REQUEST_DATA(s, 1084 + k * 256 * 3);

#if 0
	/* test #5 pattern data ... */
	if (((k * 768) + 1084 + start) > in_size)
		return -1;
#endif

	for (j = 0; j < (k << 8); j++) {
		/* relative note number + last bit of sample > $34 ? */
		if (data[start + 1084 + j * 3] > 0x74)
			return -1;
	}

	return 0;
}


static int test_unic_emptyid (uint8 *data, int s)
{
	int j, k, l, m, n, o;
	int start = 0, ssize;

	/* test 1 */
	PW_REQUEST_DATA(s, 1084);

	/* test #2 ID = $00000000 ? */
	if (readmem32b(data + start + 1080) != MAGIC_0000)
		return -1;

	/* test 2,5 :) */
	ssize = 0;
	o = 0;
	for (k = 0; k < 31; k++) {
		int x = start + k * 30, y;

		j = readmem16b(data + x + 42) * 2;
		m = readmem16b(data + x + 46) * 2;
		n = readmem16b(data + x + 48) * 2;
		ssize += j;

		if (n != 0 && (j + 2) < (m + n))
			return -1;

		if (j > 0xffff || m > 0xffff || n > 0xffff)
			return -1;

		if (data[x + 45] > 0x40)
			return -1;

		/* finetune ... */
		y = readmem16b(data + x + 40);
		if ((y != 0 && j == 0) || (y > 8 && y < 247))
			return -1;

		/* loop start but no replen ? */
		if (m != 0 && n <= 2)
			return -1;

		if (data[x + 45] != 0 && j == 0)
			return -1;

		/* get the highest !0 sample */
		if (j != 0)
			o = j + 1;
	}

	if (ssize <= 2)
		return -1;

	/* test #4  pattern list size */
	l = data[start + 950];
	if (l > 127 || l == 0)
		return -1;
	/* l holds the size of the pattern list */

	k = 0;
	for (j = 0; j < l; j++) {
		if (data[start + 952 + j] > k)
			k = data[start + 952 + j];
		if (data[start + 952 + j] > 127)
			return -1;
	}
	/* k holds the highest pattern number */

	/* test last patterns of the pattern list = 0 ? */
	while (j != 128) {
		if (data[start + 952 + j] != 0)
			return -1;
		j += 1;
	}
	/* k is the number of pattern in the file (-1) */
	k += 1;

#if 0
	/* test #5 pattern data ... */
	if ((k * 768 + 1084 + start) > in_size)
		return -1;
#endif

	PW_REQUEST_DATA(s, 1084 + k * 256 * 3 + 2);

	for (j = 0; j < (k << 8); j++) {
		int y = start + 1084 + j * 3;

		/* relative note number + last bit of sample > $34 ? */
		if (data[y] > 0x74)
			return -1;

		if ((data[y] & 0x3F) > 0x24)
			return -1;

		if ((data[y + 1] & 0x0F) == 0x0C
			&& data[y + 2] > 0x40)
			return -1;

		if ((data[y + 1] & 0x0F) == 0x0B
			&& data[y + 2] > 0x7F)
			return -1;

		if ((data[y + 1] & 0x0F) == 0x0D
			&& data[y + 2] > 0x40)
			return -1;

		n = ((data[y] >> 2) & 0x30) |
			((data[start + 1085 + j * 3 + 1] >> 4) & 0x0F);

		if (n > o)
			return -1;
	}

	return 0;
}


static int test_unic_noid (uint8 *data, int s)
{
	int j, k, l, m, n, o;
	int start = 0, ssize;

	/* test 1 */
	PW_REQUEST_DATA(s, 1084);

	/* test #2 ID = $00000000 ? */
	if (readmem32b(data + start + 1080) == MAGIC_0000)
		return -1;

	/* test 2,5 :) */
	ssize = 0;
	o = 0;
	for (k = 0; k < 31; k++) {
		int x = start + k * 30, y;

		j = readmem16b(data + x + 42) * 2;
		m = readmem16b(data + x + 46) * 2;
		n = readmem16b(data + x + 48) * 2;

		ssize += j;
		if (n != 0 && (j + 2) < (m + n))
			return -1;

		/* samples too big ? */
		if (j > 0xffff || m > 0xffff || n > 0xffff)
			return -1;

		/* volume too big */
		if (data[x + 45] > 0x40)
			return -1;

		/* finetune ... */
		y = readmem16b(data + x + 40);
		if ((y != 0 && j == 0) || (y > 8 && y < 247))
			return -1;

		/* loop start but no replen ? */
		if (m != 0 && n <= 2)
			return -1;

		if (data[x + 45] != 0 && j == 0)
			return -1;

		/* get the highest !0 sample */
		if (j != 0)
			o = j + 1;
	}
	if (ssize <= 2)
		return -1;

	/* test #4  pattern list size */
	l = data[start + 950];
	if (l > 127 || l == 0)
		return -1;
	/* l holds the size of the pattern list */

	k = 0;
	for (j = 0; j < l; j++) {
		if (data[start + 952 + j] > k)
			k = data[start + 952 + j];
		if (data[start + 952 + j] > 127)
			return -1;
	}
	/* k holds the highest pattern number */

	/* test last patterns of the pattern list = 0 ? */
	while (j != 128) {
		if (data[start + 952 + j] != 0)
			return -1;
		j += 1;
	}
	/* k is the number of pattern in the file (-1) */
	k += 1;

	/* test #5 pattern data ... */
	/* o is the highest !0 sample */

#if 0
	if (((k * 768) + 1080 + start) > in_size) {
		Test = BAD;
		return;
	}
#endif

	PW_REQUEST_DATA(s, 1080 + k * 256 * 3 + 2);

	for (j = 0; j < (k << 8); j++) {
		int y = start + 1080 + j * 3;

		/* relative note number + last bit of sample > $34 ? */
		if (data[y] > 0x74)
			return -1;
		if ((data[y] & 0x3F) > 0x24)
			return -1;
		if ((data[y + 1] & 0x0F) == 0x0C && data[y + 2] > 0x40)
			return -1;

		if ((data[y + 1] & 0x0F) == 0x0B && data[y + 2] > 0x7F)
			return -1;

		if ((data[y + 1] & 0x0F) == 0x0D && data[y + 2] > 0x40)
			return -1;

		n = ((data[y] >> 2) & 0x30) |
			((data[start + 1081 + j * 3 + 1] >> 4) & 0x0F);

		if (n > o)
			return -1;
	}

	/* test #6  title coherent ? */
	for (j = 0; j < 20; j++) {
		if ((data[start + j] != 0 && data[start + j] < 32) ||
			data[start + j] > 180)
			return -1;
	}

	return 0;
}
