package org.andengine.util.metric;

/**
 * (c) winniehell (2012)
 *
 * @author <a href="https://github.com/winniehell">winniehell</a>
 * @since 2012-08-05
 */
public final class CosineMetric {

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

	/** @return cosine distance between pX1 and pX2 */
	public static float distance(final float pX1, final float pX2) {
		return Math.abs(pX1 - pX2);
	}

	/** @return cosine distance between (pX1, pY2) and (pX2, pY2) */
	public static float distance(final float pX1, final float pY1,
	                            final float pX2, final float pY2) {
		final float d1 = EuclideanMetric.displacement(pX1, pY1);
		final float d2 = EuclideanMetric.displacement(pX2, pY2);

		return ((pX1*pX2) + (pY1*pY2))/(d1*d2);
	}

	/** @return cosine distance between (pX1, pY2) and origin (0,0) */
	public static float displacement(final float pX, final float pY) {
		return Math.max(Math.abs(pX), Math.abs(pY));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
