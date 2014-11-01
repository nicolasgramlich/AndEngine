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
#include <string.h>
#include <ctype.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <limits.h>

#if !defined(HAVE_POPEN) && defined(WIN32)
#include "ptpopen.h"
#endif

#include "driver.h"
#include "convert.h"
#include "loader.h"

#include "list.h"

int pw_enable(char *, int);

extern struct list_head loader_list;

LIST_HEAD(tmpfiles_list);

struct tmpfilename {
	char *name;
	struct list_head list;
};

char *global_filename;

int decrunch_arc	(FILE *, FILE *);
int decrunch_arcfs	(FILE *, FILE *);
int decrunch_sqsh	(FILE *, FILE *);
int decrunch_pp		(FILE *, FILE *);
int decrunch_mmcmp	(FILE *, FILE *);
int decrunch_muse	(FILE *, FILE *);
int decrunch_lzx	(FILE *, FILE *);
int decrunch_oxm	(FILE *, FILE *);
int decrunch_xfd	(FILE *, FILE *);
int decrunch_s404	(FILE *, FILE *);
int test_oxm		(FILE *);
char *test_xfd		(unsigned char *, int);

#define BUILTIN_PP	0x01
#define BUILTIN_SQSH	0x02
#define BUILTIN_MMCMP	0x03
#define BUILTIN_ARC	0x05
#define BUILTIN_ARCFS	0x06
#define BUILTIN_S404	0x07
#define BUILTIN_OXM	0x08
#define BUILTIN_XFD	0x09
#define BUILTIN_MUSE	0x0a
#define BUILTIN_LZX	0x0b


#if defined __EMX__ || defined WIN32
#define REDIR_STDERR "2>NUL"
#elif defined unix || defined __unix__
#define REDIR_STDERR "2>/dev/null"
#else
#define REDIR_STDERR
#endif

