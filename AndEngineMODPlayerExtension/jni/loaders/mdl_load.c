/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* Note: envelope switching (effect 9) and sample status change (effect 8)
 * are not supported. Frequency envelopes will be added in xmp 1.2.0. In
 * MDL envelopes are defined for each instrument in a patch -- xmp accepts
 * only one envelope for each patch, and the enveope of the first instrument
 * will be taken.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include "iff.h"
#include "period.h"

#define MAGIC_DMDL	MAGIC4('D','M','D','L')


static int mdl_test (FILE *, char *, const int);
static int mdl_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info mdl_loader = {
    "MDL",
    "Digitrakker",
    mdl_test,
    mdl_load
};

static int mdl_test(FILE *f, char *t, const int start)
{
    uint16 id;

    if (read32b(f) != MAGIC_DMDL)
	return -1;

    read8(f);			/* version */
    id = read16b(f);

    if (id == 0x494e) {		/* IN */
	read32b(f);
	read_title(f, t, 32);
    } else {
	read_title(f, t, 0);
    }

    return 0;
}


#define MDL_NOTE_FOLLOWS	0x04
#define MDL_INSTRUMENT_FOLLOWS	0x08
#define MDL_VOLUME_FOLLOWS	0x10
#define MDL_EFFECT_FOLLOWS	0x20
#define MDL_PARAMETER1_FOLLOWS	0x40
#define MDL_PARAMETER2_FOLLOWS	0x80


struct mdl_envelope {
    uint8 num;
    uint8 data[30];
    uint8 sus;
    uint8 loop;
};

static int *i_index;
static int *s_index;
static int *v_index;	/* volume envelope */
static int *p_index;	/* pan envelope */
static int *f_index;	/* pitch envelope */
static int *c2spd;
static int *packinfo;
static int v_envnum;
static int p_envnum;
static int f_envnum;
static struct mdl_envelope *v_env;
static struct mdl_envelope *p_env;
static struct mdl_envelope *f_env;


/* Effects 1-6 (note effects) can only be entered in the first effect
 * column, G-L (volume-effects) only in the second column.
 */

static void xlat_fx_common(uint8 *t, uint8 *p)
{
    switch (*t) {
    case 0x00:			/* - - No effect */
	*p = 0;
	break;
    case 0x07:			/* 7 - Set BPM */
	*t = FX_S3M_BPM;
	break;
    case 0x08:			/* 8 - Set pan */
    case 0x09:			/* 9 - Set envelope -- not supported */
    case 0x0a:			/* A - Not used */
	*t = *p = 0x00;
	break;
    case 0x0b:			/* B - Position jump */
    case 0x0c:			/* C - Set volume */
    case 0x0d:			/* D - Pattern break */
	/* Like protracker */
	break;
    case 0x0e:			/* E - Extended */
	switch (MSN (*p)) {
	case 0x0:		/* E0 - not used */
	case 0x3:		/* E3 - not used */
	case 0x8:		/* Set sample status -- unsupported */
	    *t = *p = 0x00;
	    break;
	case 0x1:		/* Pan slide left */
	    *t = FX_PANSLIDE;
	    *p <<= 4;
	    break;
	case 0x2:		/* Pan slide right */
	    *t = FX_PANSLIDE;
	    *p &= 0x0f;
	    break;
	}
	break;
    case 0x0f:
	*t = FX_S3M_TEMPO;
	break;
    }
}

static void xlat_fx1(uint8 *t, uint8 *p)
{
    switch (*t) {
    case 0x05:			/* 5 - Arpeggio */
	*t = FX_ARPEGGIO;
	break;
    case 0x06:			/* 6 - Not used */
	*t = *p = 0x00;
	break;
    }

    xlat_fx_common(t, p);
}


