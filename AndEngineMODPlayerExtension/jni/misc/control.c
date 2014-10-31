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

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <time.h>
#include <sys/time.h>
#include <unistd.h>

#include "driver.h"
#include "mixer.h"

static int drv_parm = 0;


int pw_init(void);

void *xmp_create_context()
{
	struct xmp_context *ctx;
	struct xmp_options *o;
	uint16 w;

	ctx = calloc(1, sizeof(struct xmp_context));

	if (ctx == NULL)
		return NULL;

	/* Explicitly initialize to keep valgrind happy */
	*ctx->p.m.name = *ctx->p.m.type = 0;

	o = &ctx->o;

	w = 0x00ff;
	o->big_endian = (*(char *)&w == 0x00);

	/* Set defaults */
	o->amplify = DEFAULT_AMPLIFY;
	o->freq = 44100;
	o->mix = 70;
	o->resol = 16;
	o->flags = XMP_CTL_DYNPAN | XMP_CTL_FILTER | XMP_CTL_ITPT;
	o->cf_cutoff = 0;

	return ctx;
}

void xmp_free_context(xmp_context ctx)
{
	free(ctx);
}

struct xmp_options *xmp_get_options(xmp_context ctx)
{
	return &((struct xmp_context *)ctx)->o;
}

int xmp_get_flags(xmp_context ctx)
{
	return ((struct xmp_context *)ctx)->p.m.flags;
}

void xmp_set_flags(xmp_context ctx, int flags)
{
	((struct xmp_context *)ctx)->p.m.flags = flags;
}

void xmp_init(xmp_context ctx, int argc, char **argv)
{
	struct xmp_player_context *p = &((struct xmp_context *)ctx)->p;
	int num;

	xmp_init_formats(ctx);
	pw_init();

	p->event_callback = NULL;

	/* must be parsed before loading the rc file. */
	for (num = 1; num < argc; num++) {
		if (!strcmp(argv[num], "--norc"))
			break;
	}
	if (num >= argc)
		_xmp_read_rc((struct xmp_context *)ctx);
}

void xmp_deinit(xmp_context ctx)
{
	xmp_deinit_formats(ctx);
}

int xmp_open_audio(xmp_context ctx)
{
	return xmp_drv_open((struct xmp_context *)ctx);
}

void xmp_close_audio(xmp_context ctx)
{
	xmp_drv_close((struct xmp_context *)ctx);
}

void xmp_set_driver_parameter(struct xmp_options *o, char *s)
{
	o->parm[drv_parm] = s;
	while (isspace(*o->parm[drv_parm]))
		o->parm[drv_parm]++;
	drv_parm++;
}

void xmp_register_event_callback(xmp_context ctx, void (*cb)(unsigned long, void *), void *data)
{
	struct xmp_player_context *p = &((struct xmp_context *)ctx)->p;

	p->event_callback = cb;
	p->callback_data = data;
}

void xmp_channel_mute(xmp_context ctx, int from, int num, int on)
{
	from += num - 1;

	if (num > 0) {
		while (num--)
			xmp_drv_mute((struct xmp_context *)ctx, from - num, on);
	}
}

int xmp_player_ctl(xmp_context ctx, int cmd, int arg)
{
	struct xmp_player_context *p = &((struct xmp_context *)ctx)->p;
	struct xmp_mod_context *m = &p->m;

	switch (cmd) {
	case XMP_ORD_PREV:
		if (p->pos > 0)
			p->pos--;
		return p->pos;
	case XMP_ORD_NEXT:
		if (p->pos < m->xxh->len)
			p->pos++;
		return p->pos;
	case XMP_ORD_SET:
		if (arg < m->xxh->len && arg >= 0)
			p->pos = arg;
		return p->pos;
	case XMP_MOD_STOP:
		p->pos = -2;
		break;
	case XMP_MOD_RESTART:
		p->pos = -1;
		break;
	case XMP_GVOL_DEC:
		if (m->volume > 0)
			m->volume--;
		return m->volume;
	case XMP_GVOL_INC:
		if (m->volume < 64)
			m->volume++;
		return m->volume;
	case XMP_TIMER_STOP:
		xmp_drv_stoptimer((struct xmp_context *)ctx);
		break;
	case XMP_TIMER_RESTART:
		xmp_drv_starttimer((struct xmp_context *)ctx);
		break;
	}

	return 0;
}

int xmp_player_start(xmp_context ctx)
{
	return _xmp_player_start((struct xmp_context *)ctx);
}

int xmp_player_frame(xmp_context ctx)
{
	return _xmp_player_frame((struct xmp_context *)ctx);
}

void xmp_player_end(xmp_context ctx)
{
	_xmp_player_end((struct xmp_context *)ctx);
}

void xmp_play_buffer(xmp_context ctx)
{
	xmp_drv_bufdump((struct xmp_context *)ctx);
}

void xmp_get_buffer(xmp_context ctx, void **buffer, int *size)
{
	*size = xmp_smix_softmixer((struct xmp_context *)ctx);
	*buffer = xmp_smix_buffer((struct xmp_context *)ctx);
}

void xmp_get_driver_cfg(xmp_context ctx, int *srate, int *res, int *chn,
			int *itpt)
{
	struct xmp_driver_context *d = &((struct xmp_context *)ctx)->d;
	struct xmp_options *o = &((struct xmp_context *)ctx)->o;

	*srate = d->memavl ? 0 : o->freq;
	*res = o->resol ? o->resol : 8 /* U_LAW */ ;
	*chn = o->outfmt & XMP_FMT_MONO ? 1 : 2;
	*itpt = !!(o->flags & XMP_CTL_ITPT);
}

int xmp_verbosity_level(xmp_context ctx, int i)
{
	struct xmp_options *o = &((struct xmp_context *)ctx)->o;
	int tmp;

	tmp = o->verbosity;
	o->verbosity = i;

	return tmp;
}

int xmp_seek_time(xmp_context ctx, int time)
{
	struct xmp_player_context *p = &((struct xmp_context *)ctx)->p;
	int i, t;
	/* _D("seek to %d, total %d", time, xmp_cfg.time); */

	time *= 1000;
	for (i = 0; i < p->m.xxh->len; i++) {
		t = p->m.xxo_info[i].time;

		_D("%2d: %d %d", i, time, t);

		if (t > time) {
			if (i > 0)
				i--;
			xmp_ord_set(ctx, i);
			return 0;
		}
	}
	return -1;
}
