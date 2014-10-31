/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

struct liq_header {
	uint8 magic[14];	/* "Liquid Module:" */
	uint8 name[30];		/* ASCIIZ module name */
	uint8 author[20];	/* Author name */
	uint8 _0x1a;		/* 0x1a */
	uint8 tracker[20];	/* Tracker name */
	uint16 version;		/* Format version */
	uint16 speed;		/* Initial speed */
	uint16 bpm;		/* Initial bpm */
	uint16 low;		/* Lowest note (Amiga Period*4) */
	uint16 high;		/* Uppest note (Amiga Period*4) */
	uint16 chn;		/* Number of channels */
	uint32 flags;		/* Module flags */
	uint16 pat;		/* Number of patterns saved */
	uint16 ins;		/* Number of instruments */
	uint16 len;		/* Module length */
	uint16 hdrsz;		/* Header size */
};

struct liq_instrument {
#if 0
	uint8 magic[4];		/* 'L', 'D', 'S', 'S' */
#endif
	uint16 version;		/* LDSS header version */
	uint8 name[30];		/* Instrument name */
	uint8 editor[20];	/* Generator name */
	uint8 author[20];	/* Author name */
	uint8 hw_id;		/* Hardware used to record the sample */
	uint32 length;		/* Sample length */
	uint32 loopstart;	/* Sample loop start */
	uint32 loopend;		/* Sample loop end */
	uint32 c2spd;		/* C2SPD */
	uint8 vol;		/* Volume */
	uint8 flags;		/* Flags */
	uint8 pan;		/* Pan */
	uint8 midi_ins;		/* General MIDI instrument */
	uint8 gvl;		/* Global volume */
	uint8 chord;		/* Chord type */
	uint16 hdrsz;		/* LDSS header size */
	uint16 comp;		/* Compression algorithm */
	uint32 crc;		/* CRC */
	uint8 midi_ch;		/* MIDI channel */
	uint8 rsvd[11];		/* Reserved */
	uint8 filename[25];	/* DOS file name */
};

struct liq_pattern {
#if 0
	uint8 magic[4];		/* 'L', 'P', 0, 0 */
#endif
	uint8 name[30];		/* ASCIIZ pattern name */
	uint16 rows;		/* Number of rows */
	uint32 size;		/* Size of packed pattern */
	uint32 reserved;	/* Reserved */
};