static void xlat_fx2(uint8 *t, uint8 *p)
{
    switch (*t) {
    case 0x01:			/* G - Volume slide up */
	*t = FX_VOLSLIDE_UP;
	break;
    case 0x02:			/* H - Volume slide down */
	*t = FX_VOLSLIDE_DN;
	break;
    case 0x03:			/* I - Multi-retrig */
	*t = FX_MULTI_RETRIG;
	break;
    case 0x04:			/* J - Tremolo */
	*t = FX_TREMOLO;
	break;
    case 0x05:			/* K - Tremor */
	*t = FX_TREMOR;
	break;
    case 0x06:			/* L - Not used */
	*t = *p = 0x00;
	break;
    }

    xlat_fx_common(t, p);
}


static unsigned int get_bits(char i, uint8 **buf, int *len)
{
    static uint32 b = 0, n = 32;
    unsigned int x;

    if (i == 0) {
	b = readmem32l(*buf);
	*buf += 4; *len -= 4;
	n = 32;
	return 0;
    }

    x = b & ((1 << i) - 1);	/* get i bits */
    b >>= i;
    if ((n -= i) <= 24) {
	if (*len == 0)		/* FIXME: last few bits can't be consumed */
		return x;
	b |= readmem32l((*buf)++) << n;
	n += 8; (*len)--;
    }

    return x;
}

/* From the Digitrakker docs:
 *
 * The description of the sample-packmethode (1) [8bit packing]:...
 * ----------------------------------------------------------------
 *
 * The method is based on the Huffman algorithm. It's easy and very fast
 * and effective on samples. The packed sample is a bit stream:
 *
 *	     Byte 0    Byte 1    Byte 2    Byte 3
 *	Bit 76543210  fedcba98  nmlkjihg  ....rqpo
 *
 * A packed byte is stored in the following form:
 *
 *	xxxx10..0s => byte = <xxxx> + (number of <0> bits between
 *		s and 1) * 16 - 8;
 *	if s==1 then byte = byte xor 255
 *
 * If there are no <0> bits between the first bit (sign) and the <1> bit,
 * you have the following form:
 *
 *	xxx1s => byte = <xxx>; if s=1 then byte = byte xor 255
 */

static void unpack_sample8(uint8 *t, uint8 *f, int len, int l)
{
    int i, s;
    uint8 b, d;

    get_bits(0, &f, &len);

    for (i = b = d = 0; i < l; i++) {
	s = get_bits(1, &f, &len);
	if (get_bits(1, &f, &len)) {
	    b = get_bits(3, &f, &len);
	} else {
            b = 8;
	    while (len >= 0 && !get_bits(1, &f, &len))
		b += 16;
	    b += get_bits(4, &f, &len);
	}

	if (s)
	    b ^= 0xff;

	d += b;
	*t++ = d;
    }
}

/*
 * The description of the sample-packmethode (2) [16bit packing]:...
 * ----------------------------------------------------------------
 *
 * It works as methode (1) but it only crunches every 2nd byte (the high-
 * bytes of 16 bit samples). So when you depack 16 bit samples, you have to
 * read 8 bits from the data-stream first. They present the lowbyte of the
 * sample-word. Then depack the highbyte in the descripted way (methode [1]).
 * Only the highbytes are delta-values. So take the lowbytes as they are.
 * Go on this way for the whole sample!
 */

static void unpack_sample16(uint8 *t, uint8 *f, int len, int l)
{
    int i, lo, s;
    uint8 b, d;

    get_bits (0, &f, &len);

    for (i = lo = b = d = 0; i < l; i++) {
	lo = get_bits(8, &f, &len);
	s = get_bits(1, &f, &len);
	if (get_bits(1, &f, &len)) {
	    b = get_bits(3, &f, &len);
	} else {
            b = 8;
	    while (len >= 0 && !get_bits (1, &f, &len))
		b += 16;
	    b += get_bits(4, &f, &len);
	}

	if (s)
	    b ^= 0xff;
	d += b;

	*t++ = lo;
	*t++ = d;
    }
}


/*
 * IFF chunk handlers
 */

