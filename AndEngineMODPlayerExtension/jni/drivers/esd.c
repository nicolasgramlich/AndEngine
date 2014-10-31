/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Based on esdcat.c from the Enlightened Sound Daemon 0.2 for Linux
 * More details at http://www.netcom.com/~ericmit/EsounD.html
 */

/* Thu, 15 Oct 1998 00:39:42 -0500  Terry Glass <tglass@bigfoot.com>
 * Fixed number of arguments in esd_play_stream ()
 *
 * Fri, 25 Dec 1998 17:54:39 +0200  Pavel Pavlov <Simon.Says@iname.com>
 * Fixed parameters for xmp 2.0
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <unistd.h>
#include <esd.h>
#include "common.h"
#include "driver.h"
#include "mixer.h"

static int audio_fd = -1;

static int init(struct xmp_context *);
static void bufdump(struct xmp_context *, int);
static void myshutdown(struct xmp_context *);

static void dummy()
{
}

struct xmp_drv_info drv_esd = {
	"esd",			/* driver ID */
	"Enlightened Sound Daemon client",	/* driver description */
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
	int format, rate = ESD_DEFAULT_RATE;
	int bits = ESD_BITS16, channels = ESD_STEREO;
	int mode = ESD_STREAM, func = ESD_PLAY;
	struct xmp_options *o = &ctx->o;

	if (o->resol == 8)
		bits = ESD_BITS8;
	if (o->outfmt & XMP_FMT_MONO)
		channels = ESD_MONO;
	rate = o->freq;
	format = bits | channels | mode | func;

	printf("opening socket, format = 0x%08x at %d Hz\n", format, rate);

	/* Number of arguments fixed by Terry Glass <tglass@bigfoot.com> */
	if ((audio_fd = esd_play_stream(format, rate, NULL, "xmp")) <= 0) {
		fprintf(stderr, "drv_esd: unable to connect to server\n");
		return XMP_ERR_DINIT;
	}

	return xmp_smix_on(ctx);
}

static void bufdump(struct xmp_context *ctx, int i)
{
	int j;
	void *b;

	b = xmp_smix_buffer(ctx);
	do {
		if ((j = write(audio_fd, b, i)) > 0) {
			i -= j;
			b += j;
		} else
			break;
	} while (i);
}

static void myshutdown(struct xmp_context *ctx)
{
	xmp_smix_off(ctx);

	if (audio_fd)
		close(audio_fd);
}
