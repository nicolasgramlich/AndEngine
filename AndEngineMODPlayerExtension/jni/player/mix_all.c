/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr.
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See docs/COPYING
 * for more information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "common.h"
#include "driver.h"
#include "mixer.h"
#include "synth.h"


/* Mixers
 *
 * To increase performance eight mixers are defined, one for each
 * combination of the following parameters: interpolation, resolution
 * and number of channels.
 */
#define INTERPOLATE() do { \
    if (itpt >> SMIX_SHIFT) { \
	cur_bk += itpt >> SMIX_SHIFT; \
	smp_x1 = in_bk[cur_bk]; \
	smp_dt = in_bk[cur_bk + 1] - smp_x1; \
	itpt &= SMIX_MASK; \
    } \
    smp_in = smp_x1 + ((itpt * smp_dt) >> SMIX_SHIFT); \
} while (0)

#define DONT_INTERPOLATE() do { \
    smp_in = in_bk[itpt >> SMIX_SHIFT]; \
} while (0)

#define DO_FILTER() do { \
    smp_in = (smp_in * vi->flt_B0 + fx1 * vi->flt_B1 + fx2 * vi->flt_B2) / FILTER_PRECISION; \
    fx2 = fx1; fx1 = smp_in; \
} while (0)

#define SAVE_FILTER() do { \
    vi->flt_X1 = fx1; \
    vi->flt_X2 = fx2; \
} while (0)

#define MIX_STEREO() do { \
    *(tmp_bk++) += smp_in * vr; \
    *(tmp_bk++) += smp_in * vl; \
    itpt += itpt_inc; \
} while (0)

#define MIX_MONO() do { \
    *(tmp_bk++) += smp_in * vl; \
    itpt += itpt_inc; \
} while (0)

#define MIX_STEREO_AC() do { \
    if (vi->attack) { \
	int a = SLOW_ATTACK - vi->attack; \
	*(tmp_bk++) += smp_in * vr * a / SLOW_ATTACK; \
	*(tmp_bk++) += smp_in * vl * a / SLOW_ATTACK; \
	vi->attack--; \
    } else { \
	*(tmp_bk++) += smp_in * vr; \
	*(tmp_bk++) += smp_in * vl; \
    } \
    itpt += itpt_inc; \
} while (0)

#define MIX_MONO_AC() do { \
    if (vi->attack) { \
	*(tmp_bk++) += smp_in * vl * (SLOW_ATTACK - vi->attack) / SLOW_ATTACK; \
	vi->attack--; \
    } else { \
	*(tmp_bk++) += smp_in * vl; \
    } \
    itpt += itpt_inc; \
} while (0)

#define VAR_NORM(x) \
    register int smp_in; \
    x *in_bk = vi->sptr; \
    int cur_bk = vi->pos - 1; \
    int itpt = vi->itpt + (1 << SMIX_SHIFT)

#define VAR_ITPT(x) \
    VAR_NORM(x); \
    int smp_x1 = 0, smp_dt = 0

#define VAR_FILT \
    int fx1 = vi->flt_X1, fx2 = vi->flt_X2

#define SMIX_MIXER(f) void f(struct voice_info *vi, int* tmp_bk, \
    int count, int vl, int vr, int itpt_inc)


/* Handler for 8 bit samples, interpolated stereo output
 */
SMIX_MIXER(smix_st8itpt)
{
    VAR_ITPT(int8);
    while (count--) { INTERPOLATE(); MIX_STEREO_AC(); }
}


/* Handler for 16 bit samples, interpolated stereo output
 */
SMIX_MIXER(smix_st16itpt)
{
    VAR_ITPT(int16);

    vl >>= 8;
    vr >>= 8;
    while (count--) { INTERPOLATE(); MIX_STEREO_AC(); }
}


/* Handler for 8 bit samples, non-interpolated stereo output
 */
SMIX_MIXER(smix_st8norm)
{
    VAR_NORM(int8);

    in_bk += cur_bk;
    while (count--) { DONT_INTERPOLATE(); MIX_STEREO(); }
}


/* Handler for 16 bit samples, non-interpolated stereo output
 */
SMIX_MIXER(smix_st16norm)
{
    VAR_NORM(int16);

    vl >>= 8;
    vr >>= 8;
    in_bk += cur_bk;
    while (count--) { DONT_INTERPOLATE(); MIX_STEREO(); }
}


/* Handler for 8 bit samples, interpolated mono output
 */
SMIX_MIXER(smix_mn8itpt)
{
    VAR_ITPT(int8);

    vl <<= 1;
    while (count--) { INTERPOLATE(); MIX_MONO_AC(); }
}


/* Handler for 16 bit samples, interpolated mono output
 */
SMIX_MIXER(smix_mn16itpt)
{
    VAR_ITPT(int16);

    vl >>= 7;
    while (count--) { INTERPOLATE(); MIX_MONO_AC(); }
}


/* Handler for 8 bit samples, non-interpolated mono output
 */
SMIX_MIXER(smix_mn8norm)
{
    VAR_NORM(int8);

    vl <<= 1;
    in_bk += cur_bk;
    while (count--) { DONT_INTERPOLATE(); MIX_MONO(); }
}


/* Handler for 16 bit samples, non-interpolated mono output
 */
SMIX_MIXER(smix_mn16norm)
{
    VAR_NORM(int16);

    vl >>= 7;
    in_bk += cur_bk;
    while (count--) { DONT_INTERPOLATE(); MIX_MONO(); }
}

/*
 * Filtering mixers
 */

/* Handler for 8 bit samples, interpolated stereo output
 */
SMIX_MIXER(smix_st8itpt_flt)
{
    VAR_ITPT(int8);
    VAR_FILT;

    while (count--) { INTERPOLATE(); DO_FILTER(); MIX_STEREO_AC(); }
    SAVE_FILTER();
}


/* Handler for 16 bit samples, interpolated stereo output
 */
SMIX_MIXER(smix_st16itpt_flt)
{
    VAR_ITPT(int16);
    VAR_FILT;

    vl >>= 8;
    vr >>= 8;
    while (count--) { INTERPOLATE(); DO_FILTER(); MIX_STEREO_AC(); }
    SAVE_FILTER();
}


/* Handler for 8 bit samples, interpolated mono output
 */
SMIX_MIXER(smix_mn8itpt_flt)
{
    VAR_ITPT(int8);
    VAR_FILT;

    vl <<= 1;
    while (count--) { INTERPOLATE(); DO_FILTER(); MIX_MONO_AC(); }
    SAVE_FILTER();
}


/* Handler for 16 bit samples, interpolated mono output
 */
SMIX_MIXER(smix_mn16itpt_flt)
{
    VAR_ITPT(int16);
    VAR_FILT;

    vl >>= 7;
    while (count--) { INTERPOLATE(); DO_FILTER(); MIX_MONO_AC(); }
    SAVE_FILTER();
}


/* Handler for synthesized sounds
 */
SMIX_MIXER(smix_synth)
{
    synth_mixer(tmp_bk, count, vl >> 7, vr >> 7, itpt_inc);
}

