package org.anddev.andengine.collision;

/**
 * @author Nicolas Gramlich
 * @since 19:27:22 - 17.07.2010
 */
public class LineCollisionChecker extends ShapeCollisionChecker {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static boolean checkLineCollision(final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
		return ((relativeCCW(pX1, pY1, pX2, pY2, pX3, pY3) * relativeCCW(pX1, pY1, pX2, pY2, pX4, pY4) <= 0)
				&& (relativeCCW(pX3, pY3, pX4, pY4, pX1, pY1) * relativeCCW(pX3, pY3, pX4, pY4, pX2, pY2) <= 0));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