static void get_chunk_in(struct xmp_context *ctx, int size, FILE *f)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i;

    fread(m->name, 1, 32, f);
    fread(m->author, 1, 20, f);

    m->xxh->len = read16l(f);
    m->xxh->rst = read16l(f);
    read8(f);			/* gvol */
    m->xxh->tpo = read8(f);
    m->xxh->bpm = read8(f);

    for (i = 0; i < 32; i++) {
	uint8 chinfo = read8(f);
	if (chinfo & 0x80)
	    break;
	m->xxc[i].pan = chinfo << 1;
    }
    m->xxh->chn = i;
    fseek(f, 32 - i - 1, SEEK_CUR);

    fread(m->xxo, 1, m->xxh->len, f);

    MODULE_INFO();
}

static void get_chunk_pa(struct xmp_context *ctx, int size, FILE *f)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j, chn;
    int x;

    m->xxh->pat = read8(f);
    m->xxh->trk = m->xxh->pat * m->xxh->chn + 1;	/* Max */

    PATTERN_INIT();
    reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	chn = read8(f);
	m->xxp[i]->rows = (int)read8(f) + 1;

	fseek(f, 16, SEEK_CUR);		/* Skip pattern name */
	for (j = 0; j < chn; j++) {
	    x = read16l(f);
	    if (j < m->xxh->chn)
		m->xxp[i]->info[j].index = x;
	}
	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");
}

static void get_chunk_p0(struct xmp_context *ctx, int size, FILE *f)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j;
    uint16 x16;

    m->xxh->pat = read8(f);
    m->xxh->trk = m->xxh->pat * m->xxh->chn + 1;	/* Max */

    PATTERN_INIT();
    reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	m->xxp[i]->rows = 64;

	for (j = 0; j < 32; j++) {
	    x16 = read16l(f);
	    if (j < m->xxh->chn)
		m->xxp[i]->info[j].index = x16;
	}
	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");
}

static void get_chunk_tr(struct xmp_context *ctx, int size, FILE *f)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j, k, row, len;
    struct xxm_track *track;

    m->xxh->trk = read16l(f) + 1;
    m->xxt = realloc(m->xxt, sizeof (struct xxm_track *) * m->xxh->trk);

    reportv(ctx, 0, "Stored tracks  : %d ", m->xxh->trk);

    track = calloc (1, sizeof (struct xxm_track) +
	sizeof (struct xxm_event) * 256);

    /* Empty track 0 is not stored in the file */
    m->xxt[0] = calloc (1, sizeof (struct xxm_track) +
	256 * sizeof (struct xxm_event));
    m->xxt[0]->rows = 256;

    for (i = 1; i < m->xxh->trk; i++) {
	/* Length of the track in bytes */
	len = read16l(f);

	memset (track, 0, sizeof (struct xxm_track) +
            sizeof (struct xxm_event) * 256);

	for (row = 0; len;) {
	    j = read8(f);
	    len--;
	    switch (j & 0x03) {
	    case 0:
		row += j >> 2;
		break;
	    case 1:
		for (k = 0; k <= (j >> 2); k++)
		    memcpy (&track->event[row + k], &track->event[row - 1],
			sizeof (struct xxm_event));
		row += k - 1;
		break;
	    case 2:
		memcpy (&track->event[row], &track->event[j >> 2],
		    sizeof (struct xxm_event));
		break;
	    case 3:
		if (j & MDL_NOTE_FOLLOWS) {
		    uint8 b = read8(f);
		    len--;
		    track->event[row].note = b == 0xff ? XMP_KEY_OFF : b;
		}
		if (j & MDL_INSTRUMENT_FOLLOWS)
		    len--, track->event[row].ins = read8(f);
		if (j & MDL_VOLUME_FOLLOWS)
		    len--, track->event[row].vol = read8(f);
		if (j & MDL_EFFECT_FOLLOWS) {
		    len--, k = read8(f);
		    track->event[row].fxt = LSN (k);
		    track->event[row].f2t = MSN (k);
		}
		if (j & MDL_PARAMETER1_FOLLOWS)
		    len--, track->event[row].fxp = read8(f);
		if (j & MDL_PARAMETER2_FOLLOWS)
		    len--, track->event[row].f2p = read8(f);
		break;
	    }

	    xlat_fx1 (&track->event[row].fxt, &track->event[row].fxp);
	    xlat_fx2 (&track->event[row].f2t, &track->event[row].f2p);

	    row++;
	}

	if (row <= 64)
	    row = 64;
	else if (row <= 128)
	    row = 128;
	else row = 256;

	m->xxt[i] = calloc (1, sizeof (struct xxm_track) +
	    sizeof (struct xxm_event) * row);
	memcpy (m->xxt[i], track, sizeof (struct xxm_track) +
	    sizeof (struct xxm_event) * row);
	m->xxt[i]->rows = row;

	if (V(0) && !(i % m->xxh->chn))
	    report (".");
    }

    free (track);

    reportv(ctx, 0, "\n");
}

