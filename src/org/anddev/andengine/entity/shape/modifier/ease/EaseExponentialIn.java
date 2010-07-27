package org.anddev.andengine.entity.shape.modifier.ease;


/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseExponentialIn implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private static EaseExponentialIn INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseExponentialIn() {
	}

	public static EaseExponentialIn getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseExponentialIn();
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
		return (float) ((t == 0) ? b : c * Math.pow(2, 10 * (t / d - 1)) + b - c * 0.001f);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
