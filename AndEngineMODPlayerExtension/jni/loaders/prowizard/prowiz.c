/*
 * Pro-Wizard_1.c
 *
 * Copyright (C) 1997-1999 Sylvain "Asle" Chipaux
 * Copyright (C) 2006-2007 Claudio Matsuoka
 */

#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include "xmp.h"

#include "prowiz.h"

void register_format (char *, char *);

static int check (unsigned char *, int);

static LIST_HEAD(pw_format_list);


int pw_enable(char *id, int enable)
{
	struct list_head *tmp;
	struct pw_format *format;

	list_for_each(tmp, &pw_format_list) {
		format = list_entry(tmp, struct pw_format, list);
		if (!strcmp(id, format->id)) {
			format->enable = enable;
			return 0;
		}
	}

	return 1;
}

int pw_register(struct pw_format *f)
{
	f->enable = 1;
	list_add_tail(&f->list, &pw_format_list);
	register_format(f->id, f->name);
	return 0;
}

int pw_unregister(struct pw_format *f)
{
	list_del(&f->list);
	return 0;
}

int pw_move_data(FILE *out, FILE *in, int len)
{
	uint8 buf[1024];
	int l;

	do {
		l = fread(buf, 1, len > 1024 ? 1024 : len, in);
		fwrite(buf, 1, l, out);
		len -= l;
	} while (l > 0 && len > 0);

	return 0;
}

int pw_write_zero(FILE *out, int len)
{
	uint8 buf[1024];
	int l;
	
	do {
		l = len > 1024 ? 1024 : len;
		memset(buf, 0, l);
		fwrite(buf, 1, l, out);
		len -= l;
	} while (l > 0 && len > 0);

	return 0;
}

int pw_init()
{
	/* With signature */
	pw_register(&pw_ac1d);
	/* pw_register (&pw_emod); */
	pw_register(&pw_fchs);
	pw_register(&pw_fcm);
	pw_register(&pw_fuzz);
	pw_register(&pw_hrt);
	pw_register(&pw_kris);
	pw_register(&pw_ksm);
	pw_register(&pw_mp_id);
	pw_register(&pw_ntp);
	pw_register(&pw_p18a);
	pw_register(&pw_p10c);
	pw_register(&pw_pru1);
	pw_register(&pw_pru2);
	pw_register(&pw_pha);
	pw_register(&pw_wn);
	pw_register(&pw_unic_id);
	pw_register(&pw_tp3);
	pw_register(&pw_skyt);

	/* No signature */
	pw_register(&pw_xann);
	pw_register(&pw_mp_noid);	/* Must check before Heatseeker */
	pw_register(&pw_di);
	pw_register(&pw_eu);
	pw_register(&pw_p4x);
	pw_register(&pw_pp21);
	pw_register(&pw_p50a);
	pw_register(&pw_p60a);
	pw_register(&pw_p61a);
	pw_register(&pw_np2);
	pw_register(&pw_np1);
	pw_register(&pw_np3);
	pw_register(&pw_zen);
	pw_register(&pw_unic_emptyid);
	pw_register(&pw_unic_noid);
	pw_register(&pw_unic2);
	pw_register(&pw_crb);
	pw_register(&pw_tdd);
	pw_register(&pw_starpack);
	pw_register(&pw_gmc);
	pw_register(&pw_titanics);

	return 0;
}

struct list_head *checked_format = &pw_format_list;

int pw_wizardry(int in, int out, struct pw_format **fmt)
{
	struct list_head *tmp;
	struct pw_format *format;
	struct stat st;
	int size = -1, in_size;
	uint8 *data;
	FILE *file_in, *file_out;

	file_in = fdopen(dup(in), "rb");
	if (file_in == NULL)
		return -1;

	file_out = fdopen(dup(out), "w+b");

	if (fstat(fileno(file_in), &st) < 0)
		in_size = -1;
	else
		in_size = st.st_size;

	/* printf ("input file size : %d\n", in_size); */
	if (in_size < MIN_FILE_LENGHT)
		return -2;

	/* alloc mem */
	data = (uint8 *)malloc (in_size + 4096);	/* slack added */
	if (data == NULL) {
		perror("Couldn't allocate memory");
		return -1;
	}
	fread(data, in_size, 1, file_in);


  /********************************************************************/
  /**************************   SEARCH   ******************************/
  /********************************************************************/

	if (checked_format != &pw_format_list)
		goto checked;

	list_for_each(tmp, &pw_format_list) {
		format = list_entry(tmp, struct pw_format, list);
		_D("checking format: %s", format->name);
		if (format->test(data, in_size) >= 0)
			goto done;
	}
	return -1;

checked:
	format = list_entry(checked_format, struct pw_format, list);
	_D(_D_WARN "checked format: %s", format->name);
	checked_format = &pw_format_list;

done:
	fseek(file_in, 0, SEEK_SET);
	size = -1;	/* paranoia setting */
	if (format->depack) 
		size = format->depack(file_in, file_out);

	if (size < 0)
		return -1;

	//pw_crap(format, file_out);
	fclose(file_out);
	fclose(file_in);

	/*
	 * ADD: Rip based on size
	 */

	free(data);

	if (fmt)
		*fmt = format;

	return 0;
}

static struct list_head *shortcut = &pw_format_list;
static int check(unsigned char *b, int s)
{
	struct list_head *tmp;
	struct pw_format *format;
	int extra;

	list_for_each(tmp, shortcut) {
		if (tmp == &pw_format_list)
			break;
		format = list_entry(tmp, struct pw_format, list);
		_D("checking format [%d]: %s", s, format->name);
		if ((extra = format->test (b, s)) > 0) {
			_D("format: %s, extra: %d", format->id, extra);
			shortcut = tmp->prev;
			return extra;
		}
		if (extra == 0) {
			_D("format ok: %s", format->id);
			checked_format = tmp;
			shortcut = &pw_format_list;
			return 0;
		}
	}

	shortcut = &pw_format_list;
	return -1;
}

int pw_check(unsigned char *b, int s)
{
	return check(b, s);
}
