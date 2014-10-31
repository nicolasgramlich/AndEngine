/* Extended Module Player
 * Copyright (C) 1997-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include "common.h"
#include "driver.h"
#include "mixer.h"
#include "synth.h"
#include "smix.h"
#include "period.h"
#include "convert.h"


#define FLAG_ITPT	0x01
#define FLAG_16_BITS	0x02
#define FLAG_STEREO	0x04
#define FLAG_FILTER	0x08
#define FLAG_REVLOOP	0x10
#define FLAG_ACTIVE	0x20
#define FLAG_SYNTH	0x40
#define FIDX_FLAGMASK	(FLAG_ITPT | FLAG_16_BITS | FLAG_STEREO | FLAG_FILTER)

#define DOWNMIX_SHIFT	 12
#define LIM8_HI		 127
#define LIM8_LO		-127
#define LIM12_HI	 4095
#define LIM12_LO	-4096
#define LIM16_HI	 32767
#define LIM16_LO	-32768


void    smix_mn8norm      (struct voice_info *, int *, int, int, int, int);
void    smix_mn8itpt      (struct voice_info *, int *, int, int, int, int);
void    smix_mn16norm     (struct voice_info *, int *, int, int, int, int);
void    smix_mn16itpt     (struct voice_info *, int *, int, int, int, int);
void    smix_st8norm      (struct voice_info *, int *, int, int, int, int);
void    smix_st8itpt      (struct voice_info *, int *, int, int, int, int);
void    smix_st16norm     (struct voice_info *, int *, int, int, int, int);
void    smix_st16itpt	  (struct voice_info *, int *, int, int, int, int);

void    smix_mn8itpt_flt  (struct voice_info *, int *, int, int, int, int);
void    smix_mn16itpt_flt (struct voice_info *, int *, int, int, int, int);
void    smix_st8itpt_flt  (struct voice_info *, int *, int, int, int, int);
void    smix_st16itpt_flt (struct voice_info *, int *, int, int, int, int);

void    smix_synth	  (struct voice_info *, int *, int, int, int, int);

static void     out_su8norm     (char*, int*, int, int, int);
static void     out_su16norm    (int16*, int*, int, int, int);
static void     out_u8ulaw      (char*, int*, int, int, int);

/* Array index:
 *
 * bit 0: 0=non-interpolated, 1=interpolated
 * bit 1: 0=8 bit, 1=16 bit
 * bit 2: 0=mono, 1=stereo
 */

static void (*mix_fn[])() = {
    /* unfiltered */
    smix_mn8norm,
    smix_mn8itpt,
    smix_mn16norm,
    smix_mn16itpt,
    smix_st8norm,
    smix_st8itpt,
    smix_st16norm,
    smix_st16itpt,

    /* filtered */
    smix_mn8norm,
    smix_mn8itpt_flt,
    smix_mn16norm,
    smix_mn16itpt_flt,
    smix_st8norm,
    smix_st8itpt_flt,
    smix_st16norm,
    smix_st16itpt_flt
};

#define OUT_U8ULAW	0
#define OUT_SU8NORM	1
#define OUT_SU16NORM	2

static void (*out_fn[])() = {
    out_u8ulaw,
    out_su8norm,
    out_su16norm
};

/* Downmix 32bit samples to 8bit, signed or unsigned, mono or stereo output */
static void out_su8norm(char *dest, int *src, int num, int amp, int flags)
{
    int smp, lhi, llo, offs;
    int shift = DOWNMIX_SHIFT + 8 - amp;

    offs = (flags & XMP_FMT_UNS) ? 0x80 : 0;

    lhi = LIM8_HI + offs;
    llo = LIM8_LO + offs;

    for (; num--; ++src, ++dest) {
	smp = *src >> shift;
	*dest = smp > LIM8_HI ? lhi : smp < LIM8_LO ? llo : smp + offs;
    }
}


/* Downmix 32bit samples to 16bit, signed or unsigned, mono or stereo output */
static void out_su16norm(int16 *dest, int *src, int num, int amp, int flags)
{
    int smp, lhi, llo, offs;
    int shift = DOWNMIX_SHIFT - amp;

    offs = (flags & XMP_FMT_UNS) ? 0x8000 : 0;

    lhi = LIM16_HI + offs;
    llo = LIM16_LO + offs;

    for (; num--; ++src, ++dest) {
	smp = *src >> shift;
	*dest = smp > LIM16_HI ? lhi : smp < LIM16_LO ? llo : smp + offs;
    }
}


