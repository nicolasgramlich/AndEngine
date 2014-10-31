/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/*
 * Originally based on the PSM loader from Modplug by Olivier Lapicque and
 * fixed comparing the One Must Fall! PSMs with Kenny Chou's MTM files.
 */

/*
 * From EPICTEST Readme.1st:
 *
 * The Music And Sound Interface, MASI, is the basis behind all new Epic
 * games. MASI uses its own proprietary file format, PSM, for storing
 * its music.
 */

/*
 * kode54's comment on Sinaria PSMs in the foo_dumb hydrogenaudio forum:
 *
 * "The Sinaria variant uses eight character pattern and instrument IDs,
 * the sample headers are laid out slightly different, and the patterns
 * use a different format for the note values, and also different effect
 * scales for certain commands.
 *
 * [Epic] PSM uses high nibble for octave and low nibble for note, for
 * a valid range up to 0x7F, for a range of D-1 through D#9 compared to
 * IT. (...) Sinaria PSM uses plain note values, from 1 - 83, for a
 * range of C-3 through B-9.
 *
 * [Epic] PSM also uses an effect scale for portamento, volume slides,
 * and vibrato that is about four times as sensitive as the IT equivalents.
 * Sinaria does not. This seems to coincide with the MOD/S3M to PSM
 * converter that Joshua Jensen released in the EPICTEST.ZIP file which
 * can still be found on a few FTP sites. It converted effects literally,
 * even though the bundled players behaved as the libraries used with
 * Epic's games did and made the effects sound too strong."
 */

/*
 * Claudio's note: Sinaria seems to have a finetune byte just before
 * volume, slightly different sample size (subtract 2 bytes?) and some
 * kind of (stereo?) interleaved sample, with 16-byte frames (see Sinaria
 * songs 5 and 8). Sinaria song 10 sounds ugly, possibly caused by wrong
 * pitchbendings (see note above).
 */

/* FIXME: TODO: sinaria effects */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include "iff.h"
#include "period.h"

#define MAGIC_PSM_	MAGIC4('P','S','M',' ')
#define MAGIC_OPLH	MAGIC4('O','P','L','H')


static int masi_test (FILE *, char *, const int);
static int masi_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info masi_loader = {
	"MASI",
	"Epic MegaGames MASI",
	masi_test,
	masi_load
};

static int masi_test(FILE *f, char *t, const int start)
{
	if (read32b(f) != MAGIC_PSM_)
		return -1;

	read_title(f, t, 0);

	return 0;
}

static int sinaria;

static int cur_pat;
static int cur_ins;
uint8 *pnam;
uint8 *pord;


static void get_sdft(struct xmp_context *ctx, int size, FILE *f)
{
}

static void get_titl(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	char buf[40];
	
	fread(buf, 1, 40, f);
	strncpy(m->name, buf, size > 32 ? 32 : size);
}

static void get_dsmp_cnt(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;

	m->xxh->ins++;
	m->xxh->smp = m->xxh->ins;
}

static void get_pbod_cnt(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	char buf[20];

	m->xxh->pat++;
	fread(buf, 1, 20, f);
	if (buf[9] != 0 && buf[13] == 0)
		sinaria = 1;
}


static void get_dsmp(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i, srate;
	int finetune;

	read8(f);				/* flags */
	fseek(f, 8, SEEK_CUR);			/* songname */
	fseek(f, sinaria ? 8 : 4, SEEK_CUR);	/* smpid */

	if (V(1) && cur_ins == 0)
	    report("\n     Instrument name                  Len   LBeg  LEnd  L Vol Fine C2Spd");

	i = cur_ins;
	m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);

	fread(&m->xxih[i].name, 1, 34, f);
	str_adj((char *)m->xxih[i].name);
	fseek(f, 5, SEEK_CUR);
	read8(f);		/* insno */
	read8(f);
	m->xxs[i].len = read32l(f);
	m->xxih[i].nsm = !!(m->xxs[i].len);
	m->xxs[i].lps = read32l(f);
	m->xxs[i].lpe = read32l(f);
	m->xxs[i].flg = m->xxs[i].lpe > 2 ? WAVE_LOOPING : 0;
	read16l(f);

	if ((int32)m->xxs[i].lpe < 0)
		m->xxs[i].lpe = 0;

	finetune = 0;
	if (sinaria) {
		if (m->xxs[i].len > 2)
			m->xxs[i].len -= 2;
		if (m->xxs[i].lpe > 2)
			m->xxs[i].lpe -= 2;

		finetune = (int8)(read8s(f) << 4);
	}

	m->xxi[i][0].vol = read8(f) / 2 + 1;
	read32l(f);
	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].sid = i;
	srate = read32l(f);

	if ((V(1)) && (strlen((char *) m->xxih[i].name) || (m->xxs[i].len > 1)))
	    report ("\n[%2X] %-32.32s %05x %05x %05x %c V%02x %+04d %5d", i,
		m->xxih[i].name, m->xxs[i].len, m->xxs[i].lps, m->xxs[i].lpe, m->xxs[i].flg
		& WAVE_LOOPING ? 'L' : ' ', m->xxi[i][0].vol, finetune, srate);

	srate = 8363 * srate / 8448;
	c2spd_to_note(srate, &m->xxi[i][0].xpo, &m->xxi[i][0].fin);
	m->xxi[i][0].fin += finetune;

	fseek(f, 16, SEEK_CUR);
	xmp_drv_loadpatch(ctx, f, i, m->c4rate, XMP_SMP_8BDIFF, &m->xxs[i], NULL);

	cur_ins++;
}


