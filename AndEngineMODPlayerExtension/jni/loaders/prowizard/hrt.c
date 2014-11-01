/*
 * Hornet_Packer.c Copyright (C) 1997 Asle / ReDoX
 * xmp version Copyright (C) 2009 Claudio Matsuoka
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int test_hrt (uint8 *, int);
static int depack_hrt (FILE *, FILE *);

struct pw_format pw_hrt = {
	"HRT",
	"Hornet Packer",
	0x00,
	test_hrt,
	depack_hrt
};

static int depack_hrt(FILE *in, FILE *out)
{
	uint8 buf[1024];
	uint8 c1, c2, c3, c4;
	int len, npat;
	int ssize = 0;
	int i, j;

	memset(buf, 0, 950);

	fread (buf, 950, 1, in);		/* read header */
	for (i = 0; i < 31; i++)		/* erase addresses */
		*(uint32 *)(buf + 38 + 30 * i) = 0;
	fwrite(buf, 950, 1, out);		/* write header */

	for (i = 0; i < 31; i++)		/* samples size */
		ssize += readmem16b(buf + 42 + 30 * i) * 2;

	write8(out, len = read8(in));		/* song length */
	write8(out, read8(in));			/* nst byte */

	fread(buf, 1, 128, in);			/* pattern list */

	npat = 0;				/* number of patterns */
	for (i = 0; i < 128; i++) {
		if (buf[i] > npat)
			npat = buf[i];
	}
	npat++;

	write32b(out, PW_MOD_MAGIC);		/* write ptk ID */

	/* pattern data */
	fseek(in, 1084, SEEK_SET);
	for (i = 0; i < npat; i++) {
		for (j = 0; j < 256; j++) {
			buf[0] = read8(in);
			buf[1] = read8(in);
			buf[2] = read8(in);
			buf[3] = read8(in);

			buf[0] /= 2;
			c1 = buf[0] & 0xf0;

			if (buf[1] == 0)
				c2 = 0;
			else {
				c1 |= ptk_table[buf[1] / 2][0];
				c2 = ptk_table[buf[1] / 2][1];
			}

			c3 = ((buf[0] << 4) & 0xf0) | buf[2];
			c4 = buf[3];

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


static int test_hrt(uint8 *data, int s)
{
	int i;
	int start = 0;

	PW_REQUEST_DATA(s, 1084);

	if (readmem32b(data + 1080) != MAGIC4('H','R','T','!'))
		return -1;

	for (i = 0; i < 31; i++) {
		/* test finetune */
		if (data[start + 20 + i * 30 + 24] > 0x0f)
			return -1;

		/* test volume */
		if (data[start + 20 + i * 30 + 25] > 0x40)
			return -1;
		
	}

	return 0;
}
