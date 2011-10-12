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

	private float mLeft;
	private float mTop;
	private float mRight;
	private float mBottom;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FloatBounds(final float pLeft, final float pTop, final float pRight, final float pBottom) {
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
	public float getLeft() {
		return this.mLeft;
	}
	
	@Override
	public float getTop() {
		return this.mTop;
	}

	@Override
	public float getRight() {
		return this.mRight;
	}

	@Override
	public float getBottom() {
		return this.mBottom;
	}

	public void set(final float pX, final float pY) {
		this.set(pX, pY, pX, pY);
	}

	public void set(final float pLeft, final float pTop, final float pRight, final float pBottom) {
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
