/* Extended Module Player
 * Copyright (C) 1996-2010 Claudio Matsuoka and Hipolito Carraro Jr
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdio.h>
#include <stdlib.h>
#include "common.h"

int kunzip_inflate_init(void);
int kunzip_inflate_free(void);
int inflate(FILE *, FILE *, unsigned int *);


int decrunch_muse(FILE *f, FILE *fo)                          
{                                                          
	uint32 checksum;
  
	if (fo == NULL) 
		return -1; 

	fseek(f, 24, SEEK_SET);

	kunzip_inflate_init();
	inflate(f, fo, &checksum);
	kunzip_inflate_free();

	return 0;
}
