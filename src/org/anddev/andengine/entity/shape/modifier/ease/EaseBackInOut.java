package org.anddev.andengine.entity.shape.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseBackInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseBackInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseBackInOut() {
		
	}

	public static EaseBackInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseBackInOut();
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
		float s = 1.70158f;
		if((t /= d * 0.5f) < 1) {
			return c * 0.5f * (t * t * (((s *= (1.525f)) + 1) * t - s)) + b;
		}

		return c / 2 * ((t -= 2) * t * (((s *= (1.525f)) + 1) * t + s) + 2) + b;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
