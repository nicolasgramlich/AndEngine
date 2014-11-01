/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

struct mtm_file_header {
	uint8 magic[3];		/* "MTM" */
	uint8 version;		/* MSN=major, LSN=minor */
	uint8 name[20];		/* ASCIIZ Module name */
	uint16 tracks;		/* Number of tracks saved */
	uint8 patterns;		/* Number of patterns saved */
	uint8 modlen;		/* Module length */
	uint16 extralen;	/* Length of the comment field */
	uint8 samples;		/* Number of samples */
	uint8 attr;		/* Always zero */
	uint8 rows;		/* Number rows per track */
	uint8 channels;		/* Number of tracks per pattern */
	uint8 pan[32];		/* Pan positions for each channel */
};

struct mtm_instrument_header {
	uint8 name[22];		/* Instrument name */
	uint32 length;		/* Instrument length in bytes */
	uint32 loop_start;	/* Sample loop start */
	uint32 loopend;		/* Sample loop end */
	uint8 finetune;		/* Finetune */
	uint8 volume;		/* Playback volume */
	uint8 attr;		/* &0x01: 16bit sample */
};

