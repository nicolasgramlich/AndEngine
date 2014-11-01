/* Extended Module Player ALSA sequencer driver
 * Copyright (C) 2007 Claudio Matsuoka and Hipolito Carraro Jr
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
#include <alsa/asoundlib.h>
#include <linux/awe_voice.h>
#include <math.h>

#include "common.h"
#include "driver.h"


static snd_seq_t *seq;
static int queue;
static int my_client, my_port;
static int dest_client, dest_port;
static int nvoices;
static snd_hwdep_t *hwdep;

static int numvoices	(struct xmp_context *, int);
static void voicepos	(int, int);
static void echoback	(struct xmp_context *, int);
static void setpatch	(int, int);
static void setvol	(int, int);
static void setnote	(int, int);
static void setpan	(int, int);
static void setbend	(int, int);
static void seteffect	(int, int, int);
static void starttimer	(void);
static void stoptimer	(void);
static void resetvoices	(void);
static void bufdump	(void);
static void bufwipe	(void);
static void clearmem	(void);
static void seq_sync	(double);
static int writepatch	(struct patch_info *);
static int init(struct xmp_context *ctx);
static int getmsg	(void);
static void shutdown	(struct xmp_context *);

static char *help[] = {
	"addr=port", "Set ALSA sequencer port",
	NULL
};

struct xmp_drv_info drv_alsa_seq = {
	"alsa_seq",		/* driver ID */
	"ALSA sequencer",	/* driver description */
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

//static int chorusmode = 0;
//static int reverbmode = 0;


/* From Takashi Iwai's asfxload */

#define SNDRV_EMUX_HWDEP_NAME   "Emux WaveTable"

/* Shouldn't these be in a system header? */
struct sndrv_emux_misc_mode {
	int port;       /* -1 = all */
	int mode;
	int value;
	int value2;     /* reserved */
};

enum {
	SNDRV_EMUX_IOCTL_VERSION = _IOR('H', 0x80, unsigned int),
	SNDRV_EMUX_IOCTL_LOAD_PATCH = _IOWR('H', 0x81, awe_patch_info),
	SNDRV_EMUX_IOCTL_RESET_SAMPLES = _IO('H', 0x82),
	SNDRV_EMUX_IOCTL_REMOVE_LAST_SAMPLES = _IO('H', 0x83),
	SNDRV_EMUX_IOCTL_MEM_AVAIL = _IOW('H', 0x84, int),
	SNDRV_EMUX_IOCTL_MISC_MODE = _IOWR('H', 0x84,
						struct sndrv_emux_misc_mode),
};

static int try_open_emux(char *name)
{
	snd_hwdep_info_t *info;
	unsigned int version;

	if (snd_hwdep_open(&hwdep, name, 0) < 0)
		return -1;

	snd_hwdep_info_alloca(&info);

	if (snd_hwdep_info(hwdep, info) < 0)
		goto error;

	if (strcmp(snd_hwdep_info_get_name(info), SNDRV_EMUX_HWDEP_NAME))
		goto error;

	if (snd_hwdep_ioctl(hwdep, SNDRV_EMUX_IOCTL_VERSION, &version) < 0)
		goto error;

	if ((version >> 16) != 0x01) /* version 1 compatible */
		goto error;

	return 0;

error:
	snd_hwdep_close(hwdep);
	hwdep = NULL;
	return -1;
}

static int open_emux(char *name)
{
	char tmpname[32];
	int i;

	if (name == NULL || ! *name) {
		for (i = 0; i < 8; i++) {
			sprintf(tmpname, "hw:%d,2", i);
			if (try_open_emux(tmpname) == 0)
				return 0;
		}
	} else {
		if (try_open_emux(name) == 0)
			return 0;
	}

	return -1;
}







static void midi_send(snd_seq_event_t *ev)
{
	snd_seq_ev_set_direct(ev);
	snd_seq_ev_set_source(ev, my_port);
	snd_seq_ev_set_dest(ev, dest_client, dest_port);
	snd_seq_event_output(seq, ev);
	snd_seq_drain_output(seq);
}


