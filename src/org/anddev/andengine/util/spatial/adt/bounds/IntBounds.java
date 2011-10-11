package org.anddev.andengine.util.spatial.adt.bounds;

import org.anddev.andengine.util.spatial.adt.bounds.source.IBoundsSource;
import org.anddev.andengine.util.spatial.adt.bounds.source.IIntBoundsSource;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:05:32 - 08.10.2011
 */
public class IntBounds implements IBounds<IIntBoundsSource>, IIntBoundsSource {
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

	public IntBounds(final IIntBoundsSource pIntBoundsSource) throws IllegalArgumentException {
		this(pIntBoundsSource.getLeft(), pIntBoundsSource.getRight(), pIntBoundsSource.getTop(), pIntBoundsSource.getBottom());
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

	@Override
	public int getLeft() {
		return this.mLeft;
	}

	public void setLeft(final int pLeft) {
		this.mLeft = pLeft;
	}

	@Override
	public int getRight() {
		return this.mRight;
	}

	public void setRight(final int pRight) {
		this.mRight = pRight;
	}

	@Override
	public int getTop() {
		return this.mTop;
	}

	public void setTop(final int pTop) {
		this.mTop = pTop;
	}

	@Override
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

	public void set(final int pX, final int pY) {
		this.mLeft = pX;
		this.mRight = pX;
		this.mTop = pY;
		this.mBottom = pY;
	}

	public void set(final int pLeft, final int pRight, final int pTop, final int pBottom) {
		this.mLeft = pLeft;
		this.mRight = pRight;
		this.mTop = pTop;
		this.mBottom = pBottom;
	}

	@Override
	public void set(final IBoundsSource pBoundsSource) {
		if(!(pBoundsSource instanceof IIntBoundsSource)) {
			throw new IllegalArgumentException("pBounds must be an instance of '" + IIntBoundsSource.class.getSimpleName() + "'.");
		}

		this.set((IIntBoundsSource)pBoundsSource);
	}

	public void set(final IIntBoundsSource pIntBoundsSource) {
		this.set(pIntBoundsSource.getLeft(), pIntBoundsSource.getRight(), pIntBoundsSource.getTop(), pIntBoundsSource.getBottom());
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean intersects(final IBounds<IIntBoundsSource> pBounds) {
		if(!(pBounds instanceof IntBounds)) {
			throw new IllegalArgumentException("pBounds must be an instance of '" + IntBounds.class.getSimpleName() + "'.");
		}

		return this.intersects((IntBounds) pBounds);
	}

	@Override
	public boolean intersects(final IIntBoundsSource pIntBoundsSource) {
		return this.intersects(pIntBoundsSource.getLeft(), pIntBoundsSource.getRight(), pIntBoundsSource.getTop(), pIntBoundsSource.getBottom());
	}

	@Override
	public boolean contains(final IBounds<IIntBoundsSource> pBounds) {
		if(!(pBounds instanceof IntBounds)) {
			throw new IllegalArgumentException("pBounds must be an instance of '" + IntBounds.class.getSimpleName() + "'.");
		}

		return this.contains((IntBounds) pBounds);
	}

	@Override
	public boolean contains(final IIntBoundsSource pIntBoundsSource) {
		return this.contains(pIntBoundsSource.getLeft(), pIntBoundsSource.getRight(), pIntBoundsSource.getTop(), pIntBoundsSource.getBottom());
	}

	@Override
	public boolean contains(final BoundsSplit pBoundsSplit, final IIntBoundsSource pIntBoundsSource) {
		try {
			return IntBounds.contains(this.getLeft(pBoundsSplit), this.getRight(pBoundsSplit), this.getTop(pBoundsSplit), this.getBottom(pBoundsSplit), pIntBoundsSource.getLeft(), pIntBoundsSource.getRight(), pIntBoundsSource.getTop(), pIntBoundsSource.getBottom());
		} catch (BoundsSplitException e) {
			return false;
		}
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

		final int left = this.getLeft(pBoundsSplit);
		final int right = this.getRight(pBoundsSplit);
		final int top = this.getTop(pBoundsSplit);
		final int bottom = this.getBottom(pBoundsSplit);

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


	private int getLeft(final BoundsSplit pBoundsSplit) {
		final int width = this.getWidth();
		final int halfWidth = width / 2;

		if(width <= 2) {
			switch(pBoundsSplit) {
				case TOP_LEFT:
				case BOTTOM_LEFT:
					return this.mLeft;
				case BOTTOM_RIGHT:
				case TOP_RIGHT:
					throw new BoundsSplitException();
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		} else {
			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mLeft;
				case TOP_RIGHT:
					return this.mLeft + halfWidth;
				case BOTTOM_LEFT:
					return this.mLeft;
				case BOTTOM_RIGHT:
					return this.mLeft + halfWidth;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}
	}

	private int getRight(final BoundsSplit pBoundsSplit) {
		final int width = this.getWidth();
		final int halfWidth = width / 2;

		if(width <= 2) {
			switch(pBoundsSplit) {
				case TOP_LEFT:
				case BOTTOM_LEFT:
					return this.mRight;
				case BOTTOM_RIGHT:
				case TOP_RIGHT:
					throw new BoundsSplitException();
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		} else {
			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mLeft + halfWidth;
				case TOP_RIGHT:
					return this.mRight;
				case BOTTOM_LEFT:
					return this.mLeft + halfWidth;
				case BOTTOM_RIGHT:
					return this.mRight;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}
	}

	private int getBottom(final BoundsSplit pBoundsSplit) {
		final int height = this.getHeight();
		final int halfHeight = height / 2;

		if(height <= 2) {
			switch(pBoundsSplit) {
				case TOP_LEFT:
				case TOP_RIGHT:
					return this.mBottom;
				case BOTTOM_LEFT:
				case BOTTOM_RIGHT:
					throw new BoundsSplitException();
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		} else {
			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mTop + halfHeight;
				case TOP_RIGHT:
					return this.mTop + halfHeight;
				case BOTTOM_LEFT:
					return this.mBottom;
				case BOTTOM_RIGHT:
					return this.mBottom;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}
	}

	private int getTop(final BoundsSplit pBoundsSplit) {
		final int height = this.getHeight();
		final int halfHeight = height / 2;

		if(height <= 2) {
			switch(pBoundsSplit) {
				case TOP_LEFT:
				case TOP_RIGHT:
					return  this.mTop;
				case BOTTOM_LEFT:
				case BOTTOM_RIGHT:
					throw new BoundsSplitException();
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		} else {
			switch(pBoundsSplit) {
				case TOP_LEFT:
					return  this.mTop;
				case TOP_RIGHT:
					return this.mTop;
				case BOTTOM_LEFT:
					return this.mTop + halfHeight;
				case BOTTOM_RIGHT:
					return this.mTop + halfHeight;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}
	}

	public boolean intersects(final IntBounds pIntBounds) {
		return (this.mLeft < pIntBounds.mRight) && (pIntBounds.mLeft < this.mRight) && (this.mTop < pIntBounds.mBottom) && (pIntBounds.mTop < this.mBottom);
	}

	public boolean intersects(final int pLeft, final int pRight, final int pTop, final int pBottom) {
		return (this.mLeft < pRight) && (pLeft < this.mRight) && (this.mTop < pBottom) && (pTop < this.mBottom);
	}

	public static boolean intersects(final int pLeftA, final int pRightA, final int pTopA, final int pBottomA, final int pLeftB, final int pRightB, final int pTopB, final int pBottomB) {
		return (pLeftA < pRightB) && (pLeftB < pRightA) && (pTopA < pBottomB) && (pTopB < pBottomA);
	}

	public boolean contains(final IntBounds pIntBounds) {
		return (this.mLeft <= pIntBounds.mLeft) && (this.mTop <= pIntBounds.mTop) && (this.mRight >= pIntBounds.mRight) && (this.mBottom >= pIntBounds.mBottom);
	}

	public boolean contains(final int pLeft, final int pRight, final int pTop, final int pBottom) {
		return (this.mLeft <= pLeft) && (this.mTop <= pTop) && (this.mRight >= pRight) && (this.mBottom >= pBottom);
	}

	public static boolean contains(final int pLeftA, final int pRightA, final int pTopA, final int pBottomA, final int pLeftB, final int pRightB, final int pTopB, final int pBottomB) {
		return (pLeftA <= pLeftB) && (pTopA <= pTopB) && (pRightA >= pRightB) && (pBottomA >= pBottomB);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
