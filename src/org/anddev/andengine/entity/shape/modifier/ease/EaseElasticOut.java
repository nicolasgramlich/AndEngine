package org.anddev.andengine.entity.shape.modifier.ease;

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
	public float calc(float t, final float b, final float c, final float d) {
		float s;
		float p = 0.0f;
		float a = 0.0f;
		if(t == 0) {
			return b;
		}
		if((t /= d) == 1) {
			return b + c;
		}
		if(p == 0) {
			p = d * 0.3f;
		}
		if(a == 0 || (c > 0 && a < c) || (c < 0 && a < -c)) {
			a = c;
			s = p / 4;
		} else {
			s = (float) (p / _2PI * Math.asin(c / a));
		}
		return (float) (a * Math.pow(2, -10 * t) * FloatMath.sin((t * d - s) * _2PI / p) + c + b);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
