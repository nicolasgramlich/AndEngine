/* ppcrack 0.1 - decrypts PowerPacker encrypted data files with brute force
 * by Stuart Caie <kyzer@4u.net>, this software is in the Public Domain
 *
 * The whole keyspace is scanned, unless you supply the -key argument, where
 * that key (in hexadecimal) to key FFFFFFFF is scanned.
 *
 * Anything which decrypts then decrunches to valid data is saved to disk
 * as <original filename>.<decryption key>
 *
 * As a bonus, if any file is a PowerPacker data file, but not encrypted,
 * it will be decrunched anyway, and saved as <original filename>.decrunched
 *
 * - changed to work with UADE (mld) 
 *   Thanks to Kyzer for help and support.
 */

/* Code from Heikki Orsila's amigadepack 0.02 to replace previous
 * PowerPack depacker with license issues.
 *
 * You'll probably want to use ppcrack stand-alone to crack encrypted
 * powerpack files once instead of using brute force at each replay.
 *
 * Modified for xmp by Claudio Matsuoka, 08/2007
 * - merged mld's checks from the old depack sources. Original credits:
 *   - corrupt file and data detection
 *     (thanks to Don Adan and Dirk Stoecker for help and infos)
 *   - implemeted "efficiency" checks
 *   - further detection based on code by Georg Hoermann
 */
 
#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>

#include "common.h"

#define WANT_PP2X_DECRYPTING

#define val(p) ((p)[0]<<16 | (p)[1] << 8 | (p)[2])


int savefile(FILE *fo, void *mem, size_t length)
{
  int ok = fo && (fwrite(mem, 1, length, fo) == length);
  return ok;
}


static uint32 key_start = 0;


static
inline void ppDecryptCopy(uint8 *src, uint8 *dest, uint32 len, uint32 key)
{
  uint8 a = (key>>24) & 0xFF;
  uint8 b = (key>>16) & 0xFF;
  uint8 c = (key>> 8) & 0xFF;
  uint8 d = (key    ) & 0xFF;

  len = (len + 3) >> 2;
  while (len--) {
    *dest++ = *src++ ^ a;
    *dest++ = *src++ ^ b;
    *dest++ = *src++ ^ c;
    *dest++ = *src++ ^ d;
  }
}

#define PP_READ_BITS(nbits, var) do {                          \
  bit_cnt = (nbits);                                           \
  while (bits_left < bit_cnt) {                                \
    if (buf_src < src) return 0; /* out of source bits */      \
    bit_buffer |= (*--buf_src << bits_left);                   \
    bits_left += 8;                                            \
  }                                                            \
  (var) = 0;                                                   \
  bits_left -= bit_cnt;                                        \
  while (bit_cnt--) {                                          \
    (var) = ((var) << 1) | (bit_buffer & 1);                   \
    bit_buffer >>= 1;                                          \
  }                                                            \
} while(0)

#define PP_BYTE_OUT(byte) do {                                 \
  if (out <= dest) return 0; /* output overflow */              \
  *--out = (byte);                                             \
  written++;                                                   \
} while (0)

int ppDecrunch(uint8 *src, uint8 *dest, uint8 *offset_lens,
               uint32 src_len, uint32 dest_len, uint8 skip_bits)
{
  uint8 *buf_src, *out, *dest_end, bits_left = 0, bit_cnt;
  uint32 bit_buffer = 0, x, todo, offbits, offset, written=0;

  if (src == NULL || dest == NULL || offset_lens == NULL) return 0;

  /* set up input and output pointers */
  buf_src = src + src_len;
  out = dest_end = dest + dest_len;

  /* skip the first few bits */
  PP_READ_BITS(skip_bits, x);

  /* while there are input bits left */
  while (written < dest_len) {
    PP_READ_BITS(1, x);
    if (x == 0) {
      /* 1bit==0: literal, then match. 1bit==1: just match */
      todo = 1; do { PP_READ_BITS(2, x); todo += x; } while (x == 3);
      while (todo--) { PP_READ_BITS(8, x); PP_BYTE_OUT(x); }

      /* should we end decoding on a literal, break out of the main loop */
      if (written == dest_len) break;
    }

    /* match: read 2 bits for initial offset bitlength / match length */
    PP_READ_BITS(2, x);
    offbits = offset_lens[x];
    todo = x+2;
    if (x == 3) {
      PP_READ_BITS(1, x);
      if (x==0) offbits = 7;
      PP_READ_BITS(offbits, offset);
      do { PP_READ_BITS(3, x); todo += x; } while (x == 7);
    }
    else {
      PP_READ_BITS(offbits, offset);
    }
    if ((out + offset) >= dest_end) return 0; /* match overflow */
    while (todo--) { x = out[offset]; PP_BYTE_OUT(x); }
  }

  /* all output bytes written without error */
  return 1;
  /* return (src == buf_src) ? 1 : 0; */
}                     

