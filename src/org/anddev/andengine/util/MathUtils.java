package org.anddev.andengine.util;

/**
 * @author Nicolas Gramlich
 * @since 20:42:15 - 17.12.2009
 */
public class MathUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final double LOG_2 = Math.log(2);

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static boolean isPowerOfTwo(final int n) {
		return ((n != 0) && (n & (n - 1)) == 0);
	}

	public static int nextPowerOfTwo(final int n) {
		int k = n;

		if (k == 0) {
			return 1;
		}

		k--;

		for (int i = 1; i < 32; i <<= 1) {
			k = k | k >> i;
		}

		return k + 1;
	}

	public static int sum(final int[] pValues) {
		int sum = 0;
		for(int i = pValues.length - 1; i >= 0; i--) {
			sum += pValues[i];
		}

		return sum;
	}

	public static void arraySumInternal(final int[] pValues) {
		for(int i = 1; i < pValues.length; i++) {
			pValues[i] = pValues[i-1] + pValues[i];
		}
	}

	public static void arraySumInternal(final long[] pValues) {
		for(int i = 1; i < pValues.length; i++) {
			pValues[i] = pValues[i-1] + pValues[i];
		}
	}

	public static void arraySumInternal(final long[] pValues, final long pFactor) {
		pValues[0] = pValues[0] * pFactor;
		for(int i = 1; i < pValues.length; i++) {
			pValues[i] = pValues[i-1] + pValues[i] * pFactor;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
