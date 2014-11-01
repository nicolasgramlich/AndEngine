
#ifndef __XMP_COMMON_H
#define __XMP_COMMON_H

#ifdef __AROS__
#define __AMIGA__
#endif

/*
 * Sat, 15 Sep 2007 10:39:41 -0600
 * Reported by Jon Rafkind <workmin@ccs.neu.edu>
 * In megaman.xm there should be a tempo change at position 1 but in
 * xmp tempo and bpm remain the same.
 *
 * Claudio's fix: megaman has many (unused) samples, raise XMP_MAXPAT
 * from 256 to 1024. Otherwise, xmp ignores any event containing a sample
 * number above the limit.
 */

#define XMP_MAXPAT	1024		/* max number of samples in driver */
#define XMP_MAXORD	256		/* max number of patterns in module */
#define XMP_MAXROW	256		/* pattern loop stack size */
#define XMP_MAXVOL	(0x400 * 0x7fff)
#define XMP_EXTDRV	0xffff
#define XMP_MINLEN	0x1000
#define XMP_MAXCH	64		/* max virtual channels */
#define XMP_MAXVOC	64		/* max physical voices */

#if defined (DRIVER_OSS) || defined (DRIVER_OSS_SEQ)
#  if defined (HAVE_SYS_SOUNDCARD_H)
#    include <sys/soundcard.h>
#  elif defined (HAVE_MACHINE_SOUNDCARD_H)
#    include <machine/soundcard.h>
#  endif
#else
#  undef USE_ISA_CARDS
#  define WAVE_16_BITS    0x01	/* bit 0 = 8 or 16 bit wave data. */
#  define WAVE_UNSIGNED   0x02 	/* bit 1 = Signed - Unsigned data. */
#  define WAVE_LOOPING    0x04	/* bit 2 = looping enabled-1. */
#  define WAVE_BIDIR_LOOP 0x08	/* bit 3 = Set is bidirectional looping. */
#  define WAVE_LOOP_BACK  0x10	/* bit 4 = Set is looping backward. */
#endif

/* For emulation of Amiga Protracker-style sample loops: play entire
 * sample once before looping -- see menowantmiseria.mod
 */
#define WAVE_PTKLOOP	0x80	/* bit 7 = Protracker loop enable */
#define WAVE_FIRSTRUN	0x40	/* bit 6 = Protracker loop control */

#include <stdio.h>
#include <signal.h>

/* AmigaOS fixes by Chris Young <cdyoung@ntlworld.com>, Nov 25, 2007
 */
#if defined B_BEOS_VERSION
#  include <SupportDefs.h>
#elif defined __amigaos4__
#  include <exec/types.h>
#else
typedef signed char int8;
typedef signed short int int16;
typedef signed int int32;
typedef unsigned char uint8;
typedef unsigned short int uint16;
typedef unsigned int uint32;
#endif

#ifdef _MSC_VER				/* MSVC++6.0 has no long long */
typedef signed __int64 int64;
typedef unsigned __int64 uint64;
#elif !defined B_BEOS_VERSION		/*BeOS has its own int64 definition */
typedef unsigned long long uint64;
typedef signed long long int64;
#endif


#include "xmp.h"
#include "xxm.h"

/* Constants */
#define PAL_RATE	250.0		/* 1 / (50Hz * 80us)		  */
#define NTSC_RATE	208.0		/* 1 / (60Hz * 80us)		  */
#define C4_FREQ		130812		/* 440Hz / (2 ^ (21 / 12)) * 1000 */
#define C4_PAL_RATE	8287		/* 7093789.2 / period (C4) * 2	  */
#define C4_NTSC_RATE	8363		/* 7159090.5 / period (C4) * 2	  */

/* [Amiga] PAL color carrier frequency (PCCF) = 4.43361825 MHz */
/* [Amiga] CPU clock = 1.6 * PCCF = 7.0937892 MHz */

#define DEFAULT_AMPLIFY	0

/* Global flags */
#define PATTERN_BREAK	0x0001 
#define PATTERN_LOOP	0x0002 
#define MODULE_ABORT	0x0004 
#define GLOBAL_VSLIDE	0x0010
#define ROW_MAX		0x0100

#define MSN(x)		(((x)&0xf0)>>4)
#define LSN(x)		((x)&0x0f)
#define SET_FLAG(a,b)	((a)|=(b))
#define RESET_FLAG(a,b)	((a)&=~(b))
#define TEST_FLAG(a,b)	!!((a)&(b))

#define EVENT(a, c, r)	m->xxt[m->xxp[a]->info[c].index]->event[r]

#ifdef _MSC_VER
#define _D_CRIT "  Error: "
#define _D_WARN "Warning: "
#define _D_INFO "   Info: "
#ifndef CLIB_DECL
#define CLIB_DECL
#endif
#ifdef _DEBUG
#ifndef ATTR_PRINTF
#define ATTR_PRINTF(x,y)
#endif
void CLIB_DECL _D(const char *text, ...) ATTR_PRINTF(1,2);
#else
void __inline CLIB_DECL _D(const char *text, ...) { do {} while (0); }
#endif

#else	/* !_MSC_VER */

#ifdef _DEBUG
#define _D_INFO "\x1b[33m"
#define _D_CRIT "\x1b[31m"
#define _D_WARN "\x1b[36m"
#define _D(args...) do { \
	printf("\x1b[33m%s \x1b[37m[%s:%d] " _D_INFO, __PRETTY_FUNCTION__, \
		__FILE__, __LINE__); printf (args); printf ("\x1b[0m\n"); \
	} while (0)
#else
#define _D(args...) do {} while (0)
#endif

#endif	/* !_MSC_VER */

