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

	public synchronized List<T> query(final int pMinX, final int pMinY, final int pMaxX, final int pMaxY) {
		return this.query(pMinX, pMinY, pMaxX, pMaxY, new LinkedList<T>());
	}

	public synchronized List<T> query(final int pMinX, final int pMinY, final int pMaxX, final int pMaxY, final List<T> pResult) {
		this.mQueryIntBounds.set(pMinX, pMinY, pMaxX, pMaxY);
		return this.query(this.mQueryIntBounds, pResult);
	}

	public synchronized List<T> query(final int pMinX, final int pMinY, final int pMaxX, final int pMaxY, final IMatcher<T> pMatcher) {
		return this.query(pMinX, pMinY, pMaxX, pMaxY, pMatcher, new LinkedList<T>());
	}

	public synchronized List<T> query(final int pMinX, final int pMinY, final int pMaxX, final int pMaxY, final IMatcher<T> pMatcher, final List<T> pResult) {
		this.mQueryIntBounds.set(pMinX, pMinY, pMaxX, pMaxY);
		return this.query(this.mQueryIntBounds, pMatcher, pResult);
	}

	public synchronized boolean containsAny(final int pX, final int pY) {
		this.mQueryIntBounds.set(pX, pY);
		return this.containsAny(this.mQueryIntBounds);
	}

	public synchronized boolean containsAny(final int pMinX, final int pMinY, final int pMaxX, final int pMaxY) {
		this.mQueryIntBounds.set(pMinX, pMinY, pMaxX, pMaxY);
		return this.containsAny(this.mQueryIntBounds);
	}

	public synchronized boolean containsAny(final int pX, final int pY, final IMatcher<T> pMatcher) {
		this.mQueryIntBounds.set(pX, pY);
		return this.containsAny(this.mQueryIntBounds, pMatcher);
	}

	public synchronized boolean containsAny(final int pMinX, final int pMinY, final int pMaxX, final int pMaxY, final IMatcher<T> pMatcher) {
		this.mQueryIntBounds.set(pMinX, pMinY, pMaxX, pMaxY);
		return this.containsAny(this.mQueryIntBounds, pMatcher);
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

		private final int mMinX;
		private final int mMinY;
		private final int mMaxX;
		private final int mMaxY;

		// ===========================================================
		// Constructors
		// ===========================================================

		public IntQuadTreeNode(final int pLevel, final IIntBounds pIntBounds) {
			this(pLevel, pIntBounds.getMinX(), pIntBounds.getMinY(), pIntBounds.getMaxX(), pIntBounds.getMaxY());
		}

		public IntQuadTreeNode(final int pLevel, final int pMinX, final int pMinY, final int pMaxX, final int pMaxY) {
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

		public int getMinX() {
			return this.mMinX;
		}
		
		public int getMinY() {
			return this.mMinY;
		}

		public int getMaxX() {
			return this.mMaxX;
		}

		public int getMaxY() {
			return this.mMaxY;
		}

		public int getWidth() {
			return this.mMaxX - this.mMinX + 1;
		}

		public int getHeight() {
			return this.mMaxY - this.mMinY + 1;
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

			final int minX = this.getMinX(pBoundsSplit);
			final int minY = this.getMinY(pBoundsSplit);
			final int maxX = this.getMaxX(pBoundsSplit);
			final int maxY = this.getMaxY(pBoundsSplit);

			return new IntQuadTreeNode(this.mLevel + 1, minX, minY, maxX, maxY);
		}

		@Override
		protected boolean contains(final IIntBounds pIntBounds) {
			return this.contains(pIntBounds.getMinX(), pIntBounds.getMinY(), pIntBounds.getMaxX(), pIntBounds.getMaxY());
		}

		@Override
		protected boolean contains(final BoundsSplit pBoundsSplit, final IIntBounds pIntBounds) {
			return IntBoundsUtils.contains(this.getMinX(pBoundsSplit), this.getMinY(pBoundsSplit), this.getMaxX(pBoundsSplit), this.getMaxY(pBoundsSplit), pIntBounds.getMinX(), pIntBounds.getMinY(), pIntBounds.getMaxX(), pIntBounds.getMaxY());
		}

		@Override
		protected boolean intersects(final IIntBounds pIntBounds) {
			return IntBoundsUtils.intersects(this.mMinX, this.mMinY, this.mMaxX, this.mMaxY, pIntBounds.getMinX(), pIntBounds.getMinY(), pIntBounds.getMaxX(), pIntBounds.getMaxY());
		}

		@Override
		protected boolean intersects(final IIntBounds pIntBoundsA, final IIntBounds pIntBoundsB) {
			return IntBoundsUtils.intersects(pIntBoundsA, pIntBoundsB);
		}

		@Override
		protected boolean containedBy(final IIntBounds pBounds) {
			return IntBoundsUtils.contains(pBounds.getMinX(), pBounds.getMinY(), pBounds.getMaxX(), pBounds.getMaxY(), this.mMinX, this.mMinY, this.mMaxX, this.mMaxY);
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

		private int getMinX(final BoundsSplit pBoundsSplit) {
			final int width = this.getWidth();
			final int halfWidth = width / 2;

			if(width <= 2) {
				switch(pBoundsSplit) {
					case TOP_LEFT:
					case BOTTOM_LEFT:
						return this.mMinX;
					case BOTTOM_RIGHT:
					case TOP_RIGHT:
						throw new BoundsSplitException();
					default:
						throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
				}
			} else {
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
		}
		
		private int getMinY(final BoundsSplit pBoundsSplit) {
			final int height = this.getHeight();
			final int halfHeight = height / 2;
			
			if(height <= 2) {
				switch(pBoundsSplit) {
					case TOP_LEFT:
					case TOP_RIGHT:
						return  this.mMinY;
					case BOTTOM_LEFT:
					case BOTTOM_RIGHT:
						throw new BoundsSplitException();
					default:
						throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
				}
			} else {
				switch(pBoundsSplit) {
					case TOP_LEFT:
						return  this.mMinY;
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
		}

		private int getMaxX(final BoundsSplit pBoundsSplit) {
			final int width = this.getWidth();
			final int halfWidth = width / 2;

			if(width <= 2) {
				switch(pBoundsSplit) {
					case TOP_LEFT:
					case BOTTOM_LEFT:
						return this.mMaxX;
					case BOTTOM_RIGHT:
					case TOP_RIGHT:
						throw new BoundsSplitException();
					default:
						throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
				}
			} else {
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
		}

		private int getMaxY(final BoundsSplit pBoundsSplit) {
			final int height = this.getHeight();
			final int halfHeight = height / 2;

			if(height <= 2) {
				switch(pBoundsSplit) {
					case TOP_LEFT:
					case TOP_RIGHT:
						return this.mMaxY;
					case BOTTOM_LEFT:
					case BOTTOM_RIGHT:
						throw new BoundsSplitException();
					default:
						throw new IllegalArgumentException("Unexpected " + BoundsSplit.class.getSimpleName() + ": '" + pBoundsSplit + "'.");
				}
			} else {
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
		}

		public boolean intersects(final int pMinX, final int pMinY, final int pMaxX, final int pMaxY) {
			return IntBoundsUtils.intersects(this.mMinX, this.mMinY, this.mMaxX, this.mMaxY, pMinX, pMinY, pMaxX, pMaxY);
		}

		public boolean contains(final int pMinX, final int pMinY, final int pMaxX, final int pMaxY) {
			return IntBoundsUtils.contains(this.mMinX, this.mMinY, this.mMaxX, this.mMaxY, pMinX, pMinY, pMaxX, pMaxY);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
