package org.andengine.util.math.factorioal;

import android.util.SparseIntArray;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 21:37:15 - 09.06.2013
 */
public class SparseFactorialCache implements IFactorialProvider {
	// ===========================================================
	// Constants
	// ===========================================================

	private static SparseFactorialCache INSTANCE;

	private final SparseIntArray mCache = new SparseIntArray();

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private SparseFactorialCache() {

	}

	public static SparseFactorialCache getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SparseFactorialCache();
		}
		return INSTANCE;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public int factorial(final int n) {
		int result = this.mCache.get(n);
		if (result != 0) {
			return result;
		} else {
			result = IterativeFactorialProvider.getInstance().factorial(n);
			this.mCache.put(n, result);
			return result;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}