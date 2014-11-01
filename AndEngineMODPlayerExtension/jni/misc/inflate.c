#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//#include "fileio.h"
//#include "kinflate.h"
//#include "zipfile.h"

#include "common.h"

#define read_int_b(x) read32b(x)
#define read_word(x) read16l(x)

/*

This code is Copyright 2005-2006 by Michael Kohn

Modified for xmp by Claudio Matsuoka, Jul 2009

This package is licensed under the LGPL. You are free to use this library
in both commercial and non-commercial applications as long as you dynamically
link to it. If you statically link this library you must also release your
software under the LGPL. If you need more flexibility in the license email
me and we can work something out. 

Michael Kohn <mike@mikekohn.net>

*/

#define WINDOW_SIZE 32768

struct bitstream_t
{
  unsigned int holding;
  int bitptr;
};

struct huffman_t
{
  unsigned char window[WINDOW_SIZE];
  int window_ptr;
  unsigned int checksum;
  int len[288];
  int dist_len[33];
  int dist_huff_count;
};

struct huffman_tree_t
{
  unsigned short int code;
  short int left;
  short int right;
};

static int length_codes[29] = { 3,4,5,6,7,8,9,10,11,13,15,17,19,
                         23,27,31,35,43,51,59,67,83,99,115,
                         131,163,195,227,258 };

static int length_extra_bits[29] = { 0,0,0,0,0,0,0,0,1,1,1,1,
                              2,2,2,2,3,3,3,3,4,4,4,4,
                              5,5,5,5,0 };

static int dist_codes[30] = { 1,2,3,4,5,7,9,13,17,25,
                             33,49,65,97,129,193,257,385,513,769,
                             1025,1537,2049,3073,4097,6145,8193,
                             12289,16385,24577 };

static int dist_extra_bits[30] = { 0,0,0,0,1,1,2,2,3,3,
                            4,4,5,5,6,6,7,7,8,8,
                            9,9,10,10,11,11,12,12,13,13 };

static int dyn_huff_trans[19] = { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5,
                           11, 4, 12, 3, 13, 2, 14, 1, 15 };

static unsigned char reverse[256] = {
0, 128, 64, 192, 32, 160, 96, 224, 16, 144, 80, 208, 48, 176, 112, 240,
8, 136, 72, 200, 40, 168, 104, 232, 24, 152, 88, 216, 56, 184, 120, 248,
4, 132, 68, 196, 36, 164, 100, 228, 20, 148, 84, 212, 52, 180, 116, 244,
12, 140, 76, 204, 44, 172, 108, 236, 28, 156, 92, 220, 60, 188, 124, 252,
2, 130, 66, 194, 34, 162, 98, 226, 18, 146, 82, 210, 50, 178, 114, 242,
10, 138, 74, 202, 42, 170, 106, 234, 26, 154, 90, 218, 58, 186, 122, 250,
6, 134, 70, 198, 38, 166, 102, 230, 22, 150, 86, 214, 54, 182, 118, 246,
14, 142, 78, 206, 46, 174, 110, 238, 30, 158, 94, 222, 62, 190, 126, 254,
1, 129, 65, 193, 33, 161, 97, 225, 17, 145, 81, 209, 49, 177, 113, 241,         
9, 137, 73, 201, 41, 169, 105, 233, 25, 153, 89, 217, 57, 185, 121, 249,
5, 133, 69, 197, 37, 165, 101, 229, 21, 149, 85, 213, 53, 181, 117, 245,
13, 141, 77, 205, 45, 173, 109, 237, 29, 157, 93, 221, 61, 189, 125, 253,
3, 131, 67, 195, 35, 163, 99, 227, 19, 147, 83, 211, 51, 179, 115, 243,
11, 139, 75, 203, 43, 171, 107, 235, 27, 155, 91, 219, 59, 187, 123, 251,
7, 135, 71, 199, 39, 167, 103, 231, 23, 151, 87, 215, 55, 183, 119, 247,
15, 143, 79, 207, 47, 175, 111, 239, 31, 159, 95, 223, 63, 191, 127, 255 };

