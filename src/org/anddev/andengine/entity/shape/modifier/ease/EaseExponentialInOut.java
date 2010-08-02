package org.anddev.andengine.entity.shape.modifier.ease;


/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseExponentialInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseExponentialInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseExponentialInOut() {
	}

	public static EaseExponentialInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseExponentialInOut();
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
		if(t == 0) {
			return b;
		}
		if(t == d) {
			return b + c;
		}
		if((t /= d * 0.5f) < 1) {
			return (float) (c * 0.5f * Math.pow(2, 10 * (t - 1)) + b);
		}
		return (float) (c * 0.5f * (-Math.pow(2, -10 * --t) + 2) + b);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
