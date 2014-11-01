/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Loader for Imago Orpheus modules based on the format description
 * written by Lutz Roeder.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include "imf.h"
#include "period.h"

#define MAGIC_IM10	MAGIC4('I','M','1','0')
#define MAGIC_II10	MAGIC4('I','I','1','0')

static int imf_test (FILE *, char *, const int);
static int imf_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info imf_loader = {
    "IMF",
    "Imago Orpheus",
    imf_test,
    imf_load
};

static int imf_test(FILE *f, char *t, const int start)
{
    fseek(f, start + 60, SEEK_SET);
    if (read32b(f) != MAGIC_IM10)
	return -1;

    read_title(f, t, 32);

    return 0;
}

#define NONE 0xff
#define FX_IMF_FPORTA_UP 0xfe
#define FX_IMF_FPORTA_DN 0xfd

static uint8 arpeggio_val[32];

/* Effect conversion table */
static uint8 fx[] = {
	NONE,
	FX_S3M_TEMPO,
	FX_S3M_BPM,
	FX_TONEPORTA,
	FX_TONE_VSLIDE,
	FX_VIBRATO,
	FX_VIBRA_VSLIDE,
	FX_FINE4_VIBRA,
	FX_TREMOLO,
	FX_ARPEGGIO,
	FX_SETPAN,
	FX_PANSLIDE,
	FX_VOLSET,
	FX_VOLSLIDE,
	FX_F_VSLIDE,
	FX_FINETUNE,
	FX_NSLIDE_UP,
	FX_NSLIDE_DN,
	FX_PORTA_UP,
	FX_PORTA_DN,
	FX_IMF_FPORTA_UP,
	FX_IMF_FPORTA_DN,
	FX_FLT_CUTOFF,
	FX_FLT_RESN,
	FX_OFFSET,
	NONE /* fine offset */,
	FX_KEYOFF,
	FX_MULTI_RETRIG,
	FX_TREMOR,
	FX_JUMP,
	FX_BREAK,
	FX_GLOBALVOL,
	FX_G_VOLSLIDE,
	FX_EXTENDED,
	FX_CHORUS,
	FX_REVERB
};


/* Effect translation */
static void xlat_fx (int c, uint8 *fxt, uint8 *fxp)
{
    uint8 h = MSN (*fxp), l = LSN (*fxp);

    switch (*fxt = fx[*fxt]) {
    case FX_ARPEGGIO:			/* Arpeggio */
	if (*fxp)
	    arpeggio_val[c] = *fxp;
	else
	    *fxp = arpeggio_val[c];
	break;
    case FX_IMF_FPORTA_UP:
	*fxt = FX_PORTA_UP;
	if (*fxp < 0x30)
	    *fxp = LSN (*fxp >> 2) | 0xe0;
	else
	    *fxp = LSN (*fxp >> 4) | 0xf0;
	break;
    case FX_IMF_FPORTA_DN:
	*fxt = FX_PORTA_DN;
	if (*fxp < 0x30)
	    *fxp = LSN (*fxp >> 2) | 0xe0;
	else
	    *fxp = LSN (*fxp >> 4) | 0xf0;
	break;
    case FX_EXTENDED:			/* Extended effects */
	switch (h) {
	case 0x1:			/* Set filter */
	case 0x2:			/* Undefined */
	case 0x4:			/* Undefined */
	case 0x6:			/* Undefined */
	case 0x7:			/* Undefined */
	case 0x9:			/* Undefined */
	case 0xe:			/* Ignore envelope */
	case 0xf:			/* Invert loop */
	    *fxp = *fxt = 0;
	    break;
	case 0x3:			/* Glissando */
	    *fxp = l | (EX_GLISS << 4);
	    break;
	case 0x5:			/* Vibrato waveform */
	    *fxp = l | (EX_VIBRATO_WF << 4);
	    break;
	case 0x8:			/* Tremolo waveform */
	    *fxp = l | (EX_TREMOLO_WF << 4);
	    break;
	case 0xa:			/* Pattern loop */
	    *fxp = l | (EX_PATTERN_LOOP << 4);
	    break;
	case 0xb:			/* Pattern delay */
	    *fxp = l | (EX_PATT_DELAY << 4);
	    break;
	case 0xc:
	    if (l == 0)
		*fxt = *fxp = 0;
	}
	break;
    case NONE:				/* No effect */
	*fxt = *fxp = 0;
	break;
    }
}


