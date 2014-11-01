/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdlib.h>
#include <string.h>

#include "common.h"
#include "convert.h"
#include "driver.h"


/*
 * From the Audio File Formats (version 2.5)
 * Submitted-by: Guido van Rossum <guido@cwi.nl>
 * Last-modified: 27-Aug-1992
 *
 * The Acorn Archimedes uses a variation on U-LAW with the bit order
 * reversed and the sign bit in bit 0.  Being a 'minority' architecture,
 * Arc owners are quite adept at converting sound/image formats from
 * other machines, and it is unlikely that you'll ever encounter sound in
 * one of the Arc's own formats (there are several).
 */
static int8 vdic_table[128] = {
	/*   0 */	  0,   0,   0,   0,   0,   0,   0,   0,
	/*   8 */	  0,   0,   0,   0,   0,   0,   0,   0,
	/*  16 */	  0,   0,   0,   0,   0,   0,   0,   0,
	/*  24 */	  1,   1,   1,   1,   1,   1,   1,   1,
	/*  32 */	  1,   1,   1,   1,   2,   2,   2,   2,
	/*  40 */	  2,   2,   2,   2,   3,   3,   3,   3,
	/*  48 */	  3,   3,   4,   4,   4,   4,   5,   5,
	/*  56 */	  5,   5,   6,   6,   6,   6,   7,   7,
	/*  64 */	  7,   8,   8,   9,   9,  10,  10,  11,
	/*  72 */	 11,  12,  12,  13,  13,  14,  14,  15,
	/*  80 */	 15,  16,  17,  18,  19,  20,  21,  22,
	/*  88 */	 23,  24,  25,  26,  27,  28,  29,  30,
	/*  96 */	 31,  33,  34,  36,  38,  40,  42,  44,
	/* 104 */	 46,  48,  50,  52,  54,  56,  58,  60,
	/* 112 */	 62,  65,  68,  72,  77,  80,  84,  91,
	/* 120 */	 95,  98, 103, 109, 114, 120, 126, 127
};



/* Convert differential to absolute sample data */
void xmp_cvt_diff2abs (int l, int r, char *p)
{
    uint16* w = (uint16*) p;
    uint16 abs = 0;

    if (r) {
	for (l >>= 1; l--;) {
	    abs = *w + abs;
	    *w++ = abs;
	}
    } else {
	for (; l--;) {
	    abs = *p + abs;
	    *p++ = (uint8) abs;
	}
    }
}


/* Convert signed to unsigned sample data */
void xmp_cvt_sig2uns(int l, int r, char *p)
{
    uint16* w = (uint16*)p;

    if (r) {
	for (l >>= 1; l--; w++)
	    *w += 0x8000;
    } else {
	for (; l--; p++)
	    *p += (char)0x80;		/* cast needed by MSVC++ */
    }
}


/* Convert little-endian 16 bit samples to big-endian */
void xmp_cvt_sex(int l, char *p)
{
    uint8 b;

    for (l >>= 1; l--;) {
	b = p[0];
	p[0] = p[1];
	p[1] = b;
	p += 2;
    }
}


/* Downmix stereo samples to mono */
void xmp_cvt_stdownmix(int l, int r, char *p)
{
    int16 *b = (int16 *)p;
    int i;

    if (r) {
	l /= 4;
	for (i = 0; i < l; i++)
	    b[i] = (b[i * 2] + b[i * 2 + 1]) / 2;
    } else {
	l /= 2;
	for (i = 0; i < l; i++)
	    p[i] = (p[i * 2] + p[i * 2 + 1]) / 2;
    }
}


/* Convert 7 bit samples to 8 bit */
void xmp_cvt_2xsmp(int l, char *p)
{
    for (; l--; *p++ <<= 1);
}


/* Convert Archimedes VIDC samples to linear */
void xmp_cvt_vidc(int l, char *p)
{
	int i;
	uint8 x;

	for (i = 0; i < l; i++) {
		x = p[i];
		p[i] = vdic_table[x >> 1];
		if (x & 0x01)
			p[i] *= -1;
	}
}


/* Convert all patches with 16 bit samples to 8 bit (for crunching) */
void xmp_cvt_to8bit(struct xmp_context *ctx)
{
    struct xmp_driver_context *d = &ctx->d;
    int l, smp;
    int8 *p8; 
    int16 *p16;
    struct patch_info *patch;

    for (smp = XMP_MAXPAT; smp--;) {
	patch = d->patch_array[smp];

	if (!(patch && (patch->mode & WAVE_16_BITS) &&
	    (patch->len != XMP_PATCH_FM)))
	    continue;

	patch->mode &= ~WAVE_16_BITS;

	p8 = (int8 *)patch->data;
	p16 = (int16 *)patch->data;

	patch->loop_end >>= 1;
	patch->loop_start >>= 1;
	for (l = (patch->len >>= 1); l--; *p8++ = (int8) *p16++ >> 8);

	d->patch_array[smp] = realloc(patch,
				sizeof (struct patch_info) + patch->len);
    }
}


