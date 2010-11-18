package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseCubicInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseCubicInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseCubicInOut() {
	}

	public static EaseCubicInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseCubicInOut();
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
			return pMaxValue * 0.5f * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed + pMinValue;
		}
		return pMaxValue * 0.5f * ((pSecondsElapsed -= 2) * pSecondsElapsed * pSecondsElapsed + 2) + pMinValue;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
