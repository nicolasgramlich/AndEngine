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

#include "common.h"


inline uint8 read8(FILE *f)
{
	return (uint8)fgetc(f);
}

int8 read8s(FILE *f)
{
	return (int8)fgetc(f);
}

uint16 read16l(FILE *f)
{
	uint32 a, b;

	a = read8(f);
	b = read8(f);

	return (b << 8) | a;
}

uint16 read16b(FILE *f)
{
	uint32 a, b;

	a = read8(f);
	b = read8(f);

	return (a << 8) | b;
}

uint32 read24l(FILE *f)
{
	uint32 a, b, c;

	a = read8(f);
	b = read8(f);
	c = read8(f);

	return (c << 16) | (b << 8) | a;
}

uint32 read24b(FILE *f)
{
	uint32 a, b, c;

	a = read8(f);
	b = read8(f);
	c = read8(f);

	return (a << 16) | (b << 8) | c;
}

uint32 read32l(FILE *f)
{
	uint32 a, b, c, d;

	a = read8(f);
	b = read8(f);
	c = read8(f);
	d = read8(f);

	return (d << 24) | (c << 16) | (b << 8) | a;
}

uint32 read32b(FILE *f)
{
	uint32 a, b, c, d;

	a = read8(f);
	b = read8(f);
	c = read8(f);
	d = read8(f);

	return (a << 24) | (b << 16) | (c << 8) | d;
}



inline void write8(FILE *f, uint8 b)
{
	fputc(b, f);
}

void write16l(FILE *f, uint16 w)
{
	write8(f, w & 0x00ff);
	write8(f, (w & 0xff00) >> 8);
}

void write16b(FILE *f, uint16 w)
{
	write8(f, (w & 0xff00) >> 8);
	write8(f, w & 0x00ff);
}

void write32l(FILE *f, uint32 w)
{
	write8(f, w & 0x000000ff);
	write8(f, (w & 0x0000ff00) >> 8);
	write8(f, (w & 0x00ff0000) >> 16);
	write8(f, (w & 0xff000000) >> 24);
}

void write32b(FILE *f, uint32 w)
{
	write8(f, (w & 0xff000000) >> 24);
	write8(f, (w & 0x00ff0000) >> 16);
	write8(f, (w & 0x0000ff00) >> 8);
	write8(f, w & 0x000000ff);
}

uint16 readmem16l(uint8 *m)
{
	uint32 a, b;

	a = m[0];
	b = m[1];

	return (b << 8) | a;
}

uint16 readmem16b(uint8 *m)
{
	uint32 a, b;

	a = m[0];
	b = m[1];

	return (a << 8) | b;
}

uint32 readmem32l(uint8 *m)
{
	uint32 a, b, c, d;

	a = m[0];
	b = m[1];
	c = m[2];
	d = m[3];

	return (d << 24) | (c << 16) | (b << 8) | a;
}

uint32 readmem32b(uint8 *m)
{
	uint32 a, b, c, d;

	a = m[0];
	b = m[1];
	c = m[2];
	d = m[3];

	return (a << 24) | (b << 16) | (c << 8) | d;
}

