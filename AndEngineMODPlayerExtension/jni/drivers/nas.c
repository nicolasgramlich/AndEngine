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
#include <stdlib.h>
#include <audio/audiolib.h>
#include <audio/soundlib.h>
#include "common.h"
#include "driver.h"
#include "mixer.h"

/*
 * nas_sendData(), nas_flush() and nas_eventHandler() from mpg123
 * written by Martin Denn <mdenn@unix-ag.uni-kl.de>
 */

typedef struct {
	AuServer *aud;
	AuFlowID flow;
	AuDeviceAttributes *da;
	int numDevices;
	char *buf;
	AuUint32 buf_size;
	AuUint32 buf_cnt;
	AuBool data_sent;
	AuBool finished;
} InfoRec, *InfoPtr;

static char *help[] = {
	"duration=val", "NAS sound port duration in seconds (default is 2)",
	"gain=val", "Audio output gain (default is 100)",
	"server=str", "NAS server (default is $AUDIOSERVER)",
	"watermark=val", "Percentual low watermark (default is 10)",
	NULL
};

static InfoRec info;

static int init(struct xmp_context *);
static void bufdump(struct xmp_context *, int);
static void myshutdown(struct xmp_context *);

static void dummy()
{
}

struct xmp_drv_info drv_nas = {
	"nas",			/* driver ID */
	"Network Audio System",	/* driver description */
	help,			/* help */
	init,			/* init */
	myshutdown,		/* shutdown */
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

static void nas_send(AuServer * aud, InfoPtr i, AuUint32 numBytes)
{
	_D(_D_INFO "send: numBytes = %ld, i->buf_cnt = %ld", numBytes,
	   i->buf_cnt);

	if (numBytes < i->buf_cnt) {
		AuWriteElement(aud, i->flow, 0, numBytes, i->buf, AuFalse,
			       NULL);
		memmove(i->buf, i->buf + numBytes, i->buf_cnt - numBytes);
		i->buf_cnt = i->buf_cnt - numBytes;
	} else {
		AuWriteElement(aud, i->flow, 0, i->buf_cnt, i->buf,
			       (numBytes > i->buf_cnt), NULL);
		i->buf_cnt = 0;
	}
	i->data_sent = AuTrue;
}

static AuBool nas_event(AuServer * aud, AuEvent * ev,
			AuEventHandlerRec * handler)
{
	InfoPtr i = (InfoPtr) handler->data;
	AuElementNotifyEvent *event = (AuElementNotifyEvent *) ev;

	_D(_D_INFO "event: i = %p, type = %d", i, ev->type);

	switch (ev->type) {
	case AuEventTypeMonitorNotify:
		i->finished = AuTrue;
		break;
	case AuEventTypeElementNotify:
		switch (event->kind) {
		case AuElementNotifyKindLowWater:
			nas_send(aud, i, event->num_bytes);
			break;
		case AuElementNotifyKindState:
			switch (event->cur_state) {
			case AuStatePause:
				if (event->reason != AuReasonUser)
					nas_send(aud, i, event->num_bytes);
				break;
			case AuStateStop:
				i->finished = AuTrue;
				break;
			}
		}
	}

	return AuTrue;
}

static void nas_flush()
{
	AuEvent ev;

	_D(_D_INFO "flush");

	while (!info.data_sent && !info.finished) {
		AuNextEvent(info.aud, AuTrue, &ev);
		AuDispatchEvent(info.aud, &ev);
	}
	info.data_sent = AuFalse;
}

static int init(struct xmp_context *ctx)
{
	struct xmp_options *o = &ctx->o;
	int channels, rate, format, buf_samples;
	int duration, gain, watermark;
	char *server;
	AuDeviceID device = AuNone;
	AuElement element[2];
	char *token, **parm;
	int i;

	duration = 2;
	gain = 100;
	server = NULL;
	watermark = 10;
	channels = 2;
	rate = o->freq;

	parm_init();
	chkparm1("duration", duration = atoi(token));
	chkparm1("gain", gain = atoi(token));
	chkparm1("server", server = token);
	chkparm1("watermark", watermark = atoi(token));
	parm_end();

	if (o->resol == 8) {
		format = o->outfmt & XMP_FMT_UNS ?
		    AuFormatLinearUnsigned8 : AuFormatLinearSigned8;
	} else {
		if (o->big_endian) {
			format = o->outfmt & XMP_FMT_UNS ?
				AuFormatLinearUnsigned16MSB :
				AuFormatLinearSigned16MSB;
		} else {
			format = o-> outfmt & XMP_FMT_UNS ?
				AuFormatLinearUnsigned16LSB :
				AuFormatLinearSigned16LSB;
		}
	}

	if (o->outfmt & XMP_FMT_MONO)
		channels = 1;

	info.aud = AuOpenServer(server, 0, NULL, 0, NULL, NULL);
	if (!info.aud) {
		fprintf(stderr, "xmp: drv_nas: can't connect to server %s\n",
			server ? server : "");
		return XMP_ERR_DINIT;
	}

	for (i = 0; i < AuServerNumDevices(info.aud); i++) {
		if (((AuDeviceKind(AuServerDevice(info.aud, i)) ==
		     AuComponentKindPhysicalOutput) &&
		     AuDeviceNumTracks(AuServerDevice(info.aud, i)) ==
		     channels)) {
			device =
			    AuDeviceIdentifier(AuServerDevice(info.aud, i));
			break;
		}
	}

	info.da = AuGetDeviceAttributes(info.aud, device, NULL);
	if (!info.da) {
		fprintf(stderr, "xmp: drv_nas: can't get device attributes\n");
		AuCloseServer(info.aud);
		return XMP_ERR_DINIT;
	}

	AuDeviceGain(info.da) = AuFixedPointFromSum(gain, 0);
	AuSetDeviceAttributes(info.aud, AuDeviceIdentifier(info.da),
			      AuCompDeviceGainMask, info.da, NULL);

	info.flow = AuCreateFlow(info.aud, NULL);
	if (!info.flow) {
		fprintf(stderr, "xmp: drv_nas: can't create flow\n");
		AuCloseServer(info.aud);
		return XMP_ERR_DINIT;
	}

	buf_samples = rate * duration;

	AuMakeElementImportClient(&element[0], rate, format, channels, AuTrue,
				  buf_samples,
				  (AuUint32) (buf_samples * watermark / 100), 0,
				  NULL);

	AuMakeElementExportDevice(&element[1], 0, device, rate,
				  AuUnlimitedSamples, 0, NULL);

	AuSetElements(info.aud, info.flow, AuTrue, 2, element, NULL);

	AuRegisterEventHandler(info.aud, AuEventHandlerIDMask, 0, info.flow,
			       nas_event, (AuPointer) & info);

	info.buf_size = buf_samples * channels * AuSizeofFormat(format);
	info.buf = (char *)malloc(info.buf_size);
	info.buf_cnt = 0;
	info.data_sent = AuFalse;
	info.finished = AuFalse;

	AuStartFlow(info.aud, info.flow, NULL);

	return xmp_smix_on(ctx);
}

static void bufdump(struct xmp_context *ctx, int len)
{
	int buf_cnt = 0;
	unsigned char *buf = xmp_smix_buffer(ctx);

	_D(_D_INFO "bufdump: %d", len);

	while ((info.buf_cnt + (len - buf_cnt)) > info.buf_size) {
		memcpy(info.buf + info.buf_cnt, buf + buf_cnt,
		       (info.buf_size - info.buf_cnt));
		buf_cnt += (info.buf_size - info.buf_cnt);
		info.buf_cnt += (info.buf_size - info.buf_cnt);
		nas_flush();
	}

	memcpy(info.buf + info.buf_cnt, buf + buf_cnt, (len - buf_cnt));
	info.buf_cnt += (len - buf_cnt);
}

static void myshutdown(struct xmp_context *ctx)
{
	while (!info.finished) {
		nas_flush();
	}

	xmp_smix_off(ctx);
	AuDestroyFlow(info.aud, info.flow, NULL);
	AuCloseServer(info.aud);
	free(info.buf);
}