static struct huffman_tree_t *huffman_tree_len_static=0;
static int crc_built=0;
static unsigned int crc_table[256];

#ifdef DEBUG
int print_binary(int b, int l)
{
  if ((l-1)!=0)
  { print_binary(b>>1,l-1); }

  printf("%d",(b&1));

  return 0;
}
#endif

/* These CRC32 functions were taken from the gzip spec and kohninized */

static int build_crc32()
{
unsigned int c;
int n,k;

  for (n=0; n<256; n++)
  {
    c=(unsigned int)n;
    for (k=0; k<8; k++)
    {
      if (c&1)
      { c=0xedb88320^(c>>1); }
        else
      { c=c>>1; }
    }
    crc_table[n]=c;
  }

  crc_built=1;

  return 0;
}

static unsigned int crc32(unsigned char *buffer, int len, unsigned int crc)
{
int t;

  for (t=0; t<len; t++)
  {
    crc=crc_table[(crc^buffer[t])&0xff]^(crc>>8);
  }

  return crc;
}

int kunzip_inflate_init()
{
/*
int t,r,b,rev_code;

  for (t=0; t<256; t++)
  {
    b=128;
    rev_code=0;
    for (r=0; r<8; r++)
    {
      rev_code+=(((t&(1<<r))>>r)*b);
      b=b>>1;
    }

    reverse[t]=rev_code;
  }
*/
  if (crc_built==0) build_crc32();

  return 0;
}

int kunzip_inflate_free()
{
  if (huffman_tree_len_static!=0)
  { free(huffman_tree_len_static); }

  return 0;
}

#if 0
static unsigned int get_alder(FILE *out)
{
unsigned int s1,s2;
unsigned int adler;
int len,t,value;

  len=ftell(out);
  fseek(out,0,SEEK_SET);

  adler=1;

  s1=adler&0xffff;
  s2=(adler>>16);

  for (t=0; t<len; t++)
  {
    value=getc(out);

    s1=(s1+value)%65521;
    s2=(s2+s1)%65521;
  }
  adler=(s2<<16)+s1;

  fseek(out,len,SEEK_SET);

  return adler;
}
#endif

static int reverse_bitstream(struct bitstream_t *bitstream)
{
unsigned int i;

  i=reverse[((bitstream->holding>>24)&255)]|
    (reverse[((bitstream->holding>>16)&255)]<<8)|
    (reverse[((bitstream->holding>>8)&255)]<<16)|
    (reverse[(bitstream->holding&255)]<<24);

  i=i>>(32-bitstream->bitptr);
  bitstream->holding=i;

  return 0;
}


static int add_static_codes_to_tree(struct huffman_tree_t *huffman_tree, int code_len, int count, int start_code, int start_uncomp_code, int next_leaf)
{
struct huffman_tree_t *curr_huffman_leaf;
int t,x,r;

  // code_len=code_len-1;

  for (t=0; t<count; t++)
  {
    curr_huffman_leaf=huffman_tree;
    x=1<<(code_len-1);
    for (r=0; r<code_len; r++)
    {
      if ((start_code&x)==0)
      {
        if (curr_huffman_leaf->left==0)
        {
          next_leaf++;
          curr_huffman_leaf->left=next_leaf;
          huffman_tree[next_leaf].left=0;
          huffman_tree[next_leaf].right=0;
        }

        curr_huffman_leaf=&huffman_tree[curr_huffman_leaf->left];
      }
        else
      {
        if (curr_huffman_leaf->right==0)
        {
          next_leaf++;
          curr_huffman_leaf->right=next_leaf;
          huffman_tree[next_leaf].left=0;
          huffman_tree[next_leaf].right=0;
        }

        curr_huffman_leaf=&huffman_tree[curr_huffman_leaf->right];
      }

      x=x>>1;
    } 

    curr_huffman_leaf->code=start_uncomp_code++;
    start_code++;
  }


  return next_leaf;
}

