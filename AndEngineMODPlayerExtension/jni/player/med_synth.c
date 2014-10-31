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

#include "common.h"
#include "player.h"
#include "driver.h"

/* Commands in the volume and waveform sequence table:
 *
 *	Cmd	Vol	Wave	Action
 *
 *	0xff	    END		End sequence
 *	0xfe	    JMP		Jump
 *	0xfd	 -	ARE	End arpeggio definition
 *	0xfc	 -	ARP	Begin arpeggio definition
 *	0xfb	    HLT		Halt
 *	0xfa	JWS	JVS	Jump waveform/volume sequence
 *	0xf9	     -		-
 *	0xf8	     -		-
 *	0xf7	 -	VWF	Set vibrato waveform
 *	0xf6	EST	RES	?/reset pitch
 *	0xf5	EN2	VBS	Looping envelope/set vibrato speed
 *	0xf4	EN1	VBD	One shot envelope/set vibrato depth
 *	0xf3	    CHU		Change volume/pitch up speed
 *	0xf2	    CHD		Change volume/pitch down speed
 *	0xf1	    WAI		Wait
 *	0xf0	    SPD		Set speed
 */

extern uint8 **med_vol_table;
extern uint8 **med_wav_table;

#define VT p->m.med_vol_table[xc->ins][xc->med_vp++]
#define WT p->m.med_wav_table[xc->ins][xc->med_wp++]
#define VT_SKIP xc->med_vp++
#define WT_SKIP xc->med_wp++


static int sine[32] = {
       0,  49,  97, 141, 180, 212, 235, 250,
     255, 250, 235, 212, 180, 141,  97,  49,
       0, -49, -97,-141,-180,-212,-235,-250,
    -255,-250,-235,-212,-180,-141, -97, -49
};

int get_med_vibrato(struct xmp_channel *xc)
{
	int vib;

#if 0
	if (xc->med_vib_wf >= xxih[xc->ins].nsm)	/* invalid waveform */
		return 0;

	if (xxs[xxi[xc->ins][xc->med_vib_wf].sid].len != 32)
		return 0;
#endif

	/* FIXME: always using sine waveform */

	vib = (sine[xc->med_vib_idx >> 5] * xc->med_vib_depth) >> 11;
	xc->med_vib_idx += xc->med_vib_speed;
	xc->med_vib_idx %= (32 << 5);

	return vib;
}


int get_med_arp(struct xmp_player_context *p, struct xmp_channel *xc)
{
	int arp;

	if (xc->med_arp == 0)
		return 0;

	if (p->m.med_wav_table[xc->ins][xc->med_arp] == 0xfd) /* empty arpeggio */
		return 0;

	arp = p->m.med_wav_table[xc->ins][xc->med_aidx++];
	if (arp == 0xfd) {
		xc->med_aidx = xc->med_arp;
		arp = p->m.med_wav_table[xc->ins][xc->med_aidx++];
	}

	return 100 * arp;
}


void xmp_med_synth(struct xmp_context *ctx, int chn, struct xmp_channel *xc, int rst)
{
    struct xmp_player_context *p = &ctx->p;
    int b, jws = 0, jvs = 0, loop = 0, jump = 0;

    if (p->m.med_vol_table == NULL || p->m.med_wav_table == NULL)
	return;

    if (p->m.med_vol_table[xc->ins] == NULL || p->m.med_wav_table[xc->ins] == NULL)
	return;

    if (rst) {
	xc->med_arp = xc->med_aidx = 0;
	xc->med_period = xc->period;
	xc->med_vp = xc->med_vc = xc->med_vw = 0;
	xc->med_wp = xc->med_wc = xc->med_ww = 0;
	xc->med_vs = p->m.xxih[xc->ins].vts;
	xc->med_ws = p->m.xxih[xc->ins].wts;
    }

    if (xc->med_vs > 0 && xc->med_vc-- == 0) {
	xc->med_vc = xc->med_vs - 1;

	if (xc->med_vw > 0) {
	    xc->med_vw--;
	    goto skip_vol;
	}

	jump = loop = jws = 0;
	switch (b = VT) {
	    while (jump--) {
	    case 0xff:		/* END */
	    case 0xfb:		/* HLT */
		xc->med_vp--;
		break;
	    case 0xfe:		/* JMP */
		if (loop)	/* avoid infinite loop */
		    break;
		xc->med_vp = VT;
		loop = jump = 1;
		break;
	    case 0xfa:		/* JWS */
		jws = VT;
		break;
	    case 0xf5:		/* EN2 */
	    case 0xf4:		/* EN1 */
		VT_SKIP;	/* Not implemented */
		break;
	    case 0xf3:		/* CHU */
		xc->med_vv = VT;
		break;
	    case 0xf2:		/* CHD */
		xc->med_vv = -VT;
		break;
	    case 0xf1:		/* WAI */
		xc->med_vw = VT;
		break;
	    case 0xf0:		/* SPD */
		xc->med_vs = VT;
		break;
	    default:
		if (b >= 0x00 && b <= 0x40)
		    xc->volume = b;
	    }
	}

	xc->volume += xc->med_vv;
	if (xc->volume < 0)
	    xc->volume = 0;
	if (xc->volume > 64)
	    xc->volume = 64;

skip_vol:

	if (xc->med_ww > 0) {
	    xc->med_ww--;
	    goto skip_wav;
	}

	jump = loop = jvs = 0;
	switch (b = WT) {
	    while (jump--) {
	    case 0xff:		/* END */
	    case 0xfb:		/* HLT */
		xc->med_wp--;
		break;
	    case 0xfe:		/* JMP */
		if (loop)	/* avoid infinite loop */
		    break;
		xc->med_wp = WT;
		loop = jump = 1;
		break;
	    case 0xfd:		/* ARE */
		break;
	    case 0xfc:		/* ARP */
		xc->med_arp = xc->med_aidx = xc->med_wp++;
		while (WT != 0xfd);
		break;
	    case 0xfa:		/* JVS */
		jws = WT;
		break;
	    case 0xf7:		/* VWF */
		xc->med_vwf = WT;
		break;
	    case 0xf6:		/* RES */
		xc->period = xc->med_period;
		break;
	    case 0xf5:		/* VBS */
		xc->med_vib_speed = WT;
		break;
	    case 0xf4:		/* VBD */
		xc->med_vib_depth = WT;
		break;
	    case 0xf3:		/* CHU */
		xc->med_wv = -WT;
		break;
	    case 0xf2:		/* CHD */
		xc->med_wv = WT;
		break;
	    case 0xf1:		/* WAI */
		xc->med_ww = WT;
		break;
	    case 0xf0:		/* SPD */
		xc->med_ws = WT;
		break;
	    default:
		if (b < p->m.xxih[xc->ins].nsm && p->m.xxi[xc->ins][b].sid != xc->smp) {
		    xc->smp = p->m.xxi[xc->ins][b].sid;
		    xmp_drv_setsmp(ctx, chn, xc->smp);
		}
	    }
	}
skip_wav:
	;
	/* xc->period += xc->med_wv; */
    }

    if (jws) {
	xc->med_wp = jws;
	jws = 0;
    }

    if (jvs) {
	xc->med_vp = jvs;
	jvs = 0;
    }
}
