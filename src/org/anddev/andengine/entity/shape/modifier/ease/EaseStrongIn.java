package org.anddev.andengine.entity.shape.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
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
		if(INSTANCE == null) {
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
	public float calc(float t, final float b, final float c, final float d) {
		return c * (t /= d) * t * t * t * t + b;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