static int load_fixed_huffman(struct huffman_t *huffman, struct huffman_tree_t **huffman_tree_ptr)
{
struct huffman_tree_t *huffman_tree;
int next_leaf;
/*
int t;
int code;
*/

#ifdef DEBUG
printf("load_fixed_huffman()\n");
#endif

/*
  code=0x30;   // 0011 0000 
  for (t=0; t<=143; t++)
  {
    huffman->len[t]=8;
    huffman->code[t]=code;
    code++;
  }

  code=0x190;  // 1 1001 0000 
  for (t=144; t<=255; t++)
  {
    huffman->len[t]=9;
    huffman->code[t]=code;
    code++;
  }

  code=0x00;   // 0000000
  for (t=256; t<=279; t++)
  {
    huffman->len[t]=7;
    huffman->code[t]=code;
    code++;
  }

  code=0xc0;   // 1100 0000
  for (t=280; t<=287; t++)
  {
    huffman->len[t]=8;
    huffman->code[t]=code;
    code++;
  }

*/

  huffman->dist_huff_count=0;

  huffman_tree=malloc(600*sizeof(struct huffman_tree_t));

  *huffman_tree_ptr=huffman_tree;

  huffman_tree->left=0;
  huffman_tree->right=0;

/* printf("adding static\n"); */
  next_leaf=0;
  next_leaf=add_static_codes_to_tree(huffman_tree,8,144,0x30,0,next_leaf);
  next_leaf=add_static_codes_to_tree(huffman_tree,9,112,0x190,144,next_leaf);
  next_leaf=add_static_codes_to_tree(huffman_tree,7,24,0x00,256,next_leaf);
  next_leaf=add_static_codes_to_tree(huffman_tree,8,8,0xc0,280,next_leaf);
/* printf("next_leaf=%d\n",next_leaf); */

  return 0;
}

