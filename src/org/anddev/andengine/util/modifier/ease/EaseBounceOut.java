package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseBounceOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseBounceOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseBounceOut() {

	}

	public static EaseBounceOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseBounceOut();
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
		if((pSecondsElapsed /= pDuration) < (1 / 2.75)) {
			return pMaxValue * (7.5625f * pSecondsElapsed * pSecondsElapsed) + pMinValue;
		} else if(pSecondsElapsed < (2 / 2.75)) {
			return pMaxValue * (7.5625f * (pSecondsElapsed -= (1.5f / 2.75f)) * pSecondsElapsed + 0.75f) + pMinValue;
		} else if(pSecondsElapsed < (2.5 / 2.75)) {
			return pMaxValue * (7.5625f * (pSecondsElapsed -= (2.25f / 2.75f)) * pSecondsElapsed + 0.9375f) + pMinValue;
		} else {
			return pMaxValue * (7.5625f * (pSecondsElapsed -= (2.625f / 2.75f)) * pSecondsElapsed + 0.984375f) + pMinValue;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
