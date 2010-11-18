package org.anddev.andengine.util.modifier.ease;

import android.util.FloatMath;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseCircularInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseCircularInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseCircularInOut() {
	}

	public static EaseCircularInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseCircularInOut();
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
		if((pSecondsElapsed /= pDuration * 0.5) < 1) {
			return (float) (-pMaxValue * 0.5 * (FloatMath.sqrt(1 - pSecondsElapsed * pSecondsElapsed) - 1) + pMinValue);
		}

		return (float) (pMaxValue * 0.5 * (FloatMath.sqrt(1 - (pSecondsElapsed -= 2) * pSecondsElapsed) + 1) + pMinValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
