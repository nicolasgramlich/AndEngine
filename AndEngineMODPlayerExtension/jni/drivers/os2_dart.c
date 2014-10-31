//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//
//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//
/*
    This should work for OS/2 Dart 

    History: 
		1.0 - By Kevin Langman

*/
//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//
//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#define INCL_DOS
#include <os2.h>
#include <mcios2.h>
#include <meerror.h>
#include <os2medef.h>

#include "common.h"
#include "driver.h"
#include "mixer.h"

#define BUFFERCOUNT 4
#define BUF_MIN 8
#define BUF_MAX 32

static int init(struct xmp_context *);
static int setaudio(struct xmp_options *);
static void bufdump(struct xmp_context *, int);
static void shutdown(struct xmp_context *);

static MCI_MIX_BUFFER MixBuffers[BUFFERCOUNT];
static MCI_MIXSETUP_PARMS MixSetupParms;
static MCI_BUFFER_PARMS BufferParms;
static MCI_GENERIC_PARMS GenericParms;

static ULONG DeviceID = 0;
static int bsize = 16;
static short next = 2;
static short ready = 1;

static HMTX mutex;

static void dummy()
{
}

static char *help[] = {
	"sharing={Y,N}", "Device Sharing    (default is N)",
	"device=val", "OS/2 Audio Device (default is 0 auto-detect)",
	"buffer=val", "Audio buffer size (default is 16)",
	NULL
};

struct xmp_drv_info drv_os2dart = {
	"dart",			/* driver ID  */
	"OS/2 Direct Audio Realtime",	/* driver description */
	help,			/* help       */
	init,			/* init       */
	shutdown,		/* shutdown   */
	xmp_smix_numvoices,	/* numvoices  */
	dummy,			/* voicepos   */
	xmp_smix_echoback,	/* echoback   */
	dummy,			/* setpatch   */
	xmp_smix_setvol,	/* setvol     */
	dummy,			/* setnote    */
	xmp_smix_setpan,	/* setpan     */
	dummy,			/* setbend    */
	xmp_smix_seteffect,	/* seteffect  */
	dummy,			/* starttimer */
	dummy,			/* flush  */
	dummy,			/* reset      */
	bufdump,		/* bufdump    */
	dummy,			/* bufwipe    */
	dummy,			/* clearmem   */
	dummy,			/* sync       */
	xmp_smix_writepatch,	/* writepatch */
	xmp_smix_getmsg,	/* getmsg     */
	NULL
};

// Buffer update thread (created and called by DART) 
static LONG APIENTRY OS2_Dart_UpdateBuffers
    (ULONG ulStatus, PMCI_MIX_BUFFER pBuffer, ULONG ulFlags) {

	if ((ulFlags == MIX_WRITE_COMPLETE) ||
	    ((ulFlags == (MIX_WRITE_COMPLETE | MIX_STREAM_ERROR)) &&
	     (ulStatus == ERROR_DEVICE_UNDERRUN))) {
		DosRequestMutexSem(mutex, SEM_INDEFINITE_WAIT);
		ready++;
		DosReleaseMutexSem(mutex);
	}

	return (TRUE);
}

