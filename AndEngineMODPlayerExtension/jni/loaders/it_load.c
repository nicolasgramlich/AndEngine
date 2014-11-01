/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr.
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include "it.h"
#include "period.h"

#define MAGIC_IMPM	MAGIC4('I','M','P','M')
#define MAGIC_IMPS	MAGIC4('I','M','P','S')


static int it_test (FILE *, char *, const int);
static int it_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info it_loader = {
    "IT",
    "Impulse Tracker",
    it_test,
    it_load
};

static int it_test(FILE *f, char *t, const int start)
{
    if (read32b(f) != MAGIC_IMPM)
	return -1;

    read_title(f, t, 26);

    return 0;
}


#define	FX_NONE	0xff
#define FX_XTND 0xfe
#define L_CHANNELS 64


static uint32 *pp_ins;		/* Pointers to instruments */
static uint32 *pp_smp;		/* Pointers to samples */
static uint32 *pp_pat;		/* Pointers to patterns */
static uint8 arpeggio_val[64];
static uint8 last_h[64], last_fxp[64];

static uint8 fx[] = {
	/*   */ FX_NONE,
	/* A */ FX_S3M_TEMPO,
	/* B */ FX_JUMP,
	/* C */ FX_BREAK,
	/* D */ FX_VOLSLIDE,
	/* E */ FX_PORTA_DN,
	/* F */ FX_PORTA_UP,
	/* G */ FX_TONEPORTA,
	/* H */ FX_VIBRATO,
	/* I */ FX_TREMOR,
	/* J */ FX_ARPEGGIO,
	/* K */ FX_VIBRA_VSLIDE,
	/* L */ FX_TONE_VSLIDE,
	/* M */ FX_TRK_VOL,
	/* N */ FX_TRK_VSLIDE,
	/* O */ FX_OFFSET,
	/* P */ FX_NONE,
	/* Q */ FX_MULTI_RETRIG,
	/* R */ FX_TREMOLO,
	/* S */ FX_XTND,
	/* T */ FX_IT_BPM,
	/* U */ FX_NONE,
	/* V */ FX_GLOBALVOL,
	/* W */ FX_G_VOLSLIDE,
	/* X */ FX_NONE,
	/* Y */ FX_NONE,
	/* Z */ FX_FLT_CUTOFF
};

static char *nna[] = { "cut", "cont", "off", "fade" };
static char *dct[] = { "off", "note", "smp", "inst" };
static int dca2nna[] = { 0, 2, 3 };
static int new_fx;

int itsex_decompress8 (FILE *, void *, int, int);
int itsex_decompress16 (FILE *, void *, int, int);


static void xlat_fx(int c, struct xxm_event *e)
{
    uint8 h = MSN(e->fxp), l = LSN(e->fxp);

    switch (e->fxt = fx[e->fxt]) {
    case FX_ARPEGGIO:		/* Arpeggio */
	if (e->fxp)
	    arpeggio_val[c] = e->fxp;
	else
	    e->fxp = arpeggio_val[c];
	break;
    case FX_JUMP:
	e->fxp = ord_xlat[e->fxp];
	break;
    case FX_VIBRATO:		/* Old or new vibrato */
	if (new_fx)
	    e->fxt = FX_FINE2_VIBRA;
	break;
    case FX_XTND:		/* Extended effect */
	e->fxt = FX_EXTENDED;

	if (h == 0 && e->fxp == 0) {
	    h = last_h[c];
	    e->fxp = last_fxp[c];
	} else {
	    last_h[c] = h;
	    last_fxp[c] = e->fxp;
	}

	switch (h) {
	case 0x1:		/* Glissando */
	    e->fxp = 0x30 | l;
	    break;
	case 0x2:		/* Finetune */
	    e->fxp = 0x50 | l;
	    break;
	case 0x3:		/* Vibrato wave */
	    e->fxp = 0x40 | l;
	    break;
	case 0x4:		/* Tremolo wave */
	    e->fxp = 0x70 | l;
	    break;
	case 0x5:		/* Panbrello wave -- NOT IMPLEMENTED */
	    e->fxt = e->fxp = 0;
	    break;
	case 0x6:		/* Pattern delay */
	    e->fxp = 0xe0 | l;
	    break;
	case 0x7:		/* Instrument functions */
	    e->fxt = FX_IT_INSTFUNC;
	    e->fxp &= 0x0f;
	    break;
	case 0x8:		/* Set pan position */
	    e->fxt = FX_SETPAN;
	    e->fxp = l << 4;
	    break;
	case 0x9:		/* 0x91 = set surround -- NOT IMPLEMENTED */
	    e->fxt = e->fxp = 0;
	    break;
	case 0xb:		/* Pattern loop */
	    e->fxp = 0x60 | l;
	    break;
	case 0xc:		/* Note cut */
	case 0xd:		/* Note delay */
	    if ((e->fxp = l) == 0)
		e->fxp++;	/* SD0 and SC0 becomes SD1 and SC1 */
	    e->fxp |= h << 4;
	    break;
	case 0xe:		/* Pattern delay */
	    break;
	default:
	    e->fxt = e->fxp = 0;
	}
	break;
    case FX_FLT_CUTOFF:
	if (e->fxp > 0x7f && e->fxp < 0x90) {	/* Resonance */
	    e->fxt = FX_FLT_RESN;
	    e->fxp = (e->fxp - 0x80) * 16;
	} else {		/* Cutoff */
	    e->fxp *= 2;
	}
	break;
    case FX_TREMOR:
	if (!LSN(e->fxp))
	    e->fxp |= 0x01; 
	if (!MSN(e->fxp))
	    e->fxp |= 0x10; 
	break;
    case FX_NONE:		/* No effect */
	e->fxt = e->fxp = 0;
	break;
    }
}


