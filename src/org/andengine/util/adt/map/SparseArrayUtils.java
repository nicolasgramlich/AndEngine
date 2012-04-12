package org.andengine.util.adt.map;

import android.util.SparseArray;

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
			if(i < size - 1) {
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
