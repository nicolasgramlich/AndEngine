package org.anddev.andengine.util.spatial.adt.bounds;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:12:02 - 07.10.2011
 */
public class FloatBounds implements IBounds {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public float mLeft;
	public float mRight;
	public float mTop;
	public float mBottom;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FloatBounds(final float pX, final float pY) throws IllegalArgumentException {
		this(pX, pX, pY, pY);
	}

	public FloatBounds(final float pLeft, final float pRight, final float pTop, final float pBottom) throws IllegalArgumentException {
		this.mLeft = pLeft;
		this.mRight = pRight;
		this.mTop = pTop;
		this.mBottom = pBottom;

		if(pLeft > pRight) {
			throw new IllegalArgumentException("pLeft must be smaller or equal to pRight.");
		}
		if(pTop > pBottom) {
			throw new IllegalArgumentException("pTop must be smaller or equal to pBottom.");
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getLeft() {
		return this.mLeft;
	}

	public void setLeft(final float pLeft) {
		this.mLeft = pLeft;
	}

	public float getRight() {
		return this.mRight;
	}

	public void setRight(final float pRight) {
		this.mRight = pRight;
	}

	public float getTop() {
		return this.mTop;
	}

	public void setTop(final float pTop) {
		this.mTop = pTop;
	}

	public float getBottom() {
		return this.mBottom;
	}

	public void setBottom(final float pBottom) {
		this.mBottom = pBottom;
	}

	public float getWidth() {
		return this.mRight - this.mLeft;
	}

	public float getHeight() {
		return this.mBottom - this.mTop;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean intersects(final IBounds pBounds) {
		if(!(pBounds instanceof FloatBounds)) {
			throw new IllegalArgumentException("pBounds must be an instance of '" + FloatBounds.class.getSimpleName() + "'.");
		}
		// TODO Also support IntBounds?

		final FloatBounds other = (FloatBounds) pBounds;

		return this.intersects(other);
	}

	@Override
	public boolean contains(final IBounds pBounds) {
		if(!(pBounds instanceof FloatBounds)) {
			throw new IllegalArgumentException("pBounds must be an instance of '" + FloatBounds.class.getSimpleName() + "'.");
		}
		// TODO Also support IntBounds?

		final FloatBounds other = (FloatBounds) pBounds;

		return this.contains(other);
	}

	@Override
	public boolean isEmpty() {
		return this.mLeft >= this.mRight || this.mTop >= this.mBottom;
	}

	@Override
	public FloatBounds split(final BoundsSplit pBoundsSplit) {
		// TODO Does split work properly, when "mWidth % 2 != 0" or "mHeight % 2 != 0"

		final float halfWidth = this.getWidth() / 2;
		final float halfHeight = this.getHeight() / 2;

		switch(pBoundsSplit) {
			case TOP_LEFT:
				return new FloatBounds(this.mLeft, this.mLeft + halfWidth, this.mTop, this.mTop + halfHeight);
			case TOP_RIGHT:
				return new FloatBounds(this.mLeft + halfWidth, this.mRight, this.mTop, this.mTop + halfHeight);
			case BOTTOM_LEFT:
				return new FloatBounds(this.mLeft, this.mLeft + halfWidth, this.mTop + halfHeight, this.mBottom);
			case BOTTOM_RIGHT:
				return new FloatBounds(this.mLeft + halfWidth, this.mRight, this.mTop + halfHeight, this.mBottom);
			default:
				throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
		}
	}

	@Override
	public String toString() {
		return new StringBuilder()
		.append("[Left: ")
		.append(this.mLeft)
		.append(", Right: ")
		.append(this.mRight)
		.append(", Top: ")
		.append(this.mTop)
		.append(", Bottom: ")
		.append(this.mBottom)
		.append("]")
		.toString();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean intersects(final FloatBounds pFloatBounds) {
		return (this.mLeft < pFloatBounds.mRight) && (pFloatBounds.mLeft < this.mRight) && (this.mTop < pFloatBounds.mBottom) && (pFloatBounds.mTop < this.mBottom);
	}

	public boolean intersects(final float pLeft, final float pRight, final float pTop, final float pBottom) {
		return (this.mLeft < pRight) && (pLeft < this.mRight) && (this.mTop < pBottom) && (pTop < this.mBottom);
	}

	public boolean contains(final FloatBounds pFloatBounds) {
		return (this.mLeft <= pFloatBounds.mLeft) && (this.mTop <= pFloatBounds.mTop) && (this.mRight >= pFloatBounds.mRight) && (this.mBottom >= pFloatBounds.mBottom);
	}

	public boolean contains(final float pLeft, final float pRight, final float pTop, final float pBottom) {
		return (this.mLeft <= pLeft) && (this.mTop <= pTop) && (this.mRight >= pRight) && (this.mBottom >= pBottom);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