/* Downmix 32bit samples to 8bit, unsigned ulaw, mono or stereo output */
static void out_u8ulaw(char *dest, int *src, int num, int amp, int flags)
{
    int smp;
    int shift = DOWNMIX_SHIFT + 4 - amp;

    for (; num--; ++src, ++dest) {
	smp = *src >> shift;
	*dest = smp > LIM12_HI ? ulaw_encode(LIM12_HI) :
		smp < LIM12_LO ? ulaw_encode(LIM12_LO) : ulaw_encode (smp);
    }
}


/* Prepare the mixer for the next tick */
void smix_resetvar(struct xmp_context *ctx)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    struct xmp_smixer_context *s = &ctx->s;
    struct xmp_options *o = &ctx->o;

    s->ticksize = m->quirk & XMP_QRK_MEDBPM ?
	o->freq * m->rrate * 33 / p->bpm / 12500 :
    	o->freq * m->rrate / p->bpm / 100;

    if (s->buf32b) {
	s->dtright = s->dtleft = 0;
	memset(s->buf32b, 0, s->ticksize * s->mode * sizeof (int));
    }
}


/* Hipolito's rampdown anticlick */
static void smix_rampdown(struct xmp_context *ctx, int voc, int32 *buf, int cnt)
{
    struct xmp_driver_context *d = &ctx->d;
    struct xmp_smixer_context *s = &ctx->s;
    int smp_l, smp_r;
    int dec_l, dec_r;

    if (voc < 0) {
	/* initialize */
	smp_r = s->dtright;
	smp_l = s->dtleft;
    } else {
	smp_r = d->voice_array[voc].sright;
	smp_l = d->voice_array[voc].sleft;
	d->voice_array[voc].sright = d->voice_array[voc].sleft = 0;
    }

    if (!smp_l && !smp_r)
	return;

    if (!buf) {
	buf = s->buf32b;
	cnt = SLOW_RELEASE; //s->ticksize;
    }
    if (!cnt)
	return;

    dec_r = smp_r / cnt;
    dec_l = smp_l / cnt;

    while ((smp_r || smp_l) && cnt--) {
	if (dec_r > 0)
	    *(buf++) += smp_r > dec_r ? (smp_r -= dec_r) : (smp_r = 0);
	else
	    *(buf++) += smp_r < dec_r ? (smp_r -= dec_r) : (smp_r = 0);

	if (dec_l > 0)
	    *(buf++) += smp_l > dec_l ? (smp_l -= dec_l) : (smp_l = 0);
	else
	    *(buf++) += smp_l < dec_l ? (smp_l -= dec_l) : (smp_l = 0);
    }
}


/* Ok, it's messy, but it works :-) Hipolito */
static void smix_anticlick(struct xmp_context *ctx, int voc, int vol, int pan, int *buf, int cnt)
{
    int oldvol, newvol, pan0;
    struct xmp_driver_context *d = &ctx->d;
    struct xmp_smixer_context *s = &ctx->s;
    struct voice_info *vi = &d->voice_array[voc];

    if (d->ext)
	return;			/* Anticlick is useful for softmixer only */

    /* From: Mirko Buffoni <mirbuf@gmail.com>
     * To: Claudio Matsuoka <cmatsuoka@gmail.com>
     * Date: Nov 29, 2007 6:45 AM
     *	
     * Put PAN SEPARATION to 100. Then it crashes. Other modules crash when
     * PAN SEPARATION = 100, (...) moving separation one step behind, stop
     * crashes.
     */
    pan0 = vi->pan;
    if (pan0 < -127)
	pan0 = -127;

    if (vi->vol) {
	oldvol = vi->vol * (0x80 - pan0);
	newvol = vol * (0x80 - pan);
	vi->sright -= vi->sright / oldvol * newvol;

	oldvol = vi->vol * (0x80 + pan0);
	newvol = vol * (0x80 + pan);
	vi->sleft -= vi->sleft / oldvol * newvol;
    }

    if (!buf) {
	s->dtright += vi->sright;
	s->dtleft += vi->sleft;
	vi->sright = vi->sleft = 0;
    } else {
	smix_rampdown(ctx, voc, buf, cnt);
    }
}


/* Fill the output buffer calling one of the handlers. The buffer contains
 * sound for one tick (a PAL frame or 1/50s for standard vblank-timed mods)
 */
