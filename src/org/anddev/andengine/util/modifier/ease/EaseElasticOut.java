package org.anddev.andengine.util.modifier.ease;

import org.anddev.andengine.util.constants.MathConstants;

import android.util.FloatMath;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseElasticOut implements IEaseFunction, MathConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseElasticOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseElasticOut() {
	}

	public static EaseElasticOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseElasticOut();
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
		float s;
		float p = 0.0f;
		float a = 0.0f;
		if(pSecondsElapsed == 0) {
			return pMinValue;
		}
		if((pSecondsElapsed /= pDuration) == 1) {
			return pMinValue + pMaxValue;
		}
		if(p == 0) {
			p = pDuration * 0.3f;
		}
		if(a == 0 || (pMaxValue > 0 && a < pMaxValue) || (pMaxValue < 0 && a < -pMaxValue)) {
			a = pMaxValue;
			s = p / 4;
		} else {
			s = (float) (p / PI_TWICE * Math.asin(pMaxValue / a));
		}
		return (float) (a * Math.pow(2, -10 * pSecondsElapsed) * FloatMath.sin((pSecondsElapsed * pDuration - s) * PI_TWICE / p) + pMaxValue + pMinValue);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
