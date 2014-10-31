package org.andengine.util.adt.spatial.quadtree;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.IMatcher;
import org.andengine.util.adt.bounds.BoundsSplit;
import org.andengine.util.adt.bounds.FloatBounds;
import org.andengine.util.adt.bounds.IFloatBounds;
import org.andengine.util.adt.spatial.ISpatialItem;
import org.andengine.util.adt.spatial.bounds.util.FloatBoundsUtils;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:15:21 - 10.10.2011
 */
public class FloatQuadTree<T extends ISpatialItem<IFloatBounds>> extends QuadTree<IFloatBounds, T> implements IFloatBounds {
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

	public FloatQuadTree(final float pXMin, final float pYMin, final float pXMax, final float pYMax) {
		super(new FloatBounds(pXMin, pYMin, pXMax, pYMax));
	}

	public FloatQuadTree(final IFloatBounds pFloatBounds, final int pMaxLevel) {
		super(pFloatBounds, pMaxLevel);
	}

	public FloatQuadTree(final float pXMin, final float pYMin, final float pXMax, final float pYMax, final int pMaxLevel) {
		super(new FloatBounds(pXMin, pYMin, pXMax, pYMax), pMaxLevel);
	}

	@Override
	protected FloatQuadTreeNode initRoot(final IFloatBounds pFloatBounds) {
		return new FloatQuadTreeNode(QuadTree.LEVEL_ROOT, pFloatBounds);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public float getXMin() {
		return this.getRoot().getXMin();
	}

	@Override
	public float getYMin() {
		return this.getRoot().getYMin();
	}

	@Override
	public float getXMax() {
		return this.getRoot().getXMax();
	}

	@Override
	public float getYMax() {
		return this.getRoot().getYMax();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@SuppressWarnings("unchecked")
	@Override
	protected FloatQuadTreeNode getRoot() {
		return (FloatQuadTreeNode) this.mRoot;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized ArrayList<T> query(final float pX, final float pY) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.query(this.mQueryFloatBounds);
	}

	public synchronized <L extends List<T>> L query(final float pX, final float pY, final L pResult) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.query(this.mQueryFloatBounds, pResult);
	}

	public synchronized ArrayList<T> query(final float pX, final float pY, final IMatcher<T> pMatcher) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.query(this.mQueryFloatBounds, pMatcher);
	}

