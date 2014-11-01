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
#include "period.h"

#define MAGIC_MGT	MAGIC4(0x00,'M','G','T')
#define MAGIC_MCS	MAGIC4(0xbd,'M','C','S')


static int mgt_test (FILE *, char *, const int);
static int mgt_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info mgt_loader = {
	"MGT",
	"Megatracker",
	mgt_test,
	mgt_load
};

static int mgt_test(FILE *f, char *t, const int start)
{
	int sng_ptr;

	if (read24b(f) != MAGIC_MGT)
		return -1;
	read8(f);
	if (read32b(f) != MAGIC_MCS)
		return -1;

	fseek(f, 18, SEEK_CUR);
	sng_ptr = read32b(f);
	fseek(f, start + sng_ptr, SEEK_SET);

	read_title(f, t, 32);
	
	return 0;
}

static int mgt_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	struct xxm_event *event;
	int i, j;
	int ver;
	int sng_ptr, seq_ptr, ins_ptr, pat_ptr, trk_ptr, smp_ptr;
	int sdata[64];

	LOAD_INIT();

	read24b(f);		/* MGT */
	ver = read8(f);
	read32b(f);		/* MCS */

	sprintf(m->type, "MGT v%d.%d (Megatracker)", MSN(ver), LSN(ver));

	m->xxh->chn = read16b(f);
	read16b(f);			/* number of songs */
	m->xxh->len = read16b(f);
	m->xxh->pat = read16b(f);
	m->xxh->trk = read16b(f);
	m->xxh->ins = m->xxh->smp = read16b(f);
	read16b(f);			/* reserved */
	read32b(f);			/* reserved */

	sng_ptr = read32b(f);
	seq_ptr = read32b(f);
	ins_ptr = read32b(f);
	pat_ptr = read32b(f);
	trk_ptr = read32b(f);
	smp_ptr = read32b(f);
	read32b(f);			/* total smp len */
	read32b(f);			/* unpacked trk size */

	fseek(f, start + sng_ptr, SEEK_SET);

	fread(m->name, 1, 32, f);
	seq_ptr = read32b(f);
	m->xxh->len = read16b(f);
	m->xxh->rst = read16b(f);
	m->xxh->bpm = read8(f);
	m->xxh->tpo = read8(f);
	read16b(f);			/* global volume */
	read8(f);			/* master L */
	read8(f);			/* master R */

	for (i = 0; i < m->xxh->chn; i++) {
		read16b(f);		/* pan */
	}
	
	MODULE_INFO();

	/* Sequence */

	fseek(f, start + seq_ptr, SEEK_SET);
	for (i = 0; i < m->xxh->len; i++)
		m->xxo[i] = read16b(f);

	/* Instruments */

	INSTRUMENT_INIT();

	fseek(f, start + ins_ptr, SEEK_SET);
	reportv(ctx, 1, "     Name                             Len  LBeg LEnd L Vol C2Spd\n");

	for (i = 0; i < m->xxh->ins; i++) {
		int c2spd, flags;

		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);

		fread(m->xxih[i].name, 1, 32, f);
		sdata[i] = read32b(f);
		m->xxs[i].len = read32b(f);
		m->xxs[i].lps = read32b(f);
		m->xxs[i].lpe = m->xxs[i].lps + read32b(f);
		read32b(f);
		read32b(f);
		c2spd = read32b(f);
		c2spd_to_note(c2spd, &m->xxi[i][0].xpo, &m->xxi[i][0].fin);
		m->xxi[i][0].vol = read16b(f) >> 4;
		read8(f);		/* vol L */
		read8(f);		/* vol R */
		m->xxi[i][0].pan = 0x80;
		flags = read8(f);
		m->xxs[i].flg = flags & 0x03 ? WAVE_LOOPING : 0;
		m->xxs[i].flg |= flags & 0x02 ? WAVE_BIDIR_LOOP : 0;
		m->xxi[i][0].fin += 0 * read8(f);	// FIXME
		read8(f);		/* unused */
		read8(f);
		read8(f);
		read8(f);
		read16b(f);
		read32b(f);
		read32b(f);

		m->xxih[i].nsm = !!m->xxs[i].len;
		m->xxi[i][0].sid = i;
		
		if (V(1) && (strlen((char*)m->xxih[i].name) || (m->xxs[i].len > 1))) {
			report("[%2X] %-32.32s %04x %04x %04x %c V%02x %5d\n",
				i, m->xxih[i].name,
				m->xxs[i].len, m->xxs[i].lps, m->xxs[i].lpe,
				m->xxs[i].flg & WAVE_BIDIR_LOOP ? 'B' :
					m->xxs[i].flg & WAVE_LOOPING ? 'L' : ' ',
				m->xxi[i][0].vol, c2spd);
		}
	}

	/* PATTERN_INIT - alloc extra track*/
	PATTERN_INIT();

	reportv(ctx, 0, "Stored tracks  : %d ", m->xxh->trk);

	/* Tracks */

	for (i = 1; i < m->xxh->trk; i++) {
		int offset, rows;
		uint8 b;

		fseek(f, start + trk_ptr + i * 4, SEEK_SET);
		offset = read32b(f);
		fseek(f, start + offset, SEEK_SET);

		rows = read16b(f);
		m->xxt[i] = calloc(sizeof(struct xxm_track) +
				sizeof(struct xxm_event) * rows, 1);
		m->xxt[i]->rows = rows;

		//printf("\n=== Track %d ===\n\n", i);
		for (j = 0; j < rows; j++) {
			uint8 note, f2p;

			b = read8(f);
			j += b & 0x03;

			note = 0;
			event = &m->xxt[i]->event[j];
			if (b & 0x04)
				note = read8(f);
			if (b & 0x08)
				event->ins = read8(f);
			if (b & 0x10)
				event->vol = read8(f);
			if (b & 0x20)
				event->fxt = read8(f);
			if (b & 0x40)
				event->fxp = read8(f);
			if (b & 0x80)
				f2p = read8(f);

			if (note == 1)
				event->note = XMP_KEY_OFF;
			else if (note > 11) /* adjusted to play codeine.mgt */
				event->note = note - 11;

			/* effects */
			if (event->fxt < 0x10)
				/* like amiga */ ;
			else switch (event->fxt) {
			case 0x13: 
			case 0x14: 
			case 0x15: 
			case 0x17: 
			case 0x1c: 
			case 0x1d: 
			case 0x1e: 
				event->fxt = FX_EXTENDED;
				event->fxp = ((event->fxt & 0x0f) << 4) |
							(event->fxp & 0x0f);
				break;
			default:
				event->fxt = event->fxp = 0;
			}

			/* volume and volume column effects */
			if ((event->vol >= 0x10) && (event->vol <= 0x50)) {
				event->vol -= 0x0f;
				continue;
			}

			switch (event->vol >> 4) {
			case 0x06:	/* Volume slide down */
				event->f2t = FX_VOLSLIDE_2;
				event->f2p = event->vol - 0x60;
				break;
			case 0x07:	/* Volume slide up */
				event->f2t = FX_VOLSLIDE_2;
				event->f2p = (event->vol - 0x70) << 4;
				break;
			case 0x08:	/* Fine volume slide down */
				event->f2t = FX_EXTENDED;
				event->f2p = (EX_F_VSLIDE_DN << 4) |
							(event->vol - 0x80);
				break;
			case 0x09:	/* Fine volume slide up */
				event->f2t = FX_EXTENDED;
				event->f2p = (EX_F_VSLIDE_UP << 4) |
							(event->vol - 0x90);
				break;
			case 0x0a:	/* Set vibrato speed */
				event->f2t = FX_VIBRATO;
				event->f2p = (event->vol - 0xa0) << 4;
				break;
			case 0x0b:	/* Vibrato */
				event->f2t = FX_VIBRATO;
				event->f2p = event->vol - 0xb0;
				break;
			case 0x0c:	/* Set panning */
				event->f2t = FX_SETPAN;
				event->f2p = ((event->vol - 0xc0) << 4) + 8;
				break;
			case 0x0d:	/* Pan slide left */
				event->f2t = FX_PANSLIDE;
				event->f2p = (event->vol - 0xd0) << 4;
				break;
			case 0x0e:	/* Pan slide right */
				event->f2t = FX_PANSLIDE;
				event->f2p = event->vol - 0xe0;
				break;
			case 0x0f:	/* Tone portamento */
				event->f2t = FX_TONEPORTA;
				event->f2p = (event->vol - 0xf0) << 4;
				break;
			}
	
			event->vol = 0;

			/*printf("%02x  %02x %02x %02x %02x %02x\n",
				j, event->note, event->ins, event->vol,
				event->fxt, event->fxp);*/
		}

		if (V(0) && i % m->xxh->chn == 0)
			report(".");
	}
	reportv(ctx, 0, "\n");

	/* Extra track */
	m->xxt[0] = calloc(sizeof(struct xxm_track) +
			sizeof(struct xxm_event) * 64 - 1, 1);
	m->xxt[0]->rows = 64;

	/* Read and convert patterns */

	reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);
	fseek(f, start + pat_ptr, SEEK_SET);

	for (i = 0; i < m->xxh->pat; i++) {
		PATTERN_ALLOC(i);

		m->xxp[i]->rows = read16b(f);
		for (j = 0; j < m->xxh->chn; j++) {
			m->xxp[i]->info[j].index = read16b(f) - 1;
			//printf("%3d ", m->xxp[i]->info[j].index);
		}

		reportv(ctx, 0, ".");
		//printf("\n");
	}
	reportv(ctx, 0, "\n");

	/* Read samples */

	reportv(ctx, 0, "Stored samples : %d ", m->xxh->smp);

	for (i = 0; i < m->xxh->ins; i++) {
		if (m->xxih[i].nsm == 0)
			continue;

		fseek(f, start + sdata[i], SEEK_SET);
		xmp_drv_loadpatch(ctx, f, m->xxi[i][0].sid, m->c4rate, 0,
						&m->xxs[m->xxi[i][0].sid], NULL);
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	return 0;
}
