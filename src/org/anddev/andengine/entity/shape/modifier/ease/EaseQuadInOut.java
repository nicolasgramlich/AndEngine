package org.anddev.andengine.entity.shape.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseQuadInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private static EaseQuadInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseQuadInOut() {
	}

	public static EaseQuadInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseQuadInOut();
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
		if((t /= d * 0.5f) < 1) {
			return c * 0.5f * t * t + b;
		}
		return -c * 0.5f * ((--t) * (t - 2) - 1) + b;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
