package org.andengine.util.spatial.adt.bounds;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:48:53 - 11.10.2011
 */
public class FloatBounds implements IFloatBounds {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mXMin;
	private float mYMin;
	private float mXMax;
	private float mYMax;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public FloatBounds(final float pX, final float pY) {
		this.set(pX, pY);
	}

	public FloatBounds(final float pXMin, final float pYMin, final float pXMax, final float pYMax) {
		this.set(pXMin, pYMin, pXMax, pYMax);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getXMin() {
		return this.mXMin;
	}
	
	@Override
	public float getYMin() {
		return this.mYMin;
	}

	@Override
	public float getXMax() {
		return this.mXMax;
	}

	@Override
	public float getYMax() {
		return this.mYMax;
	}

	public void set(final float pX, final float pY) {
		this.set(pX, pY, pX, pY);
	}

	public void set(final float pXMin, final float pYMin, final float pXMax, final float pYMax) {
		this.mXMin = pXMin;
		this.mYMin = pYMin;
		this.mXMax = pXMax;
		this.mYMax = pYMax;

		if(pXMin > pXMax) {
			throw new IllegalArgumentException("pXMin: '" + pXMin + "' must be smaller or equal to pXMax: '" + pXMax + "'.");
		}
		if(pYMin > pYMax) {
			throw new IllegalArgumentException("pYMin: '" + pYMin + "' must be smaller or equal to pYMax: '" + pYMax + "'.");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
