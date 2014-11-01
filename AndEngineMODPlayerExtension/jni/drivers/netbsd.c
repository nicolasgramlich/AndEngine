/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Based on bsd.c and solaris.c */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <sys/types.h>
#include <sys/param.h>
#include <sys/audioio.h>
#include <sys/mman.h>
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
static int audioctl_fd;

static int init(struct xmp_context *);
static int setaudio(struct xmp_options *);
static void bufdump(struct xmp_context *, int);
static void shutdown(struct xmp_context *);

static void dummy()
{
}

static char *help[] = {
	"gain=val", "Audio output gain (0 to 255)",
/* XXX "port={s|h|l}", "Audio port (s[peaker], h[eadphones], l[ineout])", XXX */
	"buffer=val", "Audio buffer size (default is 32768)",
	NULL
};

struct xmp_drv_info drv_netbsd = {
	"netbsd",		/* driver ID */
	"NetBSD PCM audio",	/* driver description */
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

	/* try to open audioctldevice */
	if ((audioctl_fd = open("/dev/audioctl", O_RDWR)) < 0) {
		fprintf(stderr, "couldn't open audioctldevice\n");
		close(audio_fd);
		return -1;
	}

	/* empty buffers before change config */
	ioctl(audio_fd, AUDIO_DRAIN, 0);	/* drain everything out */
	ioctl(audio_fd, AUDIO_FLUSH);		/* flush audio */
	ioctl(audioctl_fd, AUDIO_FLUSH);	/* flush audioctl */

	/* get audio parameters. */
	if (ioctl(audioctl_fd, AUDIO_GETINFO, &ainfo) < 0) {
		fprintf(stderr, "AUDIO_GETINFO failed!\n");
		close(audio_fd);
		close(audioctl_fd);
		return -1;
	}

	close(audioctl_fd);

	parm_init();
	chkparm1("gain", gain = strtoul(token, NULL, 0));
	chkparm1("buffer", bsize = strtoul(token, NULL, 0));
	/* chkparm1 ("port", port = (int)*token) */
	parm_end();

#if 0
	switch (port) {
	case 'h':
		port = AUDIO_HEADPHONE;
		break;
	case 'l':
		port = AUDIO_LINE_OUT;
		break;
	case 's':
		port = AUDIO_SPEAKER;
    }
#endif
	if (gain < AUDIO_MIN_GAIN)
		gain = AUDIO_MIN_GAIN;
	if (gain > AUDIO_MAX_GAIN)
		gain = AUDIO_MAX_GAIN;

	AUDIO_INITINFO(&ainfo);

	ainfo.play.sample_rate = o->freq;
	ainfo.play.channels = o->outfmt & XMP_FMT_MONO ? 1 : 2;
	ainfo.play.precision = o->resol;
	/* ainfo.play.precision = AUDIO_ENCODING_ULINEAR; */
	ainfo.play.encoding = o->resol > 8 ?
	    AUDIO_ENCODING_SLINEAR : AUDIO_ENCODING_ULINEAR;
	ainfo.play.gain = gain;
	/* ainfo.play.port = port; */
	ainfo.play.buffer_size = bsize;

	if (ioctl(audio_fd, AUDIO_SETINFO, &ainfo) == -1) {
		close(audio_fd);
		return XMP_ERR_DINIT;
	}

	/* o->resol = 0; */
	/* o->freq = 8000; */
	/* o->outfmt |=XMP_FMT_MONO; */
	o->freq = 44000;
	drv_netbsd.description = "NetBSD PCM audio";
	return 0;
}

static int init(struct xmp_context *ctx)
{
	if ((audio_fd = open("/dev/sound", O_WRONLY)) == -1)
		return XMP_ERR_DINIT;

	if (setaudio(&ctx->o) != 0)
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
