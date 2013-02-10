package org.andengine.util.modifier.ease;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseStrongIn implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseStrongIn INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseStrongIn() {

	}

	public static EaseStrongIn getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EaseStrongIn();
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
		return EaseStrongIn.getValue(pSecondsElapsed / pDuration);
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