static int load_codes(FILE *in, struct bitstream_t *bitstream, int *lengths, int count, int *hclen_code_length, int *hclen_code, struct huffman_tree_t *huffman_tree)
{
int r,t,c,x;
int code,curr_code;
int bl_count[512];
int next_code[512];
int bits,max_bits;
int next_leaf,curr_leaf;

#ifdef DEBUG
printf("Entering load_codes()\n");
#endif


  r=0;
  while (r<count)
  {
    for (t=0; t<19; t++)
    {
      if (hclen_code_length[t]==0) continue;
      while (bitstream->bitptr<hclen_code_length[t])
      {
        bitstream->holding=reverse[getc(in)]+(bitstream->holding<<8);
        bitstream->bitptr+=8;
      }

      curr_code=(bitstream->holding>>(bitstream->bitptr-hclen_code_length[t]));

#ifdef DEBUG
/*
print_binary(curr_code,hclen_code_length[t]);
printf(" ");
print_binary(hclen_code[t],hclen_code_length[t]);
printf("\n");
*/
#endif

      if (curr_code==hclen_code[t])
      {
        bitstream->bitptr-=hclen_code_length[t];
        bitstream->holding=bitstream->holding&((1<<bitstream->bitptr)-1);
        break;
      }
    }

    if (t<=15)
    {
      lengths[r++]=t;
    }
      else
    if (t==16)
    {
      if (r!=0)
      { code=lengths[r-1]; }
        else
      { code=0; }

      if (bitstream->bitptr<2)
      {
        bitstream->holding=reverse[getc(in)]+(bitstream->holding<<8);
        bitstream->bitptr+=8;
      }

      x=reverse[bitstream->holding>>(bitstream->bitptr-2)]>>6;
      bitstream->bitptr-=2;
      bitstream->holding=bitstream->holding&((1<<bitstream->bitptr)-1);

      for (c=0; c<x+3; c++)
      { lengths[r++]=code; }
    }
      else
    if (t==17)
    {
      if (bitstream->bitptr<3)
      {
        bitstream->holding=reverse[getc(in)]+(bitstream->holding<<8);
        bitstream->bitptr+=8;
      }

      x=reverse[bitstream->holding>>(bitstream->bitptr-3)]>>5;
      bitstream->bitptr-=3;
      bitstream->holding=bitstream->holding&((1<<bitstream->bitptr)-1);

      c=x+3;
      memset(&lengths[r],0,sizeof(int)*c);
      r=r+c;
    }
      else
    if (t==18)
    {
      if (bitstream->bitptr<7)
      {
        bitstream->holding=reverse[getc(in)]+(bitstream->holding<<8);
        bitstream->bitptr+=8;
      }

      x=reverse[bitstream->holding>>(bitstream->bitptr-7)]>>1;
      bitstream->bitptr-=7;
      bitstream->holding=bitstream->holding&((1<<bitstream->bitptr)-1);

      c=x+11;
      memset(&lengths[r],0,sizeof(int)*c);
      r=r+c;

    }
      else
    {
      printf("Error in bitstream reading in literal code length %d\n",t);
      exit(0);
    }
  }

#ifdef DEBUG
printf("r=%d count=%d\n",r,count);
#endif

  /* time to load the codes */

  memset(bl_count,0,count*sizeof(int));
  /* memset(next_code,0,count*sizeof(int)); */

  max_bits=0;
  for (t=0; t<count; t++)
  {
    bl_count[lengths[t]]++;
    if (max_bits<lengths[t]) max_bits=lengths[t];
  }

  code=0;
  bl_count[0]=0;
  for (bits=1; bits<=max_bits; bits++)
  {
    code=(code+bl_count[bits-1])<<1;
    next_code[bits]=code;
  }

  huffman_tree->left=0;
  huffman_tree->right=0;
  next_leaf=0;

  for (t=0; t<count; t++)
  {
    if (lengths[t]!=0)
    {
#ifdef DEBUG
printf(">> %d %d %d    next_leaf=%d\n",t,lengths[t],codes[t],next_leaf);
#endif

      code=next_code[lengths[t]];

      curr_leaf=0;

      x=1<<(lengths[t]-1);
      for (r=0; r<lengths[t]; r++)
      {
        if ((code&x)==0)
        {
          if (huffman_tree[curr_leaf].left==0)
          {
            next_leaf++;
            huffman_tree[curr_leaf].left=next_leaf;
            huffman_tree[next_leaf].left=0;
            huffman_tree[next_leaf].right=0;
          }

          curr_leaf=huffman_tree[curr_leaf].left;
        }
          else
        {
          if (huffman_tree[curr_leaf].right==0)
          {
            next_leaf++;
            huffman_tree[curr_leaf].right=next_leaf;
            huffman_tree[next_leaf].left=0;
            huffman_tree[next_leaf].right=0;
          }

          curr_leaf=huffman_tree[curr_leaf].right;
        }

        x=x>>1;
      } 

      huffman_tree[curr_leaf].code=t;

      next_code[lengths[t]]++;
    }
  }

#ifdef DEBUG
printf("Leaving load_codes()\n");
#endif

  return 0;
}

