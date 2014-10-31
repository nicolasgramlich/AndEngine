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

#include <string.h>
#include "driver.h"

extern struct xmp_fmt_info *__fmt_head;


struct xmp_module_info *xmp_get_module_info(xmp_context ctx, struct xmp_module_info *i)
{
    struct xmp_player_context *p = &((struct xmp_context *)ctx)->p;
    struct xmp_mod_context *m = &p->m;

    strncpy(i->name, m->name, 0x40);
    strncpy(i->type, m->type, 0x40);
    i->chn = m->xxh->chn;
    i->pat = m->xxh->pat;
    i->ins = m->xxh->ins;
    i->trk = m->xxh->trk;
    i->smp = m->xxh->smp;
    i->len = m->xxh->len;
    i->time = m->time;

    return i;
}


char *xmp_get_driver_description(xmp_context ctx)
{
    struct xmp_driver_context *d = &((struct xmp_context *)ctx)->d;

    return d->description;
}


struct xmp_fmt_info *xmp_get_fmt_info(struct xmp_fmt_info **x)
{
    return *x = __fmt_head;
}


struct xmp_drv_info *xmp_get_drv_info(struct xmp_drv_info **x)
{
    return *x = xmp_drv_array();		/* **** H:FIXME **** */
}
