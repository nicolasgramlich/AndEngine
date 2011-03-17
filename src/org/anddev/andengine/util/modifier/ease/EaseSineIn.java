package org.anddev.andengine.util.modifier.ease;

import org.anddev.andengine.util.constants.MathConstants;

import android.util.FloatMath;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseSineIn implements IEaseFunction, MathConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseSineIn INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseSineIn() {
	}

	public static EaseSineIn getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseSineIn();
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
		return (-pMaxValue * FloatMath.cos(pSecondsElapsed / pDuration * PI_HALF) + pMaxValue + pMinValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
