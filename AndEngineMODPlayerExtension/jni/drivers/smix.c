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

#include "common.h"
#include "driver.h"
#include "mixer.h"

static int init(struct xmp_context *);
static void shutdown(struct xmp_context *);

static void dummy()
{
}

struct xmp_drv_info drv_smix = {
	"smix",			/* driver ID */
	"nil softmixer",	/* driver description */
	NULL,			/* help */
	init,			/* init */
	shutdown,		/* shutdown */
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
	dummy,			/* bufdump */
	dummy,			/* bufwipe */
	dummy,			/* clearmem */
	dummy,			/* sync */
	xmp_smix_writepatch,	/* writepatch */
	xmp_smix_getmsg,	/* getmsg */
	NULL
};

static int init(struct xmp_context *ctx)
{
	return xmp_smix_on(ctx);
}

static void shutdown(struct xmp_context *ctx)
{
	xmp_smix_off(ctx);
}
