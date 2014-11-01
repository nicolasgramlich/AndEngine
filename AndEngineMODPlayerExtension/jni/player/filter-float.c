/*
 * This program is  free software; you can redistribute it  and modify it
 * under the terms of the GNU  General Public License as published by the
 * Free Software Foundation; either version 2  of the license or (at your
 * option) any later version.
 *
 * Authors: Olivier Lapicque <olivierl@jps.net>
 */

/*
 * Modified for xmp 2.0 by Claudio Matsuoka
 * (optimized, removed all math functions, added precalculated tables)
 * Mon Dec 25 10:49:19 BRST 2000
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <math.h>
#include "xmp.h"
#include "common.h"
#include "player.h"
#include "driver.h"
#include "mixer.h"


static float filter_cutoff(int cutoff /*, int modifier */)
{
        float fc;

	fc = 110.0 * pow(2.0, 0.25 + ((float)(cutoff * (/*modifier +*/ 256))) /
					(24.0 * 512));
        if (fc < 120)
		return 120;

        if (fc > 10000)
		return 10000;

        return fc;
}



/*
 * Simple 2-poles resonant filter
 */
void filter_setup(struct xmp_context *ctx, struct xmp_channel *xc, int cutoff)
{
	struct xmp_options *o = &ctx->o;
	float fc, fs, dmpfac, d, e;
	float fg, fb0, fb1;

	/* [0-255] => [100Hz-8000Hz] */
	fc = filter_cutoff(cutoff /*, 0 */);
	fs = (float)o->freq;

	if (fc > fs / 2)
		fc = fs / 2;

	fc *= 3.14159265358979 * 2 / fs;
	dmpfac = pow(10.0, -((21.0 / 128.0) * (float)xc->resonance) / 20.0);
	d = (1.0 - 2.0 * dmpfac) * fc;
	if (d > 2.0)
		d = 2.0;
	d = (2.0 * dmpfac - d) / fc;
	e = 1.0 / (fc * fc);

	fg = 1.0 / (1 + d + e);
	fb0 = (d + e + e) / (1 + d + e);
	fb1 = -e / (1 + d + e);

	xc->flt_B0 = (int)(fg * FILTER_PRECISION);
	xc->flt_B1 = (int)(fb0 * FILTER_PRECISION);
	xc->flt_B2 = (int)(fb1 * FILTER_PRECISION);
}

