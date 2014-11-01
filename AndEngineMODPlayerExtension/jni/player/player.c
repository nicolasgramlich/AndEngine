/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/*
 * Sat, 18 Apr 1998 20:23:07 +0200  Frederic Bujon <lvdl@bigfoot.com>
 * Pan effect bug fixed: In Fastracker II the track panning effect erases
 * the instrument panning effect, and the same should happen in xmp.
 */

/*
 * Fri, 26 Jun 1998 13:29:25 -0400 (EDT)
 * Reported by Jared Spiegel <spieg@phair.csh.rit.edu>
 * when the volume envelope is not enabled (disabled) on a sample, and a
 * notoff is delivered to ft2 (via either a noteoff in the note column or
 * command Kxx [where xx is # of ticks into row to give a noteoff to the
 * sample]), ft2 will set the volume of playback of the sample to 00h.
 *
 * Claudio's fix: implementing effect K
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "driver.h"
#include "period.h"
#include "effects.h"
#include "player.h"
#include "synth.h"


/* Values for multi-retrig */
static struct retrig_t rval[] = {
    {   0,  1,  1 }, {  -1,  1,  1 }, {  -2,  1,  1 }, {  -4,  1,  1 },
    {  -8,  1,  1 }, { -16,  1,  1 }, {   0,  2,  3 }, {   0,  1,  2 },
    {   0,  1,  1 }, {   1,  1,  1 }, {   2,  1,  1 }, {   4,  1,  1 },
    {   8,  1,  1 }, {  16,  1,  1 }, {   0,  3,  2 }, {   0,  2,  1 },
    {   0,  0,  1 }	/* Note cut */
};

/* Vibrato/tremolo waveform tables */
static int waveform[4][64] = {
   {   0,  24,  49,  74,  97, 120, 141, 161, 180, 197, 212, 224,
     235, 244, 250, 253, 255, 253, 250, 244, 235, 224, 212, 197,
     180, 161, 141, 120,  97,  74,  49,  24,   0, -24, -49, -74,
     -97,-120,-141,-161,-180,-197,-212,-224,-235,-244,-250,-253,
    -255,-253,-250,-244,-235,-224,-212,-197,-180,-161,-141,-120,
     -97, -74, -49, -24  },	/* Sine */

   {   0,  -8, -16, -24, -32, -40, -48, -56, -64, -72, -80, -88,
     -96,-104,-112,-120,-128,-136,-144,-152,-160,-168,-176,-184,
    -192,-200,-208,-216,-224,-232,-240,-248, 255, 248, 240, 232,
     224, 216, 208, 200, 192, 184, 176, 168, 160, 152, 144, 136,
     128, 120, 112, 104,  96,  88,  80,  72,  64,  56,  48,  40,
      32,  24,  16,   8  },	/* Ramp down */

   { 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
     255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255,
     255, 255, 255, 255, 255, 255, 255, 255,-255,-255,-255,-255,
    -255,-255,-255,-255,-255,-255,-255,-255,-255,-255,-255,-255,
    -255,-255,-255,-255,-255,-255,-255,-255,-255,-255,-255,-255,
    -255,-255,-255,-255  },	/* Square */

   {   0,   8,  16,  24,  32,  40,  48,  56,  64,  72,  80,  88,
      96, 104, 112, 120, 128, 136, 144, 152, 160, 168, 176, 184,
     192, 200, 208, 216, 224, 232, 240, 248,-255,-248,-240,-232,
    -224,-216,-208,-200,-192,-184,-176,-168,-160,-152,-144,-136,
    -128,-120,-112,-104, -96, -88, -80, -72, -64, -56, -48, -40,
     -32, -24, -16,  -8  }	/* Ramp up */
};

/*
 * "Anyway I think this is the most brilliant piece of crap we
 *  have managed to put up!"
 *			  -- Ice of FC about "Mental Surgery"
 */

static int fetch_channel (struct xmp_context *, struct xxm_event *, int, int);
static void play_channel (struct xmp_context *, int, int);


static void dummy(unsigned long ul, void *data)
{
    /* dummy */
}


static int get_envelope(int16 *env, int p, int x)
{
    int x1, x2, y1, y2;

    if (--p < 0)
	return 64;

    p <<= 1;

    if ((x1 = env[p]) <= x)
	return env[p + 1];

    do {
	p -= 2;
	x1 = env[p];
    } while ((x1 > x) && p);

    y1 = env[p + 1];
    x2 = env[p + 2];
    y2 = env[p + 3];

    return ((y2 - y1) * (x - x1) / (x2 - x1)) + y1;
}


