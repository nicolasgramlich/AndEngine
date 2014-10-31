/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/*
 * OctaMED v1.00b: ftp://ftp.funet.fi/pub/amiga/fish/501-600/ff579
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "med.h"
#include "load.h"


static int mmd1_test(FILE *, char *, const int);
static int mmd1_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info mmd1_loader = {
	"MMD0/1",
	"MED 2.10/OctaMED",
	mmd1_test,
	mmd1_load
};

static int mmd1_test(FILE *f, char *t, const int start)
{
	char id[4];
	uint32 offset, len;

	if (fread(id, 1, 4, f) < 4)
		return -1;

	if (memcmp(id, "MMD0", 4) && memcmp(id, "MMD1", 4))
		return -1;

	fseek(f, 28, SEEK_CUR);
	offset = read32b(f);		/* expdata_offset */
	
	if (offset) {
		fseek(f, start + offset + 44, SEEK_SET);
		offset = read32b(f);
		len = read32b(f);
		fseek(f, start + offset, SEEK_SET);
		read_title(f, t, len);
	} else {
		read_title(f, t, 0);
	}

	return 0;
}


#define NUM_INST_TYPES 9
static char *inst_type[] = {
	"HYB",			/* -2 */
	"SYN",			/* -1 */
	"SMP",			/*  0 */
	"I5O",			/*  1 */
	"I3O",			/*  2 */
	"I2O",			/*  3 */
	"I4O",			/*  4 */
	"I6O",			/*  5 */
	"I7O",			/*  6 */
	"EXT",			/*  7 */
};

static int bpm_on, bpmlen;

static void xlat_fx(struct xxm_event *event)
{
	if (event->fxt > 0x0f) {
		event->fxt = event->fxp = 0;
		return;
	}

	switch (event->fxt) {
	case 0x05:		/* Old vibrato */
		event->fxp = (LSN(event->fxp) << 4) | MSN(event->fxp);
		break;
	case 0x06:		/* Not defined in MED 3.2 */
	case 0x07:		/* Not defined in MED 3.2 */
		break;
	case 0x08:		/* Set hold/decay */
		break;
	case 0x09:		/* Set secondary tempo */
		event->fxt = FX_TEMPO;
		break;
	case 0x0d:		/* Volume slide */
		event->fxt = FX_VOLSLIDE;
		break;
	case 0x0e:		/* Synth JMP */
		break;
	case 0x0f:
		if (event->fxp == 0x00) {	/* Jump to next block */
			event->fxt = 0x0d;
			break;
		} else if (event->fxp <= 0x0a) {
			break;
		} else if (event->fxp <= 0xf0) {
			event->fxt = FX_S3M_BPM;
			if (bpm_on)
				event->fxp = event->fxp * 8 / bpmlen;
			break;
		} else switch (event->fxp) {
		case 0xf1:	/* Play note twice */
			event->fxt = FX_EXTENDED;
			event->fxp = (EX_RETRIG << 4) | 3;
			break;
		case 0xf2:	/* Delay note */
			event->fxt = FX_EXTENDED;
			event->fxp = (EX_DELAY << 4) | 3;
			break;
		case 0xf3:	/* Play note three times */
			event->fxt = FX_EXTENDED;
			event->fxp = (EX_RETRIG << 4) | 2;
			break;
		case 0xf8:	/* Turn filter off */
		case 0xf9:	/* Turn filter on */
		case 0xfa:	/* MIDI pedal on */
		case 0xfb:	/* MIDI pedal off */
		case 0xfd:	/* Set pitch */
		case 0xfe:	/* End of song */
			event->fxt = event->fxp = 0;
			break;
		case 0xff:	/* Note cut */
			event->fxt = FX_EXTENDED;
			event->fxp = (EX_CUT << 4) | 3;
			break;
		default:
			event->fxt = event->fxp = 0;
		}
		break;
	}
}