static void get_chunk_ii(struct xmp_context *ctx, int size, FILE *f)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j, k;
    int map, last_map;
    char buf[40];

    m->xxh->ins = read8(f);
    reportv(ctx, 0, "Instruments    : %d ", m->xxh->ins);

    INSTRUMENT_INIT();

    for (i = 0; i < m->xxh->ins; i++) {
	i_index[i] = read8(f);
	m->xxih[i].nsm = read8(f);
	fread(buf, 1, 32, f);
	buf[32] = 0;
	str_adj(buf);
	strncpy((char *)m->xxih[i].name, buf, 32);

	if (V(1) && (strlen((char *) m->xxih[i].name) || m->xxih[i].nsm)) {
	    report ("\n[%2X] %-32.32s %2d ", i_index[i], m->xxih[i].name,
		m->xxih[i].nsm);
	}

	m->xxi[i] = calloc (sizeof (struct xxm_instrument), m->xxih[i].nsm);

	for (j = 0; j < XXM_KEY_MAX; j++)
	    m->xxim[i].ins[j] = -1;

	for (last_map = j = 0; j < m->xxih[i].nsm; j++) {
	    int x;

	    m->xxi[i][j].sid = read8(f);
	    map = read8(f);
	    m->xxi[i][j].vol = read8(f);
	    for (k = last_map; k <= map; k++) {
		if (k < XXM_KEY_MAX)
		    m->xxim[i].ins[k] = j;
	    }
	    last_map = map + 1;

	    x = read8(f);		/* Volume envelope */
	    if (j == 0)
		v_index[i] = x & 0x80 ? x & 0x3f : -1;
	    if (~x & 0x40)
		m->xxi[i][j].vol = 0xff;

	    m->xxi[i][j].pan = read8(f) << 1;

	    x = read8(f);		/* Pan envelope */
	    if (j == 0)
		p_index[i] = x & 0x80 ? x & 0x3f : -1;
	    if (~x & 0x40)
		m->xxi[i][j].pan = 0x80;

	    x = read16l(f);
	    if (j == 0)
		m->xxih[i].rls = x;

	    m->xxi[i][j].vra = read8(f);	/* vibrato rate */
	    m->xxi[i][j].vde = read8(f);	/* vibrato delay */
	    m->xxi[i][j].vsw = read8(f);	/* vibrato sweep */
	    m->xxi[i][j].vwf = read8(f);	/* vibrato waveform */
	    read8(f);			/* Reserved */

	    x = read8(f);		/* Pitch envelope */
	    if (j == 0)
		f_index[i] = x & 0x80 ? x & 0x3f : -1;

	    if (V(1)) {
		report("%s[%2x] V%02x S%02x  ",
		    j ? "\n\t\t\t\t\t " : "", j, m->xxi[i][j].vol, m->xxi[i][j].sid);
		if (v_index[i] >= 0)
		    report("v%02x ", v_index[i]);
		else
		    report("v-- ");
		if (p_index[i] >= 0)
		    report("p%02x ", p_index[i]);
		else
		    report("p-- ");
		if (f_index[i] >= 0)
		    report("p%02x ", f_index[i]);
		else
		    report("f-- ");
	    } else if (V(0) == 1)
		report(".");
	}
    }
    reportv(ctx, 0, "\n");
}

