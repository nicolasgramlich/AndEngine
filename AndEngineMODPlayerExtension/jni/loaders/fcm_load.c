/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Loader for FC-M Packer modules based on the format description
 * written by Sylvain Chipaux (Asle/ReDoX). Modules sent by Sylvain
 * Chipaux.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include "period.h"


struct fcm_instrument {
    uint16 size;
    uint8 finetune;
    uint8 volume;
    uint16 loop_start;
    uint16 loop_size;
};

struct fcm_header {
    uint8 magic[4];		/* 'FC-M' magic ID */
    uint8 vmaj;
    uint8 vmin;
    uint8 name_id[4];		/* 'NAME' pseudo chunk ID */
    uint8 name[20];
    uint8 inst_id[4];		/* 'INST' pseudo chunk ID */
    struct fcm_instrument ins[31];
    uint8 long_id[4];		/* 'LONG' pseudo chunk ID */
    uint8 len;
    uint8 rst;
    uint8 patt_id[4];		/* 'PATT' pseudo chunk ID */
};
    

int fcm_load(struct xmp_context *ctx, FILE *f)
{
    int i, j, k;
    struct xxm_event *event;
    struct fcm_header fh;
    uint8 fe[4];

    LOAD_INIT();

    fread (&fh, 1, sizeof (struct fcm_header), f);

    if (fh.magic[0] != 'F' || fh.magic[1] != 'C' || fh.magic[2] != '-' ||
	fh.magic[3] != 'M' || fh.name_id[0] != 'N')
	return -1;

    strncpy (m->name, fh.name, 20);
    sprintf (m->type, "FC-M %d.%d", fh.vmaj, fh.vmin);

    MODULE_INFO();

    m->xxh->len = fh.len;

    fread (m->xxo, 1, m->xxh->len, f);

    for (m->xxh->pat = i = 0; i < m->xxh->len; i++) {
	if (m->xxo[i] > m->xxh->pat)
	    m->xxh->pat = m->xxo[i];
    }
    m->xxh->pat++;

    m->xxh->trk = m->xxh->pat * m->xxh->chn;

    INSTRUMENT_INIT();

    for (i = 0; i < m->xxh->ins; i++) {
	B_ENDIAN16 (fh.ins[i].size);
	B_ENDIAN16 (fh.ins[i].loop_start);
	B_ENDIAN16 (fh.ins[i].loop_size);
	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);
	m->xxs[i].len = 2 * fh.ins[i].size;
	m->xxs[i].lps = 2 * fh.ins[i].loop_start;
	m->xxs[i].lpe = m->xxs[i].lps + 2 * fh.ins[i].loop_size;
	m->xxs[i].flg = fh.ins[i].loop_size > 1 ? WAVE_LOOPING : 0;
	m->xxi[i][0].fin = (int8)fh.ins[i].finetune << 4;
	m->xxi[i][0].vol = fh.ins[i].volume;
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].sid = i;
	m->xxih[i].nsm = !!(m->xxs[i].len);
	m->xxih[i].rls = 0xfff;
	if (m->xxi[i][0].fin > 48)
	    m->xxi[i][0].xpo = -1;
	if (m->xxi[i][0].fin < -48)
	    m->xxi[i][0].xpo = 1;

	if (V(1) && (strlen(m->xxih[i].name) || m->xxs[i].len > 2)) {
	    report ("[%2X] %04x %04x %04x %c V%02x %+d\n",
		i, m->xxs[i].len, m->xxs[i].lps, m->xxs[i].lpe,
		fh.ins[i].loop_size > 1 ? 'L' : ' ',
		m->xxi[i][0].vol, m->xxi[i][0].fin >> 4);
	}
    }

    PATTERN_INIT();

    /* Load and convert patterns */
    if (V(0))
	report ("Stored patterns: %d ", m->xxh->pat);

    fread (fe, 4, 1, f);	/* Skip 'SONG' pseudo chunk ID */

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	m->xxp[i]->rows = 64;
	TRACK_ALLOC (i);
	for (j = 0; j < 64; j++) {
	    for (k = 0; k < 4; k++) {
		event = &EVENT (i, k, j);
		fread (fe, 4, 1, f);
		cvt_pt_event (event, fe);
	    }
	}

	if (V(0))
	    report (".");
    }

    m->xxh->flg |= XXM_FLG_MODRNG;

    /* Load samples */

    fread (fe, 4, 1, f);	/* Skip 'SAMP' pseudo chunk ID */

    if (V(0))
	report ("\nStored samples : %d ", m->xxh->smp);
    for (i = 0; i < m->xxh->smp; i++) {
	if (!m->xxs[i].len)
	    continue;
	xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate, 0,
	    &m->xxs[m->xxi[i][0].sid], NULL);
	if (V(0))
	    report (".");
    }
    if (V(0))
	report ("\n");

    return 0;
}
