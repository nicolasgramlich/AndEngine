package org.andengine.util.modifier.ease;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseQuartInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseQuartInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseQuartInOut() {

	}

	public static EaseQuartInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseQuartInOut();
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

		if(percentage < 0.5f) {
			return 0.5f * EaseQuartIn.getValue(2 * percentage);
		} else {
			return 0.5f + 0.5f * EaseQuartOut.getValue(percentage * 2 - 1);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
