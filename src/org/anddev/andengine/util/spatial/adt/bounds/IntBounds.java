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

	public int mLeft;
	public int mRight;
	public int mTop;
	public int mBottom;

	// ===========================================================
	// Constructors
	// ===========================================================

	public IntBounds(final int pX, final int pY) throws IllegalArgumentException {
		this(pX, pX, pY, pY);
	}

	public IntBounds(final int pLeft, final int pRight, final int pTop, final int pBottom) throws IllegalArgumentException {
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

	public int getLeft() {
		return this.mLeft;
	}

	public void setLeft(final int pLeft) {
		this.mLeft = pLeft;
	}

	public int getRight() {
		return this.mRight;
	}

	public void setRight(final int pRight) {
		this.mRight = pRight;
	}

	public int getTop() {
		return this.mTop;
	}

	public void setTop(final int pTop) {
		this.mTop = pTop;
	}

	public int getBottom() {
		return this.mBottom;
	}

	public void setBottom(final int pBottom) {
		this.mBottom = pBottom;
	}

	public int getWidth() {
		return this.mRight - this.mLeft + 1;
	}

	public int getHeight() {
		return this.mBottom - this.mTop + 1;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean intersects(final IBounds pBounds) {
		if(!(pBounds instanceof IntBounds)) {
			throw new IllegalArgumentException("pBounds must be an instance of '" + IntBounds.class.getSimpleName() + "'.");
		}

		return this.intersects((IntBounds) pBounds);
	}

	@Override
	public boolean contains(final IBounds pBounds) {
		if(!(pBounds instanceof IntBounds)) {
			throw new IllegalArgumentException("pBounds must be an instance of '" + IntBounds.class.getSimpleName() + "'.");
		}

		return this.contains((IntBounds) pBounds);
	}

	@Override
	public boolean isEmpty() {
		return this.mLeft >= this.mRight || this.mTop >= this.mBottom;
	}

	@Override
	public IntBounds split(final BoundsSplit pBoundsSplit) throws BoundsSplitException {
		final int width = this.getWidth();
		final int height = this.getHeight();

		if(width <= 2 && height <= 2) {
			throw new BoundsSplitException();
		}

		final int halfWidth = width / 2;
		final int halfHeight = height / 2;

		final int left;
		final int right;
		if(width <= 2) {
			switch(pBoundsSplit) {
				case TOP_LEFT:
				case BOTTOM_LEFT:
					left = this.mLeft;
					right = this.mRight;
					break;
				default:
					return null;
			}
		} else {
			switch(pBoundsSplit) {
				case TOP_LEFT:
					left = this.mLeft;
					right = this.mLeft + halfWidth;
					break;
				case TOP_RIGHT:
					left = this.mLeft + halfWidth;
					right = this.mRight;
					break;
				case BOTTOM_LEFT:
					left = this.mLeft;
					right = this.mLeft + halfWidth;
					break;
				case BOTTOM_RIGHT:
					left = this.mLeft + halfWidth;
					right = this.mRight;
					break;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}

		final int top;
		final int bottom;
		if(height <= 2) {
			switch(pBoundsSplit) {
				case TOP_LEFT:
				case TOP_RIGHT:
					top = this.mTop;
					bottom = this.mBottom;
					break;
				default:
					return null;
			}
		} else {
			switch(pBoundsSplit) {
				case TOP_LEFT:
					top = this.mTop;
					bottom = this.mTop + halfHeight;
					break;
				case TOP_RIGHT:
					top = this.mTop;
					bottom = this.mTop + halfHeight;
					break;
				case BOTTOM_LEFT:
					top = this.mTop + halfHeight;
					bottom = this.mBottom;
					break;
				case BOTTOM_RIGHT:
					top = this.mTop + halfHeight;
					bottom = this.mBottom;
					break;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}

		return new IntBounds(left, right, top, bottom);
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

	public boolean intersects(final IntBounds pIntBounds) {
		return (this.mLeft < pIntBounds.mRight) && (pIntBounds.mLeft < this.mRight) && (this.mTop < pIntBounds.mBottom) && (pIntBounds.mTop < this.mBottom);
	}

	public boolean intersects(final int pLeft, final int pRight, final int pTop, final int pBottom) {
		return (this.mLeft < pRight) && (pLeft < this.mRight) && (this.mTop < pBottom) && (pTop < this.mBottom);
	}

	public boolean contains(final IntBounds pIntBounds) {
		return (this.mLeft <= pIntBounds.mLeft) && (this.mTop <= pIntBounds.mTop) && (this.mRight >= pIntBounds.mRight) && (this.mBottom >= pIntBounds.mBottom);
	}

	public boolean contains(final int pLeft, final int pRight, final int pTop, final int pBottom) {
		return (this.mLeft <= pLeft) && (this.mTop <= pTop) && (this.mRight >= pRight) && (this.mBottom >= pBottom);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
