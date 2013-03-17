package org.andengine.util.modifier.ease;

import org.andengine.util.math.MathConstants;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseSineIn implements IEaseFunction {
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
		if (INSTANCE == null) {
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
	public float getPercentage(final float pSecondsElapsed, final float pDuration) {
		return EaseSineIn.getValue(pSecondsElapsed / pDuration);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static float getValue(final float pPercentage) {
		return -(float) Math.cos(pPercentage * MathConstants.PI_HALF) + 1;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
