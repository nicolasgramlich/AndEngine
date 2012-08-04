package org.andengine.util.metric;

/**
 * (c) winniehell (2012)
 *
 * @author <a href="https://github.com/winniehell">winniehell</a>
 * @since 2012-08-05
 */
public class ManhattanMetric implements IMinkowskiMetric {

	// ===========================================================
	// Constants
	// ===========================================================

	public static final ManhattanMetric INSTANCE = new ManhattanMetric();

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	protected ManhattanMetric() {

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
		return this.distance(pX1, pX2) + this.distance(pY1, pY2);
	}

	@Override
	public final float displacement(final float pX, final float pY) {
		return Math.abs(pX) + Math.abs(pY);
	}

	@Override
	public final float getMinkowskiParameter() {
		return 1;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
