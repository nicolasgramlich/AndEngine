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

#include <artsc.h>
#include "common.h"
#include "driver.h"
#include "mixer.h"

static arts_stream_t as;

static int init(struct xmp_context *);
static void bufdump(struct xmp_context *, int);
static void myshutdown(struct xmp_context *);

static void dummy()
{
}

struct xmp_drv_info drv_arts = {
	"arts",			/* driver ID */
	"aRts client driver",	/* driver description */
	NULL,			/* help */
	init,			/* init */
	myshutdown,		/* shutdown */
	xmp_smix_numvoices,	/* numvoices */
	dummy,			/* voicepos */
	xmp_smix_echoback,	/* echoback */
	dummy,			/* setpatch */
	xmp_smix_setvol,	/* setvol */
	dummy,			/* setnote */
	xmp_smix_setpan,	/* setpan */
	dummy,			/* setbend */
	xmp_smix_seteffect,	/* seteffect */
	dummy,			/* starttimer */
	dummy,			/* flush */
	dummy,			/* resetvoices */
	bufdump,		/* bufdump */
	dummy,			/* bufwipe */
	dummy,			/* clearmem */
	dummy,			/* sync */
	xmp_smix_writepatch,	/* writepatch */
	xmp_smix_getmsg,	/* getmsg */
	NULL
};

static int init(struct xmp_context *ctx)
{
	struct xmp_options *o = &ctx->o;
	int rc, rate, bits, channels;

	rate = o->freq;
	bits = o->resol;
	channels = 2;

	if ((rc = arts_init()) < 0) {
		fprintf(stderr, "%s\n", arts_error_text(rc));
		return XMP_ERR_DINIT;
	}

	if (o->outfmt & XMP_FMT_MONO)
		channels = 1;

	as = arts_play_stream(rate, bits, channels, "xmp");

	return xmp_smix_on(ctx);
}

static void bufdump(struct xmp_context *ctx, int i)
{
	int j;
	void *b;

	b = xmp_smix_buffer(ctx);
	do {
		if ((j = arts_write(as, b, i)) > 0) {
			i -= j;
			b += j;
		} else
			break;
	} while (i);
}

static void myshutdown(struct xmp_context *ctx)
{
	xmp_smix_off(ctx);
	arts_close_stream(as);
	arts_free();
}
