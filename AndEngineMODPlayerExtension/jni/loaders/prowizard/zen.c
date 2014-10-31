/*
 * Zen_Packer.c   Copyright (C) 1998 Asle / ReDoX
 *                Copyright (C) 2006-2007 Claudio Matsuoka
 *
 * Converts ZEN packed MODs back to PTK MODs
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"


static int test_zen (uint8 *, int);
static int depack_zen (FILE *, FILE *);

struct pw_format pw_zen = {
	"ZEN",
	"Zen Packer",
	0x00,
	test_zen,
	depack_zen
};

static int depack_zen(FILE *in, FILE *out)
{
	uint8 c1, c2, c3, c4;
	uint8 finetune, vol;
	uint8 pat_pos;
	uint8 pat_max;
	uint8 note, ins, fxt, fxp;
	uint8 pat[1024];
	uint8 ptable[128];
	int size, ssize = 0;
	int paddr[128];
	int paddr_Real[128];
	int ptable_addr;
	int sdata_addr = 999999l;
	int i, j, k;

	memset(paddr, 0, 128 * 4);
	memset(paddr_Real, 0, 128 * 4);
	memset(ptable, 0, 128);

	ptable_addr = read32b(in);	/* read pattern table address */
	pat_max = read8(in);		/* read patmax */
	pat_pos = read8(in);		/* read size of pattern table */

	pw_write_zero(out, 20);		/* write title */

	for (i = 0; i < 31; i++) {
		pw_write_zero(out, 22);			/* sample name */

		finetune = read16b(in) / 0x48;		/* read finetune */

		read8(in);
		vol = read8(in);			/* read volume */

		write16b(out, size = read16b(in));	/* read sample size */
		ssize += size * 2;

		write8(out, finetune);			/* write finetune */
		write8(out, vol);			/* write volume */

		size = read16b(in);			/* read loop size */

		k = read32b(in);			/* sample start addr */
		if (k < sdata_addr)
			sdata_addr = k;

		/* read loop start address */
		j = (read32b(in) - k) / 2;

		write16b(out, j);	/* write loop start */
		write16b(out, size);	/* write loop size */
	}

	write8(out, pat_pos);		/* write size of pattern list */
	write8(out, 0x7f);		/* write ntk byte */

	/* read pattern table .. */
	fseek(in, ptable_addr, SEEK_SET);
	for (i = 0; i < pat_pos; i++)
		paddr[i] = read32b(in);

	/* deduce pattern list */
	c4 = 0;
	for (i = 0; i < pat_pos; i++) {
		if (i == 0) {
			ptable[0] = 0;
			paddr_Real[0] = paddr[0];
			c4++;
			continue;
		}
		for (j = 0; j < i; j++) {
			if (paddr[i] == paddr[j]) {
				ptable[i] = ptable[j];
				break;
			}
		}
		if (j == i) {
			paddr_Real[c4] = paddr[i];
			ptable[i] = c4;
			c4++;
		}
	}

	fwrite(ptable, 128, 1, out);		/* write pattern table */
	write32b(out, PW_MOD_MAGIC);		/* write ptk ID */

	/* pattern data */
	/*printf ( "converting pattern datas " ); */
	for (i = 0; i <= pat_max; i++) {
		memset(pat, 0, 1024);
		fseek(in, paddr_Real[i], SEEK_SET);
		for (j = 0; j < 256; j++) {
			c1 = read8(in);
			c2 = read8(in);
			c3 = read8(in);
			c4 = read8(in);

			note = (c2 & 0x7f) / 2;
			fxp = c4;
			ins = ((c2 << 4) & 0x10) | ((c3 >> 4) & 0x0f);
			fxt = c3 & 0x0f;

			k = c1;
			pat[k * 4] = ins & 0xf0;
			pat[k * 4] |= ptk_table[note][0];
			pat[k * 4 + 1] = ptk_table[note][1];
			pat[k * 4 + 2] = fxt | ((ins << 4) & 0xf0);
			pat[k * 4 + 3] = fxp;
			j = c1;
		}
		fwrite (pat, 1024, 1, out);
		/*printf ( "." ); */
	}
	/*printf ( " ok\n" ); */

	/* sample data */
	fseek(in, sdata_addr, SEEK_SET);
	pw_move_data(out, in, ssize);

	return 0;
}


static int test_zen(uint8 *data, int s)
{
	int j, k, l, m, n, o;
	int start = 0, ssize;

	PW_REQUEST_DATA(s, 9 + 16 * 31);

	/* test #2 */
	l = readmem32b(data + start);
	if (l < 502 || l > 2163190L)
		return -1;
	/* l is the address of the pattern list */

	for (k = 0; k < 31; k++) {
		/* volumes */
		if (data[start + 9 + (16 * k)] > 0x40)
			return -1;

		/* finetune */
		if (readmem16b(data + start + 6 + (k * 16)) % 72)
			return -1;
	}

	/* smp sizes .. */
	n = 0;
	for (k = 0; k < 31; k++) {
		o = readmem16b(data + start + 10 + k * 16) * 2;
		m = readmem16b(data + start + 12 + k * 16) * 2;
		j = readmem32b(data + start + 14 + k * 16);

		/* sample size and loop size > 64k ? */
		if (o > 0xFFFF || m > 0xFFFF)
			return -1;

		/* sample address < pattern table address? */
		if (j < l)
			return -1;

#if 0
		/* too big an address ? */
		if (j > in_size) {
			Test = BAD;
			return;
		}
#endif

		/* get the nbr of the highest sample address and its size */
		if (j > n) {
			n = j;
			ssize = o;
		}
	}
	/* n is the highest sample data address */
	/* ssize is the size of the same sample */

	/* test size of the pattern list */
	j = data[start + 5];
	if (j > 0x7f || j == 0)
		return -1;

	PW_REQUEST_DATA(s, start + l + j * 4 + 4);

	/* test if the end of pattern list is $FFFFFFFF */
	if (readmem32b(data + start + l + j * 4) != 0xffffffff)
		return -1;

	/* n is the highest address of a sample data */
	/* ssize is its size */

	return 0;
}