static int numvoices(struct xmp_context *ctx, int num)
{
	return nvoices;
}


static void voicepos(int ch, int pos)
{
	//GUS_VOICE_POS (dev, ch, pos);
}


static void echoback(struct xmp_context *ctx, int msg)
{
	//SEQ_ECHO_BACK (msg);
}


static void setpatch(int ch, int smp)
{
	snd_seq_event_t ev;

	snd_seq_ev_set_pgmchange(&ev, ch, smp);
	midi_send(&ev);
}


static void setvol(int ch, int vol)
{
	snd_seq_event_t ev;

	snd_seq_ev_set_noteon(&ev, ch, 255, vol);	/* 255 works here? */
	midi_send(&ev);
}


static void setnote(int ch, int note)
{
	snd_seq_event_t ev;

	snd_seq_ev_set_noteon(&ev, ch, note, 0);
	midi_send(&ev);
}


static void seteffect(int ch, int type, int val)
{
	snd_seq_event_t ev;

	switch (type) {
	case XMP_FX_CHORUS:
		snd_seq_ev_set_controller(&ev, ch,
					MIDI_CTL_E3_CHORUS_DEPTH, val);
		break;
	case XMP_FX_REVERB:
		snd_seq_ev_set_controller(&ev, ch,
					MIDI_CTL_E1_REVERB_DEPTH, val);
		break;
	case XMP_FX_CUTOFF:
		//snd_seq_ev_set_controller(&ev, ch, , val);
		break;
	case XMP_FX_RESONANCE:
		//snd_seq_ev_set_controller(&ev, ch, , val);
		break;
	}

	midi_send(&ev);
}


static void setpan(int ch, int pan)
{
	snd_seq_event_t ev;

	snd_seq_ev_set_controller(&ev, ch, MIDI_CTL_MSB_PAN, pan);
	midi_send(&ev);
}


static void setbend(int ch, int bend)
{
	snd_seq_event_t ev;

	/* pitch bend; zero centered from -8192 to 8191 */
	snd_seq_ev_set_pitchbend(&ev, ch, bend);
	midi_send(&ev);
}


static void starttimer ()
{
	snd_seq_start_queue(seq, queue, 0);
	seq_sync(0);
	bufdump();
}


static void stoptimer ()
{
	snd_seq_stop_queue(seq, queue, 0);
	bufdump();
}


static void resetvoices ()
{
	snd_hwdep_ioctl(hwdep, SNDRV_EMUX_IOCTL_RESET_SAMPLES, NULL);	// ??
#if 0
    int i;

#ifdef AWE_DEVICE
    if (si.synth_subtype == SAMPLE_TYPE_AWE32) {
	AWE_CHORUS_MODE (dev, chorusmode);
	AWE_REVERB_MODE (dev, reverbmode);
    }
#endif
    for (i = 0; i < SEQ_NUM_VOICES; i++) {
	SEQ_STOP_NOTE (dev, i, 255, 0);
	SEQ_EXPRESSION (dev, i, 255);
	SEQ_MAIN_VOLUME (dev, i, 100);
	SEQ_CONTROL (dev, i, CTRL_PITCH_BENDER_RANGE, 8191);
	SEQ_BENDER (dev, i, 0);
	SEQ_PANNING (dev, i, 0);
	bufdump ();
    }
#endif
}


static void bufwipe ()
{
#if 0
    bufdump ();
    ioctl (seqfd, SNDCTL_SEQ_RESET, 0);
    _seqbufptr = 0;
#endif
}


