/*

  portable code to decrunch XPK-SQSH files
  written from "xpksqsh.library 1.10 (6.3.94)"

  (24.12.97) jah@fh-zwickau.de
  
  tested with sas6.51(m68k) gcc2.7.2(i386) gcc2.7.2(sparc)

*/

/*

  modified by Claudio Matsuoka for use with xmp
  checksum added by Sipos Attila <h430827@stud.u-szeged.hu>

*/

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "common.h"

#define XPKERR_CHKSUM 1
#define XPKERR_UNKNOWNTYPE 2

static volatile int xpkerrno;

static int unpack(uint8 *src, uint8 *dst, int len);

static int unsqsh(FILE *in, FILE *out)
{
  unsigned char *src;
  unsigned char *dst;
  int srclen;
  int dstlen;

  if ('X' != fgetc (in) ||
      'P' != fgetc (in) ||
      'K' != fgetc (in) ||
      'F' != fgetc (in)) {
    return -1;
  }

  srclen  = fgetc (in); srclen <<= 8;
  srclen |= fgetc (in); srclen <<= 8;
  srclen |= fgetc (in); srclen <<= 8;
  srclen |= fgetc (in);

  if ('S' != fgetc (in) ||
      'Q' != fgetc (in) ||
      'S' != fgetc (in) ||
      'H' != fgetc (in)) {
    return -1;
  }

  dstlen  = fgetc (in); dstlen <<= 8;
  dstlen |= fgetc (in); dstlen <<= 8;
  dstlen |= fgetc (in); dstlen <<= 8;
  dstlen |= fgetc (in);
 
  src=(unsigned char*)malloc(srclen+3);
  dst=(unsigned char*)malloc(dstlen+100);
  if (src==NULL || dst==NULL)
    return -1;

  if (1 != fread(src,srclen-8,1,in))
    return -1;

  if (unpack(src,dst,dstlen)!=dstlen)
    return -1;

  if (1 != fwrite(dst,dstlen,1,out))
    return -1;
    
  free(src);
  free(dst);

  return 0;
}

static uint16 xchecksum(uint32* ptr, uint32 count)
{
  register uint32 sum=0;

  while(count-- > 0){
    sum ^= *ptr++;
   }

  return (uint16) (sum ^ (sum >> 16));
}

static int bfextu(uint8 *p,int bo,int bc)
{
  int r;
  
  p += bo / 8;
  r = *(p++);
  r <<= 8;
  r |= *(p++);
  r <<= 8;
  r |= *p;
  r <<= bo % 8;
  r &= 0xffffff;
  r >>= 24 - bc;

  return r;
}

static int bfexts(uint8 *p,int bo,int bc)
{
  int r;
  
  p += bo / 8;
  r = *(p++);
  r <<= 8;
  r |= *(p++);
  r <<= 8;
  r |= *p;
  r <<= (bo % 8) + 8;
  r >>= 32 - bc;

  return r;
}

/*
;    0	LONG	"XPKF"
;    4	LONG	file size - 8
;    8	LONG	"SQSH"
;   12	LONG	unpacked size
;  $10	QUAD	original file header
;  $20	WORD
;  $22	WORD
;  $24
;
;    0	WORD	ratio?
;    2	WORD	?
;    4	WORD	packed chunk size starting from offset 6, long aligned
;    6	WORD	unpacked chunk size
;    8	WORD	unpacked chunk size
;   10	LABEL	bitstream...
*/

