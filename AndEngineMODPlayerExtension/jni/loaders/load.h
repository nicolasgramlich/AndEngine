/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifndef __XMP_LOAD_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "common.h"
#include "xxm.h"
#include "effects.h"
#include "driver.h"
#include "convert.h"
#include "loader.h"

char *copy_adjust(uint8 *, uint8 *, int);
int test_name(uint8 *, int);
void read_title(FILE *, char *, int);
void set_xxh_defaults(struct xxm_header *);
void cvt_pt_event(struct xxm_event *, uint8 *);
void disable_continue_fx(struct xxm_event *);
void clean_s3m_seq(struct xxm_header *, uint8 *);

extern uint8 ord_xlat[];
extern int arch_vol_table[];

#define V(x) (m->verbosity > (x))

#define MAGIC4(a,b,c,d) \
    (((uint32)(a)<<24)|((uint32)(b)<<16)|((uint32)(c)<<8)|(d))

#define LOAD_INIT() do { \
    fseek(f, start, SEEK_SET); \
    m->med_vol_table = m->med_wav_table = NULL; \
    set_xxh_defaults(m->xxh); \
} while (0)

#define MODULE_INFO() do { \
    if (m->verbosity) { \
	if (*m->name) report("Module title   : %s\n", m->name); \
        if (*m->type) report("Module type    : %s\n", m->type); \
	if (*m->author) report("Author name    : %s\n", m->author); \
        if (m->xxh->len) report("Module length  : %d patterns\n", m->xxh->len); \
    } \
} while (0)

#define INSTRUMENT_INIT() do { \
    m->xxih = calloc(sizeof (struct xxm_instrument_header), m->xxh->ins); \
    m->xxim = calloc(sizeof (struct xxm_instrument_map), m->xxh->ins); \
    m->xxi = calloc(sizeof (struct xxm_instrument *), m->xxh->ins); \
    if (m->xxh->smp) { m->xxs = calloc (sizeof (struct xxm_sample), m->xxh->smp); }\
    m->xxae = calloc(sizeof (uint16 *), m->xxh->ins); \
    m->xxpe = calloc(sizeof (uint16 *), m->xxh->ins); \
    m->xxfe = calloc(sizeof (uint16 *), m->xxh->ins); \
} while (0)

#define PATTERN_INIT() do { \
    m->xxt = calloc(sizeof (struct xxm_track *), m->xxh->trk); \
    m->xxp = calloc(sizeof (struct xxm_pattern *), m->xxh->pat + 1); \
} while (0)

#define PATTERN_ALLOC(x) do { \
    m->xxp[x] = calloc(1, sizeof (struct xxm_pattern) + \
	sizeof (struct xxm_trackinfo) * (m->xxh->chn - 1)); \
} while (0)

#define TRACK_ALLOC(i) do { \
    int j; \
    for (j = 0; j < m->xxh->chn; j++) { \
	m->xxp[i]->info[j].index = i * m->xxh->chn + j; \
	m->xxt[i * m->xxh->chn + j] = calloc (sizeof (struct xxm_track) + \
	    sizeof (struct xxm_event) * m->xxp[i]->rows, 1); \
	m->xxt[i * m->xxh->chn + j]->rows = m->xxp[i]->rows; \
    } \
} while (0)

#define PATTERN_INIT() do { \
    m->xxt = calloc(sizeof (struct xxm_track *), m->xxh->trk); \
    m->xxp = calloc(sizeof (struct xxm_pattern *), m->xxh->pat + 1); \
} while (0)

#define PATTERN_ALLOC(x) do { \
    m->xxp[x] = calloc(1, sizeof (struct xxm_pattern) + \
	sizeof (struct xxm_trackinfo) * (m->xxh->chn - 1)); \
} while (0)

#define TRACK_ALLOC(i) do { \
    int j; \
    for (j = 0; j < m->xxh->chn; j++) { \
	m->xxp[i]->info[j].index = i * m->xxh->chn + j; \
	m->xxt[i * m->xxh->chn + j] = calloc (sizeof (struct xxm_track) + \
	    sizeof (struct xxm_event) * m->xxp[i]->rows, 1); \
	m->xxt[i * m->xxh->chn + j]->rows = m->xxp[i]->rows; \
    } \
} while (0)

#define INSTRUMENT_DEALLOC_ALL(i) do { \
    int k; \
    for (k = (i) - 1; k >= 0; k--) free(m->xxi[k]); \
    free(m->xxfe); \
    free(m->xxpe); \
    free(m->xxae); \
    if (m->xxh->smp) free(m->xxs); \
    free(m->xxi); \
    free(m->xxim); \
    free(m->xxih); \
} while (0)

#define PATTERN_DEALLOC_ALL(x) do { \
    int k; \
    for (k = x; k >= 0; k--) free(m->xxp[k]); \
    free(m->xxp); \
} while (0)

#define TRACK_DEALLOC_ALL(i) do { \
    int j, k; \
    for (k = i; k >= 0; k--) { \
	for (j = 0; j < m->xxh->chn; j++) \
	    free(m->xxt[k * m->xxh->chn + j]); \
    } \
    free(m->xxt); \
} while (0)

#endif