static int imf_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int c, r, i, j;
    struct xxm_event *event = 0, dummy;
    struct imf_header ih;
    struct imf_instrument ii;
    struct imf_sample is;
    int pat_len, smp_num;
    uint8 n, b;

    LOAD_INIT();

    /* Load and convert header */
    fread(&ih.name, 32, 1, f);
    ih.len = read16l(f);
    ih.pat = read16l(f);
    ih.ins = read16l(f);
    ih.flg = read16l(f);
    fread(&ih.unused1, 8, 1, f);
    ih.tpo = read8(f);
    ih.bpm = read8(f);
    ih.vol = read8(f);
    ih.amp = read8(f);
    fread(&ih.unused2, 8, 1, f);
    ih.magic = read32b(f);

    for (i = 0; i < 32; i++) {
	fread(&ih.chn[i].name, 12, 1, f);
	ih.chn[i].status = read8(f);
	ih.chn[i].pan = read8(f);
	ih.chn[i].chorus = read8(f);
	ih.chn[i].reverb = read8(f);
    }

    fread(&ih.pos, 256, 1, f);

#if 0
    if (ih.magic != MAGIC_IM10)
	return -1;
#endif

    copy_adjust((uint8 *)m->name, (uint8 *)ih.name, 32);

    m->xxh->len = ih.len;
    m->xxh->ins = ih.ins;
    m->xxh->smp = 1024;
    m->xxh->pat = ih.pat;

    if (ih.flg & 0x01)
	m->xxh->flg |= XXM_FLG_LINEAR;

    m->xxh->tpo = ih.tpo;
    m->xxh->bpm = ih.bpm;

    sprintf(m->type, "IM10 (Imago Orpheus)");

    MODULE_INFO();

    for (m->xxh->chn = i = 0; i < 32; i++) {
	if (ih.chn[i].status != 0x00)
	    m->xxh->chn = i + 1;
	else
	    continue;
	m->xxc[i].pan = ih.chn[i].pan;
	m->xxc[i].cho = ih.chn[i].chorus;
	m->xxc[i].rvb = ih.chn[i].reverb;
	m->xxc[i].flg |= XXM_CHANNEL_FX;
    }
    m->xxh->trk = m->xxh->pat * m->xxh->chn;
 
    memcpy(m->xxo, ih.pos, m->xxh->len);
    for (i = 0; i < m->xxh->len; i++)
	if (m->xxo[i] == 0xff)
	    m->xxo[i]--;

    m->c4rate = C4_NTSC_RATE;
    m->quirk |= XMP_QRK_FINEFX;

    PATTERN_INIT();

    /* Read patterns */

    reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

    memset(arpeggio_val, 0, 32);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);

	pat_len = read16l(f) - 4;
	m->xxp[i]->rows = read16l(f);
	TRACK_ALLOC (i);

	r = 0;

	while (--pat_len >= 0) {
	    b = read8(f);

	    if (b == IMF_EOR) {
		r++;
		continue;
	    }

	    c = b & IMF_CH_MASK;
	    event = c >= m->xxh->chn ? &dummy : &EVENT (i, c, r);

	    if (b & IMF_NI_FOLLOW) {
		n = read8(f);
		switch (n) {
		case 255:
		case 160:	/* ??!? */
		    n = XMP_KEY_OFF;
		    break;	/* Key off */
		default:
		    n = 1 + 12 * MSN (n) + LSN (n);
		}

		event->note = n;
		event->ins = read8(f);
		pat_len -= 2;
	    }
	    if (b & IMF_FX_FOLLOWS) {
		event->fxt = read8(f);
		event->fxp = read8(f);
		xlat_fx (c, &event->fxt, &event->fxp);
		pat_len -= 2;
	    }
	    if (b & IMF_F2_FOLLOWS) {
		event->f2t = read8(f);
		event->f2p = read8(f);
		xlat_fx (c, &event->f2t, &event->f2p);
		pat_len -= 2;
	    }
	}
	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    INSTRUMENT_INIT();

    /* Read and convert instruments and samples */

    reportv(ctx, 0, "Instruments    : %d ", m->xxh->ins);

    reportv(ctx, 1, 
"\n     Instrument name                NSm Fade Env Smp# Len   Start End   C2Spd");

    for (smp_num = i = 0; i < m->xxh->ins; i++) {

	fread(&ii.name, 32, 1, f);
	fread(&ii.map, 120, 1, f);
	fread(&ii.unused, 8, 1, f);
	for (j = 0; j < 32; j++)
		ii.vol_env[j] = read16l(f);
	for (j = 0; j < 32; j++)
		ii.pan_env[j] = read16l(f);
	for (j = 0; j < 32; j++)
		ii.pitch_env[j] = read16l(f);
	for (j = 0; j < 3; j++) {
	    ii.env[j].npt = read8(f);
	    ii.env[j].sus = read8(f);
	    ii.env[j].lps = read8(f);
	    ii.env[j].lpe = read8(f);
	    ii.env[j].flg = read8(f);
	    fread(&ii.env[j].unused, 3, 1, f);
	}
	ii.fadeout = read16l(f);
	ii.nsm = read16l(f);
	ii.magic = read32b(f);

	if (ii.magic != MAGIC_II10)
	    return -2;

        if (ii.nsm)
 	    m->xxi[i] = calloc (sizeof (struct xxm_instrument), ii.nsm);

	m->xxih[i].nsm = ii.nsm;

	str_adj ((char *) ii.name);
	strncpy ((char *) m->xxih[i].name, ii.name, 24);

	memcpy (m->xxim[i].ins, ii.map, XXM_KEY_MAX);

	if (V(1) && (strlen((char *) ii.name) || ii.nsm))
	    report ("\n[%2X] %-31.31s %2d %4x %c%c%c ",
		i, ii.name, ii.nsm, ii.fadeout,
		ii.env[0].flg & 0x01 ? 'V' : '-',
		'-', '-');

	m->xxih[i].aei.npt = ii.env[0].npt;
	m->xxih[i].aei.sus = ii.env[0].sus;
	m->xxih[i].aei.lps = ii.env[0].lps;
	m->xxih[i].aei.lpe = ii.env[0].lpe;
	m->xxih[i].aei.flg = ii.env[0].flg & 0x01 ? XXM_ENV_ON : 0;
	m->xxih[i].aei.flg |= ii.env[0].flg & 0x02 ? XXM_ENV_SUS : 0;
	m->xxih[i].aei.flg |= ii.env[0].flg & 0x04 ?  XXM_ENV_LOOP : 0;

	if (m->xxih[i].aei.npt)
	    m->xxae[i] = calloc (4, m->xxih[i].aei.npt);

	for (j = 0; j < m->xxih[i].aei.npt; j++) {
	    m->xxae[i][j * 2] = ii.vol_env[j * 2];
	    m->xxae[i][j * 2 + 1] = ii.vol_env[j * 2 + 1];
	}

	for (j = 0; j < ii.nsm; j++, smp_num++) {

	    fread(&is.name, 13, 1, f);
	    fread(&is.unused1, 3, 1, f);
	    is.len = read32l(f);
	    is.lps = read32l(f);
	    is.lpe = read32l(f);
	    is.rate = read32l(f);
	    is.vol = read8(f);
	    is.pan = read8(f);
	    fread(&is.unused2, 14, 1, f);
	    is.flg = read8(f);
	    fread(&is.unused3, 5, 1, f);
	    is.ems = read16l(f);
	    is.dram = read32l(f);
	    is.magic = read32b(f);

	    m->xxi[i][j].sid = smp_num;
	    m->xxi[i][j].vol = is.vol;
	    m->xxi[i][j].pan = is.pan;
	    m->xxs[smp_num].len = is.len;
	    m->xxs[smp_num].lps = is.lps;
	    m->xxs[smp_num].lpe = is.lpe;
	    m->xxs[smp_num].flg = is.flg & 1 ? WAVE_LOOPING : 0;
	    m->xxs[smp_num].flg |= is.flg & 4 ? WAVE_16_BITS : 0;

	    if (V(1)) {
		if (j)
		    report("\n\t\t\t\t\t\t ");
		report ("[%02x] %05x %05x %05x %5d ",
		    j, is.len, is.lps, is.lpe, is.rate);
	    }
	    c2spd_to_note (is.rate, &m->xxi[i][j].xpo, &m->xxi[i][j].fin);

	    if (!m->xxs[smp_num].len)
		continue;

	    xmp_drv_loadpatch(ctx, f, m->xxi[i][j].sid, m->c4rate, 0,
		&m->xxs[m->xxi[i][j].sid], NULL);

	    reportv(ctx, 0, ".");
	}
    }
    m->xxh->smp = smp_num;
    m->xxs = realloc(m->xxs, sizeof (struct xxm_sample) * m->xxh->smp);

    reportv(ctx, 0, "\n");

    m->flags |= XMP_CTL_FILTER;
    m->quirk |= XMP_QUIRK_ST3;

    return 0;
}
