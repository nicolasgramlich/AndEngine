/*
 * The_Player_4.0.c   Copyright (C) 1997 Asle / ReDoX
 *                    Copyright (C) 2007 Claudio Matsuoka
 *
 * The Player 4.0a and 4.0b to Protracker.
 */

#include <string.h>
#include <stdlib.h>
#include "prowiz.h"

#define MAGIC_P40A	MAGIC4('P','4','0','A')
#define MAGIC_P40B	MAGIC4('P','4','0','B')
#define MAGIC_P41A	MAGIC4('P','4','1','A')


static int test_p4x(uint8 *, int);
static int depack_p4x (FILE *, FILE *);

struct pw_format pw_p4x = {
	"P4x",
	"The Player 4.x",
	0x00,
	test_p4x,
	depack_p4x
};


struct smp {
	uint8 name[22];
	int addr;
	uint16 size;
	int loop_addr;
	uint16 loop_size;
	int16 fine;
	uint8 vol;
};

static int depack_p4x (FILE *in, FILE *out)
{
	uint8 c1, c2, c3, c4, c5;
	uint8 tmp[1024];
	uint8 len, npat, nsmp;
	uint8 sample, mynote, note[2];
	uint8 tr[512][256];
	short track_addr[128][4];
	int trkdat_ofs, trktab_ofs, smp_ofs;
	int ssize = 0;
	int SampleAddress[31];
	int SampleSize[31];
	int i, j, k, l, a, b, c;
	struct smp ins;
	uint32 id;

	memset(track_addr, 0, 128 * 4 * 2);
	memset(tr, 0, 512 << 8);
	memset(SampleAddress, 0, 31 * 4);
	memset(SampleSize, 0, 31 * 4);

	id = read32b(in);
	if (id == MAGIC_P40A) {
		pw_p4x.id = "P40A";
		pw_p4x.name = "The Player 4.0A";
	} else if (id == MAGIC_P40B) {
		pw_p4x.id = "P40B";
		pw_p4x.name = "The Player 4.0B";
	} else {
		pw_p4x.id = "P41A";
		pw_p4x.name = "The Player 4.1A";
	}

	npat = read8(in);		/* read Real number of pattern */
	len = read8(in);		/* read number of pattern in list */
	nsmp = read8(in);		/* read number of samples */
	read8(in);			/* bypass empty byte */
	trkdat_ofs = read32b(in);	/* read track data address */
	trktab_ofs = read32b(in);	/* read track table address */
	smp_ofs = read32b(in);		/* read sample data address */

	pw_write_zero(out, 20);		/* write title */

	/* sample headers stuff */
	for (i = 0; i < nsmp; i++) {
		ins.addr = read32b(in);		/* read sample address */
		SampleAddress[i] = ins.addr;
		ins.size = read16b(in);		/* read sample size */
		SampleSize[i] = ins.size * 2;
		ssize += SampleSize[i];
		ins.loop_addr = read32b(in);	/* loop start */
		ins.loop_size = read16b(in);	/* loop size */
		ins.fine = 0;
		if (id == MAGIC_P40A || id == MAGIC_P40B)
			ins.fine = read16b(in);	/* finetune */
		read8(in);			/* bypass 00h */
		ins.vol = read8(in);		/* read vol */
		if (id == MAGIC_P41A)
			ins.fine = read16b(in);	/* finetune */

		/* writing now */
		pw_write_zero(out, 22);		/* sample name */
		write16b(out, ins.size);
		write8(out, ins.fine / 74);
		write8(out, ins.vol);
		write16b(out, (ins.loop_addr - ins.addr) / 2);
		write16b(out, ins.loop_size);
	}

	/* go up to 31 samples */
	memset(tmp, 0, 30);
	tmp[29] = 0x01;
	for (; i < 31; i++)
		fwrite (tmp, 30, 1, out);

	write8(out, len);		/* write size of pattern list */
	write8(out, 0x7f);		/* write noisetracker byte */

	fseek(in, trktab_ofs + 4, SEEK_SET);

	for (c1 = 0; c1 < len; c1++)	/* write pattern list */
		write8(out, c1);
	for (; c1 < 128; c1++)
		write8(out, 0);

	write32b(out, PW_MOD_MAGIC);	/* write ptk ID */

	for (i = 0; i < len; i++) {	/* read all track addresses */
		for (j = 0; j < 4; j++)
			track_addr[i][j] = read16b(in) + trkdat_ofs + 4;
	}

	fseek(in, trkdat_ofs + 4, SEEK_SET);

	for (i = 0; i < len; i++) {	/* rewrite the track data */
		for (j = 0; j < 4; j++) {
			int y, x = i * 4 + j;

			fseek(in, track_addr[i][j], SEEK_SET);

			for (k = 0; k < 64; k++) {
				c1 = read8(in);
				c2 = read8(in);
				c3 = read8(in);
				c4 = read8(in);

				if (c1 != 0x80) {
					sample = ((c1 << 4) & 0x10) |
							((c2 >> 4) & 0x0f);
					memset(note, 0, 2);
					mynote = c1 & 0x7f;
					note[0] = ptk_table[mynote / 2][0];
					note[1] = ptk_table[mynote / 2][1];
					switch (c2 & 0x0f) {
					case 0x08:
						c2 -= 0x08;
						break;
					case 0x05:
					case 0x06:
					case 0x0A:
						if (c3 >= 0x80)
							c3 = (c3 << 4) & 0xf0;
						break;
					default:
						break;
					}
					y = k * 4;
					tr[x][y] = (sample & 0xf0) |
							(note[0] & 0x0f);
					tr[x][y + 1] = note[1];
					tr[x][y + 2] = c2;
					tr[x][y + 3] = c3;

					if ((c4 > 0x00) && (c4 < 0x80))
						k += c4;
					if (c4 > 0x7f) {
						k++;
						for (l = 256; l > c4; l--) {
							y = k * 4;
							tr[x][y] =
							  (sample & 0xf0) |
							  (note[0] & 0x0f);
							tr[x][y + 1] = note[1];
							tr[x][y + 2] = c2;
							tr[x][y + 3] = c3;
							k++;
						}
						k--;
					}
					continue;
				}

				a = ftell (in);

				c5 = c2;
				b = (c3 << 8) + c4 + trkdat_ofs + 4;

				fseek(in, b, SEEK_SET);

				for (c = 0; c <= c5; c++) {
					c1 = read8(in);
					c2 = read8(in);
					c3 = read8(in);
					c4 = read8(in);

					sample = ((c1 << 4) & 0x10) |
						((c2 >> 4) & 0x0f);
					memset(note, 0, 2);
					mynote = c1 & 0x7f;
					note[0] = ptk_table[mynote / 2][0];
					note[1] = ptk_table[mynote / 2][1];
					switch (c2 & 0x0f) {
					case 0x08:
						c2 -= 0x08;
						break;
					case 0x05:
					case 0x06:
					case 0x0A:
						if (c3 >= 0x80)
							c3 = (c3 << 4) & 0xf0;
						break;
					default:
						break;
					}
					tr[x][k * 4] = (sample & 0xf0) |
							(note[0] & 0x0f);
					tr[x][k * 4 + 1] = note[1];
					tr[x][k * 4 + 2] = c2;
					tr[x][k * 4 + 3] = c3;

					if ((c4 > 0x00) && (c4 < 0x80))
						k += c4;
					if (c4 > 0x7f) {
						k++;
						for (l = 256; l > c4; l--) {
							y = k * 4;
							tr[x][y] =
							  (sample & 0xf0) |
							  (note [0] & 0x0f);
							tr[x][y + 1] = note[1];
							tr[x][y + 2] = c2;
							tr[x][y + 3] = c3;
							k++;
						}
						k--;
					}
					k++;
				}
				k--;
				fseek(in, a, SEEK_SET);
			}
		}
	}

	/* write pattern data */
	for (i = 0; i < len; i++) {
		memset(tmp, 0, 1024);
		for (j = 0; j < 64; j++) {
			for (k = 0; k < 4; k++) {
				int x = j * 16 + k * 4;
				int y = k + i * 4;

				tmp[x + 0] = tr[y][j * 4];
				tmp[x + 1] = tr[y][j * 4 + 1];
				tmp[x + 2] = tr[y][j * 4 + 2];
				tmp[x + 3] = tr[y][j * 4 + 3];
			}
		}
		fwrite(tmp, 1024, 1, out);
	}

	/* read and write sample data */
	for (i = 0; i < nsmp; i++) {
		fseek(in, SampleAddress[i] + smp_ofs, SEEK_SET);
		pw_move_data(out, in, SampleSize[i]);
	}

	return 0;
}


