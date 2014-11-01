
/* Empty file */

#include <string.h>
#include <stdlib.h>

void testSoundTracker (void)
{
	/* test 1 */
	/* start of stk before start of file ? */
	if (i < 45) {
/*printf ( "#1 (i:%ld)\n" , i );*/
		Test = BAD;
		return;
	}

	/* test 2 */
	/* samples tests */
	start = i - 45;
	o = 0;
	for (k = 0; k < 15; k++) {
		/* size */
		j =
			(((data[start + 42 + k * 30] << 8) +
				 data[start + 43 +
					k * 30]) * 2);
		/* loop start */
		m =
			(((data[start + 46 + k * 30] << 8) +
				 data[start + 47 +
					k * 30]) * 2);
		/* loop size */
		n =
			(((data[start + 48 + k * 30] << 8) +
				 data[start + 49 +
					k * 30]) * 2);
		/* all sample sizes */
		o += j;

		/* size,loopstart,replen > 64k ? */
		if ((j > 0xFFFF) || (m > 0xFFFF) || (n > 0xFFFF)) {
/*printf ( "#2,0 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		/* replen > size ? */
		if (n > (j + 2)) {
/*printf ( "#2 (Start:%ld) (smp:%ld) (size:%ld) (replen:%ld)\n"
         , start , k+1 , j , n );*/
			Test = BAD;
			return;
		}
		/* loop start > size ? */
		if (m > j) {
/*printf ( "#2,0 (Start:%ld) (smp:%ld) (size:%ld) (lstart:%ld)\n"
         , start , k+1 , j , m );*/
			Test = BAD;
			return;
		}
		/* loop size =0 & loop start != 0 ? */
		if ((m != 0) && (n == 0)) {
/*printf ( "#2,1\n" );*/
			Test = BAD;
			return;
		}
		/* size & loopstart !=0 & size=loopstart ? */
		if ((j != 0) && (j == m)) {
/*printf ( "#2,15\n" );*/
			Test = BAD;
			return;
		}
		/* size =0 & loop start !=0 */
		if ((j == 0) && (m != 0)) {
/*printf ( "#2,2\n" );*/
			Test = BAD;
			return;
		}
	}
	/* all sample sizes < 8 ? */
	if (o < 8) {
/*printf ( "#2,3\n" );*/
		Test = BAD;
		return;
	}

	/* test #3  finetunes & volumes */
	for (k = 0; k < 15; k++) {
		if ((data[start + 44 + k * 30] > 0x0f)
			|| (data[start + 45 + k * 30] >
				0x40)) {
/*printf ( "#3 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
	}

	/* test #4  pattern list size */
	l = data[start + 470];
	if ((l > 127) || (l == 0)) {
/*printf ( "#4,0 (Start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}
	/* l holds the size of the pattern list */
	k = 0;
	for (j = 0; j < l; j++) {
		if (data[start + 472 + j] > k)
			k = data[start + 472 + j];
		if (data[start + 472 + j] > 127) {
/*printf ( "#4,1 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
	}
	/* k holds the highest pattern number */
	/* test last patterns of the pattern list = 0 ? */
	j += 2;		/* found some obscure stk :( */
	while (j != 128) {
		if (data[start + 472 + j] != 0) {
/*printf ( "#4,2 (Start:%ld) (j:%ld) (at:%ld)\n" , start,j ,start+472+j );*/
			Test = BAD;
			return;
		}
		j += 1;
	}
	/* k is the number of pattern in the file (-1) */
	k += 1;


	/* test #5 pattern data ... */
	if (((k * 1024) + 600 + start) > in_size) {
/*printf ( "#5,0 (Start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}
	for (j = 0; j < (k << 8); j++) {
		/* sample > 1f   or   pitch > 358 ? */
		if (data[start + 600 + j * 4] > 0x13) {
/*printf ( "#5.1 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		l =
			((data[start + 600 +
					 j * 4] & 0x0f) << 8) +
			data[start + 601 + j * 4];
		if ((l > 0) && (l < 0x71)) {
/*printf ( "#5,2 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
	}

	Test = GOOD;
}
