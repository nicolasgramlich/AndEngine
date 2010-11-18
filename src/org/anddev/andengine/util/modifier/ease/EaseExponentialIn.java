package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseExponentialIn implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseExponentialIn INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseExponentialIn() {
	}

	public static EaseExponentialIn getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseExponentialIn();
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
	public float getPercentageDone(final float pSecondsElapsed, final float pDuration, final float pMinValue, final float pMaxValue) {
		return (float) ((pSecondsElapsed == 0) ? pMinValue : pMaxValue * Math.pow(2, 10 * (pSecondsElapsed / pDuration - 1)) + pMinValue - pMaxValue * 0.001f);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