static int setaudio(struct xmp_options *o)
{
	char sharing = 0;
	int device = 0;
	int flags;
	int i;
	MCI_AMP_OPEN_PARMS AmpOpenParms;
	char *token, **parm;

	//printf( "In SetAudio...\n" );

	parm_init();
	chkparm1("sharing", sharing = *token);
	chkparm1("device", device = atoi(token));
	chkparm1("buffer", bsize = strtoul(token, NULL, 0));
	parm_end();

	if ((bsize < BUF_MIN || bsize > BUF_MAX) && bsize != 0) {
		bsize = 16 * 1024;
	} else {
		bsize *= 1024;
	}

	//if( sharing < 1 || sharing > 0 ){
	//     	sharing = 0;
	//}

	MixBuffers[0].pBuffer = NULL;	/* marker */
	memset(&GenericParms, 0, sizeof(MCI_GENERIC_PARMS));

	/* open AMP device */
	memset(&AmpOpenParms, 0, sizeof(MCI_AMP_OPEN_PARMS));
	AmpOpenParms.usDeviceID = 0;

	AmpOpenParms.pszDeviceType =
	    (PSZ) MAKEULONG(MCI_DEVTYPE_AUDIO_AMPMIX, (USHORT) device);

	flags = MCI_WAIT | MCI_OPEN_TYPE_ID;
	if (sharing == 'Y' || sharing == 'y') {
		flags = flags | MCI_OPEN_SHAREABLE;
	}

	if (mciSendCommand(0, MCI_OPEN, flags,
			   (PVOID) & AmpOpenParms, 0) != MCIERR_SUCCESS) {
		return -1;
	}

	DeviceID = AmpOpenParms.usDeviceID;

	/* setup playback parameters */
	memset(&MixSetupParms, 0, sizeof(MCI_MIXSETUP_PARMS));

	MixSetupParms.ulBitsPerSample = o->resol;
	MixSetupParms.ulFormatTag = MCI_WAVE_FORMAT_PCM;
	MixSetupParms.ulSamplesPerSec = o->freq;
	MixSetupParms.ulChannels = o->outfmt & XMP_FMT_MONO ? 1 : 2;
	MixSetupParms.ulFormatMode = MCI_PLAY;
	MixSetupParms.ulDeviceType = MCI_DEVTYPE_WAVEFORM_AUDIO;
	MixSetupParms.pmixEvent = OS2_Dart_UpdateBuffers;

	if (mciSendCommand(DeviceID, MCI_MIXSETUP,
			   MCI_WAIT | MCI_MIXSETUP_INIT,
			   (PVOID) & MixSetupParms, 0) != MCIERR_SUCCESS) {

		mciSendCommand(DeviceID, MCI_CLOSE, MCI_WAIT,
			       (PVOID) & GenericParms, 0);
		return -1;
	}

	/* take in account the DART suggested buffer size... */
	if (bsize == 0) {
		bsize = MixSetupParms.ulBufferSize;
	}
	//printf( "Dart Buffer Size = %d\n", bsize );

	BufferParms.ulNumBuffers = BUFFERCOUNT;
	BufferParms.ulBufferSize = bsize;
	BufferParms.pBufList = MixBuffers;

	if (mciSendCommand(DeviceID, MCI_BUFFER,
			   MCI_WAIT | MCI_ALLOCATE_MEMORY,
			   (PVOID) & BufferParms, 0) != MCIERR_SUCCESS) {
		mciSendCommand(DeviceID, MCI_CLOSE, MCI_WAIT,
			       (PVOID) & GenericParms, 0);
		return -1;
	}

	for (i = 0; i < BUFFERCOUNT; i++) {
		MixBuffers[i].ulBufferLength = bsize;
	}

	//MixBuffers[0].ulBufferLength = bsize;
	//MixBuffers[1].ulBufferLength = bsize;
	//MixBuffers[2].ulBufferLength = bsize;
	//MixBuffers[3].ulBufferLength = bsize;

	return 0;
}

static int init(struct xmp_context *ctx)
{
	//printf( "In Init...\n" );

	if (setaudio(ctl) != 0)
		return XMP_ERR_DINIT;

	/* Start Playback */
	//printf("Starting Playback!!\n");
	memset(MixBuffers[0].pBuffer, /*32767 */ 0, bsize);
	memset(MixBuffers[1].pBuffer, /*32767 */ 0, bsize);
	MixSetupParms.pmixWrite(MixSetupParms.ulMixHandle, MixBuffers, 2);

	//printf("Starting the Mixer!\n");
	return xmp_smix_on(ctx);

	//printf("Init Done!!\n");
}

/* Build and write one tick (one PAL frame or 1/50 s in standard vblank
 * timed mods) of audio data to the output device.
 */
static void bufdump(struct xmp_context *ctx, int i)
{
	static int index = 0;
	void *b;

	//printf( "In BufDump...\n" );

	b = xmp_smix_buffer(ctx);
	if (index + i > bsize) {

		//printf("Next = %d, ready = %d\n", next, ready);

		do {
			DosRequestMutexSem(mutex, SEM_INDEFINITE_WAIT);
			if (ready != 0) {
				DosReleaseMutexSem(mutex);
				break;
			}
			DosReleaseMutexSem(mutex);
			DosSleep(20);
		} while (TRUE);

		MixBuffers[next].ulBufferLength = index;
		MixSetupParms.pmixWrite(MixSetupParms.ulMixHandle,
					&(MixBuffers[next]), 1);
		ready--;
		next++;
		index = 0;
		if (next == BUFFERCOUNT) {
			next = 0;
		}
	}
	memcpy(&((char *)MixBuffers[next].pBuffer)[index], b, i);
	index += i;

}

static void shutdown(struct xmp_context *ctx)
{
	//printf( "In ShutDown...\n" );

	xmp_smix_off(ctx);

	if (MixBuffers[0].pBuffer) {
		mciSendCommand(DeviceID, MCI_BUFFER,
			       MCI_WAIT | MCI_DEALLOCATE_MEMORY, &BufferParms,
			       0);
		MixBuffers[0].pBuffer = NULL;
	}
	if (DeviceID) {
		mciSendCommand(DeviceID, MCI_CLOSE, MCI_WAIT,
			       (PVOID) & GenericParms, 0);
		DeviceID = 0;
	}
}
