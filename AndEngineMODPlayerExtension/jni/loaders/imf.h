/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#define IMF_EOR		0x00
#define IMF_CH_MASK	0x1f
#define IMF_NI_FOLLOW	0x20
#define IMF_FX_FOLLOWS	0x80
#define IMF_F2_FOLLOWS	0x40

struct imf_channel {
	char name[12];		/* Channelname (ASCIIZ-String, max 11 chars) */
	uint8 status;		/* Channel status */
	uint8 pan;		/* Pan positions */
	uint8 chorus;		/* Default chorus */
	uint8 reverb;		/* Default reverb */
};

struct imf_header {
	char name[32];		/* Songname (ASCIIZ-String, max. 31 chars) */
	uint16 len;		/* Number of orders saved */
	uint16 pat;		/* Number of patterns saved */
	uint16 ins;		/* Number of instruments saved */
	uint16 flg;		/* Module flags */
	uint8 unused1[8];
	uint8 tpo;		/* Default tempo (1..255) */
	uint8 bpm;		/* Default beats per minute (BPM) (32..255) */
	uint8 vol;		/* Default mastervolume (0..64) */
	uint8 amp;		/* Amplification factor (4..127) */
	uint8 unused2[8];
	uint32 magic;		/* 'IM10' */
	struct imf_channel chn[32];	/* Channel settings */
	uint8 pos[256];		/* Order list */
};

struct imf_env {
	uint8 npt;		/* Number of envelope points */
	uint8 sus;		/* Envelope sustain point */
	uint8 lps;		/* Envelope loop start point */
	uint8 lpe;		/* Envelope loop end point */
	uint8 flg;		/* Envelope flags */
	uint8 unused[3];
};

struct imf_instrument {
	char name[32];		/* Inst. name (ASCIIZ-String, max. 31 chars) */
	uint8 map[120];		/* Multisample settings */
	uint8 unused[8];
	uint16 vol_env[32];	/* Volume envelope settings */
	uint16 pan_env[32];	/* Pan envelope settings */
	uint16 pitch_env[32];	/* Pitch envelope settings */
	struct imf_env env[3];
	uint16 fadeout;		/* Fadeout rate (0...0FFFH) */
	uint16 nsm;		/* Number of samples in instrument */
	uint32 magic;		/* 'II10' */
};

struct imf_sample {
	char name[13];		/* Sample filename (12345678.ABC) */
	uint8 unused1[3];
	uint32 len;		/* Length */
	uint32 lps;		/* Loop start */
	uint32 lpe;		/* Loop end */
	uint32 rate;		/* Samplerate */
	uint8 vol;		/* Default volume (0..64) */
	uint8 pan;		/* Default pan (00h = Left / 80h = Middle) */
	uint8 unused2[14];
	uint8 flg;		/* Sample flags */
	uint8 unused3[5];
	uint16 ems;		/* Reserved for internal usage */
	uint32 dram;		/* Reserved for internal usage */
	uint32 magic;		/* 'IS10' */
};

