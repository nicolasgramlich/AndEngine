package org.andengine.util.modifier.ease;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseQuintIn implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseQuintIn INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseQuintIn() {

	}

	public static EaseQuintIn getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseQuintIn();
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
		return EaseQuintIn.getValue(pSecondsElapsed / pDuration);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static float getValue(final float pPercentage) {
		return pPercentage * pPercentage * pPercentage * pPercentage * pPercentage;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