static int unpack(uint8 *src, uint8 *dst, int len) {
  int decrunched=0;
  /*uint8 *osrc=src-16;*/
  int type, hchk;
  int d0,d1,d2,d3,d4,d5,d6,a2,a5;
  int u, cp, cup1;
  int lchk;
  uint8 *a4,*a6,*c;
  uint8  bc1, bc2, bc3;
  uint8 a3[] = { 2,3,4,5,6,7,8,0,3,2,4,5,6,7,8,0,4,3,5,2,6,7,8,0,5,4,
        6,2,3,7,8,0,6,5,7,2,3,4,8,0,7,6,8,2,3,4,5,0,8,7,6,2,3,4,5,0 };
  
   
  xpkerrno=0;
  c = src + 20;
  
  while (len) {
    type = *c++; hchk = *c++;		/* type+hchk */
#if 0
    u = (*c++); u |= (*c++)<<8;		/* checksum */
#endif
    u = *(uint16*)c; c+=2;
    cp = *c++; cp <<= 8; cp |= *c++;		/* packed */
    cup1 = *c++; cup1 <<= 8; cup1 |= *c++;	/* unpacked */

    src = c+2;
    bc1=c[cp+2];bc2=c[cp+1];bc3=c[cp];
    c[cp+2]=0;c[cp+1]=0;c[cp]=0;
	lchk=xchecksum((uint32*)(c),(cp+3)>>2);
    c[cp+2]=bc1;c[cp+1]=bc2;c[cp]=bc3;
	/*fprintf(stderr,"c=%x type=%02x chk=%04x cp=%04x cup1=%04x chk=%04x\n",c-osrc-8,type,u,cp,cup1,lchk);*/
	if (lchk != u) {
		xpkerrno=XPKERR_CHKSUM;
		return(decrunched);		
	}

	if (type == 0 ) {
	  /* RAW chunk */
	  memcpy(dst,c,cp);
	  dst+=cp;
	  c+=cp;
	  len -= cp;	  	
	  decrunched+=cp;
	  continue;
	}
	  
	if (type != 1) {
	  xpkerrno=XPKERR_UNKNOWNTYPE;
	  return(decrunched);
	}

    len -= cup1;
	decrunched+=cup1;;
    
    cp = (cp + 3) & 0xfffc;
    c += cp;

	a6 = dst + cup1;
	d0 = d1 = d2 = d3 = a2 = 0;
  
	d3 = *(src++);
	*(dst++) = d3;
l6c6:	if (d1 >= 8) goto l6dc;
	if (bfextu(src,d0,1)) goto l75a;
	d0 += 1;
	d5 = 0;
	d6 = 8;
	goto l734;
  
l6dc:	if (bfextu(src,d0,1)) goto l726;
	d0 += 1;
	if (! bfextu(src,d0,1)) goto l75a;
	d0 += 1;
	if (bfextu(src,d0,1)) goto l6f6;
	d6 = 2;
	goto l708;

l6f6:	d0 += 1;
	if (! bfextu(src,d0,1)) goto l706;
	d6 = bfextu(src,d0,3);
	d0 += 3;
	goto l70a;
  
l706:	d6 = 3;
l708:	d0 += 1;
l70a:	d6 = *(a3 + (8*a2) + d6 -17);
	if (d6 != 8) goto l730;
l718:	if (d2 < 20) goto l722;
	d5 = 1;
	goto l732;
  
l722:	d5 = 0;
	goto l734;

l726:	d0 += 1;
	d6 = 8;
	if (d6 == a2) goto l718;
	d6 = a2;
l730:	d5 = 4;
l732:	d2 += 8;
l734:	d4 = bfexts(src,d0,d6);
	d0 += d6;
	d3 -= d4;
	*dst++ = d3;
	d5--;
	if (d5 != -1) goto l734;
	if (d1 == 31) goto l74a;
	d1 += 1;
l74a:	a2 = d6;
l74c:	d6 = d2;
	d6 >>= 3;
	d2 -= d6;
	if (dst < a6) goto l6c6;
    /*
    if (dst != a6) {
      fprintf(stderr,"fatal dst=%p a6=%p\n",dst,a6);
      exit(1);
    }
    */
    dst = a6;
  }
  return(decrunched);

l75a:	d0 += 1;
	if (bfextu(src,d0,1)) goto l766;
	d4 = 2;
	goto l79e;
  
l766:	d0 += 1;
	if (bfextu(src,d0,1)) goto l772;
	d4 = 4;
	goto l79e;

l772:	d0 += 1;
	if (bfextu(src,d0,1)) goto l77e;
	d4 = 6;
	goto l79e;

l77e:	d0 += 1;
	if (bfextu(src,d0,1)) goto l792;
	d0 += 1;
	d6 = bfextu(src,d0,3);
	d0 += 3;
	d6 += 8;
	goto l7a8;
  
l792:	d0 += 1;
	d6 = bfextu(src,d0,5);
	d0 += 5;
	d4 = 16;
	goto l7a6;
  
l79e:	d0 += 1;
	d6 = bfextu(src,d0,1);
	d0 += 1;
l7a6:	d6 += d4;
l7a8:	if (bfextu(src,d0,1)) goto l7c4;
	d0 += 1;
	if (bfextu(src,d0,1)) goto l7bc;
	d5 = 8;
	a5 = 0;
	goto l7ca;

l7bc:	d5 = 14;
	a5 = -0x1100;
	goto l7ca;

l7c4:	d5 = 12;
	a5 = -0x100;
l7ca:	d0 += 1;
	d4 = bfextu(src,d0,d5);
	d0 += d5;
	d6 -= 3;
	if (d6 < 0) goto l7e0;
	if (d6 == 0) goto l7da;
	d1 -= 1;
l7da:	d1 -= 1;
	if (d1 >= 0) goto l7e0;
	d1 = 0;
l7e0:	d6 += 2;
	a4 = -1 + dst + a5 - d4;
l7ex:	*dst++ = *a4++;
	d6--;
	if (d6 != -1) goto l7ex;
	d3 = *(--a4);
	goto l74c;
}

