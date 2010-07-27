package org.anddev.andengine.entity.shape.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseBounceOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private static EaseBounceOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	private EaseBounceOut() {
		
	}

	public static EaseBounceOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseBounceOut();
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
		if((t /= d) < (1 / 2.75)) {
			return c * (7.5625f * t * t) + b;
		} else if(t < (2 / 2.75)) {
			return c * (7.5625f * (t -= (1.5f / 2.75f)) * t + 0.75f) + b;
		} else if(t < (2.5 / 2.75)) {
			return c * (7.5625f * (t -= (2.25f / 2.75f)) * t + 0.9375f) + b;
		} else {
			return c * (7.5625f * (t -= (2.625f / 2.75f)) * t + 0.984375f) + b;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
