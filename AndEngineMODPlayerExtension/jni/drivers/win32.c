/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

/*
 * Based on Bjornar Henden's driver for Mikmod
 */
#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <windows.h>
#include "common.h"
#include "driver.h"
#include "mixer.h"

#define MAXBUFFERS	32			/* max number of buffers */
#define BUFFERSIZE	120			/* buffer size in ms */

static HWAVEOUT hwaveout;
static WAVEHDR header[MAXBUFFERS];
static LPSTR buffer[MAXBUFFERS];		/* pointers to buffers */
static WORD freebuffer;				/*  */
static WORD nextbuffer;				/* next buffer to be mixed */
static int num_buffers;

static int init(struct xmp_context *);
static void bufdump(struct xmp_context *, int);
static void deinit(struct xmp_context *);

static void dummy()
{
}

static char *help[] = {
	"buffers=val", "Number of buffers (default 10)",
	NULL
};

struct xmp_drv_info drv_win32 = {
	"win32",		/* driver ID */
	"Windows WinMM driver",	/* driver description */
	help,			/* help */
	init,			/* init */
	deinit,			/* shutdown */
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

static void show_error(int res)
{
	char *msg;

	switch (res) {
	case MMSYSERR_ALLOCATED:
		msg = "Device is already open";
		break;
	case MMSYSERR_BADDEVICEID:
		msg = "Device is out of range";
		break;
	case MMSYSERR_NODRIVER:
		msg = "No audio driver in this system";
		break;
	case MMSYSERR_NOMEM:
		msg = "Unable to allocate sound memory";
		break;
	case WAVERR_BADFORMAT:
		msg = "Audio format not supported";
		break;
	case WAVERR_SYNC:
		msg = "The device is synchronous";
		break;
	default:
		msg = "Unknown media error";
	}

	fprintf(stderr, "Error: %s", msg);
}

static void CALLBACK wave_callback(HWAVEOUT hwo, UINT uMsg, DWORD dwInstance,
				   DWORD dwParam1, DWORD dwParam2)
{
	if (uMsg == WOM_DONE) {
        	freebuffer++;
		freebuffer %= num_buffers;
	}
}

static int init(struct xmp_context *ctx)
{
	struct xmp_options *o = &ctx->o;
	MMRESULT res;
	WAVEFORMATEX wfe;
	int i;
	char *token, **parm;

	num_buffers = 10;
	
	parm_init();
	chkparm1("buffers", num_buffers = strtoul(token, NULL, 0));
	parm_end();

	if (num_buffers > MAXBUFFERS)
		num_buffers = MAXBUFFERS;

	if (!waveOutGetNumDevs())
		return XMP_ERR_DINIT;

	wfe.wFormatTag = WAVE_FORMAT_PCM;
	wfe.wBitsPerSample = o->resol;
	wfe.nChannels = o->flags & XMP_FMT_MONO ? 1 : 2;
	wfe.nSamplesPerSec = o->freq;
	wfe.nAvgBytesPerSec = wfe.nSamplesPerSec * wfe.nChannels *
	    wfe.wBitsPerSample / 8;
	wfe.nBlockAlign = wfe.nChannels * wfe.wBitsPerSample / 8;

	res = waveOutOpen(&hwaveout, WAVE_MAPPER, &wfe, (DWORD) wave_callback,
			  0, CALLBACK_FUNCTION);

	if (res != MMSYSERR_NOERROR) {
		show_error(res);
		return XMP_ERR_DINIT;
	}

	waveOutReset(hwaveout);

	for (i = 0; i < num_buffers; i++) {
		buffer[i] = malloc(OUT_MAXLEN);
		header[i].lpData = buffer[i];

		if (!buffer[i] || res != MMSYSERR_NOERROR) {
			show_error(res);
			return XMP_ERR_DINIT;
		}
	}

	freebuffer = nextbuffer = 0;

	return xmp_smix_on(ctx);
}

static void bufdump(struct xmp_context *ctx, int len)
{
	memcpy(buffer[nextbuffer], xmp_smix_buffer(ctx), len);

	while ((nextbuffer + 1) % num_buffers == freebuffer)
		Sleep(10);

        header[nextbuffer].dwBufferLength = len;
	waveOutPrepareHeader(hwaveout, &header[nextbuffer], sizeof(WAVEHDR));
        waveOutWrite(hwaveout, &header[nextbuffer], sizeof(WAVEHDR));

        nextbuffer++;
	nextbuffer %= num_buffers;
}

static void deinit(struct xmp_context *ctx)
{
	int i;

	xmp_smix_off(ctx);

	if (hwaveout) {
		for (i = 0; i < num_buffers; i++) {
			if (header[i].dwFlags & WHDR_PREPARED)
				waveOutUnprepareHeader(hwaveout, &header[i],
						       sizeof(WAVEHDR));
			free(buffer[i]);
		}
		while (waveOutClose(hwaveout) == WAVERR_STILLPLAYING)
			Sleep(10);
		hwaveout = NULL;
	}
}
