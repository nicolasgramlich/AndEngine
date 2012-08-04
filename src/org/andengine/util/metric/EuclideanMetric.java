package org.andengine.util.metric;

import android.util.FloatMath;

/**
 * (c) winniehell (2012)
 *
 * @author <a href="https://github.com/winniehell">winniehell</a>
 * @since 2012-08-05
 */
public class EuclideanMetric implements IMinkowskiMetric {

	// ===========================================================
	// Constants
	// ===========================================================

	public static final EuclideanMetric INSTANCE = new EuclideanMetric();

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	protected EuclideanMetric() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public final float distance(final float pX1, final float pX2) {
		return Math.abs(pX1 - pX2);
	}

	@Override
	public final float distance(final float pX1, final float pY1,
	                      final float pX2, final float pY2) {
		final float distX = pX1 - pX2;
		final float distY = pY1 - pY2;

		return FloatMath.sqrt(distX * distX + distY * distY);
	}

	@Override
	public final float displacement(final float pX, final float pY) {
		return FloatMath.sqrt((pX * pX) + (pY * pY));
	}

	@Override
	public final float getMinkowskiParameter() {
		return 2;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
