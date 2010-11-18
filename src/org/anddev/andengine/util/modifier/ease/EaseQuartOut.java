package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseQuartOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseQuartOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseQuartOut() {
	}

	public static EaseQuartOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseQuartOut();
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
		return -pMaxValue * ((pSecondsElapsed = pSecondsElapsed / pDuration - 1) * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed - 1) + pMinValue;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
