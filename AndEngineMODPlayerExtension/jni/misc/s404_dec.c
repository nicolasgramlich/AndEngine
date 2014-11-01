/*
   StoneCracker S404 algorithm data decompression routine
   (c) 2006 Jouni 'Mr.Spiv' Korhonen. The code is in public domain.
  
   from shd:
   Some portability notes. We are using int32_t as a file size, and that fits
   all Amiga file sizes. size_t is of course the right choice.

   Warning: Code is not re-entrant.

   modified for xmp by Claudio Matsuoka, Jan 2010
   (couldn't keep stdint types, some platforms we build on didn't like them)
*/

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include "common.h"

/* #include "compat.h" 

#include "s404_dec.h" */


struct bitstream {
  /* bit buffer for rolling data bit by bit from the compressed file */
  uint32 word;

  /* bits left in the bit buffer */
  int left;

  /* compressed data source */
  uint16 *src;
  uint8 *orgsrc;
};


static int initGetb(struct bitstream *bs, uint8 *src, uint32 src_length)
{
  int eff;

  bs->src = (uint16 *) (src + src_length);
  bs->orgsrc = src;

  bs->left = readmem16b((uint8 *)bs->src); /* bit counter */
  if (bs->left & (~0xf))
    fprintf(stderr, "Workarounded an ancient stc bug\n");
  /* mask off any corrupt bits */
  bs->left &= 0x000f;
  bs->src--;

  /* get the first 16-bits of the compressed stream */
  bs->word = readmem16b((uint8 *)bs->src);
  bs->src--;

  eff = readmem16b((uint8 *)bs->src); /* efficiency */
  bs->src--;

  return eff;
}


/* get nbits from the compressed stream */
static uint16 getb(struct bitstream *bs, int nbits)
{
  bs->word &= 0x0000ffff;

  /* If not enough bits in the bit buffer, get more */
  if (bs->left < nbits) {
    bs->word <<= bs->left;
    assert((bs->word & 0x0000ffffU) == 0);

    /* Check that we don't go out of bounds */
    assert((uint8 *)bs->src >= bs->orgsrc);

    bs->word |= readmem16b((uint8 *)bs->src);
    bs->src--;

    nbits -= bs->left;
    bs->left = 16; /* 16 unused (and some used) bits left in the word */
  }

  /* Shift nbits off the word and return them */
  bs->left -= nbits;
  bs->word <<= nbits;
  return bs->word >> 16;
}


/* Returns bytes still to read.. or < 0 if error. */
static int checkS404File(uint32 *buf, /*size_t len,*/
			 int32 *oLen, int32 *pLen, int32 *sLen )
{
  /*if (len < 16)
    return -1;*/

  if (memcmp(buf, "S404", 4) != 0)
    return -1;

  *sLen = readmem32b((uint8 *)&buf[1]); /* Security length */
  if (*sLen < 0)
    return -1;
  *oLen = readmem32b((uint8 *)&buf[2]); /* Depacked length */
  if (*oLen < 0)
    return -1;
  *pLen = readmem32b((uint8 *)&buf[3]); /* Packed length */
  if (*pLen < 0)
    return -1;

  return 0;
}


