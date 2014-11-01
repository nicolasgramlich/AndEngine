/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */


#define STM_TYPE_SONG	0x01
#define STM_TYPE_MODULE	0x02

struct stm_instrument_header {
	uint8 name[12];		/* ASCIIZ instrument name */
	uint8 id;		/* Id=0 */
	uint8 idisk;		/* Instrument disk */
	uint16 rsvd1;		/* Reserved */
	uint16 length;		/* Sample length */
	uint16 loopbeg;		/* Loop begin */
	uint16 loopend;		/* Loop end */
	uint8 volume;		/* Playback volume */
	uint8 rsvd2;		/* Reserved */
	uint16 c2spd;		/* C4 speed */
	uint32 rsvd3;		/* Reserved */
	uint16 paralen;		/* Length in paragraphs */
};

struct stm_file_header {
	uint8 name[20];		/* ASCIIZ song name */
	uint8 magic[8];		/* '!Scream!' */
	uint8 rsvd1;		/* '\x1a' */
	uint8 type;		/* 1=song, 2=module */
	uint8 vermaj;		/* Major version number */
	uint8 vermin;		/* Minor version number */
	uint8 tempo;		/* Playback tempo */
	uint8 patterns;		/* Number of patterns */
	uint8 gvol;		/* Global volume */
	uint8 rsvd2[13];	/* Reserved */
	struct stm_instrument_header ins[31];
};

