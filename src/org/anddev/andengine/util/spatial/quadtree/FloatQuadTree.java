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

	public synchronized List<T> query(final float pMinX, final float pMinY, final float pMaxX, final float pMaxY) {
		return this.query(pMinX, pMinY, pMaxX, pMaxY, new LinkedList<T>());
	}

	public synchronized List<T> query(final float pMinX, final float pMinY, final float pMaxX, final float pMaxY, final List<T> pResult) {
		this.mQueryFloatBounds.set(pMinX, pMinY, pMaxX, pMaxY);
		return this.query(this.mQueryFloatBounds, pResult);
	}

	public synchronized List<T> query(final float pMinX, final float pMinY, final float pMaxX, final float pMaxY, final IMatcher<T> pMatcher) {
		return this.query(pMinX, pMinY, pMaxX, pMaxY, pMatcher, new LinkedList<T>());
	}

	public synchronized List<T> query(final float pMinX, final float pMinY, final float pMaxX, final float pMaxY, final IMatcher<T> pMatcher, final List<T> pResult) {
		this.mQueryFloatBounds.set(pMinX, pMinY, pMaxX, pMaxY);
		return this.query(this.mQueryFloatBounds, pMatcher, pResult);
	}

	public synchronized boolean containsAny(final float pX, final float pY) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.containsAny(this.mQueryFloatBounds);
	}

	public synchronized boolean containsAny(final float pMinX, final float pMinY, final float pMaxX, final float pMaxY) {
		this.mQueryFloatBounds.set(pMinX, pMinY, pMaxX, pMaxY);
		return this.containsAny(this.mQueryFloatBounds);
	}

	public synchronized boolean containsAny(final float pX, final float pY, final IMatcher<T> pMatcher) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.containsAny(this.mQueryFloatBounds, pMatcher);
	}

	public synchronized boolean containsAny(final float pMinX, final float pMinY, final float pMaxX, final float pMaxY, final IMatcher<T> pMatcher) {
		this.mQueryFloatBounds.set(pMinX, pMinY, pMaxX, pMaxY);
		return this.containsAny(this.mQueryFloatBounds, pMatcher);
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

		private final float mMinX;
		private final float mMinY;
		private final float mMaxX;
		private final float mMaxY;

		// ===========================================================
		// Constructors
		// ===========================================================

		public FloatQuadTreeNode(final int pLevel, final IFloatBounds pFloatBounds) {
			this(pLevel, pFloatBounds.getMinX(), pFloatBounds.getMinY(), pFloatBounds.getMaxX(), pFloatBounds.getMaxY());
		}

		public FloatQuadTreeNode(final int pLevel, final float pMinX, final float pMinY, final float pMaxX, final float pMaxY) {
			super(pLevel);

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
		// Getter & Setter
		// ===========================================================

		public float getMinX() {
			return this.mMinX;
		}
		
		public float getMinY() {
			return this.mMinY;
		}

		public float getMaxX() {
			return this.mMaxX;
		}

		public float getMaxY() {
			return this.mMaxY;
		}

		public float getWidth() {
			return this.mMaxX - this.mMinX;
		}

		public float getHeight() {
			return this.mMaxY - this.mMinY;
		}

		// ===========================================================
		// Methods for/from SuperClass/Floaterfaces
		// ===========================================================

		@Override
		protected FloatQuadTreeNode split(final BoundsSplit pBoundsSplit) {
			final float minX = this.getMinX(pBoundsSplit);
			final float minY = this.getMinY(pBoundsSplit);
			final float maxX = this.getMaxX(pBoundsSplit);
			final float maxY = this.getMaxY(pBoundsSplit);

			return new FloatQuadTreeNode(this.mLevel + 1, minX, minY, maxX, maxY);
		}

		@Override
		protected boolean contains(final IFloatBounds pFloatBounds) {
			return this.contains(pFloatBounds.getMinX(), pFloatBounds.getMinY(), pFloatBounds.getMaxX(), pFloatBounds.getMaxY());
		}

		@Override
		protected boolean contains(final BoundsSplit pBoundsSplit, final IFloatBounds pFloatBounds) {
			return FloatBoundsUtils.contains(this.getMinX(pBoundsSplit), this.getMinY(pBoundsSplit), this.getMaxX(pBoundsSplit), this.getMaxY(pBoundsSplit), pFloatBounds.getMinX(), pFloatBounds.getMinY(), pFloatBounds.getMaxX(), pFloatBounds.getMaxY());
		}

		@Override
		protected boolean intersects(final IFloatBounds pFloatBounds) {
			return FloatBoundsUtils.intersects(this.mMinX, this.mMinY, this.mMaxX, this.mMaxY, pFloatBounds.getMinX(), pFloatBounds.getMinY(), pFloatBounds.getMaxX(), pFloatBounds.getMaxY());
		}

		@Override
		protected boolean intersects(final IFloatBounds pFloatBoundsA, final IFloatBounds pFloatBoundsB) {
			return FloatBoundsUtils.intersects(pFloatBoundsA, pFloatBoundsB);
		}

		@Override
		protected boolean containedBy(final IFloatBounds pFloatBounds) {
			return FloatBoundsUtils.contains(pFloatBounds.getMinX(), pFloatBounds.getMinY(), pFloatBounds.getMaxX(), pFloatBounds.getMaxY(), this.mMinX, this.mMinY, this.mMaxX, this.mMaxY);
		}

		@Override
		protected void appendBoundsToString(final StringBuilder pStringBuilder) {
			pStringBuilder
				.append("[MinX: ")
				.append(this.mMinX)
				.append(", MinY: ")
				.append(this.mMinY)
				.append(", MaxX: ")
				.append(this.mMaxX)
				.append(", MaxY: ")
				.append(this.mMaxY)
				.append("]");
		}

		// ===========================================================
		// Methods
		// ===========================================================

		private float getMinX(final BoundsSplit pBoundsSplit) {
			final float halfWidth = this.getWidth() / 2;

			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mMinX;
				case TOP_RIGHT:
					return this.mMinX + halfWidth;
				case BOTTOM_LEFT:
					return this.mMinX;
				case BOTTOM_RIGHT:
					return this.mMinX + halfWidth;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}

		private float getMinY(final BoundsSplit pBoundsSplit) {
			final float halfHeight = this.getHeight() / 2;

			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mMinY;
				case TOP_RIGHT:
					return this.mMinY;
				case BOTTOM_LEFT:
					return this.mMinY + halfHeight;
				case BOTTOM_RIGHT:
					return this.mMinY + halfHeight;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}
		
		private float getMaxX(final BoundsSplit pBoundsSplit) {
			final float halfWidth = this.getWidth() / 2;
			
			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mMinX + halfWidth;
				case TOP_RIGHT:
					return this.mMaxX;
				case BOTTOM_LEFT:
					return this.mMinX + halfWidth;
				case BOTTOM_RIGHT:
					return this.mMaxX;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}

		private float getMaxY(final BoundsSplit pBoundsSplit) {
			final float halfHeight = this.getHeight() / 2;

			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mMinY + halfHeight;
				case TOP_RIGHT:
					return this.mMinY + halfHeight;
				case BOTTOM_LEFT:
					return this.mMaxY;
				case BOTTOM_RIGHT:
					return this.mMaxY;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}

		public boolean intersects(final float pMinX, final float pMinY, final float pMaxX, final float pMaxY) {
			return FloatBoundsUtils.intersects(this.mMinX, this.mMinY, this.mMaxX, this.mMaxY, pMinX, pMinY, pMaxX, pMaxY);
		}

		public boolean contains(final float pMinX, final float pMinY, final float pMaxX, final float pMaxY) {
			return FloatBoundsUtils.contains(this.mMinX, this.mMinY, this.mMaxX, this.mMaxY, pMinX, pMinY, pMaxX, pMaxY);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