static int load_dynamic_huffman(FILE *in, struct huffman_t *huffman, struct bitstream_t *bitstream, struct huffman_tree_t *huffman_tree_len, struct huffman_tree_t *huffman_tree_dist)
{
int hlit,hdist,hclen;
int hclen_code_lengths[19];
int hclen_code[19];
int bl_count[19];
int next_code[19];
int code,bits;
int t;


  while (bitstream->bitptr<14)
  {
    bitstream->holding=reverse[getc(in)]+(bitstream->holding<<8);
    bitstream->bitptr+=8;
  }

  hlit=(bitstream->holding>>(bitstream->bitptr-5));
  bitstream->bitptr-=5;
  bitstream->holding=bitstream->holding&((1<<bitstream->bitptr)-1);

  hdist=(bitstream->holding>>(bitstream->bitptr-5));
  bitstream->bitptr-=5;
  bitstream->holding=bitstream->holding&((1<<bitstream->bitptr)-1);

  hclen=(bitstream->holding>>(bitstream->bitptr-4));
  bitstream->bitptr-=4;
  bitstream->holding=bitstream->holding&((1<<bitstream->bitptr)-1);

  hlit=(reverse[hlit]>>3)+257;
  hdist=(reverse[hdist]>>3)+1;
  hclen=(reverse[hclen]>>4)+4;

/* printf("%d %d %d\n",hclen,sizeof(struct huffman_tree_t),hclen*sizeof(struct huffman_tree_t)); */

#ifdef DEBUG
printf("hlit: %d\n",hlit);
printf("hdist: %d\n",hdist);
printf("hclen: %d\n",hclen);
#endif

  memset(hclen_code_lengths,0,19*sizeof(int));
  memset(hclen_code,0,19*sizeof(int));
  memset(bl_count,0,19*sizeof(int));
  /* memset(next_code,0,19*sizeof(int)); */

  /* load the first huffman table */

  for (t=0; t<hclen; t++)
  {
    if (bitstream->bitptr<3)
    {
      bitstream->holding=reverse[getc(in)]+(bitstream->holding<<8);
      bitstream->bitptr+=8;
    }

    hclen_code_lengths[dyn_huff_trans[t]]=(bitstream->holding>>(bitstream->bitptr-3));
    hclen_code_lengths[dyn_huff_trans[t]]=reverse[hclen_code_lengths[dyn_huff_trans[t]]]>>5;
    bitstream->bitptr-=3;
    bitstream->holding=bitstream->holding&((1<<bitstream->bitptr)-1);
  }

#ifdef DEBUG
  printf("\nCode Lengths\n");
  printf("------------------\n");
  for (t=0; t<19; t++)
  {
    printf("%d %d\n",t,hclen_code_lengths[t]);
  }
  printf("\n\n");
#endif

  for (t=0; t<19; t++)
  {
    bl_count[hclen_code_lengths[t]]++;
  }

#ifdef DEBUG
printf("\nbl_count[]\n");
printf("------------------\n");
for (t=0; t<8; t++)
{
  printf("%d  %d\n",t,bl_count[t]);
}
printf("\n");
#endif

  code=0;
  bl_count[0]=0;
  for (bits=1; bits<=7; bits++)
  {
    code=(code+bl_count[bits-1])<<1;
    next_code[bits]=code;
  }

#ifdef DEBUG
printf("next_code[]\n");
printf("------------------\n");
for (t=0; t<bits; t++)
{
  printf("%d  %d ",t,next_code[t]);
  if (t!=0) print_binary(next_code[t],t);
  printf("\n");
}
printf("\n");
#endif

  for (t=0; t<19; t++)
  {
    if (hclen_code_lengths[t]!=0)
    {
      hclen_code[t]=next_code[hclen_code_lengths[t]];
      next_code[hclen_code_lengths[t]]++;
    }
  }

#ifdef DEBUG
printf("Huffman1 Table\n");
printf("------------------\n");
for (t=0; t<19; t++)
{
  printf("%d  %d %d ",t,hclen_code[t],hclen_code_lengths[t]);
  if (hclen_code_lengths[t]!=0) print_binary(hclen_code[t],hclen_code_lengths[t]);
  printf("\n");
}
printf("\n");
#endif

  /* load literal tables */

  memset(huffman->len,0,288*sizeof(int));
  /* memset(huffman->code,0,288*sizeof(int)); */

  load_codes(in,bitstream,huffman->len,hlit,hclen_code_lengths,hclen_code,huffman_tree_len);

#ifdef DEBUG
printf("\nLiteral Table\n");
printf("------------------\n");
for (t=0; t<hlit; t++)
{
  if (huffman->len[t]!=0)
  {
    printf("%d  %d %d  ",t,huffman->code[t],huffman->len[t]);
    if (huffman->len[t]!=0) print_binary(huffman->code[t],huffman->len[t]);
    printf("\n");
  }
}
#endif

  /* load distant tables */

  if (hdist==0)
  {
    huffman->dist_huff_count=0;
  }
    else
  {
    huffman->dist_huff_count=hdist;

    memset(huffman->dist_len,0,33*sizeof(int));
    /* memset(huffman->dist_code,0,33*sizeof(int)); */

    load_codes(in,bitstream,huffman->dist_len,hdist,hclen_code_lengths,hclen_code,huffman_tree_dist);

  }

  return 0;
}

