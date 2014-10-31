/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */


/* S3M packed pattern macros */
#define S3M_EOR		0	/* End of row */
#define S3M_CH_MASK	0x1f	/* Channel */
#define S3M_NI_FOLLOW	0x20	/* Note and instrument follow */
#define S3M_VOL_FOLLOWS	0x40	/* Volume follows */
#define S3M_FX_FOLLOWS	0x80	/* Effect and parameter follow */

/* S3M channel info macros */
#define S3M_CH_ON	0x80	/* Psi says it's bit 8, I'll assume bit 7 */
#define S3M_CH_OFF	0xff
#define S3M_CH_PAN	0x7f	/* Left/Right */

/* S3M channel pan macros */
#define S3M_PAN_SET	0x20
#define S3M_PAN_MASK	0x0f

/* S3M flags */
#define S3M_ST2_VIB	0x01	/* Not recognized */
#define S3M_ST2_TEMPO	0x02	/* Not recognized */
#define S3M_AMIGA_SLIDE	0x04	/* Not recognized */
#define S3M_VOL_OPT	0x08	/* Not recognized */
#define S3M_AMIGA_RANGE	0x10
#define S3M_SB_FILTER	0x20	/* Not recognized */
#define S3M_ST300_VOLS	0x40
#define S3M_CUSTOM_DATA	0x80	/* Not recognized */

/* S3M Adlib instrument types */
#define S3M_INST_SAMPLE	0x01
#define S3M_INST_AMEL	0x02
#define S3M_INST_ABD	0x03
#define S3M_INST_ASNARE	0x04
#define S3M_INST_ATOM	0x05
#define S3M_INST_ACYM	0x06
#define S3M_INST_AHIHAT	0x07

struct s3m_file_header {
	uint8 name[28];		/* Song name */
	uint8 doseof;		/* 0x1a */
	uint8 type;		/* File type */
	uint8 rsvd1[2];		/* Reserved */
	uint16 ordnum;		/* Number of orders (must be even) */
	uint16 insnum;		/* Number of instruments */
	uint16 patnum;		/* Number of patterns */
	uint16 flags;		/* Flags */
	uint16 version;		/* Tracker ID and version */
	uint16 ffi;		/* File format information */
	uint32 magic;		/* 'SCRM' */
	uint8 gv;		/* Global volume */
	uint8 is;		/* Initial speed */
	uint8 it;		/* Initial tempo */
	uint8 mv;		/* Master volume */
	uint8 uc;		/* Ultra click removal */
	uint8 dp;		/* Default pan positions if 0xfc */
	uint8 rsvd2[8];		/* Reserved */
	uint16 special;		/* Ptr to special custom data */
	uint8 chset[32];	/* Channel settings */
};

struct s3m_instrument_header {
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
	uint32 magic;		/* 'SCRS' */
};

struct s3m_adlib_header {
	uint8 dosname[12];	/* DOS file name */
	uint8 rsvd1[3];		/* 0x00 0x00 0x00 */
	uint8 reg[12];		/* Adlib registers */
	uint8 vol;
	uint8 dsk;
	uint8 rsvd2[2];
	uint16 c2spd;		/* C 4 speed */
	uint16 rsvd3;		/* Reserved */
	uint8 rsvd4[12];	/* Reserved */
	uint8 name[28];		/* Instrument name */
	uint32 magic;		/* 'SCRI' */
};
