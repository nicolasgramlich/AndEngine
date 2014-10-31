/* Extended Module Player
 * Copyright (C) 1998-2010 Claudio Matsuoka and Hipolito Carraro Jr.
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See docs/COPYING
 * for more information.
 */

/* Date: Fri, 25 Dec 1998 03:49:17 +0200 (EET)
 * From: janne <sniff@utanet.fi>
 * To: claudio@helllabs.org, hipolito@brhs.com.br
 * Subject: gusmax
 * 
 * i got rh5.1 running on p90/48mb and my soundcard is gusmax which i use
 * with jaroslav's lowlevel gus drivers.. well there's a problem with xmp
 * when i try to load modules bigger than half megs (my gusmax has only 512k
 * memory), it hangs and goes in to 'uninoterruptible sleep' and cannot be
 * killed even with kill -9.. well anyway i thought it'd be nice to have
 * perhaps a cubicplayer style sample crunching if soundcard doesn't have
 * enough memory, or at least try and make it not hang if i try to load
 * modules that are too big.. :)
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdlib.h>
#include <string.h>
#include "driver.h"
#include "convert.h"
#include "mixer.h"

#define INTERPOLATE() \
    if (itpt >> SMIX_SHIFT) { \
	cur_bk += itpt >> SMIX_SHIFT; \
	smp_x1 = in_bk[cur_bk]; \
	smp_dt = in_bk[cur_bk + 1] - smp_x1; \
	itpt &= SMIX_MASK; \
    }

#define CRUNCHER() \
    cur_bk = -1; \
    itpt = 0x10000; \
    smp_x1 = smp_dt = 0; \
    while (count--) { \
	INTERPOLATE (); \
	*(out_bk++) = smp_x1 + ((itpt * smp_dt) >> SMIX_SHIFT); \
	itpt += itpt_inc; \
    }


static void
cruncher_8bit (int count, int itpt_inc, char *in_bk, char *out_bk)
{
    int smp_x1, smp_dt, itpt, cur_bk;

    CRUNCHER ();
}


static void
cruncher_16bit (int count, int itpt_inc, int16 *in_bk, int16 *out_bk)
{
    int smp_x1, smp_dt, itpt, cur_bk;

    CRUNCHER ();
}


int xmp_cvt_crunch (struct patch_info **patch, unsigned int ratio)
{
    struct patch_info *pi;
    int type, loop_end, loop_dt, smp_len;
    int itpt_inc, base_note;

    if (ratio == 0x10000)
	return ratio;
	
    if ((*patch)->len == XMP_PATCH_FM)
	return 0;

    type = 0;
    loop_end = (*patch)->loop_end;
    loop_dt = loop_end - (*patch)->loop_start;
    smp_len = (*patch)->len;

    if ((*patch)->mode & WAVE_16_BITS) {
        smp_len >>= 1;
        loop_dt >>= 1;
        loop_end >>= 1;
        ++type;
    }

    if (ratio < 0x10000 && smp_len < XMP_MINLEN)
	return 0x10000;

    base_note = ((int64) ((*patch)->base_note) << SMIX_SHIFT) / ratio;
    itpt_inc  = ((int64) base_note << SMIX_SHIFT) / (*patch)->base_note;
    smp_len   = ((int64) smp_len << SMIX_SHIFT) / itpt_inc;
    loop_end  = ((int64) loop_end << SMIX_SHIFT) / itpt_inc;
    loop_dt   = ((int64) loop_dt << SMIX_SHIFT) / itpt_inc;

    pi = calloc (1, sizeof (struct patch_info) + (smp_len << type) +
	sizeof (int));

    memcpy (pi, *patch, sizeof (struct patch_info));
    pi->len = smp_len << type;
    pi->loop_end = loop_end << type;
    pi->loop_start = (loop_end - loop_dt) << type;
    pi->base_note = base_note;

    if (type)
	cruncher_16bit (smp_len, itpt_inc,
	(int16 *) &(*patch)->data, (int16 *) &pi->data);
    else
	cruncher_8bit (smp_len, itpt_inc,
	(char *) &(*patch)->data, (char *) &pi->data);

    free (*patch);
    *patch = pi;

    return ratio;
}