static int do_envelope(struct xmp_context *ctx, struct xxm_envinfo *ei, uint16 *env, uint16 *x, int rl, int chn)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int loop;

    if (*x < 0xffff)
	(*x)++;

    if (!(ei->flg & XXM_ENV_ON))
	return 0;

    if (ei->npt <= 0)
	return 0;

    if (ei->lps >= ei->npt || ei->lpe >= ei->npt)
	loop = 0;
    else
	loop = ei->flg & XXM_ENV_LOOP;

    if (HAS_QUIRK(XMP_QRK_ITENV)) {
	if (!rl && (ei->flg & XXM_ENV_SUS)) {
	    if (*x >= env[ei->sue << 1])
		*x = env[ei->sus << 1];
	}
	else if (loop) {
	    if (*x >= env[ei->lpe << 1])
		*x = env[ei->lps << 1];
	}
    } else {
	if (!rl && (ei->flg & XXM_ENV_SUS) && *x > env[ei->sus << 1])
	    *x = env[ei->sus << 1];
	if (loop && *x >= env[ei->lpe << 1])
	    if (!(rl && (ei->flg & XXM_ENV_SUS) && ei->sus == ei->lpe))
		*x = env[ei->lps << 1];
    }

    if (chn < 0)
	return 0;

    if (*x > env[rl = (ei->npt - 1) << 1]) {
	if (!env[rl + 1])
	    xmp_drv_resetchannel(ctx, chn);
	else
	    return HAS_QUIRK(XMP_QRK_ENVFADE);
    }

    return 0;
}


static inline int copy_channel(struct xmp_player_context *p, int to, int from)
{
    if (to > 0 && to != from)
	memcpy(&p->xc_data[to], &p->xc_data[from], sizeof (struct xmp_channel));

    return to;
}


static inline void reset_channel(struct xmp_context *ctx)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_driver_context *d = &ctx->d;
    int i;
    struct xmp_channel *xc;

    synth_reset();
    memset(p->xc_data, 0, sizeof (struct xmp_channel) * d->numchn);

    for (i = d->numchn; i--; ) {
	xc = &p->xc_data[i];
	xc->insdef = xc->ins = xc->key = -1;
    }
    for (i = d->numtrk; i--; ) {
	xc = &p->xc_data[i];
	xc->masterpan = p->m.xxc[i].pan;
	xc->mastervol = p->m.xxc[i].vol; //0x40;
	xc->cutoff = 0xff;
    }
}


static inline void fetch_row(struct xmp_context *ctx, int ord, int row)
{
    int count, chn;
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;

    count = 0;
    for (chn = 0; chn < p->m.xxh->chn; chn++) {
	if (fetch_channel(ctx, &EVENT(ord, chn, row), chn, 1) != 0) {
	    p->fetch_ctl[chn]++;
	    count++;
	}
    }

    for (chn = 0; count; chn++) {
	if (p->fetch_ctl[chn]) {
	    fetch_channel(ctx, &EVENT(ord, chn, row), chn, 0);
	    p->fetch_ctl[chn] = 0;
	    count--;
	}
    }
}


