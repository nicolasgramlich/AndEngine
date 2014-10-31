package org.andengine.util.adt.bounds;

import org.andengine.util.adt.spatial.bounds.util.IntBoundsUtils;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:45:04 - 11.11.2011
 */
public class IntBounds implements IIntBounds {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mXMin;
	private int mYMin;
	private int mXMax;
	private int mYMax;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public IntBounds(final int pX, final int pY) {
		this.set(pX, pY);
	}

	public IntBounds(final int pXMin, final int pYMin, final int pXMax, final int pYMax) {
		this.set(pXMin, pYMin, pXMax, pYMax);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getXMin() {
		return this.mXMin;
	}
	
	@Override
	public int getYMin() {
		return this.mYMin;
	}

	@Override
	public int getXMax() {
		return this.mXMax;
	}

	@Override
	public int getYMax() {
		return this.mYMax;
	}

	public void set(final int pX, final int pY) {
		this.set(pX, pY, pX, pY);
	}

	public void set(final int pXMin, final int pYMin, final int pXMax, final int pYMax) {
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
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public boolean contains(final int pX, final int pY) {
		return IntBoundsUtils.contains(this.mXMin, this.mYMin, this.mXMax, this.mYMax, pX, pY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
