
#ifndef __XMP_CONVERT_H
#define __XMP_CONVERT_H

#include "driver.h"

void xmp_cvt_hsc2sbi (char *);
void xmp_cvt_diff2abs (int, int, char *);
void xmp_cvt_stdownmix (int, int, char *);
void xmp_cvt_sig2uns (int, int, char *);
void xmp_cvt_sex (int, char *);
void xmp_cvt_2xsmp (int, char *);
void xmp_cvt_vidc (int, char *);
void xmp_cvt_to8bit (struct xmp_context *);
void xmp_cvt_to16bit (struct xmp_context *);
void xmp_cvt_bid2und (struct xmp_context *);

void xmp_cvt_anticlick (struct patch_info *);
int xmp_cvt_crunch (struct patch_info **, unsigned int);

#endif /* __XMP_CONVERT_H */
