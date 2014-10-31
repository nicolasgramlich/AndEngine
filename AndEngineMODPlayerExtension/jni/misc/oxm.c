/* Extended Module Player OMX depacker
 * Copyright (C) 2007 Claudio Matsuoka
 *
 * This file is part of the Extended Module Player and is distributed
 * under the terms of the GNU General Public License. See doc/COPYING
 * for more information.
 */

#if !defined WIN32 && !defined __AMIGA__ && !defined __AROS__

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <unistd.h>

#include "common.h"

#define MAGIC_OGGS	0x4f676753


struct xm_instrument {
	uint32 len;
	uint8 buf[36];
};

static void move_data(FILE *out, FILE *in, int len)
{
	uint8 buf[1024];
	int l;

	do {
		l = fread(buf, 1, len > 1024 ? 1024 : len, in);
		fwrite(buf, 1, l, out);
		len -= l;
	} while (l > 0 && len > 0);
}


int test_oxm(FILE *f)
{
	int i, j;
	int hlen, npat, len, plen;
	int nins, nsmp;
	uint32 ilen;
	int slen[256];
	uint8 buf[1024];

	fseek(f, 0, SEEK_SET);
	if (fread(buf, 16, 1, f) < 16)
		return -1;
	if (memcmp(buf, "Extended Module:", 16))
		return -1;
	
	fseek(f, 60, SEEK_SET);
	hlen = read32l(f);
	fseek(f, 6, SEEK_CUR);
	npat = read16l(f);
	nins = read16l(f);

	if (npat > 256 || nins > 128)
		return -1;
	
	fseek(f, 60 + hlen, SEEK_SET);

	for (i = 0; i < npat; i++) {
		len = read32l(f);
		fseek(f, 3, SEEK_CUR);
		plen = read16l(f);
		fseek(f, len - 9 + plen, SEEK_CUR);
	}

	for (i = 0; i < nins; i++) {
		ilen = read32l(f);
		if (ilen > 263)
			return -1;
		fseek(f, -4, SEEK_CUR);
		fread(buf, ilen, 1, f);		/* instrument header */
		nsmp = readmem16l(buf + 27);

		if (nsmp > 255)
			return -1;
		if (nsmp == 0)
			continue;

		/* Read instrument data */
		for (j = 0; j < nsmp; j++) {
			slen[j] = read32l(f);
			fseek(f, 36, SEEK_CUR);
		}

		/* Read samples */
		for (j = 0; j < nsmp; j++) {
			read32b(f);
			if (read32b(f) == 0x4f676753)
				return 0;
			fseek(f, slen[j] - 8, SEEK_CUR);
		}
	}

	return -1;
}

/*
 * Invoke oggdec to decode our vorbis file to a temporary file,
 * then read that back when writing to our depacked XM file
 */
static char *oggdec(FILE *f, int len, int res, int *newlen)
{
	char buf[1024];
	FILE *t;
	int i, l;
	struct stat st;
	int8 *pcm;
	int16 *pcm16;
	uint32 id;
	int status, p[2];

	read32b(f);
	id = read32b(f);
	fseek(f, -8, SEEK_CUR);

	if (id != MAGIC_OGGS) {		/* copy input data if not Ogg file */
		if ((pcm = malloc(len)) == NULL)
			return NULL;
		fread(pcm, 1, len, f);
		*newlen = len;
		return (char *)pcm;
	}
	
	if ((t = tmpfile()) == NULL)
		return NULL;

	if (pipe(p) < 0)
		return NULL;

	if (fork() == 0) {		/* child process runs oggdec */
		char b[10];
		int l;

		close(p[1]);
		dup2(p[0], STDIN_FILENO);
		dup2(fileno(t), STDOUT_FILENO);

		snprintf(b, 10, "-b%d", res);
		execlp("oggdec", "oggdec", "-Q", b, "-e0", "-R", "-s1",
							"-o-", "-", NULL);

		do {			/* drain input data */
			l = read(STDIN_FILENO, buf, 1024);
		} while (l == 1024);

		exit(1);
	}

	close(p[0]);

	do {				/* write vorbis data to oggdec */
		l = len > 1024 ? 1024 : len;
		fread(buf, 1, l, f);
		write(p[1], buf, l);
		len -= l;
	} while (l > 0 && len > 0);

	close(p[1]);
	wait(&status);

	if (!WIFEXITED(status) || WEXITSTATUS(status) != 0) {
		fclose(t);
		return NULL;
	}

	if (fstat(fileno(t), &st) < 0) {
		fclose(t);
		return NULL;
	}

	if ((pcm = malloc(st.st_size)) == NULL) {
		fclose(t);
		return NULL;
	}

	pcm16 = (int16 *)pcm;
	fseek(t, 0, SEEK_SET);
	fread(pcm, 1, st.st_size, t);
	fclose(t);

	/* Convert to delta */
	if (res == 8) {
		for (i = st.st_size - 1; i > 0; i--)
			pcm[i] -= pcm[i - 1];
		*newlen = st.st_size;
	} else {
		for (i = st.st_size / 2 - 1; i > 0; i--)
			pcm16[i] -= pcm16[i - 1];
		*newlen = st.st_size / 2;
	}

	return (char *)pcm;
}

int decrunch_oxm(FILE *f, FILE *fo)
{
	int i, j, pos;
	int hlen, npat, len, plen;
	int nins, nsmp;
	uint32 ilen;
	uint8 buf[1024];
	struct xm_instrument xi[256];
	char *pcm[256];
	int newlen = 0;

	fseek(f, 60, SEEK_SET);
	hlen = read32l(f);
	fseek(f, 6, SEEK_CUR);
	npat = read16l(f);
	nins = read16l(f);
	
	fseek(f, 60 + hlen, SEEK_SET);

	for (i = 0; i < npat; i++) {
		len = read32l(f);
		fseek(f, 3, SEEK_CUR);
		plen = read16l(f);
		fseek(f, len - 9 + plen, SEEK_CUR);
	}

	pos = ftell(f);
	fseek(f, 0, SEEK_SET);
	move_data(fo, f, pos);			/* module header + patterns */

	for (i = 0; i < nins; i++) {
		ilen = read32l(f);
		if (ilen > 1024)
			return -1;
		fseek(f, -4, SEEK_CUR);
		fread(buf, ilen, 1, f);		/* instrument header */
		buf[26] = 0;
		fwrite(buf, ilen, 1, fo);
		nsmp = readmem16l(buf + 27);

		if (nsmp == 0)
			continue;

		/* Read sample headers */
		for (j = 0; j < nsmp; j++) {
			xi[j].len = read32l(f);
			fread(xi[j].buf, 1, 36, f);
		}

		/* Read samples */
		for (j = 0; j < nsmp; j++) {
			if (xi[j].len > 0) {
				int res = 8;
				if (xi[j].buf[10] & 0x10)
					res = 16;
				pcm[j] = oggdec(f, xi[j].len, res, &newlen);
				xi[j].len = newlen;

				if (pcm[j] == NULL)
					return -1;
			}
		}

		/* Write sample headers */
		for (j = 0; j < nsmp; j++) {
			write32l(fo, xi[j].len);
			fwrite(xi[j].buf, 1, 36, fo);
		}

		/* Write samples */
		for (j = 0; j < nsmp; j++) {
			if (xi[j].len > 0) {
				fwrite(pcm[j], 1, xi[j].len, fo);
				free(pcm[j]);
			}
		}
	}

	return 0;
}

#endif /* !WIN32 && !__AMIGA__ && !__AROS__ */
