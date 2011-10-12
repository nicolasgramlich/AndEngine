package org.anddev.andengine.util.spatial.quadtree;

import java.util.LinkedList;
import java.util.List;

import org.anddev.andengine.util.IMatcher;
import org.anddev.andengine.util.spatial.ISpatialItem;
import org.anddev.andengine.util.spatial.adt.bounds.BoundsSplit;
import org.anddev.andengine.util.spatial.adt.bounds.FloatBounds;
import org.anddev.andengine.util.spatial.adt.bounds.IFloatBounds;
import org.anddev.andengine.util.spatial.adt.bounds.util.FloatBoundsUtils;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:15:21 - 10.10.2011
 */
public class FloatQuadTree<T extends ISpatialItem<IFloatBounds>> extends QuadTree<IFloatBounds, T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final FloatBounds mQueryFloatBounds = new FloatBounds(0, 0, 0, 0);

	// ===========================================================
	// Constructors
	// ===========================================================

	public FloatQuadTree(final IFloatBounds pFloatBounds) {
		super(pFloatBounds);
	}

	public FloatQuadTree(final IFloatBounds pFloatBounds, final int pMaxLevel) {
		super(pFloatBounds, pMaxLevel);
	}

	@Override
	protected FloatQuadTreeNode initRoot(final IFloatBounds pFloatBounds) {
		return new FloatQuadTreeNode(QuadTree.LEVEL_ROOT, pFloatBounds);
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

	public synchronized List<T> query(final float pX, final float pY) {
		return this.query(pX, pY, new LinkedList<T>());
	}

	public synchronized List<T> query(final float pX, final float pY, final List<T> pResult) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.query(this.mQueryFloatBounds, pResult);
	}

	public synchronized List<T> query(final float pX, final float pY, final IMatcher<T> pMatcher) {
		return this.query(pX, pY, pMatcher, new LinkedList<T>());
	}

	public synchronized List<T> query(final float pX, final float pY, final IMatcher<T> pMatcher, final List<T> pResult) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.query(this.mQueryFloatBounds, pMatcher, pResult);
	}

	public synchronized List<T> query(final float pLeft, final float pTop, final float pRight, final float pBottom) {
		return this.query(pLeft, pTop, pRight, pBottom, new LinkedList<T>());
	}

	public synchronized List<T> query(final float pLeft, final float pTop, final float pRight, final float pBottom, final List<T> pResult) {
		this.mQueryFloatBounds.set(pLeft, pTop, pRight, pBottom);
		return this.query(this.mQueryFloatBounds, pResult);
	}

	public synchronized List<T> query(final float pLeft, final float pTop, final float pRight, final float pBottom, final IMatcher<T> pMatcher) {
		return this.query(pLeft, pTop, pRight, pBottom, pMatcher, new LinkedList<T>());
	}

	public synchronized List<T> query(final float pLeft, final float pTop, final float pRight, final float pBottom, final IMatcher<T> pMatcher, final List<T> pResult) {
		this.mQueryFloatBounds.set(pLeft, pTop, pRight, pBottom);
		return this.query(this.mQueryFloatBounds, pMatcher, pResult);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class FloatQuadTreeNode extends QuadTreeNode {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final float mLeft;
		private final float mTop;
		private final float mRight;
		private final float mBottom;

		// ===========================================================
		// Constructors
		// ===========================================================

		public FloatQuadTreeNode(final int pLevel, final IFloatBounds pFloatBounds) {
			this(pLevel, pFloatBounds.getLeft(), pFloatBounds.getTop(), pFloatBounds.getRight(), pFloatBounds.getBottom());
		}

		public FloatQuadTreeNode(final int pLevel, final float pLeft, final float pTop, final float pRight, final float pBottom) {
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

		public float getLeft() {
			return this.mLeft;
		}
		
		public float getTop() {
			return this.mTop;
		}

		public float getRight() {
			return this.mRight;
		}

		public float getBottom() {
			return this.mBottom;
		}

		public float getWidth() {
			return this.mRight - this.mLeft;
		}

		public float getHeight() {
			return this.mBottom - this.mTop;
		}

		// ===========================================================
		// Methods for/from SuperClass/Floaterfaces
		// ===========================================================

		@Override
		protected FloatQuadTreeNode split(final BoundsSplit pBoundsSplit) {
			final float left = this.getLeft(pBoundsSplit);
			final float top = this.getTop(pBoundsSplit);
			final float right = this.getRight(pBoundsSplit);
			final float bottom = this.getBottom(pBoundsSplit);

			return new FloatQuadTreeNode(this.mLevel + 1, left, top, right, bottom);
		}

		@Override
		protected boolean contains(final IFloatBounds pFloatBounds) {
			return this.contains(pFloatBounds.getLeft(), pFloatBounds.getTop(), pFloatBounds.getRight(), pFloatBounds.getBottom());
		}

		@Override
		protected boolean contains(final BoundsSplit pBoundsSplit, final IFloatBounds pFloatBounds) {
			return FloatBoundsUtils.contains(this.getLeft(pBoundsSplit), this.getTop(pBoundsSplit), this.getRight(pBoundsSplit), this.getBottom(pBoundsSplit), pFloatBounds.getLeft(), pFloatBounds.getTop(), pFloatBounds.getRight(), pFloatBounds.getBottom());
		}

		@Override
		protected boolean intersects(final IFloatBounds pFloatBounds) {
			return FloatBoundsUtils.intersects(this.mLeft, this.mTop, this.mRight, this.mBottom, pFloatBounds.getLeft(), pFloatBounds.getTop(), pFloatBounds.getRight(), pFloatBounds.getBottom());
		}

		@Override
		protected boolean intersects(final IFloatBounds pFloatBoundsA, final IFloatBounds pFloatBoundsB) {
			return FloatBoundsUtils.intersects(pFloatBoundsA, pFloatBoundsB);
		}

		@Override
		protected boolean containedBy(final IFloatBounds pFloatBounds) {
			return FloatBoundsUtils.contains(pFloatBounds.getLeft(), pFloatBounds.getTop(), pFloatBounds.getRight(), pFloatBounds.getBottom(), this.mLeft, this.mTop, this.mRight, this.mBottom);
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

		private float getLeft(final BoundsSplit pBoundsSplit) {
			final float halfWidth = this.getWidth() / 2;

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

		private float getTop(final BoundsSplit pBoundsSplit) {
			final float halfHeight = this.getHeight() / 2;

			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mTop;
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
		
		private float getRight(final BoundsSplit pBoundsSplit) {
			final float halfWidth = this.getWidth() / 2;
			
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

		private float getBottom(final BoundsSplit pBoundsSplit) {
			final float halfHeight = this.getHeight() / 2;

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

		public boolean intersects(final float pLeft, final float pTop, final float pRight, final float pBottom) {
			return FloatBoundsUtils.intersects(this.mLeft, this.mTop, this.mRight, this.mBottom, pLeft, pTop, pRight, pBottom);
		}

		public boolean contains(final float pLeft, final float pTop, final float pRight, final float pBottom) {
			return FloatBoundsUtils.contains(this.mLeft, this.mTop, this.mRight, this.mBottom, pLeft, pTop, pRight, pBottom);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
