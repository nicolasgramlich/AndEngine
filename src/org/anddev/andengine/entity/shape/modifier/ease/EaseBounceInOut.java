package org.anddev.andengine.entity.shape.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 16:52:11 - 26.07.2010
 */
public class EaseBounceInOut implements IEaseFunction {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static EaseBounceInOut INSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	private EaseBounceInOut() {

	}

	public static EaseBounceInOut getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EaseBounceInOut();
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
		if(t < d * 0.5) {
			return EaseBounceIn.getInstance().calc(t * 2, 0, c, d) * 0.5f + b;
		} else {
			return EaseBounceOut.getInstance().calc(t * 2 - d, 0, c, d) * 0.5f + c * 0.5f + b;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