static void get_chunk_is(struct xmp_context *ctx, int size, FILE *f)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i;
    char buf[64];
    uint8 x;

    m->xxh->smp = read8(f);
    m->xxs = calloc(sizeof (struct xxm_sample), m->xxh->smp);
    packinfo = calloc(sizeof (int), m->xxh->smp);

    reportv(ctx, 1, "Sample infos   : %d ", m->xxh->smp);

    for (i = 0; i < m->xxh->smp; i++) {
	s_index[i] = read8(f);		/* Sample number */
	fread(buf, 1, 32, f);
	buf[32] = 0;
	str_adj(buf);
	reportv(ctx, 2, "\n[%2X] %-32.32s ", s_index[i],buf);
	fseek(f, 8, SEEK_CUR);		/* Sample filename */

	c2spd[i] = read32l(f);

	m->xxs[i].len = read32l(f);
	m->xxs[i].lps = read32l(f);
	m->xxs[i].lpe = read32l(f);

	m->xxs[i].flg = m->xxs[i].lpe > 0 ? WAVE_LOOPING : 0;
	m->xxs[i].lpe = m->xxs[i].lps + m->xxs[i].lpe;
	if (m->xxs[i].lpe > 0)
	    m->xxs[i].lpe--;

	read8(f);			/* Volume in DMDL 0.0 */
	x = read8(f);
	m->xxs[i].flg |= (x & 0x01) ? WAVE_16_BITS : 0;
	m->xxs[i].flg |= (x & 0x02) ? WAVE_BIDIR_LOOP : 0;
	packinfo[i] = (x & 0x0c) >> 2;

	if (V(2)) {
	    report ("%05x%c %05x %05x %c %6d ",
		m->xxs[i].len,
		m->xxs[i].flg & WAVE_16_BITS ? '+' : ' ',
		m->xxs[i].lps,
		m->xxs[i].lpe,
		m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ',
		c2spd[i]);
	    switch (packinfo[i]) {
	    case 0:
		report ("[nopack]");
		break;
	    case 1:
		report ("[pack08]");
		break;
	    case 2:
		report ("[pack16]");
		break;
	    case 3:
		report ("[error ]");
		break;
	    }
	} else {
	    reportv(ctx, 1, ".");
	}
    }
    reportv(ctx, 1, "\n");
}

static void get_chunk_i0(struct xmp_context *ctx, int size, FILE *f)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i;
    char buf[64];
    uint8 x;

    m->xxh->ins = m->xxh->smp = read8(f);

    reportv(ctx, 0, "Instruments    : %d ", m->xxh->ins);

    INSTRUMENT_INIT();

    packinfo = calloc (sizeof (int), m->xxh->smp);

    for (i = 0; i < m->xxh->ins; i++) {
	m->xxih[i].nsm = 1;
	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);
	m->xxi[i][0].sid = i_index[i] = s_index[i] = read8(f);

	fread(buf, 1, 32, f);
	buf[32] = 0;
	str_adj(buf);			/* Sample name */
	reportv(ctx, 1, "\n[%2X] %-32.32s ", i_index[i], buf);
	fseek(f, 8, SEEK_CUR);		/* Sample filename */

	c2spd[i] = read16l(f);

	m->xxs[i].len = read32l(f);
	m->xxs[i].lps = read32l(f);
	m->xxs[i].lpe = read32l(f);

	m->xxs[i].flg = m->xxs[i].lpe > 0 ? WAVE_LOOPING : 0;
	m->xxs[i].lpe = m->xxs[i].lps + m->xxs[i].lpe;

	m->xxi[i][0].vol = read8(f);	/* Volume */
	m->xxi[i][0].pan = 0x80;

	x = read8(f);
	m->xxs[i].flg |= (x & 0x01) ? WAVE_16_BITS : 0;
	m->xxs[i].flg |= (x & 0x02) ? WAVE_BIDIR_LOOP : 0;
	packinfo[i] = (x & 0x0c) >> 2;

	if (V(1)) {
	    report ("%5d V%02x %05x%c %05x %05x ",
		c2spd[i],  m->xxi[i][0].vol,
		m->xxs[i].len, m->xxs[i].flg & WAVE_16_BITS ? '+' : ' ',
		m->xxs[i].lps, m->xxs[i].lpe);
	    switch (packinfo[i]) {
	    case 0:
		report ("[nopack]");
		break;
	    case 1:
		report ("[pack08]");
		break;
	    case 2:
		report ("[pack16]");
		break;
	    case 3:
		report ("[error ]");
		break;
	    }
	} else {
	    reportv(ctx, 0, ".");
	}
    }
    reportv(ctx, 0, "\n");
}

