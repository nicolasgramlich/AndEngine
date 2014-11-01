/*
 * ProRunner1.c   Copyright (C) 1996 Asle / ReDoX
 *                Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Converts MODs packed with Prorunner v1.0
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int test_pru1 (uint8 *, int);
static int depack_pru1 (FILE *, FILE *);

struct pw_format pw_pru1 = {
	"PRU1",
	"Prorunner 1.0",
	0x00,
	test_pru1,
	depack_pru1
};


static int depack_pru1 (FILE *in, FILE *out)
{
	uint8 header[2048];
	uint8 c1, c2, c3, c4;
	uint8 npat, max;
	uint8 ptable[128];
	int ssize = 0;
	int i, j;

	memset(header, 0, 2048);
	memset(ptable, 0, 128);

	/* read and write whole header */
	fread(header, 950, 1, in);
	fwrite(header, 950, 1, out);

	/* get whole sample size */
	for (i = 0; i < 31; i++)
		ssize += readmem16b(header + i * 30 + 42) * 2;

	/* read and write size of pattern list */
	write8(out, npat = read8(in));

	memset(header, 0, 2048);

	/* read and write ntk byte and pattern list */
	fread(header, 129, 1, in);
	fwrite(header, 129, 1, out);

	/* write ID */
	write32b(out, PW_MOD_MAGIC);

	/* get number of pattern */
	max = 0;
	for (i = 1; i < 129; i++) {
		if (header[i] > max)
			max = header[i];
	}

	/* pattern data */
	fseek (in, 1084, SEEK_SET);
	for (i = 0; i <= max; i++) {
		for (j = 0; j < 256; j++) {
			header[0] = read8(in);
			header[1] = read8(in);
			header[2] = read8(in);
			header[3] = read8(in);
			c1 = header[0] & 0xf0;
			c3 = (header[0] & 0x0f) << 4;
			c3 |= header[2];
			c4 = header[3];
			c1 |= ptk_table[header[1]][0];
			c2 = ptk_table[header[1]][1];
			write8(out, c1);
			write8(out, c2);
			write8(out, c3);
			write8(out, c4);
		}
	}

	/* sample data */
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_pru1 (uint8 *data, int s)
{
	int start = 0;

	PW_REQUEST_DATA(s, 1080);

	if (data[1080] != 'S' || data[1081] != 'N' ||
		data[1082] != 'T' || data[1083] != '.')
		return -1;

	/* test 2 */
	if (data[start + 951] != 0x7f)
		return -1;

	/* test 3 */
	if (data[start + 950] > 0x7f)
		return -1;

	return 0;
}