static int decrunch(struct xmp_context *ctx, FILE **f, char **s)
{
    struct xmp_options *o = &ctx->o;
    unsigned char b[1024];
    char *cmd;
    FILE *t;
    int fd, builtin, res;
    char *packer, *temp2, tmp[PATH_MAX];
    struct tmpfilename *temp;

    packer = cmd = NULL;
    builtin = res = 0;

    if (get_temp_dir(tmp, PATH_MAX) < 0)
	return 0;

    strncat(tmp, "xmp_XXXXXX", PATH_MAX);

    fseek(*f, 0, SEEK_SET);
    if (fread(b, 1, 1024, *f) < 100)	/* minimum valid file size */
	return 0;

#if defined __AMIGA__ && !defined __AROS__
    if (packer = test_xfd(b, 1024)) {
	builtin = BUILTIN_XFD;
    } else
#endif
    if (b[0] == 'P' && b[1] == 'K' && b[2] == 0x03 && b[3] == 0x04) {
	packer = "Zip";
#if defined WIN32
	cmd = "unzip -pqqC \"%s\" -x readme *.diz *.nfo *.txt *.exe *.com "
		"README *.DIZ *.NFO *.TXT *.EXE *.COM " REDIR_STDERR;
#else
	cmd = "unzip -pqqC \"%s\" -x readme '*.diz' '*.nfo' '*.txt' '*.exe' "
		"'*.com' README '*.DIZ' '*.NFO' '*.TXT' '*.EXE' '*.COM' "
		REDIR_STDERR;
#endif
    } else if (b[2] == '-' && b[3] == 'l' && b[4] == 'h') {
	packer = "LHa";
#if defined __EMX__
	fprintf( stderr, "LHA for OS/2 does NOT support output to stdout.\n" );
#elif defined __AMIGA__
	cmd = "lha p -q \"%s\"";
#else
	cmd = "lha -pq \"%s\"";
#endif
    } else if (b[0] == 31 && b[1] == 139) {
	packer = "gzip";
	cmd = "gzip -dc \"%s\"";
    } else if (b[0] == 'B' && b[1] == 'Z' && b[2] == 'h') {
	packer = "bzip2";
	cmd = "bzip2 -dc \"%s\"";
    } else if (b[0] == 0x5d && b[1] == 0 && b[2] == 0 && b[3] == 0x80) {
	packer = "lzma";
	cmd = "lzma -dc \"%s\"";
    } else if (b[0] == 0xfd && b[3] == 'X' && b[4] == 'Z' && b[5] == 0x00) {
	packer = "xz";
	cmd = "xz -dc \"%s\"";
    } else if (b[0] == 'Z' && b[1] == 'O' && b[2] == 'O' && b[3] == ' ') {
	packer = "zoo";
	cmd = "zoo xpq \"%s\"";
    } else if (b[0] == 'M' && b[1] == 'O' && b[2] == '3') {
	packer = "MO3";
	cmd = "unmo3 -s \"%s\" STDOUT";
    } else if (b[0] == 31 && b[1] == 157) {
	packer = "compress";
#ifdef __EMX__
	fprintf( stderr, "I was unable to find a OS/2 version of UnCompress...\n" );
	fprintf( stderr, "I hope that the default command will work if a OS/2 version is found/created!\n" );
#endif
	cmd = "uncompress -c \"%s\"";
    } else if (memcmp(b, "PP20", 4) == 0) {
	packer = "PowerPack";
	builtin = BUILTIN_PP;
    } else if (memcmp(b, "XPKF", 4) == 0 && memcmp(b + 8, "SQSH", 4) == 0) {
	packer = "SQSH";
	builtin = BUILTIN_SQSH;
    } else if (!memcmp(b, "Archive\0", 8)) {
	packer = "ArcFS";
	builtin = BUILTIN_ARCFS;
    } else if (memcmp(b, "ziRCONia", 8) == 0) {
	packer = "MMCMP";
	builtin = BUILTIN_MMCMP;
    } else if (memcmp(b, "MUSE", 4) == 0 && readmem32b(b + 4) == 0xdeadbeaf) {
	packer = "J2B MUSE";
	builtin = BUILTIN_MUSE;
    } else if (memcmp(b, "MUSE", 4) == 0 && readmem32b(b + 4) == 0xdeadbabe) {
	packer = "MOD2J2B MUSE";
	builtin = BUILTIN_MUSE;
    } else if (memcmp(b, "LZX", 3) == 0) {
	packer = "LZX";
	builtin = BUILTIN_LZX;
    } else if (memcmp(b, "Rar", 3) == 0) {
	packer = "rar";
	cmd = "unrar p -inul -xreadme -x*.diz -x*.nfo -x*.txt "
	    "-x*.exe -x*.com \"%s\"";
    } else if (memcmp(b, "S404", 4) == 0) {
	packer = "Stonecracker";
	builtin = BUILTIN_S404;
#if !defined WIN32 && !defined __AMIGA__
    } else if (test_oxm(*f) == 0) {
	packer = "oggmod";
	builtin = BUILTIN_OXM;
#endif
    }

    if (packer == NULL && b[0] == 0x1a) {
	int x = b[1] & 0x7f;
	int i, flag = 0;
	long size;
	
	/* check file name */
	for (i = 0; i < 13; i++) {
	    if (b[2 + i] == 0) {
		if (i == 0)		/* name can't be empty */
		    flag = 1;
		break;
	    }
	    if (!isprint(b[2 + i])) {	/* name must be printable */
		flag = 1;
		break;
	    }
	}

	size = readmem32l(b + 15);	/* max file size is 512KB */
	if (size < 0 || size > 512 * 1024)
		flag = 1;

        if (flag == 0) {
	    if (x >= 1 && x <= 9 && x != 7) {
		packer = "Arc";
		builtin = BUILTIN_ARC;
	    } else if (x == 0x7f) {
		packer = "!Spark";
		builtin = BUILTIN_ARC;
	    }
	}
    }

    fseek(*f, 0, SEEK_SET);

    if (packer == NULL)
	return 0;

#ifdef ANDROID
    /* Don't use external helpers in android */
    if (cmd)
	return 0;
#endif

    reportv(ctx, 0, "Depacking %s file... ", packer);

    temp = calloc(sizeof (struct tmpfilename), 1);
    if (!temp) {
	report("calloc failed\n");
	goto err;
    }

    temp->name = strdup(tmp);
    if ((fd = mkstemp(temp->name)) < 0) {
	if (o->verbosity > 0)
	    report("failed\n");
	goto err;
    }

    list_add_tail(&temp->list, &tmpfiles_list);

    if ((t = fdopen(fd, "w+b")) == NULL) {
	reportv(ctx, 0, "failed\n");
	goto err;
    }

    if (cmd) {
#define BSIZE 0x4000
	int n;
	char line[1024], buf[BSIZE];
	FILE *p;

	snprintf(line, 1024, cmd, *s);

#ifdef WIN32
	/* Note: The _popen function returns an invalid file handle, if
	 * used in a Windows program, that will cause the program to hang
	 * indefinitely. _popen works properly in a Console application.
	 * To create a Windows application that redirects input and output,
	 * read the section "Creating a Child Process with Redirected Input
	 * and Output" in the Win32 SDK. -- Mirko 
	 */
	if ((p = popen(line, "rb")) == NULL) {
#else
	/* Linux popen fails with "rb" */
	if ((p = popen(line, "r")) == NULL) {
#endif
	    reportv(ctx, 0, "failed\n");
	    fclose(t);
	    goto err;
	}
	while ((n = fread(buf, 1, BSIZE, p)) > 0)
	    fwrite(buf, 1, n, t);
	pclose (p);
    } else {
	switch (builtin) {
	case BUILTIN_PP:    
	    res = decrunch_pp(*f, t);
	    break;
	case BUILTIN_ARC:
	    res = decrunch_arc(*f, t);
	    break;
	case BUILTIN_ARCFS:
	    res = decrunch_arcfs(*f, t);
	    break;
	case BUILTIN_SQSH:    
	    res = decrunch_sqsh(*f, t);
	    break;
	case BUILTIN_MMCMP:    
	    res = decrunch_mmcmp(*f, t);
	    break;
	case BUILTIN_MUSE:    
	    res = decrunch_muse(*f, t);
	    break;
	case BUILTIN_LZX:    
	    res = decrunch_lzx(*f, t);
	    break;
	case BUILTIN_S404:
	    res = decrunch_s404(*f, t);
	    break;
#if !defined WIN32 && !defined __MINGW32__ && !defined __AMIGA__
	case BUILTIN_OXM:
	    res = decrunch_oxm(*f, t);
	    break;
#endif
#ifdef AMIGA
	case BUILTIN_XFD:
	    res = decrunch_xfd(*f, t);
	    break;
#endif
	}
    }

    if (res < 0) {
	reportv(ctx, 0, "failed\n");
	fclose(t);
	goto err;
    }

    reportv(ctx, 0, "done\n");

    fclose(*f);
    *f = t;
 
    temp2 = strdup(temp->name);
    decrunch(ctx, f, &temp->name);
    unlink(temp2);
    free(temp2);
    /* Mirko: temp is now deallocated in xmp_unlink_tempfiles
     * not a problem, since xmp_unlink_tempfiles is called after decrunch
     * in loader routines
     *
     * free(temp);
     */

    return 1;

err:
    return -1;
}


/*
 * Windows doesn't allow you to unlink an open file, so we changed the
 * temp file cleanup system to remove temporary files after we close it
 */
void xmp_unlink_tempfiles(void)
{
	struct tmpfilename *li;
	struct list_head *tmp;

	/* can't use list_for_each when freeing the node! */
	for (tmp = (&tmpfiles_list)->next; tmp != (&tmpfiles_list); ) {
		li = list_entry(tmp, struct tmpfilename, list);
		_D(_D_INFO "unlink tmpfile %s", li->name);
		unlink(li->name);
		free(li->name);
		list_del(&li->list);
		tmp = tmp->next;
		free(li);
	}
}


static void get_smp_size(struct xmp_player_context *p, int awe, int *a, int *b)
{
    int i, len, smp_size, smp_4kb;

    for (smp_4kb = smp_size = i = 0; i < p->m.xxh->smp; i++) {
	len = p->m.xxs[i].len;

	/* AWE cards work with 16 bit samples only and expand bidirectional
	 * loops.
	 */
	if (awe) {
	    if (p->m.xxs[i].flg & WAVE_BIDIR_LOOP)
		len += p->m.xxs[i].lpe - p->m.xxs[i].lps;
	    if (awe && (~p->m.xxs[i].flg & WAVE_16_BITS))
		len <<= 1;
	}

	smp_size += (len += sizeof (int)); 
	if (len < 0x1000)
	    smp_4kb += len;
    }

    *a = smp_size;
    *b = smp_4kb;
}


static int crunch_ratio(struct xmp_context *ctx, int awe)
{
    struct xmp_player_context *p = &ctx->p;
    struct xmp_driver_context *d = &ctx->d;
    struct xmp_options *o = &ctx->o;
    int memavl, smp_size, ratio, smp_4kb;

    ratio = 0x10000;
    memavl = d->memavl;

    if (memavl == 0)
	return ratio;

    memavl = memavl * 100 / (100 + o->crunch);

    get_smp_size(p, awe, &smp_size, &smp_4kb);

    if (smp_size > memavl) {
	if (!awe)
	    xmp_cvt_to8bit(ctx);
	get_smp_size(p, awe, &smp_size, &smp_4kb);
    }

    if (smp_size > memavl) {
	ratio = (int)
	    (((int64)(memavl - smp_4kb) << 16) / (smp_size - smp_4kb));
	if (o->verbosity)
	    report ("Crunch ratio   : %d%% [Mem:%.3fMb Smp:%.3fMb]\n",
		100 - 100 * ratio / 0x10000, .000001 * d->memavl,
		.000001 * smp_size);
    }
	
    return ratio;
}

int xmp_test_module(xmp_context ctx, char *s, char *n)
{
    FILE *f;
    struct xmp_loader_info *li;
    struct list_head *head;
    struct stat st;

    if ((f = fopen(s, "rb")) == NULL)
	return -3;

    if (fstat(fileno(f), &st) < 0)
	goto err;

    if (S_ISDIR(st.st_mode))
	goto err;

    if (decrunch((struct xmp_context *)ctx, &f, &s) < 0)
	goto err;

    if (fstat(fileno(f), &st) < 0)	/* get size after decrunch */
	goto err;

    if (st.st_size < 500)		/* set minimum valid module size */
	goto err;

    if (n) *n = 0;			/* reset name prior to testing */

    list_for_each(head, &loader_list) {
	li = list_entry(head, struct xmp_loader_info, list);
	if (li->enable) {
	    fseek(f, 0, SEEK_SET);
	    if (li->test(f, n, 0) == 0) {
	        fclose(f);
		xmp_unlink_tempfiles();
	        return 0;
	    }
	}
    }

err:
    fclose (f);
    xmp_unlink_tempfiles();
    return -1;
}


static void split_name(char *s, char **d, char **b)
{
	char tmp, *div;

	_D("alloc dirname/basename");
	if ((div = strrchr(s, '/'))) {
		tmp = *(div + 1);
		*(div + 1) = 0;
		*d = strdup(s);
		*(div + 1) = tmp;
		*b = strdup(div + 1);
	} else {
		*d = strdup("");
		*b = strdup(s);
	}
}


int xmp_load_module(xmp_context ctx, char *s)
{
    FILE *f;
    int i, t;
    struct xmp_loader_info *li;
    struct list_head *head;
    struct stat st;
    struct xmp_player_context *p = &((struct xmp_context *)ctx)->p;
    struct xmp_driver_context *d = &((struct xmp_context *)ctx)->d;
    struct xmp_mod_context *m = &p->m;
    struct xmp_options *o = &((struct xmp_context *)ctx)->o;
    uint32 crc = 0;

    _D(_D_WARN "s = %s", s);

    if ((f = fopen(s, "rb")) == NULL)
	return -3;

    if (fstat(fileno (f), &st) < 0)
	goto err;

    if (S_ISDIR(st.st_mode))
	goto err;

    _D(_D_INFO "decrunch");
    if ((t = decrunch((struct xmp_context *)ctx, &f, &s)) < 0)
	goto err;

    if (fstat(fileno(f), &st) < 0)	/* get size after decrunch */
	goto err;

    split_name(s, &m->dirname, &m->basename);

    _D(_D_INFO "clear mem");
    xmp_drv_clearmem((struct xmp_context *)ctx);

    /* Reset variables */
    memset(m->name, 0, XMP_NAMESIZE);
    memset(m->type, 0, XMP_NAMESIZE);
    memset(m->author, 0, XMP_NAMESIZE);
    m->filename = s;		/* For ALM, SSMT, etc */
    m->size = st.st_size;
    m->rrate = PAL_RATE;
    m->c4rate = C4_PAL_RATE;
    m->volbase = 0x40;
    m->volume = 0x40;
    m->vol_table = NULL;
    /* Reset control for next module */
    m->flags = o->flags & ~XMP_CTL_FILTER;	/* verify this later */
    m->quirk = o->quirk;
    m->comment = NULL;

    m->xxh = calloc(sizeof (struct xxm_header), 1);
    /* Set defaults */
    m->xxh->tpo = 6;
    m->xxh->bpm = 125;
    m->xxh->chn = 4;

    for (i = 0; i < 64; i++) {
	m->xxc[i].pan = (((i + 1) / 2) % 2) * 0xff;
	m->xxc[i].vol = 0x40;
	m->xxc[i].flg = 0;
    }

    m->verbosity = o->verbosity;

    _D(_D_WARN "load");
    list_for_each(head, &loader_list) {
	li = list_entry(head, struct xmp_loader_info, list);

        _D(_D_INFO "check exclusion");
	if (li->enable == 0)
	    continue;
        _D(_D_INFO "not excluded");
	
	if (o->verbosity > 3)
	    report("Test format: %s (%s)\n", li->id, li->name);
	fseek(f, 0, SEEK_SET);
   	if ((i = li->test(f, NULL, 0)) == 0) {
	    if (o->verbosity > 3)
		report("Identified as %s\n", li->id);
	    fseek(f, 0, SEEK_SET);
	    _D(_D_WARN "load format: %s (%s)", li->id, li->name);
	    if ((i = li->loader((struct xmp_context *)ctx, f, 0) == 0)) {
		crc = cksum(f);
	        break;
	    } else {
		report("can't load module, possibly corrupted file\n");
		i = -1;
		break;
	    }
	}
    }

    fclose(f);
    xmp_unlink_tempfiles();

    if (i < 0) {
	free(m->basename);
	free(m->dirname);
	free(m->xxh);
	return i;
    }

    _xmp_read_modconf((struct xmp_context *)ctx, crc, st.st_size);

    for (i = 0; i < 64; i++) {
	m->xxc[i].cho = o->chorus;	/* set after reading modconf */
	m->xxc[i].rvb = o->reverb;
    }

    if (d->description && (i = (strstr(d->description, " [AWE") != NULL))) {
	xmp_cvt_to16bit((struct xmp_context *)ctx);
	xmp_cvt_bid2und((struct xmp_context *)ctx);
    }

    xmp_drv_flushpatch((struct xmp_context *)ctx, crunch_ratio((struct xmp_context *)ctx, i));

    /* Fix cases where the restart value is invalid e.g. kc_fall8.xm
     * from http://aminet.net/mods/mvp/mvp_0002.lha (reported by
     * Ralf Hoffmann <ralf@boomerangsworld.de>)
     */
    if (m->xxh->rst >= m->xxh->len)
	m->xxh->rst = 0;

    /* Disable filter if --nofilter is specified */
    m->flags &= ~(~o->flags & XMP_CTL_FILTER);

    str_adj(m->name);
    if (!*m->name)
	strncpy(m->name, m->basename, XMP_NAMESIZE);

    if (o->verbosity > 1) {
	report("Module looping : %s\n",
	    m->flags & XMP_CTL_LOOP ? "yes" : "no");
	report("Period mode    : %s\n",
	    m->xxh->flg & XXM_FLG_LINEAR ? "linear" : "Amiga");
    }

    if (o->verbosity > 2) {
	char * amp_factor[] = { "Normal", "x2", "x4", "x8" };
	report("Amiga range    : %s\n", m->xxh->flg & XXM_FLG_MODRNG ?
		"yes" : "no");
	report("Restart pos    : %d\n", m->xxh->rst);
	report("Base volume    : %d\n", m->volbase);
	report("C4 replay rate : %d\n", m->c4rate);
	report("Channel mixing : %d%% (dynamic pan %s)\n",
		m->flags & XMP_CTL_REVERSE ? -o->mix : o->mix,
		m->flags & XMP_CTL_DYNPAN ? "enabled" : "disabled");
	report("Checksum       : %u %ld\n", crc, st.st_size);
	report("Volume amplify : %s\n", amp_factor[o->amplify]);
    }

    if (o->verbosity) {
	report("Channels       : %d [ ", m->xxh->chn);
	for (i = 0; i < m->xxh->chn; i++) {
	    if (m->xxc[i].flg & XXM_CHANNEL_MUTE)
		report("- ");
	    else if (m->xxc[i].flg & XXM_CHANNEL_FM)
		report("F ");
	    else
	        report("%x ", m->xxc[i].pan >> 4);
	}
	report("]\n");
    }

    t = _xmp_scan_module((struct xmp_context *)ctx);

    if (o->verbosity) {
	if (m->flags & XMP_CTL_LOOP)
	    report ("One loop time  : %dmin%02ds\n",
		(t + 500) / 60000, ((t + 500) / 1000) % 60);
	else
	    report ("Estimated time : %dmin%02ds\n",
		(t + 500) / 60000, ((t + 500) / 1000) % 60);
    }

    m->time = t;

    return t;

err:
    fclose(f);
    xmp_unlink_tempfiles();
    return -1;
}


int xmp_enable_format(char *id, int enable)
{
    struct list_head *head;
    struct xmp_loader_info *li;

    list_for_each(head, &loader_list) {
	li = list_entry(head, struct xmp_loader_info, list);
	if (!strcasecmp(id, li->id)) {
	    li->enable = enable;
	    return 0;
        }
    }

    return pw_enable(id, enable);
}


void xmp_release_module(xmp_context ctx)
{
	struct xmp_player_context *p = &((struct xmp_context *)ctx)->p;
	struct xmp_mod_context *m = &p->m;
	int i;

	_D(_D_INFO "Freeing memory");

	if (m->med_vol_table) {
		for (i = 0; i < m->xxh->ins; i++)
			if (m->med_vol_table[i])
				free(m->med_vol_table[i]);
		free(m->med_vol_table);
	}

	if (m->med_wav_table) {
		for (i = 0; i < m->xxh->ins; i++)
			if (m->med_wav_table[i])
				free(m->med_wav_table[i]);
		free(m->med_wav_table);
	}

	for (i = 0; i < m->xxh->trk; i++)
		free(m->xxt[i]);

	for (i = 0; i < m->xxh->pat; i++)
		free(m->xxp[i]);

	for (i = 0; i < m->xxh->ins; i++) {
		free(m->xxfe[i]);
		free(m->xxpe[i]);
		free(m->xxae[i]);
		free(m->xxi[i]);
	}

	free(m->xxt);
	free(m->xxp);
	free(m->xxi);
	if (m->xxh->smp > 0)
		free(m->xxs);
	free(m->xxim);
	free(m->xxih);
	free(m->xxfe);
	free(m->xxpe);
	free(m->xxae);
	free(m->xxh);
	if (m->comment)
		free(m->comment);

	_D("free dirname/basename");
	free(m->dirname);
	free(m->basename);
}
