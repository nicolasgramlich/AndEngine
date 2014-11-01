/* nomarch 1.3 - extract old `.arc' archives.
 * Copyright (C) 2001,2002 Russell Marks. See main.c for license details.
 *
 * Modified for xmp by Claudio Matsuoka, Aug 2007
 *
 * readlzw.h
 */

#define ALIGN4(x) (((x) + 3) & ~3L)

/* Digital Symphony LZW quirk */
#define XMP_LZW_QUIRK_DSYM	(NOMARCH_QUIRK_END101|NOMARCH_QUIRK_NOCHK| \
				 NOMARCH_QUIRK_NOSYNC|NOMARCH_QUIRK_START101| \
				 NOMARCH_QUIRK_ALIGN4)

#define NOMARCH_QUIRK_END101	(1L << 0)	/* code 0x101 is end mark */
#define NOMARCH_QUIRK_NOCHK	(1L << 1)	/* don't check input size */
#define NOMARCH_QUIRK_NOSYNC	(1L << 2)	/* don't resync */
#define NOMARCH_QUIRK_START101	(1L << 3)	/* start at 0x101 not 0x100 */
#define NOMARCH_QUIRK_ALIGN4	(1L << 4)	/* input buffer size aligned */
#define NOMARCH_QUIRK_SKIPMAX	(1L << 5)	/* skip max code size */

unsigned char *convert_lzw_dynamic(unsigned char *data_in,
                                          int bits,int use_rle,
                                          unsigned long in_len,
                                          unsigned long orig_len,
					  int q);

uint8 *read_lzw_dynamic(FILE *f, uint8 *buf, int max_bits,int use_rle,
                        unsigned long in_len, unsigned long orig_len, int q);