int decompress(FILE *in, struct huffman_t *huffman, struct bitstream_t *bitstream, struct huffman_tree_t *huffman_tree_len, struct huffman_tree_t *huffman_tree_dist, FILE *out)
{
int code=0,len,dist;
int t,r;
unsigned char *window;
struct huffman_tree_t *curr_huffman_leaf;
int window_ptr;
int curr_leaf;

#ifdef DEBUG
printf("decompress()\n");
printf("holding=%d bitptr=%d\n",bitstream->holding,bitstream->bitptr);
#endif

  /* printf("bitstream: %08x  %d\n",bitstream->holding,bitstream->bitptr); */
  reverse_bitstream(bitstream);
  /* printf("bitstream: %08x  %d\n",bitstream->holding,bitstream->bitptr); */

  window_ptr=huffman->window_ptr;
  window=huffman->window;

  while(1)
  {
    curr_huffman_leaf=huffman_tree_len;
    curr_leaf=0;

    while(1)
    {
      if (bitstream->bitptr<=0)
      {
        /* bitstream->holding+=(getc(in)<<bitstream->bitptr); */
        /* bitstream->bitptr+=8; */
        bitstream->holding=getc(in);
        bitstream->bitptr=8;
      }
#ifdef DEBUG
printf("%d  (%c)  %d %d   (holding=%d  bitptr=%d)\n",curr_leaf,huffman_tree_len[curr_leaf].code,huffman_tree_len[curr_leaf].left,huffman_tree_len[curr_leaf].right,bitstream->holding,bitstream->bitptr);
fflush(stdout);
#endif

      if ((bitstream->holding&1)==0)
      {
        if (huffman_tree_len[curr_leaf].left==0)
        {
          code=huffman_tree_len[curr_leaf].code;
          break;
        }
        curr_leaf=huffman_tree_len[curr_leaf].left;
      }
        else
      {
        if (huffman_tree_len[curr_leaf].right==0)
        {
          code=huffman_tree_len[curr_leaf].code;
          break;
        }
        curr_leaf=huffman_tree_len[curr_leaf].right;
      }

      bitstream->bitptr-=1;
      bitstream->holding>>=1;
    }

    /* if (t==288) { printf("Unknown huffman code\n"); return -1; } */

#ifdef DEBUG
printf("------------------------\n");
printf("code=%d\n",code);
#endif

    if (code<256)
    {
      /* putc(code,out); */
#ifdef DEBUG
if (code>=32 && code<=128)
{ printf("output %d %c\n",code,code); }
  else
{ printf("output %d\n",code); }
#endif
      window[window_ptr++]=code;
      if (window_ptr>=WINDOW_SIZE)
      {
        fwrite(window,1,WINDOW_SIZE,out);
        huffman->checksum=crc32(huffman->window,WINDOW_SIZE,huffman->checksum);
        window_ptr=0;
      }
    }
      else
    if (code==256)
    {
#ifdef DEBUG
printf("end-of-block %d\n",code);
#endif
      break;
    }
      else
    {
#ifdef DEBUG
printf("LZ77 TIME %d\n",code);
fflush(stdout);
#endif
      code=code-257;
      len=length_codes[code];
      if (length_extra_bits[code]!=0)
      {
        while (bitstream->bitptr<length_extra_bits[code])
        {
          bitstream->holding+=(getc(in)<<bitstream->bitptr);
          bitstream->bitptr+=8;
        }

#ifdef DEBUG
printf("len=%d  extra_bits=%d  extra bits value=%d\n",len,length_extra_bits[code],bitstream->holding>>(bitstream->bitptr-length_extra_bits[code]));
fflush(stdout);
#endif

        len=len+(bitstream->holding&((1<<length_extra_bits[code])-1));
        bitstream->bitptr-=length_extra_bits[code];
        bitstream->holding>>=length_extra_bits[code];
      }

      if (huffman->dist_huff_count==0)
      {
        if (bitstream->bitptr<5)
        {
          bitstream->holding+=(getc(in)<<bitstream->bitptr);
          bitstream->bitptr+=8;
        }
 
        code=(bitstream->holding&0x1f);
        code=reverse[code&255]>>3;
        bitstream->bitptr-=5;
        bitstream->holding>>=5;
#ifdef DEBUG
printf("DIST code=%d\n",code);
fflush(stdout);
#endif
      }
        else
      {
        curr_huffman_leaf=huffman_tree_len;
        curr_leaf=0;

        while(1)
        {
          if (bitstream->bitptr<=0)
          {
            /* bitstream->holding+=(getc(in)<<bitstream->bitptr); */
            /* bitstream->bitptr+=8; */
            bitstream->holding=getc(in);
            bitstream->bitptr=8;
          }
#ifdef DEBUG
printf("%d  (%c)  %d %d   (holding=%d  bitptr=%d)\n",curr_leaf,huffman_tree_len[curr_leaf].code,huffman_tree_len[curr_leaf].left,huffman_tree_len[curr_leaf].right,bitstream->holding,bitstream->bitptr);
fflush(stdout);
#endif

          if ((bitstream->holding&1)==0)
          {
            if (huffman_tree_dist[curr_leaf].left==0)
            {
              code=huffman_tree_dist[curr_leaf].code;
              break;
            }
            curr_leaf=huffman_tree_dist[curr_leaf].left;
          }
            else
          {
            if (huffman_tree_dist[curr_leaf].right==0)
            {
              code=huffman_tree_dist[curr_leaf].code;
              break;
            }
            curr_leaf=huffman_tree_dist[curr_leaf].right;
          }

          bitstream->bitptr-=1;
          bitstream->holding>>=1;
        }
      }

      dist=dist_codes[code];

      if (dist_extra_bits[code]!=0)
      {
        while (bitstream->bitptr<dist_extra_bits[code])
        {
          bitstream->holding+=(getc(in)<<bitstream->bitptr);
          bitstream->bitptr+=8;
        }

#ifdef DEBUG
printf("code=%d  distance=%d  num_extra_bits=%d extra_bits=%d\n",code,dist_codes[code],dist_extra_bits[code],(bitstream->holding>>(bitstream->bitptr-dist_extra_bits[code])
));
#endif

        dist=dist+(bitstream->holding&((1<<dist_extra_bits[code])-1));

        bitstream->bitptr-=dist_extra_bits[code];
        bitstream->holding>>=dist_extra_bits[code];
      }

#ifdef DEBUG
printf("len: %d dist: %d\n",len,dist);
#endif
/*
if (dist<0 || len>dist)
{
printf(">> OOPS! dist=%d  len=%d  (%d) ftell=%d\n",dist,len,huffman->dist_huff_count,ftell(out));
exit(0);

}
*/


/* HERE */

      r=window_ptr-dist;

/* I would have thought the memcpy (which gets called most of the time)
   would have been a huge speed up.  I was wrong.  Maybe I'll try writing
   some inline assembly later for x86 for this */

      if (r>=0 && (window_ptr+len<WINDOW_SIZE && r+len<window_ptr))
      /* if (r>=0 && (window_ptr+len<WINDOW_SIZE)) */
      {
        memcpy(window+window_ptr,window+r,len);
        window_ptr=window_ptr+len;
      }
        else
      {
        if (r<0) r=r+WINDOW_SIZE;

        for (t=0; t<len; t++)
        {
          window[window_ptr++]=window[r++];
          if (r>=WINDOW_SIZE) r=0;

          if (window_ptr>=WINDOW_SIZE)
          {
            fwrite(window,1,WINDOW_SIZE,out);
            huffman->checksum=crc32(huffman->window,WINDOW_SIZE,huffman->checksum);
            window_ptr=0;
          }
        }
      }

#ifdef DEBUG
printf("\n");
#endif
    }
  }

  huffman->window_ptr=window_ptr;

  reverse_bitstream(bitstream);

  return 0;
}

