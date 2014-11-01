/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Reverse engineered from the two SFX files in the Delitracker mods disk
 * and music from Future Wars, Twinworld and Operation Stealth. Effects
 * must be verified/implemented.
 */

/* From the ExoticRipper docs:
 * [SoundFX 2.0 is] simply the same as SoundFX 1.3, except that it
 * uses 31 samples [instead of 15].
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "period.h"
#include "load.h"

#define MAGIC_SONG	MAGIC4('S','O','N','G')


static int sfx_test (FILE *, char *, const int);
static int sfx_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info sfx_loader = {
    "SFX",
    "SoundFX",
    sfx_test,
    sfx_load
};

static int sfx_test(FILE *f, char *t, const int start)
{
    uint32 a, b;

    fseek(f, 4 * 15, SEEK_CUR);
    a = read32b(f);
    fseek(f, 4 * 15, SEEK_CUR);
    b = read32b(f);

    if (a != MAGIC_SONG && b != MAGIC_SONG)
	return -1;

    read_title(f, t, 0);

    return 0;
}


struct sfx_ins {
    uint8 name[22];		/* Instrument name */
    uint16 len;			/* Sample length in words */
    uint8 finetune;		/* Finetune */
    uint8 volume;		/* Volume (0-63) */
    uint16 loop_start;		/* Sample loop start in bytes */
    uint16 loop_length;		/* Sample loop length in words */
};

struct sfx_header {
    uint32 magic;		/* 'SONG' */
    uint16 delay;		/* Delay value (tempo), default is 0x38e5 */
    uint16 unknown[7];		/* ? */
};

struct sfx_header2 {
    uint8 len;			/* Song length */
    uint8 restart;		/* Restart pos (?) */
    uint8 order[128];		/* Order list */
};


static int sfx_13_20_load(struct xmp_context *ctx, FILE *f, const int nins, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j;
    struct xxm_event *event;
    struct sfx_header sfx;
    struct sfx_header2 sfx2;
    uint8 ev[4];
    int ins_size[31];
    struct sfx_ins ins[31];	/* Instruments */

    LOAD_INIT();

    for (i = 0; i < nins; i++)
	ins_size[i] = read32b(f);

    sfx.magic = read32b(f);
    sfx.delay = read16b(f);
    fread(&sfx.unknown, 14, 1, f);

    if (sfx.magic != MAGIC_SONG)
	return -1;

    m->xxh->ins = nins;
    m->xxh->smp = m->xxh->ins;
    m->xxh->bpm = 14565 * 122 / sfx.delay;

    for (i = 0; i < m->xxh->ins; i++) {
	fread(&ins[i].name, 22, 1, f);
	ins[i].len = read16b(f);
	ins[i].finetune = read8(f);
	ins[i].volume = read8(f);
	ins[i].loop_start = read16b(f);
	ins[i].loop_length = read16b(f);
    }

    sfx2.len = read8(f);
    sfx2.restart = read8(f);
    fread(&sfx2.order, 128, 1, f);

    m->xxh->len = sfx2.len;
    if (m->xxh->len > 0x7f)
	return -1;

    memcpy (m->xxo, sfx2.order, m->xxh->len);
    for (m->xxh->pat = i = 0; i < m->xxh->len; i++)
	if (m->xxo[i] > m->xxh->pat)
	    m->xxh->pat = m->xxo[i];
    m->xxh->pat++;

    m->xxh->trk = m->xxh->chn * m->xxh->pat;

    strcpy (m->type, m->xxh->ins == 15 ? "SoundFX 1.3" : "SoundFX 2.0");

    MODULE_INFO();

    INSTRUMENT_INIT();

    reportv(ctx, 1, "     Instrument name        Len  LBeg LEnd L Vol Fin\n");

    for (i = 0; i < m->xxh->ins; i++) {
	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);
	m->xxih[i].nsm = !!(m->xxs[i].len = ins_size[i]);
	m->xxs[i].lps = ins[i].loop_start;
	m->xxs[i].lpe = m->xxs[i].lps + 2 * ins[i].loop_length;
	m->xxs[i].flg = ins[i].loop_length > 1 ? WAVE_LOOPING : 0;
	m->xxi[i][0].vol = ins[i].volume;
	m->xxi[i][0].fin = (int8)(ins[i].finetune << 4); 
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].sid = i;

	copy_adjust(m->xxih[i].name, ins[i].name, 22);

	if ((V(1)) && (strlen((char *)m->xxih[i].name) || (m->xxs[i].len > 2)))
	    report("[%2X] %-22.22s %04x %04x %04x %c  %02x %+d\n",
		i, m->xxih[i].name, m->xxs[i].len, m->xxs[i].lps, m->xxs[i].lpe,
		m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ', m->xxi[i][0].vol,
		m->xxi[i][0].fin >> 4);
    }

    PATTERN_INIT();

    reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC(i);
	m->xxp[i]->rows = 64;
	TRACK_ALLOC(i);

	for (j = 0; j < 64 * m->xxh->chn; j++) {
	    event = &EVENT(i, j % m->xxh->chn, j / m->xxh->chn);
	    fread(ev, 1, 4, f);

	    event->note = period_to_note ((LSN (ev[0]) << 8) | ev[1]);
	    event->ins = (MSN (ev[0]) << 4) | MSN (ev[2]);
	    event->fxp = ev[3];

	    switch (LSN(ev[2])) {
	    case 0x1:			/* Arpeggio */
		event->fxt = FX_ARPEGGIO;
		break;
	    case 0x02:			/* Pitch bend */
		if (event->fxp >> 4) {
		    event->fxt = FX_PORTA_DN;
		    event->fxp >>= 4;
		} else if (event->fxp & 0x0f) {
		    event->fxt = FX_PORTA_UP;
		    event->fxp &= 0x0f;
		}
		break;
	    case 0x5:			/* Volume up */
		event->fxt = FX_VOLSLIDE_DN;
		break;
	    case 0x6:			/* Set volume (attenuation) */
		event->fxt = FX_VOLSET;
		event->fxp = 0x40 - ev[3];
		break;
	    case 0x3:			/* LED on */
	    case 0x4:			/* LED off */
	    case 0x7:			/* Set step up */
	    case 0x8:			/* Set step down */
	    default:
		event->fxt = event->fxp = 0;
		break;
	    }
	}
	reportv(ctx, 0, ".");
    }

    m->xxh->flg |= XXM_FLG_MODRNG;

    /* Read samples */

    reportv(ctx, 0, "\nStored samples : %d ", m->xxh->smp);

    for (i = 0; i < m->xxh->ins; i++) {
	if (m->xxs[i].len <= 2)
	    continue;
	xmp_drv_loadpatch(ctx, f, i, m->c4rate, 0, &m->xxs[i], NULL);
	if (V(0))
	    report(".");
    }
    reportv(ctx, 0, "\n");

    return 0;
}


static int sfx_load(struct xmp_context *ctx, FILE *f, const int start)
{
    if (sfx_13_20_load(ctx, f, 15, start) < 0)
	return sfx_13_20_load(ctx, f, 31, start);
    return 0;
}