static int test_p4x(uint8 *data, int s)
{
	//int j, k, l, o, n;
	//int start = 0, ssize;
	uint32 id;

	id = readmem32b(data);

	if (id == MAGIC_P40A || id == MAGIC_P40B || id == MAGIC_P41A)
		return 0;

	return -1;

#if 0
	/* number of pattern (real) */
	j = data[start + 4];
	if (j > 0x7f)
		return -1;

	/* number of sample */
	k = data[start + 6];
	if ((k > 0x1F) || (k == 0))
		return -1;

	/* test volumes */
	for (l = 0; l < k; l++) {
		if (data[start + 35 + l * 16] > 0x40)
			return -1;
	}

	/* test sample sizes */
	ssize = 0;
	for (l = 0; l < k; l++) {
		/* size */
		o = (data[start + 24 + l * 16] << 8) +
			data[start + 25 + l * 16];
		/* loop size */
		n = (data[start + 30 + l * 16] << 8) +
			data[start + 31 + l * 16];
		o *= 2;
		n *= 2;

		if ((o > 0xFFFF) || (n > 0xFFFF))
			return -1;

		if (n > (o + 2))
			return -1;

		ssize += o;
	}
	if (ssize <= 4)
		return -1;

	/* ssize is the size of the sample data .. WRONG !! */
	/* k is the number of samples */
	return 0;
#endif
}
