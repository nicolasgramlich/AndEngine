package org.andengine.util.adt.map;

import java.util.ArrayList;

import org.andengine.util.adt.list.BooleanArrayList;
import org.andengine.util.adt.list.ByteArrayList;
import org.andengine.util.adt.list.CharArrayList;
import org.andengine.util.adt.list.DoubleArrayList;
import org.andengine.util.adt.list.FloatArrayList;
import org.andengine.util.adt.list.IntArrayList;
import org.andengine.util.adt.list.LongArrayList;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:16:35 - 12.04.2012
 */
public final class SparseArrayUtils {
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

	public static final String toString(final SparseBooleanArray pSparseBooleanArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pSparseBooleanArray.size();
		stringBuilder.append('{');
		for (int i = 0; i < size; i++) {
			stringBuilder.append(pSparseBooleanArray.keyAt(i)).append('=').append(pSparseBooleanArray.valueAt(i));
			if (i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append('}');

		return stringBuilder.toString();
	}

	public static final String toString(final SparseByteArray pSparseByteArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pSparseByteArray.size();
		stringBuilder.append('{');
		for (int i = 0; i < size; i++) {
			stringBuilder.append(pSparseByteArray.keyAt(i)).append('=').append(pSparseByteArray.valueAt(i));
			if (i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append('}');

		return stringBuilder.toString();
	}

	public static final String toString(final SparseCharArray pSparseCharArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pSparseCharArray.size();
		stringBuilder.append('{');
		for (int i = 0; i < size; i++) {
			stringBuilder.append(pSparseCharArray.keyAt(i)).append('=').append(pSparseCharArray.valueAt(i));
			if (i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append('}');

		return stringBuilder.toString();
	}

	public static final String toString(final SparseIntArray pSparseIntArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pSparseIntArray.size();
		stringBuilder.append('{');
		for (int i = 0; i < size; i++) {
			stringBuilder.append(pSparseIntArray.keyAt(i)).append('=').append(pSparseIntArray.valueAt(i));
			if (i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append('}');

		return stringBuilder.toString();
	}

	public static final String toString(final SparseLongArray pSparseLongArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pSparseLongArray.size();
		stringBuilder.append('{');
		for (int i = 0; i < size; i++) {
			stringBuilder.append(pSparseLongArray.keyAt(i)).append('=').append(pSparseLongArray.valueAt(i));
			if (i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append('}');

		return stringBuilder.toString();
	}

	public static final String toString(final SparseFloatArray pSparseFloatArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pSparseFloatArray.size();
		stringBuilder.append('{');
		for (int i = 0; i < size; i++) {
			stringBuilder.append(pSparseFloatArray.keyAt(i)).append('=').append(pSparseFloatArray.valueAt(i));
			if (i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append('}');

		return stringBuilder.toString();
	}

	public static final String toString(final SparseDoubleArray pSparseDoubleArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pSparseDoubleArray.size();
		stringBuilder.append('{');
		for (int i = 0; i < size; i++) {
			stringBuilder.append(pSparseDoubleArray.keyAt(i)).append('=').append(pSparseDoubleArray.valueAt(i));
			if (i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append('}');

		return stringBuilder.toString();
	}

	public static final String toString(final SparseArray<?> pSparseArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pSparseArray.size();
		stringBuilder.append('{');
		for (int i = 0; i < size; i++) {
			stringBuilder.append(pSparseArray.keyAt(i)).append('=').append(pSparseArray.valueAt(i));
			if (i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append('}');

		return stringBuilder.toString();
	}

	public static final String toString(final LongSparseArray<?> pLongSparseArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pLongSparseArray.size();
		stringBuilder.append('{');
		for (int i = 0; i < size; i++) {
			stringBuilder.append(pLongSparseArray.keyAt(i)).append('=').append(pLongSparseArray.valueAt(i));
			if (i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append('}');

		return stringBuilder.toString();
	}

	public static final BooleanArrayList valuesToList(final SparseBooleanArray pSparseBooleanArray) {
		final int size = pSparseBooleanArray.size();
		final BooleanArrayList result = new BooleanArrayList(size);

		for (int i = 0; i < size; i++) {
			result.add(pSparseBooleanArray.valueAt(i));
		}

		return result;
	}

	public static final ByteArrayList valuesToList(final SparseByteArray pSparseByteArray) {
		final int size = pSparseByteArray.size();
		final ByteArrayList result = new ByteArrayList(size);

		for (int i = 0; i < size; i++) {
			result.add(pSparseByteArray.valueAt(i));
		}

		return result;
	}

	public static final CharArrayList valuesToList(final SparseCharArray pSparseCharArray) {
		final int size = pSparseCharArray.size();
		final CharArrayList result = new CharArrayList(size);

		for (int i = 0; i < size; i++) {
			result.add(pSparseCharArray.valueAt(i));
		}

		return result;
	}

	public static final IntArrayList valuesToList(final SparseIntArray pSparseIntArray) {
		final int size = pSparseIntArray.size();
		final IntArrayList result = new IntArrayList(size);

		for (int i = 0; i < size; i++) {
			result.add(pSparseIntArray.valueAt(i));
		}

		return result;
	}

	public static final LongArrayList valuesToList(final SparseLongArray pSparseLongArray) {
		final int size = pSparseLongArray.size();
		final LongArrayList result = new LongArrayList(size);

		for (int i = 0; i < size; i++) {
			result.add(pSparseLongArray.valueAt(i));
		}

		return result;
	}

	public static final FloatArrayList valuesToList(final SparseFloatArray pSparseFloatArray) {
		final int size = pSparseFloatArray.size();
		final FloatArrayList result = new FloatArrayList(size);

		for (int i = 0; i < size; i++) {
			result.add(pSparseFloatArray.valueAt(i));
		}

		return result;
	}

	public static final DoubleArrayList valuesToList(final SparseDoubleArray pSparseDoubleArray) {
		final int size = pSparseDoubleArray.size();
		final DoubleArrayList result = new DoubleArrayList(size);

		for (int i = 0; i < size; i++) {
			result.add(pSparseDoubleArray.valueAt(i));
		}

		return result;
	}

	public static final <T> ArrayList<T> valuesToList(final SparseArray<T> pSparseArray) {
		final int size = pSparseArray.size();
		final ArrayList<T> result = new ArrayList<T>(size);

		for (int i = 0; i < size; i++) {
			result.add(pSparseArray.valueAt(i));
		}

		return result;
	}

	public static final <T> ArrayList<T> valuesToList(final LongSparseArray<T> pSparseLongArray) {
		final int size = pSparseLongArray.size();
		final ArrayList<T> result = new ArrayList<T>(size);

		for (int i = 0; i < size; i++) {
			result.add(pSparseLongArray.valueAt(i));
		}

		return result;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
