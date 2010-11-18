package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseQuintIn implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseQuintIn INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseQuintIn() {
	}

	public static EaseQuintIn getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseQuintIn();
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
		return pMaxValue * (pSecondsElapsed /= pDuration) * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed + pMinValue;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
