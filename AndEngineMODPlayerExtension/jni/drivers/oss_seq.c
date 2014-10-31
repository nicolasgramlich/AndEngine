/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 * AWE32 support Copyright (C) 1996, 1997 Takashi Iwai
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#define USE_SEQ_MACROS

#include <string.h>
#include <stdlib.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/ioctl.h>

#ifdef HAVE_SYS_PARAM_H
#include <sys/param.h>
#endif

#if defined(HAVE_SYS_SOUNDCARD_H)
#include <sys/soundcard.h>
#elif defined(HAVE_MACHINE_SOUNDCARD_H)
#include <machine/soundcard.h>
#endif

#if defined(HAVE_SYS_ULTRASOUND_H)
#include <sys/ultrasound.h>
#elif defined(HAVE_LINUX_ULTRASOUND_H)
#include <linux/ultrasound.h>
#elif defined(HAVE_MACHINE_ULTRASOUND_H)
#include <machine/ultrasound.h>
#endif

#if defined(HAVE_AWE_VOICE_H)
#include <awe_voice.h>
#elif defined(HAVE_SYS_AWE_VOICE_H)
#include <sys/awe_voice.h>
#elif defined(HAVE_LINUX_AWE_VOICE_H)
#include <linux/awe_voice.h>
#endif

#include "common.h"
#include "driver.h"

#if !defined (DISABLE_AWE) && (defined (HAVE_AWE_VOICE_H) || \
	defined (HAVE_SYS_AWE_VOICE_H) || defined (HAVE_LINUX_AWE_VOICE_H))
#define AWE_DEVICE
#endif

#define SEQ_NUM_VOICES	32	/* FIXME? */

SEQ_DEFINEBUF(2048);

static int seqfd = -1, hz;
static int echo_msg;

static int numvoices(struct xmp_context *, int);
static void voicepos(int, int);
static void echoback(struct xmp_context *, int);
static void setpatch(int, int);
static void setvol(struct xmp_context *, int, int);
static void setnote(int, int);
static void setpan(struct xmp_context *, int, int);
static void setbend(int, int);
static void seteffect(struct xmp_context *, int, int, int);
static void starttimer(void);
static void stoptimer(void);
static void resetvoices(void);
static void bufdump(void);
static void bufwipe(void);
static void clearmem(void);
static void seq_sync(double);
static int writepatch(struct xmp_context *, struct patch_info *);
static int init(struct xmp_context *);
static int getmsg(void);
static void shutdown(struct xmp_context *);

#define seqbuf_dump bufdump

static char *help[] = {
	"awechorus=mode", "Set chorus mode in AWE cards",
	"awereverb=mode", "Set reverb mode in AWE cards",
	"dev=<device_name>", "Device to use (default /dev/sequencer)",
	"opl2", "Use OPL2 (YM3812) FM synthesizer",
	NULL
};

struct xmp_drv_info drv_oss_seq = {
	"oss_seq",		/* driver ID */
	"OSS sequencer",	/* driver description */
	help,			/* help */
	init,			/* init */
	shutdown,		/* shutdown */
	numvoices,		/* numvoices */
	voicepos,		/* voicepos */
	echoback,		/* echoback */
	setpatch,		/* setpatch */
	setvol,			/* setvol */
	setnote,		/* setnote */
	setpan,			/* setpan */
	setbend,		/* setbend */
	seteffect,		/* seteffect */
	starttimer,		/* settimer */
	stoptimer,		/* stoptimer */
	resetvoices,		/* resetvoices */
	bufdump,		/* bufdump */
	bufwipe,		/* bufwipe */
	clearmem,		/* clearmem */
	seq_sync,		/* sync */
	writepatch,		/* writepatch */
	getmsg,			/* getmsg */
	NULL
};

static int dev;
static struct synth_info si;
static int chorusmode = 0;
static int reverbmode = 0;
static char *dev_sequencer = "/dev/sequencer";
static struct xmp_player_context *p_ctx;

static int numvoices(struct xmp_context *ctx, int num)
{
	switch (si.synth_subtype) {
	case SAMPLE_TYPE_GUS:
		if (num < 14)
			num = 14;
#ifdef AWE_DEVICE
	case SAMPLE_TYPE_AWE32:
#endif
		if (num > SEQ_NUM_VOICES)
			return SEQ_NUM_VOICES;
		GUS_NUMVOICES(dev, num);
		break;
	}

	return num;
}

