
#ifndef __PERIOD_H
#define __PERIOD_H

/* Macros for period conversion */
#define NOTE_B0		11
#define NOTE_Bb0	NOTE_B0+1 
#define MAX_NOTE	NOTE_B0*8
#define MAX_PERIOD	0x1c56
#define MIN_PERIOD_A	0x0008
#define MAX_PERIOD_A	0x1abf
#define MIN_PERIOD_L	0x0000
#define MAX_PERIOD_L	0x1e00

/* Amiga limits */
#define AMIGA_LIMIT_UPPER 108
#define AMIGA_LIMIT_LOWER 907

double	note_to_period		(int, int, int);
int	note_to_period_mix	(int, int);
int	period_to_note		(int);
int	period_to_bend		(double, int, int, int, int);
void	c2spd_to_note		(int, int *, int *);

#endif /* __PERIOD_H */
