/* nomarch 1.0 - extract old `.arc' archives.
 * Copyright (C) 2001 Russell Marks. See main.c for license details.
 *
 * readrle.c - read RLE-compressed files.
 *
 * Also provides the generic outputrle() for the other RLE-using methods
 * to use.
 */

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>

#include "readrle.h"


static unsigned char *data_in_point,*data_in_max;
static unsigned char *data_out_point,*data_out_max;


static void rawoutput(int byte)
{
if(data_out_point<data_out_max)
  *data_out_point++=byte;
}


/* call with -1 before starting, to make sure state is initialised */
void outputrle(int chr,void (*outputfunc)(int))
{
static int lastchr=0,repeating=0;
int f;

if(chr==-1)
  {
  lastchr=repeating=0;
  return;
  }

if(repeating)
  {
  if(chr==0)
    (*outputfunc)(0x90);
  else
    for(f=1;f<chr;f++)
      (*outputfunc)(lastchr);
  repeating=0;
  }
else
  {
  if(chr==0x90)
    repeating=1;
  else
    {
    (*outputfunc)(chr);
    lastchr=chr;
    }
  }
}


unsigned char *convert_rle(unsigned char *data_in,
                           unsigned long in_len,
                           unsigned long orig_len)
{
unsigned char *data_out;

if((data_out=malloc(orig_len))==NULL)
  fprintf(stderr,"nomarch: out of memory!\n"),exit(1);

data_in_point=data_in; data_in_max=data_in+in_len;
data_out_point=data_out; data_out_max=data_out+orig_len;
outputrle(-1,NULL);

while(data_in_point<data_in_max)
  outputrle(*data_in_point++,rawoutput);

return(data_out);
}
