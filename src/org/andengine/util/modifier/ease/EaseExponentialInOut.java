package org.andengine.util.modifier.ease;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseExponentialInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseExponentialInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseExponentialInOut() {

	}

	public static EaseExponentialInOut getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EaseExponentialInOut();
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
		final float percentage = pSecondsElapsed / pDuration;

		if (percentage < 0.5f) {
			return 0.5f * EaseExponentialIn.getValue(2 * percentage);
		} else {
			return 0.5f + 0.5f * EaseExponentialOut.getValue(percentage * 2 - 1);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
