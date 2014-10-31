/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifndef __XMP_H
#define __XMP_H

#define XMP_NAMESIZE		64

#define XMP_KEY_OFF		0x81
#define XMP_KEY_CUT		0x82
#define XMP_KEY_FADE		0x83

/* DSP effects */
#define XMP_FX_CHORUS		0x00
#define XMP_FX_REVERB		0x01
#define XMP_FX_CUTOFF		0x02
#define XMP_FX_RESONANCE	0x03
#define XMP_FX_FILTER_B0	0xb0
#define XMP_FX_FILTER_B1	0xb1
#define XMP_FX_FILTER_B2	0xb2

/* Event echo messages */
#define XMP_ECHO_NONE		0x00
#define XMP_ECHO_END		0x01
#define XMP_ECHO_BPM		0x02
#define XMP_ECHO_VOL		0x03
#define XMP_ECHO_INS		0x04
#define XMP_ECHO_ORD		0x05
#define XMP_ECHO_ROW		0x06
#define XMP_ECHO_CHN		0x07
#define XMP_ECHO_PBD		0x08
#define XMP_ECHO_GVL		0x09
#define XMP_ECHO_NCH		0x0a
#define XMP_ECHO_FRM		0x0b
#define XMP_ECHO_TIME		0x0c

/* xmp_player_ctl arguments */
#define XMP_ORD_NEXT		0x00
#define XMP_ORD_PREV		0x01
#define XMP_ORD_SET		0x02
#define XMP_MOD_STOP		0x03
#define XMP_MOD_RESTART		0x04
#define XMP_GVOL_INC		0x05
#define XMP_GVOL_DEC		0x06
#define XMP_TIMER_STOP          0x07
#define XMP_TIMER_RESTART       0x08

/* Format quirks */
#define XMP_QUIRK_ST3		(XMP_QRK_NCWINS | XMP_QRK_IGNWINS | \
				 XMP_QRK_S3MLOOP | XMP_QRK_RTGINS | \
				 XMP_QRK_VOLPDN)
#define XMP_QUIRK_FT2		(XMP_QRK_OINSMOD | XMP_QRK_CUTNWI | \
				 XMP_QRK_OFSRST)
#define XMP_QUIRK_IT		(XMP_QRK_NCWINS | XMP_QRK_INSPRI | \
				 XMP_QRK_ENVFADE | XMP_QRK_S3MLOOP | \
				 XMP_QRK_OFSRST | XMP_QRK_ITENV | \
				 XMP_QRK_VOLPDN | XMP_QRK_RTGINS | \
				 XMP_QRK_SAVEINS | XMP_QRK_ITVPOR)

/* Player control macros */
#define xmp_ord_next(p)		xmp_player_ctl((p), XMP_ORD_NEXT, 0)
#define xmp_ord_prev(p)		xmp_player_ctl((p), XMP_ORD_PREV, 0)
#define xmp_ord_set(p,x)	xmp_player_ctl((p), XMP_ORD_SET, x)
#define xmp_mod_stop(p)		xmp_player_ctl((p), XMP_MOD_STOP, 0)
#define xmp_stop_module(p)	xmp_player_ctl((p), XMP_MOD_STOP, 0)
#define xmp_mod_restart(p)	xmp_player_ctl((p), XMP_MOD_RESTART, 0)
#define xmp_restart_module(p)	xmp_player_ctl((p), XMP_MOD_RESTART, 0)
#define xmp_timer_stop(p)	xmp_player_ctl((p), XMP_TIMER_STOP, 0)
#define xmp_timer_restart(p)	xmp_player_ctl((p), XMP_TIMER_RESTART, 0)
#define xmp_gvol_inc(p)		xmp_player_ctl((p), XMP_GVOL_INC, 0)
#define xmp_gvol_dec(p)		xmp_player_ctl((p), XMP_GVOL_DEC, 0)
#define xmp_mod_load		xmp_load_module
#define xmp_mod_test		xmp_test_module
#define xmp_mod_play		xmp_play_module

