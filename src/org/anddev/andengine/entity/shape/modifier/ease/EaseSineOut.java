package org.anddev.andengine.entity.shape.modifier.ease;

import org.anddev.andengine.util.constants.MathConstants;

import android.util.FloatMath;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseSineOut implements IEaseFunction, MathConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private static EaseSineOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseSineOut() {
	}

	public static EaseSineOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseSineOut();
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
	public float calc(final float t, final float b, final float c, final float d) {
		return (float) (c * FloatMath.sin(t / d * _HALF_PI) + b);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
