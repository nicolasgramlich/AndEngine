package org.anddev.andengine.entity.shape.modifier.ease;

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
	public float calc(float t, final float b, final float c, final float d) {
		return (c * FloatMath.sqrt(1 - (t = t / d - 1) * t) + b);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
