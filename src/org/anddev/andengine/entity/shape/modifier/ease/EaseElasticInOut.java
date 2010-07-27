package org.anddev.andengine.entity.shape.modifier.ease;

import org.anddev.andengine.util.constants.MathConstants;

import android.util.FloatMath;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseElasticInOut implements IEaseFunction, MathConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private static EaseElasticInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseElasticInOut() {
	}

	public static EaseElasticInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseElasticInOut();
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
		if((t /= d * 0.5) == 2) {
			return b + c;
		}
		if(p == 0) {
			p = d * (0.3f * 1.5f);
		}
		if(a == 0 || (c > 0 && a < c) || (c < 0 && a < -c)) {
			a = c;
			s = p / 4;
		} else {
			s = (float) (p / _2PI * Math.asin(c / a));
		}
		if(t < 1) {
			return (float) (-0.5 * (a * Math.pow(2, 10 * (t -= 1)) * FloatMath.sin((t * d - s) * _2PI / p)) + b);
		}
		return (float) (a * Math.pow(2, -10 * (t -= 1)) * FloatMath.sin((t * d - s) * _2PI / p) * .5 + c + b);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
