/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */


struct mod_instrument {
	uint8 name[22];		/* Instrument name */
	uint16 size;		/* Sample length in 16-bit words */
	int8 finetune;		/* Finetune (signed nibble) */
	int8 volume;		/* Linear playback volume */
	uint16 loop_start;	/* Loop start in 16-bit words */
	uint16 loop_size;	/* Loop length in 16-bit words */
};

struct mod_header {
	uint8 name[20];
	struct mod_instrument ins[31];
	uint8 len;
	uint8 restart;		/* Number of patterns in Soundtracker,
				 * Restart in Noisetracker/Startrekker,
				 * 0x7F in Protracker
				 */
	uint8 order[128];
	uint8 magic[4];
};


/* Soundtracker 15-instrument module header */

struct st_header {
	uint8 name[20];
	struct mod_instrument ins[15];
	uint8 len;
	uint8 restart;
	uint8 order[128];
};

