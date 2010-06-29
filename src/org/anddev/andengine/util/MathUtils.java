package org.anddev.andengine.util;

import java.util.Random;

/**
 * @author Nicolas Gramlich
 * @since 20:42:15 - 17.12.2009
 */
public class MathUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static Random RANDOM = new Random(System.nanoTime());

	public static final float PI = (float) Math.PI;
	public static final float DEG_TO_RAD = PI / 180.0f;
	public static final float RAD_TO_DEG = 180.0f / PI;

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

	public static float radToDeg(final float pRad) {
		return RAD_TO_DEG * pRad;
	}

	public static float degToRad(final float pDegree) {
		return DEG_TO_RAD * pDegree;
	}

	public static float random(final float pMin, final float pMax) {
		return pMin + RANDOM.nextFloat() * (pMax - pMin);
	}

	public static int random(final int pMin, final int pMax) {
		return pMin + RANDOM.nextInt(pMax - pMin + 1);
	}

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
		final int valueCount = pValues.length;
		for(int i = 1; i < valueCount; i++) {
			pValues[i] = pValues[i-1] + pValues[i];
		}
	}

	public static void arraySumInternal(final long[] pValues) {
		final int valueCount = pValues.length;
		for(int i = 1; i < valueCount; i++) {
			pValues[i] = pValues[i-1] + pValues[i];
		}
	}

	public static void arraySumInternal(final long[] pValues, final long pFactor) {
		pValues[0] = pValues[0] * pFactor;
		final int valueCount = pValues.length;
		for(int i = 1; i < valueCount; i++) {
			pValues[i] = pValues[i-1] + pValues[i] * pFactor;
		}
	}

	public static float arraySum(final float[] pValues) {
		float sum = 0;
		final int valueCount = pValues.length;
		for(int i = 0; i < valueCount; i++) {
			sum += pValues[i];
		}
		return sum;
	}

	public static float arrayAverage(final float[] pValues) {
		return arraySum(pValues) / pValues.length;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
