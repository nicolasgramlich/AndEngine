/* nomarch 1.4 - extract old `.arc' archives.
 * Copyright (C) 2001-2006 Russell Marks.
 *
 * main.c - most of the non-extraction stuff.
 *
 * Modified by Claudio Matsuoka for xmp, 24-Aug-2007
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

#define NOMARCH_VER	"1.4"

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <time.h>
#include <sys/types.h>
#include <unistd.h>
#include "common.h"
#include "readrle.h"
#include "readhuff.h"
#include "readlzw.h"


struct archived_file_header_tag {
	unsigned char method;
	char name[13];
	unsigned long compressed_size;	/* 4 bytes in file */
	unsigned int date, time, crc;	/* 2 bytes each in file */
	unsigned long orig_size;	/* 4 bytes in file */

	int has_crc;
};

/* there is no overall header for the archive, but there's a header
 * for each file stored in it.
 * returns zero if we couldn't get a header.
 * NB: a header with method zero marks EOF.
 */
static int read_file_header(FILE * in, struct archived_file_header_tag *hdrp)
{
	unsigned char buf[4 + 2 + 2 + 2 + 4];	/* used to read size1/date/time/crc/size2 */
	int bufsiz = sizeof(buf);
	int method_high;
	int c;

	hdrp->method = 0xff;
	if (fgetc(in) != 0x1a)
		return (0);

	if ((c = fgetc(in)) == EOF)
		return (0);

/* allow for the spark archive variant's alternate method encoding */
	method_high = (c >> 7);
	hdrp->method = (c & 127);

/* zero if EOF, which also means no further `header' */
	if (hdrp->method == 0)
		return (1);

/* `old' version of uncompressed storage was weird */
	if (hdrp->method == 1)
		bufsiz -= 4;	/* no `orig_size' field */

	if (fread(hdrp->name, 1, sizeof(hdrp->name), in) != sizeof(hdrp->name)
	    || fread(buf, 1, bufsiz, in) != bufsiz)
		return (0);

/* extract the bits from buf */
	hdrp->compressed_size =
	    (buf[0] | (buf[1] << 8) | (buf[2] << 16) | (buf[3] << 24));
	hdrp->date = (buf[4] | (buf[5] << 8));
	hdrp->time = (buf[6] | (buf[7] << 8));
	hdrp->crc = (buf[8] | (buf[9] << 8));	/* yes, only 16-bit CRC */
	hdrp->has_crc = 1;
	if (hdrp->method == 1)
		hdrp->orig_size = hdrp->compressed_size;
	else
		hdrp->orig_size =
		    (buf[10] | (buf[11] << 8) | (buf[12] << 16) |
		     (buf[13] << 24));

/* make *sure* name is asciiz */
	hdrp->name[12] = 0;

#if 0
/* strip top bits, and lowercase the name */
	for (f = 0; f < strlen(hdrp->name); f++)
		hdrp->name[f] = maybe_downcase(hdrp->name[f] & 127);
#endif

/* lose the possible extra bytes in spark archives */
	if (method_high) {
		if (fread(buf, 1, 12, in) != 12)
			return (0);

		/* has a weird recursive-.arc file scheme for subdirs,
		 * and since these are supposed to be dealt with inline
		 * (though they aren't here) the CRCs could be junk.
		 * So check for it being marked as a stored dir.
		 */
		if (hdrp->method == 2 && buf[3] == 0xff && buf[2] == 0xfd
		    && buf[1] == 0xdc)
			hdrp->has_crc = 0;
	}

	return (1);
}

/* self-extracting archives, for both CP/M and MS-DOS, have up to
 * 3 bytes before the initial ^Z. This skips those if present.
 * Returns zero if there's an input error, or we fail to find ^Z in
 * the first 4 bytes.
 *
 * This should work with self-extracting archives for CP/M
 * (e.g. unarc16.ark), and those produced by `arc'. It won't work with
 * pkpak self-extracting archives, for two reasons:
 *
 * - they have 4 bytes before the ^Z.
 * - they have an EOF member (zero byte) right after that, giving you
 *   an archive containing no files (grrr).
 *
 * So I thought it was better (and less confusing) to effectively stick
 * with the not-an-archive error for those. :-)
 */
static int skip_sfx_header(FILE * in)
{
	int c, f, got = 0;

	for (f = 0; f < 4; f++) {
		if ((c = fgetc(in)) == EOF)
			return (0);
		if (c == 0x1a) {
			got = 1;
			ungetc(c, in);
			break;
		}
	}

	return (got);
}

/* read file data, assuming header has just been read from in
 * and hdrp's data matches it. Caller is responsible for freeing
 * the memory allocated.
 * Returns NULL for file I/O error only; OOM is fatal (doesn't return).
 */
static unsigned char *read_file_data(FILE * in,
				     struct archived_file_header_tag *hdrp)
{
	unsigned char *data;
	int siz = hdrp->compressed_size;

	if ((data = malloc(siz)) == NULL)
		fprintf(stderr, "nomarch: out of memory!\n"), exit(1);

