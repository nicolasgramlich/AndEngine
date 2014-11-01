/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#include "common.h"

typedef int32 LONG;
typedef uint32 ULONG;
typedef int16 WORD;
typedef uint16 UWORD;
typedef int8 BYTE;
typedef uint8 UBYTE;
#ifndef __amigaos4__
typedef char *STRPTR;
#endif


/* Structures as defined in the MED/OctaMED MMD0 and MMD1 file formats,
 * revision 1, described by Teijo Kinnunen in Apr 25 1992
 */

struct PlaySeq {                                                            
    char name[32];		/* (0)  31 chars + \0 */                        
    ULONG reserved[2];		/* (32) for possible extensions */              
    UWORD length;		/* (40) # of entries */                         
    UWORD seq[1];		/* (42) block numbers.. */                      
};                                                      


struct MMD0sample {
    UWORD rep, replen;		/* offs: 0(s), 2(s) */
    UBYTE midich;		/* offs: 4(s) */
    UBYTE midipreset;		/* offs: 5(s) */
    UBYTE svol;			/* offs: 6(s) */
    BYTE strans;		/* offs: 7(s) */
}; 


struct MMD0song {
    struct MMD0sample sample[63];	/* 63 * 8 bytes = 504 bytes */
    UWORD numblocks;		/* offs: 504 */
    UWORD songlen;		/* offs: 506 */
    UBYTE playseq[256];		/* offs: 508 */
    UWORD deftempo;		/* offs: 764 */
    BYTE playtransp;		/* offs: 766 */
#define FLAG_FILTERON   0x1	/* the hardware audio filter is on */
#define FLAG_JUMPINGON  0x2	/* mouse pointer jumping on */
#define FLAG_JUMP8TH    0x4	/* ump every 8th line (not in OctaMED Pro) */
#define FLAG_INSTRSATT  0x8	/* sng+samples indicator (not useful in MMDs) */
#define FLAG_VOLHEX	0x10	/* volumes are HEX */
#define FLAG_STSLIDE    0x20	/* use ST/NT/PT compatible sliding */
#define FLAG_8CHANNEL   0x40	/* this is OctaMED 5-8 channel song */
#define FLAG_SLOWHQ     0X80	/* HQ V2-4 compatibility mode */
    UBYTE flags;		/* offs: 767 */
#define FLAG2_BMASK	0x1F	/* (bits 0-4) BPM beat length (in lines) */
#define FLAG2_BPM       0x20	/* BPM mode on */
#define FLAG2_MIX	0x80	/* Module uses mixing */
    UBYTE flags2;		/* offs: 768 */
    UBYTE tempo2;		/* offs: 769 */
    UBYTE trkvol[16];		/* offs: 770 */
    UBYTE mastervol;		/* offs: 786 */
    UBYTE numsamples;		/* offs: 787 */
};				/* length = 788 bytes */


/* This structure is exactly as long as the MMDsong structure. Common fields
 * are located at same offsets. You can also see, that there's a lot of room
 * for expansion in this structure.
 */
struct MMD2song {
    struct MMD0sample sample[63];
    UWORD numblocks;
    UWORD songlen;		/* NOTE: number of sections in MMD2 */
    struct PlaySeq **playseqtable;
    UWORD *sectiontable;	/* UWORD section numbers */
    UBYTE *trackvols;		/* UBYTE track volumes */
    UWORD numtracks;		/* max. number of tracks in the song
				 * (also the number of entries in
				 * 'trackvols' table) */
    UWORD numpseqs;		/* number of PlaySeqs in 'playseqtable' */
    BYTE *trackpans;		/* NULL means 'all centered */
#define FLAG3_STEREO	0x1	/* Mixing in stereo */
#define FLAG3_FREEPAN	0x2	/* Mixing flag: free pan */
    ULONG flags3;		/* see defs below */
    UWORD voladj;		/* volume adjust (%), 0 means 100 */
    UWORD channels;		/* mixing channels, 0 means 4 */
    UBYTE mix_echotype;		/* 0 = nothing, 1 = normal, 2 = cross */
    UBYTE mix_echodepth;	/* 1 - 6, 0 = default */
    UWORD mix_echolen;		/* echo length in milliseconds */
    BYTE mix_stereosep;		/* stereo separation */
    UBYTE pad0[223];		/* reserved for future expansion */
/* Fields below are MMD0/MMD1-compatible (except pad1[]) */
    UWORD deftempo;
    BYTE playtransp;
    UBYTE flags;
    UBYTE flags2;
    UBYTE tempo2;
    UBYTE pad1[16];		/* used to be trackvols, in MMD2 reserved */
    UBYTE mastervol;
    UBYTE numsamples;
};				/* length = 788 bytes */


struct MMD0 {
    ULONG id;
    ULONG modlen;
    struct MMD0song *song;
    UWORD psecnum;			/* MMD2 only */
    UWORD pseq;				/* MMD2 only */
    struct MMD0Block **blockarr;
#define MMD_LOADTOFASTMEM 0x1
    UBYTE mmdflags;			/* MMD2 only */
    UBYTE reserved[3];
    struct InstrHdr **smplarr;
    ULONG reserved2;
    struct MMD0exp *expdata;
    ULONG reserved3;
    UWORD pstate;			/* some data for the player routine */
    UWORD pblock;
    UWORD pline;
    UWORD pseqnum;
    WORD actplayline;
    UBYTE counter;
    UBYTE extra_songs;			/* number of songs - 1 */
};					/* length = 52 bytes */


struct MMD0Block {                                                          
    UBYTE numtracks, lines;                                                  
};                     


struct BlockCmdPageTable {                                                      
    UWORD num_pages;                                                      
    UWORD reserved;                                                       
    UWORD *page[1];                                                       
};


