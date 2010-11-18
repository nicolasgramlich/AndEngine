package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseBackInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseBackInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseBackInOut() {

	}

	public static EaseBackInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseBackInOut();
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
		float s = 1.70158f;
		if((pSecondsElapsed /= pDuration * 0.5f) < 1) {
			return pMaxValue * 0.5f * (pSecondsElapsed * pSecondsElapsed * (((s *= (1.525f)) + 1) * pSecondsElapsed - s)) + pMinValue;
		}

		return pMaxValue / 2 * ((pSecondsElapsed -= 2) * pSecondsElapsed * (((s *= (1.525f)) + 1) * pSecondsElapsed + s) + 2) + pMinValue;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
