/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

struct stx_file_header {
	uint8 name[20];		/* Song name */
	uint8 magic[8];		/* !Scream! */
	uint16 psize;		/* Pattern 0 size? */
	uint16 unknown1;	/* ??!? */
	uint16 pp_pat;		/* Pointer to pattern table */
	uint16 pp_ins;		/* Pattern to instrument table */
	uint16 pp_chn;		/* Pointer to channel table (?) */
	uint16 unknown2;
	uint16 unknown3;
	uint8 gvol;		/* Global volume */
	uint8 tempo;		/* Playback tempo */
	uint16 unknown4;
	uint16 unknown5;
	uint16 patnum;		/* Number of patterns */
	uint16 insnum;		/* Number of instruments */
	uint16 ordnum;		/* Number of orders */
	uint16 unknown6;	/* Flags? */
	uint16 unknown7;	/* Version? */
	uint16 unknown8;	/* Ffi? */
	uint8 magic2[4];	/* 'SCRM' */
};

struct stx_instrument_header {
	uint8 type;		/* Instrument type */
	uint8 dosname[13];	/* DOS file name */
	uint16 memseg;		/* Pointer to sample data */
	uint32 length;		/* Length */
	uint32 loopbeg;		/* Loop begin */
	uint32 loopend;		/* Loop end */
	uint8 vol;		/* Volume */
	uint8 rsvd1;		/* Reserved */
	uint8 pack;		/* Packing type (not used) */
	uint8 flags;		/* Loop/stereo/16bit samples flags */
	uint16 c2spd;		/* C 4 speed */
	uint16 rsvd2;		/* Reserved */
	uint8 rsvd3[4];		/* Reserved */
	uint16 int_gp;		/* Internal - GUS pointer */
	uint16 int_512;		/* Internal - SB pointer */
	uint32 int_last;	/* Internal - SB index */
	uint8 name[28];		/* Instrument name */
	uint8 magic[4];		/* Reserved (for 'SCRS') */
};