int xmp_smix_softmixer(struct xmp_context *ctx)
{
    struct xmp_driver_context *d = &ctx->d;
    struct xmp_smixer_context *s = &ctx->s;
    struct voice_info *vi;
    struct patch_info *pi;
    int smp_cnt, tic_cnt, lps, lpe;
    int vol_l, vol_r, itp_inc, voc;
    int prv_l, prv_r;
    int synth = 1;
    int *buf_pos;

    if (!d->ext)
	smix_rampdown(ctx, -1, NULL, 0);	/* Anti-click */

    for (voc = d->maxvoc; voc--; ) {
	vi = &d->voice_array[voc];

	if (vi->chn < 0)
	    continue;

	if (vi->period < 1) {
	    xmp_drv_resetvoice(ctx, voc, 1);
	    continue;
	}

	buf_pos = s->buf32b;
	vol_r = vi->vol * (0x80 - vi->pan);
	vol_l = vi->vol * (0x80 + vi->pan);

	if (vi->fidx & FLAG_SYNTH) {
	    if (synth) {
		smix_synth(vi, buf_pos, s->ticksize, vol_l, vol_r,
						vi->fidx & FLAG_STEREO);
	        synth = 0;
	    }
	    continue;
	}

	itp_inc = ((int64)vi->pbase << SMIX_SHIFT) / vi->period;

	if (itp_inc == 0)	/* otherwise m5v-nwlf.t crashes */
	    continue;

	pi = d->patch_array[vi->smp];

	/* This is for bidirectional sample loops */
	if (vi->fidx & FLAG_REVLOOP)
	    itp_inc = -itp_inc;

	/* Sample loop processing. Offsets in samples, not bytes */
	if (vi->fidx & FLAG_16_BITS) {
	    lps = pi->loop_start >> 1;
	    lpe = pi->loop_end >> 1;
	} else {
	    lps = pi->loop_start;
	    lpe = pi->loop_end;
	}

	/* check for Protracker loop */
	if (pi->mode & WAVE_PTKLOOP && pi->mode & WAVE_FIRSTRUN) {
	    lpe = pi->len - 2;
	    if (vi->fidx & FLAG_16_BITS)
		lpe >>= 1;
	}

	for (tic_cnt = s->ticksize; tic_cnt; ) {
	    /* How many samples we can write before the loop break or
	     * sample end... */
	    smp_cnt = 1 + (((int64)(vi->end - vi->pos) << SMIX_SHIFT)
		- vi->itpt) / itp_inc;

	    if (itp_inc > 0) {
		if (vi->end < vi->pos)
		    smp_cnt = 0;
	    } else {
		if (vi->end > vi->pos)
		    smp_cnt = 0;
	    }

	    /* Ok, this shouldn't happen, but in 'Languede.mod'... */
	    if (smp_cnt < 0)
		smp_cnt = 0;
	    
	    /* ...inside the tick boundaries */
	    if (smp_cnt > tic_cnt)
		smp_cnt = tic_cnt;

	    if (vi->vol) {
		int idx;
		int mix_size = s->mode * smp_cnt;
		int mixer = vi->fidx & FIDX_FLAGMASK;

		/* Something for Hipolito's anticlick routine */

		/* Workaround to prevent memory access violaton -- we should
		 * fix it properly later */
		idx = mix_size;
		if (idx < 2)
		    idx = 2;
		prv_r = buf_pos[idx - 2];
		prv_l = buf_pos[idx - 1];

		/* "Beautiful Ones" apparently uses 0xfe as 'no filter' :\ */
		if (vi->cutoff >= 0xfe)
		    mixer &= ~FLAG_FILTER;

		/* Call the output handler */
		mix_fn[mixer](vi, buf_pos, smp_cnt, vol_l, vol_r, itp_inc);
		buf_pos += s->mode * smp_cnt;

		/* More stuff for Hipolito's anticlick routine */
		idx = 0;
		if (mix_size < 2)
		    idx = 2;
		vi->sright = buf_pos[idx - 2] - prv_r;
		vi->sleft = buf_pos[idx - 1] - prv_l;
	    }

	    vi->itpt += itp_inc * smp_cnt;
	    vi->pos += vi->itpt >> SMIX_SHIFT;
	    vi->itpt &= SMIX_MASK;

	    /* No more samples in this tick */
	    if (!(tic_cnt -= smp_cnt))
		continue;

	    vi->fidx ^= vi->fxor;

	    /* First sample loop run */
            if (vi->fidx == 0 || lps >= lpe) {
		smix_anticlick(ctx, voc, 0, 0, buf_pos, tic_cnt);
		xmp_drv_resetvoice(ctx, voc, 0);
		tic_cnt = 0;
		continue;
	    }

	    if ((~vi->fidx & FLAG_REVLOOP) && vi->fxor == 0) {
		vi->pos -= lpe - lps;			/* forward loop */
		if (pi->mode & WAVE_PTKLOOP) {
	            vi->end = lpe = pi->loop_end;
	            pi->mode &= ~WAVE_FIRSTRUN;
		}
	    } else {
		itp_inc = -itp_inc;			/* invert dir */
		vi->itpt += itp_inc;
		/* keep bidir loop at the same size of forward loop */
		vi->pos += (vi->itpt >> SMIX_SHIFT) + 1;
		vi->itpt &= SMIX_MASK;
		vi->end = itp_inc > 0 ? lpe : lps;
	    }
	}
    }

    return s->ticksize * s->mode * s->resol;
}


