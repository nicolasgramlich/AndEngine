package org.anddev.andengine.entity.shape.modifier.ease;

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
	public float calc(float t, final float b, final float c, final float d) {
		if((t /= d * 0.5) < 1) {
			return (float) (-c * 0.5 * (FloatMath.sqrt(1 - t * t) - 1) + b);
		}

		return (float) (c * 0.5 * (FloatMath.sqrt(1 - (t -= 2) * t) + 1) + b);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
