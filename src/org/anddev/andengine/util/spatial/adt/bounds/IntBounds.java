package org.anddev.andengine.util.spatial.adt.bounds;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:05:32 - 08.10.2011
 */
public class IntBounds implements IBounds {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public final int mLeft;
	public final int mRight;
	public final int mTop;
	public final int mBottom;
	public final int mWidth;
	public final int mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public IntBounds(final int pLeft, final int pRight, final int pTop, final int pBottom) {
		this.mLeft = pLeft;
		this.mRight = pRight;
		this.mTop = pTop;
		this.mBottom = pBottom;

		this.mWidth = this.mRight - this.mLeft;
		this.mHeight = this.mBottom - this.mTop;

// this.left < this.right && this.top < this.bottom
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getLeft() {
		return this.mLeft;
	}

	public int getRight() {
		return this.mRight;
	}

	public int getTop() {
		return this.mTop;
	}

	public int getBottom() {
		return this.mBottom;
	}

	public int getWidth() {
		return this.mWidth;
	}

	public int getHeight() {
		return this.mHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean intersects(final IBounds pBounds) {
		if(!(pBounds instanceof IntBounds)) {
			throw new IllegalArgumentException("pBounds must be an instance of '" + IntBounds.class.getSimpleName() + "'.");
		}

		final IntBounds other = (IntBounds) pBounds;

		return (this.mLeft < other.mRight) && (other.mLeft < this.mRight) && (this.mTop < other.mBottom) && (other.mTop < this.mBottom);
	}

	@Override
	public boolean contains(final IBounds pBounds) {
		if(!(pBounds instanceof IntBounds)) {
			throw new IllegalArgumentException("pBounds must be an instance of '" + IntBounds.class.getSimpleName() + "'.");
		}

		final IntBounds other = (IntBounds) pBounds;

		return (this.mLeft <= other.mLeft) && (this.mTop <= other.mTop) && (this.mRight >= other.mRight) && (this.mBottom >= other.mBottom);
	}

	@Override
	public boolean isEmpty() {
		return this.mLeft >= this.mRight || this.mTop >= this.mBottom;
	}

	@Override
	public IntBounds split(final BoundsSplit pBoundsSplit) {
		// TODO Does split work properly, when "mWidth % 2 != 0" or "mHeight % 2 != 0"

		final int halfWidth = this.mWidth / 2;
		final int halfHeight = this.mHeight / 2;

		switch(pBoundsSplit) {
			case TOP_LEFT:
				return new IntBounds(this.mLeft, this.mLeft + halfWidth, this.mTop, this.mTop + halfHeight);
			case TOP_RIGHT:
				return new IntBounds(this.mLeft + halfWidth, this.mRight, this.mTop, this.mTop + halfHeight);
			case BOTTOM_LEFT:
				return new IntBounds(this.mLeft, this.mLeft + halfWidth, this.mTop + halfHeight, this.mBottom);
			case BOTTOM_RIGHT:
				return new IntBounds(this.mLeft + halfWidth, this.mRight, this.mTop + halfHeight, this.mBottom);
			default:
				throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