struct BlockInfo {
    ULONG *hlmask;
    UBYTE *blockname;
    ULONG blocknamelen;
    struct BlockCmdPageTable *pagetable;
    ULONG reserved[5];
};


struct MMD1Block {
    UWORD numtracks;
    UWORD lines;
    struct BlockInfo *info;
};


struct InstrHdr {
    ULONG length;
#define S_16 0x10			/* 16-bit sample */
#define STEREO 0x20			/* Stereo sample, not interleaved */
    WORD type;
    /* Followed by actual data */
};


struct SynthWF {                                                
    UWORD length;			/* length in words */
    BYTE  wfdata[1];			/* the waveform */
};          


struct SynthInstr {
    ULONG length;			/* length of this struct */
    WORD type;				/* -1 or -2 (offs: 4) */
    UBYTE defaultdecay;
    UBYTE reserved[3];
    UWORD rep;
    UWORD replen;
    UWORD voltbllen;			/* offs: 14 */
    UWORD wftbllen;			/* offs: 16 */
    UBYTE volspeed;			/* offs: 18 */
    UBYTE wfspeed;			/* offs: 19 */
    UWORD wforms;			/* offs: 20 */
    UBYTE voltbl[128];			/* offs: 22 */
    UBYTE wftbl[128];			/* offs: 150 */
    ULONG wf[64];			/* offs: 278 */
};


struct InstrExt {
    UBYTE hold;
    UBYTE decay;
    UBYTE suppress_midi_off;
    BYTE  finetune;
    /* Below fields saved by >= V5 */
    UBYTE default_pitch;
    UBYTE instr_flags;
    UWORD long_midi_preset;
    /* Below fields saved by >= V5.02 */
    UBYTE output_device;
    UBYTE reserved;
    /* Below fields saved by >= V7 */
    ULONG long_repeat;
    ULONG long_replen;
};


struct MMDInfo {
    struct MMDInfo *next;		/* next MMDInfo structure */
    UWORD reserved;
    UWORD type;				/* data type (1 = ASCII) */
    ULONG length;			/* data length in bytes */
    /* data follows... */                                         
};                                        


struct MMDARexxTrigCmd {                                      
    struct MMDARexxTrigCmd *next;	/* the next command, or NULL */
    UBYTE cmdnum;			/* command number (01..FF) */
    UBYTE pad;
    WORD cmdtype;			/* command type (OMACTION_...) */
    STRPTR cmd;				/* command, or NULL */
    STRPTR port;			/* port, or NULL */
    UWORD cmd_len;			/* length of 'cmd' string (without
					 * term. 0) */
    UWORD port_len;			/* length of 'port' string (without
					 * term. 0) */
};		/* current (V7) structure size: 20 */             


struct MMDARexx {
    UWORD res;				/* reserved, must be zero! */           
    UWORD trigcmdlen;			/* size of trigcmd entries             
					 * (MUST be used!!) */                 
    struct MMDARexxTrigCmd *trigcmd;	/* chain of MMDARexxTrigCmds or NULL */
};                                               


struct MMDMIDICmd3x {                                         
    UBYTE struct_vers;			/* current version = 0 */
    UBYTE pad;                                                
    UWORD num_of_settings;		/* number of Cmd3x settings 
					 * (currently set to 15) */
    UBYTE *ctrlr_types;			/* controller types */                  
    UWORD *ctrlr_numbers;		/* controller numbers */
};                                             


struct MMDInstrInfo {
    UBYTE name[40];
};

struct MMD0exp {
    struct MMD0 *nextmod;		/* pointer to the next module */
    struct InstrExt *exp_smp;		/* pointer to InstrExt */
    UWORD s_ext_entries;		/* size of InstrExt structure array */
    UWORD s_ext_entrsz;			/* size of each InstrExt structure */
    UBYTE *annotxt;			/* pointer to the annotation text */
    ULONG annolen;			/* length of 'annotxt' */
    struct MMDInstrInfo *iinfo;		/* pointer to MMDInstrInfo */
    UWORD i_ext_entries;		/* size of MMDInstrInfo struct array */
    UWORD i_ext_entrsz;			/* size of each MMDInstrInfo struct */
    ULONG jumpmask;			/* mouse pointer jump control */
    UWORD *rgbtable;			/* screen colors */
    UBYTE channelsplit[4];		/* channel splitting control */
    struct NotationInfo *n_info;	/* info for the notation editor */
    UBYTE *songname;			/* song name of the current song */
    ULONG songnamelen;			/* song name length */
    struct MMDDumpData *dumps;		/* MIDI dump data */
    struct MMDInfo *mmdinfo;		/* more information about the song */
    struct MMDARexx *mmdrexx;		/* embedded ARexx commands */
    struct MMDMIDICmd3x *mmdcmd3x;	/* settings for command 3x */
    ULONG reserved2[3];			/* future expansion fields */
    ULONG tag_end;
};


struct NotationInfo {
    UBYTE n_of_sharps;			/* number of sharps or flats */
#define NFLG_FLAT   1
#define NFLG_3_4    2
    UBYTE flags;
    WORD trksel[5];			/* number of the selected track */
    UBYTE trkshow[16];			/* tracks shown */
    UBYTE trkghost[16];			/* tracks ghosted */
    BYTE notetr[63];			/* note transpose for each instrument */
    UBYTE pad;
};


struct MMDDumpData {
    UWORD numdumps;
    UWORD reserved[3];
};


struct MMDDump {
    ULONG length;			/* length of the MIDI message dump */
    UBYTE *data;			/* pointer to MIDI dump data */
    UWORD ext_len;			/* MMDDump struct extension length */
    /* if ext_len >= 20: */
    UBYTE name[20];			/* name of the dump */
};