struct xmp_ord_info {
	int bpm;
	int tempo;
	int gvl;
	int time;
};



/* Context */

#include "list.h"
#include "xxm.h"

struct xmp_mod_context {
	int verbosity;			/* verbosity level */
	int time;			/* replay time in ms */
	char *dirname;			/* file dirname */
	char *basename;			/* file basename */
	char name[XMP_NAMESIZE];	/* module name */
	char type[XMP_NAMESIZE];	/* module type */
	char author[XMP_NAMESIZE];	/* module author */
	char *filename;			/* Module file name */
	char *comment;			/* Comments, if any */
	int size;			/* File size */
	double rrate;			/* Replay rate */
	int c4rate;			/* C4 replay rate */
	int volbase;			/* Volume base */
	int volume;			/* Global volume */
	int *vol_table;			/* Volume translation table */
	int flags;			/* Copy from options */
	int quirk;			/* Copy from options */

	struct xxm_header *xxh;		/* Header */
	struct xxm_pattern **xxp;	/* Patterns */
	struct xxm_track **xxt;		/* Tracks */
	struct xxm_instrument_header *xxih;	/* Instrument headers */
	struct xxm_instrument_map *xxim;	/* Instrument map */
	struct xxm_instrument **xxi;	/* Instruments */
	struct xxm_sample *xxs;		/* Samples */
	uint16 **xxae;			/* Amplitude envelope */
	uint16 **xxpe;			/* Pan envelope */
	uint16 **xxfe;			/* Pitch envelope */
	struct xxm_channel xxc[64];	/* Channel info */
	struct xmp_ord_info xxo_info[XMP_MAXORD];
	int xxo_fstrow[XMP_MAXORD];
	uint8 xxo[XMP_MAXORD];		/* Orders */

	uint8 **med_vol_table;		/* MED volume sequence table */
	uint8 **med_wav_table;		/* MED waveform sequence table */
};

struct flow_control {
	int pbreak;
	int jump;
	int delay;
	int jumpline;
	int row;
	int loop_chn;
	int* loop_start;
	int* loop_stack;
	int frame;
	int num_rows;
	int ord;
	int end_point;
	double time;
	double playing_time;
};

struct xmp_player_context {
	int pause;
	int pos;
	int tempo;
	int gvol_slide;
	int gvol_flag;
	int gvol_base;
	double tick_time;
	struct flow_control flow;
	struct xmp_channel *xc_data;
	int *fetch_ctl;
	int scan_ord;
	int scan_row;
	int scan_num;
	int bpm;
	void (*event_callback)(unsigned long, void *);
	void *callback_data;

	struct xmp_mod_context m;
};

struct xmp_driver_context {
	struct xmp_drv_info *driver;	/* Driver */
	char *description;		/* Driver description */
	char **help;			/* Driver help info */

	int ext;			/* External non-softmixer driver */
	int memavl;			/* Memory availble in sound card */
	int numtrk;			/* Number of tracks */
	int numchn;			/* Number of virtual channels */
	int numbuf;			/* Number of output buffers */
	int curvoc;			/* Number of voices currently in use */
	int maxvoc;			/* Number of sound card voices */
	int chnvoc;			/* Number of voices per channel */
	int agevoc;			/* Voice age control (?) */

	int cmute_array[XMP_MAXCH];

	int *ch2vo_count;
	int *ch2vo_array;
	struct voice_info *voice_array;
	struct patch_info **patch_array;

	void *buffer;
	int size;
};

struct xmp_smixer_context {
	char** buffer;		/* array of output buffers */
	int* buf32b;		/* temp buffer for 32 bit samples */
	int numvoc;		/* default softmixer voices number */
	int numbuf;		/* number of buffer to output */
	int mode;		/* mode = 0:OFF, 1:MONO, 2:STEREO */
	int resol;		/* resolution output 1:8bit, 2:16bit */
	int ticksize;
	int dtright;		/* anticlick control, right channel */
	int dtleft;		/* anticlick control, left channel */
	int echo_msg;		/* echoback message */
};

struct xmp_context {
	struct xmp_options o;
	struct xmp_driver_context d;
	struct xmp_player_context p;
	struct xmp_smixer_context s;
};


/* Externs */

extern void (*xmp_event_callback)(unsigned long);


/* Prototypes */

int	report			(char *, ...);
int	reportv			(struct xmp_context *, int, char *, ...);
int	ulaw_encode		(int);
char	*str_adj		(char *);
int	_xmp_scan_module	(struct xmp_context *);
int	_xmp_player_start	(struct xmp_context *);
int	_xmp_player_frame	(struct xmp_context *);
void	_xmp_player_end		(struct xmp_context *);
int	_xmp_tell_wait		(void);
int	_xmp_select_read	(int, int);
int	_xmp_read_rc		(struct xmp_context *);
void	_xmp_read_modconf	(struct xmp_context *, uint32, long);
int	cksum			(FILE *);

int8	read8s			(FILE *);
uint8	read8			(FILE *);
uint16	read16l			(FILE *);
uint16	read16b			(FILE *);
uint32	read24l			(FILE *);
uint32	read24b			(FILE *);
uint32	read32l			(FILE *);
uint32	read32b			(FILE *);

void	write8			(FILE *, uint8);
void	write16l		(FILE *, uint16);
void	write16b		(FILE *, uint16);
void	write32l		(FILE *, uint32);
void	write32b		(FILE *, uint32);

uint16	readmem16l		(uint8 *);
uint16	readmem16b		(uint8 *);
uint32	readmem32l		(uint8 *);
uint32	readmem32b		(uint8 *);

int	get_temp_dir		(char *, int);
#ifdef WIN32
int	mkstemp			(char *);
#endif

#endif /* __XMP_COMMON_H */
