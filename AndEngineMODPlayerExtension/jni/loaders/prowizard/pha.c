/*
 * PhaPacker.c   Copyright (C) 1996-1999 Asle / ReDoX
 *               Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Converts PHA packed MODs back to PTK MODs
 * nth revision :(.
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

static int test_pha (uint8 *, int);
static int depack_pha (FILE *, FILE *);

struct pw_format pw_pha = {
	"PHA",
	"Pha Packer",
	0x00,
	test_pha,
	depack_pha
};

static int depack_pha(FILE *in, FILE *out)
{
	uint8 c1, c2;
	uint8 pnum[128];
	uint8 pnum1[128];
	uint8 nop;
	uint8 *pdata;
	uint8 *pat;
	uint8 onote[4][4];
	uint8 note, ins, fxt, fxp;
	uint8 npat = 0x00;
	int paddr[128];
	int i, j, k;
	int paddr1[128];
	int paddr2[128];
	int tmp_ptr, tmp1, tmp2;
	int pat_addr;
	int psize;
	int size, ssize = 0;
	int smp_addr;
	short ocpt[4];

	memset(paddr, 0, 128 * 4);
	memset(paddr1, 0, 128 * 4);
	memset(paddr2, 0, 128 * 4);
	memset(pnum, 0, 128);
	memset(pnum1, 0, 128);
	memset(onote, 0, 4 * 4);
	memset(ocpt, 0, 4 * 2);

	pw_write_zero(out, 20);			/* title */

	for (i = 0; i < 31; i++) {
		pw_write_zero(out, 22);			/*sample name */
		write16b(out, size = read16b(in));	/* size */
		ssize += size * 2;
		read8(in);
		write8(out, 0);				/* finetune byte */
		write8(out, read8(in));			/* volume */
		write16b(out, read16b(in));		/* loop start */
		write16b(out, read16b(in));		/* loop size */

		read32b(in);

		c1 = read8(in);
		if(c1 != 0x00)
			c1 += 0x0b;
		fseek(out, -6, SEEK_END);
		write8(out, c1);
		fseek(out, 0, SEEK_END);
		fseek(in, 1, SEEK_CUR);
	}

	fseek(in, 14, SEEK_CUR);		/* bypass unknown 14 bytes */

	for (i = 0; i < 128; i++)
		paddr[i] = read32b(in);

	/* ordering of patterns addresses */

	tmp_ptr = 0;
	for (i = 0; i < 128; i++) {
		if (i == 0) {
			pnum[0] = 0;
			tmp_ptr++;
			continue;
		}

		for (j = 0; j < i; j++) {
			if (paddr[i] == paddr[j]) {
				pnum[i] = pnum[j];
				break;
			}
		}
		if (j == i)
			pnum[i] = tmp_ptr++;
	}

	/* correct re-order */
	for (i = 0; i < 128; i++)
		paddr1[i] = paddr[i];

restart:
	for (i = 0; i < 128; i++) {
		for (j = 0; j < i; j++) {
			if (paddr1[i] < paddr1[j]) {
				tmp2 = pnum[j];
				pnum[j] = pnum[i];
				pnum[i] = tmp2;
				tmp1 = paddr1[j];
				paddr1[j] = paddr1[i];
				paddr1[i] = tmp1;
				goto restart;
			}
		}
	}

	j = 0;
	for (i = 0; i < 128; i++) {
		if (i == 0) {
			paddr2[j] = paddr1[i];
			continue;
		}

		if (paddr1[i] == paddr2[j])
			continue;
		paddr2[++j] = paddr1[i];
	}

	/* try to take care of unused patterns ... HARRRRRRD */
	memset(paddr1, 0, 128 * 4);
	j = 0;
	k = paddr[0];
	/* 120 ... leaves 8 unused ptk_tableible patterns .. */
	for (i = 0; i < 120; i++) {
		paddr1[j] = paddr2[i];
		j += 1;
		if ((paddr2[i + 1] - paddr2[i]) > 1024) {
			paddr1[j] = paddr2[i] + 1024;
			j += 1;
		}
	}

	for (c1 = 0; c1 < 128; c1++) {
		for (c2 = 0; c2 < 128; c2++)
			if (paddr[c1] == paddr1[c2]) {
				pnum1[c1] = c2;
			}
	}

	memset(pnum, 0, 128);
	pat_addr = 999999l;
	for (i = 0; i < 128; i++) {
		pnum[i] = pnum1[i];
		if (paddr[i] < pat_addr)
			pat_addr = paddr[i];
	}

	/* try to get the number of pattern in pattern list */
	for (nop = 128; nop > 0; nop--) {
		if (pnum[nop - 1] != 0)
			break;
	}

	/* write this value */
	write8(out, nop);

	/* get highest pattern number */
	for (i = 0; i < nop; i++)
		if (pnum[i] > npat)
			npat = pnum[i];

	write8(out, 0x7f);			/* ntk restart byte */

	for (i = 0; i < 128; i++)		/* write pattern list */
		write8(out, pnum[i]);

	write32b(out, PW_MOD_MAGIC);		/* ID string */

	smp_addr = ftell(in);
	fseek(in, pat_addr, SEEK_SET);

	/* pattern datas */
	/* read ALL pattern data */

