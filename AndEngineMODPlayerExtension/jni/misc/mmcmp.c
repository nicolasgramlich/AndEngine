/*
 * This program is  free software; you can redistribute it  and modify it
 * under the terms of the GNU  General Public License as published by the
 * Free Software Foundation; either version 2  of the license or (at your
 * option) any later version.
 *
 * Author: Olivier Lapicque <olivierl@jps.net>
 * Modified by Claudio Matsuoka for xmp
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <string.h>
#include "common.h"

/* #include "stdafx.h" */
/* #include "sndfile.h" */


typedef struct MMCMPFILEHEADER
{
	uint32 id_ziRC;	/* "ziRC" */
	uint32 id_ONia;	/* "ONia" */
	uint16 hdrsize;
} MMCMPFILEHEADER, *LPMMCMPFILEHEADER;

typedef struct MMCMPHEADER
{
	uint16 version;
	uint16 nblocks;
	uint32 filesize;
	uint32 blktable;
	uint8 glb_comp;
	uint8 fmt_comp;
} MMCMPHEADER, *LPMMCMPHEADER;

typedef struct MMCMPBLOCK
{
	uint32 unpk_size;
	uint32 pk_size;
	uint32 xor_chk;
	uint16 sub_blk;
	uint16 flags;
	uint16 tt_entries;
	uint16 num_bits;
} MMCMPBLOCK, *LPMMCMPBLOCK;

typedef struct MMCMPSUBBLOCK
{
	uint32 unpk_pos;
	uint32 unpk_size;
} MMCMPSUBBLOCK, *LPMMCMPSUBBLOCK;

#define mmcmp_COMP		0x0001
#define mmcmp_DELTA		0x0002
#define mmcmp_16BIT		0x0004
#define mmcmp_STEREO	0x0100
#define mmcmp_ABS16		0x0200
#define mmcmp_ENDIAN	0x0400

typedef struct MMCMPBITBUFFER
{
	uint32 bitcount;
	uint32 bitbuffer;
	uint8 * pSrc;
	uint8 * pEnd;

} MMCMPBITBUFFER;


uint32 GetBits(struct MMCMPBITBUFFER *bb, uint32 nBits)
/*--------------------------------------- */
{
	uint32 d;
	if (!nBits) return 0;
	while (bb->bitcount < 24)
	{
		bb->bitbuffer |= ((bb->pSrc < bb->pEnd) ? *bb->pSrc++ : 0) << bb->bitcount;
		bb->bitcount += 8;
	}
	d = bb->bitbuffer & ((1 << nBits) - 1);
	bb->bitbuffer >>= nBits;
	bb->bitcount -= nBits;
	return d;
}

/*#define mmcmp_LOG */

#ifdef mmcmp_LOG
extern void Log(LPCSTR s, uint32 d1=0, uint32 d2=0, uint32 d3=0);
#endif

const uint32 MMCMP8BitCommands[8] =
{
	0x01, 0x03,	0x07, 0x0F,	0x1E, 0x3C,	0x78, 0xF8
};

const uint32 MMCMP8BitFetch[8] =
{
	3, 3, 3, 3, 2, 1, 0, 0
};

const uint32 MMCMP16BitCommands[16] =
{
	0x01, 0x03,	0x07, 0x0F,	0x1E, 0x3C,	0x78, 0xF0,
	0x1F0, 0x3F0, 0x7F0, 0xFF0, 0x1FF0, 0x3FF0, 0x7FF0, 0xFFF0
};

const uint32 MMCMP16BitFetch[16] =
{
	4, 4, 4, 4, 3, 2, 1, 0,
	0, 0, 0, 0, 0, 0, 0, 0
};



