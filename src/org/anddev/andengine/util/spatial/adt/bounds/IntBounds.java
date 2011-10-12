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

	private int mLeft;
	private int mTop;
	private int mRight;
	private int mBottom;

	// ===========================================================
	// Constructors
	// ===========================================================

	public IntBounds(final int pLeft, final int pTop, final int pRight, final int pBottom) {
		this.mLeft = pLeft;
		this.mRight = pRight;
		this.mTop = pTop;
		this.mBottom = pBottom;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getLeft() {
		return this.mLeft;
	}
	
	@Override
	public int getTop() {
		return this.mTop;
	}

	@Override
	public int getRight() {
		return this.mRight;
	}

	@Override
	public int getBottom() {
		return this.mBottom;
	}

	public void set(final int pX, final int pY) {
		this.set(pX, pY, pX, pY);
	}

	public void set(final int pLeft, final int pTop, final int pRight, final int pBottom) {
		this.mLeft = pLeft;
		this.mRight = pRight;
		this.mTop = pTop;
		this.mBottom = pBottom;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
