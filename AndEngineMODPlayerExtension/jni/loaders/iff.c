/* Extended Module Player
 * Copyright (C) 1997-2010 Claudio Matsuoka and Hipolito Carraro Jr
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
#include "common.h"
#include "list.h"
#include "iff.h"

#include "load.h"

static LIST_HEAD(iff_list);

static int __id_size;
static int __flags;

void iff_chunk(struct xmp_context *ctx, FILE *f)
{
    long size;
    char id[17] = "";

    if (fread(id, 1, __id_size, f) != __id_size)
	return;

    if (__flags & IFF_SKIP_EMBEDDED) {
	/* embedded RIFF hack */
	if (!strncmp(id, "RIFF", 4)) {
	    read32b(f);
	    read32b(f);
	    fread(id, 1, __id_size, f);	/* read first chunk ID instead */
        }
    }

    size = (__flags & IFF_LITTLE_ENDIAN) ? read32l(f) : read32b(f);

    if (__flags & IFF_CHUNK_ALIGN2)
	size = (size + 1) & ~1;

    if (__flags & IFF_CHUNK_ALIGN4)
	size = (size + 3) & ~3;

    if (__flags & IFF_FULL_CHUNK_SIZE)
	size -= __id_size + 4;

    iff_process(ctx, id, size, f);
}


void iff_register(char *id, void (*loader)(struct xmp_context *, int, FILE *))
{
    struct iff_info *f;

    __id_size = 4;
    __flags = 0;

    f = malloc(sizeof (struct iff_info));
    strcpy(f->id, id);
    f->loader = loader;

    list_add_tail(&f->list, &iff_list);
}


void iff_release()
{
    struct list_head *tmp;
    struct iff_info *i;

    /* can't use list_for_each because we free the node before incrementing */
    for (tmp = (&iff_list)->next; tmp != (&iff_list); ) {
	i = list_entry(tmp, struct iff_info, list);
	list_del(&i->list);
	tmp = tmp->next;
	free(i);
    }
}


int iff_process(struct xmp_context *ctx, char *id, long size, FILE *f)
{
    struct list_head *tmp;
    struct iff_info *i;
    int pos;

    pos = ftell(f);

    list_for_each(tmp, &iff_list) {
	i = list_entry(tmp, struct iff_info, list);
	if (id && !strncmp(id, i->id, __id_size)) {
	    i->loader(ctx, size, f);
	    break;
	}
    }

    fseek(f, pos + size, SEEK_SET);

    return 0;
}


/* Functions to tune IFF mutations */

void iff_idsize (int n)
{
    __id_size = n;
}

void iff_setflag (int i)
{
    __flags |= i;
}

