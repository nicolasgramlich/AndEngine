package org.andengine.util.metric;

/**
 * (c) winniehell (2012)
 *
 * @author <a href="https://github.com/winniehell">winniehell</a>
 * @since 2012-08-05
 */
public class CosineMetric implements IMetric {

	// ===========================================================
	// Constants
	// ===========================================================

	public static final CosineMetric INSTANCE = new CosineMetric();

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	protected CosineMetric() {

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
		float disp1 = EuclideanMetric.INSTANCE.displacement(pX1, pY1);
		float disp2 = EuclideanMetric.INSTANCE.displacement(pX2, pY2);

		return ((pX1*pX2) + (pY1*pY2))/(disp1*disp2);
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