static int mmd1_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i, j, k;
	struct MMD0 header;
	struct MMD0song song;
	struct MMD1Block block;
	struct InstrHdr instr;
	struct SynthInstr synth;
	struct InstrExt exp_smp;
	struct MMD0exp expdata;
	struct xxm_event *event;
	int ver = 0;
	int smp_idx = 0;
	uint8 e[4];
	int song_offset;
	int blockarr_offset;
	int smplarr_offset;
	int expdata_offset;
	int expsmp_offset;
	int songname_offset;
	int iinfo_offset;
	int pos;

	LOAD_INIT();

	fread(&header.id, 4, 1, f);

	ver = *((char *)&header.id + 3) - '1' + 1;

	_D(_D_WARN "load header");
	header.modlen = read32b(f);
	song_offset = read32b(f);
	_D(_D_INFO "song_offset = 0x%08x", song_offset);
	read16b(f);
	read16b(f);
	blockarr_offset = read32b(f);
	_D(_D_INFO "blockarr_offset = 0x%08x", blockarr_offset);
	read32b(f);
	smplarr_offset = read32b(f);
	_D(_D_INFO "smplarr_offset = 0x%08x", smplarr_offset);
	read32b(f);
	expdata_offset = read32b(f);
	_D(_D_INFO "expdata_offset = 0x%08x", expdata_offset);
	read32b(f);
	header.pstate = read16b(f);
	header.pblock = read16b(f);
	header.pline = read16b(f);
	header.pseqnum = read16b(f);
	header.actplayline = read16b(f);
	header.counter = read8(f);
	header.extra_songs = read8(f);

	/*
	 * song structure
	 */
	_D(_D_WARN "load song");
	fseek(f, start + song_offset, SEEK_SET);
	for (i = 0; i < 63; i++) {
		song.sample[i].rep = read16b(f);
		song.sample[i].replen = read16b(f);
		song.sample[i].midich = read8(f);
		song.sample[i].midipreset = read8(f);
		song.sample[i].svol = read8(f);
		song.sample[i].strans = read8s(f);
	}
	song.numblocks = read16b(f);
	song.songlen = read16b(f);
	_D(_D_INFO "song.songlen = %d", song.songlen);
	for (i = 0; i < 256; i++)
		song.playseq[i] = read8(f);
	song.deftempo = read16b(f);
	song.playtransp = read8(f);
	song.flags = read8(f);
	song.flags2 = read8(f);
	song.tempo2 = read8(f);
	for (i = 0; i < 16; i++)
		song.trkvol[i] = read8(f);
	song.mastervol = read8(f);
	song.numsamples = read8(f);

	/*
	 * convert header
	 */
	m->c4rate = C4_NTSC_RATE;
	m->quirk |= song.flags & FLAG_STSLIDE ? 0 : XMP_QRK_VSALL;
	bpm_on = song.flags2 & FLAG2_BPM;
	bpmlen = 1 + (song.flags2 & FLAG2_BMASK);
	m->quirk |= XMP_QRK_MEDBPM;

	/* From the OctaMEDv4 documentation:
	 *
	 * In 8-channel mode, you can control the playing speed more
	 * accurately (to techies: by changing the size of the mix buffer).
	 * This can be done with the left tempo gadget (values 1-10; the
	 * lower, the faster). Values 11-240 are equivalent to 10.
	 */

	/* Just a half-assed implementation of the spec above for tempo 1
	 * in PrivInv.med
	 */
	if (!bpm_on && song.deftempo < 10)
		song.deftempo = 0x35 - song.deftempo * 2;

	/* FIXME: med tempos are incorrectly handled */
	if (bpm_on) {
		m->xxh->tpo = song.tempo2;
		m->xxh->bpm = song.deftempo * bpmlen / 16;
	} else {
		m->xxh->tpo = song.tempo2;
		m->xxh->bpm = song.deftempo;

		if (m->xxh->bpm <= 10)
			m->xxh->bpm = m->xxh->bpm * 33 / 6;
	}

	m->xxh->pat = song.numblocks;
	m->xxh->ins = song.numsamples;
	//m->xxh->smp = m->xxh->ins;
	m->xxh->len = song.songlen;
	m->xxh->rst = 0;
	m->xxh->chn = 0;
	memcpy(m->xxo, song.playseq, m->xxh->len);
	m->name[0] = 0;

	/*
	 * Obtain number of samples from each instrument
	 */
	m->xxh->smp = 0;
	for (i = 0; i < m->xxh->ins; i++) {
		uint32 smpl_offset;
		int16 type;
		fseek(f, start + smplarr_offset + i * 4, SEEK_SET);
		smpl_offset = read32b(f);
		if (smpl_offset == 0)
			continue;
		fseek(f, start + smpl_offset, SEEK_SET);
		read32b(f);				/* length */
		type = read16b(f);
		if (type == -1) {			/* type is synth? */
			fseek(f, 14, SEEK_CUR);
			m->xxh->smp += read16b(f);		/* wforms */
		} else {
			m->xxh->smp++;
		}
	}

	/*
	 * expdata
	 */
	_D(_D_WARN "load expdata");
	expdata.s_ext_entries = 0;
	expdata.s_ext_entrsz = 0;
	expdata.i_ext_entries = 0;
	expdata.i_ext_entrsz = 0;
	expsmp_offset = 0;
	iinfo_offset = 0;
	if (expdata_offset) {
		fseek(f, start + expdata_offset, SEEK_SET);
		read32b(f);
		expsmp_offset = read32b(f);
		_D(_D_INFO "expsmp_offset = 0x%08x", expsmp_offset);
		expdata.s_ext_entries = read16b(f);
		expdata.s_ext_entrsz = read16b(f);
		read32b(f);
		read32b(f);
		iinfo_offset = read32b(f);
		_D(_D_INFO "iinfo_offset = 0x%08x", iinfo_offset);
		expdata.i_ext_entries = read16b(f);
		expdata.i_ext_entrsz = read16b(f);
		read32b(f);
		read32b(f);
		read32b(f);
		read32b(f);
		songname_offset = read32b(f);
		_D(_D_INFO "songname_offset = 0x%08x", songname_offset);
		expdata.songnamelen = read32b(f);
		fseek(f, start + songname_offset, SEEK_SET);
		_D(_D_INFO "expdata.songnamelen = %d", expdata.songnamelen);
		for (i = 0; i < expdata.songnamelen; i++) {
			if (i >= XMP_NAMESIZE)
				break;
			m->name[i] = read8(f);
		}
	}

	/*
	 * Quickly scan patterns to check the number of channels
	 */
	_D(_D_WARN "find number of channels");

	for (i = 0; i < m->xxh->pat; i++) {
		int block_offset;

		fseek(f, start + blockarr_offset + i * 4, SEEK_SET);
		block_offset = read32b(f);
		_D(_D_INFO "block %d block_offset = 0x%08x", i, block_offset);
		if (block_offset == 0)
			continue;
		fseek(f, start + block_offset, SEEK_SET);

		if (ver > 0) {
			block.numtracks = read16b(f);
			block.lines = read16b(f);
		} else {
			block.numtracks = read8(f);
			block.lines = read8(f);
		}

		if (block.numtracks > m->xxh->chn)
			m->xxh->chn = block.numtracks;
	}

	m->xxh->trk = m->xxh->pat * m->xxh->chn;

	strcpy(m->type, ver == 0 ? m->xxh->chn > 4 ? "MMD0 (OctaMED 2.00)" :
			"MMD0 (MED 2.10)" : "MMD1 (OctaMED 4.00)");
	
	MODULE_INFO();

	if (V(0)) {
		report("BPM mode       : %s", bpm_on ? "on" : "off");
		if (bpm_on)
			report(" (length = %d)", bpmlen);
		report("\n");
		if (V(1) && song.playtransp)
			report("Song transpose : %d\n", song.playtransp);
		report("Stored patterns: %d ", m->xxh->pat);
	}

	/*
	 * Read and convert patterns
	 */
	_D(_D_WARN "read patterns");
	PATTERN_INIT();

	for (i = 0; i < m->xxh->pat; i++) {
		int block_offset;

		fseek(f, start + blockarr_offset + i * 4, SEEK_SET);
		block_offset = read32b(f);
		if (block_offset == 0)
			continue;
		fseek(f, start + block_offset, SEEK_SET);

		if (ver > 0) {
			block.numtracks = read16b(f);
			block.lines = read16b(f);
			read32b(f);
		} else {
			block.numtracks = read8(f);
			block.lines = read8(f);
		}

		PATTERN_ALLOC(i);

		m->xxp[i]->rows = block.lines + 1;
		TRACK_ALLOC(i);

		if (ver > 0) {		/* MMD1 */
			for (j = 0; j < m->xxp[i]->rows; j++) {
				for (k = 0; k < block.numtracks; k++) {
					e[0] = read8(f);
					e[1] = read8(f);
					e[2] = read8(f);
					e[3] = read8(f);

					event = &EVENT(i, k, j);
					event->note = e[0] & 0x7f;
					if (event->note)
						event->note +=
						    36 + song.playtransp;
					event->ins = e[1] & 0x3f;
					event->fxt = e[2];
					event->fxp = e[3];
					xlat_fx(event);
				}
			}
		} else {		/* MMD0 */
			for (j = 0; j < m->xxp[i]->rows; j++) {
				for (k = 0; k < block.numtracks; k++) {
					e[0] = read8(f);
					e[1] = read8(f);
					e[2] = read8(f);

					event = &EVENT(i, k, j);
					event->note = e[0] & 0x3f;
					if (event->note)
						event->note += 36;
					event->ins =
					    (e[1] >> 4) | ((e[0] & 0x80) >> 3)
					    | ((e[0] & 0x40) >> 1);
					event->fxt = e[1] & 0x0f;
					event->fxp = e[2];
					xlat_fx(event);
				}
			}
		}
		reportv(ctx, 0, ".");
	}
	reportv(ctx, 0, "\n");

	m->med_vol_table = calloc(sizeof(uint8 *), m->xxh->ins);
	m->med_wav_table = calloc(sizeof(uint8 *), m->xxh->ins);

	/*
	 * Read and convert instruments and samples
	 */
	_D(_D_WARN "read instruments");
	INSTRUMENT_INIT();

	reportv(ctx, 0, "Instruments    : %d ", m->xxh->ins);
	reportv(ctx, 1, "\n     Instrument name                          Typ Len   LBeg  LEnd  Vl Xpo Ft");

	for (smp_idx = i = 0; i < m->xxh->ins; i++) {
		int smpl_offset;
		fseek(f, start + smplarr_offset + i * 4, SEEK_SET);
		smpl_offset = read32b(f);
		//_D(_D_INFO "sample %d smpl_offset = 0x%08x", i, smpl_offset);
		if (smpl_offset == 0)
			continue;

		fseek(f, start + smpl_offset, SEEK_SET);
		instr.length = read32b(f);
		instr.type = read16b(f);

		pos = ftell(f);

		if (V(1)) {
			char name[40] = "";
			if (expdata_offset && i < expdata.i_ext_entries) {
				fseek(f, iinfo_offset +
					i * expdata.i_ext_entrsz, SEEK_SET);
				fread(name, 40, 1, f);
			}

			report("\n[%2x] %-40.40s %s ", i, name,
				instr.type + 2 <= NUM_INST_TYPES ?
					inst_type[instr.type + 2] : "???");
		}

		exp_smp.finetune = 0;
		if (expdata_offset && i < expdata.s_ext_entries) {
			fseek(f, expsmp_offset + i * expdata.s_ext_entrsz,
							SEEK_SET);
			exp_smp.hold = read8(f);
			exp_smp.decay = read8(f);
			exp_smp.suppress_midi_off = read8(f);
			exp_smp.finetune = read8(f);

		}

		fseek(f, start + pos, SEEK_SET);

		if (instr.type == -2) {			/* Hybrid */
			int length, type;
			int pos = ftell(f);

			synth.defaultdecay = read8(f);
			fseek(f, 3, SEEK_CUR);
			synth.rep = read16b(f);
			synth.replen = read16b(f);
			synth.voltbllen = read16b(f);
			synth.wftbllen = read16b(f);
			synth.volspeed = read8(f);
			synth.wfspeed = read8(f);
			synth.wforms = read16b(f);
			fread(synth.voltbl, 1, 128, f);;
			fread(synth.wftbl, 1, 128, f);;

			fseek(f, start + pos - 6 + read32b(f), SEEK_SET);
			length = read32b(f);
			type = read16b(f);

			m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);
			m->xxih[i].nsm = 1;
			m->xxih[i].vts = synth.volspeed;
			m->xxih[i].wts = synth.wfspeed;
			m->xxi[i][0].pan = 0x80;
			m->xxi[i][0].vol = song.sample[i].svol;
			m->xxi[i][0].xpo = song.sample[i].strans;
			m->xxi[i][0].sid = smp_idx;
			m->xxi[i][0].fin = exp_smp.finetune;
			m->xxs[smp_idx].len = length;
			m->xxs[smp_idx].lps = 2 * song.sample[i].rep;
			m->xxs[smp_idx].lpe = m->xxs[smp_idx].lps +
						2 * song.sample[i].replen;
			m->xxs[smp_idx].flg = song.sample[i].replen > 1 ?
						WAVE_LOOPING : 0;

			reportv(ctx, 1, "%05x %05x %05x %02x %+3d %+1d ",
				       m->xxs[smp_idx].len, m->xxs[smp_idx].lps,
				       m->xxs[smp_idx].lpe, m->xxi[i][0].vol,
				       m->xxi[i][0].xpo,
				       m->xxi[i][0].fin >> 4);

			xmp_drv_loadpatch(ctx, f, smp_idx, m->c4rate, 0,
					&m->xxs[smp_idx], NULL);

			smp_idx++;

			m->med_vol_table[i] = calloc(1, synth.voltbllen);
			memcpy(m->med_vol_table[i], synth.voltbl, synth.voltbllen);

			m->med_wav_table[i] = calloc(1, synth.wftbllen);
			memcpy(m->med_wav_table[i], synth.wftbl, synth.wftbllen);

			reportv(ctx, 0, ".");

			continue;
		}

		if (instr.type == -1) {			/* Synthetic */
			int pos = ftell(f);

			synth.defaultdecay = read8(f);
			fseek(f, 3, SEEK_CUR);
			synth.rep = read16b(f);
			synth.replen = read16b(f);
			synth.voltbllen = read16b(f);
			synth.wftbllen = read16b(f);
			synth.volspeed = read8(f);
			synth.wfspeed = read8(f);
			synth.wforms = read16b(f);
			fread(synth.voltbl, 1, 128, f);;
			fread(synth.wftbl, 1, 128, f);;
			for (j = 0; j < 64; j++)
				synth.wf[j] = read32b(f);

			reportv(ctx, 1, "VS:%02x WS:%02x WF:%02x %02x %+3d %+1d ",
					synth.volspeed, synth.wfspeed,
					synth.wforms & 0xff,
					song.sample[i].svol,
					song.sample[i].strans,
					exp_smp.finetune);

			if (synth.wforms == 0xffff)	
				continue;

			m->xxi[i] = calloc(sizeof(struct xxm_instrument),
							synth.wforms);
			m->xxih[i].nsm = synth.wforms;
			m->xxih[i].vts = synth.volspeed;
			m->xxih[i].wts = synth.wfspeed;

			for (j = 0; j < synth.wforms; j++) {
				m->xxi[i][j].pan = 0x80;
				m->xxi[i][j].vol = song.sample[i].svol;
				m->xxi[i][j].xpo = song.sample[i].strans - 24;
				m->xxi[i][j].sid = smp_idx;
				m->xxi[i][j].fin = exp_smp.finetune;

				fseek(f, start + pos - 6 + synth.wf[j], SEEK_SET);

				m->xxs[smp_idx].len = read16b(f) * 2;
				m->xxs[smp_idx].lps = 0;
				m->xxs[smp_idx].lpe = m->xxs[smp_idx].len;
				m->xxs[smp_idx].flg = WAVE_LOOPING;

				xmp_drv_loadpatch(ctx, f, smp_idx, m->c4rate,
					XMP_SMP_8X, &m->xxs[smp_idx], NULL);


				smp_idx++;
			}

			m->med_vol_table[i] = calloc(1, synth.voltbllen);
			memcpy(m->med_vol_table[i], synth.voltbl, synth.voltbllen);

			m->med_wav_table[i] = calloc(1, synth.wftbllen);
			memcpy(m->med_wav_table[i], synth.wftbl, synth.wftbllen);

			reportv(ctx, 0, ".");

			continue;
		}

		if (instr.type != 0)
			continue;

		/* instr type is sample */
		m->xxi[i] = calloc(sizeof(struct xxm_instrument), 1);
		m->xxih[i].nsm = 1;

		m->xxi[i][0].vol = song.sample[i].svol;
		m->xxi[i][0].pan = 0x80;
		m->xxi[i][0].xpo = song.sample[i].strans;
		m->xxi[i][0].sid = smp_idx;
		m->xxi[i][0].fin = exp_smp.finetune << 4;

		m->xxs[smp_idx].len = instr.length;
		m->xxs[smp_idx].lps = 2 * song.sample[i].rep;
		m->xxs[smp_idx].lpe = m->xxs[smp_idx].lps + 2 *
						song.sample[i].replen;

		m->xxs[smp_idx].flg = 0;
		if (song.sample[i].replen > 1)
			m->xxs[smp_idx].flg |= WAVE_LOOPING;

		reportv(ctx, 1, "%05x %05x %05x %02x %+3d %+1d ",
				m->xxs[smp_idx].len,
				m->xxs[smp_idx].lps,
				m->xxs[smp_idx].lpe,
				m->xxi[i][0].vol,
				m->xxi[i][0].xpo,
				m->xxi[i][0].fin >> 4);

		fseek(f, start + smpl_offset + 6, SEEK_SET);
		xmp_drv_loadpatch(ctx, f, smp_idx, m->c4rate, 0,
				  &m->xxs[smp_idx], NULL);

		reportv(ctx, 0, ".");

		smp_idx++;
	}

	reportv(ctx, 0, "\n");

	/*
	 * Adjust event note data in patterns
	 */

	/* Restrict non-synth instruments to 3 octave range.
	 * Checked in MMD0 with med.egypian/med.medieval from Lemmings 2
	 * and MED.ParasolStars, MMD1 with med.Lemmings2
	 */
	for (i = 0; i < m->xxh->pat; i++) {
		for (j = 0; j < m->xxp[i]->rows; j++) {
			for (k = 0; k < m->xxh->chn; k++) {
				event = &EVENT(i, k, j);

				if (!event->note || !event->ins)
					continue;

				/* Not a synth instrument */
				if (!p->m.med_wav_table[event->ins - 1]) {
					while (event->note > (35 + 36))
						event->note -= 12;
				}
			}
		}
	}

	for (i = 0; i < m->xxh->chn; i++) {
		m->xxc[i].vol = song.trkvol[i];
		m->xxc[i].pan = (((i + 1) / 2) % 2) * 0xff;
	}

	return 0;
}