/* FIXME: shouldn't use file size */
#if 0
	j = ftell (in);
	fseek (in, 0, 2);	/* SEEK_END */
	psize = ftell (in) - j;
	fseek (in, j, 0);	/* SEEK_SET */
#endif
	psize = npat * 1024;
	pdata = (uint8 *) malloc (psize);
	psize = fread(pdata, 1, psize, in);
	npat += 1;		/* coz first value is $00 */
	pat = (uint8 *)malloc(npat * 1024);
	memset(pat, 0, npat * 1024);

	j = 0;
	for (i = 0; j < psize; i++) {
		if (pdata[i] == 0xff) {
			i += 1;
			ocpt[(k + 3) % 4] = 0xff - pdata[i];
			continue;
		}
		if (ocpt[k % 4] != 0) {
			ins = onote[k % 4][0];
			note = onote[k % 4][1];
			fxt = onote[k % 4][2];
			fxp = onote[k % 4][3];
			ocpt[k % 4] -= 1;

			pat[j] = ins & 0xf0;
			pat[j] |= ptk_table[(note / 2)][0];
			pat[j + 1] = ptk_table[(note / 2)][1];
			pat[j + 2] = (ins << 4) & 0xf0;
			pat[j + 2] |= fxt;
			pat[j + 3] = fxp;
			k += 1;
			j += 4;
			i -= 1;
			continue;
		}
		ins = pdata[i];
		note = pdata[i + 1];
		fxt = pdata[i + 2];
		fxp = pdata[i + 3];
		onote[k % 4][0] = ins;
		onote[k % 4][1] = note;
		onote[k % 4][2] = fxt;
		onote[k % 4][3] = fxp;
		i += 3;
		pat[j] = ins & 0xf0;
		pat[j] |= ptk_table[(note / 2)][0];
		pat[j + 1] = ptk_table[(note / 2)][1];
		pat[j + 2] = (ins << 4) & 0xf0;
		pat[j + 2] |= fxt;
		pat[j + 3] = fxp;
		k += 1;
		j += 4;
	}
	fwrite(pat, npat * 1024, 1, out);
	free(pdata);
	free(pat);

	/* Sample data */
	fseek(in, smp_addr, SEEK_SET);
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_pha (uint8 *data, int s)
{
	int j, k, l, m, n;
	int start = 0, ssize;

	PW_REQUEST_DATA(s, 451 + 128 * 4);

	if (data[10] != 0x03 || data[11] != 0xc0)
		return -1;

	/* test #2 (volumes,sample addresses and whole sample size) */
	l = 0;
	for (j = 0; j < 31; j++) {
		/* sample size */
		n = readmem16b(data + start + j * 14) * 2;
		l += n;

		if (data[start + j * 14 + 3] > 0x40)
			return -1;

		/* loop start */
		m = readmem16b(data + start + j * 14 + 4) * 2;

		if (m > l)
			return -1;

		/* address of sample data */
		k = readmem32b(data + start + j * 14 + 8);

		if (k < 0x3C0)
			return -1;
	}

	if (l <= 2 || l > (31 * 65535))
		return -1;

	/* test #3 (addresses of pattern in file ... ptk_tableible ?) */
	/* l is the whole sample size */
	/* ssize is used here as a variable ... set to 0 afterward */
	l += 960;
	k = 0;
	for (j = 0; j < 128; j++) {
		ssize = readmem32b(data + start + 448 + j * 4);

		if (ssize > k)
			k = ssize;

		if ((ssize + 2) < l)
			return -1;
	}
	ssize = 0;
	/* k is the highest pattern data address */

	return 0;
}