static int fetch_channel(struct xmp_context *ctx, struct xxm_event *e, int chn, int ctl)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int xins, ins, smp, note, key, flg;
    struct xmp_channel *xc;
    int cont_sample;

    xc = &p->xc_data[chn];

    /* Tempo affects delay and must be computed first */
    if ((e->fxt == FX_TEMPO && e->fxp < 0x20) || e->fxt == FX_S3M_TEMPO) {
	if (e->fxp)
	    p->tempo = e->fxp;
    }
    if ((e->f2t == FX_TEMPO && e->f2p < 0x20) || e->f2t == FX_S3M_TEMPO) {
	if (e->f2p)
	    p->tempo = e->f2p;
    }

    /* Delay the entire fetch cycle */
    if (e->fxt == FX_EXTENDED && MSN(e->fxp) == EX_DELAY) {
	xc->delay = LSN(e->fxp) + 1;
	xc->delayed_event = e;
	if (e->ins)
		xc->delayed_ins = e->ins;
	e->fxt = e->fxp = 0;
	return 0;
    }
    if (e->f2t == FX_EXTENDED && MSN(e->f2p) == EX_DELAY) {
	xc->delay = LSN(e->f2p) + 1;
	xc->delayed_event = e;
	if (e->ins)
		xc->delayed_ins = e->ins;
	e->f2t = e->f2p = 0;
	return 0;
    }

    /* Emulate Impulse Tracker "always read instrument" bug */
    if (e->note && !e->ins && xc->delayed_ins && HAS_QUIRK(XMP_QRK_SAVEINS)) {
	e->ins = xc->delayed_ins;
	xc->delayed_ins = 0;
    }

    flg = 0;
    smp = ins = note = -1;
    xins = xc->ins;
    key = e->note;
    cont_sample = 0;

    /* Check instrument */

    if (e->ins) {
	ins = e->ins - 1;
	flg = NEW_INS | RESET_VOL | RESET_ENV;
	xc->fadeout = 0x8000;	/* for painlace.mod pat 0 ch 3 echo */
	xc->per_flags = 0;

	if (HAS_QUIRK(XMP_QRK_OINSMOD)) {
	    if (TEST(IS_READY)) {
		xins = xc->insdef;
		RESET(IS_READY);
	    }
	} else if ((uint32)ins < m->xxh->ins && m->xxih[ins].nsm) {	/* valid ins */
	    if (!key && HAS_QUIRK(XMP_QRK_INSPRI)) {
		if (xins == ins)
		    flg = NEW_INS | RESET_VOL;
		else 
		    key = xc->key + 1;
	    }
	    xins = ins;
	} else {					/* invalid ins */
	    if (!HAS_QUIRK(XMP_QRK_NCWINS))
		xmp_drv_resetchannel(ctx, chn);

	    if (HAS_QUIRK(XMP_QRK_IGNWINS)) {
		ins = -1;
		flg = 0;
	    }
	}

	xc->insdef = ins;
	xc->med_arp = xc->med_aidx = 0;
    }

    /* Check note */

    if (key) {
	flg |= NEW_NOTE;

	if (key == XMP_KEY_FADE) {
	    SET(FADEOUT);
	    flg &= ~(RESET_VOL | RESET_ENV);
	} else if (key == XMP_KEY_CUT) {
	    xmp_drv_resetchannel(ctx, chn);
	} else if (key == XMP_KEY_OFF) {
	    SET(RELEASE);
	    flg &= ~(RESET_VOL | RESET_ENV);
	} else if (e->fxt == FX_TONEPORTA || e->f2t == FX_TONEPORTA
		|| e->fxt == FX_TONE_VSLIDE || e->f2t == FX_TONE_VSLIDE) {
	    /* Fix portamento in 7spirits.s3m and mod.Biomechanoid */
	    if (HAS_QUIRK(XMP_QRK_RTGINS) && e->ins && xc->ins != ins) {
		flg |= NEW_INS;
		xins = ins;
	    } else {
		/* When a toneporta is issued after a keyoff event,
		 * retrigger the instrument (xr-nc.xm, bug #586377)
		 *
		 *   flg |= NEW_INS;
		 *   xins = ins;
		 *
		 * (From Decibelter - Cosmic 'Wegian Mamas.xm p04 ch7)
		 * We don't retrigger the sample, it simply continues.
		 * This is important to play sweeps and loops correctly.
		 */
		cont_sample = 1;

		/* set key to 0 so we can have the tone portamento from
		 * the original note (see funky_stars.xm pos 5 ch 9)
		 */
		key = 0;

		/* And do the same if there's no keyoff (see comic bakery
		 * remix.xm pos 1 ch 3)
		 */
	    }
	} else if (flg & NEW_INS) {
	    xins = ins;
	} else {
	    ins = xc->insdef;
	    flg |= IS_READY;
	}
    }

    if (!key || key >= XMP_KEY_OFF)
	ins = xins;

    if ((uint32)ins < m->xxh->ins && m->xxih[ins].nsm)
	flg |= IS_VALID;

    if ((uint32)key < XMP_KEY_OFF && key > 0) {
	xc->key = --key;

	if (flg & IS_VALID && key < XXM_KEY_MAX) {
	    if (m->xxim[ins].ins[key] != 0xff) {
		note = key + m->xxi[ins][m->xxim[ins].ins[key]].xpo +
						m->xxim[ins].xpo[key];
		smp = m->xxi[ins][m->xxim[ins].ins[key]].sid;
	    } else {
		flg &= ~(RESET_VOL | RESET_ENV | NEW_INS | NEW_NOTE);
	    }
	} else {
	    if (!HAS_QUIRK(XMP_QRK_CUTNWI))
		xmp_drv_resetchannel(ctx, chn);
	}
    }

    if (smp >= 0) {
	if (copy_channel(p, xmp_drv_setpatch(ctx, chn, ins, smp, note,
	 		m->xxi[ins][m->xxim[ins].ins[key]].nna,
	   		m->xxi[ins][m->xxim[ins].ins[key]].dct,
			m->xxi[ins][m->xxim[ins].ins[key]].dca, ctl,
			cont_sample), chn) < 0)
	{
	    return XMP_ERR_VIRTC;
	}
	xc->smp = smp;
    }

    /* Reset flags */
    xc->delay = xc->retrig = 0;
    xc->flags = flg | (xc->flags & 0xff000000);	/* keep persistent flags */

    xc->a_idx = 0;
    xc->a_size = 1;
    xc->a_val[0] = 0;

    if ((uint32)xins >= m->xxh->ins || !m->xxih[xins].nsm) {
	RESET(IS_VALID);
    } else {
	SET(IS_VALID);
    }

    xc->ins = xins;

    /* Process new volume */
    if (e->vol) {
	xc->volume = e->vol - 1;
	RESET(RESET_VOL);
	SET(NEW_VOL);
    }

    if (TEST(NEW_INS) || HAS_QUIRK(XMP_QRK_OFSRST))
	xc->offset_val = 0;

    /* Secondary effect is processed _first_ and can be overriden
     * by the primary effect.
     */
    process_fx(ctx, chn, e->note, e->f2t, e->f2p, xc, 1);
    process_fx(ctx, chn, e->note, e->fxt, e->fxp, xc, 0);

    if (!TEST(IS_VALID)) {
	xc->volume = 0;
	return 0;
    }

    if (note >= 0) {
	xc->note = note;

	if (cont_sample == 0) {
	    xmp_drv_voicepos(ctx, chn, xc->offset_val);
	    if (TEST(OFFSET) && HAS_QUIRK(XMP_QRK_FX9BUG))
		xc->offset_val <<= 1;
	}
	RESET(OFFSET);

	/* Fixed by Frederic Bujon <lvdl@bigfoot.com> */
	if (!TEST(NEW_PAN))
	    xc->pan = m->xxi[ins][m->xxim[ins].ins[key]].pan;

	if (!TEST(FINETUNE))
	    xc->finetune = m->xxi[ins][m->xxim[ins].ins[key]].fin;

	xc->s_end = xc->period = note_to_period(note, xc->finetune,
			m->xxh->flg & XXM_FLG_LINEAR);

	xc->y_idx = xc->t_idx = 0;	/* H: where should I put this? */

	SET(ECHOBACK);
    }

    if (xc->key < 0 || XXIM.ins[xc->key] == 0xff)
	return 0;

    if (TEST(RESET_ENV)) {
	/* xc->fadeout = 0x8000; -- moved to fetch */
	RESET(RELEASE | FADEOUT);

	/* H: where should I put these? */
	xc->gvl = XXI[XXIM.ins[xc->key]].gvl;
	xc->insvib_swp = XXI->vsw;
	xc->insvib_idx = 0;

	xc->v_idx = xc->p_idx = xc->f_idx = 0;
	xc->cutoff = XXI->ifc & 0x80 ? (XXI->ifc - 0x80) * 2 : 0xff;
	xc->resonance = XXI->ifr & 0x80 ? (XXI->ifr - 0x80) * 2 : 0;
    }

    if (TEST(RESET_VOL)) {
	xc->volume = XXI[XXIM.ins[xc->key]].vol;
	SET(ECHOBACK | NEW_VOL);
    }

    if (HAS_QUIRK(XMP_QRK_ST3GVOL) && TEST(NEW_VOL))
	xc->volume = xc->volume * m->volume / m->volbase;

    return 0;
}


