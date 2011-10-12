package org.anddev.andengine.util.spatial.adt.bounds;

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

	private float mMinX;
	private float mMinY;
	private float mMaxX;
	private float mMaxY;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public FloatBounds(final float pX, final float pY) {
		this.set(pX, pY);
	}

	public FloatBounds(final float pMinX, final float pMinY, final float pMaxX, final float pMaxY) {
		this.set(pMinX, pMinY, pMaxX, pMaxY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getMinX() {
		return this.mMinX;
	}
	
	@Override
	public float getMinY() {
		return this.mMinY;
	}

	@Override
	public float getMaxX() {
		return this.mMaxX;
	}

	@Override
	public float getMaxY() {
		return this.mMaxY;
	}

	public void set(final float pX, final float pY) {
		this.set(pX, pY, pX, pY);
	}

	public void set(final float pMinX, final float pMinY, final float pMaxX, final float pMaxY) {
		this.mMinX = pMinX;
		this.mMinY = pMinY;
		this.mMaxX = pMaxX;
		this.mMaxY = pMaxY;

		if(pMinX > pMaxX) {
			throw new IllegalArgumentException("pMinX must be smaller or equal to pMaxX.");
		}
		if(pMinY > pMaxY) {
			throw new IllegalArgumentException("pMinY must be smaller or equal to pMaxY.");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
