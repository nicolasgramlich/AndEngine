/* OpenCP Module Player
 * copyright (c) '94-'05 Niklas Beisert <nbeisert@physik.tu-muenchen.de>
 *
 * IT 2.14/2.15 sample decompression routines.
 *
 * revision history: (please note changes here)
 *  -kb980717    Tammo Hinrichs <kb@nwn.de>
 *    -first release
 *
 * Minor changes for xmp 2.2.0 by Claudio Matsuoka
 *
 * Well.
 * The existance of this file, and the fact that you're just reading this
 * text may upset some people. I know this. I also know about the risks of
 * not only having coded this, but freely releasing it to the public. But
 * well, if this is the end of my life, why not, at least i have done some-
 * thing good to all player coders / game developers with IT fanatic mu-
 * sicians etc. ;)
 *
 * And to make it even worse: A short (?) description of what the routines
 * in this file do.
 *
 * It's all about sample compression. Due to the rather "analog" behaviour
 * of audio streams, it's not always possible to gain high reduction rates
 * with generic compression algorithms. So the idea is to find an algorithm
 * which is specialized for the kind of data we're actually dealing with:
 * mono sample data.
 *
 * in fact, PKZIP etc. is still somewhat better than this algorithm in most
 * cases, but the advantage of this is it's decompression speed which might
 * enable sometimes players or even synthesizer chips to decompress IT
 * samples in real-time. And you can still pack these compressed samples with
 * "normal" algorithms and get better results than these algorothms would
 * ever achieve alone.
 *
 * some assumptions i made (and which also pulse made - and without which it
 * would have been impossible for me to figure out the algorithm) :
 *
 * - it must be possible to find values which are found more often in the
 *   file than others. Thus, it's possible to somehow encode the values
 *   which we come across more often with less bits than the rest.
 * - In general, you can say that low values (considering distance to
 *   the null line) are found more often, but then, compression results
 *   would heavily depend on signal amplitude and DC offsets and such.
 * - But: ;)
 * - higher frequencies have generally lower amplitudes than low ones, just
 *   due to the nature of sound and our ears
 * - so we could somehow filter the signal to decrease the low frequencies'
 *   amplitude, thus resulting in lesser overall amplitude, thus again resul-
 *   ting in better ratios, if we take the above thoughts into consideration.
 * - every signal can be split into a sum of single frequencies, that is a
 *   sum of a(f)*sin(f*t) terms (just believe me if you don't already know).
 * - if we differentiate this sum, we get a sum of (a(f)*f)*cos(f*t). Due to
 *   f being scaled to the nyquist of the sample frequency, it's always
 *   between 0 and 1, and we get just what we want - we decrease the ampli-
 *   tude of the low frequencies (and shift the signal's phase by 90 degrees,
 *   but that's just a side-effect that doesn't have to interest us)
 * - the backwards way is simple integrating over the data and is completely
 *   lossless. good.
 * - so how to differentiate or integrate a sample stream? the solution is
 *   simple: we simply use deltas from one sample to the next and have the
 *   perfectly numerically differentiated curve. When we decompress, we
 *   just add the value we get to the last one and thus restore the original
 *   signal.
 * - then, we assume that the "-1"st sample value is always 0 to avoid nasty
 *   DC offsets when integrating.
 *
 * ok. now we have a sample stream which definitely contains more low than
 * high values. How do we compress it now?
 *
 * Pulse had chosen a quite unusual, but effective solution: He encodes the
 * values with a specific "bit width" and places markers between the values
 * which indicate if this width would change. He implemented three different
 * methods for that, depending on the bit width we actually have (i'll write
 * it down for 8 bit samples, values which change for 16bit ones are in these
 * brackets [] ;):
 *
 * method 1: 1 to 6 bits
 * * there are two possibilities (example uses a width of 6)
 *   - 100000 (a one with (width-1) zeroes ;) :
 *     the next 3 [4] bits are read, incremented and used as new width...
 *     and as it would be completely useless to switch to the same bit
 *     width again, any value equal or greater the actual width is
 *     incremented, thus resulting in a range from 1-9 [1-17] bits (which
 *     we definitely need).
 *   - any other value is expanded to a signed byte [word], integrated
 *     and stored.
 * method 2: 7 to 8 [16] bits
 * * again two possibilities (this time using a width of eg. 8 bits)
 *   - 01111100 to 10000011 [01111000 to 10000111] :
 *     this value will be subtracted by 01111011 [01110111], thus resulting
 *     again in a 1-8 [1-16] range which will be expanded to 1-9 [1-17] in
 *     the same manner as above
 *   - any other value is again expanded (if necessary), integrated and
 *     stored
 * method 3: 9 [17] bits
 * * this time it depends on the highest bit:
 *   - if 0, the last 8 [16] bits will be integrated and stored
 *   - if 1, the last 8 [16] bits (+1) will be used as new bit width.
 * any other width isnt supposed to exist and will result in a premature
 * exit of the decompressor.
 *
 * Few annotations:
 * - The compressed data is processed in blocks of 0x8000 bytes. I dont
 *   know the reason of this (it's definitely NOT better concerning compres-
 *   sion ratio), i just think that it has got something to do with Pulse's
 *   EMS memory handling or such. Anyway, this was really nasty to find
 *   out ;)
 * - The starting bit width is 9 [17]
 * - IT2.15 compression simply doubles the differentiation/integration
 *   of the signal, thus eliminating low frequencies some more and turning
 *   the signal phase to 180 degrees instead of 90 degrees which can eliminate
 *   some signal peaks here and there - all resulting in a somewhat better
 *   ratio.
 *
 * ok, but now lets start... but think before you easily somehow misuse
 * this code, the algorithm is (C) Jeffrey Lim aka Pulse... and my only
 * intention is to make IT's file format more open to the Tracker Community
 * and especially the rest of the scene. Trackers ALWAYS were open standards,
 * which everyone was able (and WELCOME) to adopt, and I don't think this
 * should change. There are enough other things in the computer world
 * which did, let's just not be mainstream, but open-minded. Thanks.
 *
 *                    Tammo Hinrichs [ KB / T.O.M / PuRGE / Smash Designs ]
 *
 * ----------------------------------------------------------------------
 *  includes...
 * ----------------------------------------------------------------------
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"

/* ----------------------------------------------------------------------
 *  some helpful typedefs which are lame, but so am I :)
 * ----------------------------------------------------------------------
 */

