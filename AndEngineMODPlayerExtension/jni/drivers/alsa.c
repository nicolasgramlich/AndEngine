/* ALSA driver for xmp
 * Copyright (C) 2005-2007 Claudio Matsuoka and Hipolito Carraro Jr
 * Based on the ALSA 0.5 driver for xmp, Copyright (C) 2000 Tijs
 * van Bakel and Rob Adamson.
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <alsa/asoundlib.h>
#include <alsa/pcm.h>

#include "driver.h"
#include "mixer.h"

static int init (struct xmp_context *);
static int prepare_driver (void);
static void echoback (struct xmp_context *, int);
static void dshutdown (struct xmp_context *);
static int to_fmt (struct xmp_options *);
static void bufdump (struct xmp_context *, int);
static void flush (void);

static void dummy () { }

static char *help[] = {
	"buffer=num", "Set the ALSA buffer time in milliseconds",
	"period=num", "Set the ALSA period time in milliseconds",
	"card <name>", "Select sound card to use",
	NULL
};

struct xmp_drv_info drv_alsa = {
	"alsa",			/* driver ID */
	"ALSA PCM audio",	/* driver description */
	help,			/* help */
	init,			/* init */
	dshutdown,		/* shutdown */
	xmp_smix_numvoices,	/* numvoices */
	dummy,			/* voicepos */
	echoback,		/* echoback */
	dummy,			/* setpatch */
	xmp_smix_setvol,	/* setvol */
	dummy,			/* setnote */
	xmp_smix_setpan,	/* setpan */
	dummy,			/* setbend */
	xmp_smix_seteffect,	/* seteffect */
	dummy,			/* starttimer */
	flush,			/* flush */
	dummy,			/* reset */
	bufdump,		/* bufdump */
	dummy,			/* bufwipe */
	dummy,			/* clearmem */
	dummy,			/* sync */
	xmp_smix_writepatch,	/* writepatch */
	xmp_smix_getmsg,	/* getmsg */
	NULL
};

static snd_pcm_t *pcm_handle;

static void echoback(struct xmp_context *ctx, int msg)
{
	xmp_smix_echoback(ctx, msg);
}

static int init(struct xmp_context *ctx)
{
	snd_pcm_hw_params_t *hwparams;
	int ret;
	char *token, **parm;
	unsigned int channels, rate;
	unsigned int btime = 2000000;	/* 2s */
	unsigned int ptime = 100000;	/* 100ms */
	char *card_name = "default";
	struct xmp_options *o = &ctx->o;

	parm_init();  
	chkparm1("buffer", btime = 1000 * strtoul(token, NULL, 0));
	chkparm1("period", btime = 1000 * strtoul(token, NULL, 0));
	chkparm1("card", card_name = token);
	parm_end();

	if ((ret = snd_pcm_open(&pcm_handle, card_name,
		SND_PCM_STREAM_PLAYBACK, 0)) < 0) {
		fprintf(stderr, "Unable to initialize ALSA pcm device: %s\n",
					snd_strerror(ret));
		return XMP_ERR_DINIT;
	}

	channels = (o->outfmt & XMP_FMT_MONO) ? 1 : 2;
	rate = o->freq;

	snd_pcm_hw_params_alloca(&hwparams);
	snd_pcm_hw_params_any(pcm_handle, hwparams);
	snd_pcm_hw_params_set_access(pcm_handle, hwparams,
		SND_PCM_ACCESS_RW_INTERLEAVED);
	snd_pcm_hw_params_set_format(pcm_handle, hwparams, to_fmt(o));
	snd_pcm_hw_params_set_rate_near(pcm_handle, hwparams, &rate, 0);
	snd_pcm_hw_params_set_channels_near(pcm_handle, hwparams, &channels);
	snd_pcm_hw_params_set_buffer_time_near(pcm_handle, hwparams, &btime, 0);
	snd_pcm_hw_params_set_period_time_near(pcm_handle, hwparams, &ptime, 0);
	snd_pcm_nonblock(pcm_handle, 0);
	
	if ((ret = snd_pcm_hw_params(pcm_handle, hwparams)) < 0) {
		fprintf(stderr, "Unable to set ALSA output parameters: %s\n",
					snd_strerror(ret));
		return XMP_ERR_DINIT;
	}

	if (prepare_driver() < 0)
		return XMP_ERR_DINIT;
  
	o->freq = rate;

	return xmp_smix_on(ctx);
}

static int prepare_driver()
{
	int ret;

	if ((ret = snd_pcm_prepare(pcm_handle)) < 0 ) {
		fprintf(stderr, "Unable to prepare ALSA: %s\n",
					snd_strerror(ret));
		return ret;
	}

	return 0;
}

static int to_fmt(struct xmp_options *o)
{
	int fmt;

	switch (o->resol) {
	case 0:
		return SND_PCM_FORMAT_MU_LAW;
	case 8:
		fmt = SND_PCM_FORMAT_U8 | SND_PCM_FORMAT_S8;
		break;
	default:
		if (o->big_endian) {
			fmt = SND_PCM_FORMAT_S16_BE | SND_PCM_FORMAT_U16_BE;
		} else {
			fmt = SND_PCM_FORMAT_S16_LE | SND_PCM_FORMAT_U16_LE;
		}
	}

	if (o->outfmt & XMP_FMT_UNS) {
      		fmt &= SND_PCM_FORMAT_U8 | SND_PCM_FORMAT_U16_LE |
			SND_PCM_FORMAT_U16_BE;
	} else {
		fmt &= SND_PCM_FORMAT_S8 | SND_PCM_FORMAT_S16_LE |
			SND_PCM_FORMAT_S16_BE;
	}

	return fmt;
}

/* Build and write one tick (one PAL frame or 1/50 s in standard vblank
 * timed mods) of audio data to the output device.
 */
static void bufdump(struct xmp_context *ctx, int i)
{
	void *b;
	int frames;

	b = xmp_smix_buffer(ctx);
	frames = snd_pcm_bytes_to_frames(pcm_handle, i);
	if (snd_pcm_writei(pcm_handle, b, frames) < 0) {
		snd_pcm_prepare(pcm_handle);
	}
}

static void dshutdown(struct xmp_context *ctx)
{
	xmp_smix_off(ctx);
	snd_pcm_close(pcm_handle);
}

static void flush()
{
	snd_pcm_drain(pcm_handle);
	prepare_driver();
}
