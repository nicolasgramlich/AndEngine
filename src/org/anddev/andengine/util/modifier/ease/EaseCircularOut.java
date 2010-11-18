package org.anddev.andengine.util.modifier.ease;

import android.util.FloatMath;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseCircularOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseCircularOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseCircularOut() {
	}

	public static EaseCircularOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseCircularOut();
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
		return (pMaxValue * FloatMath.sqrt(1 - (pSecondsElapsed = pSecondsElapsed / pDuration - 1) * pSecondsElapsed) + pMinValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
