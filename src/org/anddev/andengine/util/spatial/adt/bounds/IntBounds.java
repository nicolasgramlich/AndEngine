package org.anddev.andengine.util.spatial.adt.bounds;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 9:45:04 PM - Oct 11, 2011
 */
public class IntBounds implements IIntBounds {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mMinX;
	private int mMinY;
	private int mMaxX;
	private int mMaxY;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public IntBounds(final int pX, final int pY) {
		this.set(pX, pY);
	}

	public IntBounds(final int pMinX, final int pMinY, final int pMaxX, final int pMaxY) {
		this.set(pMinX, pMinY, pMaxX, pMaxY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getMinX() {
		return this.mMinX;
	}
	
	@Override
	public int getMinY() {
		return this.mMinY;
	}

	@Override
	public int getMaxX() {
		return this.mMaxX;
	}

	@Override
	public int getMaxY() {
		return this.mMaxY;
	}

	public void set(final int pX, final int pY) {
		this.set(pX, pY, pX, pY);
	}

	public void set(final int pMinX, final int pMinY, final int pMaxX, final int pMaxY) {
		this.mMinX = pMinX;
		this.mMinY = pMinY;
		this.mMaxX = pMaxX;
		this.mMaxY = pMaxY;

		if(pMinX > pMaxX) {
			throw new IllegalArgumentException("pMinX: '" + pMinX + "' must be smaller or equal to pMaxX: '" + pMaxX + "'.");
		}
		if(pMinY > pMaxY) {
			throw new IllegalArgumentException("pMinY: '" + pMinY + "' must be smaller or equal to pMaxY: '" + pMaxY + "'.");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