static void voicepos(int ch, int pos)
{
	GUS_VOICE_POS(dev, ch, pos);
}

static void echoback(struct xmp_context *ctx, int msg)
{
	SEQ_ECHO_BACK(msg);
}

static void setpatch(int ch, int smp)
{
	SEQ_SET_PATCH(dev, ch, smp);
}

static void setvol(struct xmp_context *ctx, int ch, int vol)
{
	SEQ_START_NOTE(dev, ch, 255, vol >> 4);
}

static void setnote(int ch, int note)
{
	SEQ_START_NOTE(dev, ch, note, 0);
}

static void seteffect(struct xmp_context *ctx, int ch, int type, int val)
{
#ifdef AWE_DEVICE
	if (si.synth_subtype == SAMPLE_TYPE_AWE32) {
		switch (type) {
		case XMP_FX_CHORUS:
			AWE_SEND_EFFECT(dev, ch, AWE_FX_CHORUS, val);
			break;
		case XMP_FX_REVERB:
			AWE_SEND_EFFECT(dev, ch, AWE_FX_REVERB, val);
			break;
		case XMP_FX_CUTOFF:
			AWE_SEND_EFFECT(dev, ch, AWE_FX_CUTOFF, val);
			break;
		case XMP_FX_RESONANCE:
			AWE_SEND_EFFECT(dev, ch, AWE_FX_FILTERQ, val / 16);
			break;
		}
	}
#endif
}

static void setpan(struct xmp_context *ctx, int ch, int pan)
{
	GUS_VOICEBALA(dev, ch, (pan + 0x80) >> 4)
}

static void setbend(int ch, int bend)
{
	SEQ_PITCHBEND(dev, ch, bend);
}

static void starttimer()
{
	SEQ_START_TIMER();
	seq_sync(0);
	bufdump();
}

static void stoptimer()
{
	SEQ_STOP_TIMER();
	bufdump();
}

static void resetvoices()
{
	int i;

#ifdef AWE_DEVICE
	if (si.synth_subtype == SAMPLE_TYPE_AWE32) {
		AWE_CHORUS_MODE(dev, chorusmode);
		AWE_REVERB_MODE(dev, reverbmode);
	}
#endif
	for (i = 0; i < SEQ_NUM_VOICES; i++) {
		SEQ_STOP_NOTE(dev, i, 255, 0);
		SEQ_EXPRESSION(dev, i, 255);
		SEQ_MAIN_VOLUME(dev, i, 100);
		SEQ_CONTROL(dev, i, CTRL_PITCH_BENDER_RANGE, 8191);
		SEQ_BENDER(dev, i, 0);
		SEQ_PANNING(dev, i, 0);
		bufdump();
	}
}

static void bufwipe()
{
	bufdump();
	ioctl(seqfd, SNDCTL_SEQ_RESET, 0);
	_seqbufptr = 0;
}

static void bufdump()
{
	int i, j;
	fd_set rfds, wfds;

	FD_ZERO(&rfds);
	FD_ZERO(&wfds);

	do {
		FD_SET(seqfd, &rfds);
		FD_SET(seqfd, &wfds);
		select(seqfd + 1, &rfds, &wfds, NULL, NULL);

		if (FD_ISSET(seqfd, &rfds)) {
			if ((read(seqfd, &echo_msg, 4) == 4) &&
			    ((echo_msg & 0xff) == SEQ_ECHO)) {
				echo_msg >>= 8;
				p_ctx->event_callback(echo_msg, p_ctx->callback_data);
			} else
				echo_msg = 0;	/* ECHO_NONE */
		}

		if (FD_ISSET(seqfd, &wfds) && ((j = _seqbufptr) != 0)) {
			if ((i = write(seqfd, _seqbuf, _seqbufptr)) == -1) {
				fprintf(stderr,
					"xmp: can't write to sequencer\n");
				exit(-4);
			} else if (i < j) {
				_seqbufptr -= i;
				memmove(_seqbuf, _seqbuf + i, _seqbufptr);
			} else
				_seqbufptr = 0;
		}
	} while (_seqbufptr);
}