static int mmcmp_unpack(uint8 **ppMemFile, uint32 *pdwMemLength)
/*--------------------------------------------------------- */
{
	uint32 dwMemLength = *pdwMemLength;
	uint8 *lpMemFile = *ppMemFile;
	uint8 *pBuffer;
	LPMMCMPFILEHEADER pmfh = (LPMMCMPFILEHEADER)(lpMemFile);
	LPMMCMPHEADER pmmh = (LPMMCMPHEADER)(lpMemFile+10);
	uint32 *pblk_table;
	uint32 dwFileSize;
	uint32 nBlock, i;

	if ((dwMemLength < 256) || (!pmfh) || (pmfh->id_ziRC != 0x4352697A) || (pmfh->id_ONia != 0x61694e4f) || (pmfh->hdrsize < 14)
	 || (!pmmh->nblocks) || (pmmh->filesize < 16) || (pmmh->filesize > 0x8000000)
	 || (pmmh->blktable >= dwMemLength) || (pmmh->blktable + 4*pmmh->nblocks > dwMemLength)) return -1;
	dwFileSize = pmmh->filesize;
/*
	if ((pBuffer = (uint8 *)GlobalAllocPtr(GHND, (dwFileSize + 31) & ~15)) == NULL) return -1;
*/
	if ((pBuffer = (uint8 *)calloc(1, (dwFileSize + 31) & ~15)) == NULL) return -1;
	pblk_table = (uint32 *)(lpMemFile+pmmh->blktable);
	for (nBlock=0; nBlock<pmmh->nblocks; nBlock++)
	{
		uint32 dwMemPos = pblk_table[nBlock];
		LPMMCMPBLOCK pblk = (LPMMCMPBLOCK)(lpMemFile+dwMemPos);
		LPMMCMPSUBBLOCK psubblk = (LPMMCMPSUBBLOCK)(lpMemFile+dwMemPos+20);

		if ((dwMemPos + 20 >= dwMemLength) || (dwMemPos + 20 + pblk->sub_blk*8 >= dwMemLength)) break;
		dwMemPos += 20 + pblk->sub_blk*8;
#ifdef mmcmp_LOG
		Log("block %d: flags=%04X sub_blocks=%d", nBlock, (uint32)pblk->flags, (uint32)pblk->sub_blk);
		Log(" pksize=%d unpksize=%d", pblk->pk_size, pblk->unpk_size);
		Log(" tt_entries=%d num_bits=%d\n", pblk->tt_entries, pblk->num_bits);
#endif
		/* Data is not packed */
		if (!(pblk->flags & mmcmp_COMP))
		{
			for (i=0; i<pblk->sub_blk; i++)
			{
				if ((psubblk->unpk_pos > dwFileSize) || (psubblk->unpk_pos + psubblk->unpk_size > dwFileSize)) break;
#ifdef mmcmp_LOG
				Log("  Unpacked sub-block %d: offset %d, size=%d\n", i, psubblk->unpk_pos, psubblk->unpk_size);
#endif
				memcpy(pBuffer+psubblk->unpk_pos, lpMemFile+dwMemPos, psubblk->unpk_size);
				dwMemPos += psubblk->unpk_size;
				psubblk++;
			}
		} else
		/* Data is 16-bit packed */
		if (pblk->flags & mmcmp_16BIT)
		{
			MMCMPBITBUFFER bb;
			uint16 * pDest = (uint16 *)(pBuffer + psubblk->unpk_pos);
			uint32 dwSize = psubblk->unpk_size >> 1;
			uint32 dwPos = 0;
			uint32 numbits = pblk->num_bits;
			uint32 subblk = 0, oldval = 0;

#ifdef mmcmp_LOG
			Log("  16-bit block: pos=%d size=%d ", psubblk->unpk_pos, psubblk->unpk_size);
			if (pblk->flags & mmcmp_DELTA) Log("DELTA ");
			if (pblk->flags & mmcmp_ABS16) Log("ABS16 ");
			Log("\n");
#endif
			bb.bitcount = 0;
			bb.bitbuffer = 0;
			bb.pSrc = lpMemFile+dwMemPos+pblk->tt_entries;
			bb.pEnd = lpMemFile+dwMemPos+pblk->pk_size;
			while (subblk < pblk->sub_blk)
			{
				uint32 newval = 0x10000;
				uint32 d = GetBits(&bb, numbits+1);

				if (d >= MMCMP16BitCommands[numbits])
				{
					uint32 nFetch = MMCMP16BitFetch[numbits];
					uint32 newbits = GetBits(&bb, nFetch) + ((d - MMCMP16BitCommands[numbits]) << nFetch);
					if (newbits != numbits)
					{
						numbits = newbits & 0x0F;
					} else
					{
						if ((d = GetBits(&bb, 4)) == 0x0F)
						{
							if (GetBits(&bb,1)) break;
							newval = 0xFFFF;
						} else
						{
							newval = 0xFFF0 + d;
						}
					}
				} else
				{
					newval = d;
				}
				if (newval < 0x10000)
				{
					newval = (newval & 1) ? (uint32)(-(int32)((newval+1) >> 1)) : (uint32)(newval >> 1);
					if (pblk->flags & mmcmp_DELTA)
					{
						newval += oldval;
						oldval = newval;
					} else
					if (!(pblk->flags & mmcmp_ABS16))
					{
						newval ^= 0x8000;
					}
					pDest[dwPos++] = (uint16)newval;
				}
				if (dwPos >= dwSize)
				{
					subblk++;
					dwPos = 0;
					dwSize = psubblk[subblk].unpk_size >> 1;
					pDest = (uint16 *)(pBuffer + psubblk[subblk].unpk_pos);
				}
			}
		} else
		/* Data is 8-bit packed */
		{
			MMCMPBITBUFFER bb;
			uint8 * pDest = pBuffer + psubblk->unpk_pos;
			uint32 dwSize = psubblk->unpk_size;
			uint32 dwPos = 0;
			uint32 numbits = pblk->num_bits;
			uint32 subblk = 0, oldval = 0;
			uint8 * ptable = lpMemFile+dwMemPos;

			bb.bitcount = 0;
			bb.bitbuffer = 0;
			bb.pSrc = lpMemFile+dwMemPos+pblk->tt_entries;
			bb.pEnd = lpMemFile+dwMemPos+pblk->pk_size;
			while (subblk < pblk->sub_blk)
			{
				uint32 newval = 0x100;
				uint32 d = GetBits(&bb,numbits+1);

				if (d >= MMCMP8BitCommands[numbits])
				{
					uint32 nFetch = MMCMP8BitFetch[numbits];
					uint32 newbits = GetBits(&bb,nFetch) + ((d - MMCMP8BitCommands[numbits]) << nFetch);
					if (newbits != numbits)
					{
						numbits = newbits & 0x07;
					} else
					{
						if ((d = GetBits(&bb,3)) == 7)
						{
							if (GetBits(&bb,1)) break;
							newval = 0xFF;
						} else
						{
							newval = 0xF8 + d;
						}
					}
				} else
				{
					newval = d;
				}
				if (newval < 0x100)
				{
					int n = ptable[newval];
					if (pblk->flags & mmcmp_DELTA)
					{
						n += oldval;
						oldval = n;
					}
					pDest[dwPos++] = (uint8)n;
				}
				if (dwPos >= dwSize)
				{
					subblk++;
					dwPos = 0;
					dwSize = psubblk[subblk].unpk_size;
					pDest = pBuffer + psubblk[subblk].unpk_pos;
				}
			}
		}
	}
	*ppMemFile = pBuffer;
	*pdwMemLength = dwFileSize;
	return 0;
}


int decrunch_mmcmp (FILE *f, FILE *fo)                          
{                                                          
	struct stat st;
	uint8 *b, *buf;
	uint32 s;
  
	if (fo == NULL) 
		return -1; 

	if (fstat (fileno (f), &st))
		return -1;

	b = buf = malloc (s = st.st_size);
	fread(buf, 1, s, f);
	mmcmp_unpack(&buf, &s);
	fwrite(buf, 1, s, fo);
	free(b);
	free(buf);

	return 0;
}