static void xlat_volfx(struct xxm_event *event)
{
    int b;

    b = event->vol;
    event->vol = 0;

    if (b <= 0x40) {
	event->vol = b + 1;
    } else if (b >= 65 && b <= 74) {
	event->f2t = FX_EXTENDED;
	event->f2p = (EX_F_VSLIDE_UP << 4) | (b - 65);
    } else if (b >= 75 && b <= 84) {
	event->f2t = FX_EXTENDED;
	event->f2p = (EX_F_VSLIDE_DN << 4) | (b - 75);
    } else if (b >= 85 && b <= 94) {
	event->f2t = FX_VOLSLIDE_2;
	event->f2p = (b - 85) << 4;
    } else if (b >= 95 && b <= 104) {
	event->f2t = FX_VOLSLIDE_2;
	event->f2p = b - 95;
    } else if (b >= 105 && b <= 114) {
	event->f2t = FX_PORTA_DN;
	event->f2p = (b - 105) << 2;
    } else if (b >= 115 && b <= 124) {
	event->f2t = FX_PORTA_UP;
	event->f2p = (b - 115) << 2;
    } else if (b >= 128 && b <= 192) {
	if (b == 192)
	    b = 191;
	event->f2t = FX_SETPAN;
	event->f2p = (b - 128) << 2;
    } else if (b >= 193 && b <= 202) {
	event->f2t = FX_TONEPORTA;
	event->f2p = 1 << (b - 193);
#if 0
    } else if (b >= 193 && b <= 202) {
	event->f2t = FX_VIBRATO;
	event->f2p = ???;
#endif
    }
}


static void fix_name(uint8 *s, int l)
{
    int i;

    /* IT names can have 0 at start of data, replace with space */
    for (l--, i = 0; i < l; i++) {
	if (s[i] == 0)
	    s[i] = ' ';
    }
    for (i--; i >= 0 && s[i] == ' '; i--) {
	if (s[i] == ' ')
	    s[i] = 0;
    }
}


