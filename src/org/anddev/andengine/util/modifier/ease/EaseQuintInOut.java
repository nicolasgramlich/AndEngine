package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseQuintInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseQuintInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseQuintInOut() {
	}

	public static EaseQuintInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseQuintInOut();
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
		if((pSecondsElapsed /= pDuration * 0.5f) < 1) {
			return pMaxValue * 0.5f * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed + pMinValue;
		}
		return pMaxValue * 0.5f * ((pSecondsElapsed -= 2) * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed + 2) + pMinValue;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
