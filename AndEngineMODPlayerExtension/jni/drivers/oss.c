/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/*
 * devfs /dev/sound/dsp support by Dirk Jagdmann
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <string.h>
#include <stdlib.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>

#if defined(HAVE_SYS_SOUNDCARD_H)
#include <sys/soundcard.h>
#elif defined(HAVE_MACHINE_SOUNDCARD_H)
#include <machine/soundcard.h>
#endif

#include "driver.h"
#include "mixer.h"

static int audio_fd;

static void from_fmt(struct xmp_options *, int);
static int to_fmt(struct xmp_options *);
static void setaudio(struct xmp_options *);
static int init(struct xmp_context *);
static void shutdown(struct xmp_context *);
static void bufdump(struct xmp_context *, int);
static void flush(void);

static void dummy()
{
}

static char *help[] = {
	"frag=num,size", "Set the number and size of fragments",
	"dev=<device_name>", "Audio device to use (default /dev/dsp)",
	"nosync", "Don't flush OSS buffers between modules",
#ifdef HAVE_AUDIO_BUF_INFO
	"voxware", "For VoxWare 2.90 (used in Linux 1.2.13)",
#endif
	NULL
};

struct xmp_drv_info drv_oss = {
	"oss",			/* driver ID */
	"OSS PCM audio",	/* driver description */
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
	flush,			/* stoptimer */
	dummy,			/* reset */
	bufdump,		/* bufdump */
	dummy,			/* bufwipe */
	dummy,			/* clearmem */
	dummy,			/* sync */
	xmp_smix_writepatch,	/* writepatch */
	xmp_smix_getmsg,	/* getmsg */
	NULL
};

static int fragnum, fragsize;
static int do_sync = 1;
#ifdef HAVE_AUDIO_BUF_INFO
static int voxware = 0;		/* For Linux 1.2.13 */
#endif

static int to_fmt(struct xmp_options *o)
{
	int fmt;

	if (!o->resol)
		return AFMT_MU_LAW;

	if (o->resol == 8)
		fmt = AFMT_U8 | AFMT_S8;
	else {
		fmt = o->big_endian ? AFMT_S16_BE | AFMT_U16_BE :
		    AFMT_S16_LE | AFMT_U16_LE;
	}

	if (o->outfmt & XMP_FMT_UNS)
		fmt &= AFMT_U8 | AFMT_U16_LE | AFMT_U16_BE;
	else
		fmt &= AFMT_S8 | AFMT_S16_LE | AFMT_S16_BE;

	return fmt;
}

static void from_fmt(struct xmp_options *o, int outfmt)
{
	if (outfmt & AFMT_MU_LAW) {
		o->resol = 0;
		return;
	}

	if (outfmt & (AFMT_S16_LE | AFMT_S16_BE | AFMT_U16_LE | AFMT_U16_BE))
		o->resol = 16;
	else
		o->resol = 8;

	if (outfmt & (AFMT_U8 | AFMT_U16_LE | AFMT_U16_BE))
		o->outfmt |= XMP_FMT_UNS;
	else
		o->outfmt &= ~XMP_FMT_UNS;
}

static void setaudio(struct xmp_options *o)
{
	static int fragset = 0;
	int frag = 0;
	int fmt;

	frag = (fragnum << 16) + fragsize;

	fmt = to_fmt(o);
	ioctl(audio_fd, SNDCTL_DSP_SETFMT, &fmt);
	from_fmt(o, fmt);

	fmt = !(o->outfmt & XMP_FMT_MONO);
	ioctl(audio_fd, SNDCTL_DSP_STEREO, &fmt);
	if (fmt)
		o->outfmt &= ~XMP_FMT_MONO;
	else
		o->outfmt |= XMP_FMT_MONO;

	ioctl(audio_fd, SNDCTL_DSP_SPEED, &o->freq);

	/* Set the fragments only once */
	if (!fragset) {
		if (fragnum && fragsize)
			ioctl(audio_fd, SNDCTL_DSP_SETFRAGMENT, &frag);
		fragset++;
	}
}

static int init(struct xmp_context *ctx)
{
	char *dev_audio[] = { "/dev/dsp", "/dev/sound/dsp" };
	struct xmp_options *o = &ctx->o;

#ifdef HAVE_AUDIO_BUF_INFO
	audio_buf_info info;
	static char buf[80];
#endif
	char *token, **parm;
	int i;

	fragnum = 16;		/* default number of fragments */
	i = 1024;		/* default size of fragment */
	
	parm_init();
	chkparm2("frag", "%d,%d", &fragnum, &i);
	chkparm1("dev", dev_audio[0] = token);
#ifdef HAVE_AUDIO_BUF_INFO
	chkparm0("voxware", voxware = 1);
#endif
	chkparm0("nosync", do_sync = 0);
	parm_end();

	for (fragsize = 0; i >>= 1; fragsize++) ;
	if (fragsize < 4)
		fragsize = 4;

	for (i = 0; i < sizeof(dev_audio) / sizeof(dev_audio[0]); i++)
		if ((audio_fd = open(dev_audio[i], O_WRONLY)) >= 0)
			break;
	if (audio_fd < 0)
		return XMP_ERR_DINIT;

	setaudio(o);

#ifdef HAVE_AUDIO_BUF_INFO
	if (!voxware) {
		if (ioctl(audio_fd, SNDCTL_DSP_GETOSPACE, &info) == 0) {
			snprintf(buf, 80, "%s [%d fragments of %d bytes]",
				 drv_oss.description, info.fragstotal,
				 info.fragsize);
			drv_oss.description = buf;
		}
	}
#endif

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
			b = (char *)b + j;
		} else
			break;
	};
}

static void shutdown(struct xmp_context *ctx)
{
	xmp_smix_off(ctx);
	ioctl(audio_fd, SNDCTL_DSP_RESET, NULL);
	close(audio_fd);
}

static void flush()
{
	if (!do_sync)
		return;

	ioctl(audio_fd, SNDCTL_DSP_SYNC, NULL);
}
