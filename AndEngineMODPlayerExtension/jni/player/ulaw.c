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

/* ulaw encoding function from sound2sun.c
 * Copyright (C) 1989 by Rich Gopstein and Harris Corporation
 */

int ulaw_encode (register int c)
{
    register int mask;

    if (c < 0) {
	c = -c;
	mask = 0x7f;
    } else
	mask = 0xff;

    if (c < 32)
	c = 0xf0 | (15 - (c >> 1));
    else if (c < 96)
	c = 0xe0 | (15 - ((c - 32) >> 2));
    else if (c < 224)
	c = 0xd0 | (15 - ((c - 96) >> 3));
    else if (c < 480)
	c = 0xc0 | (15 - ((c - 224) >> 4));
    else if (c < 992)
	c = 0xb0 | (15 - ((c - 480) >> 5));
    else if (c < 2016)
	c = 0xa0 | (15 - ((c - 992) >> 6));
    else if (c < 4064)
	c = 0x90 | (15 - ((c - 2016) >> 7));
    else if (c < 8160)
	c = 0x80 | (15 - ((c - 4064) >> 8));
    else
	c = 0x80;

    return mask & c;
}