#ifdef WANT_PP2X_DECRYPTING
/* this pretends to decrunch a data stream. If it wasn't decrypted
 * exactly right, it will access match offsets that don't exist, or
 * request match lengths that there isn't enough data for, or will
 * underrun or overrun the theoretical output buffer
 */
static
inline int ppValidate(uint8 *src, uint8 *offset_lens,
                      uint32 src_len, uint32 dest_len, uint8 skip_bits)
{
  uint8 *buf_src, bits_left = 0, bit_cnt;
  uint32 bit_buffer = 0, x, todo, offbits, offset, written=0;

  if (src == NULL || offset_lens == NULL) return 0;

  /* set up input pointer */
  buf_src = src + src_len;

  /* skip the first few bits */
  PP_READ_BITS(skip_bits, x);

  /* while there are input bits left */
  while (written < dest_len) {
    PP_READ_BITS(1, x);
    if (x == 0) {
      /* 1bit==0: literal, then match. 1bit==1: just match */
      todo = 1; do { PP_READ_BITS(2, x); todo += x; } while (x == 3);
      written += todo; if (written > dest_len) return 0;
      while (todo--) PP_READ_BITS(8, x);

      /* should we end decoding on a literal, break out of the main loop */
      if (written == dest_len) break;
    }

    /* match: read 2 bits for initial offset bitlength / match length */
    PP_READ_BITS(2, x);
    offbits = offset_lens[x];
    todo = x+2;
    if (x == 3) {
      PP_READ_BITS(1, x);
      if (x==0) offbits = 7;
      PP_READ_BITS(offbits, offset);
      do { PP_READ_BITS(3, x); todo += x; } while (x == 7);
    }
    else {
      PP_READ_BITS(offbits, offset);
    }
    if (offset >= written) return 0; /* match overflow */
    written += todo; if (written > dest_len) return 0;
  }

  /* all output bytes written without error */
  return 1;
}                     
#endif

