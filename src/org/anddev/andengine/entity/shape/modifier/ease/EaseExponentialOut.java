package org.anddev.andengine.entity.shape.modifier.ease;


/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseExponentialOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseExponentialOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseExponentialOut() {
	}

	public static EaseExponentialOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseExponentialOut();
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
	public float calc(final float t, final float b, final float c, final float d) {
		return (float) ((t == d) ? b + c : c * (-Math.pow(2, -10 * t / d) + 1) + b);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
