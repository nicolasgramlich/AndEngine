package org.andengine.util.modifier.ease;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseExponentialOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseExponentialOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseExponentialOut() {

	}

	public static EaseExponentialOut getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EaseExponentialOut();
		}
		return INSTANCE;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getPercentage(final float pSecondsElapsed, final float pDuration) {
		return EaseExponentialOut.getValue(pSecondsElapsed / pDuration);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static float getValue(final float pPercentage) {
		return (pPercentage == 1) ? 1 : (-(float) Math.pow(2, -10 * pPercentage) + 1);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
