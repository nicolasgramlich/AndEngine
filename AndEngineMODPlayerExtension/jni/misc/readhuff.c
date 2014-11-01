/* nomarch 1.0 - extract old `.arc' archives.
 * Copyright (C) 2001 Russell Marks. See main.c for license details.
 *
 * readhuff.c - read RLE+Huffman-compressed files (the CP/M `SQ' scheme).
 */

/* I was originally going to adapt some old GPL'd Huffman code,
 * but it turns out the format pretty much forces an array-based
 * implementation. Not that I mind that :-), it just makes it
 * a bit unusual. So this is from scratch (though it wasn't too hard).
 */

#include <stdio.h>
#include <stdlib.h>
#include "readrle.h"

#include "readhuff.h"


struct huff_node_tag
  {
  /* data is stored as a negative `pointer' */
  int kids[2];
  };

#define READ_WORD(x) (x)=rawinput(),(x)|=(rawinput()<<8)
#define VALUE_CONV(x) ((x)^0xffff)

#define HUFF_EOF	256


static int bitbox,bitsleft;

static unsigned char *data_in_point,*data_in_max;
static unsigned char *data_out_point,*data_out_max;


static int rawinput(void)
{
if(data_in_point<data_in_max)
  return(*data_in_point++);
return(-1);
}

static void rawoutput(int byte)
{
if(data_out_point<data_out_max)
  *data_out_point++=byte;
}


static void bit_init(void)
{
bitbox=0; bitsleft=0;
}

static int bit_input(void)
{
if(bitsleft==0)
  {
  bitbox=rawinput();
  if(bitbox==-1) return(-1);
  bitsleft=8;
  }

bitsleft--;
return((bitbox&(1<<(7-bitsleft)))?1:0);
}


unsigned char *convert_huff(unsigned char *data_in,
                            unsigned long in_len,
                            unsigned long orig_len)
{
unsigned char *data_out;
struct huff_node_tag *nodearr;
int nodes,f,b;

if((data_out=malloc(orig_len))==NULL)
  fprintf(stderr,"nomarch: out of memory!\n"),exit(1);

data_in_point=data_in; data_in_max=data_in+in_len;
data_out_point=data_out; data_out_max=data_out+orig_len;

READ_WORD(nodes);

if(!nodes)
  {
  free(data_out);
  return(NULL);
  }

if((nodearr=malloc(sizeof(struct huff_node_tag)*nodes))==NULL)
  fprintf(stderr,"nomarch: out of memory!\n"),exit(1);

/* apparently the tree can be empty (zero-length file?), so
 * there's a preset entry which is required. In the context of
 * .arc I'm sure this is cruft which we don't actually need,
 * but just in case...
 */
nodearr[0].kids[0]=nodearr[0].kids[1]=VALUE_CONV(HUFF_EOF);

for(f=0;f<nodes;f++)
  {
  READ_WORD(nodearr[f].kids[0]);
  READ_WORD(nodearr[f].kids[1]);
  }

/* after the table, we get the codes to interpret; this is
 * a bitstream, with EOF marked by the code HUFF_EOF.
 */
bit_init();
outputrle(-1,NULL);

do
  {
  f=0;
  while((f&0x8000)==0)
    {
    if(f>=nodes)
      {
      /* must be corrupt */
      free(nodearr);
      free(data_out);
      return(NULL);
      }

    /* it seems we can't rely on getting the EOF code (even though we
     * do >95% of the time!), so check for `real' EOF too.
     * (worth checking in case of corrupt file too, I guess.)
     */
    if((b=bit_input())==-1)
      {
      f=VALUE_CONV(HUFF_EOF);
      break;
      }
    
    f=nodearr[f].kids[b];
    }
  
  f=VALUE_CONV(f);
  if(f!=HUFF_EOF)
    outputrle(f,rawoutput);
  }
while(f!=HUFF_EOF);

free(nodearr);

return(data_out);
}
