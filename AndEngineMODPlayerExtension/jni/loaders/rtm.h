/* Real Tracker data structures
 * Copyright (C) 1997 Arnaud Hasenfratz
 *
 * Modified for xmp by Claudio Matsuoka
 * AmigaOS4 fixes by Chris Young
 */

#ifndef __amigaos4__
typedef uint8 BYTE;
typedef uint16 WORD;
#endif
typedef uint32 DWORD;

/* The following definitions are from the specification of the RTM format
 * version 1.10 by Arnaud Hasenfratz
 */

struct ObjectHeader {
    char id[4];			/* "RTMM", "RTND", "RTIN" or "RTSM" */
    char rc;			/* 0x20 */
    char name[32];		/* object name */
    char eof;			/* "\x1A" */
    WORD version;		/* version of the format (actual : 0x110) */
    WORD headerSize;		/* object header size */
};

struct RTMMHeader {		/* Real Tracker Music Module */
    char software[20];		/* software used for saving the module */
    char composer[32];
    WORD flags;			/* song flags */
    				/* bit 0 : linear table,
       				   bit 1 : track names present */
    BYTE ntrack;		/* number of tracks */
    BYTE ninstr;		/* number of instruments */
    WORD nposition;		/* number of positions */
    WORD npattern;		/* number of patterns */
    BYTE speed;			/* initial speed */
    BYTE tempo;			/* initial tempo */
    char panning[32];		/* initial pannings (for S3M compatibility) */
    DWORD extraDataSize;	/* length of data after the header */

/* version 1.12 */
    char originalName[32];
};

struct RTNDHeader {		/* Real Tracker Note Data */
    WORD flags;			/* Always 1 */
    BYTE ntrack;
    WORD nrows;
    DWORD datasize;		/* Size of packed data */
};

struct EnvelopePoint {
    long x;
    long y;
};

struct Envelope {
    BYTE npoint;
    struct EnvelopePoint point[12];
    BYTE sustain;
    BYTE loopstart;
    BYTE loopend;
    WORD flags;			/* bit 0 : enable envelope,
				   bit 1 : sustain, bit 2 : loop */
};

struct RTINHeader {		/* Real Tracker Instrument */
    BYTE nsample;
    WORD flags;			/* bit 0 : default panning enabled
				   bit 1 : mute samples */
    BYTE table[120];		/* sample number for each note */
    struct Envelope volumeEnv;
    struct Envelope panningEnv;
    char vibflg;		/* vibrato type */
    char vibsweep;		/* vibrato sweep */
    char vibdepth;		/* vibrato depth */
    char vibrate;		/* vibrato rate */
    WORD volfade;

/* version 1.10 */
    BYTE midiPort;
    BYTE midiChannel;
    BYTE midiProgram;
    BYTE midiEnable;

/* version 1.12 */
    char midiTranspose;                                                     
    BYTE midiBenderRange;                                                   
    BYTE midiBaseVolume;                                                    
    char midiUseVelocity;
};

struct RTSMHeader {		/* Real Tracker Sample */
    WORD flags;			/* bit 1 : 16 bits,
				   bit 2 : delta encoded (always) */
    BYTE basevolume;
    BYTE defaultvolume;
    DWORD length;
    BYTE loop;			/* =0:no loop, =1:forward loop,
				   =2:bi-directional loop */
    BYTE reserved[3];
    DWORD loopbegin;
    DWORD loopend;
    DWORD basefreq;
    BYTE basenote;
    char panning;		/* Panning from -64 to 64 */
};