static void get_chunk_sa(struct xmp_context *ctx, int size, FILE *f)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    struct xmp_options *o = &ctx->o;
    int i, len;
    uint8 *smpbuf, *buf;

    if (o->skipsmp)
	return;

    reportv(ctx, 0, "Stored samples : %d ", m->xxh->smp);

    for (i = 0; i < m->xxh->smp; i++) {
	smpbuf = calloc (1, m->xxs[i].flg & WAVE_16_BITS ?
		m->xxs[i].len << 1 : m->xxs[i].len);

	switch (packinfo[i]) {
	case 0:
	    fread(smpbuf, 1, m->xxs[i].len, f);
	    break;
	case 1: 
	    len = read32l(f);
	    buf = malloc(len + 4);
	    fread(buf, 1, len, f);
	    unpack_sample8(smpbuf, buf, len, m->xxs[i].len);
	    free(buf);
	    break;
	case 2:
	    len = read32l(f);
	    buf = malloc(len + 4);
	    fread(buf, 1, len, f);
	    unpack_sample16(smpbuf, buf, len, m->xxs[i].len >> 1);
	    free(buf);
	    break;
	}
	
	xmp_drv_loadpatch(ctx, NULL, i, m->c4rate, XMP_SMP_NOLOAD, &m->xxs[i],
					(char *)smpbuf);

	free (smpbuf);

	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    free(packinfo);
}

static void get_chunk_ve(struct xmp_context *ctx, int size, FILE *f)
{
    int i;

    if ((v_envnum = read8(f)) == 0)
	return;

    reportv(ctx, 1, "Vol envelopes  : %d\n", v_envnum);

    v_env = calloc(v_envnum, sizeof (struct mdl_envelope));

    for (i = 0; i < v_envnum; i++) {
	v_env[i].num = read8(f);
	fread(v_env[i].data, 1, 30, f);
	v_env[i].sus = read8(f);
	v_env[i].loop = read8(f);
    }
}

static void get_chunk_pe(struct xmp_context *ctx, int size, FILE *f)
{
    int i;

    if ((p_envnum = read8(f)) == 0)
	return;

    reportv(ctx, 1, "Pan envelopes  : %d\n", p_envnum);

    p_env = calloc (p_envnum, sizeof (struct mdl_envelope));

    for (i = 0; i < p_envnum; i++) {
	p_env[i].num = read8(f);
	fread(p_env[i].data, 1, 30, f);
	p_env[i].sus = read8(f);
	p_env[i].loop = read8(f);
    }
}

static void get_chunk_fe(struct xmp_context *ctx, int size, FILE *f)
{
    int i;

    if ((f_envnum = read8(f)) == 0)
	return;

    reportv(ctx, 1, "Pitch envelopes: %d\n", f_envnum);

    f_env = calloc (f_envnum, sizeof (struct mdl_envelope));

    for (i = 0; i < f_envnum; i++) {
	f_env[i].num = read8(f);
	fread(f_env[i].data, 1, 30, f);
	f_env[i].sus = read8(f);
	f_env[i].loop = read8(f);
    }
}


