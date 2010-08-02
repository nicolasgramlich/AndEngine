package org.anddev.andengine.entity.shape.modifier.ease;

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
	public float calc(float t, final float b, final float c, final float d) {
		return (-c * (FloatMath.sqrt(1 - (t /= d) * t) - 1.0f) + b);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
