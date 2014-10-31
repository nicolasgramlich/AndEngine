/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* From the STMIK 0.2 documentation:
 *
 * "The STMIK uses a special [Scream Tracker] beta-V3.0 module format.
 *  Due to the module formats beta nature, the current STMIK uses a .STX
 *  extension instead of the normal .STM. I'm not intending to do a
 *  STX->STM converter, so treat STX as the format to be used in finished
 *  programs, NOT as a format to be used in distributing modules. A program
 *  called STM2STX is included, and it'll convert STM modules to the STX
 *  format for usage in your own programs."
 *
 * Tested using "Future Brain" from Mental Surgery by Future Crew and
 * STMs converted with STM2STX.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include "stx.h"
#include "s3m.h"
#include "period.h"


static int stx_test (FILE *, char *, const int);
static int stx_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info stx_loader = {
    "STX",
    "STMIK 0.2",
    stx_test,
    stx_load
};

static int stx_test(FILE *f, char *t, const int start)
{
    char buf[8];

    fseek(f, start + 20, SEEK_SET);
    if (fread(buf, 1, 8, f) < 8)
	return -1;
    if (memcmp(buf, "!Scream!", 8) && memcmp(buf, "BMOD2STM", 8))
	return -1;

    fseek(f, start + 60, SEEK_SET);
    if (fread(buf, 1, 4, f) < 4)
	return -1;
    if (memcmp(buf, "SCRM", 4))
	return -1;

    fseek(f, start + 0, SEEK_SET);
    read_title(f, t, 20);

    return 0;
}

#define FX_NONE 0xff

static uint16 *pp_ins;		/* Parapointers to instruments */
static uint16 *pp_pat;		/* Parapointers to patterns */

static uint8 fx[] = {
    FX_NONE,		FX_TEMPO,
    FX_JUMP,		FX_BREAK,
    FX_VOLSLIDE,	FX_PORTA_DN,
    FX_PORTA_UP,	FX_TONEPORTA,
    FX_VIBRATO,		FX_TREMOR,
    FX_ARPEGGIO
};


static int stx_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int c, r, i, broken = 0;
    struct xxm_event *event = 0, dummy;
    struct stx_file_header sfh;
    struct stx_instrument_header sih;
    uint8 n, b;
    uint16 x16;
    int bmod2stm = 0;

    LOAD_INIT();

    fread(&sfh.name, 20, 1, f);
    fread(&sfh.magic, 8, 1, f);
    sfh.psize = read16l(f);
    sfh.unknown1 = read16l(f);
    sfh.pp_pat = read16l(f);
    sfh.pp_ins = read16l(f);
    sfh.pp_chn = read16l(f);
    sfh.unknown2 = read16l(f);
    sfh.unknown3 = read16l(f);
    sfh.gvol = read8(f);
    sfh.tempo = read8(f);
    sfh.unknown4 = read16l(f);
    sfh.unknown5 = read16l(f);
    sfh.patnum = read16l(f);
    sfh.insnum = read16l(f);
    sfh.ordnum = read16l(f);
    sfh.unknown6 = read16l(f);
    sfh.unknown7 = read16l(f);
    sfh.unknown8 = read16l(f);
    fread(&sfh.magic2, 4, 1, f);

    /* BMOD2STM does not convert pitch */
    if (!strncmp ((char *) sfh.magic, "BMOD2STM", 8))
	bmod2stm = 1;

#if 0
    if ((strncmp ((char *) sfh.magic, "!Scream!", 8) &&
	!bmod2stm) || strncmp ((char *) sfh.magic2, "SCRM", 4))
	return -1;