/* Convert all patches with 8 bit samples to 16 bit (for AWE) */
void xmp_cvt_to16bit(struct xmp_context *ctx)
{
    struct xmp_driver_context *d = &ctx->d;
    int l, smp;
    int8 *p8;
    int16 *p16;
    struct patch_info *patch;

    for (smp = XMP_MAXPAT; smp--;) {
	patch = d->patch_array[smp];
	if ((!patch) || patch->mode & WAVE_16_BITS ||
	    (patch->len == XMP_PATCH_FM))
	    continue;

	patch->mode |= WAVE_16_BITS;

	l = patch->len;
	patch = realloc(patch,
	    sizeof (struct patch_info) + (patch->len <<= 1));
	patch->loop_start <<= 1;
	patch->loop_end <<= 1;

	p8 = (int8 *)patch->data;
	p16= (int16 *)patch->data;
	for (p8 += l, p16 += l; l--; *(--p16) = (int16) *(--p8) << 8);

	d->patch_array[smp] = patch;
    }
}


/* Hipolito's routine to minimize loop clicking */
void xmp_cvt_anticlick (struct patch_info *patch)
{
    if (patch->len == XMP_PATCH_FM)
	return;

    /* Ok, I need this messy code to anticlick in AWE :-( */

    if ((patch->mode & WAVE_LOOPING) && !(patch->mode & WAVE_BIDIR_LOOP)) {
	if (patch->mode & WAVE_16_BITS) {
	    patch->data[patch->loop_end++] = patch->data[patch->loop_start++];
	    patch->data[patch->loop_end++] = patch->data[patch->loop_start++];
	    patch->data[patch->loop_end] = patch->data[patch->loop_start];
	    patch->data[patch->loop_end + 1] = patch->data[patch->loop_start + 1];
	    patch->len += 4;
	} else {
	    patch->data[patch->loop_end++] = patch->data[patch->loop_start++];
	    patch->data[patch->loop_end] = patch->data[patch->loop_start];
	    patch->len += 2;
	}
    }
    else {
	if (patch->mode & WAVE_16_BITS) {
	    patch->data[patch->len] = patch->data[patch->len - 2];
	    patch->data[patch->len + 1] = patch->data[patch->len - 1];
	    patch->len += 2;
	} else {
	    patch->data[patch->len] = patch->data[patch->len - 1];
	    patch->len++;
	}
    }
}


/* Unroll bidirectional loops for AWE */
void xmp_cvt_bid2und(struct xmp_context *ctx)
{
    struct xmp_driver_context *d = &ctx->d;
    int8 *s8;
    int16 *s16;
    int r, l, le, smp;
    struct patch_info *patch;

    for (smp = XMP_MAXPAT; smp--;) {
	patch = d->patch_array[smp];
	if (!(patch && (patch->mode & WAVE_BIDIR_LOOP) &&
	    (patch->len != XMP_PATCH_FM)))
	    continue;

	patch->mode &= ~WAVE_BIDIR_LOOP;

	r = !!(patch->mode & WAVE_16_BITS);
	le = patch->loop_end >> r;
	l = patch->len >> r;
	le = l = l > le ? le : l - 1;
	l -= patch->loop_start >> r;
	patch->len = patch->loop_end = (--le + l) << r;
	patch = realloc (patch,
	    sizeof (struct patch_info) + patch->len + sizeof (int));

	s8 = (int8 *)patch->data;
	s16 = (int16 *)patch->data;
	if (r)
	    for (s16 += le; l--; *(s16 + l) = *(s16 - l));
	else 
	    for (s8 += le; l--; *(s8 + l) = *(s8 - l));

	xmp_cvt_anticlick(patch);
	d->patch_array[smp] = patch;
    }
}


/* Convert HSC OPL2 instrument data to SBI instrument data */
void xmp_cvt_hsc2sbi(char *a)
{
    char b[11];
    int i;

    for (i = 0; i < 10; i += 2) {
	uint8 x;
 	x = a[i];
	a[i] = a[i + 1];
	a[i + 1] = x;
    }

    memcpy (b, a, 11);
    a[8] = b[10];
    a[10] = b[9];
    a[9] = b[8];
}
