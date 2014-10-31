/* Extended Module Player UMX module loader
 * Copyright (C) 2007 Claudio Matsuoka
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include "load.h"
#include "list.h"

#define TEST_SIZE 1500

#define MAGIC_UMX	MAGIC4(0xc1,0x83,0x2a,0x9e)
#define MAGIC_IMPM	MAGIC4('I','M','P','M')
#define MAGIC_SCRM	MAGIC4('S','C','R','M')
#define MAGIC_M_K_	MAGIC4('M','.','K','.')

extern struct list_head loader_list;

static int umx_test (FILE *, char *, const int);
static int umx_load (struct xmp_context *, FILE *, const int);

struct xmp_loader_info umx_loader = {
	"UMX",
	"Epic Games Unreal/UT",
	umx_test,
	umx_load
};

static int umx_test(FILE *f, char *t, const int start)
{
	int i, offset = -1;
	uint8 buf[TEST_SIZE], *b = buf;
	uint32 id;

	if (fread(buf, 1, TEST_SIZE, f) < TEST_SIZE)
		return -1;
;
	id = readmem32b(b);

	if (id != MAGIC_UMX)
		return -1;

	for (i = 0; i < TEST_SIZE; i++, b++) {
		id = readmem32b(b);

		if (!memcmp(b, "Extended Module:", 16)) {
			offset = i;
			break;
		}
		if (id == MAGIC_IMPM) { 
			offset = i;
			break;
		}
		if (i > 44 && id == MAGIC_SCRM) { 
			offset = i - 44;
			break;
		}
		if (i > 1080 && id == MAGIC_M_K_) { 
			offset = i - 1080;
			break;
		}
	}
	
	if (offset < 0)
		return -1;

	return 0;
}


static int load(struct xmp_context *ctx, FILE *f, char *fmt, int offset)
{
	struct xmp_loader_info *li;
	struct list_head *head;

	list_for_each(head, &loader_list) {
		li = list_entry(head, struct xmp_loader_info, list);
		if (strcmp(li->id, fmt) == 0) {
			if (li->loader(ctx, f, offset) == 0)
				return 0;
		}
	}

	return -1;
}


static int umx_load(struct xmp_context *ctx, FILE *f, const int start)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int i;
	uint8 buf[TEST_SIZE], *b = buf;
	uint32 id;

	LOAD_INIT();

	reportv(ctx, 0, "Container type : Epic Games UMX\n");

	fread(buf, 1, TEST_SIZE, f);

	for (i = 0; i < TEST_SIZE; i++, b++) {
		id = readmem32b(b);

		if (!memcmp(b, "Extended Module:", 16))
			return load(ctx, f, "XM", i);
		if (id == MAGIC_IMPM)
			return load(ctx, f, "IT", i);
		if (i > 44 && id == MAGIC_SCRM)
			return load(ctx, f, "S3M", i - 44);
		if (i > 1080 && id == MAGIC_M_K_)
			return load(ctx, f, "MOD", i - 1080);
	}
	
	return -1;
}





