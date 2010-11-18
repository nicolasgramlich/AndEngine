package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseBackOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseBackOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseBackOut() {

	}

	public static EaseBackOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseBackOut();
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
		return pMaxValue * ((pSecondsElapsed = pSecondsElapsed / pDuration - 1) * pSecondsElapsed * ((1.70158f + 1) * pSecondsElapsed + 1.70158f) + 1) + pMinValue;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
