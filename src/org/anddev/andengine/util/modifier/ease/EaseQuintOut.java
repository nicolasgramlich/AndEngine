package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseQuintOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseQuintOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseQuintOut() {
	}

	public static EaseQuintOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseQuintOut();
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
		return pMaxValue * ((pSecondsElapsed = pSecondsElapsed / pDuration - 1) * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed + 1) + pMinValue;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
