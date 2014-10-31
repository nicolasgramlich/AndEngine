
#ifndef __MIXER_H
#define __MIXER_H

/*#define SMIX_C4NOTE	6947*/
#define SMIX_C4NOTE	6864

#define SMIX_NUMVOC	64	/* default number of softmixer voices */
#define SMIX_MAXRATE	48000	/* max sampling rate (Hz) */
#define SMIX_MINBPM	5	/* min BPM */
#define SMIX_RESMAX	(sizeof (int16))	/* max output resolution */

#define SMIX_SHIFT	16
#define SMIX_MASK	0xffff

#define OUT_MAXLEN	(5 * 2 * SMIX_MAXRATE * SMIX_RESMAX / SMIX_MINBPM / 3)

#define FILTER_PRECISION (1 << 12)

/* Anticlick ramps */
#define SLOW_ATTACK	64
#define SLOW_RELEASE	16


struct voice_info {
	int chn;		/* channel link */
	int root;		/* */
	unsigned int age;	/* */
	int note;		/* */
	int pan;		/* */
	int vol;		/* */
	int period;		/* current period */
	int pbase;		/* base period */
	int itpt;		/* interpolation */
	int pos;		/* position in sample */
	int fidx;		/* function index */
	int fxor;		/* function xor control */
	int ins;		/* instrument number */
	int smp;		/* sample number */
	int end;		/* loop end */
	int freq;
	int shl8;
	int act;		/* nna info & status of voice */
	int sleft;		/* last left sample output, in 32bit */
	int sright;		/* last right sample output, in 32bit */
	void *sptr;		/* sample pointer */

	int flt_X1;		/* filter variables */
	int flt_X2;
	int flt_B0;
	int flt_B1;
	int flt_B2;
	int cutoff;
	int resonance;

	int attack;		/* ramp up anticlick */
};

int	xmp_smix_on		(struct xmp_context *);
void	xmp_smix_off		(struct xmp_context *);
void    xmp_smix_setvol		(struct xmp_context *, int, int);
void    xmp_smix_seteffect	(struct xmp_context *, int, int, int);
void    xmp_smix_setpan		(struct xmp_context *, int, int);
int	xmp_smix_numvoices	(struct xmp_context *, int);
void	xmp_smix_echoback	(struct xmp_context *, int);
void	xmp_smix_starttimer	(void);
void	xmp_smix_stoptimer	(void);
int	xmp_smix_softmixer	(struct xmp_context *);
int	xmp_smix_writepatch	(struct xmp_context *, struct patch_info *);
int	xmp_smix_getmsg		(struct xmp_context *);
void    *xmp_smix_buffer	(struct xmp_context *);

#endif /* __MIXER_H */
