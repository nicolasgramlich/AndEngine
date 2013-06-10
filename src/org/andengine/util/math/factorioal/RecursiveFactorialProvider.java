package org.andengine.util.math.factorioal;


/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 21:37:15 - 09.06.2013
 */
public class RecursiveFactorialProvider implements IFactorialProvider {
	// ===========================================================
	// Constants
	// ===========================================================

	private static RecursiveFactorialProvider INSTANCE;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private RecursiveFactorialProvider() {

	}

	public static RecursiveFactorialProvider getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RecursiveFactorialProvider();
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
			return n * this.factorial(n - 1);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}