/* Error messages */
#define XMP_ERR_NOCTL		-1
#define XMP_ERR_NODRV		-2
#define XMP_ERR_DSPEC		-3
#define XMP_ERR_DOPEN		-4
#define XMP_ERR_DINIT		-5
#define XMP_ERR_PATCH		-6
#define XMP_ERR_VIRTC		-7
#define XMP_ERR_ALLOC		-8

struct xmp_options {
	int big_endian;		/* Machine byte order */
	char *drv_id;		/* Driver ID */
	char *outfile;		/* Output file name when mixing to file */
	int verbosity;		/* Verbosity level */
#define XMP_FMT_FM	(1 << 0)	/* Active mode FM */
#define XMP_FMT_UNS	(1 << 1)	/* Unsigned samples */
#define XMP_FMT_MONO	(1 << 2)	/* Mono output */
	int amplify;		/* Software mixing amplify volume:
				   0 = none, 1 = x2, 2 = x4, 3 = x8 */
	int outfmt;		/* Software mixing output data format */
	int resol;		/* Software mixing resolution output */
	int freq;		/* Software mixing rate (Hz) */
#define XMP_CTL_ITPT	(1 << 0)	/* Mixer interpolation */
#define XMP_CTL_REVERSE	(1 << 1)	/* Reverse stereo */
#define XMP_CTL_8BIT	(1 << 2)	/* Convert 16 bit samples to 8 bit */
#define XMP_CTL_LOOP	(1 << 3)	/* Enable module looping */
#define XMP_CTL_VBLANK	(1 << 4)	/* Use vblank timing only */
#define XMP_CTL_VIRTUAL	(1 << 5)	/* Enable virtual channels */
#define XMP_CTL_DYNPAN	(1 << 6)	/* Enable dynamic pan */
#define XMP_CTL_FIXLOOP	(1 << 7)	/* Fix sample loop start */
#define XMP_CTL_FILTER	(1 << 8)	/* IT lowpass filter */
	int flags;		/* internal control flags, set default mode */
	/* Format quirks */
#define XMP_QRK_MEDBPM	(1 << 0)	/* Enable MED BPM timing */
#define XMP_QRK_S3MLOOP	(1 << 1)	/* S3M loop mode */
#define XMP_QRK_ENVFADE	(1 << 2)	/* Fade at end of envelope */
#define XMP_QRK_ITENV	(1 << 3)	/* IT envelope mode */
#define XMP_QRK_IGNWINS	(1 << 4)	/* Ignore invalid instrument */
#define XMP_QRK_NCWINS	(1 << 5)	/* Don't cut invalid instrument */
#define XMP_QRK_INSPRI	(1 << 6)	/* Reset note for every new != ins */
#define XMP_QRK_CUTNWI	(1 << 7)	/* Cut only when note + invalid ins */
#define XMP_QRK_OINSMOD	(1 << 8)	/* XM old instrument mode */
#define XMP_QRK_OFSRST	(1 << 9)	/* Always reset sample offset */
#define XMP_QRK_FX9BUG	(1 << 10)	/* Protracker effect 9 bug emulation */
#define XMP_QRK_ST3GVOL	(1 << 11)	/* ST 3 weird global volume effect */
#define XMP_QRK_FINEFX	(1 << 12)	/* Enable 0xf/0xe for fine effects */
#define XMP_QRK_VSALL	(1 << 13)	/* Volume slides in all frames */
#define XMP_QRK_RTGINS	(1 << 14)	/* Retrig instrument on toneporta */
#define XMP_QRK_PBALL	(1 << 15)	/* Pitch bending in all frames */
#define XMP_QRK_PERPAT	(1 << 16)	/* Cancel persistent fx at pat start */
#define XMP_QRK_VOLPDN	(1 << 17)	/* Set priority to volume slide down */
#define XMP_QRK_UNISLD	(1 << 18)	/* Unified pitch slide/portamento */
#define XMP_QRK_SAVEINS	(1 << 19)	/* Always save instrument number */
#define XMP_QRK_ITVPOR	(1 << 20)	/* Disable fine bends in IT vol fx */
	int quirk;		/* extra control flags */
	int crunch;		/* Sample crunching ratio */
	int start;		/* Set initial order (default = 0) */
	int mix;		/* Percentage of L/R channel separation */
	int time;		/* Maximum playing time in seconds */
	int tempo;		/* Set initial tempo */
	int chorus;		/* Chorus level */
	int reverb;		/* Reverb leval */
	int skipsmp;		/* Don't load sample data */
	int cf_cutoff;		/* Cutoff for filter-based anticlick */
	char *parm[16];		/* Driver parameter data */
};

