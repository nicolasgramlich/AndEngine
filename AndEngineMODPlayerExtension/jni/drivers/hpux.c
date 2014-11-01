/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* This code was tested on a 9000/710 running HP-UX 9.05 with 8 kHz,
 * 16 bit mono output.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/audio.h>
#include <fcntl.h>

#include "common.h"
#include "driver.h"
#include "mixer.h"

static int audio_fd;

static int init(struct xmp_context *);
static int setaudio(struct xmp_options *);
static void bufdump(struct xmp_context *, int);
static void shutdown(struct xmp_context *);

static void dummy()
{
}

/* Standard sampling rates */
static int srate[] = {
	44100, 32000, 22050, 16000, 11025, 8000, 0
};

static char *help[] = {
	"gain=val", "Audio output gain (0 to 255)",
	"port={s|h|l}", "Audio port (s[peaker], h[eadphones], l[ineout])",
	"buffer=val", "Audio buffer size",
	NULL
};

struct xmp_drv_info drv_hpux = {
	"hpux",			/* driver ID */
	"HP-UX PCM audio",	/* driver description */
	help,			/* help */
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
	dummy,			/* reset */
	bufdump,		/* bufdump */
	dummy,			/* bufwipe */
	dummy,			/* clearmem */
	dummy,			/* sync */
	xmp_smix_writepatch,	/* writepatch */
	xmp_smix_getmsg,	/* getmsg */
	NULL
};

static int setaudio(struct xmp_options *o)
{
	int flags;
	int gain = 128;
	int bsize = 32 * 1024;
	int port = AUDIO_OUT_SPEAKER;
	struct audio_gains agains;
	struct audio_describe adescribe;
	char *token, **parm;
	int i;

	parm_init();
	chkparm1("gain", gain = strtoul(token, NULL, 0));
	chkparm1("buffer", bsize = strtoul(token, NULL, 0));
	chkparm1("port", port = (int)*token)
	    parm_end();

	switch (port) {
	case 'h':
		port = AUDIO_OUT_HEADPHONE;
		break;
	case 'l':
		port = AUDIO_OUT_LINE;
		break;
	default:
		port = AUDIO_OUT_SPEAKER;
	}

	if ((flags = fcntl(audio_fd, F_GETFL, 0)) < 0)
		return XMP_ERR_DINIT;

	flags |= O_NDELAY;
	if ((flags = fcntl(audio_fd, F_SETFL, flags)) < 0)
		return XMP_ERR_DINIT;

	if (ioctl(audio_fd, AUDIO_SET_DATA_FORMAT, AUDIO_FORMAT_LINEAR16BIT) ==
	    -1)
		return XMP_ERR_DINIT;

	if (ioctl(audio_fd, AUDIO_SET_CHANNELS,
		  o->outfmt & XMP_FMT_MONO ? 1 : 2) == -1) {
		o->outfmt ^= XMP_FMT_MONO;

		if (ioctl(audio_fd, AUDIO_SET_CHANNELS,
			  o->outfmt & XMP_FMT_MONO ? 1 : 2) == -1)
			return XMP_ERR_DINIT;
	}

	ioctl(audio_fd, AUDIO_SET_OUTPUT, port);

	for (i = 0; ioctl(audio_fd, AUDIO_SET_SAMPLE_RATE, o->freq) == -1; i++)
		if ((o->freq = srate[i]) == 0)
			return XMP_ERR_DINIT;

	if (ioctl(audio_fd, AUDIO_DESCRIBE, &adescribe) == -1)
		return XMP_ERR_DINIT;

	if (ioctl(audio_fd, AUDIO_GET_GAINS, &agains) == -1)
		return XMP_ERR_DINIT;

	agains.transmit_gain = adescribe.min_transmit_gain +
		(adescribe.max_transmit_gain - adescribe.min_transmit_gain) *
		gain / 256;

	if (ioctl(audio_fd, AUDIO_SET_GAINS, &agains) == -1)
		return XMP_ERR_DINIT;

	ioctl(audio_fd, AUDIO_SET_TXBUFSIZE, bsize);

	return 0;
}

static int init(struct xmp_context *ctx)
{
	if ((audio_fd = open("/dev/audio", O_WRONLY)) == -1)
		return XMP_ERR_DINIT;

	if (setaudio(ctl) != 0)
		return XMP_ERR_DINIT;

	return xmp_smix_on(ctx);
}

/* Build and write one tick (one PAL frame or 1/50 s in standard vblank
 * timed mods) of audio data to the output device.
 */
static void bufdump(struct xmp_context *ctx, int i)
{
	int j;
	void *b;

	b = xmp_smix_buffer(ctx);
	while (i) {
		if ((j = write(audio_fd, b, i)) > 0) {
			i -= j;
			(char *)b += j;
		} else
			break;
	}
}

static void shutdown(struct xmp_context *ctx)
{
	xmp_smix_off(ctx);
	close(audio_fd);
}