static int mdl_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j, k, l;
    char buf[8];

    LOAD_INIT();

    /* Check magic and get version */
    read32b(f);
    fread(buf, 1, 1, f);

    /* IFFoid chunk IDs */
    iff_register ("IN", get_chunk_in);	/* Module info */
    iff_register ("TR", get_chunk_tr);	/* Tracks */
    iff_register ("SA", get_chunk_sa);	/* Sampled data */
    iff_register ("VE", get_chunk_ve);	/* Volume envelopes */
    iff_register ("PE", get_chunk_pe);	/* Pan envelopes */
    iff_register ("FE", get_chunk_fe);	/* Pitch envelopes */

    if (MSN(*buf)) {
	iff_register ("II", get_chunk_ii);	/* Instruments */
	iff_register ("PA", get_chunk_pa);	/* Patterns */
	iff_register ("IS", get_chunk_is);	/* Sample info */
    } else {
	iff_register ("PA", get_chunk_p0);	/* Old 0.0 patterns */
	iff_register ("IS", get_chunk_i0);	/* Old 0.0 Sample info */
    }

    /* MDL uses a degenerated IFF-style file format with 16 bit IDs and
     * little endian 32 bit chunk size. There's only one chunk per data
     * type i.e. one huge chunk for all the sampled instruments.
     */
    iff_idsize(2);
    iff_setflag(IFF_LITTLE_ENDIAN);

    sprintf(m->type, "DMDL %d.%d (Digitrakker)", MSN(*buf), LSN(*buf));

    m->volbase = 0xff;
    m->c4rate = C4_NTSC_RATE;

    v_envnum = p_envnum = f_envnum = 0;
    s_index = calloc(256, sizeof (int));
    i_index = calloc(256, sizeof (int));
    v_index = malloc(256 * sizeof (int));
    p_index = malloc(256 * sizeof (int));
    f_index = malloc(256 * sizeof (int));
    c2spd = calloc(256, sizeof (int));

    for (i = 0; i < 256; i++) {
	v_index[i] = p_index[i] = f_index[i] = -1;
    }

    /* Load IFFoid chunks */
    while (!feof(f))
	iff_chunk(ctx, f);

    iff_release();

