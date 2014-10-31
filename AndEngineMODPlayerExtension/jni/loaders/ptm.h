/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#define PTM_CH_MASK	0x1f
#define PTM_NI_FOLLOW	0x20
#define PTM_VOL_FOLLOWS	0x80
#define PTM_FX_FOLLOWS	0x40

struct ptm_file_header {
	uint8 name[28];		/* Song name */
	uint8 doseof;		/* 0x1a */
	uint8 vermin;		/* Minor version */
	uint8 vermaj;		/* Major type */
	uint8 rsvd1;		/* Reserved */
	uint16 ordnum;		/* Number of orders (must be even) */
	uint16 insnum;		/* Number of instruments */
	uint16 patnum;		/* Number of patterns */
	uint16 chnnum;		/* Number of channels */
	uint16 flags;		/* Flags (set to 0) */
	uint16 rsvd2;		/* Reserved */
	uint32 magic;		/* 'PTMF' */
	uint8 rsvd3[16];	/* Reserved */
	uint8 chset[32];	/* Channel settings */
	uint8 order[256];	/* Orders */
	uint16 patseg[128];
};

struct ptm_instrument_header {
	uint8 type;		/* Sample type */
	uint8 dosname[12];	/* DOS file name */
	uint8 vol;		/* Volume */
	uint16 c4spd;		/* C4 speed */
	uint16 smpseg;		/* Sample segment (not used) */
	uint32 smpofs;		/* Sample offset */
	uint32 length;		/* Length */
	uint32 loopbeg;		/* Loop begin */
	uint32 loopend;		/* Loop end */
	uint32 gusbeg;		/* GUS begin address */
	uint32 guslps;		/* GUS loop start address */
	uint32 guslpe;		/* GUS loop end address */
	uint8 gusflg;		/* GUS loop flags */
	uint8 rsvd1;		/* Reserved */
	uint8 name[28];		/* Instrument name */
	uint32 magic;		/* 'PTMS' */
};
