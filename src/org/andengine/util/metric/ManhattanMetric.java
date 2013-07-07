package org.andengine.util.metric;

/**
 * (c) winniehell (2012)
 *
 * @author <a href="https://github.com/winniehell">winniehell</a>
 * @since 2012-08-05
 */
public final class ManhattanMetric {

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

	/** @return Manhattan distance between pX1 and pX2 */
	public static float distance(final float pX1, final float pX2) {
		return Math.abs(pX1 - pX2);
	}

	/** @return Manhattan distance between (pX1, pY2) and (pX2, pY2) */
	public static float distance(final float pX1, final float pY1,
	                            final float pX2, final float pY2) {
		return distance(pX1, pX2) + distance(pY1, pY2);
	}

	/** @return Manhattan distance between (pX1, pY2) and origin (0,0) */
	public static float displacement(final float pX, final float pY) {
		return Math.abs(pX) + Math.abs(pY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