struct xmp_fmt_info {
	struct xmp_fmt_info *next;
	char *id;
	char *tracker;
};

struct xmp_drv_info {
	char *id;
	char *description;
	char **help;
	int (*init) ();
	void (*shutdown) ();
	int (*numvoices) ();
	void (*voicepos) ();
	void (*echoback) ();
	void (*setpatch) ();
	void (*setvol) ();
	void (*setnote) ();
	void (*setpan) ();
	void (*setbend) ();
	void (*seteffect) ();
	void (*starttimer) ();
	void (*stoptimer) ();
	void (*reset) ();
	void (*bufdump) ();
	void (*bufwipe) ();
	void (*clearmem) ();
	void (*sync) ();
	int (*writepatch) ();
	int (*getmsg) ();
	void *data;
	struct xmp_drv_info *next;
};

struct xmp_module_info {
	char name[0x40];
	char type[0x40];
	int chn;
	int pat;
	int ins;
	int trk;
	int smp;
	int len;
	int bpm;
	int tpo;
	int time;
};

typedef char *xmp_context;

extern char *global_filename;	/* FIXME: hack for the wav driver */

void *xmp_create_context(void);
void xmp_free_context(xmp_context);
void xmp_unlink_tempfiles(void);
struct xmp_options *xmp_get_options(xmp_context);
int xmp_get_flags(xmp_context);
void xmp_set_flags(xmp_context, int);

void xmp_init(xmp_context, int, char **);
void xmp_deinit(xmp_context);
void xmp_drv_register(struct xmp_drv_info *);
int xmp_load_module(xmp_context, char *);
int xmp_test_module(xmp_context, char *, char *);
struct xmp_module_info *xmp_get_module_info(xmp_context, struct xmp_module_info *);
struct xmp_fmt_info *xmp_get_fmt_info(struct xmp_fmt_info **);
struct xmp_drv_info *xmp_get_drv_info(struct xmp_drv_info **);
char *xmp_get_driver_description(xmp_context);
void xmp_set_driver_parameter(struct xmp_options *, char *);
void xmp_get_driver_cfg(xmp_context, int *, int *, int *, int *);
void xmp_channel_mute(xmp_context, int, int, int);
void xmp_register_event_callback(xmp_context, void (*)(unsigned long, void *), void *);
int xmp_player_ctl(xmp_context, int, int);
int xmp_open_audio(xmp_context);
void xmp_close_audio(xmp_context);
int xmp_play_module(xmp_context);
int xmp_player_start(xmp_context);
int xmp_player_frame(xmp_context);
void xmp_player_end(xmp_context);
void xmp_play_buffer(xmp_context);
void xmp_get_buffer(xmp_context, void **, int *);
void xmp_release_module(xmp_context);
int xmp_verbosity_level(xmp_context, int);
int xmp_seek_time(xmp_context, int);
void xmp_init_formats(xmp_context);
void xmp_deinit_formats(xmp_context);
int xmp_enable_format(char *, int);
int xmp_checksum(char *, unsigned int *, unsigned int *);

#endif	/* __XMP_H */
