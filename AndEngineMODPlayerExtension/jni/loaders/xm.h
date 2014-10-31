/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */


#define XM_EVENT_PACKING 0x80
#define XM_EVENT_PACK_MASK 0x7f
#define XM_EVENT_NOTE_FOLLOWS 0x01
#define XM_EVENT_INSTRUMENT_FOLLOWS 0x02
#define XM_EVENT_VOLUME_FOLLOWS 0x04
#define XM_EVENT_FXTYPE_FOLLOWS 0x08
#define XM_EVENT_FXPARM_FOLLOWS 0x10
#define XM_LINEAR_FREQ 0x01
#define XM_LOOP_MASK 0x03
#define XM_LOOP_NONE 0
#define XM_LOOP_FORWARD 1
#define XM_LOOP_PINGPONG 2
#define XM_SAMPLE_16BIT 0x10
#define XM_ENVELOPE_ON 0x01
#define XM_ENVELOPE_SUSTAIN 0x02
#define XM_ENVELOPE_LOOP 0x04
#define XM_END_OF_SONG 0x80	/* Undocumented */
#define XM_LINEAR_PERIOD_MODE 0x01


struct xm_file_header {
	uint8 id[17];		/* ID text: "Extended module: " */
	uint8 name[20];		/* Module name, padded with zeroes */
	uint8 doseof;		/* 0x1a */
	uint8 tracker[20];	/* Tracker name */
	uint16 version;		/* Version number, minor-major */
	uint32 headersz;	/* Header size */
	uint16 songlen;		/* Song length (in patten order table) */
	uint16 restart;		/* Restart position */
	uint16 channels;	/* Number of channels (2,4,6,8,10,...,32) */
	uint16 patterns;	/* Number of patterns (max 256) */
	uint16 instruments;	/* Number of instruments (max 128) */
	uint16 flags;		/* bit 0: 0=Amiga freq table, 1=Linear */
	uint16 tempo;		/* Default tempo */
	uint16 bpm;		/* Default BPM */
	uint8 order[256];	/* Pattern order table */
};

struct xm_pattern_header {
	uint32 length;		/* Pattern header length */
	uint8 packing;		/* Packing type (always 0) */
	uint16 rows;		/* Number of rows in pattern (1..256) */
	uint16 datasize;	/* Packed patterndata size */
};

struct xm_instrument_header {
	uint32 size;		/* Instrument size */
	uint8 name[22];		/* Instrument name */
	uint8 type;		/* Instrument type (always 0) */
	uint16 samples;		/* Number of samples in instrument */
	uint32 sh_size;		/* Sample header size */
};

struct xm_instrument {
	uint8 sample[96];	/* Sample number for all notes */
	uint16 v_env[24];	/* Points for volume envelope */
	uint16 p_env[24];	/* Points for panning envelope */
	uint8 v_pts;		/* Number of volume points */
	uint8 p_pts;		/* Number of panning points */
	uint8 v_sus;		/* Volume sustain point */
	uint8 v_start;		/* Volume loop start point */
	uint8 v_end;		/* Volume loop end point */
	uint8 p_sus;		/* Panning sustain point */
	uint8 p_start;		/* Panning loop start point */
	uint8 p_end;		/* Panning loop end point */
	uint8 v_type;		/* Bit 0: On; 1: Sustain; 2: Loop */
	uint8 p_type;		/* Bit 0: On; 1: Sustain; 2: Loop */
	uint8 y_wave;		/* Vibrato waveform */
	uint8 y_sweep;		/* Vibrato sweep */
	uint8 y_depth;		/* Vibrato depth */
	uint8 y_rate;		/* Vibrato rate */
	uint16 v_fade;		/* Volume fadeout */
#if 0
	uint8 reserved[22];	/* Reserved; 2 bytes in specs, 22 in 1.04 */
#endif
};

struct xm_sample_header {
	uint32 length;		/* Sample length */
	uint32 loop_start;	/* Sample loop start */
	uint32 loop_length;	/* Sample loop length */
	uint8 volume;		/* Volume */
	int8 finetune;		/* Finetune (signed byte -128..+127) */
	uint8 type;		/* 0=No loop,1=Fwd loop,2=Ping-pong,16-bit */
	uint8 pan;		/* Panning (0-255) */
	int8 relnote;		/* Relative note number (signed byte) */
	uint8 reserved;		/* Reserved */
	uint8 name[22];		/* Sample_name */
};

struct xm_event {
	uint8 note;		/* Note (0-71, 0 = C-0) */
	uint8 instrument;	/* Instrument (0-128) */
	uint8 volume;		/* Volume column byte */
	uint8 fx_type;		/* Effect type */
	uint8 fx_parm;		/* Effect parameter */
};
