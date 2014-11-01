package org.andengine.util.adt.array;

import java.util.List;

import org.andengine.util.math.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:35:42 - 01.05.2011
 */
public final class ArrayUtils {
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

	public static final byte random(final byte[] pArray) {
		return pArray[MathUtils.random(0, pArray.length - 1)];
	}

	public static final short random(final short[] pArray) {
		return pArray[MathUtils.random(0, pArray.length - 1)];
	}

	public static final int random(final int[] pArray) {
		return pArray[MathUtils.random(0, pArray.length - 1)];
	}

	public static final long random(final long[] pArray) {
		return pArray[MathUtils.random(0, pArray.length - 1)];
	}

	public static final float random(final float[] pArray) {
		return pArray[MathUtils.random(0, pArray.length - 1)];
	}

	public static final double random(final double[] pArray) {
		return pArray[MathUtils.random(0, pArray.length - 1)];
	}

	public static final <T> T random(final T[] pArray) {
		return pArray[MathUtils.random(0, pArray.length - 1)];
	}

	public static final void reverse(final byte[] pArray) {
		if(pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		byte tmp;
		while(j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static final void reverse(final short[] pArray) {
		if(pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		short tmp;
		while(j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static final void reverse(final int[] pArray) {
		if(pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		int tmp;
		while(j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static final void reverse(final long[] pArray) {
		if(pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		long tmp;
		while(j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static final void reverse(final float[] pArray) {
		if(pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		float tmp;
		while(j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static final void reverse(final double[] pArray) {
		if(pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		double tmp;
		while(j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static final void reverse(final Object[] pArray) {
		if(pArray == null) {
			return;
		}
		int i = 0;
		int j = pArray.length - 1;
		Object tmp;
		while(j > i) {
			tmp = pArray[j];
			pArray[j] = pArray[i];
			pArray[i] = tmp;
			j--;
			i++;
		}
	}

	public static final boolean equals(final byte[] pArrayA, final int pOffsetA, final byte[] pArrayB, final int pOffsetB, final int pLength) {
		final int lastIndexA = pOffsetA + pLength;
		if(lastIndexA > pArrayA.length) {
			throw new ArrayIndexOutOfBoundsException(pArrayA.length);
		}

		final int lastIndexB = pOffsetB + pLength;
		if(lastIndexB > pArrayB.length) {
			throw new ArrayIndexOutOfBoundsException(pArrayB.length);
		}

		for(int a = pOffsetA, b = pOffsetB; a < lastIndexA; a++, b++) {
			if(pArrayA[a] != pArrayB[b]) {
				return false;
			}
		}

		return true;
	}

	public static final byte[] toByteArray(final List<Byte> pItems) {
		final byte[] out = new byte[pItems.size()];
		for(int i = out.length - 1; i >= 0; i--) {
			out[i] = pItems.get(i);
		}
		return out;
	}

	public static final char[] toCharArray(final List<Character> pItems) {
		final char[] out = new char[pItems.size()];
		for(int i = out.length - 1; i >= 0; i--) {
			out[i] = pItems.get(i);
		}
		return out;
	}

	public static final short[] toShortArray(final List<Short> pItems) {
		final short[] out = new short[pItems.size()];
		for(int i = out.length - 1; i >= 0; i--) {
			out[i] = pItems.get(i);
		}
		return out;
	}

	public static final int[] toIntArray(final List<Integer> pItems) {
		final int[] out = new int[pItems.size()];
		for(int i = out.length - 1; i >= 0; i--) {
			out[i] = pItems.get(i);
		}
		return out;
	}

	public static final long[] toLongArray(final List<Long> pItems) {
		final long[] out = new long[pItems.size()];
		for(int i = out.length - 1; i >= 0; i--) {
			out[i] = pItems.get(i);
		}
		return out;
	}

	public static final float[] toFloatArray(final List<Float> pItems) {
		final float[] out = new float[pItems.size()];
		for(int i = out.length - 1; i >= 0; i--) {
			out[i] = pItems.get(i);
		}
		return out;
	}

	public static final double[] toDoubleArray(final List<Double> pItems) {
		final double[] out = new double[pItems.size()];
		for(int i = out.length - 1; i >= 0; i--) {
			out[i] = pItems.get(i);
		}
		return out;
	}

	public static final boolean contains(final byte[] pItems, final byte pItem) {
		for(int i = pItems.length - 1; i >= 0; i--) {
			if(pItems[i] == pItem) {
				return true;
			}
		}
		return false;
	}

	public static final boolean contains(final char[] pItems, final char pItem) {
		for(int i = pItems.length - 1; i >= 0; i--) {
			if(pItems[i] == pItem) {
				return true;
			}
		}
		return false;
	}

	public static final boolean contains(final short[] pItems, final short pItem) {
		for(int i = pItems.length - 1; i >= 0; i--) {
			if(pItems[i] == pItem) {
				return true;
			}
		}
		return false;
	}

	public static final boolean contains(final int[] pItems, final int pItem) {
		for(int i = pItems.length - 1; i >= 0; i--) {
			if(pItems[i] == pItem) {
				return true;
			}
		}
		return false;
	}

	public static final boolean contains(final long[] pItems, final long pItem) {
		for(int i = pItems.length - 1; i >= 0; i--) {
			if(pItems[i] == pItem) {
				return true;
			}
		}
		return false;
	}

	public static final boolean contains(final float[] pItems, final float pItem) {
		for(int i = pItems.length - 1; i >= 0; i--) {
			if(pItems[i] == pItem) {
				return true;
			}
		}
		return false;
	}

	public static final boolean contains(final double[] pItems, final double pItem) {
		for(int i = pItems.length - 1; i >= 0; i--) {
			if(pItems[i] == pItem) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param pClass the type of the returned array T[].
	 * @param pArrays items or pArrays itself can be null.
	 * @return <code>null</code> when pArrays is <code>null</code> or all arrays in pArrays are <code>null</code> or of length zero. Otherwise an in-order joined array of <code>T[]</code> of all not null, not zero length arrays in pArrays.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] join(final Class<T> pClass, final T[]... pArrays) {
		if(pArrays == null) {
			return null;
		}

		final int arrayCount = pArrays.length;
		if(arrayCount == 0) {
			return null;
		} else if(arrayCount == 1) {
			return pArrays[0];
		}

		int resultLength = 0;
		/* Determine length of result. */
		for(int i = pArrays.length - 1; i >= 0; i--) {
			final T[] array = pArrays[i];
			if((array != null) && (array.length > 0)) {
				resultLength += array.length;
			}
		}

		if(resultLength == 0) {
			return null;
		}

		/* Determine length of result. */
		final T[] result = (T[]) java.lang.reflect.Array.newInstance(pClass.getComponentType(), resultLength);
		int offset = 0;
		for(int i = 0; i < arrayCount; i++) {
			final T[] array = pArrays[i];
			if((array != null) && (array.length > 0)) {
				System.arraycopy(array, 0, result, offset, array.length);
				offset += array.length;
			}
		}
		return result;
	}

	public static int idealByteArraySize(final int pSize) {
		for(int i = 4; i < 32; i++) {
			if(pSize <= ((1 << i) - 12)) {
				return (1 << i) - 12;
			}
		}

		return pSize;
	}

	public static int idealBooleanArraySize(final int pSize) {
		return ArrayUtils.idealByteArraySize(pSize);
	}

	public static int idealShortArraySize(final int pSize) {
		return ArrayUtils.idealByteArraySize(pSize << 1) >> 1;
	}

	public static int idealCharArraySize(final int pSize) {
		return ArrayUtils.idealByteArraySize(pSize << 1) >> 1;
	}

	public static int idealIntArraySize(final int pSize) {
		return ArrayUtils.idealByteArraySize(pSize << 2) >> 2;
	}

	public static int idealFloatArraySize(final int pSize) {
		return ArrayUtils.idealByteArraySize(pSize << 2) >> 2;
	}

	public static int idealDoubleArraySize(final int pSize) {
		return ArrayUtils.idealByteArraySize(pSize << 3) >> 3;
	}

	public static int idealLongArraySize(final int pSize) {
		return ArrayUtils.idealByteArraySize(pSize << 3) >> 3;
	}

	public static int idealObjectArraySize(final int pSize) {
		return ArrayUtils.idealByteArraySize(pSize << 2) >> 2;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
