/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

struct far_header {
	uint32 magic;		/* File magic: 'FAR\xfe' */
	uint8 name[40];		/* Song name */
	uint8 crlf[3];		/* 0x0d 0x0a 0x1A */
	uint16 headersize;	/* Remaining header size in bytes */
	uint8 version;		/* Version MSN=major, LSN=minor */
	uint8 ch_on[16];	/* Channel on/off switches */
	uint8 rsvd1[9];		/* Current editing values */
	uint8 tempo;		/* Default tempo */
	uint8 pan[16];		/* Channel pan definitions */
	uint8 rsvd2[4];		/* Grid, mode (for editor) */
	uint16 textlen;		/* Length of embedded text */
};

struct far_header2 {
	uint8 order[256];	/* Orders */
	uint8 patterns;		/* Number of stored patterns (?) */
	uint8 songlen;		/* Song length in patterns */
	uint8 restart;		/* Restart pos */
	uint16 patsize[256];	/* Size of each pattern in bytes */
};

struct far_instrument {
	uint8 name[32];		/* Instrument name */
	uint32 length;		/* Length of sample (up to 64Kb) */
	uint8 finetune;		/* Finetune (unsuported) */
	uint8 volume;		/* Volume (unsuported?) */
	uint32 loop_start;	/* Loop start */
	uint32 loopend;		/* Loop end */
	uint8 sampletype;	/* 1=16 bit sample */
	uint8 loopmode;
};

struct far_event {
	uint8 note;
	uint8 instrument;
	uint8 volume;		/* In reverse nibble order? */
	uint8 effect;
};


