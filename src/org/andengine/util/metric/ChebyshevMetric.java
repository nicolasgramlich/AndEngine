package org.andengine.util.metric;

/**
 * (c) winniehell (2012)
 *
 * @author <a href="https://github.com/winniehell">winniehell</a>
 * @since 2012-08-05
 */
public class ChebyshevMetric implements IMetric {

	// ===========================================================
	// Constants
	// ===========================================================

	public static final ChebyshevMetric INSTANCE = new ChebyshevMetric();

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	protected ChebyshevMetric() {

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
		return Math.max(this.distance(pX1, pX2), this.distance(pY1, pY2));
	}

	@Override
	public final float displacement(final float pX, final float pY) {
		return Math.max(Math.abs(pX), Math.abs(pY));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