static void play_channel(struct xmp_context *ctx, int chn, int t)
{
    struct xmp_channel *xc;
    int finalvol, finalpan, cutoff, act;
    int pan_envelope, frq_envelope;
    int med_arp, vibrato, med_vibrato;
    uint16 vol_envelope;
    struct xmp_player_context *p = &ctx->p;
    struct xmp_driver_context *d = &ctx->d;
    struct xmp_mod_context *m = &p->m;
    struct xmp_options *o = &ctx->o;

    xc = &p->xc_data[chn];

    /* Do delay */
    if (xc->delay && !--xc->delay) {
	if (fetch_channel(ctx, xc->delayed_event, chn, 1) != 0)
	    fetch_channel(ctx, xc->delayed_event, chn, 0);
    }

    if ((act = xmp_drv_cstat(ctx, chn)) == XMP_CHN_DUMB)
	return;

    if (!t && act != XMP_CHN_ACTIVE) {
	if (!TEST(IS_VALID) || act == XMP_ACT_CUT) {
	    xmp_drv_resetchannel(ctx, chn);
	    return;
	}
	xc->delay = xc->retrig = xc->a_idx = 0;
	xc->a_size = 1;
	xc->a_val[0] = 0;
	xc->flags &= (0xff000000 | IS_VALID);	/* keep persistent flags */
    }

    if (!TEST(IS_VALID))
	return;

    /* Process MED synth instruments */
    xmp_med_synth(ctx, chn, xc, !t && TEST(NEW_INS | NEW_NOTE));

    if (TEST(RELEASE) && !(XXIH.aei.flg & XXM_ENV_ON))
	xc->fadeout = 0;
 
    if (TEST(FADEOUT | RELEASE) || act == XMP_ACT_FADE || act == XMP_ACT_OFF) {
	xc->fadeout = xc->fadeout > XXIH.rls ? xc->fadeout - XXIH.rls : 0;

	if (xc->fadeout == 0) {

	    /* Setting the volume to 0 instead of resetting the channel
	     * will make us spend more CPU, but allows portamento after
	     * keyoff to continue the sample instead of resetting it.
	     * This is used in Decibelter - Cosmic 'Wegian Mamas.xm
	     * Only reset the channel in virtual channel mode so we
	     * can release it.
	     */
	     if (m->flags & XMP_CTL_VIRTUAL) {
		 xmp_drv_resetchannel(ctx, chn);
		 return;
	     } else {
		 xc->volume = 0;
	     }
	}
    }

    vol_envelope = XXIH.aei.flg & XXM_ENV_ON ?
	get_envelope((int16 *)XXAE, XXIH.aei.npt, xc->v_idx) : 64;

    pan_envelope = XXIH.pei.flg & XXM_ENV_ON ?
	get_envelope((int16 *)XXPE, XXIH.pei.npt, xc->p_idx) : 32;

    frq_envelope = XXIH.fei.flg & XXM_ENV_ON ?
	(int16)get_envelope((int16 *)XXFE, XXIH.fei.npt, xc->f_idx) : 0;

    /* Update envelopes */
    if (do_envelope(ctx, &XXIH.aei, XXAE, &xc->v_idx, DOENV_RELEASE, chn))
	SET(FADEOUT);
    do_envelope(ctx, &XXIH.pei, XXPE, &xc->p_idx, DOENV_RELEASE, -1323);
    do_envelope(ctx, &XXIH.fei, XXFE, &xc->f_idx, DOENV_RELEASE, -1137);

    /* Do note slide */
    if (TEST(NOTE_SLIDE)) {
	if (!--xc->ns_count) {
	    xc->note += xc->ns_val;
	    xc->period = note_to_period(xc->note, xc->finetune,
				m->xxh->flg & XXM_FLG_LINEAR);
	    xc->ns_count = xc->ns_speed;
	}
    }

    /* Do cut/retrig */
    if (xc->retrig) {
	if (!--xc->rcount) {
	    if (xc->rtype < 0x10)
		xmp_drv_retrig(ctx, chn);	/* don't retrig on cut */
	    xc->volume += rval[xc->rtype].s;
	    xc->volume *= rval[xc->rtype].m;
	    xc->volume /= rval[xc->rtype].d;
	    xc->rcount = xc->retrig;
	}
    }

    finalvol = xc->volume;

    if (TEST(TREMOLO))
	finalvol += waveform[xc->t_type][xc->t_idx] * xc->t_depth / 512;
    if (finalvol > p->gvol_base)
	finalvol = p->gvol_base;
    if (finalvol < 0)
	finalvol = 0;

    finalvol = (finalvol * xc->fadeout) >> 5;	/* 16 bit output */

    finalvol = (uint32) (vol_envelope *
	(HAS_QUIRK(XMP_QRK_ST3GVOL) ? 0x40 : m->volume) *
	xc->mastervol / 0x40 * ((int)finalvol * 0x40 / p->gvol_base)) >> 18;

    /* Volume translation table (for PTM, ARCH, COCO) */
    if (m->vol_table) {
	finalvol = m->volbase == 0xff ?
		m->vol_table[finalvol >> 2] << 2 :
		m->vol_table[finalvol >> 4] << 4;
    }

    if (m->xxh->flg & XXM_FLG_INSVOL)
	finalvol = (finalvol * XXIH.vol * xc->gvl) >> 12;

    med_vibrato = get_med_vibrato(xc);

    vibrato = ((TEST(VIBRATO) || TEST_PER(VIBRATO)) ?
	(waveform[xc->y_type][xc->y_idx] * xc->y_depth) >> 10 : 0) +
	waveform[XXI->vwf][xc->insvib_idx] * XXI->vde / (1024 *
	(1 + xc->insvib_swp));

    /* IT pitch envelopes are always linear, even in Amiga period mode.
     * Each unit in the envelope scale is 1/25 semitone.
     */
    xc->pitchbend = period_to_bend(
	xc->period + vibrato + med_vibrato,
	xc->note,
	/* xc->finetune, */
	m->xxh->flg & XXM_FLG_MODRNG,
	xc->gliss,
	m->xxh->flg & XXM_FLG_LINEAR);

    xc->pitchbend += XXIH.fei.flg & XXM_ENV_FLT ? 0 : frq_envelope;

    /* From Takashi Iwai's awedrv FAQ:
     *
     * Q3.9: Many clicking noises can be heard in some midi files.
     *    A: If this happens when panning status changes, it is due to the
     *       restriction of Emu8000 chip. Try -P option with drvmidi. This
     *       option suppress the realtime pan position change. Otherwise,
     *       it may be a bug.
     */

    finalpan = m->flags & XMP_CTL_DYNPAN ?  xc->pan + (pan_envelope - 32) *
			(128 - abs (xc->pan - 128)) / 32 : 0x80;
    finalpan = xc->masterpan + (finalpan - 128) *
			(128 - abs (xc->masterpan - 128)) / 128;

    if (o->cf_cutoff) {
	cutoff = o->cf_cutoff;		/* Click-filter cutoff */
    } else {
	cutoff = XXIH.fei.flg & XXM_ENV_FLT ? frq_envelope : 0xff;
	cutoff = xc->cutoff * cutoff / 0xff;
    }

    /* Echoback events */
    if (chn < d->numtrk) {
	xmp_drv_echoback(ctx, (finalpan << 12) | (chn << 4) | XMP_ECHO_CHN);

	if (TEST(ECHOBACK | PITCHBEND | TONEPORTA) ||
					TEST_PER(PITCHBEND | TONEPORTA)) {
	    xmp_drv_echoback(ctx, (xc->key << 12)|(xc->ins << 4)|XMP_ECHO_INS);
	    xmp_drv_echoback(ctx, (xc->volume << 4) * 0x40 / p->gvol_base |
							XMP_ECHO_VOL);

	    xmp_drv_echoback(ctx, (xc->pitchbend << 4) | XMP_ECHO_PBD);
	}
    }

    /* Do tremor */
    if (xc->tcnt_up || xc->tcnt_dn) {
	if (xc->tcnt_up > 0) {
	    if (xc->tcnt_up--)
		xc->tcnt_dn = LSN(xc->tremor);
	} else {
	    finalvol = 0;
	    if (xc->tcnt_dn--)
		xc->tcnt_up = MSN(xc->tremor);
	}
    }

    /* Do keyoff */
    if (xc->keyoff) {
	if (!--xc->keyoff)
	    SET(RELEASE);
    }

    /* Volume slides happen in all frames but the first, except when the
     * "volume slide on all frames" flag is set.
     */
    if (t % p->tempo || HAS_QUIRK(XMP_QRK_VSALL)) {
	if (!chn && p->gvol_flag) {
	    m->volume += p->gvol_slide;
	    if (m->volume < 0)
		m->volume = 0;
	    else if (m->volume > p->gvol_base)
		m->volume = p->gvol_base;
	}
	if (TEST(VOL_SLIDE) || TEST_PER(VOL_SLIDE))
	    xc->volume += xc->v_val;

	if (TEST_PER(VOL_SLIDE)) {
	    if (xc->v_val > 0 && xc->volume > m->volbase) {
		xc->volume = m->volbase;
		RESET_PER(VOL_SLIDE);
	    }
	    if (xc->v_val < 0 && xc->volume < 0) {
		xc->volume = 0;
		RESET_PER(VOL_SLIDE);
	    }
	}

	if (TEST(VOL_SLIDE_2))
	    xc->volume += xc->v_val2;

	if (TEST(TRK_VSLIDE))
	    xc->mastervol += xc->trk_val;
    }

    /* "Fine" sliding effects are processed in the first frame of each row,
     * and standard slides in the rest of the frames.
     */
    if (t % p->tempo || HAS_QUIRK(XMP_QRK_PBALL)) {
	/* Do pan and pitch sliding */
	if (TEST(PAN_SLIDE)) {
	    xc->pan += xc->p_val;
	    if (xc->pan < 0)
		xc->pan = 0;
	    else if (xc->pan > 0xff)
		xc->pan = 0xff;
	}

	if (TEST(PITCHBEND) || TEST_PER(PITCHBEND))
	    xc->period += xc->f_val;

	/* Do tone portamento */
	if (TEST(TONEPORTA) || TEST_PER(TONEPORTA)) {
	    xc->period += xc->s_sgn * xc->s_val;
	    if ((xc->s_sgn * xc->s_end) < (xc->s_sgn * xc->period)) {
		xc->period = xc->s_end;
		RESET(TONEPORTA);
		RESET_PER(TONEPORTA);
   	    }
	}

	/* Workaround for panic.s3m (from Toru Egashira's NSPmod) */
	if (xc->period <= 8)
	    xc->volume = 0;
    }

    if (t % p->tempo == 0) {
	/* Process "fine" effects */
	if (TEST(FINE_VOLS))
	    xc->volume += xc->v_fval;
	if (TEST(FINE_BEND))
	    xc->period = (4 * xc->period + xc->f_fval) / 4;
	if (TEST(TRK_FVSLIDE))
	    xc->mastervol += xc->trk_fval;
	if (TEST(FINE_NSLIDE)) {
	    xc->note += xc->ns_fval;
	    xc->period = note_to_period(xc->note, xc->finetune,
				m->xxh->flg & XXM_FLG_LINEAR);
	}
    }

    if (xc->volume < 0)
	xc->volume = 0;
    else if (xc->volume > p->gvol_base)
	xc->volume = p->gvol_base;

    if (xc->mastervol < 0)
	xc->mastervol = 0;
    else if (xc->mastervol > p->gvol_base)
	xc->mastervol = p->gvol_base;

    if (m->xxh->flg & XXM_FLG_LINEAR) {
	if (xc->period < MIN_PERIOD_L)
	    xc->period = MIN_PERIOD_L;
	else if (xc->period > MAX_PERIOD_L)
	    xc->period = MAX_PERIOD_L;
    } else {
	if (xc->period < MIN_PERIOD_A)
	    xc->period = MIN_PERIOD_A;
	else if (xc->period > MAX_PERIOD_A)
	    xc->period = MAX_PERIOD_A;
    }

    /* Update vibrato, tremolo and arpeggio indexes */
    xc->insvib_idx += XXI->vra >> 2;
    xc->insvib_idx %= 64;
    if (xc->insvib_swp > 1)
	xc->insvib_swp -= 2;
    else
	xc->insvib_swp = 0;
    xc->y_idx += xc->y_rate;
    xc->y_idx %= 64;
    xc->t_idx += xc->t_rate;
    xc->t_idx %= 64;
    xc->a_idx++;
    xc->a_idx %= xc->a_size;

    /* Process MED synth arpeggio */
    med_arp = get_med_arp(p, xc);

    /* Adjust pitch and pan, then play the note */
    finalpan = o->outfmt & XMP_FMT_MONO ?
	0 : (finalpan - 0x80) * o->mix / 100;
    xmp_drv_setbend(ctx, chn, xc->pitchbend + xc->a_val[xc->a_idx] + med_arp);
    xmp_drv_setpan(ctx, chn, m->flags & XMP_CTL_REVERSE ? -finalpan : finalpan);
    xmp_drv_setvol(ctx, chn, finalvol);

    if (cutoff < 0xff && (m->flags & XMP_CTL_FILTER)) {
	filter_setup(ctx, xc, cutoff);
	xmp_drv_seteffect(ctx, chn, XMP_FX_FILTER_B0, xc->flt_B0);
	xmp_drv_seteffect(ctx, chn, XMP_FX_FILTER_B1, xc->flt_B1);
	xmp_drv_seteffect(ctx, chn, XMP_FX_FILTER_B2, xc->flt_B2);
    } else {
	cutoff = 0xff;
    }

    xmp_drv_seteffect(ctx, chn, XMP_FX_RESONANCE, xc->resonance);
    xmp_drv_seteffect(ctx, chn, XMP_FX_CUTOFF, cutoff);
    xmp_drv_seteffect(ctx, chn, XMP_FX_CHORUS, m->xxc[chn].cho);
    xmp_drv_seteffect(ctx, chn, XMP_FX_REVERB, m->xxc[chn].rvb);
}


