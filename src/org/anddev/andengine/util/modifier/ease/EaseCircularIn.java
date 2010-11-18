package org.anddev.andengine.util.modifier.ease;

import android.util.FloatMath;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseCircularIn implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseCircularIn INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseCircularIn() {
	}

	public static EaseCircularIn getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseCircularIn();
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
		return (-pMaxValue * (FloatMath.sqrt(1 - (pSecondsElapsed /= pDuration) * pSecondsElapsed) - 1.0f) + pMinValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