static int it_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    struct xmp_options *o = &ctx->o;
    int r, c, i, j, k, pat_len;
    struct xxm_event *event, dummy, lastevent[L_CHANNELS];
    struct it_file_header ifh;
    struct it_instrument1_header i1h;
    struct it_instrument2_header i2h;
    struct it_sample_header ish;
    struct it_envelope env;
    uint8 b, mask[L_CHANNELS];
    int max_ch, flag;
    int inst_map[120], inst_rmap[XXM_KEY_MAX];
    char tracker_name[80];
    int mpt = 0;	/* ModPlug Tracker has quirks */

    LOAD_INIT();

    /* Load and convert header */
    read32b(f);		/* magic */

    fread(&ifh.name, 26, 1, f);
    fread(&ifh.rsvd1, 2, 1, f);

    ifh.ordnum = read16l(f);
    ifh.insnum = read16l(f);
    ifh.smpnum = read16l(f);
    ifh.patnum = read16l(f);

    ifh.cwt = read16l(f);
    ifh.cmwt = read16l(f);
    ifh.flags = read16l(f);
    ifh.special = read16l(f);

    ifh.gv = read8(f);
    ifh.mv = read8(f);
    ifh.is = read8(f);
    ifh.it = read8(f);
    ifh.sep = read8(f);
    ifh.zero = read8(f);

    ifh.msglen = read16l(f);
    ifh.msgofs = read32l(f);

    fread(&ifh.rsvd2, 4, 1, f);
    fread(&ifh.chpan, 64, 1, f);
    fread(&ifh.chvol, 64, 1, f);

    strcpy (m->name, (char *) ifh.name);
    m->xxh->len = ifh.ordnum;
    m->xxh->ins = ifh.insnum;
    m->xxh->smp = ifh.smpnum;
    m->xxh->pat = ifh.patnum;
    pp_ins = m->xxh->ins ? calloc(4, m->xxh->ins) : NULL;
    pp_smp = calloc(4, m->xxh->smp);
    pp_pat = calloc(4, m->xxh->pat);
    m->xxh->tpo = ifh.is;
    m->xxh->bpm = ifh.it;
    m->xxh->flg = ifh.flags & IT_LINEAR_FREQ ? XXM_FLG_LINEAR : 0;
    m->xxh->flg |= (ifh.flags & IT_USE_INST) && (ifh.cmwt >= 0x200) ?
					XXM_FLG_INSVOL : 0;

    m->xxh->chn = 64;	/* Effects in muted channels are still processed! */

    for (i = 0; i < 64; i++) {
	if (ifh.chpan[i] == 100)	/* Surround -> center */
	    ifh.chpan[i] = 32;

	if (ifh.chpan[i] & 0x80) {	/* Channel mute */
	    ifh.chvol[i] = 0;
	    m->xxc[i].flg |= XXM_CHANNEL_MUTE;
	}

	if (ifh.flags & IT_STEREO) {
	    m->xxc[i].pan = (int)ifh.chpan[i] * 0x80 >> 5;
	    if (m->xxc[i].pan > 0xff)
		m->xxc[i].pan = 0xff;
	} else {
	    m->xxc[i].pan = 0x80;
	}

	m->xxc[i].vol = ifh.chvol[i];
    }
    fread(m->xxo, 1, m->xxh->len, f);
    clean_s3m_seq(m->xxh, m->xxo);

    new_fx = ifh.flags & IT_OLD_FX ? 0 : 1;

    /* S3M skips pattern 0xfe */
    for (i = 0; i < (m->xxh->len - 1); i++) {
	if (m->xxo[i] == 0xfe) {
	    memmove(&m->xxo[i], &m->xxo[i + 1], m->xxh->len - i - 1);
	    m->xxh->len--;
	}
    }
    for (i = 0; i < m->xxh->ins; i++)
	pp_ins[i] = read32l(f);
    for (i = 0; i < m->xxh->smp; i++)
	pp_smp[i] = read32l(f);
    for (i = 0; i < m->xxh->pat; i++)
	pp_pat[i] = read32l(f);

    m->c4rate = C4_NTSC_RATE;
    m->quirk |= XMP_QRK_FINEFX | XMP_QRK_ENVFADE;

    /* Identify tracker */

    switch (ifh.cwt >> 8) {
    case 0x00:
	sprintf(tracker_name, "unmo3");
	break;
    case 0x01:
    case 0x02:
	if (ifh.cmwt == 0x0200 && ifh.cwt == 0x0217) {
	    sprintf(tracker_name, "ModPlug Tracker 1.16");
	    /* ModPlug Tracker files aren't really IMPM 2.00 */
	    ifh.cmwt = ifh.flags & IT_USE_INST ? 0x214 : 0x100;	
	    mpt = 1;
	} else if (ifh.cwt == 0x0216) {
	    sprintf(tracker_name, "Impulse Tracker 2.14v3");
	} else if (ifh.cwt == 0x0217) {
	    sprintf(tracker_name, "Impulse Tracker 2.14v5");
	} else if (ifh.cwt == 0x0214 && !memcmp(ifh.rsvd2, "CHBI", 4)) {
	    sprintf(tracker_name, "Chibi Tracker");
	} else {
	    sprintf(tracker_name, "Impulse Tracker %d.%02x",
			(ifh.cwt & 0x0f00) >> 8, ifh.cwt & 0xff);
	}
	break;
    case 0x08:
	if (ifh.cwt == 0x0888) {
	    sprintf(tracker_name, "ModPlug Tracker >= 1.17");
	} else {
	    sprintf(tracker_name, "unknown (%04x)", ifh.cwt);
	}
	break;
    case 0x10:
	sprintf(tracker_name, "Schism Tracker %d.%02x",
			(ifh.cwt & 0x0f00) >> 8, ifh.cwt & 0xff);
	break;
    default:
	sprintf(tracker_name, "unknown (%04x)", ifh.cwt);
    }

    sprintf (m->type, "IMPM %d.%02x (%s)",
			ifh.cmwt >> 8, ifh.cmwt & 0xff, tracker_name);

    MODULE_INFO();

    reportv(ctx, 0, "Instr/FX mode  : %s/%s",
			ifh.flags & IT_USE_INST ? ifh.cmwt >= 0x200 ?
			"new" : "old" : "sample",
			ifh.flags & IT_OLD_FX ? "old" : "IT");

    if (~ifh.flags & IT_USE_INST)
	m->xxh->ins = m->xxh->smp;

    if (ifh.special & IT_HAS_MSG) {
	if ((m->comment = malloc(ifh.msglen + 1)) == NULL)
	    return -1;
	i = ftell(f);
	fseek(f, start + ifh.msgofs, SEEK_SET);
	reportv(ctx, 2, "\nMessage length : %d\n| ", ifh.msglen);
	for (j = 0; j < ifh.msglen; j++) {
	    b = read8(f);
	    if (b == '\r')
		b = '\n';
	    if ((b < 32 || b > 127) && b != '\n' && b != '\t')
		b = '.';
	    m->comment[j] = b;
	    reportv(ctx, 2, "%c", b);
	    if (b == '\n')
		reportv(ctx, 2, "| ");
	}
	m->comment[j] = 0;

	fseek(f, i, SEEK_SET);
    }

    INSTRUMENT_INIT();

    if (m->xxh->ins && V(0) && (ifh.flags & IT_USE_INST)) {
	report ("\nInstruments    : %d ", m->xxh->ins);
	if (V(1)) {
	    if (ifh.cmwt >= 0x200)
		report ("\n     Instrument name            NNA  DCT  DCA  "
		    "Fade  GbV Pan RV Env NSm FC FR");
	    else
		report ("\n     Instrument name            NNA  DNC  "
		    "Fade  VolEnv NSm");
	}
    }

    for (i = 0; i < m->xxh->ins; i++) {
	/*
	 * IT files can have three different instrument types: 'New'
	 * instruments, 'old' instruments or just samples. We need a
	 * different loader for each of them.
	 */

	if ((ifh.flags & IT_USE_INST) && (ifh.cmwt >= 0x200)) {
	    /* New instrument format */
	    fseek(f, start + pp_ins[i], SEEK_SET);

	    i2h.magic = read32b(f);
	    fread(&i2h.dosname, 12, 1, f);
	    i2h.zero = read8(f);
	    i2h.nna = read8(f);
	    i2h.dct = read8(f);
	    i2h.dca = read8(f);
	    i2h.fadeout = read16l(f);

	    i2h.pps = read8(f);
	    i2h.ppc = read8(f);
	    i2h.gbv = read8(f);
	    i2h.dfp = read8(f);
	    i2h.rv = read8(f);
	    i2h.rp = read8(f);
	    i2h.trkvers = read16l(f);

	    i2h.nos = read8(f);
	    i2h.rsvd1 = read8(f);
	    fread(&i2h.name, 26, 1, f);

	    fix_name(i2h.name, 26);

	    i2h.ifc = read8(f);
	    i2h.ifr = read8(f);
	    i2h.mch = read8(f);
	    i2h.mpr = read8(f);
	    i2h.mbnk = read16l(f);
	    fread(&i2h.keys, 240, 1, f);

	    copy_adjust(m->xxih[i].name, i2h.name, 24);
	    m->xxih[i].rls = i2h.fadeout << 6;

	    /* Envelopes */

#define BUILD_ENV(X) { \
            env.flg = read8(f); \
            env.num = read8(f); \
            env.lpb = read8(f); \
            env.lpe = read8(f); \
            env.slb = read8(f); \
            env.sle = read8(f); \
            for (j = 0; j < 25; j++) { \
            	env.node[j].y = read8(f); \
            	env.node[j].x = read16l(f); \
            } \
            env.unused = read8(f); \
	    m->xxih[i].X##ei.flg = env.flg & IT_ENV_ON ? XXM_ENV_ON : 0; \
	    m->xxih[i].X##ei.flg |= env.flg & IT_ENV_LOOP ? XXM_ENV_LOOP : 0; \
	    m->xxih[i].X##ei.flg |= env.flg & IT_ENV_SLOOP ? XXM_ENV_SUS : 0; \
	    m->xxih[i].X##ei.npt = env.num; \
	    m->xxih[i].X##ei.sus = env.slb; \
	    m->xxih[i].X##ei.sue = env.sle; \
	    m->xxih[i].X##ei.lps = env.lpb; \
	    m->xxih[i].X##ei.lpe = env.lpe; \
	    if (env.num) m->xx##X##e[i] = calloc (4, env.num); \
	    for (j = 0; j < env.num; j++) { \
		m->xx##X##e[i][j * 2] = env.node[j].x; \
		m->xx##X##e[i][j * 2 + 1] = env.node[j].y; \
	    } \
}

	    BUILD_ENV(a);
	    BUILD_ENV(p);
	    BUILD_ENV(f);
	    
	    if (m->xxih[i].pei.flg & XXM_ENV_ON)
		for (j = 0; j < m->xxih[i].pei.npt; j++)
		    m->xxpe[i][j * 2 + 1] += 32;

	    if (env.flg & IT_ENV_FILTER) {
		m->xxih[i].fei.flg |= XXM_ENV_FLT;
		for (j = 0; j < env.num; j++) {
		    m->xxfe[i][j * 2 + 1] += 32;
		    m->xxfe[i][j * 2 + 1] *= 4;
		}
	    } else {
		/* Pitch envelope is *50 to get fine interpolation */
		for (j = 0; j < env.num; j++)
		    m->xxfe[i][j * 2 + 1] *= 50;
	    }

	    /* See how many different instruments we have */
	    for (j = 0; j < XXM_KEY_MAX; j++)
		inst_map[j] = -1;

	    for (k = j = 0; j < XXM_KEY_MAX; j++) {
		c = i2h.keys[25 + j * 2] - 1;
		if (c < 0) {
		    m->xxim[i].ins[j] = 0xff;	/* No sample */
		    m->xxim[i].xpo[j] = 0;
		    continue;
		}
		if (inst_map[c] == -1) {
		    inst_map[c] = k;
		    inst_rmap[k] = c;
		    k++;
		}
		m->xxim[i].ins[j] = inst_map[c];
		m->xxim[i].xpo[j] = i2h.keys[24 + j * 2] - (j + 12);
	    }

	    m->xxih[i].nsm = k;
	    m->xxih[i].vol = i2h.gbv >> 1;

	    if (k) {
		m->xxi[i] = calloc (sizeof (struct xxm_instrument), k);
		for (j = 0; j < k; j++) {
		    m->xxi[i][j].sid = inst_rmap[j];
		    m->xxi[i][j].nna = i2h.nna;
		    m->xxi[i][j].dct = i2h.dct;
		    m->xxi[i][j].dca = dca2nna[i2h.dca & 0x03];
		    m->xxi[i][j].pan = i2h.dfp & 0x80 ? 0x80 : i2h.dfp * 4;
		    m->xxi[i][j].ifc = i2h.ifc;
		    m->xxi[i][j].ifr = i2h.ifr;
	        }
	    }

	    reportv(ctx, 1,
			"\n[%2X] %-26.26s %-4.4s %-4.4s %-4.4s %4d %4d  %2x "
			"%02x %c%c%c %3d %02x %02x ",
		i, i2h.name,
		i2h.nna < 4 ? nna[i2h.nna] : "none",
		i2h.dct < 4 ? dct[i2h.dct] : "none",
		i2h.dca < 3 ? nna[dca2nna[i2h.dca]] : "none",
		i2h.fadeout,
		i2h.gbv,
		i2h.dfp & 0x80 ? 0x80 : i2h.dfp * 4,
		i2h.rv,
		m->xxih[i].aei.flg & XXM_ENV_ON ? 'V' : '-',
		m->xxih[i].pei.flg & XXM_ENV_ON ? 'P' : '-',
		env.flg & 0x01 ? env.flg & 0x80 ? 'F' : 'P' : '-',
		m->xxih[i].nsm,
		i2h.ifc,
		i2h.ifr
	    );
	    reportv(ctx, 0, ".");

	} else if (ifh.flags & IT_USE_INST) {
/* Old instrument format */
	    fseek(f, start + pp_ins[i], SEEK_SET);

	    i1h.magic = read32b(f);
	    fread(&i1h.dosname, 12, 1, f);

	    i1h.zero = read8(f);
	    i1h.flags = read8(f);
	    i1h.vls = read8(f);
	    i1h.vle = read8(f);
	    i1h.sls = read8(f);
	    i1h.sle = read8(f);
	    i1h.rsvd1 = read16l(f);
	    i1h.fadeout = read16l(f);

	    i1h.nna = read8(f);
	    i1h.dnc = read8(f);
	    i1h.trkvers = read16l(f);
	    i1h.nos = read8(f);
	    i1h.rsvd2 = read8(f);

	    fread(&i1h.name, 26, 1, f);

	    fix_name(i1h.name, 26);

	    fread(&i1h.rsvd3, 6, 1, f);
	    fread(&i1h.keys, 240, 1, f);
	    fread(&i1h.epoint, 200, 1, f);
	    fread(&i1h.enode, 50, 1, f);

	    copy_adjust(m->xxih[i].name, i1h.name, 24);

	    m->xxih[i].rls = i1h.fadeout << 7;

	    m->xxih[i].aei.flg = i1h.flags & IT_ENV_ON ? XXM_ENV_ON : 0;
	    m->xxih[i].aei.flg |= i1h.flags & IT_ENV_LOOP ? XXM_ENV_LOOP : 0;
	    m->xxih[i].aei.flg |= i1h.flags & IT_ENV_SLOOP ? XXM_ENV_SUS : 0;
	    m->xxih[i].aei.lps = i1h.vls;
	    m->xxih[i].aei.lpe = i1h.vle;
	    m->xxih[i].aei.sus = i1h.sls;
	    m->xxih[i].aei.sue = i1h.sle;

	    for (k = 0; i1h.enode[k * 2] != 0xff; k++);
	    m->xxae[i] = calloc (4, k);
	    for (m->xxih[i].aei.npt = k; k--; ) {
		m->xxae[i][k * 2] = i1h.enode[k * 2];
		m->xxae[i][k * 2 + 1] = i1h.enode[k * 2 + 1];
	    }
	    
	    /* See how many different instruments we have */
	    for (j = 0; j < XXM_KEY_MAX; j++)
		inst_map[j] = -1;

	    for (k = j = 0; j < XXM_KEY_MAX; j++) {
		c = i1h.keys[25 + j * 2] - 1;
		if (c < 0) {
		    m->xxim[i].ins[j] = 0xff;	/* No sample */
		    m->xxim[i].xpo[j] = 0;
		    continue;
		}
		if (inst_map[c] == -1) {
		    inst_map[c] = k;
		    inst_rmap[k] = c;
		    k++;
		}
		m->xxim[i].ins[j] = inst_map[c];
		m->xxim[i].xpo[j] = i1h.keys[24 + j * 2] - (j + 12);
	    }

	    m->xxih[i].nsm = k;
	    m->xxih[i].vol = i2h.gbv >> 1;

	    if (k) {
		m->xxi[i] = calloc (sizeof (struct xxm_instrument), k);
		for (j = 0; j < k; j++) {
		    m->xxi[i][j].sid = inst_rmap[j];
		    m->xxi[i][j].nna = i1h.nna;
		    m->xxi[i][j].dct = i1h.dnc ? XXM_DCT_NOTE : XXM_DCT_OFF;
		    m->xxi[i][j].dca = XXM_DCA_CUT;
		    m->xxi[i][j].pan = 0x80;
	        }
	    }

	    reportv(ctx, 1, "\n[%2X] %-26.26s %-4.4s %-4.4s %4d  "
			"%2d %c%c%c %3d ",
		i, i1h.name,
		i1h.nna < 4 ? nna[i1h.nna] : "none",
		i1h.dnc ? "on" : "off",
		i1h.fadeout,
		m->xxih[i].aei.npt,
		m->xxih[i].aei.flg & XXM_ENV_ON ? 'V' : '-',
		m->xxih[i].aei.flg & XXM_ENV_LOOP ? 'L' : '-',
		m->xxih[i].aei.flg & XXM_ENV_SUS ? 'S' : '-',
		m->xxih[i].nsm
	    );
	    reportv(ctx, 0, ".");
	}
    }

    reportv(ctx, 0, "\nStored Samples : %d ", m->xxh->smp);

    if (V(2) || (~ifh.flags & IT_USE_INST && V(1)))
	report (
"\n     Sample name                Len   LBeg  LEnd  SBeg  SEnd  FlCv VlGv C5Spd"
	);
    
    for (i = 0; i < m->xxh->smp; i++) {
	if (~ifh.flags & IT_USE_INST)
	    m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);
	fseek(f, start + pp_smp[i], SEEK_SET);

	ish.magic = read32b(f);
	fread(&ish.dosname, 12, 1, f);
	ish.zero = read8(f);
	ish.gvl = mpt ? read8(f),0x40 : read8(f);
	ish.flags = read8(f);
	ish.vol = read8(f);
	fread(&ish.name, 26, 1, f);

	fix_name(ish.name, 26);

	ish.convert = read8(f);
	ish.dfp = read8(f);
	ish.length = read32l(f);
	ish.loopbeg = read32l(f);
	ish.loopend = read32l(f);
	ish.c5spd = read32l(f);
	ish.sloopbeg = read32l(f);
	ish.sloopend = read32l(f);
	ish.sample_ptr = read32l(f);

	ish.vis = read8(f);
	ish.vid = read8(f);
	ish.vir = read8(f);
	ish.vit = read8(f);

	/* Changed to continue to allow use-brdg.it and use-funk.it to
	 * load correctly (both IT 2.04)
	 */
	if (ish.magic != MAGIC_IMPS)
	    continue;
	
	if (ish.flags & IT_SMP_16BIT) {
	    m->xxs[i].len = ish.length * 2;
	    m->xxs[i].lps = ish.loopbeg * 2;
	    m->xxs[i].lpe = ish.loopend * 2;
	    m->xxs[i].flg = WAVE_16_BITS;
	} else {
	    m->xxs[i].len = ish.length;
	    m->xxs[i].lps = ish.loopbeg;
	    m->xxs[i].lpe = ish.loopend;
	}

	m->xxs[i].flg |= ish.flags & IT_SMP_LOOP ? WAVE_LOOPING : 0;
	m->xxs[i].flg |= ish.flags & IT_SMP_BLOOP ? WAVE_BIDIR_LOOP : 0;

	if (~ifh.flags & IT_USE_INST) {
	    /* Create an instrument for each sample */
	    m->xxi[i][0].vol = ish.vol;
	    m->xxi[i][0].pan = 0x80;
	    m->xxi[i][0].sid = i;
	    m->xxih[i].nsm = !!(m->xxs[i].len);
	    copy_adjust(m->xxih[i].name, ish.name, 24);
	} else {
	    copy_adjust(m->xxs[i].name, ish.name, 24);
	}

	if (V(2) || (~ifh.flags & IT_USE_INST && V(1))) {
	    if (strlen((char *) ish.name) || m->xxs[i].len > 1) {
		report (
		    "\n[%2X] %-26.26s %05x%c%05x %05x %05x %05x "
		    "%02x%02x %02x%02x %5d ",
		    i, ish.name,
		    m->xxs[i].len,
		    ish.flags & IT_SMP_16BIT ? '+' : ' ',
		    m->xxs[i].lps, m->xxs[i].lpe,
		    ish.sloopbeg, ish.sloopend,
		    ish.flags, ish.convert,
		    ish.vol, ish.gvl, ish.c5spd
		);
	    }
	}

	/* Convert C5SPD to relnote/finetune
	 *
	 * In IT we can have a sample associated with two or more
	 * instruments, but c5spd is a sample attribute -- so we must
	 * scan all xmp instruments to set the correct transposition
	 */
	
	for (j = 0; j < m->xxh->ins; j++) {
	    for (k = 0; k < m->xxih[j].nsm; k++) {
		if (m->xxi[j][k].sid == i) {
		    m->xxi[j][k].vol = ish.vol;
		    m->xxi[j][k].gvl = ish.gvl;
		    c2spd_to_note(ish.c5spd, &m->xxi[j][k].xpo, &m->xxi[j][k].fin);
		}
	    }
	}

	if (ish.flags & IT_SMP_SAMPLE && m->xxs[i].len > 1) {
	    int cvt = 0;

	    if (o->skipsmp)
		continue;

	    fseek(f, start + ish.sample_ptr, SEEK_SET);

	    if (~ish.convert & IT_CVT_SIGNED)
		cvt |= XMP_SMP_UNS;

	    /* Handle compressed samples using Tammo Hinrichs' routine */
	    if (ish.flags & IT_SMP_COMP) {
		char *buf;
		buf = calloc(1, m->xxs[i].len);

		if (ish.flags & IT_SMP_16BIT) {
		    itsex_decompress16(f, buf, m->xxs[i].len >> 1, 
					ish.convert & IT_CVT_DIFF);

		    /* decompression generates native-endian samples, but
		     * we want little-endian */
		    if (o->big_endian)
			xmp_cvt_sex(m->xxs[i].len, buf);
		} else {
		    itsex_decompress8(f, buf, m->xxs[i].len, ifh.cmwt == 0x0215);
		}

		xmp_drv_loadpatch(ctx, NULL, i, m->c4rate,
				XMP_SMP_NOLOAD | cvt, &m->xxs[i], buf);
		free (buf);
		reportv(ctx, 0, "c");
	    } else {
		if (o->skipsmp) {
		    fseek(f, m->xxs[i].len, SEEK_CUR);
		    continue;
		}

		xmp_drv_loadpatch(ctx, f, i, m->c4rate, cvt, &m->xxs[i], NULL);

		reportv(ctx, 0, ".");
	    }
	}
    }

    reportv(ctx, 0, "\nStored Patterns: %d ", m->xxh->pat);

    m->xxh->trk = m->xxh->pat * m->xxh->chn;
    memset(arpeggio_val, 0, 64);
    memset(last_h, 0, 64);
    memset(last_fxp, 0, 64);

    PATTERN_INIT();

    /* Read patterns */
    for (max_ch = i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	r = 0;
	/* If the offset to a pattern is 0, the pattern is empty */
	if (!pp_pat[i]) {
	    m->xxp[i]->rows = 64;
	    m->xxt[i * m->xxh->chn] = calloc (sizeof (struct xxm_track) +
		sizeof (struct xxm_event) * 64, 1);
	    m->xxt[i * m->xxh->chn]->rows = 64;
	    for (j = 0; j < m->xxh->chn; j++)
		m->xxp[i]->info[j].index = i * m->xxh->chn;
	    continue;
	}
	fseek(f, start + pp_pat[i], SEEK_SET);
	pat_len = read16l(f) /* - 4*/;
	m->xxp[i]->rows = read16l(f);
	TRACK_ALLOC (i);
	memset (mask, 0, L_CHANNELS);
	read16l(f);
	read16l(f);

	while (--pat_len >= 0) {
	    b = read8(f);
	    if (!b) {
		r++;
		continue;
	    }
	    c = (b - 1) & 63;

	    if (b & 0x80) {
		mask[c] = read8(f);
		pat_len--;
	    }
	    /*
	     * WARNING: we IGNORE events in disabled channels. Disabled
	     * channels should be muted only, but we don't know the
	     * real number of channels before loading the patterns and
	     * we don't want to set it to 64 channels.
	     */
	    event = c >= m->xxh->chn ? &dummy : &EVENT (i, c, r);
	    if (mask[c] & 0x01) {
		b = read8(f);

		if (b > 0x7f && b < 0xfd)
			b = 0;

		switch (b) {
		case 0xff:	/* key off */
		    b = XMP_KEY_OFF;
		    break;
		case 0xfe:	/* cut */
		    b = XMP_KEY_CUT;
		    break;
		case 0xfd:	/* fade */
		    b = XMP_KEY_FADE;
		    break;
		default:
		    if (b < 11)
			b = 0;
		    else
			b -= 11;
		}
		lastevent[c].note = event->note = b;
		pat_len--;
	    }
	    if (mask[c] & 0x02) {
		b = read8(f);
		lastevent[c].ins = event->ins = b;
		pat_len--;
	    }
	    if (mask[c] & 0x04) {
		b = read8(f);
		lastevent[c].vol = event->vol = b;
		xlat_volfx (event);
		pat_len--;
	    }
	    if (mask[c] & 0x08) {
		b = read8(f);
		event->fxt = b;
		event->fxp = read8(f);
		xlat_fx (c, event);
		lastevent[c].fxt = event->fxt;
		lastevent[c].fxp = event->fxp;
		pat_len -= 2;
	    }
	    if (mask[c] & 0x10) {
		event->note = lastevent[c].note;
	    }
	    if (mask[c] & 0x20) {
		event->ins = lastevent[c].ins;
	    }
	    if (mask[c] & 0x40) {
		event->vol = lastevent[c].vol;
		xlat_volfx (event);
	    }
	    if (mask[c] & 0x80) {
		event->fxt = lastevent[c].fxt;
		event->fxp = lastevent[c].fxp;
	    }
	}
	reportv(ctx, 0, ".");

	/* Scan channels, look for unused tracks */
	for (c = m->xxh->chn - 1; c >= max_ch; c--) {
	    for (flag = j = 0; j < m->xxt[m->xxp[i]->info[c].index]->rows; j++) {
		event = &EVENT (i, c, j);
		if (event->note || event->vol || event->ins || event->fxt ||
		    event->fxp || event->f2t || event->f2p) {
		    flag = 1;
		    break;
		}
	    }
	    if (flag && c > max_ch)
		max_ch = c;
	}
    }

    free(pp_pat);
    free(pp_smp);
    if (pp_ins)		/* sample mode has no instruments */
	free(pp_ins);

    m->xxh->chn = max_ch + 1;
    m->flags |= XMP_CTL_VIRTUAL | XMP_CTL_FILTER;
    m->quirk |= XMP_QUIRK_IT;
    if (~ifh.flags & IT_LINK_GXX)
	m->quirk |= XMP_QRK_UNISLD;

    reportv(ctx, 0, "\n");

    return 0;
}