static void bufdump ()
{
#if 0
    int i, j;
    fd_set rfds, wfds;

    FD_ZERO (&rfds);
    FD_ZERO (&wfds);

    do {
	FD_SET (seqfd, &rfds);
	FD_SET (seqfd, &wfds);
	select (seqfd + 1, &rfds, &wfds, NULL, NULL);

	if (FD_ISSET (seqfd, &rfds)) {
	    if ((read (seqfd, &echo_msg, 4) == 4) &&
		((echo_msg & 0xff) == SEQ_ECHO)) {
		echo_msg >>= 8;
		xmp_event_callback (echo_msg);
	    } else
		echo_msg = 0;		/* ECHO_NONE */
	}

	if (FD_ISSET (seqfd, &wfds) && ((j = _seqbufptr) != 0)) {
	    if ((i = write (seqfd, _seqbuf, _seqbufptr)) == -1) {
		fprintf (stderr, "xmp: can't write to sequencer\n");
		exit (-4);
	    } else if (i < j) {
		_seqbufptr -= i;
		memmove (_seqbuf, _seqbuf + i, _seqbufptr);
	    } else
		_seqbufptr = 0;
	}
    } while (_seqbufptr);
#endif
}


static void clearmem()
{
	snd_hwdep_ioctl(hwdep, SNDRV_EMUX_IOCTL_REMOVE_LAST_SAMPLES, NULL);
}


static void seq_sync(double next_time)
{
	snd_seq_real_time_t t;
	snd_seq_event_t ev;
	double iptr;

	t.tv_sec = next_time / 100;
	t.tv_nsec = modf(next_time / 100, &iptr) * 1e9;
	snd_seq_ev_schedule_real(&ev, queue, 0, &t);
	midi_send(&ev);
}


/* FIXME: convert GUS patch to AWE patch */
static int writepatch(struct patch_info *patch)
{
	awe_patch_info *p;

	p = (awe_patch_info*)patch;
	p->key = AWE_PATCH;
	p->device_no = 0;
	p->sf_id = 0;
	snd_hwdep_ioctl(hwdep, SNDRV_EMUX_IOCTL_LOAD_PATCH, p);

	return 0;
}


static int getmsg()
{
	return 0;
	//return echo_msg;
}


static int init(struct xmp_context *ctx)
{
	snd_seq_addr_t d;
	char *token, **parm;
	char *addr = "";
	int err;

	parm_init();
	chkparm1("addr", addr = token);
	parm_end();

	if (snd_seq_open(&seq, "default", SND_SEQ_OPEN_DUPLEX, 0) < 0) {
		if (ctl->verbosity > 2) {
			fprintf(stderr, "xmp: can't open sequencer: %s",
						snd_strerror(errno));
		}
		return XMP_ERR_DINIT;
	}

	// FIXME
	if (open_emux(addr) < 0) {
		fprintf(stderr, "xmp: no Emux synth hwdep device found\n");
		goto error;
	}

        if (snd_seq_parse_address(seq, &d, addr) < 0) {
		fprintf(stderr, "xmp: can't parse address: %s", addr);
		goto error;
	}

	dest_client = d.client;
	dest_port = d.port;

	my_client = snd_seq_client_id(seq);
	snd_seq_set_client_name(seq, "xmp");

	my_port = snd_seq_create_simple_port(seq, NULL,
		SND_SEQ_PORT_CAP_WRITE | SND_SEQ_PORT_CAP_SUBS_WRITE |
		SND_SEQ_PORT_CAP_READ, SND_SEQ_PORT_TYPE_MIDI_GENERIC);

	if (my_port < 0) {
		fprintf(stderr, "xmp: can't create port: %s",
						snd_strerror(errno));
		goto error;
	}

        err = snd_seq_connect_to(seq, my_port, dest_client, dest_port);
        if (err < 0) {
                fprintf(stderr, "xmp: can't connect to %d:%d (%s)\n",
				dest_client, dest_port, strerror(-err));
		goto error;
        }

	queue = snd_seq_alloc_queue(seq);
	
	bufdump ();

	return 0;

error:
	snd_seq_close(seq);
	return XMP_ERR_DINIT;
}


static void shutdown(struct xmp_context *ctx)
{
	snd_seq_free_queue(seq, queue);
	snd_hwdep_close(hwdep);
	snd_seq_close(seq);
}