int _xmp_player_start(struct xmp_context *ctx)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_driver_context *d = &ctx->d;
	struct xmp_mod_context *m = &p->m;
	struct xmp_options *o = &ctx->o;
	struct flow_control *f = &p->flow;
	int ret;

	if (p->event_callback == NULL)
		p->event_callback = dummy;

	p->gvol_slide = 0;
	p->gvol_base = m->volbase;
	p->pos = f->ord = o->start;
	f->frame = 0;
	f->row = 0;
	f->time = 0;

	if (m->xxh->len == 0 || m->xxh->chn == 0) {
		/* set variables to sane state */
		m->flags &= ~XMP_CTL_LOOP;
		f->ord = p->scan_ord = 0;
		f->row = p->scan_row = 0;
		f->end_point = 0;
		return 0;
	}

	f->num_rows = m->xxp[m->xxo[f->ord]]->rows;

	/* Skip invalid patterns at start (the seventh laboratory.it) */
	while (f->ord < m->xxh->len && m->xxo[f->ord] >= m->xxh->pat)
		f->ord++;

	m->volume = m->xxo_info[f->ord].gvl;
	p->tick_time = m->rrate / (p->bpm = m->xxo_info[f->ord].bpm);
	p->tempo = m->xxo_info[f->ord].tempo;
	f->jumpline = m->xxo_fstrow[f->ord];
	f->playing_time = 0;
	f->end_point = p->scan_num;

	if ((ret = xmp_drv_on(ctx, m->xxh->chn)) != 0)
		return ret;

	f->jump = -1;

	p->fetch_ctl = calloc(m->xxh->chn, sizeof (int));
	f->loop_stack = calloc(d->numchn, sizeof (int));
	f->loop_start = calloc(d->numchn, sizeof (int));
	p->xc_data = calloc(d->numchn, sizeof (struct xmp_channel));
	if (!(p->fetch_ctl && f->loop_stack && f->loop_start && p->xc_data))
		return XMP_ERR_ALLOC;

	reset_channel(ctx);

	xmp_drv_starttimer(ctx);

	return 0;
}

