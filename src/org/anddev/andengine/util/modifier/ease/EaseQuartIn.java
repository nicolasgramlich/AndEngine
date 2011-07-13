package org.anddev.andengine.util.modifier.ease;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Gil
 * @author Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseQuartIn implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseQuartIn INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseQuartIn() {

	}

	public static EaseQuartIn getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseQuartIn();
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
	public float getPercentageDone(float pSecondsElapsed, final float pDuration, final float pMinValue, final float pMaxValue) {
		return pMaxValue * (pSecondsElapsed /= pDuration) * pSecondsElapsed * pSecondsElapsed * pSecondsElapsed + pMinValue;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
