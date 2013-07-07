package org.andengine.util.metric;

import android.util.FloatMath;

/**
 * (c) winniehell (2012)
 *
 * @author <a href="https://github.com/winniehell">winniehell</a>
 * @since 2012-08-05
 */
public final class EuclideanMetric {

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

	/** @return Euclidean distance between pX1 and pX2 */
	public static float distance(final float pX1, final float pX2) {
		return Math.abs(pX1 - pX2);
	}

	/** @return Euclidean distance between (pX1, pY2) and (pX2, pY2) */
	public static float distance(final float pX1, final float pY1,
	                      final float pX2, final float pY2) {
		final float distX = pX1 - pX2;
		final float distY = pY1 - pY2;

		return FloatMath.sqrt(distX * distX + distY * distY);
	}

	/** @return Euclidean distance between (pX1, pY2) and origin (0,0) */
	public static float displacement(final float pX, final float pY) {
		return FloatMath.sqrt((pX * pX) + (pY * pY));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
