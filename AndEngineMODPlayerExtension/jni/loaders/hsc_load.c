/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"

/* Based on the HSC File Format Spec, by Simon Peter <dn.tlp@gmx.net>
 * 
 * "Although the format is most commonly known through the HSC-Tracker by
 *  Electronic Rats, it was originally developed by Hannes Seifert of NEO
 *  Software for use in their commercial game productions in the time of
 *  1991 - 1994. ECR just ripped his player and coded an editor around it."
 */


static int hsc_test (FILE *, char *, const int);
static int hsc_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info hsc_loader = {
    "HSC",
    "HSC-Tracker",
    hsc_test,
    hsc_load
};

static int hsc_test(FILE *f, char *t, const int start)
{
    int p, i, r, c;
    uint8 buf[1200];

    fseek(f, 128 * 12, SEEK_CUR);

    if (fread(buf, 1, 51, f) != 51)
	return -1;
;
    for (p = i = 0; i < 51; i++) {
	if (buf[i] == 0xff)
	    break;
	if (buf[i] > p)
	    p = buf[i];
    }
    if (!i || !p || i > 50 || p > 50)		/* Test number of patterns */
	return -1;		

    for (i = 0; i < p; i++) {
	fread(buf, 1, 64 * 9 * 2, f);
	for (r = 0; r < 64; r++) {
	    for (c = 0; c < 9; c++) {
		uint8 n = buf[r * 9 * 2 + c * 2];
		uint8 m = buf[r * 9 * 2 + c * 2 + 1];
		if (m > 0x06 && m < 0x10 && n != 0x80)	/* Test effects 07..0f */
		    return -1;
		if (MSN(m) > 6 && MSN(m) < 10)	/* Test effects 7x .. 9x */
		    return -1;
	    }
	}
    }

    read_title(f, t, 0);

    return 0;
}

static int hsc_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int pat, i, r, c;
    struct xxm_event *event;
    uint8 *x, *sid, e[2], buf[128 * 12];

    LOAD_INIT();

    fread(buf, 1, 128 * 12, f);

    x = buf;
    for (i = 0; i < 128; i++, x += 12) {
	if (x[9] & ~0x3 || x[10] & ~0x3)	/* Test waveform register */
	    break;
	if (x[8] & ~0xf)			/* Test feedback & algorithm */
	    break;
    }

    m->xxh->ins = i;

    fseek(f, start + 0, SEEK_SET);

    m->xxh->chn = 9;
    m->xxh->bpm = 135;
    m->xxh->tpo = 6;
    m->xxh->smp = 0;
    m->xxh->flg = XXM_FLG_LINEAR;

    sprintf(m->type, "HSC (HSC-Tracker)");

    MODULE_INFO();

    reportv(ctx, 1,
"               Modulator                       Carrier               Common\n"
"     Char Fr LS OL At De Su Re WS   Char Fr LS OL At De Su Re WS   Fbk Alg Fin\n");

    /* Read instruments */
    INSTRUMENT_INIT();

    fread (buf, 1, 128 * 12, f);
    sid = buf;
    for (i = 0; i < m->xxh->ins; i++, sid += 12) {
	xmp_cvt_hsc2sbi((char *)sid);

	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);
	m->xxih[i].nsm = 1;
	m->xxi[i][0].vol = 0x40;
	m->xxi[i][0].fin = (int8)sid[11] / 4;
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].xpo = 0;
	m->xxi[i][0].sid = i;
	m->xxih[i].rls = LSN(sid[7]) * 32;	/* carrier release */

	if (V(1)) {
	    int j, x;

	    for (j = x = 0; j < 12; j++)
		x |= sid[j];

	    if (!x)
		goto skip;

	    report ("[%2X] ", i);

	    report ("%c%c%c%c %2d ",
		sid[0] & 0x80 ? 'a' : '-', sid[0] & 0x40 ? 'v' : '-',
		sid[0] & 0x20 ? 's' : '-', sid[0] & 0x10 ? 'e' : '-',
		sid[0] & 0x0f);
	    report ("%2d %2d ", sid[2] >> 6, sid[2] & 0x3f);
	    report ("%2d %2d ", sid[4] >> 4, sid[4] & 0x0f);
	    report ("%2d %2d ", sid[6] >> 4, sid[6] & 0x0f);
	    report ("%2d   ", sid[8]);

	    report ("%c%c%c%c %2d ",
		sid[1] & 0x80 ? 'a' : '-', sid[1] & 0x40 ? 'v' : '-',
		sid[1] & 0x20 ? 's' : '-', sid[1] & 0x10 ? 'e' : '-',
		sid[1] & 0x0f);
	    report ("%2d %2d ", sid[3] >> 6, sid[3] & 0x3f);
	    report ("%2d %2d ", sid[5] >> 4, sid[5] & 0x0f);
	    report ("%2d %2d ", sid[7] >> 4, sid[7] & 0x0f);
	    report ("%2d   ", sid[9]);

	    report ("%2d  %2d %4d\n", sid[10] >> 1, sid[10] & 0x01,
							(int8)sid[11]);
	}
skip:
	xmp_drv_loadpatch(ctx, f, i, 0, 0, NULL, (char *)sid);
    }

    /* Read orders */
    for (pat = i = 0; i < 51; i++) {
	fread (&m->xxo[i], 1, 1, f);
	if (m->xxo[i] & 0x80)
	    break;			/* FIXME: jump line */
	if (m->xxo[i] > pat)
	    pat = m->xxo[i];
    }
    fseek(f, 50 - i, SEEK_CUR);
    m->xxh->len = i;
    m->xxh->pat = pat + 1;
    m->xxh->trk = m->xxh->pat * m->xxh->chn;

    if (V(0)) {
	report ("Module length  : %d patterns\n", m->xxh->len);
	report ("Instruments    : %d\n", m->xxh->ins);
	report ("Stored patterns: %d ", m->xxh->pat);
    }
    PATTERN_INIT();

    /* Read and convert patterns */
    for (i = 0; i < m->xxh->pat; i++) {
	int ins[9] = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	PATTERN_ALLOC (i);
	m->xxp[i]->rows = 64;
	TRACK_ALLOC (i);
        for (r = 0; r < m->xxp[i]->rows; r++) {
            for (c = 0; c < 9; c++) {
	        fread (e, 1, 2, f);
	        event = &EVENT (i, c, r);
		if (e[0] & 0x80) {
		    ins[c] = e[1] + 1;
		} else if (e[0] == 0x7f) {
		    event->note = XMP_KEY_OFF;
		} else if (e[0] > 0) {
		    event->note = e[0] + 13;
		    event->ins = ins[c];
		}

		event->fxt = 0;
		event->fxp = 0;

		if (e[1] == 0x01) {
		    event->fxt = 0x0d;
		    event->fxp = 0;
		}
	    }
	}
	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    for (i = 0; i < m->xxh->chn; i++) {
	m->xxc[i].pan = 0x80;
	m->xxc[i].flg = XXM_CHANNEL_FM;
    }

    return 0;
}

