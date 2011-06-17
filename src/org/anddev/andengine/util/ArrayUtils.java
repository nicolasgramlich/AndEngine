package org.anddev.andengine.util;

/**
 * @author Nicolas Gramlich
 * @since 22:35:42 - 01.05.2011
 */
public class ArrayUtils {
	// ===========================================================
	// Constants
	// ===========================================================

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

	public static <T> T random(final T[] pArray) {
		return pArray[MathUtils.random(0, pArray.length - 1)];
	}
	
	public static void reverse(final byte[] pArray) {
		if (pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		byte tmp;
		while (j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static void reverse(final short[] pArray) {
		if (pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		short tmp;
		while (j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static void reverse(final int[] pArray) {
		if (pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		int tmp;
		while (j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static void reverse(final long[] pArray) {
		if (pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		long tmp;
		while (j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static void reverse(final float[] pArray) {
		if (pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		float tmp;
		while (j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static void reverse(final double[] pArray) {
		if (pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		double tmp;
		while (j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static void reverse(final Object[] pArray) {
		if (pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		Object tmp;
		while (j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
}
