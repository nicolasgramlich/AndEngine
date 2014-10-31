/* Amiga AHI driver for Extended Module Player
 * Copyright (C) 2007 Lorence Lombardo
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>

#include "common.h"
#include "driver.h"
#include "mixer.h"
#include "convert.h"

static int fd;

static int init(struct xmp_context *);
static void bufdump(struct xmp_context *, int);
static void shutdown(struct xmp_context *);

static void dummy()
{
}

static char *help[] = {
	"buffer=val", "Audio buffer size",
	NULL
};

struct xmp_drv_info drv_amiga = {
	"ahi",			/* driver ID */
	"Amiga AHI audio",	/* driver description */
	help,			/* help */
	init,			/* init */
	shutdown,		/* shutdown */
	xmp_smix_numvoices,	/* numvoices */
	dummy,			/* voicepos */
	xmp_smix_echoback,	/* echoback */
	dummy,			/* setpatch */
	xmp_smix_setvol,	/* setvol */
	dummy,			/* setnote */
	xmp_smix_setpan,	/* setpan */
	dummy,			/* setbend */
	xmp_smix_seteffect,	/* seteffect */
	dummy,			/* starttimer */
	dummy,			/* flush */
	dummy,			/* resetvoices */
	bufdump,		/* bufdump */
	dummy,			/* bufwipe */
	dummy,			/* clearmem */
	dummy,			/* sync */
	xmp_smix_writepatch,	/* writepatch */
	xmp_smix_getmsg,	/* getmsg */
	NULL
};

static int init(struct xmp_context *ctx)
{
	struct xmp_options *o = &ctx->o;
	char outfile[256];
	int nch = o->outfmt & XMP_FMT_MONO ? 1 : 2;
	int bsize = o->freq * nch * o->resol / 4;
	char *token, **parm;
	
	parm_init();
	chkparm1("buffer", bsize = strtoul(token, NULL, 0));
	parm_end();

	sprintf(outfile, "AUDIO:B/%d/F/%d/C/%d/BUFFER/%d",
				o->resol, o->freq, nch, bsize);

	fd = open(outfile, O_WRONLY);

	return xmp_smix_on(ctx);
}

static void bufdump(struct xmp_context *ctx, int i)
{
	int j;
	void *b;

	b = xmp_smix_buffer(ctx);

	while (i) {
		if ((j = write(fd, b, i)) > 0) {
			i -= j;
			b = (char *)b + j;
		} else
			break;
	}
}

static void shutdown(struct xmp_context *ctx)
{
	xmp_smix_off(ctx);

	if (fd)
		close(fd);
}
