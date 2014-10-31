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

#include <Application.h>
#include <SoundPlayer.h>

#define B_AUDIO_CHAR 1
#define B_AUDIO_SHORT 2

extern "C" {
#include <string.h>
#include <stdlib.h>

#include "xmp.h"

/* g++ doesn't like typedef xmp_context and struct xmp_context */
#define xmp_context _xmp_context
#include "common.h"

#include "driver.h"
#include "mixer.h"
}

static int init (struct xmp_context *ctx);
static void bufdump (struct xmp_context *, int);
static void myshutdown (struct xmp_context *);

static void dummy()
{
}

static char *help[] = {
	"buffer=num,size", "set the number and size of buffer fragments",
	NULL
};

struct xmp_drv_info drv_beos = {
	"beos",				/* driver ID */
	"BeOS PCM audio",		/* driver description */
	NULL,				/* help */
	(int (*)())init,		/* init */
	(void (*)())myshutdown,		/* shutdown */
	(int (*)())xmp_smix_numvoices,	/* numvoices */
	dummy,				/* voicepos */
	(void (*)())xmp_smix_echoback,	/* echoback */
	dummy,				/* setpatch */
	(void (*)())xmp_smix_setvol,	/* setvol */
	dummy,				/* setnote */
	(void (*)())xmp_smix_setpan,	/* setpan */
	dummy,				/* setbend */
	(void (*)())xmp_smix_seteffect,	/* seteffect */
	dummy,				/* starttimer */
	dummy,				/* flush */
	dummy,				/* resetvoices */
	(void (*)())bufdump,		/* bufdump */
	dummy,				/* bufwipe */
	dummy,				/* clearmem */
	dummy,				/* sync */
	(int (*)())xmp_smix_writepatch,	/* writepatch */
	(int (*)())xmp_smix_getmsg,	/* getmsg */
	NULL
};

static media_raw_audio_format fmt;
static BSoundPlayer *player;


/*
 * CoreAudio helpers from mplayer/libao
 * The player fills a ring buffer, BSP retrieves data from the buffer
 */

static int paused;
static uint8 *buffer;
static int buffer_len;
static int buf_write_pos;
static int buf_read_pos;
static int chunk_size;
static int chunk_num;
static int packet_size;


/* return minimum number of free bytes in buffer, value may change between
 * two immediately following calls, and the real number of free bytes
 * might actually be larger!  */
static int buf_free()
{
	int free = buf_read_pos - buf_write_pos - chunk_size;
	if (free < 0)
		free += buffer_len;
	return free;
}

/* return minimum number of buffered bytes, value may change between
 * two immediately following calls, and the real number of buffered bytes
 * might actually be larger! */
static int buf_used()
{
	int used = buf_write_pos - buf_read_pos;
	if (used < 0)
		used += buffer_len;
	return used;
}

/* add data to ringbuffer */
static int write_buffer(unsigned char *data, int len)
{
	int first_len = buffer_len - buf_write_pos;
	int free = buf_free();

	if (len > free)
		len = free;
	if (first_len > len)
		first_len = len;

	/* till end of buffer */
	memcpy(buffer + buf_write_pos, data, first_len);
	if (len > first_len) {	/* we have to wrap around */
		/* remaining part from beginning of buffer */
		memcpy(buffer, data + first_len, len - first_len);
	}
	buf_write_pos = (buf_write_pos + len) % buffer_len;

	return len;
}

/* remove data from ringbuffer */
static int read_buffer(unsigned char *data, int len)
{
	int first_len = buffer_len - buf_read_pos;
	int buffered = buf_used();

	if (len > buffered)
		len = buffered;
	if (first_len > len)
		first_len = len;

	/* till end of buffer */
	memcpy(data, buffer + buf_read_pos, first_len);
	if (len > first_len) {	/* we have to wrap around */
		/* remaining part from beginning of buffer */
		memcpy(data + first_len, buffer, len - first_len);
	}
	buf_read_pos = (buf_read_pos + len) % buffer_len;

	return len;
}

/*
 * end of CoreAudio helpers
 */


void render_proc(void *theCookie, void *buffer, size_t req, 
				const media_raw_audio_format &format)
{ 
        int amt;

	while ((amt = buf_used()) < req)
		snooze(100000);

        read_buffer((unsigned char *)buffer, req);
}


static int init(struct xmp_context *ctx)
{
	struct xmp_options *o = &ctx->o;
	char *dev;
	char *token, **parm;
	static char desc[80];

	be_app = new BApplication("application/x-vnd.cm-xmp");

	chunk_size = 4096;
	chunk_num = 20;

	parm_init();
	chkparm2("buffer", "%d,%d", &chunk_num, &chunk_size);
	parm_end();

	snprintf(desc, 80, "%s [%d fragments of %d bytes]",
			drv_beos.description, chunk_num, chunk_size);
	drv_beos.description = desc;

	fmt.frame_rate = o->freq;
	fmt.channel_count = o->outfmt & XMP_FMT_MONO ? 1 : 2;
	fmt.format = o->resol > 8 ? B_AUDIO_SHORT : B_AUDIO_CHAR;
	fmt.byte_order = B_HOST_IS_LENDIAN ?
				B_MEDIA_LITTLE_ENDIAN : B_MEDIA_BIG_ENDIAN;
	fmt.buffer_size = chunk_size * chunk_num;

	buffer_len = chunk_size * chunk_num;
	buffer = (uint8 *)calloc(1, buffer_len);
	buf_read_pos = 0;
	buf_write_pos = 0;
	paused = 1;
	
	player = new BSoundPlayer(&fmt, "xmp output", render_proc);

	return xmp_smix_on(ctx);
}


/* Build and write one tick (one PAL frame or 1/50 s in standard vblank
 * timed mods) of audio data to the output device.
 */
static void bufdump(struct xmp_context *ctx, int i)
{
	uint8 *b;
	int j = 0;

	/* block until we have enough free space in the buffer */
	while (buf_free() < i)
		snooze(100000);

	b = (uint8 *)xmp_smix_buffer(ctx);

	while (i) {
        	if ((j = write_buffer(b, i)) > 0) {
			i -= j;
			b += j;
		} else
			break;
	}

	if (paused) {
		player->Start(); 
		player->SetHasData(true);
		paused = 0;
	}
}

static void myshutdown(struct xmp_context *ctx)
{
	player->Stop(); 
	xmp_smix_off(ctx);
	be_app->Lock();
	be_app->Quit();
}