static void decompressS404(uint8 *src, uint8 *orgdst,
			   int32 dst_length, int32 src_length)
{
  uint16 w;
  int32 eff;
  int32 n;
  uint8 *dst;
  int32 oLen = dst_length;
  struct bitstream bs;

  dst = orgdst + oLen;

  eff = initGetb(&bs, src, src_length);

  /*printf("_bl: %02X, _bb: %04X, eff: %d\n",_bl,_bb, eff);*/

  while (oLen > 0) {
    w = getb(&bs, 9);

    /*printf("oLen: %d _bl: %02X, _bb: %04X, w: %04X\n",oLen,_bl,_bb,w);*/

    if (w < 0x100) {
      assert(dst > orgdst);
      *--dst = w;
      /*printf("0+[8] -> %02X\n",w);*/
      oLen--;
    } else if (w == 0x13e || w == 0x13f) {
      w <<= 4;
      w |= getb(&bs, 4);

      n = (w & 0x1f) + 14;
      oLen -= n;
      while (n-- > 0) {
        w = getb(&bs, 8);

        /*printf("1+001+1111+[4] -> [8] -> %02X\n",w);*/
	assert(dst > orgdst);
        *--dst = w;
      }
    } else {
      if (w >= 0x180) {
        /* copy 2-3 */
        n = w & 0x40 ? 3 : 2;
        
        if (w & 0x20) {
          /* dist 545 -> */
          w = (w & 0x1f) << (eff - 5);
          w |= getb(&bs, eff - 5);
          w += 544;
          /* printf("1+1+[1]+1+[%d] -> ", eff); */
        } else if (w & 0x30) {
          // dist 1 -> 32
          w = (w & 0x0f) << 1;
          w |= getb(&bs, 1);
          /* printf("1+1+[1]+01+[5] %d %02X %d %04X-> ",n,w, _bl, _bb); */
        } else {
          /* dist 33 -> 544 */
          w = (w & 0x0f) << 5;
          w |= getb(&bs, 5);
          w += 32;
          /* printf("1+1+[1]+00+[9] -> "); */
        }
      } else if (w >= 0x140) {
        /* copy 4-7 */
        n = ((w & 0x30) >> 4) + 4;
        
        if (w & 0x08) {
          /* dist 545 -> */
          w = (w & 0x07) << (eff - 3);
          w |= getb(&bs, eff - 3);
          w += 544;
          /* printf("1+01+[2]+1+[%d] -> ", eff); */
        } else if (w & 0x0c) {
          /* dist 1 -> 32 */
          w = (w & 0x03) << 3;
          w |= getb(&bs, 3);
          /* printf("1+01+[2]+01+[5] -> "); */
        } else {
          /* dist 33 -> 544 */
          w = (w & 0x03) << 7;
          w |= getb(&bs, 7);
          w += 32;
          /* printf("1+01+[2]+00+[9] -> "); */
        }
      } else if (w >= 0x120) {
        /* copy 8-22 */
        n = ((w & 0x1e) >> 1) + 8;
        
        if (w & 0x01) {
          /* dist 545 -> */
          w = getb(&bs, eff);
          w += 544;
          /* printf("1+001+[4]+1+[%d] -> ", eff); */
        } else {
          w = getb(&bs, 6);

          if (w & 0x20) {
            /* dist 1 -> 32 */
            w &= 0x1f;
            /* printf("1+001+[4]+001+[5] -> "); */
          } else {
            /* dist 33 -> 544 */
            w <<= 4;
            w |= getb(&bs, 4);

            w += 32;
            /* printf("1+001+[4]+00+[9] -> "); */
          }
        }
      } else {
        w = (w & 0x1f) << 3;
	w |= getb(&bs, 3);
        n = 23;

        while (w == 0xff) {
          n += w;
          w = getb(&bs, 8);
        }
        n += w;

        w = getb(&bs, 7);

        if (w & 0x40) {
          /* dist 545 -> */
          w = (w & 0x3f) << (eff - 6);
          w |= getb(&bs, eff - 6);

          w += 544;
        } else if (w & 0x20) {
          /* dist 1 -> 32 */
          w &= 0x1f;
          /* printf("1+000+[8]+01+[5] -> "); */
        } else {
          /* dist 33 -> 544; */
          w <<= 4;
	  w |= getb(&bs, 4);

          w += 32;
          /* printf("1+000+[8]+00+[9] -> "); */
        }
      }

      /* printf("<%d,%d>\n",n,w+1); fflush(stdout); */
      oLen -= n;

      while (n-- > 0) {
        /* printf("Copying: %02X\n",dst[w]); */
	dst--;
	assert(dst >= orgdst);
	assert((dst + w + 1) < (orgdst + dst_length));
	*dst = dst[w + 1];
      }
    }
  }
}


int decrunch_s404(uint8 *src, /* size_t s, */ FILE *out)
{
  int32 oLen, sLen, pLen;
  uint8 *dst = NULL;

  if (checkS404File((uint32 *) src, /*s,*/ &oLen, &pLen, &sLen)) {
    fprintf(stderr,"S404 Error: checkS404File() failed..\n");
    goto error;
  }

  if ((dst = malloc(oLen)) == NULL) {
    fprintf(stderr,"S404 Error: malloc(%d) failed..\n", oLen);
    goto error;
  }

  /* src + 16 skips S404 header */
  decompressS404(src + 16, dst, oLen, pLen);

  if (fwrite(dst, oLen, 1, out) == 0) {
      fprintf(stderr,"S404 Error: fwrite() failed..\n");
      goto error;
  }

  free(dst);
  return 0;

 error:
  free(dst);
  return -1;
}


#if 0
struct decruncher decruncher_s404 = {
    .name = "StoneCracker S404",
    .decrunch = decrunch_s404
};
#endif
