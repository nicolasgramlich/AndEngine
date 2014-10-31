/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */


/* IT flags */
#define IT_STEREO	0x01
#define IT_VOL_OPT	0x02	/* Not recognized */
#define IT_USE_INST	0x04
#define IT_LINEAR_FREQ	0x08
#define IT_OLD_FX	0x10
#define IT_LINK_GXX	0x20

/* IT special */
#define IT_HAS_MSG	0x01

/* IT instrument flags */
#define IT_INST_SAMPLE	0x01
#define IT_INST_16BIT	0x02
#define IT_INST_STEREO	0x04
#define IT_INST_LOOP	0x10
#define IT_INST_SLOOP	0x20
#define IT_INST_BLOOP	0x40
#define IT_INST_BSLOOP	0x80

/* IT sample flags */
#define IT_SMP_SAMPLE	0x01
#define IT_SMP_16BIT	0x02
#define IT_SMP_STEREO	0x04	/* unsupported */
#define IT_SMP_COMP	0x08	/* unsupported */
#define IT_SMP_LOOP	0x10
#define IT_SMP_SLOOP	0x20
#define IT_SMP_BLOOP	0x40
#define IT_SMP_BSLOOP	0x80

/* IT sample conversion flags */
#define IT_CVT_SIGNED	0x01
#define IT_CVT_BIGEND	0x02	/* 'safe to ignore' according to ittech.txt */
#define IT_CVT_DIFF	0x04	/* idem */
#define IT_CVT_BYTEDIFF	0x08	/* idem */
#define IT_CVT_12BIT	0x10	/* idem */

/* IT envelope flags */
#define IT_ENV_ON	0x01
#define IT_ENV_LOOP	0x02
#define IT_ENV_SLOOP	0x04
#define IT_ENV_FILTER	0x80


struct it_file_header {
	uint32 magic;		/* 'IMPM' */
	uint8 name[26];		/* ASCIIZ Song name */
	uint8 rsvd1[2];		/* Reserved */
	uint16 ordnum;		/* Number of orders (must be even) */
	uint16 insnum;		/* Number of instruments */
	uint16 smpnum;		/* Number of samples */
	uint16 patnum;		/* Number of patterns */
	uint16 cwt;		/* Tracker ID and version */
	uint16 cmwt;		/* Format version */
	uint16 flags;		/* Flags */
	uint16 special;		/* More flags */
	uint8 gv;		/* Global volume */
	uint8 mv;		/* Master volume */
	uint8 is;		/* Initial speed */
	uint8 it;		/* Initial tempo */
	uint8 sep;		/* Panning separation */
	uint8 zero;		/* Always zero */
	uint16 msglen;		/* Message length */
	uint32 msgofs;		/* Message offset */
	uint8 rsvd2[4];		/* Reserved */
	uint8 chpan[64];	/* Channel pan settings */
	uint8 chvol[64];	/* Channel volume settings */
};

struct it_instrument1_header {
	uint32 magic;		/* 'IMPI' */
	uint8 dosname[12];	/* DOS filename */
	uint8 zero;		/* Always zero */
	uint8 flags;		/* Instrument flags */
	uint8 vls;		/* Volume loop start */
	uint8 vle;		/* Volume loop end */
	uint8 sls;		/* Sustain loop start */
	uint8 sle;		/* Sustain loop end */
	uint16 rsvd1;		/* Reserved */
	uint16 fadeout;		/* Fadeout (release) */
	uint8 nna;		/* New note action */
	uint8 dnc;		/* Duplicate note check */
	uint16 trkvers;		/* Tracker version */
	uint8 nos;		/* Number of samples */
	uint8 rsvd2;		/* Reserved */
	uint8 name[26];		/* ASCIIZ Instrument name */
	uint8 rsvd3[6];		/* Reserved */
	uint8 keys[240];
	uint8 epoint[200];
	uint8 enode[50];
};

struct it_instrument2_header {
	uint32 magic;		/* 'IMPI' */
	uint8 dosname[12];	/* DOS filename */
	uint8 zero;		/* Always zero */
	uint8 nna;		/* New Note Action */
	uint8 dct;		/* Duplicate Check Type */
	uint8 dca;		/* Duplicate Check Action */
	uint16 fadeout;
	uint8 pps;		/* Pitch-Pan Separation */
	uint8 ppc;		/* Pitch-Pan Center */
	uint8 gbv;		/* Global Volume */
	uint8 dfp;		/* Default pan */
	uint8 rv;		/* Random volume variation */
	uint8 rp;		/* Random pan variation */
	uint16 trkvers;		/* Not used: tracked version */
	uint8 nos;		/* Not used: number of samples */
	uint8 rsvd1;		/* Reserved */
	uint8 name[26];		/* ASCIIZ Instrument name */
	uint8 ifc;		/* Initial filter cutoff */
	uint8 ifr;		/* Initial filter resonance */
	uint8 mch;		/* MIDI channel */
	uint8 mpr;		/* MIDI program */
	uint16 mbnk;		/* MIDI bank */
	uint8 keys[240];
};

struct it_envelope_node {
	int8 y;
	uint16 x;
};

struct it_envelope {
	uint8 flg;		/* Flags */
	uint8 num;		/* Number of node points */
	uint8 lpb;		/* Loop beginning */
	uint8 lpe;		/* Loop end */
	uint8 slb;		/* Sustain loop beginning */
	uint8 sle;		/* Sustain loop end */
	struct it_envelope_node node[25];
	uint8 unused;
};

struct it_sample_header {
	uint32 magic;		/* 'IMPS' */
	uint8 dosname[12];	/* DOS filename */
	uint8 zero;		/* Always zero */
	uint8 gvl;		/* Global volume for instrument */
	uint8 flags;		/* Instrument flags */
	uint8 vol;		/* Volume */
	uint8 name[26];		/* ASCIIZ sample name */
	uint8 convert;		/* Sample flags */
	uint8 dfp;		/* Default pan */
	uint32 length;		/* Length */
	uint32 loopbeg;		/* Loop begin */
	uint32 loopend;		/* Loop end */
	uint32 c5spd;		/* C 5 speed */
	uint32 sloopbeg;	/* SusLoop begin */
	uint32 sloopend;	/* SusLoop end */
	uint32 sample_ptr;	/* Sample pointer */
	uint8 vis;		/* Vibrato speed */
	uint8 vid;		/* Vibrato depth */
	uint8 vir;		/* Vibrato rate */
	uint8 vit;		/* Vibrato waveform */
};

