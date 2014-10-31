/*
 * TrackerPacker_v3.c   Copyright (C) 1998 Asle / ReDoX
 *                      Copyright (C) 2007 Claudio Matsuoka
 *
 * Converts tp3 packed MODs back to PTK MODs
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int depack_tp3 (FILE *, FILE *);
static int test_tp3 (uint8 *, int);

struct pw_format pw_tp3 = {
        "TP3",
        "Tracker Packer v3",
        0x00,
        test_tp3,
        depack_tp3
};


static int depack_tp3(FILE *in, FILE *out)
{
	uint8 c1, c2, c3, c4;
	uint8 pnum[128];
	uint8 pdata[1024];
	uint8 tmp[50];
	uint8 note, ins, fxt, fxp;
	uint8 npat, nsmp;
	uint8 len;
	int trk_ofs[128][4];
	int i = 0, j = 0, k;
	int pat_ofs = 999999;
	int size, ssize = 0;
	int max_trk_ofs = 0;

	memset(trk_ofs, 0, 128 * 4 * 4);
	memset(pnum, 0, 128);

	fseek(in, 8, SEEK_CUR);
	pw_move_data(out, in, 20);		/* title */
	nsmp = read16b(in) / 8;			/* number of sample */

	for (i = 0; i < nsmp; i++) {
		pw_write_zero(out, 22);		/*sample name */

		c3 = read8(in);			/* read finetune */
		c4 = read8(in);			/* read volume */

		write16b(out, size = read16b(in)); /* size */
		ssize += size * 2;

		write8(out, c3);		/* write finetune */
		write8(out, c4);		/* write volume */

		write16b(out, read16b(in));	/* loop start */
		write16b(out, read16b(in));	/* loop size */
	}

	memset(tmp, 0, 30);
	tmp[29] = 0x01;

	for (; i < 31; i++)
		fwrite(tmp, 30, 1, out);

	/* read size of pattern table */
	read8(in);
	write8(out, len = read8(in));		/* sequence length */
	write8(out, 0x7f);			/* ntk byte */

	for (npat = i = 0; i < len; i++) {
		pnum[i] = read16b(in) / 8;
		if (pnum[i] > npat)
			npat = pnum[i];
	}

	/* read tracks addresses */
	/* bypass 4 bytes or not ?!? */
	/* Here, I choose not :) */

	for (i = 0; i <= npat; i++) {
		for (j = 0; j < 4; j++) {
			trk_ofs[i][j] = read16b(in);
			if (trk_ofs[i][j] > max_trk_ofs)
				max_trk_ofs = trk_ofs[i][j];
		}
	}

	fwrite(pnum, 128, 1, out);		/* write pattern list */
	write32b(out, PW_MOD_MAGIC);		/* ID string */

	pat_ofs = ftell(in) + 2;

	/* pattern datas */
	for (i = 0; i <= npat; i++) {
		memset(pdata, 0, 1024);

		for (j = 0; j < 4; j++) {
			int where;

			fseek(in, pat_ofs + trk_ofs[i][j], SEEK_SET);

			for (k = 0; k < 64; k++) {
				int x = k * 16 + j * 4;

				c1 = read8(in);
				if ((c1 & 0xc0) == 0xc0) {
					//k += (0x100 - c1);
					//k -= 1;
					k += 0x80 - (c1 & 0x3f);
					continue;
				}

				if ((c1 & 0xc0) == 0x80) {
					c2 = read8(in);
					fxt = (c1 >> 1) & 0x0f;
					fxp = c2;
					if ((fxt == 0x05) || (fxt == 0x06)
						|| (fxt == 0x0A)) {
						if (fxp > 0x80)
							fxp = 0x100 - fxp;
						else if (fxp <= 0x80)
							fxp = (fxp << 4) & 0xf0;
					}
					if (fxt == 0x08)
						fxt = 0x00;
					pdata[x + 2] = fxt;
					pdata[x + 3] = fxp;
					continue;
				}

				c2 = read8(in);

				ins = ((c2 >> 4) & 0x0f) | ((c1 >> 2) & 0x10);

				if ((c1 & 0x40) == 0x40)
					note = 0x7f - c1;
				else
					note = c1 & 0x3f;

				fxt = c2 & 0x0f;

				if (fxt == 0x00) {
					pdata[x] = ins & 0xf0;
					pdata[x] |= ptk_table[note][0];
					pdata[x + 1] = ptk_table[note][1];
					pdata[x + 2] = (ins << 4) & 0xf0;
					continue;
				}

				c3 = read8(in);

				if (fxt == 0x08)
					fxt = 0x00;

				fxp = c3;
				if ((fxt == 0x05) || (fxt == 0x06)
					|| (fxt == 0x0A)) {
					if (fxp > 0x80)
						fxp = 0x100 - fxp;
					else if (fxp <= 0x80)
						fxp = (fxp << 4) & 0xf0;
				}

				pdata[x] = ins & 0xf0;
				pdata[x] |= ptk_table[note][0];
				pdata[x + 1] = ptk_table[note][1];
				pdata[x + 2] = (ins << 4) & 0xf0;
				pdata[x + 2] |= fxt;
				pdata[x + 3] = fxp;
			}
			where = ftell(in);
			if (where > max_trk_ofs)
				max_trk_ofs = where;
		}
		fwrite(pdata, 1024, 1, out);
	}

	/* Sample data */
	if (((max_trk_ofs / 2) * 2) != max_trk_ofs)
		max_trk_ofs += 1;

	fseek(in, max_trk_ofs, SEEK_SET);
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_tp3(uint8 *data, int s)
{
	int start = 0;
	int j, k, l, m, n;
	int ssize;

	PW_REQUEST_DATA(s, 1024);

	if (memcmp(data, "CPLX_TP3", 8))
		return -1;

	/* number of sample */
	l = ((data[start + 28] << 8) + data[start + 29]);

	if ((((l / 8) * 8) != l) || (l == 0))
		return -1;

	l /= 8;

	/* l is the number of sample */

	/* test finetunes */
	for (k = 0; k < l; k++) {
		if (data[start + 30 + k * 8] > 0x0f)
			return -1;
	}

	/* test volumes */
	for (k = 0; k < l; k++) {
		if (data[start + 31 + k * 8] > 0x40)
			return - 1;
	}

	/* test sample sizes */
	ssize = 0;
	for (k = 0; k < l; k++) {
		int x = start + k * 8;

		j = ((data[x + 32] << 8) + data[x + 33]) * 2;	/* size */
		m = ((data[x + 34] << 8) + data[x + 35]) * 2;	/* loop start */
		n = ((data[x + 36] << 8) + data[x + 37]) * 2;	/* loop size */

		if ((j > 0xFFFF) || (m > 0xFFFF) || (n > 0xFFFF))
			return -1;

		if ((m + n) > (j + 2))
			return -1;

		if ((m != 0) && (n == 0))
			return -1;

		ssize += j;
	}

	if (ssize <= 4)
		return -1;

	/* pattern list size */
	j = data[start + l * 8 + 31];
	if ((l == 0) || (l > 128))
		return -1;

	/* j is the size of the pattern list */
	/* l is the number of sample */
	/* ssize is the sample data size */

	return 0;
}