int _xmp_player_frame(struct xmp_context *ctx)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_driver_context *d = &ctx->d;
	struct xmp_mod_context *m = &p->m;
	struct xmp_options *o = &ctx->o;
	struct flow_control *f = &p->flow;
	int i;

	if (p->pause) {
		p->event_callback(0, p->callback_data);
		return 0;
	}

	/* check reposition */
	if (f->ord != p->pos) {
		if (p->pos == -1)
			p->pos++;		/* restart module */

		if (p->pos == -2) {		/* set by xmp_module_stop */
			xmp_drv_bufwipe(ctx);
			return -1;		/* that's all folks */
		}

		if (p->pos == 0)
			f->end_point = p->scan_num;

		f->ord = p->pos;
		if (m->xxo_info[f->ord].tempo)
			p->tempo = m->xxo_info[f->ord].tempo;
		p->bpm = m->xxo_info[f->ord].bpm;
		p->tick_time = m->rrate / p->bpm;
		m->volume = m->xxo_info[f->ord].gvl;
		f->jump = f->ord;
		f->time = (double)m->xxo_info[f->ord].time / 1000;
		f->jumpline = m->xxo_fstrow[f->ord];
		f->row = -1;
		f->pbreak = 1;
		f->ord--;
		xmp_drv_bufwipe(ctx);
		xmp_drv_sync(ctx, 0);
		xmp_drv_reset(ctx);
		reset_channel(ctx);
		goto next_row;
	}

	/* check new row */

	if (f->frame == 0) {			/* first frame in row */
		/* check end of module */
	    	if ((~m->flags & XMP_CTL_LOOP) && f->ord == p->scan_ord &&
					f->row == p->scan_row) {
			if (!f->end_point--)
				return -1;
		}

		p->gvol_flag = 0;
		fetch_row(ctx, m->xxo[f->ord], f->row);

		xmp_drv_echoback(ctx, (p->tempo << 12) | (p->bpm << 4) |
							XMP_ECHO_BPM);
		xmp_drv_echoback(ctx, (m->volume << 4) | XMP_ECHO_GVL);
		xmp_drv_echoback(ctx, (m->xxo[f->ord] << 12) | (f->ord << 4) |
							XMP_ECHO_ORD);
		xmp_drv_echoback(ctx, (d->curvoc << 4) | XMP_ECHO_NCH);
		xmp_drv_echoback(ctx, ((m->xxp[m->xxo[f->ord]]->rows - 1)
				<< 12) | (f->row << 4) | XMP_ECHO_ROW);
		xmp_drv_echoback(ctx, ((int)(f->time * 10) << 4)
							| XMP_ECHO_TIME);
	}


	xmp_drv_echoback(ctx, (f->frame << 4) | XMP_ECHO_FRM);
	/* play_frame */
	for (i = 0; i < d->numchn; i++)
		play_channel(ctx, i, f->frame);

	if (o->time && (o->time < f->playing_time))	/* expired time */
		return -1;

	if (HAS_QUIRK(XMP_QRK_MEDBPM)) {
		xmp_drv_sync(ctx, p->tick_time * 33 / 125);
		f->playing_time += m->rrate * 33 / (100 * p->bpm * 125);
		f->time += m->rrate * 33 / (100 * p->bpm * 125);
	} else {
		xmp_drv_sync(ctx, p->tick_time);
		f->playing_time += m->rrate / (100 * p->bpm);
		f->time += m->rrate / (100 * p->bpm);
	}

	f->frame++;

	if (f->frame >= (p->tempo * (1 + f->delay))) {
next_row:
		f->frame = 0;
		f->delay = 0;

		if (f->pbreak) {
			f->pbreak = 0;

			if (f->jump != -1) {
				f->ord = f->jump - 1;
				f->jump = -1;
				goto next_order;
			}

			goto next_order;
		}

		if (f->loop_chn) {
			f->row = f->loop_start[--f->loop_chn] - 1;
			f->loop_chn = 0;
		}

		f->row++;

		/* check end of pattern */
		if (f->row >= f->num_rows) {
next_order:
    			f->ord++;

			/* Restart module */
			if (f->ord >= m->xxh->len) {
    				f->ord = ((uint32)m->xxh->rst > m->xxh->len ||
					(uint32)m->xxo[m->xxh->rst] >=
					m->xxh->pat) ?  0 : m->xxh->rst;
				m->volume = m->xxo_info[f->ord].gvl;
			}

			/* Skip invalid patterns */
			if (m->xxo[f->ord] >= m->xxh->pat) {
    				f->ord++;
    				goto next_order;
			}

			f->num_rows = m->xxp[m->xxo[f->ord]]->rows;
			if (f->jumpline >= f->num_rows)
				f->jumpline = 0;
			f->row = f->jumpline;
			f->jumpline = 0;

			p->pos = f->ord;

			/* Reset persistent effects at new pattern */
			if (HAS_QUIRK(XMP_QRK_PERPAT)) {
				int chn;
				for (chn = 0; chn < p->m.xxh->chn; chn++)
					p->xc_data[chn].per_flags = 0;
			}
		}
	}

	return 0;
}
    

void _xmp_player_end(struct xmp_context *ctx)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	struct flow_control *f = &p->flow;

	xmp_drv_echoback(ctx, XMP_ECHO_END);

	while (xmp_drv_getmsg(ctx) != XMP_ECHO_END)
		xmp_drv_bufdump(ctx);

	xmp_drv_stoptimer(ctx);
	xmp_drv_off(ctx);

	if (m->xxh->len == 0 || m->xxh->chn == 0)
                return;

	free(p->xc_data);
	free(f->loop_start);
	free(f->loop_stack);
	free(p->fetch_ctl);

}