typedef uint8 byte;
typedef int8 sbyte;
typedef uint16 word;
typedef int16 sword;
typedef uint32 dword;

/* ----------------------------------------------------------------------
 *  auxiliary routines to get bits from the input stream
 * ----------------------------------------------------------------------
 */

static uint8 *sourcebuffer = NULL;
static uint8 *ibuf = NULL;	/* actual reading position */
static uint32 bitlen;
static uint8 bitnum;

static inline uint32 readbits(uint8 n)
{
	uint32 retval = 0;
	int offset = 0;
	while (n) {
		int m = n;

		if (!bitlen) {
			fprintf(stderr, "readbits: ran out of buffer\n");
			return 0;
		}

		if (m > bitnum)
			m = bitnum;
		retval |= (*ibuf & ((1L << m) - 1)) << offset;
		*ibuf >>= m;
		n -= m;
		offset += m;
		if (!(bitnum -= m)) {
			bitlen--;
			ibuf++;
			bitnum = 8;
		}
	}
	return retval;
}

static int readblock(FILE *f)
{				/* gets block of compressed data from file */
	uint16 size;

	size = read16l(f);

	if (!size)
		return 0;
	if (!(sourcebuffer = malloc(size)))
		return 0;

	if (fread(sourcebuffer, size, 1, f) != 1) {
		free(sourcebuffer);
		sourcebuffer = NULL;	/* Just looks better to have it present */
		return 0;
	}
	ibuf = sourcebuffer;
	bitnum = 8;
	bitlen = size;
	return 1;
}

static int freeblock(void)
{				/* frees that block again */
	if (sourcebuffer)
		free(sourcebuffer);
	sourcebuffer = NULL;
	return 1;
}

/* ----------------------------------------------------------------------
 *  decompression routines
 * ----------------------------------------------------------------------
 *
 * decompresses 8-bit sample (params : file, outbuffer, lenght of
 *                                     uncompressed sample, IT2.15
 *                                     compression flag
 *                            returns: status                     )
 */

