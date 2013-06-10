package org.andengine.util.math.factorioal;


/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 21:37:15 - 09.06.2013
 */
public class IterativeFactorialProvider implements IFactorialProvider {
	// ===========================================================
	// Constants
	// ===========================================================

	private static IterativeFactorialProvider INSTANCE;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private IterativeFactorialProvider() {

	}

	public static IterativeFactorialProvider getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new IterativeFactorialProvider();
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
		int result = 1;
		for (int i = 1; i <= n; i++) {
			result *= i;
		}
		return result;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}