#endif

    m->xxh->ins = sfh.insnum;
    m->xxh->pat = sfh.patnum;
    m->xxh->trk = m->xxh->pat * m->xxh->chn;
    m->xxh->len = sfh.ordnum;
    m->xxh->tpo = MSN (sfh.tempo);
    m->xxh->smp = m->xxh->ins;
    m->c4rate = C4_NTSC_RATE;

    /* STM2STX 1.0 released with STMIK 0.2 converts STMs with the pattern
     * length encoded in the first two bytes of the pattern (like S3M).
     */
    fseek(f, start + (sfh.pp_pat << 4), SEEK_SET);
    x16 = read16l(f);
    fseek(f, start + (x16 << 4), SEEK_SET);
    x16 = read16l(f);
    if (x16 == sfh.psize)
	broken = 1;

    strncpy(m->name, (char *)sfh.name, 20);
    if (bmod2stm)
	sprintf(m->type, "STMIK 0.2 (BMOD2STM)");
    else
	snprintf(m->type, XMP_NAMESIZE, "STMIK 0.2 (STM2STX 1.%d)",
							broken ? 0 : 1);

    MODULE_INFO();
 
    pp_pat = calloc (2, m->xxh->pat);
    pp_ins = calloc (2, m->xxh->ins);

    /* Read pattern pointers */
    fseek(f, start + (sfh.pp_pat << 4), SEEK_SET);
    for (i = 0; i < m->xxh->pat; i++)
	pp_pat[i] = read16l(f);

    /* Read instrument pointers */
    fseek(f, start + (sfh.pp_ins << 4), SEEK_SET);
    for (i = 0; i < m->xxh->ins; i++)
	pp_ins[i] = read16l(f);

    /* Skip channel table (?) */
    fseek(f, start + (sfh.pp_chn << 4) + 32, SEEK_SET);

    /* Read orders */
    for (i = 0; i < m->xxh->len; i++) {
	m->xxo[i] = read8(f);
	fseek(f, 4, SEEK_CUR);
    }
 
    INSTRUMENT_INIT();

    /* Read and convert instruments and samples */

    reportv(ctx, 1, "     Sample name    Len  LBeg LEnd L Vol C2Spd\n");

    for (i = 0; i < m->xxh->ins; i++) {
	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);
	fseek(f, start + (pp_ins[i] << 4), SEEK_SET);

	sih.type = read8(f);
	fread(&sih.dosname, 13, 1, f);
	sih.memseg = read16l(f);
	sih.length = read32l(f);
	sih.loopbeg = read32l(f);
	sih.loopend = read32l(f);
	sih.vol = read8(f);
	sih.rsvd1 = read8(f);
	sih.pack = read8(f);
	sih.flags = read8(f);
	sih.c2spd = read16l(f);
	sih.rsvd2 = read16l(f);
	fread(&sih.rsvd3, 4, 1, f);
	sih.int_gp = read16l(f);
	sih.int_512 = read16l(f);
	sih.int_last = read32l(f);
	fread(&sih.name, 28, 1, f);
	fread(&sih.magic, 4, 1, f);

	m->xxih[i].nsm = !!(m->xxs[i].len = sih.length);
	m->xxs[i].lps = sih.loopbeg;
	m->xxs[i].lpe = sih.loopend;
	if (m->xxs[i].lpe == 0xffff)
	    m->xxs[i].lpe = 0;
	m->xxs[i].flg = m->xxs[i].lpe > 0 ? WAVE_LOOPING : 0;
	m->xxi[i][0].vol = sih.vol;
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].sid = i;

	copy_adjust(m->xxih[i].name, sih.name, 12);

	if (V(1) &&
	    (strlen((char *) m->xxih[i].name) || (m->xxs[i].len > 1))) {
	    report ("[%2X] %-14.14s %04x %04x %04x %c V%02x %5d\n", i,
		m->xxih[i].name, m->xxs[i].len, m->xxs[i].lps, m->xxs[i].lpe, m->xxs[i].flg
		& WAVE_LOOPING ? 'L' : ' ', m->xxi[i][0].vol, sih.c2spd);
	}

	sih.c2spd = 8363 * sih.c2spd / 8448;
	c2spd_to_note (sih.c2spd, &m->xxi[i][0].xpo, &m->xxi[i][0].fin);
    }

    PATTERN_INIT();

    /* Read and convert patterns */
    reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	m->xxp[i]->rows = 64;
	TRACK_ALLOC (i);

	if (!pp_pat[i])
	    continue;

	fseek(f, start + (pp_pat[i] << 4), SEEK_SET);
	if (broken)
	    fseek(f, 2, SEEK_CUR);

	for (r = 0; r < 64; ) {
	    b = read8(f);

	    if (b == S3M_EOR) {
		r++;
		continue;
	    }

	    c = b & S3M_CH_MASK;
	    event = c >= m->xxh->chn ? &dummy : &EVENT (i, c, r);

	    if (b & S3M_NI_FOLLOW) {
		n = read8(f);

		switch (n) {
		case 255:
		    n = 0;
		    break;	/* Empty note */
		case 254:
		    n = XMP_KEY_OFF;
		    break;	/* Key off */
		default:
		    n = 25 + 12 * MSN (n) + LSN (n);
		}

		event->note = n;
		event->ins = read8(f);;
	    }

	    if (b & S3M_VOL_FOLLOWS) {
		event->vol = read8(f) + 1;
	    }

	    if (b & S3M_FX_FOLLOWS) {
		event->fxt = fx[read8(f)];
		event->fxp = read8(f);
		switch (event->fxt) {
		case FX_TEMPO:
		    event->fxp = MSN (event->fxp);
		    break;
		case FX_NONE:
		    event->fxp = event->fxt = 0;
		    break;
		}
	    }
	}

	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    free (pp_pat);
    free (pp_ins);

    /* Read samples */
    reportv(ctx, 0, "Stored samples : %d ", m->xxh->smp);

    for (i = 0; i < m->xxh->ins; i++) {
	xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate, 0,
	    &m->xxs[m->xxi[i][0].sid], NULL);
	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    m->quirk |= XMP_QRK_VSALL | XMP_QUIRK_ST3;

    return 0;
}