static int ppcrack(FILE *fo, uint8 *data, uint32 len)
{
  /* PP FORMAT:
   *      1 longword identifier           'PP20' or 'PX20'
   *     [1 word checksum (if 'PX20')     $ssss]
   *      1 longword efficiency           $eeeeeeee
   *      X longwords crunched file       $cccccccc,$cccccccc,...
   *      1 longword decrunch info        'decrlen' << 8 | '8 bits other info'
   */
  int success=0;
  uint8 *output, crypted;
  uint32 outlen;

  if (len < 16) {
    fprintf(stderr, "File is too short to be a PP file (%u bytes)\n", len);
    return -1;
  }

  if (data[0]=='P' && data[1]=='P' && data[2]=='2' && data[3]=='0') {
    if (len & 0x03) {
      fprintf(stderr, "File length is not a multiple of 4\n");
      return -1;
    }
    crypted = 0;
  }
  else if (data[0]=='P' && data[1]=='X' && data[2]=='2' && data[3]=='0') {
    if ((len-2) & 0x03) {
      fprintf(stderr, "(file length - 2) is not a multiple of 4\n");
      return -1;
    }
    crypted = 1;
  }
  else {
    fprintf(stderr, "File does not have the PP signature\n");
    return -1;
  }

  outlen = (data[len-4]<<16) | (data[len-3]<<8) | data[len-2];

  /* fprintf(stderr, "decrunched length = %u bytes\n", outlen); */

  output = (uint8 *) malloc(outlen);
  if (output == NULL) {
    fprintf(stderr, "out of memory!\n");
    return -1;
  }

  if (crypted == 0) {
    /*fprintf(stderr, "not encrypted, decrunching anyway\n"); */
    if (ppDecrunch(&data[8], output, &data[4], len-12, outlen, data[len-1])) {
      /* fprintf(stderr, "Decrunch successful! "); */
      savefile(fo, (void *) output, outlen);
    } else {
      success=-1;
    } 
  } else {

#ifdef WANT_PP2X_DECRYPTING
    /* brute-force calculate the key */

    uint32 key = key_start;

    /* shortcut to halve keyspace:
     * PowerPacker alternates between two operations - literal and match.
     * The FIRST operation must be literal, as there's no data been output
     * to match yet, so the first BIT in the compressed stream must be set
     * to 0. The '8 bits other info' is actually the number of bits unused
     * in the first longword. We must ignore these.
     *
     * So we know which bit is the first one in the compressed stream, and
     * that is matched a bit in the decryption XOR key.
     *
     * We know the encrypted value of the first bit, and we know it must
     * actually be 0 when decrypted. So, if the value is 1, then that bit
     * of the decryption key must be 1, to invert that bit to a 0. If the
     * value is 0, then that bit of the decryption key must be 0, to leave
     * that bit set at 0.
     *
     * Given the knowledge of exactly one of the bits in the keys, we can
     * reject all keys that do not have the appropriate value for this bit.
     */
    uint32 drop_mask = 1 << data[len-1];
    uint32 drop_value = ( (data[len-8]<<24) | (data[len-7]<<16)
                       | (data[len-6]<<8)  |  data[len-5] ) & drop_mask;

    uint8 *temp = (uint8 *) malloc(len-14);


    
    fprintf(stderr, "\nEncrypted. Hang on, while trying to find the right key...\n");
    if (temp == NULL) {
      fprintf(stderr, "out of memory!\n");
      return -1;
    }

    do {
      if ((key & 0xFFF) == 0) {
        fprintf(stderr, "key %08x\r", key);
        fflush(stdout);
      }

      if ((key & drop_mask) != drop_value) continue;

      /* decrypt with this key */
      ppDecryptCopy(&data[10], temp, len-14, key);

      if (ppValidate(temp, &data[6], len-14, outlen, data[len-1])) {
        fprintf(stderr, "key %08x success!\n", key);
	ppDecrunch(temp, output, &data[6], len-14, outlen, data[len-1]);

	/* key_match = key */
        /* sprintf(output_name, "%s.%08x", name, key); */
	
        savefile(fo, output, outlen);
	break;
      }
    } while (key++ != 0xFFFFFFFF);
    free(temp);
    fprintf(stderr, "All keys done!\n");
#else
     fprintf(stderr, "\nWarning: support for encrypted powerpacker files not compiled in.\n");
     success=-1;
#endif
    }
  free((void *) output);
return success;
}


static int ppdepack(uint8 *src, size_t s, FILE *fo)
{
  int success;
  key_start = 0;
  success = ppcrack(fo, (uint8 *)src, s);
  return success;
}


int decrunch_pp(FILE *f, FILE *fo)
{
    uint8 *packed /*, *unpacked */;
    int plen, unplen;
    struct stat st;

    if (fo == NULL)
	return -1;

    fstat(fileno(f), &st);
    plen = st.st_size;
    //counter = 0;

    /* Amiga longwords are only on even addresses.
     * The pp20 data format has the length stored in a longword
     * after the packed data, so I guess a file that is not even
     * is probl not a valid pp20 file. Thanks for Don Adan for
     * reminding me on this! - mld
     */

    if ((plen != (plen / 2) * 2)) {    
	 fprintf(stderr, "filesize not even\n");
	 return -1 ;
    }

    packed = malloc(plen);
    if (!packed) {
	 fprintf(stderr, "can't allocate memory for packed data\n");
	 return -1;
    }

    fread (packed, plen, 1, f);

    /* Hmmh... original pp20 only support efficiency from 9 9 9 9 up to 9 10 12 13, afaik
     * but the xfd detection code says this... *sigh*
     *
     * move.l 4(a0),d0
     * cmp.b #9,d0
     * blo.b .Exit
     * and.l #$f0f0f0f0,d0
     * bne.s .Exit
     */	 

    if (((packed[4] < 9) || (packed[5] < 9) || (packed[6] < 9) || (packed[7] < 9))) {
	 fprintf(stderr, "invalid efficiency\n");
	 return -1;
    }


    if (((((val (packed +4) ) * 256 ) + packed[7] ) & 0xf0f0f0f0) != 0 ) {
	 fprintf(stderr, "invalid efficiency(?)\n");
	 return -1;
    }

    unplen = val (packed + plen - 4);
    if (!unplen) {
	  fprintf(stderr, "not a powerpacked file\n");
	  return -1;
    }
    
    if (ppdepack (packed, plen, fo) == -1) {
	 fprintf(stderr, "error while decrunching data...");
	 return -1;
    }
     
    free (packed);

    return 0;
}