	if (fread(data, 1, siz, in) != siz) {
		free(data);
		data = NULL;
	}

	return (data);
}

/* variant which just skips past the data */
static int skip_file_data(FILE *in,struct archived_file_header_tag *hdrp)
{
	int siz = hdrp->compressed_size;
	int f;

	for(f = 0; f < siz; f++)
		if (fgetc(in) == EOF)
			return 0;

	return 1;
}

static int arc_extract(FILE * in, FILE * out)
{
	struct archived_file_header_tag hdr;
	/* int done = 0; */
	unsigned char *data, *orig_data;
	int supported;
	int exitval = 0;

	if (!skip_sfx_header(in) || !read_file_header(in, &hdr))
		return -1;

	/* We don't files named 'From?' */
	while (!strcmp(hdr.name, "From?") || *hdr.name == '!') {
		if (!skip_file_data(in,&hdr))
			return -1;
		if (!read_file_header(in, &hdr))
			return -1;
	}

	/* extract a single file */
	/* do { */
	if (hdr.method == 0) {	/* EOF */
		/* done = 1;
		continue; */
		return -1;
	}

	if ((data = read_file_data(in, &hdr)) == NULL) {
		fprintf(stderr, "nomarch: error reading data (hit EOF)\n");
		return -1;
	}

	orig_data = NULL;
	supported = 0;

	/* FWIW, most common types are (by far) 8/9 and 2.
	 * (127 is the most common in Spark archives, but only those.)
	 * 3 and 4 crop up occasionally. 5 and 6 are very, very rare.
	 * And I don't think I've seen a *single* file with 1 or 7 yet.
	 */
	switch (hdr.method) {
	case 1:
	case 2:		/* no compression */
		supported = 1;
		orig_data = data;
		break;

	case 3:		/* "packed" (RLE) */
		supported = 1;
		orig_data =
		    convert_rle(data, hdr.compressed_size, hdr.orig_size);
		break;

	case 4:		/* "squeezed" (Huffman, like CP/M `SQ') */
		supported = 1;
		orig_data =
		    convert_huff(data, hdr.compressed_size, hdr.orig_size);
		break;

	case 5:		/* "crunched" (12-bit static LZW) */
		supported = 1;
		orig_data = convert_lzw_dynamic(data, 0, 0,
					hdr.compressed_size, hdr.orig_size, 0);
		break;

	case 6:		/* "crunched" (RLE+12-bit static LZW) */
		supported = 1;
		orig_data = convert_lzw_dynamic(data, 0, 1,
					hdr.compressed_size, hdr.orig_size, 0);
		break;

	case 7:	/* PKPAK docs call this one "internal to SEA" */
		/* it looks like this one was only used by a development version
		 * of SEA ARC, so chances are it can be safely ignored.
		 * OTOH, it's just method 6 with a slightly different hash,
		 * so I presume it wouldn't be *that* hard to add... :-)
		 */
		break;

	case 8:		/* "Crunched" [sic]
			 * (RLE+9-to-12-bit dynamic LZW, a *bit* like GIF) */
		supported = 1;
		orig_data = convert_lzw_dynamic(data, 12, 1,
					hdr.compressed_size, hdr.orig_size,
					NOMARCH_QUIRK_SKIPMAX);
		break;

	case 9:		/* "Squashed" (9-to-13-bit, no RLE) */
		supported = 1;
		orig_data = convert_lzw_dynamic(data, 13, 0,
					hdr.compressed_size, hdr.orig_size, 0);
		break;

	case 127:	/* "Compress" (9-to-16-bit, no RLE) ("Spark" only) */
		supported = 1;
		orig_data = convert_lzw_dynamic(data, 16, 0,
					hdr.compressed_size, hdr.orig_size, 0);
		break;
	}

	/* there was a `pak 2.0' which added a type 10 ("distill"), but I don't
	 * plan to support that unless there's some desperate need for it.
	 */

	if (orig_data == NULL) {
		if (supported)
			fprintf(stderr, "error extracting file");
		else
			fprintf(stderr, "unsupported compression method %d\n",
							hdr.method);
		exitval = 1;
	} else {
		char *ptr;

		/* CP/M stuff in particular likes those slashes... */
		while ((ptr = strchr(hdr.name, '/')) != NULL)
			*ptr = '_';

		if (fwrite(orig_data, 1, hdr.orig_size, out) != hdr.orig_size) {
			fprintf(stderr, "error, %s\n", strerror(errno));
			exitval = 1;
		}

		if (orig_data != data)	/* don't free uncompressed stuff twice :-) */
			free(orig_data);
	}

	free(data);

#if 0
	/* read header ready for next file */
	if (!read_file_header(in, &hdr))
		fprintf(stderr,
			"nomarch: error reading record header\n"), exit(1);
#endif
	/*done = 1; */
	/* } while (!done); */

	return exitval;
}

int decrunch_arc(FILE * f, FILE * fo)
{
	int ret;

	if (fo == NULL)
		return -1;

	ret = arc_extract(f, fo);
	if (ret < 0)
		return -1;

	return 0;
}