/*

Unpack	move.l	a6,-(sp)
	movea.l	a0,a6
	movea.l	(a6),a0
	movea.l	(8,a6),a1
	moveq	#0,d7
	move.w	(a0)+,d7
	move.l	d7,($10,a6)
	bsr.b	lbc0006b0
	moveq	#0,d0
	movea.l	(sp)+,a6
	rts

lbc0006b0	movea.l	a1,a6
	adda.l	d7,a6
	moveq	#0,d0
	moveq	#0,d1
	moveq	#0,d2
	moveq	#0,d3
	suba.w	a2,a2
	lea	(data_a3,pc),a3
	move.b	(a0)+,d3
	move.b	d3,(a1)+
lbc0006c6	cmp.w	#8,d1
	bcc.b	lbc0006dc
	bftst	(a0){d0:1}
	bne.w	lbc00075a
	addq.l	#1,d0
	moveq	#0,d5
	moveq	#8,d6
	bra.b	lbc000734

lbc0006dc	bftst	(a0){d0:1}
	bne.b	lbc000726
	addq.l	#1,d0
	bftst	(a0){d0:1}
	beq.b	lbc00075a
	addq.l	#1,d0
	bftst	(a0){d0:1}
	bne.b	lbc0006f6
	moveq	#2,d6
	bra.b	lbc000708

lbc0006f6	addq.l	#1,d0
	bftst	(a0){d0:1}
	beq.b	lbc000706
	bfextu	(a0){d0:3},d6
	addq.l	#3,d0
	bra.b	lbc00070a

lbc000706	moveq	#3,d6
lbc000708	addq.l	#1,d0
lbc00070a	lea	(-$10,a3,a2.w*8),a4
	move.b	(-1,a4,d6.w),d6
	cmp.w	#8,d6
	bne.b	lbc000730
lbc000718	cmp.w	#$14,d2
	bcs.b	lbc000722
	moveq	#1,d5
	bra.b	lbc000732

lbc000722	moveq	#0,d5
	bra.b	lbc000734

lbc000726	addq.l	#1,d0
	moveq	#8,d6
	cmpa.w	d6,a2
	beq.b	lbc000718
	move.l	a2,d6
lbc000730	moveq	#4,d5
lbc000732	addq.w	#8,d2
lbc000734	bfexts	(a0){d0:d6},d4
	add.l	d6,d0
	sub.b	d4,d3
	move.b	d3,(a1)+
	dbra	d5,lbc000734
	cmp.w	#$1f,d1
	beq.b	lbc00074a
	addq.w	#1,d1
lbc00074a	movea.w	d6,a2
lbc00074c	move.w	d2,d6
	lsr.w	#3,d6
	sub.w	d6,d2
	cmpa.l	a6,a1
	blt.w	lbc0006c6
	rts

lbc00075a	addq.l	#1,d0
	bftst	(a0){d0:1}
	bne.b	lbc000766
	moveq	#2,d4
	bra.b	lbc00079e

lbc000766	addq.l	#1,d0
	bftst	(a0){d0:1}
	bne.b	lbc000772
	moveq	#4,d4
	bra.b	lbc00079e

lbc000772	addq.l	#1,d0
	bftst	(a0){d0:1}
	bne.b	lbc00077e
	moveq	#6,d4
	bra.b	lbc00079e

lbc00077e	addq.l	#1,d0
	bftst	(a0){d0:1}
	bne.b	lbc000792
	addq.l	#1,d0
	bfextu	(a0){d0:3},d6
	addq.l	#3,d0
	addq.l	#8,d6
	bra.b	lbc0007a8

lbc000792	addq.l	#1,d0
	bfextu	(a0){d0:5},d6
	addq.l	#5,d0
	moveq	#$10,d4
	bra.b	lbc0007a6

lbc00079e	addq.l	#1,d0
	bfextu	(a0){d0:1},d6
	addq.l	#1,d0
lbc0007a6	add.w	d4,d6
lbc0007a8	bftst	(a0){d0:1}
	bne.b	lbc0007c4
	addq.l	#1,d0
	bftst	(a0){d0:1}
	bne.b	lbc0007bc
	moveq	#8,d5
	suba.w	a5,a5
	bra.b	lbc0007ca

lbc0007bc	moveq	#14,d5
	movea.w	#$ef00,a5
	bra.b	lbc0007ca

lbc0007c4	moveq	#12,d5
	movea.w	#$ff00,a5
lbc0007ca	addq.l	#1,d0
	bfextu	(a0){d0:d5},d4
	add.l	d5,d0
	subq.w	#3,d6
	bcs.b	lbc0007e0
	beq.b	lbc0007da
	subq.w	#1,d1
lbc0007da	subq.w	#1,d1
	bge.b	lbc0007e0
	moveq	#0,d1
lbc0007e0	addq.w	#2,d6
	lea	(-1,a1,a5.w),a4
	suba.w	d4,a4
	lsr.w	#1,d6
	bcc.b	lbc0007ee
lbc0007ec	move.b	(a4)+,(a1)+
lbc0007ee	move.b	(a4)+,(a1)+
	dbra	d6,lbc0007ec
	move.b	-(a4),d3
	bra.w	lbc00074c

data_a3	dc.l	$2030405
	dc.l	$6070800
	dc.l	$3020405
	dc.l	$6070800
	dc.l	$4030502
	dc.l	$6070800
	dc.l	$5040602
	dc.l	$3070800
	dc.l	$6050702
	dc.l	$3040800
	dc.l	$7060802
	dc.l	$3040500
	dc.l	$8070602
	dc.l	$3040500
	dc.b	0
	dc.b	0

*/


int decrunch_sqsh (FILE *f, FILE *fo)
{
    if (fo == NULL)
        return -1;

    if (unsqsh (f, fo) < 0)
	return -1;

    return 0;
}

