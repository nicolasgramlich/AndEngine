package org.andengine.util.math.factorioal;

import android.util.SparseIntArray;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 21:37:15 - 09.06.2013
 */
public class DenseFactorialCache implements IFactorialProvider {
	// ===========================================================
	// Constants
	// ===========================================================

	private static DenseFactorialCache INSTANCE;

	private final SparseIntArray mCache = new SparseIntArray();

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private DenseFactorialCache() {

	}

	public static DenseFactorialCache getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DenseFactorialCache();
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
		if (n == 0 || n == 1) {
			return 1;
		} else {
			int result = this.mCache.get(n);
			if (result != 0) {
				return result;
			} else {
				result = n * this.factorial(n - 1);
				this.mCache.put(n, result);
				return result;
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}