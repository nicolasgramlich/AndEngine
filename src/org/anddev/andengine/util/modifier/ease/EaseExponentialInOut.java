package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
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
		if(INSTANCE == null) {
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
	public float getPercentageDone(float pSecondsElapsed, final float pDuration, final float pMinValue, final float pMaxValue) {
		if(pSecondsElapsed == 0) {
			return pMinValue;
		}
		if(pSecondsElapsed == pDuration) {
			return pMinValue + pMaxValue;
		}
		if((pSecondsElapsed /= pDuration * 0.5f) < 1) {
			return (float) (pMaxValue * 0.5f * Math.pow(2, 10 * (pSecondsElapsed - 1)) + pMinValue);
		}
		return (float) (pMaxValue * 0.5f * (-Math.pow(2, -10 * --pSecondsElapsed) + 2) + pMinValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