int inflate(FILE *in, FILE *out, unsigned int *checksum)
{
#ifndef ZIP
unsigned char CMF, FLG;
unsigned int DICT;
#endif
struct bitstream_t bitstream;
struct huffman_t huffman;
int comp_method;
int block_len,bfinal;
int t;
struct huffman_tree_t *huffman_tree_len;
struct huffman_tree_t *huffman_tree_dist;

  huffman.checksum=0xffffffff;

  huffman_tree_len_static=0;
  huffman_tree_len=malloc(1024*sizeof(struct huffman_tree_t));
  huffman_tree_dist=malloc(1024*sizeof(struct huffman_tree_t));

  huffman.window_ptr=0;

#ifdef DEBUG
printf("\nStarting at %d 0x%x\n",(int)ftell(in),(int)ftell(in));
#endif

#ifndef ZIP
  CMF=getc(in);
  FLG=getc(in);

#ifdef DEBUG
printf("   CMF: %d\n",CMF);
printf("   FLG: %d\n",FLG);
printf("    CM: %d\n",CMF&15);
printf(" CINFO: %d\n",(CMF>>4)&15);
printf("FCHECK: %d\n",FLG&31);
printf(" FDICT: %d\n",(FLG>5)&1);
printf("FLEVEL: %d\n\n",(FLG>6)&3);
#endif

  if ((CMF&15)!=8)
  {
    printf("Unsupported compression used.\n");
    return -1;
  }

  if ((FLG&32)!=0)
  {
    DICT=read_int_b(in);
  }

  if (((CMF*256+FLG)%31)!=0)
  {
    printf("FCHECK fails.\n");
  }
#endif


  bitstream.holding=0;
  bitstream.bitptr=0;

  while(1)
  {
    if (bitstream.bitptr<3)
    {
      bitstream.holding=reverse[getc(in)]+(bitstream.holding<<8);
      bitstream.bitptr+=8;
    }

#ifdef DEBUG
printf("holding=%d\n",bitstream.holding);
#endif

    bfinal=bitstream.holding>>(bitstream.bitptr-1);
    comp_method=(bitstream.holding>>(bitstream.bitptr-3))&3;

    bitstream.bitptr-=3;
    bitstream.holding=bitstream.holding&((1<<bitstream.bitptr)-1);

#ifdef DEBUG
printf("comp_method=%d  bfinal=%d\n",comp_method,bfinal);
#endif

    if (comp_method==0)
    {
      /* No Compression */
      bitstream.holding=0;
      bitstream.bitptr=0;

      block_len=read_word(in);

      t=read_word(in)^0xffff;

      if (block_len!=t)
      {
        printf("Error: LEN and NLEN don't match (%d %d)\n",block_len,t);
        break;
      }

      for (t=0; t<block_len; t++)
      {
        huffman.window[huffman.window_ptr++]=getc(in);

        if (huffman.window_ptr>=WINDOW_SIZE)
        {
          fwrite(huffman.window,1,WINDOW_SIZE,out);
          huffman.checksum=crc32(huffman.window,WINDOW_SIZE,huffman.checksum);
          huffman.window_ptr=0;
        }
      }
    } 
      else
    if (comp_method==2)
    {
      /* Fixed Huffman */
      if (huffman_tree_len_static==0)
      { load_fixed_huffman(&huffman, &huffman_tree_len_static); }
      decompress(in, &huffman, &bitstream, huffman_tree_len_static, 0, out);
/*
      free(huffman_tree_len);
      huffman_tree_len=0;
*/
    }
      else
    if (comp_method==1)
    {

      /* Dynamic Huffman */
      load_dynamic_huffman(in,&huffman,&bitstream,huffman_tree_len,huffman_tree_dist);
      decompress(in, &huffman, &bitstream, huffman_tree_len, huffman_tree_dist, out);

    }
      else
    if (comp_method==3)
    {
      /* Error */
      printf("Error (inflate): unknown compression type\n");
      break;
    }

    if (bfinal==1) { break; }
  }

  if (huffman.window_ptr!=0)
  {
    fwrite(huffman.window,1,huffman.window_ptr,out);
    huffman.checksum=crc32(huffman.window,huffman.window_ptr,huffman.checksum);
  }

/*
  if (buffer!=0)
  { free(buffer); }
*/

  if (huffman_tree_len!=0) free(huffman_tree_len);
  if (huffman_tree_dist!=0) free(huffman_tree_dist);

  *checksum=huffman.checksum^0xffffffff;

/*
  huffman_tree_len=0;
  huffman_tree_dist=0;
*/

  return 0;
}
