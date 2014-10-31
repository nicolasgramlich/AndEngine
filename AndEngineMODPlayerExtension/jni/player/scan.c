/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/*
 * Sun, 31 May 1998 17:50:02 -0600
 * Reported by ToyKeeper <scriven@CS.ColoState.EDU>:
 * For loop-prevention, I know a way to do it which lets most songs play
 * fine once through even if they have backward-jumps. Just keep a small
 * array (256 bytes, or even bits) of flags, each entry determining if a
 * pattern in the song order has been played. If you get to an entry which
 * is already non-zero, skip to the next song (assuming looping is off).
 */

/*
 * Tue, 6 Oct 1998 21:23:17 +0200 (CEST)
 * Reported by John v/d Kamp <blade_@dds.nl>:
 * scan.c was hanging when it jumps to an invalid restart value.
 * (Fixed by hipolito)
 */


#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdlib.h>
#include <string.h>
#include "common.h"
#include "effects.h"
#include "mixer.h"

#define S3M_END		0xff
#define S3M_SKIP	0xff
#define TIME		6
#define MAX_GVL		0x40


int _xmp_scan_module(struct xmp_context *ctx)
{
    int parm, gvol_slide, f1, f2, p1, p2, ord, ord2;
    int row, last_row, break_row, cnt_row;
    int gvl, bpm, tempo, base_time, chn;
    int alltmp, clock, clock_rst, medbpm;
    int loop_chn, loop_flg;
    int* loop_stk;
    int* loop_row;
    char** tab_cnt;
    struct xxm_event* event;
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    struct xmp_options *o = &ctx->o;

    if (m->xxh->len == 0)
	return 0;

    medbpm = m->quirk & XMP_QRK_MEDBPM;

    tab_cnt = calloc (sizeof (char *), m->xxh->len);
    for (ord = m->xxh->len; ord--;)
	tab_cnt[ord] = calloc(1, m->xxo[ord] >= m->xxh->pat ?  1 :
		m->xxp[m->xxo[ord]]->rows ? m->xxp[m->xxo[ord]]->rows : 1);

    loop_stk = calloc(sizeof (int), m->xxh->chn);
    loop_row = calloc(sizeof (int), m->xxh->chn);
    loop_chn = loop_flg = 0;

    memset(m->xxo_fstrow, 0, XMP_MAXORD);

    gvl = m->xxh->gvl;
    bpm = m->xxh->bpm;

    tempo = (tempo = o->tempo ? o->tempo : m->xxh->tpo) ? tempo : TIME;
    base_time = m->rrate;

    /* By erlk ozlr <erlk.ozlr@gmail.com>
     *
     * xmp doesn't handle really properly the "start" option (-s for the
     * command-line) for DeusEx's .umx files. These .umx files contain
     * several loop "tracks" that never join together. That's how they put
     * multiple musics on each level with a file per level. Each "track"
     * starts at the same order in all files. The problem is that xmp starts
     * decoding the module at order 0 and not at the order specified with
     * the start option. If we have a module that does "0 -> 2 -> 0 -> ...",
     * we cannot play order 1, even with the supposed right option.
     *
     * was: ord2 = ord = -1;
     */
    ord2 = -1;
    ord = ctx->o.start - 1;

    gvol_slide = break_row = cnt_row = alltmp = clock_rst = clock = 0;

    while (42) {
	if ((uint32)++ord >= m->xxh->len) {
	    /*if ((uint32)++ord >= m->xxh->len)*/
		ord = ((uint32)m->xxh->rst > m->xxh->len ||
			(uint32)m->xxo[m->xxh->rst] >= m->xxh->pat) ?
			0 : m->xxh->rst;
		//if (m->xxo[ord] == S3M_END)
		 //   break;
	} 

	/* All invalid patterns skipped, only S3M_END aborts replay */
	if ((uint32)m->xxo[ord] >= m->xxh->pat) {
	    /*if (m->xxo[ord] == S3M_SKIP) ord++;*/
	    if (m->xxo[ord] == S3M_END) {
		ord = m->xxh->len;
	        continue;
	    }
	    continue;
	}

	if (break_row < m->xxp[m->xxo[ord]]->rows && tab_cnt[ord][break_row])
	    break;

	m->xxo_info[ord].gvl = gvl;
	m->xxo_info[ord].bpm = bpm;
	m->xxo_info[ord].tempo = tempo;

	if (medbpm)
	    m->xxo_info[ord].time = (clock + 132 * alltmp / 5 / bpm) / 10;
	else
	    m->xxo_info[ord].time = (clock + 100 * alltmp / bpm) / 10;

	if (!m->xxo_fstrow[ord] && ord) {
	    if (ord == o->start && !(m->flags & XMP_CTL_LOOP)) {
		if (medbpm)
	            clock_rst = clock + 132 * alltmp / 5 / bpm;
		else
		    clock_rst = clock + 100 * alltmp / bpm;
	    }

	    m->xxo_fstrow[ord] = break_row;
	}

	last_row = m->xxp[m->xxo[ord]]->rows;
	for (row = break_row, break_row = 0; row < last_row; row++, cnt_row++) {
	    /* Prevent crashes caused by large softmixer frames */
	    if (bpm < SMIX_MINBPM)
		bpm = SMIX_MINBPM;

	    /* Date: Sat, 8 Sep 2007 04:01:06 +0200
	     * Reported by Zbigniew Luszpinski <zbiggy@o2.pl>
	     * The scan routine falls into infinite looping and doesn't let
	     * xmp play jos-dr4k.xm.
	     * Claudio's workaround: we'll break infinite loops here.
	     *
	     * Date: Oct 27, 2007 8:05 PM
	     * From: Adric Riedel <adric.riedel@gmail.com>
	     * Jesper Kyd: Global Trash 3.mod (the 'Hardwired' theme) only
	     * plays the first 4:41 of what should be a 10 minute piece.
	     * (...) it dies at the end of position 2F
	     */

	    if (cnt_row > 512)	/* was 255, but Global trash goes to 318 */
		goto end_module;

	    if (!loop_flg && tab_cnt[ord][row]) {
		cnt_row--;
		goto end_module;
	    }
	    tab_cnt[ord][row]++;

	    for (chn = 0; chn < m->xxh->chn; chn++) {
		if (row >= m->xxt[m->xxp[m->xxo[ord]]->info[chn].index]->rows)
		    continue;

		event = &EVENT(m->xxo[ord], chn, row);

		f1 = event->fxt;
		p1 = event->fxp;
		f2 = event->f2t;
		p2 = event->f2p;

		if (f1 == FX_GLOBALVOL || f2 == FX_GLOBALVOL) {
		    gvl = (f1 == FX_GLOBALVOL) ? p1 : p2;
		    gvl = gvl > MAX_GVL ? MAX_GVL : gvl < 0 ? 0 : gvl;
		}

		if (f1 == FX_G_VOLSLIDE || f2 == FX_G_VOLSLIDE) {
		    parm = (f1 == FX_G_VOLSLIDE) ? p1 : p2;
		    if (parm)
			gvol_slide = MSN(parm) - LSN(parm);
		    gvl += gvol_slide * (tempo - !(m->quirk & XMP_QRK_VSALL));
		}

		if ((f1 == FX_TEMPO && p1) || (f2 == FX_TEMPO && p2)) {
		    parm = (f1 == FX_TEMPO) ? p1 : p2;
		    alltmp += cnt_row * tempo * base_time;
		    cnt_row = 0;
		    if (parm) {
			if (parm <= 0x20)
			    tempo = parm;
			else if (~m->flags & XMP_CTL_VBLANK) {
			    if (medbpm)
				clock += 132 * alltmp / 5 / bpm;
			    else
				clock += 100 * alltmp / bpm;

			    alltmp = 0;
			    bpm = parm;
			}
		    }
		}

		if ((f1 == FX_S3M_TEMPO && p1) || (f2 == FX_S3M_TEMPO && p2)) {
		    parm = (f1 == FX_S3M_TEMPO) ? p1 : p2;
		    alltmp += cnt_row * tempo * base_time;
		    cnt_row  = 0;
		    tempo = parm;
		}

		if ((f1 == FX_S3M_BPM && p1) || (f2 == FX_S3M_BPM && p2)) {
		    parm = (f1 == FX_S3M_BPM) ? p1 : p2;
		    alltmp += cnt_row * tempo * base_time;
		    cnt_row = 0;

		    if (medbpm)
			clock += 132 * alltmp / 5 / bpm;
		    else
			clock += 100 * alltmp / bpm;

		    alltmp = 0;
		    bpm = parm;
		}

		if ((f1 == FX_IT_BPM && p1) || (f2 == FX_IT_BPM && p2)) {
		    parm = (f1 == FX_IT_BPM) ? p1 : p2;
		    alltmp += cnt_row * tempo * base_time;
		    cnt_row = 0;
		    clock += 100 * alltmp / bpm;
		    alltmp = 0;

		    if (MSN(parm) == 0) {
			bpm -= LSN(parm);
			if (bpm < 0x20)
			    bpm = 0x20;
		    } else if (MSN(parm) == 1) {
			bpm += LSN(parm);
			if (bpm > 0xff)
			    bpm = 0xff;
		    } else {
			bpm = parm;
		    }
		}

		if (f1 == FX_JUMP || f2 == FX_JUMP) {
		    ord2 = (f1 == FX_JUMP) ? p1 : p2;
		    last_row = 0;
		}

		if (f1 == FX_BREAK || f2 == FX_BREAK) {
		    parm = (f1 == FX_BREAK) ? p1 : p2;
		    break_row = 10 * MSN(parm) + LSN(parm);
		    last_row = 0;
		}

		if (f1 == FX_EXTENDED || f2 == FX_EXTENDED) {
		    parm = (f1 == FX_EXTENDED) ? p1 : p2;

		    if ((parm >> 4) == EX_PATT_DELAY)
			alltmp += (parm & 0x0f) * tempo * base_time;

		    if ((parm >> 4) == EX_PATTERN_LOOP) {
			if (parm &= 0x0f) {
			    if (loop_stk[chn]) {
				if (--loop_stk[chn])
				    loop_chn = chn + 1;
				else {
				    loop_flg--;
				    if (m->quirk & XMP_QRK_S3MLOOP)
					loop_row[chn] = row + 1;
				}
			    } else {
				if (loop_row[chn] <= row) {
				    loop_stk[chn] = parm;
				    loop_chn = chn + 1;
				    loop_flg++;
				}
			    }
			} else { 
			    loop_row[chn] = row;
			}
		    }
		}
	    }

	    if (loop_chn) {
		row = loop_row[--loop_chn] - 1;
		loop_chn = 0;
	    }
	}

	if (ord2 >= 0) {
	    ord = --ord2;
	    ord2 = -1;
	}

	alltmp += cnt_row * tempo * base_time;
	cnt_row = 0;
    }
    row = break_row;

end_module:
    p->scan_num = o->start > ord ? 0: tab_cnt[ord][row];
    p->scan_row = row;
    p->scan_ord = ord;

    free(loop_row);
    free(loop_stk);

    for (ord = m->xxh->len; ord--; free (tab_cnt[ord]));
    free(tab_cnt);

    clock -= clock_rst;
    alltmp += cnt_row * tempo * base_time;

    if (medbpm)
	return (clock + 132 * alltmp / 5 / bpm) / 10;
    else
	return (clock + 100 * alltmp / bpm) / 10;
}


