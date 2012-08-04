package org.andengine.util.metric;

/**
 * (c) winniehell (2012)
 *
 * @author <a href="https://github.com/winniehell">winniehell</a>
 * @since 2012-08-04
 */
public interface IMetric {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/** @return distance between pX1 and pX2 */
	float distance(float pX1, float pX2);
	/** @return distance between (pX1, pY2) and (pX2, pY2) */
	float distance(float pX1, float pY1, float pX2, float pY2);

	/** @return distance between (pX1, pY2) and origin (0,0) */
	float displacement(float pX, float pY);
}
