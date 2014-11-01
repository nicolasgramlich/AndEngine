/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/* ALM (Aley's Module) is a module format used on 8bit computers. It was
 * designed to be usable on Sam Coupe (CPU Z80 6MHz) and PC XT. The ALM file
 * format is very simple and it have no special effects, so every computer
 * can play the ALMs.
 *
 * Note: xmp's module loading mechanism was not designed to load samples
 * from different files. Using *module into a global variable is a hack.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>


static int alm_test (FILE *, char *, const int);
static int alm_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info alm_loader = {
    "ALM",
    "Aley Keptr",
    alm_test,
    alm_load
};

static int alm_test(FILE *f, char *t, const int start)
{
    char buf[7];

    if (fread(buf, 1, 7, f) < 7)
	return -1;

    if (memcmp(buf, "ALEYMOD", 7) && memcmp(buf, "ALEY MO", 7))
	return -1;

    read_title(f, t, 0);

    return 0;
}



struct alm_file_header {
    uint8 id[7];		/* "ALEY MO" or "ALEYMOD" */
    uint8 speed;		/* Only in versions 1.1 and 1.2 */
    uint8 length;		/* Length of module */
    uint8 restart;		/* Restart position */
    uint8 order[128];		/* Pattern sequence */
};

#define NAME_SIZE 255

static int alm_load(struct xmp_context *ctx, FILE *f, const int start)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_mod_context *m = &p->m;
    int i, j;
    struct alm_file_header afh;
    struct xxm_event *event;
    struct stat stat;
    uint8 b;
    char *basename;
    char filename[NAME_SIZE];
    char modulename[NAME_SIZE];
    FILE *s;

    LOAD_INIT();

    fread(&afh.id, 7, 1, f);

    if (!strncmp((char *)afh.id, "ALEYMOD", 7))		/* Version 1.0 */
	m->xxh->tpo = afh.speed / 2;

    strncpy(modulename, m->filename, NAME_SIZE);
    basename = strtok (modulename, ".");

    afh.speed = read8(f);
    afh.length = read8(f);
    afh.restart = read8(f);
    fread(&afh.order, 128, 1, f);

    m->xxh->len = afh.length;
    m->xxh->rst = afh.restart;
    memcpy (m->xxo, afh.order, m->xxh->len);

    for (m->xxh->pat = i = 0; i < m->xxh->len; i++)
	if (m->xxh->pat < afh.order[i])
	    m->xxh->pat = afh.order[i];
    m->xxh->pat++;

    m->xxh->ins = 31;
    m->xxh->trk = m->xxh->pat * m->xxh->chn;
    m->xxh->smp = m->xxh->ins;
    m->c4rate = C4_NTSC_RATE;

    sprintf (m->type, "Aley's Module");

    MODULE_INFO();

    PATTERN_INIT();

    /* Read and convert patterns */
    reportv(ctx, 0, "Stored patterns: %d ", m->xxh->pat);

    for (i = 0; i < m->xxh->pat; i++) {
	PATTERN_ALLOC (i);
	m->xxp[i]->rows = 64;
	TRACK_ALLOC (i);
	for (j = 0; j < 64 * m->xxh->chn; j++) {
	    event = &EVENT (i, j % m->xxh->chn, j / m->xxh->chn);
	    b = read8(f);
	    if (b)
		event->note = (b == 37) ? 0x61 : b + 36;
	    event->ins = read8(f);
	}
	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    INSTRUMENT_INIT();

    /* Read and convert instruments and samples */

    reportv(ctx, 0, "Loading samples: %d ", m->xxh->ins);

    for (i = 0; i < m->xxh->ins; i++) {
	m->xxi[i] = calloc (sizeof (struct xxm_instrument), 1);
	snprintf(filename, NAME_SIZE, "%s.%d", basename, i + 1);
	s = fopen (filename, "rb");

	if (!(m->xxih[i].nsm = (s != NULL)))
	    continue;

	fstat (fileno (s), &stat);
	b = read8(s);		/* Get first octet */
	m->xxs[i].len = stat.st_size - 5 * !b;

	if (!b) {		/* Instrument with header */
	    m->xxs[i].lps = read16l(f);
	    m->xxs[i].lpe = read16l(f);
	    m->xxs[i].flg = m->xxs[i].lpe > m->xxs[i].lps ? WAVE_LOOPING : 0;
	} else {
	    fseek(s, 0, SEEK_SET);
	}

	m->xxi[i][0].pan = 0x80;
	m->xxi[i][0].vol = 0x40;
	m->xxi[i][0].sid = i;

	if ((V(1)) && (strlen((char *) m->xxih[i].name) ||
		(m->xxs[i].len > 1))) {
	    report ("\n[%2X] %-14.14s %04x %04x %04x %c V%02x ", i,
		filename, m->xxs[i].len, m->xxs[i].lps, m->xxs[i].lpe, m->xxs[i].flg
		& WAVE_LOOPING ? 'L' : ' ', m->xxi[i][0].vol);
	}

	xmp_drv_loadpatch(ctx, s, m->xxi[i][0].sid, m->c4rate,
	    XMP_SMP_UNS, &m->xxs[m->xxi[i][0].sid], NULL);

	fclose(s);

	reportv(ctx, 0, ".");
    }
    reportv(ctx, 0, "\n");

    /* ALM is LRLR, not LRRL */
    for (i = 0; i < m->xxh->chn; i++)
	m->xxc[i].pan = (i % 2) * 0xff;

    return 0;
}
