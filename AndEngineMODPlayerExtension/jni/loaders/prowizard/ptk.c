
#include <string.h>
#include <stdlib.h>

void testPTK (void)
{
	/* test 1 */
	if (i < 1080) {
/*printf ( "#1 (i:%ld)\n" , i );*/
		Test = BAD;
		return;
	}

	/* test 2 */
	start = i - 1080;
	for (k = 0; k < 31; k++) {
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

		/* size = 0 & loop start != 0 */
		if ((j == 0) && (m != 0)) {
/*printf ( "#2 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		/* loop size =0 & loop start != 0 ? */
		if ((m != 0) && (n == 0)) {
/*printf ( "#2,1\n" );*/
			Test = BAD;
			return;
		}
		/* loop size > size ? */
		if ((j + 2) < n) {
/*printf ( "#2,2 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}

	}

	/* test #3  finetunes & volumes */
	for (k = 0; k < 31; k++) {
		if ((data[start + 44 + k * 30] > 0x0f)
			|| (data[start + 45 + k * 30] >
				0x40)) {
/*printf ( "#3 (Start:%ld)(smp:%ld)(Fine:%d)(vol:%d)\n"
         ,start
         ,k
         ,data[start+44+k*30]
         ,data[start+45+k*30] );*/
			Test = BAD;
			return;
		}
	}

	/* test #4  pattern list size */
	l = data[start + 950];
	if ((l > 127) || (l == 0)) {
/*printf ( "#4,0 (Start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}
	/* l holds the size of the pattern list */
	k = 0;
	for (j = 0; j < 128; j++) {
		if (data[start + 952 + j] > k)
			k = data[start + 952 + j];
		if (data[start + 952 + j] > 127) {
/*printf ( "#4,1 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
	}
	/* k holds the highest pattern number */
	/* test last patterns of the pattern list = 0 ? */
	j += 2;		/* found some obscure ptk :( */
	while (j < 128) {
		if (data[start + 952 + j] > 0x7f) {
/*printf ( "#4,2 (Start:%ld) (j:%ld) (at:%ld)\n" , start,j ,start+952+j );*/
			Test = BAD;
			return;
		}
		j += 1;
	}
	/* k is the number of pattern in the file (-1) */
	k += 1;


	/* test #5 pattern data ... */
	if (((k * 1024) + 1084 + start) > in_size) {
/*printf ( "#5,0 (Start:%ld)\n" , start );*/
		Test = BAD;
		return;
	}
	for (j = 0; j < (k << 8); j++) {
		/* sample > 1f   or   pitch > 358 ? */
		if (data[start + 1084 + j * 4] > 0x13) {
/*printf ( "#5.1 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
		l =
			((data[start + 1084 +
					 j * 4] & 0x0f) << 8) +
			data[start + 1085 + j * 4];
		if ((l > 0) && (l < 0x1C)) {
/*printf ( "#5,2 (Start:%ld)\n" , start );*/
			Test = BAD;
			return;
		}
	}

	Test = GOOD;
}
