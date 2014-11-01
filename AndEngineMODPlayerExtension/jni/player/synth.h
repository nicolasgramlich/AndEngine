
#ifndef __XMP_SYNTH_H
#define __XMP_SYNTH_H

#include "common.h"

int	synth_init	(int);
int	synth_deinit	(void);
int	synth_reset	(void);
void	synth_setpatch	(int, uint8 *);
void	synth_setnote	(int, int, int);
void	synth_setvol	(int, int);
void	synth_mixer	(int*, int, int, int, int);


#endif
