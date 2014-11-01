
#ifndef __PROWIZ_H
#define __PROWIZ_H

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdio.h>
#include "list.h"
#include "common.h"

#define MIN_FILE_LENGHT 2048

#define PW_TEST_CHUNK   0x10000

#define MAGIC4(a,b,c,d) \
    (((uint32)(a)<<24)|((uint32)(b)<<16)|((uint32)(c)<<8)|(d))

#ifndef __XMP_H
typedef unsigned char uint8;
typedef unsigned short uint16;
typedef unsigned int uint32;
#endif

#define PW_DELTA	0x0002
#define PW_PACKED	0x0004

#define PW_MOD_MAGIC	MAGIC4('M','.','K','.')

#define PW_REQUEST_DATA(s,n) \
	do { if ((s)<(n)) return ((n)-(s)); } while (0)

/*
 * depackb() and depackf() perform the same action reading the packed
 * module from a buffer or a file. We're supporting both protocols to
 * to avoid rewriting Asle's functions.
 */

struct pw_format {
	char *id;
	char *name;
	int flags;
	int (*test)(uint8 *, int);
	int (*depack)(FILE *, FILE *);
	int enable;
	struct list_head list;
};

int pw_wizardry(int, int, struct pw_format **);
int pw_move_data(FILE *, FILE *, int);
int pw_write_zero(FILE *, int);
int pw_enable(char *, int);
int pw_check(unsigned char *, int);

extern const uint8 ptk_table[37][2];
extern const short tun_table[16][36];

extern struct pw_format pw_ac1d;
extern struct pw_format pw_crb;
extern struct pw_format pw_di;
extern struct pw_format pw_eu;
extern struct pw_format pw_emod;
extern struct pw_format pw_fcm;
extern struct pw_format pw_fchs;
extern struct pw_format pw_fuzz;
extern struct pw_format pw_gmc;
extern struct pw_format pw_hrt;
extern struct pw_format pw_kris;
extern struct pw_format pw_ksm;
extern struct pw_format pw_mp_id;
extern struct pw_format pw_mp_noid;
extern struct pw_format pw_np1;
extern struct pw_format pw_np2;
extern struct pw_format pw_np3;
extern struct pw_format pw_ntp;
extern struct pw_format pw_p01;
extern struct pw_format pw_p10c;
extern struct pw_format pw_p18a;
extern struct pw_format pw_p20;
extern struct pw_format pw_p4x;
extern struct pw_format pw_p50a;
extern struct pw_format pw_p60a;
extern struct pw_format pw_p61a;
extern struct pw_format pw_pha;
extern struct pw_format pw_pp21;
extern struct pw_format pw_pru1;
extern struct pw_format pw_pru2;
extern struct pw_format pw_skyt;
extern struct pw_format pw_starpack;
extern struct pw_format pw_stim;
extern struct pw_format pw_tdd;
extern struct pw_format pw_titanics;
extern struct pw_format pw_tp3;
extern struct pw_format pw_unic_emptyid;
extern struct pw_format pw_unic_id;
extern struct pw_format pw_unic_noid;
extern struct pw_format pw_unic2;
extern struct pw_format pw_wn;
extern struct pw_format pw_xann;
extern struct pw_format pw_zen;

#endif
