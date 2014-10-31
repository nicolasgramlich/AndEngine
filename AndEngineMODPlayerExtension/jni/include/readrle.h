/* nomarch 1.0 - extract old `.arc' archives.
 * Copyright (C) 2001 Russell Marks. See main.c for license details.
 *
 * readrle.h
 */

extern void outputrle(int chr,void (*outputfunc)(int));
extern unsigned char *convert_rle(unsigned char *data_in,
                                  unsigned long in_len,
                                  unsigned long orig_len);
