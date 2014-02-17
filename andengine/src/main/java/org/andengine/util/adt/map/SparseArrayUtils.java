package org.andengine.util.adt.map;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

/**
 * (c) Zynga 2012
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

	public static final String toString(final SparseArray<?> pSparseArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pSparseArray.size();
		stringBuilder.append("{");
		for(int i = 0; i < size; i++) {
			stringBuilder.append(pSparseArray.keyAt(i)).append("=").append(pSparseArray.valueAt(i));
			if(i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append("}");

		return stringBuilder.toString();
	}

	public static final String toString(final SparseIntArray pSparseIntArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pSparseIntArray.size();
		stringBuilder.append("{");
		for(int i = 0; i < size; i++) {
			stringBuilder.append(pSparseIntArray.keyAt(i)).append("=").append(pSparseIntArray.valueAt(i));
			if(i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append("}");

		return stringBuilder.toString();
	}

	public static final String toString(final SparseBooleanArray pSparseBooleanArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pSparseBooleanArray.size();
		stringBuilder.append("{");
		for(int i = 0; i < size; i++) {
			stringBuilder.append(pSparseBooleanArray.keyAt(i)).append("=").append(pSparseBooleanArray.valueAt(i));
			if(i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append("}");

		return stringBuilder.toString();
	}

	public static final String toString(final LongSparseArray<?> pLongSparseArray) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int size = pLongSparseArray.size();
		stringBuilder.append("{");
		for(int i = 0; i < size; i++) {
			stringBuilder.append(pLongSparseArray.keyAt(i)).append("=").append(pLongSparseArray.valueAt(i));
			if(i < (size - 1)) {
				stringBuilder.append(", ");
			}
		}
		stringBuilder.append("}");

		return stringBuilder.toString();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