static void get_pbod(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i, r;
	struct xxm_event *event, dummy;
	uint8 flag, chan;
	uint32 len;
	int rows, rowlen;

	i = cur_pat;

	len = read32l(f);
	fread(pnam + i * 8, 1, sinaria ? 8 : 4, f);

	rows = read16l(f);

	PATTERN_ALLOC(i);
	m->xxp[i]->rows = rows;
	TRACK_ALLOC(i);

	r = 0;

	do {
		rowlen = read16l(f) - 2;
		while (rowlen > 0) {
			flag = read8(f);
	
			if (rowlen == 1)
				break;
	
			chan = read8(f);
			rowlen -= 2;
	
			event = chan < m->xxh->chn ? &EVENT(i, chan, r) : &dummy;
	
			if (flag & 0x80) {
				uint8 note = read8(f);
				rowlen--;
				if (sinaria)
					note += 25;
				else
					note = (note >> 4) * 12 + (note & 0x0f) + 2;
				event->note = note;
			}

			if (flag & 0x40) {
				event->ins = read8(f) + 1;
				rowlen--;
			}
	
			if (flag & 0x20) {
				event->vol = read8(f) / 2;
				rowlen--;
			}
	
			if (flag & 0x10) {
				uint8 fxt = read8(f);
				uint8 fxp = read8(f);
				rowlen -= 2;
	
				/* compressed events */
				if (fxt >= 0x40) {
					switch (fxp >> 4) {
					case 0x0: {
						uint8 note;
						note = (fxt>>4)*12 +
							(fxt & 0x0f) + 2;
						event->note = note;
						fxt = FX_TONEPORTA;
						fxp = (fxp + 1) * 2;
						break; }
					default:
printf("p%d r%d c%d: compressed event %02x %02x\n", i, r, chan, fxt, fxp);
					}
				} else
				switch (fxt) {
				case 0x01:		/* fine volslide up */
					fxt = FX_EXTENDED;
					fxp = (EX_F_VSLIDE_UP << 4) |
						((fxp / 2) & 0x0f);
					break;
				case 0x02:		/* volslide up */
					fxt = FX_VOLSLIDE;
					fxp = (fxp / 2) << 4;
					break;
				case 0x03:		/* fine volslide down */
					fxt = FX_EXTENDED;
					fxp = (EX_F_VSLIDE_DN << 4) |
						((fxp / 2) & 0x0f);
					break;
				case 0x04: 		/* volslide down */
					fxt = FX_VOLSLIDE;
					fxp /= 2;
					break;
			    	case 0x0C:		/* portamento up */
					fxt = FX_PORTA_UP;
					fxp = (fxp - 1) / 2;
					break;
				case 0x0E:		/* portamento down */
					fxt = FX_PORTA_DN;
					fxp = (fxp - 1) / 2;
					break;
				case 0x0f:		/* tone portamento */
					fxt = FX_TONEPORTA;
					fxp /= 4;
					break;
				case 0x15:		/* vibrato */
					fxt = sinaria ?
						FX_VIBRATO : FX_FINE4_VIBRA;
					/* fxp remains the same */
					break;
				case 0x2a:		/* retrig note */
					fxt = FX_EXTENDED;
					fxp = (EX_RETRIG << 4) | (fxp & 0x0f); 
					break;
				case 0x29:		/* unknown */
					read16l(f);
					rowlen -= 2;
					break;
				case 0x33:		/* position Jump */
					fxt = FX_JUMP;
					break;
			    	case 0x34:		/* pattern break */
					fxt = FX_BREAK;
					break;
				case 0x3D:		/* speed */
					fxt = FX_TEMPO;
					break;
				case 0x3E:		/* tempo */
					fxt = FX_TEMPO;
					break;
				default:
printf("p%d r%d c%d: unknown effect %02x %02x\n", i, r, chan, fxt, fxp);
					fxt = fxp = 0;
				}
	
				event->fxt = fxt;
				event->fxp = fxp;
			}
		}
		r++;
	} while (r < rows);

	cur_pat++;
}

