package org.andengine.util.modifier.ease;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseQuadOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseQuadOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseQuadOut() {

	}

	public static EaseQuadOut getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EaseQuadOut();
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
		return EaseQuadOut.getValue(pSecondsElapsed / pDuration);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static float getValue(final float pPercentage) {
		return -pPercentage * (pPercentage - 2);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
