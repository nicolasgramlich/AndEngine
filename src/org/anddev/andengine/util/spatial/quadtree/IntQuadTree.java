package org.anddev.andengine.util.spatial.quadtree;

import java.util.LinkedList;
import java.util.List;

import org.anddev.andengine.util.IMatcher;
import org.anddev.andengine.util.spatial.ISpatialItem;
import org.anddev.andengine.util.spatial.adt.bounds.BoundsSplit;
import org.anddev.andengine.util.spatial.adt.bounds.BoundsSplit.BoundsSplitException;
import org.anddev.andengine.util.spatial.adt.bounds.IIntBounds;
import org.anddev.andengine.util.spatial.adt.bounds.IntBounds;
import org.anddev.andengine.util.spatial.adt.bounds.util.IntBoundsUtils;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:22:18 - 10.10.2011
 */
public class IntQuadTree<T extends ISpatialItem<IIntBounds>> extends QuadTree<IIntBounds, T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IntBounds mQueryIntBounds = new IntBounds(0, 0, 0, 0);

	// ===========================================================
	// Constructors
	// ===========================================================

	public IntQuadTree(final IIntBounds pIntBounds) {
		super(pIntBounds);
	}

	public IntQuadTree(final IIntBounds pIntBounds, final int pMaxLevel) {
		super(pIntBounds, pMaxLevel);
	}

	@Override
	protected IntQuadTreeNode initRoot(final IIntBounds pIntBounds) {
		return new IntQuadTreeNode(QuadTree.LEVEL_ROOT, pIntBounds);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized List<T> query(final int pX, final int pY) {
		return this.query(pX, pY, new LinkedList<T>());
	}

	public synchronized List<T> query(final int pX, final int pY, final List<T> pResult) {
		this.mQueryIntBounds.set(pX, pY);
		return this.query(this.mQueryIntBounds, pResult);
	}

	public synchronized List<T> query(final int pX, final int pY, final IMatcher<T> pMatcher) {
		return this.query(pX, pY, pMatcher, new LinkedList<T>());
	}

	public synchronized List<T> query(final int pX, final int pY, final IMatcher<T> pMatcher, final List<T> pResult) {
		this.mQueryIntBounds.set(pX, pY);
		return this.query(this.mQueryIntBounds, pMatcher, pResult);
	}

	public synchronized List<T> query(final int pLeft, final int pTop, final int pRight, final int pBottom) {
		return this.query(pLeft, pTop, pRight, pBottom, new LinkedList<T>());
	}

	public synchronized List<T> query(final int pLeft, final int pTop, final int pRight, final int pBottom, final List<T> pResult) {
		this.mQueryIntBounds.set(pLeft, pTop, pRight, pBottom);
		return this.query(this.mQueryIntBounds, pResult);
	}

	public synchronized List<T> query(final int pLeft, final int pTop, final int pRight, final int pBottom, final IMatcher<T> pMatcher) {
		return this.query(pLeft, pTop, pRight, pBottom, pMatcher, new LinkedList<T>());
	}

	public synchronized List<T> query(final int pLeft, final int pTop, final int pRight, final int pBottom, final IMatcher<T> pMatcher, final List<T> pResult) {
		this.mQueryIntBounds.set(pLeft, pTop, pRight, pBottom);
		return this.query(this.mQueryIntBounds, pMatcher, pResult);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class IntQuadTreeNode extends QuadTreeNode {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mLeft;
		private final int mTop;
		private final int mRight;
		private final int mBottom;

		// ===========================================================
		// Constructors
		// ===========================================================

		public IntQuadTreeNode(final int pLevel, final IIntBounds pIntBounds) {
			this(pLevel, pIntBounds.getLeft(), pIntBounds.getTop(), pIntBounds.getRight(), pIntBounds.getBottom());
		}

		public IntQuadTreeNode(final int pLevel, final int pLeft, final int pTop, final int pRight, final int pBottom) {
			super(pLevel);

			this.mLeft = pLeft;
			this.mTop = pTop;
			this.mRight = pRight;
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
		
		public int getTop() {
			return this.mTop;
		}

		public int getRight() {
			return this.mRight;
		}

		public int getBottom() {
			return this.mBottom;
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
		protected IntQuadTreeNode split(final BoundsSplit pBoundsSplit) {
			final int width = this.getWidth();
			final int height = this.getHeight();

			if(width <= 2 && height <= 2) {
				throw new BoundsSplitException();
			}

			final int left = this.getLeft(pBoundsSplit);
			final int top = this.getTop(pBoundsSplit);
			final int right = this.getRight(pBoundsSplit);
			final int bottom = this.getBottom(pBoundsSplit);

			return new IntQuadTreeNode(this.mLevel + 1, left, top, right, bottom);
		}

		@Override
		protected boolean contains(final IIntBounds pIntBounds) {
			return this.contains(pIntBounds.getLeft(), pIntBounds.getTop(), pIntBounds.getRight(), pIntBounds.getBottom());
		}

		@Override
		protected boolean contains(final BoundsSplit pBoundsSplit, final IIntBounds pIntBounds) {
			return IntBoundsUtils.contains(this.getLeft(pBoundsSplit), this.getTop(pBoundsSplit), this.getRight(pBoundsSplit), this.getBottom(pBoundsSplit), pIntBounds.getLeft(), pIntBounds.getTop(), pIntBounds.getRight(), pIntBounds.getBottom());
		}

		@Override
		protected boolean intersects(final IIntBounds pIntBounds) {
			return IntBoundsUtils.intersects(this.mLeft, this.mTop, this.mRight, this.mBottom, pIntBounds.getLeft(), pIntBounds.getTop(), pIntBounds.getRight(), pIntBounds.getBottom());
		}

		@Override
		protected boolean intersects(final IIntBounds pIntBoundsA, final IIntBounds pIntBoundsB) {
			return IntBoundsUtils.intersects(pIntBoundsA, pIntBoundsB);
		}

		@Override
		protected boolean containedBy(final IIntBounds pBounds) {
			return IntBoundsUtils.contains(pBounds.getLeft(), pBounds.getTop(), pBounds.getRight(), pBounds.getBottom(), this.mLeft, this.mTop, this.mRight, this.mBottom);
		}

		@Override
		protected void appendBoundsToString(final StringBuilder pStringBuilder) {
			pStringBuilder
				.append("[Left: ")
				.append(this.mLeft)
				.append(", Top: ")
				.append(this.mTop)
				.append(", Right: ")
				.append(this.mRight)
				.append(", Bottom: ")
				.append(this.mBottom)
				.append("]");
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

		public boolean intersects(final int pLeft, final int pTop, final int pRight, final int pBottom) {
			return IntBoundsUtils.intersects(this.mLeft, this.mTop, this.mRight, this.mBottom, pLeft, pTop, pRight, pBottom);
		}

		public boolean contains(final int pLeft, final int pTop, final int pRight, final int pBottom) {
			return IntBoundsUtils.contains(this.mLeft, this.mTop, this.mRight, this.mBottom, pLeft, pTop, pRight, pBottom);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