int itsex_decompress8(FILE *module, void *dst, int len, char it215)
{
	sbyte *destbuf;		/* the destination buffer which will be returned */

	word blklen;		/* length of compressed data block in samples */
	word blkpos;		/* position in block */
	byte width;		/* actual "bit width" */
	word value;		/* value read from file to be processed */
	sbyte d1, d2;		/* integrator buffers (d2 for it2.15) */
	sbyte *destpos;

	destbuf = (sbyte *) dst;
	if (!destbuf)
		return 0;

	memset(destbuf, 0, len);
	destpos = destbuf;	/* position in output buffer */

	/* now unpack data till the dest buffer is full */
	while (len) {
		/* read a new block of compressed data and reset variables */

		if (!readblock(module))
			return 0;
		blklen = (len < 0x8000) ? len : 0x8000;
		blkpos = 0;

		width = 9;	/* start with width of 9 bits */
		d1 = d2 = 0;	/* reset integrator buffers */

		/* now uncompress the data block */
		while (blkpos < blklen) {
			sbyte v;

			value = readbits(width);	/* read bits */

			if (width < 7) {	/* method 1 (1-6 bits) */
				if (value == (1 << (width - 1))) {	/* check for "100..." */
					value = readbits(3) + 1;	/* yes -> read new width; */
					width = (value < width) ? value : value + 1;	/* and expand it */
					continue;	/* ... next value */
				}
			} else if (width < 9) {	/* method 2 (7-8 bits) */
				byte border = (0xFF >> (9 - width)) - 4;	/* lower border for width chg */

				if (value > border && value <= (border + 8)) {
					value -= border;	/* convert width to 1-8 */
					width = (value < width) ? value : value + 1;	/* and expand it */
					continue;	/* ... next value */
				}
			} else if (width == 9) {	/* method 3 (9 bits) */
				if (value & 0x100) {	/* bit 8 set? */
					width = (value + 1) & 0xff;	/* new width... */
					continue;	/* ... and next value */
				}
			} else {	/* illegal width, abort */
				freeblock();
				return 0;
			}

			/* now expand value to signed byte */
			/*      sbyte v;  // sample value */
			if (width < 8) {
				byte shift = 8 - width;
				v = (value << shift);
				v >>= shift;
			} else
				v = (sbyte) value;

			/* integrate upon the sample values */
			d1 += v;
			d2 += d1;

			/* ... and store it into the buffer */
			*(destpos++) = it215 ? d2 : d1;
			blkpos++;

		}

		/* now subtract block lenght from total length and go on */
		freeblock();
		len -= blklen;
	}

	return 1;
}

/* decompresses 16-bit sample (params : file, outbuffer, lenght of
 *                                      uncompressed sample, IT2.15
 *                                      compression flag
 *                             returns: status                     )
 */
int itsex_decompress16(FILE *module, void *dst, int len, char it215)
{
	sword *destbuf;		/* the destination buffer which will be returned */

	word blklen;		/* length of compressed data block in samples */
	word blkpos;		/* position in block */
	byte width;		/* actual "bit width" */
	dword value;		/* value read from file to be processed */
	sword d1, d2;		/* integrator buffers (d2 for it2.15) */
	sword *destpos;

	destbuf = (sword *)dst;
	if (!destbuf)
		return 0;

	memset(destbuf, 0, len << 1);
	destpos = destbuf;	/* position in output buffer */

	/* now unpack data till the dest buffer is full */
	while (len) {

		/* read a new block of compressed data and reset variables */

		if (!readblock(module))
			return 0;
		blklen = (len < 0x4000) ? len : 0x4000;	/* 0x4000 samples => 0x8000 bytes again */
		blkpos = 0;

		width = 17;	/* start with width of 17 bits */
		d1 = d2 = 0;	/* reset integrator buffers */

		/* now uncompress the data block */
		while (blkpos < blklen) {
			sword v;

			value = readbits(width);	/* read bits */

			if (width < 7) {	/* method 1 (1-6 bits) */
				if (value == (1 << (width - 1))) {	/* check for "100..." */
					value = readbits(4) + 1;	/* yes -> read new width; */
					width = (value < width) ? value : value + 1;	/* and expand it */
					continue;	/* ... next value */
				}
			} else if (width < 17) {	/* method 2 (7-16 bits) */
				word border = (0xFFFF >> (17 - width)) - 8;	/* lower border for width chg */

				if (value > border && value <= (border + 16)) {
					value -= border;	/* convert width to 1-8 */
					width = (value < width) ? value : value + 1;	/* and expand it */
					continue;	/* ... next value */
				}
			} else if (width == 17) {	/* method 3 (17 bits) */
				if (value & 0x10000) {	/* bit 16 set? */
					width = (value + 1) & 0xff;	/* new width... */
					continue;	/* ... and next value */
				}
			} else {	/* illegal width, abort */
				freeblock();
				return 0;
			}

			/* now expand value to signed word */
			/* sword v; // sample value */
			if (width < 16) {
				byte shift = 16 - width;
				v = (value << shift);
				v >>= shift;
			} else
				v = (sword) value;

			/* integrate upon the sample values */
			d1 += v;
			d2 += d1;

			/* ... and store it into the buffer */
			*(destpos++) = it215 ? d2 : d1;
			blkpos++;
		}

		/* now subtract block lenght from total length and go on */
		freeblock();
		len -= blklen;
	}

	return 1;
}
