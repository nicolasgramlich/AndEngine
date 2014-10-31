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

#include <stdio.h>
#include <string.h>
#include "common.h"
#include "period.h"

#include <math.h>

/* Amiga periods */
static int period_amiga[] = {
   /*  0       1       2       3       4       5       6       7   */
    0x1c56, 0x1c22, 0x1bee, 0x1bbb, 0x1b87, 0x1b55, 0x1b22, 0x1af0,  /* B  */
    0x1abf, 0x1a8e, 0x1a5d, 0x1a2c, 0x19fc, 0x19cc, 0x199c, 0x196d,  /* C  */
    0x193e, 0x1910, 0x18e2, 0x18b4, 0x1886, 0x1859, 0x182c, 0x1800,  /* C# */
    0x17d4, 0x17a8, 0x177c, 0x1751, 0x1726, 0x16fb, 0x16d1, 0x16a7,  /* D  */
    0x167d, 0x1654, 0x162b, 0x1602, 0x15d9, 0x15b1, 0x1589, 0x1562,  /* D# */
    0x153a, 0x1513, 0x14ec, 0x14c6, 0x149f, 0x1479, 0x1454, 0x142e,  /* E  */
    0x1409, 0x13e4, 0x13c0, 0x139b, 0x1377, 0x1353, 0x1330, 0x130c,  /* F  */
    0x12e9, 0x12c6, 0x12a4, 0x1282, 0x125f, 0x123e, 0x121c, 0x11fb,  /* F# */
    0x11da, 0x11b9, 0x1198, 0x1178, 0x1157, 0x1137, 0x1118, 0x10f8,  /* G  */
    0x10d9, 0x10ba, 0x109b, 0x107d, 0x105e, 0x1040, 0x1022, 0x1004,  /* G# */
    0x0fe7, 0x0fca, 0x0fad, 0x0f90, 0x0f73, 0x0f57, 0x0f3a, 0x0f1e,  /* A  */
    0x0f02, 0x0ee7, 0x0ecb, 0x0eb0, 0x0e95, 0x0e7a, 0x0e5f, 0x0e45,  /* A# */
    0x0e2b, 0x0e11, 0x0df7, 0x0ddd, 0x0dc3, 0x0daa, 0x0d91, 0x0d78,  /* B  */
};


/* Get period from note */
inline double note_to_period(int n, int f, int type)
{
    double d = (double)n + (double)f / 128;

    return type ?
	(120.0 - d) * 16 :			/* Linear */
        6847.0 / pow(2, d / 12);		/* Amiga */
}


/* For the software mixer */
int note_to_period_mix(int n, int b)
{
    double d = (double)n + (double)b / 100;
    return (int)(16.0 * 6847.0 / pow(2, d / 12));
}


/* Get note from period using the Amiga frequency table */
/* This function is used only by the MOD loader */
int period_to_note(int p)
{
    int n, f, *t = period_amiga + MAX_NOTE;

    if (!p)
	return 0;
    for (n = NOTE_Bb0; p <= (MAX_PERIOD / 2); n += 12, p <<= 1);
    for (; p > *t; t -= 8, n--);
    for (f = 7; f && (*t > p); t++, f--);
    return n - (f >> 2);
}


/* Get pitchbend from base note and period */
int period_to_bend(double p, int n, int limit, int gliss, int type)
{
    int b;
    double d;

    if (!n)
	return 0;

    if (limit) {				/* Force Amiga limits */
	if (p > AMIGA_LIMIT_LOWER)
	    p = AMIGA_LIMIT_LOWER;
	if (p < AMIGA_LIMIT_UPPER)
	    p = AMIGA_LIMIT_UPPER;
    }

    if (type) {
	/* b = (100 * (((120 - n) << 4) - p)) >> 4 + f * 100 / 128; */
	b = 100 * (8 * (((120 - n) << 4) - p)) / 128;	/* Linear */
	return gliss ? b / 100 * 100 : b;
    }

    if (p < MIN_PERIOD_A)
	p = MIN_PERIOD_A;

    d = note_to_period(n, 0, 0);
    b = 100.0 * ((1536.0 * log(d / p) / M_LN2)) / 128;

    return gliss ? b / 100 * 100 : b;	/* Amiga */
}


/* Convert finetune = 1200 * log2(C2SPD/8363))
 *
 *      c = (1200.0 * log(c2spd) - 1200.0 * log(c4_rate)) / M_LN2;
 *      xpo = c/100;
 *      fin = 128 * (c%100) / 100;
 */
void c2spd_to_note(int c2spd, int *n, int *f)
{
    int c;

    if (c2spd == 0) {
	*n = *f = 0;
	return;
    }

    c = (int)(1536.0 * log((double)c2spd / 8363) / M_LN2);
    *n = c / 128;
    *f = c % 128;
}