static void get_song(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;

	fseek(f, 10, SEEK_CUR);
	m->xxh->chn = read8(f);
}

static void get_song_2(struct xmp_context *ctx, int size, FILE *f)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	uint32 magic;
	char c, buf[20];
	int i;

	fread(buf, 1, 9, f);
	read16l(f);

	reportv(ctx, 2, "\nSubsong title  : %-9.9s", buf);

	magic = read32b(f);
	while (magic != MAGIC_OPLH) {
		int skip;
		skip = read32l(f);;
		fseek(f, skip, SEEK_CUR);
		magic = read32b(f);
	}

	read32l(f);	/* chunk size */

	fseek(f, 9, SEEK_CUR);		/* unknown data */
	
	c = read8(f);
	for (i = 0; c != 0x01; c = read8(f)) {
		switch (c) {
		case 0x07:
			m->xxh->tpo = read8(f);
			read8(f);		/* 08 */
			m->xxh->bpm = read8(f);
			break;
		case 0x0d:
			read8(f);		/* channel number? */
			m->xxc[i].pan = read8(f);
			read8(f);		/* flags? */
			i++;
			break;
		case 0x0e:
			read8(f);		/* channel number? */
			read8(f);		/* ? */
			break;
		default:
			printf("channel %d: %02x %02x\n", i, c, read8(f));
		}
	}

	for (; c == 0x01; c = read8(f)) {
		fread(pord + m->xxh->len * 8, 1, sinaria ? 8 : 4, f);
		m->xxh->len++;
	}
}

static int masi_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int offset;
	int i, j;

	LOAD_INIT();

	read32b(f);

	sinaria = 0;
	m->name[0] = 0;

	fseek(f, 8, SEEK_CUR);		/* skip file size and FILE */
	m->xxh->smp = m->xxh->ins = 0;
	cur_pat = 0;
	cur_ins = 0;
	offset = ftell(f);

	/* IFF chunk IDs */
	iff_register("TITL", get_titl);
	iff_register("SDFT", get_sdft);
	iff_register("SONG", get_song);
	iff_register("DSMP", get_dsmp_cnt);
	iff_register("PBOD", get_pbod_cnt);
	iff_setflag(IFF_LITTLE_ENDIAN);

	/* Load IFF chunks */
	while (!feof(f))
		iff_chunk(ctx, f);

	iff_release();

	m->xxh->trk = m->xxh->pat * m->xxh->chn;
	pnam = malloc(m->xxh->pat * 8);		/* pattern names */
	pord = malloc(255 * 8);			/* pattern orders */

	strcpy (m->type, sinaria ?
		"MASI (Sinaria PSM)" : "MASI (Epic MegaGames MASI)");

	MODULE_INFO();
	INSTRUMENT_INIT();
	PATTERN_INIT();

	if (V(0)) {
	    report("Stored patterns: %d\n", m->xxh->pat);
	    report("Stored samples : %d", m->xxh->smp);
	}

	fseek(f, start + offset, SEEK_SET);

	m->xxh->len = 0;

	iff_register("SONG", get_song_2);
	iff_register("DSMP", get_dsmp);
	iff_register("PBOD", get_pbod);
	iff_setflag(IFF_LITTLE_ENDIAN);

	/* Load IFF chunks */
	while (!feof (f))
		iff_chunk(ctx, f);

	iff_release();

	for (i = 0; i < m->xxh->len; i++) {
		for (j = 0; j < m->xxh->pat; j++) {
			if (!memcmp(pord + i * 8, pnam + j * 8, sinaria ? 8 : 4)) {
				m->xxo[i] = j;
				break;
			}
		}

		if (j == m->xxh->pat)
			break;
	}

	free(pnam);
	free(pord);

	reportv(ctx, 0, "\n");

	return 0;
}
