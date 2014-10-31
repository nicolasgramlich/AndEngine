/*
 * NovoTrade.c   Copyright (C) 2007 Asle / ReDoX
 * xmp version Copyright (C) 2009 Claudio Matsuoka
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int test_ntp (uint8 *, int);
static int depack_ntp (FILE *, FILE *);

struct pw_format pw_ntp = {
	"NTP",
	"Novotrade Packer",
	0x00,
	test_ntp,
	depack_ntp
};


static int depack_ntp(FILE *in, FILE *out)
{
	uint8 buf[1024];
	int i, j;
	int pat_addr[128];
	short body_addr, smp_addr, nins, len, npat;
	int size, ssize = 0;

	read32b(in);				/* skip MODU */

	pw_move_data(out, in, 16);		/* title */
	write32b(out, 0);
	
	body_addr = read16b(in) + 4;		/* get 'BODY' address */
	nins = read16b(in);			/* number of samples */
	len = read16b(in);			/* size of pattern list */
	npat = read16b(in);			/* number of patterns stored */
	smp_addr = read16b(in) + body_addr + 4;	/* get 'SAMP' address */

	memset(buf, 0, 930);

	/* instruments */
	for (i = 0; i < nins; i++) {
		int x = read8(in);		/* instrument number */

		if (x > 30) {
			fseek(in, 7, SEEK_CUR);
			continue;
		}

		x *= 30;

		buf[x + 25] = read8(in);	/* volume */

		size = read16b(in);		/* size */
		buf[x + 22] = size >> 8;
		buf[x + 23] = size & 0xff;
		ssize += size * 2;

		buf[x + 26] = read8(in);	/* loop start */
		buf[x + 27] = read8(in);

		buf[x + 28] = read8(in);	/* loop size */
		buf[x + 29] = read8(in);
	}
	fwrite(buf, 930, 1, out);

	write8(out, len);
	write8(out, 0x7f);

	/* pattern list */
	memset(buf, 0, 128);
	for (i = 0; i < len; i++)
		buf[i] = read16b(in);
	fwrite(buf, 128, 1, out);

	/* pattern addresses now */
	/* Where is on it */
	memset(pat_addr, 0, 256);
	for (i = 0; i < npat; i++)
		pat_addr[i] = read16b(in);

	write32b(out, PW_MOD_MAGIC);

	/* pattern data now ... *gee* */
	for (i = 0; i < npat; i++) {
		fseek(in, body_addr + 4 + pat_addr[i], SEEK_SET);
		memset(buf, 0, 1024);

		for (j = 0; j < 64; j++) {
			int x = read16b(in);

			if (x & 0x0001)
				fread(buf + j * 16, 1, 4, in);
			if (x & 0x0002)
				fread(buf + j * 16 + 4, 1, 4, in);
			if (x & 0x0004)
				fread(buf + j * 16 + 8, 1, 4, in);
			if (x & 0x0008)
				fread(buf + j * 16 + 12, 1, 4, in);
		}
		fwrite(buf, 1024, 1, out);
	}

	/* samples */
	fseek(in, smp_addr, SEEK_SET);
	pw_move_data(out, in, ssize);
	
	return 0;
}


static int test_ntp(uint8 *data, int s)
{
	int j, k;
	int start = 0;

	PW_REQUEST_DATA(s, 64);
	if (readmem32b(data + start) != MAGIC4('M','O','D','U'))
		return -1;

	j = readmem16b(data + start + 20) + 4;		/* "BODY" tag */
	k = readmem16b(data + start + 28) + j + 4;	/* "SAMP" tag */

	PW_REQUEST_DATA(s, j + 4);
	if (readmem32b(data + start + j) != MAGIC4('B','O','D','Y'))
		return -1;

	PW_REQUEST_DATA(s, k + 4);
	if (readmem32b(data + start + k) != MAGIC4('S','A','M','P'))
		return -1;

	return 0;
}

