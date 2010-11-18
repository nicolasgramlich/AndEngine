package org.anddev.andengine.util.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseBounceInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseBounceInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseBounceInOut() {

	}

	public static EaseBounceInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseBounceInOut();
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
		if(pSecondsElapsed < pDuration * 0.5) {
			return EaseBounceIn.getInstance().getPercentageDone(pSecondsElapsed * 2, pDuration, 0, pMaxValue) * 0.5f + pMinValue;
		} else {
			return EaseBounceOut.getInstance().getPercentageDone(pSecondsElapsed * 2 - pDuration, pDuration, 0, pMaxValue) * 0.5f + pMaxValue * 0.5f + pMinValue;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
