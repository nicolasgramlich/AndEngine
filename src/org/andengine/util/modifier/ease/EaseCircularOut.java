package org.andengine.util.modifier.ease;

import android.util.FloatMath;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
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
	public float getPercentage(final float pSecondsElapsed, final float pDuration) {
		return EaseCircularOut.getValue(pSecondsElapsed / pDuration);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static float getValue(final float pPercentage) {
		final float t = pPercentage - 1;
		return FloatMath.sqrt(1 - t * t);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
