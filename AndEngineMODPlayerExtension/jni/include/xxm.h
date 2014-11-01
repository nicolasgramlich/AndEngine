
#ifndef __XXM_H
#define __XXM_H

struct xxm_header {
#define XXM_FLG_LINEAR	0x01
#define XXM_FLG_MODRNG	0x02
#define XXM_FLG_INSVOL  0x04
	int flg;		/* Flags */
	int pat;		/* Number of patterns */
	int ptc;		/* Number of patches */
	int trk;		/* Number of tracks */
	int chn;		/* Tracks per pattern */
	int ins;		/* Number of instruments */
	int smp;		/* Number of samples */
	int tpo;		/* Initial tempo */
	int bpm;		/* Initial BPM */
	int len;		/* Module length in patterns */
	int rst;		/* Restart position */
	int gvl;		/* Global volume */
};

struct xxm_channel {
	int pan;
	int vol;
#define XXM_CHANNEL_FM	0x01
#define XXM_CHANNEL_FX	0x02
#define XXM_CHANNEL_MUTE 0x04
	int flg;
	int cho;
	int rvb;
};

struct xxm_trackinfo {
	int index;		/* Track index */
};

struct xxm_pattern {
	int rows;		/* Number of rows */
	struct xxm_trackinfo info[1];
};

struct xxm_event {
	uint8 note;		/* Note number (0==no note) */
	uint8 ins;		/* Patch number */
	uint8 vol;		/* Volume (0 to 64) */
	uint8 fxt;		/* Effect type */
	uint8 fxp;		/* Effect parameter */
	uint8 f2t;		/* Secondary effect type */
	uint8 f2p;		/* Secondary effect parameter */
};

struct xxm_track {
	int rows;		/* Number of rows */
	struct xxm_event event[1];
};

struct xxm_envinfo {
#define XXM_ENV_ON	0x01
#define XXM_ENV_SUS	0x02
#define XXM_ENV_LOOP	0x04
#define XXM_ENV_FLT	0x08
	int flg;		/* Flags */
	int npt;		/* Number of envelope points */
	int scl;		/* Envelope scaling */
	int sus;		/* Sustain start point */
	int sue;		/* Sustain end point */
	int lps;		/* Loop start point */
	int lpe;		/* Loop end point */
};

struct xxm_instrument_header {
	uint8 name[32];		/* Instrument name */
	int vol;		/* Volume */
	int nsm;		/* Number of samples */
	int rls;		/* Release (fadeout) */
	struct xxm_envinfo aei;	/* Amplitude envelope info */
	struct xxm_envinfo pei;	/* Pan envelope info */
	struct xxm_envinfo fei;	/* Frequency envelope info */
	int vts;		/* Volume table speed -- for MED */
	int wts;		/* Waveform table speed -- for MED */
};

#define XXM_KEY_MAX 108
struct xxm_instrument_map {
	uint8 ins[XXM_KEY_MAX];	/* Instrument number for each key */
	int8 xpo[XXM_KEY_MAX];	/* Instrument transpose for each key */
};

struct xxm_instrument {
	int vol;		/* [default] Volume */
	int gvl;		/* [global] Volume */
	int pan;		/* Pan */
	int xpo;		/* Transpose */
	int fin;		/* Finetune */
	int vwf;		/* Vibrato waveform */
	int vde;		/* Vibrato depth */
	int vra;		/* Vibrato rate */
	int vsw;		/* Vibrato sweep */
	int rvv;		/* Random volume var -- for IT */
	int sid;		/* Sample number */
#define XXM_NNA_CUT	0x00
#define XXM_NNA_CONT	0x01
#define XXM_NNA_OFF	0x02
#define XXM_NNA_FADE	0x03
	int nna;		/* New note action -- for IT */
#define XXM_DCT_OFF	0x00
#define XXM_DCT_NOTE	0x01
#define XXM_DCT_SMP	0x02
#define XXM_DCT_INST	0x03
	int dct;		/* Duplicate check type -- for IT */
#define XXM_DCA_CUT	XXM_NNA_CUT
#define XXM_DCA_OFF	XXM_NNA_OFF
#define XXM_DCA_FADE	XXM_NNA_FADE
	int dca;		/* Duplicate check action -- for IT */
	int ifc;		/* Initial filter cutoff -- for IT */
	int ifr;		/* Initial filter resonance -- for IT */
	int hld;		/* Hold -- for MED */
};

struct xxm_sample {
	uint8 name[32];		/* Sample name */
	int len;		/* Sample length */
	int lps;		/* Loop start */
	int lpe;		/* Loop end */
	int flg;		/* Flags */
};

#endif				/* __XXM_H */