void smix_voicepos(struct xmp_context *ctx, int voc, int pos, int itp)
{
    struct xmp_driver_context *d = &ctx->d;
    struct voice_info *vi = &d->voice_array[voc];
    struct patch_info *pi = d->patch_array[vi->smp];
    int lpe, res, mode;

    if (pi->len == XMP_PATCH_FM)
	return;

    res = !!(pi->mode & WAVE_16_BITS);
    mode = (pi->mode & WAVE_LOOPING) && !(pi->mode & WAVE_BIDIR_LOOP);
    mode = (mode << res) + res + 1;	/* see xmp_cvt_anticlick */

    lpe = pi->len - mode;
    if (pi->mode & WAVE_LOOPING && ~pi->mode & WAVE_FIRSTRUN)
	lpe = lpe > pi->loop_end ? pi->loop_end : lpe;

    lpe >>= res;

    if (pos >= lpe)			/* Happens often in MED synth */
	pos = 0;

    vi->pos = pos;
    vi->itpt = itp;
    vi->end = lpe;

    if (vi->fidx & FLAG_REVLOOP)
	vi->fidx ^= vi->fxor;
}


void smix_setpatch(struct xmp_context *ctx, int voc, int smp)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_driver_context *d = &ctx->d;
    struct xmp_mod_context *m = &p->m;
    struct xmp_options *o = &ctx->o;
    struct voice_info *vi = &d->voice_array[voc];
    struct patch_info *pi = d->patch_array[smp];

    vi->smp = smp;
    vi->vol = 0;
    vi->freq = (int64)C4_FREQ * pi->base_freq / o->freq;
    
    if (pi->len == XMP_PATCH_FM) {
	vi->fidx = FLAG_SYNTH;
	if (o->outfmt & XMP_FMT_MONO)
	    vi->pan = 0;
	else {
	    vi->pan = pi->panning;
	    vi->fidx |= FLAG_STEREO;
	}

	synth_setpatch(voc, (uint8 *)pi->data);

	return;
    }
    
    xmp_smix_setvol(ctx, voc, 0);

    vi->sptr = d->ext ? NULL : pi->data;
    vi->fidx = m->flags & XMP_CTL_ITPT ? FLAG_ITPT | FLAG_ACTIVE : FLAG_ACTIVE;

    if (o->outfmt & XMP_FMT_MONO) {
	vi->pan = 0;
    } else {
	vi->pan = pi->panning;
	vi->fidx |= FLAG_STEREO;
    }

    if (pi->mode & WAVE_16_BITS)
	vi->fidx |= FLAG_16_BITS;

    if (m->flags & XMP_CTL_FILTER)
	vi->fidx |= FLAG_FILTER;

    if (pi->mode & WAVE_LOOPING)
	vi->fxor = pi->mode & WAVE_BIDIR_LOOP ? FLAG_REVLOOP : 0;
    else
	vi->fxor = vi->fidx;

    if (o->cf_cutoff)			/* Filter-based anticlick */
	vi->fidx |= FLAG_FILTER;

    if (pi->mode & WAVE_PTKLOOP)
	pi->mode |= WAVE_FIRSTRUN;

    smix_voicepos(ctx, voc, 0, 0);
}


void smix_setnote(struct xmp_context *ctx, int voc, int note)
{
    struct xmp_driver_context *d = &ctx->d;
    struct voice_info *vi = &d->voice_array[voc];

    vi->period = note_to_period_mix(vi->note = note, 0);
    vi->pbase = SMIX_C4NOTE * vi->freq / d->patch_array[vi->smp]->base_note;
    vi->attack = SLOW_ATTACK;
}


void smix_setbend(struct xmp_context *ctx, int voc, int bend)
{
    struct xmp_driver_context *d = &ctx->d;
    struct voice_info *vi = &d->voice_array[voc];

    vi->period = note_to_period_mix(vi->note, bend);

    if (vi->fidx & FLAG_SYNTH)
	synth_setnote(voc, vi->note, bend);
}