static void clearmem()
{
	int i = dev;

	ioctl(seqfd, SNDCTL_SEQ_RESETSAMPLES, &i);
}

static void seq_sync(double next_time)
{
	static double this_time = 0;

	if (next_time == 0)
		this_time = 0;

	if (next_time - this_time < 1.0)
		return;

	if (next_time > this_time) {
		SEQ_WAIT_TIME(next_time * hz / 100);
		this_time = next_time;
	}
}

static int writepatch(struct xmp_context *ctx, struct patch_info *patch)
{
	struct sbi_instrument sbi;
	struct xmp_options *o = &ctx->o;

	if (!patch) {
		clearmem();
		return 0;
	}

	if ((!!(o->outfmt & XMP_FMT_FM)) ^ (patch->len == XMP_PATCH_FM))
		return XMP_ERR_PATCH;

	patch->device_no = dev;
	if (patch->len == XMP_PATCH_FM) {
		sbi.key = FM_PATCH;
		sbi.device = dev;
		sbi.channel = patch->instr_no;
		memcpy(&sbi.operators, patch->data, 11);
		write(seqfd, &sbi, sizeof(sbi));

		return 0;
	}
	SEQ_WRPATCH(patch, sizeof(struct patch_info) + patch->len - 1);

	return 0;
}

static int getmsg()
{
	return echo_msg;
}

static int init(struct xmp_context *ctx)
{
	int found;
	char *buf, *token;
	char **parm;
	struct xmp_driver_context *d = &ctx->d;
	struct xmp_options *o = &ctx->o;

	parm_init();
	chkparm1("awechorus", chorusmode = strtoul(token, NULL, 0));
	chkparm1("awereverb", reverbmode = strtoul(token, NULL, 0));
	chkparm0("opl2", o->outfmt |= XMP_FMT_FM);
	chkparm1("dev", dev_sequencer = token);
	parm_end();

	p_ctx = &ctx->p;

	if ((seqfd = open(dev_sequencer, O_RDWR)) != -1) {
		if ((seqfd != -1)
		    && (ioctl(seqfd, SNDCTL_SEQ_NRSYNTHS, &dev) == -1)) {
			fprintf(stderr,
				"xmp: can't determine number of synths\n");
			return XMP_ERR_DINIT;
		}
	} else {
		if (o->verbosity > 2)
			fprintf(stderr, "xmp: can't open sequencer\n");
		return XMP_ERR_DINIT;
	}

	for (found = 0; dev--;) {
		si.device = dev;

		if (ioctl(seqfd, SNDCTL_SYNTH_INFO, &si) == -1) {
			fprintf(stderr, "xmp: can't determine synth info\n");
			return XMP_ERR_DINIT;
		}

		if (si.synth_type == (o->outfmt & XMP_FMT_FM ?
				      SYNTH_TYPE_FM : SYNTH_TYPE_SAMPLE)) {
			if (si.synth_type != SYNTH_TYPE_FM) {
				int i = dev;
				ioctl(seqfd, SNDCTL_SEQ_RESETSAMPLES, &i);
				i = dev;
				ioctl(seqfd, SNDCTL_SYNTH_MEMAVL, &i);
				if (!i)
					continue;
				d->memavl = i;
			}

			buf = calloc(1, 256);
			sprintf(buf, "%s [%s]", drv_oss_seq.description,
				si.name);

#if defined AWE_DEVICE && defined HAVE_AWE_MD_NEW_VOLUME_CALC
			if (si.synth_subtype == SAMPLE_TYPE_AWE32)
				AWE_MISC_MODE(dev, AWE_MD_NEW_VOLUME_CALC, 0);
#endif

			drv_oss_seq.description = buf;

			found = 1;
			break;
		}
	}

	if (!found) {
		close(seqfd);
		return XMP_ERR_DINIT;
	}

	hz = 0;
	ioctl(seqfd, SNDCTL_SEQ_CTRLRATE, &hz);	/* Always 100 in ALSA */
	SEQ_VOLUME_MODE(dev, VOL_METHOD_LINEAR);
	bufdump();
	ioctl(seqfd, SNDCTL_SEQ_SYNC, 0);

	return 0;
}

static void shutdown(struct xmp_context *ctx)
{
	free(drv_oss_seq.description);
	close(seqfd);
}