	public synchronized <L extends List<T>> L query(final float pX, final float pY, final IMatcher<T> pMatcher, final L pResult) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.query(this.mQueryFloatBounds, pMatcher, pResult);
	}

	public synchronized ArrayList<T> query(final float pXMin, final float pYMin, final float pXMax, final float pYMax) {
		this.mQueryFloatBounds.set(pXMin, pYMin, pXMax, pYMax);
		return this.query(this.mQueryFloatBounds);
	}

	public synchronized <L extends List<T>> L query(final float pXMin, final float pYMin, final float pXMax, final float pYMax, final L pResult) {
		this.mQueryFloatBounds.set(pXMin, pYMin, pXMax, pYMax);
		return this.query(this.mQueryFloatBounds, pResult);
	}

	public synchronized ArrayList<T> query(final float pXMin, final float pYMin, final float pXMax, final float pYMax, final IMatcher<T> pMatcher) {
		this.mQueryFloatBounds.set(pXMin, pYMin, pXMax, pYMax);
		return this.query(this.mQueryFloatBounds, pMatcher);
	}

	public synchronized <L extends List<T>> L query(final float pXMin, final float pYMin, final float pXMax, final float pYMax, final IMatcher<T> pMatcher, final L pResult) {
		this.mQueryFloatBounds.set(pXMin, pYMin, pXMax, pYMax);
		return this.query(this.mQueryFloatBounds, pMatcher, pResult);
	}

	public synchronized <S extends T> List<S> queryForSubclass(final float pX, final float pY, final IMatcher<T> pMatcher, final List<S> pResult) throws ClassCastException {
		this.mQueryFloatBounds.set(pX, pY);
		return this.queryForSubclass(this.mQueryFloatBounds, pMatcher, pResult);
	}

	public synchronized <S extends T> List<S> queryForSubclass(final float pXMin, final float pYMin, final float pXMax, final float pYMax, final IMatcher<T> pMatcher, final List<S> pResult) throws ClassCastException {
		this.mQueryFloatBounds.set(pXMin, pYMin, pXMax, pYMax);
		return this.queryForSubclass(this.mQueryFloatBounds, pMatcher, pResult);
	}

	public synchronized boolean containsAny(final float pX, final float pY) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.containsAny(this.mQueryFloatBounds);
	}

	public synchronized boolean containsAny(final float pXMin, final float pYMin, final float pXMax, final float pYMax) {
		this.mQueryFloatBounds.set(pXMin, pYMin, pXMax, pYMax);
		return this.containsAny(this.mQueryFloatBounds);
	}

	public synchronized boolean containsAny(final float pX, final float pY, final IMatcher<T> pMatcher) {
		this.mQueryFloatBounds.set(pX, pY);
		return this.containsAny(this.mQueryFloatBounds, pMatcher);
	}

	public synchronized boolean containsAny(final float pXMin, final float pYMin, final float pXMax, final float pYMax, final IMatcher<T> pMatcher) {
		this.mQueryFloatBounds.set(pXMin, pYMin, pXMax, pYMax);
		return this.containsAny(this.mQueryFloatBounds, pMatcher);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class FloatQuadTreeNode extends QuadTreeNode implements IFloatBounds {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final float mXMin;
		private final float mYMin;
		private final float mXMax;
		private final float mYMax;

		// ===========================================================
		// Constructors
		// ===========================================================

		public FloatQuadTreeNode(final int pLevel, final IFloatBounds pFloatBounds) {
			this(pLevel, pFloatBounds.getXMin(), pFloatBounds.getYMin(), pFloatBounds.getXMax(), pFloatBounds.getYMax());
		}

		public FloatQuadTreeNode(final int pLevel, final float pXMin, final float pYMin, final float pXMax, final float pYMax) {
			super(pLevel);

			this.mXMin = pXMin;
			this.mYMin = pYMin;
			this.mXMax = pXMax;
			this.mYMax = pYMax;

			if(pXMin > pXMax) {
				throw new IllegalArgumentException("pXMin must be smaller or equal to pXMax.");
			}
			if(pYMin > pYMax) {
				throw new IllegalArgumentException("pYMin must be smaller or equal to pYMax.");
			}
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		@Override
		public float getXMin() {
			return this.mXMin;
		}

		@Override
		public float getYMin() {
			return this.mYMin;
		}

		@Override
		public float getXMax() {
			return this.mXMax;
		}

		@Override
		public float getYMax() {
			return this.mYMax;
		}

		public float getWidth() {
			return this.mXMax - this.mXMin;
		}

		public float getHeight() {
			return this.mYMax - this.mYMin;
		}

		// ===========================================================
		// Methods for/from SuperClass/Floaterfaces
		// ===========================================================

		@Override
		protected FloatQuadTreeNode split(final BoundsSplit pBoundsSplit) {
			final float xMin = this.getXMin(pBoundsSplit);
			final float yMin = this.getYMin(pBoundsSplit);
			final float xMax = this.getXMax(pBoundsSplit);
			final float yMax = this.getYMax(pBoundsSplit);

			return new FloatQuadTreeNode(this.mLevel + 1, xMin, yMin, xMax, yMax);
		}

		@Override
		protected boolean contains(final IFloatBounds pFloatBounds) {
			return this.contains(pFloatBounds.getXMin(), pFloatBounds.getYMin(), pFloatBounds.getXMax(), pFloatBounds.getYMax());
		}

		@Override
		protected boolean contains(final BoundsSplit pBoundsSplit, final IFloatBounds pFloatBounds) {
			return FloatBoundsUtils.contains(this.getXMin(pBoundsSplit), this.getYMin(pBoundsSplit), this.getXMax(pBoundsSplit), this.getYMax(pBoundsSplit), pFloatBounds.getXMin(), pFloatBounds.getYMin(), pFloatBounds.getXMax(), pFloatBounds.getYMax());
		}

		@Override
		protected boolean intersects(final IFloatBounds pFloatBounds) {
			return FloatBoundsUtils.intersects(this.mXMin, this.mYMin, this.mXMax, this.mYMax, pFloatBounds.getXMin(), pFloatBounds.getYMin(), pFloatBounds.getXMax(), pFloatBounds.getYMax());
		}

		@Override
		protected boolean intersects(final IFloatBounds pFloatBoundsA, final IFloatBounds pFloatBoundsB) {
			return FloatBoundsUtils.intersects(pFloatBoundsA, pFloatBoundsB);
		}

		@Override
		protected boolean containedBy(final IFloatBounds pFloatBounds) {
			return FloatBoundsUtils.contains(pFloatBounds.getXMin(), pFloatBounds.getYMin(), pFloatBounds.getXMax(), pFloatBounds.getYMax(), this.mXMin, this.mYMin, this.mXMax, this.mYMax);
		}

		@Override
		protected void appendBoundsToString(final StringBuilder pStringBuilder) {
			pStringBuilder
				.append("[XMin: ")
				.append(this.mXMin)
				.append(", YMin: ")
				.append(this.mYMin)
				.append(", XMax: ")
				.append(this.mXMax)
				.append(", YMax: ")
				.append(this.mYMax)
				.append("]");
		}

		// ===========================================================
		// Methods
		// ===========================================================

		private float getXMin(final BoundsSplit pBoundsSplit) {
			final float halfWidth = this.getWidth() / 2;

			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mXMin;
				case TOP_RIGHT:
					return this.mXMin + halfWidth;
				case BOTTOM_LEFT:
					return this.mXMin;
				case BOTTOM_RIGHT:
					return this.mXMin + halfWidth;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}

		private float getYMin(final BoundsSplit pBoundsSplit) {
			final float halfHeight = this.getHeight() / 2;

			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mYMin;
				case TOP_RIGHT:
					return this.mYMin;
				case BOTTOM_LEFT:
					return this.mYMin + halfHeight;
				case BOTTOM_RIGHT:
					return this.mYMin + halfHeight;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}
		
		private float getXMax(final BoundsSplit pBoundsSplit) {
			final float halfWidth = this.getWidth() / 2;
			
			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mXMin + halfWidth;
				case TOP_RIGHT:
					return this.mXMax;
				case BOTTOM_LEFT:
					return this.mXMin + halfWidth;
				case BOTTOM_RIGHT:
					return this.mXMax;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}

		private float getYMax(final BoundsSplit pBoundsSplit) {
			final float halfHeight = this.getHeight() / 2;

			switch(pBoundsSplit) {
				case TOP_LEFT:
					return this.mYMin + halfHeight;
				case TOP_RIGHT:
					return this.mYMin + halfHeight;
				case BOTTOM_LEFT:
					return this.mYMax;
				case BOTTOM_RIGHT:
					return this.mYMax;
				default:
					throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
			}
		}

		public boolean intersects(final float pXMin, final float pYMin, final float pXMax, final float pYMax) {
			return FloatBoundsUtils.intersects(this.mXMin, this.mYMin, this.mXMax, this.mYMax, pXMin, pYMin, pXMax, pYMax);
		}

		public boolean contains(final float pXMin, final float pYMin, final float pXMax, final float pYMax) {
			return FloatBoundsUtils.contains(this.mXMin, this.mYMin, this.mXMax, this.mYMax, pXMin, pYMin, pXMax, pYMax);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
