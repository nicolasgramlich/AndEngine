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

#include <sys/types.h>
#include <sys/param.h>
#include <sys/audioio.h>
#include <sys/ioctl.h>
#include <sys/stat.h>

#include <fcntl.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "common.h"
#include "driver.h"
#include "mixer.h"

static int audio_fd;

static int init(struct xmp_context *);
static int setaudio(struct xmp_option *);
static void bufdump(struct xmp_context *, int);
static void shutdown(struct xmp_context *);

static void dummy()
{
}

static char *help[] = {
	"gain=val", "Audio output gain (0 to 255)",
	"buffer=val", "Audio buffer size (default is 32768)",
	NULL
};

struct xmp_drv_info drv_bsd = {
	"bsd",			/* driver ID */
	"BSD PCM audio",	/* driver description */
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
	audio_info_t ainfo;
	int gain = 128;
	int bsize = 32 * 1024;
	char *token, **parm;

	parm_init();
	chkparm1("gain", gain = strtoul(token, NULL, 0));
	chkparm1("buffer", bsize = strtoul(token, NULL, 0));
	parm_end();

	if (gain < AUDIO_MIN_GAIN)
		gain = AUDIO_MIN_GAIN;
	if (gain > AUDIO_MAX_GAIN)
		gain = AUDIO_MAX_GAIN;

	AUDIO_INITINFO(&ainfo);

	ainfo.play.sample_rate = o->freq;
	ainfo.play.channels = o->outfmt & XMP_FMT_MONO ? 1 : 2;
	ainfo.play.precision = o->resol;
	ainfo.play.encoding = o->resol > 8 ?
	    AUDIO_ENCODING_SLINEAR : AUDIO_ENCODING_ULINEAR;
	ainfo.play.gain = gain;
	ainfo.play.buffer_size = bsize;

	if (ioctl(audio_fd, AUDIO_SETINFO, &ainfo) == -1) {
		close(audio_fd);
		return XMP_ERR_DINIT;
	}

	drv_bsd.description = "BSD PCM audio";

	return 0;
}

static int init(struct xmp_context *ctx)
{
	if ((audio_fd = open("/dev/sound", O_WRONLY)) == -1)
		return XMP_ERR_DINIT;

	if (setaudio(o) != 0)
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
	};
}

static void shutdown(struct xmp_context *ctx)
{
	xmp_smix_off(ctx);
	close(audio_fd);
}
