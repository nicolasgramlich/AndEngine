/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifndef __XMP_PLAYER_H
#define __XMP_PLAYER_H

/* Quirk control */
#define HAS_QUIRK(x)	(m->quirk & (x))

/* Channel flag control */
#define SET(f)		SET_FLAG(xc->flags,f)
#define RESET(f) 	RESET_FLAG(xc->flags,f)
#define TEST(f)		TEST_FLAG(xc->flags,f)

/* Persistent effect flag control */
#define SET_PER(f)	SET_FLAG(xc->per_flags,f)
#define RESET_PER(f)	RESET_FLAG(xc->per_flags,f)
#define TEST_PER(f)	TEST_FLAG(xc->per_flags,f)

/* Global flags */
#define PATTERN_BREAK	0x0001 
#define PATTERN_LOOP	0x0002 
#define MODULE_ABORT	0x0004 
#define MODULE_RESTART	0x0008 

#define XXIH p->m.xxih[xc->ins]
#define XXIM p->m.xxim[xc->ins]
#define XXAE p->m.xxae[xc->ins]
#define XXPE p->m.xxpe[xc->ins]
#define XXFE p->m.xxfe[xc->ins]
#define XXI p->m.xxi[xc->ins]

#define DOENV_RELEASE	((TEST (RELEASE) || act == XMP_ACT_OFF))

#define ARP_SIZE 4

struct retrig_t {
    int s;
    int m;
    int d;
};

/* The following macros are used to set the flags for each channel */
#define VOL_SLIDE	0x00000001
#define PAN_SLIDE	0x00000002
#define TONEPORTA	0x00000004
#define PITCHBEND	0x00000008
#define VIBRATO		0x00000010
#define TREMOLO		0x00000020
#define FINE_VOLS	0x00000040
#define FINE_BEND	0x00000080
#define NEW_PAN		0x00000100
#define FINETUNE	0x00000200
#define OFFSET		0x00000400
#define TRK_VSLIDE	0x00000800
#define TRK_FVSLIDE	0x00001000
#define RESET_VOL	0x00002000
#define RESET_ENV	0x00004000
#define IS_VALID	0x00008000
#define NEW_INS		0x00010000
#define NEW_VOL		0x00020000
#define ECHOBACK	0x00040000
#define VOL_SLIDE_2	0x00080000
#define NOTE_SLIDE	0x00100000
#define FINE_NSLIDE	0x00200000
#define NEW_NOTE	0x00400000
#define FINE_TPORTA	0x00800000

/* These need to be "persistent" between frames */
#define IS_READY	0x01000000
#define FADEOUT		0x02000000
#define RELEASE		0x04000000

/* Prefixes: 
 * a_ for arpeggio
 * v_ for volume slide
 * p_ for pan
 * f_ for frequency (period) slide
 * y_ for vibrato (v is already being used by volume)
 * s_ for slide to note (tone portamento)
 * t_ for tremolo
 */

