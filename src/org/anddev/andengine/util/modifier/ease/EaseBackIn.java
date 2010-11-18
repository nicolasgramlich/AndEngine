package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseBackIn implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseBackIn INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseBackIn() {

	}

	public static EaseBackIn getInstance() {
		if(null == INSTANCE) {
			INSTANCE = new EaseBackIn();
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
	public float getPercentageDone(float pSecondsElapsed, final float pDuration, final float pMinValue, final float pMaxValue) {
		return pMaxValue * (pSecondsElapsed /= pDuration) * pSecondsElapsed * ((1.70158f + 1) * pSecondsElapsed - 1.70158f) + pMinValue;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
