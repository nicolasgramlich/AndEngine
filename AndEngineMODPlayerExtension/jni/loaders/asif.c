
#ifdef HAVE_CONFIG_H
#include "config.h"
#endif

#include <stdio.h>

#include "load.h"
#include "asif.h"

#define MAGIC_FORM	MAGIC4('F','O','R','M')
#define MAGIC_ASIF	MAGIC4('A','S','I','F')
#define MAGIC_NAME	MAGIC4('N','A','M','E')
#define MAGIC_INST	MAGIC4('I','N','S','T')
#define MAGIC_WAVE	MAGIC4('W','A','V','E')

int asif_load(struct xmp_context *ctx, FILE *f, int i)
{
	struct xmp_player_context *p = &ctx->p;
	struct xmp_mod_context *m = &p->m;
	int size, pos;
	uint32 id;
	int chunk;
	int j;

	if (f == NULL)
		return -1;

	if (read32b(f) != MAGIC_FORM)
		return -1;
	size = read32b(f);

	if (read32b(f) != MAGIC_ASIF)
		return -1;

	for (chunk = 0; chunk < 2; ) {
		id = read32b(f);
		size = read32b(f);
		pos = ftell(f) + size;

		switch (id) {
		case MAGIC_WAVE:
			//printf("wave chunk\n");
		
			fseek(f, read8(f), SEEK_CUR);	/* skip name */
			m->xxs[i].len = read16l(f) + 1;
			size = read16l(f);		/* NumSamples */
			
			//printf("WaveSize = %d\n", xxs[i].len);
			//printf("NumSamples = %d\n", size);

			for (j = 0; j < size; j++) {
				read16l(f);		/* Location */
				m->xxs[j].len = 256 * read16l(f);
				read16l(f);		/* OrigFreq */
				read16l(f);		/* SampRate */
			}
		
			xmp_drv_loadpatch(ctx, f, i, m->c4rate,
					XMP_SMP_UNS, &m->xxs[i], NULL);

			chunk++;
			break;

		case MAGIC_INST:
			//printf("inst chunk\n");
		
			fseek(f, read8(f), SEEK_CUR);	/* skip name */
		
			read16l(f);			/* SampNum */
			fseek(f, 24, SEEK_CUR);		/* skip envelope */
			read8(f);			/* ReleaseSegment */
			read8(f);			/* PriorityIncrement */
			read8(f);			/* PitchBendRange */
			read8(f);			/* VibratoDepth */
			read8(f);			/* VibratoSpeed */
			read8(f);			/* UpdateRate */
		
			m->xxih[i].nsm = 1;
			m->xxi[i][0].vol = 0x40;
			m->xxi[i][0].pan = 0x80;
			m->xxi[i][0].sid = i;

			chunk++;
		}

		fseek(f, pos, SEEK_SET);
	}

	return 0;
}