void xmp_smix_setvol(struct xmp_context *ctx, int voc, int vol)
{
    struct xmp_driver_context *d = &ctx->d;
    struct voice_info *vi = &d->voice_array[voc];
 
    smix_anticlick(ctx, voc, vol, vi->pan, NULL, 0);
    vi->vol = vol;

    if (vi->fidx & FLAG_SYNTH)
	synth_setvol(voc, vol >> 4);
}


void xmp_smix_seteffect(struct xmp_context *ctx, int voc, int type, int val)
{
    struct xmp_driver_context *d = &ctx->d;
    struct voice_info *vi = &d->voice_array[voc];
 
    switch (type) {
    case XMP_FX_CUTOFF:
        vi->cutoff = val;
	break;
    case XMP_FX_RESONANCE:
        vi->resonance = val;
	break;
    case XMP_FX_FILTER_B0:
        vi->flt_B0 = val;
	break;
    case XMP_FX_FILTER_B1:
        vi->flt_B1 = val;
	break;
    case XMP_FX_FILTER_B2:
        vi->flt_B2 = val;
	break;
    }
}


void xmp_smix_setpan(struct xmp_context *ctx, int voc, int pan)
{
    struct xmp_driver_context *d = &ctx->d;
    struct voice_info *vi = &d->voice_array[voc];
 
    vi->pan = pan;
}


void xmp_smix_echoback(struct xmp_context *ctx, int msg)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_smixer_context *s = &ctx->s;

    p->event_callback(s->echo_msg = msg, p->callback_data);
}


int xmp_smix_getmsg(struct xmp_context *ctx)
{
    struct xmp_smixer_context *s = &ctx->s;
    return s->echo_msg;
}


int xmp_smix_numvoices(struct xmp_context *ctx, int num)
{
    struct xmp_smixer_context *s = &ctx->s;
    return num > s->numvoc ? s->numvoc : num;
}

/* WARNING! Output samples must have the same byte order of the host machine!
 * (That's what happens in most cases anyway)
 */
int xmp_smix_writepatch(struct xmp_context *ctx, struct patch_info *patch)
{
    if (patch) {
	if (patch->len == XMP_PATCH_FM)
	    return 0;

	if (patch->len <= 0)
	    return XMP_ERR_PATCH;

	if (patch->mode & WAVE_UNSIGNED)
	    xmp_cvt_sig2uns (patch->len, patch->mode & WAVE_16_BITS,
		patch->data);
    }

    return 0;
}


int xmp_smix_on(struct xmp_context *ctx)
{
    struct xmp_driver_context *d = &ctx->d;
    struct xmp_smixer_context *s = &ctx->s;
    int cnt;

    if (s->numbuf)
	return 0;

    if (d->numbuf < 1)
	d->numbuf = 1;
    cnt = s->numbuf = d->numbuf;

    s->buffer = calloc(sizeof (void *), cnt);
    s->buf32b = calloc(sizeof (int), OUT_MAXLEN);
    if (!(s->buffer && s->buf32b))
	return XMP_ERR_ALLOC;

    while (cnt--) {
	if (!(s->buffer[cnt] = calloc(SMIX_RESMAX, OUT_MAXLEN)))
	    return XMP_ERR_ALLOC;
    }

    s->numvoc = SMIX_NUMVOC;
    d->ext = 0;

    return 0;
}


void xmp_smix_off(struct xmp_context *ctx)
{
    struct xmp_driver_context *d = &ctx->d;
    struct xmp_smixer_context *s = &ctx->s;

    while (s->numbuf)
	free(s->buffer[--s->numbuf]);

    free(s->buf32b);
    free(s->buffer);
    s->buf32b = NULL;
    s->buffer = NULL;
    d->ext = 1;
}


void *xmp_smix_buffer(struct xmp_context *ctx)
{
    static int outbuf;
    int fmt, size;
    struct xmp_smixer_context *s = &ctx->s;
    struct xmp_options *o = &ctx->o;

    if (!o->resol)
	fmt = OUT_U8ULAW;
    else if (o->resol > 8)
	fmt = OUT_SU16NORM;
    else
	fmt = OUT_SU8NORM;

    /* The mixer works with multiple buffers -- this is useless when using
     * multi-buffered sound output (e.g. OSS fragments) but can be useful for
     * DMA transfers in DOS.
     */
    if (++outbuf >= s->numbuf)
	outbuf = 0;

    size = s->mode * s->ticksize;
    assert(size <= OUT_MAXLEN);

    out_fn[fmt](s->buffer[outbuf], s->buf32b, size, o->amplify, o->outfmt);

    smix_resetvar(ctx);

    return s->buffer[outbuf]; 
}