struct xmp_channel {
    int flags;			/* Channel flags */
    int per_flags;		/* Persistent effect channel flags */
    int note;			/* Note number */
    int key;			/* Key number */
    double period;		/* Amiga or linear period */
    int pitchbend;		/* Pitch bender value */
    int finetune;		/* Guess what */
    int ins;			/* Instrument number */
    int smp;			/* Sample number */
    int insdef;			/* Instrument default */
    int pan;			/* Current pan */
    int masterpan;		/* Master pan -- for S3M set pan effect */
    int mastervol;		/* Master vol -- for IT track vol effect */
    int delay;			/* Note delay in frames */
    int retrig;			/* Retrig delay in frames */
    int rval;			/* Retrig parameters */
    int rtype;			/* Retrig type */
    int rcount;			/* Retrig counter */
    int tremor;			/* Tremor */
    int tcnt_up;		/* Tremor counter (up cycle) */
    int tcnt_dn;		/* Tremor counter (down cycle) */
    int keyoff;			/* Key off counter */
    int volume;			/* Current volume */
    int gvl;			/* Global volume for instrument for IT */
    int v_val;			/* Volume slide value */
    int v_fval;			/* Fine volume slide value */
    int v_val2;			/* Volume slide value */
    int volslide;		/* Volume slide effect memory */
    int fvolslide;		/* Fine volume slide effect memory */
    int p_val;			/* Current pan value */
    int trk_val;		/* Track volume slide value */
    int trk_fval;		/* Track fine volume slide value */
    int trkvsld;		/* Track volume slide effect memory */
    uint16 v_idx;		/* Volume envelope index */
    uint16 p_idx;		/* Pan envelope index */
    uint16 f_idx;		/* Freq envelope index */
    int y_type;			/* Vibrato waveform */
    int y_depth;		/* Vibrato depth */
    int y_sweep;		/* Vibrato sweep */
    int y_rate;			/* Vibrato rate */
    int y_idx;			/* Vibrato index */
    int t_type;			/* Tremolo waveform */
    int t_depth;		/* Tremolo depth */
    int t_rate;			/* Tremolo rate */
    int t_idx;			/* Tremolo index */
    int f_val;			/* Frequency slide value */
    int f_fval;			/* Fine frequency slide value */
    int porta;			/* Portamento effect memory */
    int fadeout;		/* Current fadeout (release) value */
    int gliss;			/* Glissando active */
    int s_end;			/* Target period for tone portamento */
    int s_sgn;			/* Tone portamento up/down switch */
    int s_val;			/* Delta for tone portamento */
    int a_val[ARP_SIZE];	/* Arpeggio relative notes */
    int a_idx;			/* Arpeggio index */
    int a_size;			/* Arpeggio size */
    int insvib_idx;		/* Instrument vibrato index */
    int insvib_swp;		/* Instrument vibrato sweep */
    int offset;			/* Sample offset memory */
    int offset_val;		/* Sample offset */
    int ns_val;			/* PTM note slide amount */
    int ns_fval;		/* OKT fine note slide amount */
    int ns_speed;		/* PTM note slide speed */
    int ns_count;		/* PTM note slide counter */
    int cutoff;			/* IT filter cutoff frequency */
    int cutoff2;		/* IT filter cutoff frequency (with envelope) */
    int resonance;		/* IT filter resonance */
    
    int med_vp;			/* MED synth volume sequence table pointer */
    int med_vv;			/* MED synth volume slide value */
    int med_vs;			/* MED synth volume speed */
    int med_vc;			/* MED synth volume speed counter */
    int med_vw;			/* MED synth volume wait counter */
    int med_wp;			/* MED synth waveform sequence table pointer */
    int med_wv;			/* MED synth waveform slide value */
    int med_ws;			/* MED synth waveform speed */
    int med_wc;			/* MED synth waveform speed counter */
    int med_ww;			/* MED synth waveform wait counter */
    int med_period;		/* MED synth period for RES */
    int med_arp;		/* MED synth arpeggio start */
    int med_aidx;		/* MED synth arpeggio index */
    int med_vwf;		/* MED synth vibrato waveform */
    int med_vib_depth;		/* MED synth vibrato depth */
    int med_vib_speed;		/* MED synth vibrato speed */
    int med_vib_idx;		/* MED synth vibrato index */
    int med_vib_wf;		/* MED synth vibrato waveform */

    int flt_B0;			/* IT filter stuff */
    int flt_B1;
    int flt_B2;

    struct xxm_event *delayed_event;
    int delayed_ins;		/* IT save instrument emulation */
};


void process_fx(struct xmp_context *, int, uint8, uint8, uint8, struct xmp_channel *, int);
void xmp_med_synth(struct xmp_context *, int, struct xmp_channel *, int);
int get_med_arp(struct xmp_player_context *, struct xmp_channel *);
int get_med_vibrato(struct xmp_channel *);
void filter_setup(struct xmp_context *, struct xmp_channel *, int);

#endif /* __XMP_PLAYER_H */
