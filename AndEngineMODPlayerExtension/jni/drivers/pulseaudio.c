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

#include <unistd.h>
#include <pulse/simple.h>
#include <pulse/error.h>

#include "common.h"
#include "driver.h"
#include "mixer.h"

static pa_simple *s;

static int init(struct xmp_context *);
static void bufdump(struct xmp_context *, int);
static void myshutdown(struct xmp_context *);
static void flush();

static void dummy()
{
}

struct xmp_drv_info drv_pulseaudio = {
	"pulseaudio",		/* driver ID */
	"PulseAudio",		/* driver description */
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
	flush,			/* flush */
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
	pa_sample_spec ss;
	int error;

	ss.format = PA_SAMPLE_S16NE;
	ss.channels = o->outfmt & XMP_FMT_MONO ? 1 : 2;
	ss.rate = o->freq;

	s = pa_simple_new(NULL,		/* Use the default server */
		"xmp",			/* Our application's name */
		PA_STREAM_PLAYBACK,
		NULL,			/* Use the default device */
		"Music",		/* Description of our stream */
		&ss,			/* Our sample format */
		NULL,			/* Use default channel map */
		NULL,			/* Use default buffering attributes */
		&error);		/* Ignore error code */

	if (s == 0) {
		fprintf(stderr, "pulseaudio error: %s\n", pa_strerror(error));
		return XMP_ERR_DINIT;
	}

	return xmp_smix_on(ctx);
}

static void flush()
{
	int error;

	if (pa_simple_drain(s, &error) < 0) {
		fprintf(stderr, "pulseaudio error: %s\n", pa_strerror(error));
	}
}

static void bufdump(struct xmp_context *ctx, int i)
{
	int j, error;
	void *b;

	b = xmp_smix_buffer(ctx);
	do {
		if ((j = pa_simple_write(s, b, i, &error)) > 0) {
			i -= j;
			b += j;
		} else
			break;
	} while (i);

	if (j < 0) {
		fprintf(stderr, "pulseaudio error: %s\n", pa_strerror(error));
	}
}

static void myshutdown(struct xmp_context *ctx)
{
	xmp_smix_off(ctx);

	if (s)
		pa_simple_free(s);
}