#define EVENT(a, c, r)  m->xxt[m->xxp[a]->info[c].index]->event[r]

    /* Re-index instruments & samples */

    for (i = 0; i < m->xxh->pat; i++)
	for (j = 0; j < m->xxp[i]->rows; j++)
	    for (k = 0; k < m->xxh->chn; k++)
		for (l = 0; l < m->xxh->ins; l++) {
		    if (j >= m->xxt[m->xxp[i]->info[k].index]->rows)
			continue;
		    
		    if (EVENT(i, k, j).ins && EVENT(i, k, j).ins == i_index[l]) {
		    	EVENT(i, k, j).ins = l + 1;
			break;
		    }
		}

    for (i = 0; i < m->xxh->ins; i++) {

	/* FIXME: envelope timing is wrong */

	/* volume envelopes */
	if (v_index[i] >= 0) {
	    m->xxih[i].aei.flg = XXM_ENV_ON;
	    m->xxih[i].aei.npt = 16;
	    m->xxae[i] = calloc (4, m->xxih[i].aei.npt);

	    for (j = 0; j < v_envnum; j++) {
		if (v_index[i] == j) {
		    m->xxih[i].aei.flg |= v_env[j].sus & 0x10 ? XXM_ENV_SUS : 0;
		    m->xxih[i].aei.flg |= v_env[j].sus & 0x20 ? XXM_ENV_LOOP : 0;
		    m->xxih[i].aei.sus = v_env[j].sus & 0x0f;
		    m->xxih[i].aei.lps = v_env[j].loop & 0x0f;
		    m->xxih[i].aei.lpe = v_env[j].loop & 0xf0;
		    m->xxae[i][0] = 0;
		    for (k = 1; k < m->xxih[i].aei.npt; k++) {
			m->xxae[i][k * 2] = m->xxae[i][(k - 1) * 2] +
						v_env[j].data[(k - 1) * 2];
			if (v_env[j].data[k * 2] == 0)
			    break;
			m->xxae[i][k * 2 + 1] = v_env[j].data[(k - 1) * 2 + 1];
		    }
		    m->xxih[i].aei.npt = k;
		    break;
		}
	    }
	}

	/* pan envelopes */
	if (p_index[i] >= 0) {
	    m->xxih[i].pei.flg = XXM_ENV_ON;
	    m->xxih[i].pei.npt = 16;
	    m->xxpe[i] = calloc (4, m->xxih[i].pei.npt);

	    for (j = 0; j < p_envnum; j++) {
		if (p_index[i] == j) {
		    m->xxih[i].pei.flg |= p_env[j].sus & 0x10 ? XXM_ENV_SUS : 0;
		    m->xxih[i].pei.flg |= p_env[j].sus & 0x20 ? XXM_ENV_LOOP : 0;
		    m->xxih[i].pei.sus = p_env[j].sus & 0x0f;
		    m->xxih[i].pei.lps = p_env[j].loop & 0x0f;
		    m->xxih[i].pei.lpe = p_env[j].loop & 0xf0;
		    m->xxpe[i][0] = 0;

		    for (k = 1; k < m->xxih[i].pei.npt; k++) {
			m->xxpe[i][k * 2] = m->xxpe[i][(k - 1) * 2] +
						p_env[j].data[(k - 1) * 2];
			if (p_env[j].data[k * 2] == 0)
			    break;
			m->xxpe[i][k * 2 + 1] = p_env[j].data[(k - 1) * 2 + 1];
		    }
		    m->xxih[i].pei.npt = k;
		    break;
		}
	    }
	}

	/* pitch envelopes */
	if (f_index[i] >= 0) {
	    m->xxih[i].fei.flg = XXM_ENV_ON;
	    m->xxih[i].fei.npt = 16;
	    m->xxfe[i] = calloc (4, m->xxih[i].fei.npt);

	    for (j = 0; j < f_envnum; j++) {
		if (f_index[i] == j) {
		    m->xxih[i].fei.flg |= f_env[j].sus & 0x10 ? XXM_ENV_SUS : 0;
		    m->xxih[i].fei.flg |= f_env[j].sus & 0x20 ? XXM_ENV_LOOP : 0;
		    m->xxih[i].fei.sus = f_env[j].sus & 0x0f;
		    m->xxih[i].fei.lps = f_env[j].loop & 0x0f;
		    m->xxih[i].fei.lpe = f_env[j].loop & 0xf0;
		    m->xxfe[i][0] = 0;
		    m->xxfe[i][1] = 32;

		    for (k = 1; k < m->xxih[i].fei.npt; k++) {
			m->xxfe[i][k * 2] = m->xxfe[i][(k - 1) * 2] +
						f_env[j].data[(k - 1) * 2];
			if (f_env[j].data[k * 2] == 0)
			    break;
			m->xxfe[i][k * 2 + 1] = f_env[j].data[(k - 1) * 2 + 1] * 4;
		    }

		    m->xxih[i].fei.npt = k;
		    break;
		}
	    }
	}

	for (j = 0; j < m->xxih[i].nsm; j++)
	    for (k = 0; k < m->xxh->smp; k++)
		if (m->xxi[i][j].sid == s_index[k]) {
		    m->xxi[i][j].sid = k;
		    c2spd_to_note (c2spd[k], &m->xxi[i][j].xpo, &m->xxi[i][j].fin);
		    break;
		}
    }

    free (c2spd);
    free (f_index);
    free (p_index);
    free (v_index);
    free (i_index);
    free (s_index);

    if (v_envnum)
	free(v_env);
    if (p_envnum)
	free(p_env);
    if (f_envnum)
	free(f_env);

    m->quirk |= XMP_QRK_FINEFX;

    return 0;
}

