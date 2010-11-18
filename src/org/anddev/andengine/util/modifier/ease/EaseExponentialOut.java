package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseExponentialOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseExponentialOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseExponentialOut() {
	}

	public static EaseExponentialOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseExponentialOut();
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
		return (float) ((pSecondsElapsed == pDuration) ? pMinValue + pMaxValue : pMaxValue * (-Math.pow(2, -10 * pSecondsElapsed / pDuration) + 1) + pMinValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
