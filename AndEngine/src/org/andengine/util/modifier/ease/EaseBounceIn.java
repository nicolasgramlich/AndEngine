package org.andengine.util.modifier.ease;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseBounceIn implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseBounceIn INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseBounceIn() {

	}

	public static EaseBounceIn getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseBounceIn();
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
		return EaseBounceIn.getValue(pSecondsElapsed / pDuration);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static float getValue(final float pPercentage) {
		// TODO Inline?
		return 1 - EaseBounceOut.getValue(1 - pPercentage);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
