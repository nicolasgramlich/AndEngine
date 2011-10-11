package org.anddev.andengine.util.spatial.quadtree;

import java.util.LinkedList;
import java.util.List;

import org.anddev.andengine.util.IMatcher;
import org.anddev.andengine.util.spatial.ISpatialItem;
import org.anddev.andengine.util.spatial.adt.bounds.IBounds.BoundsSplit;
import org.anddev.andengine.util.spatial.adt.bounds.IntBounds;
import org.anddev.andengine.util.spatial.adt.bounds.source.IIntBoundsSource;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:22:18 - 10.10.2011
 */
public class IntQuadTree<T extends ISpatialItem<IIntBoundsSource>> extends QuadTree<IIntBoundsSource, IntBounds, T> {
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

	public IntQuadTree(final IntBounds pIntBounds) {
		super(pIntBounds);
	}

	public IntQuadTree(final IntBounds pIntBounds, final int pMaxLevel) {
		super(pIntBounds, pMaxLevel);
	}

	@Override
	protected IntQuadTreeNode initRoot(final IntBounds pIntBounds) {
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

	public synchronized List<T> query(final int pLeft, final int pRight, final int pTop, final int pBottom) {
		return this.query(pLeft, pRight, pTop, pBottom, new LinkedList<T>());
	}

	public synchronized List<T> query(final int pLeft, final int pRight, final int pTop, final int pBottom, final List<T> pResult) {
		this.mQueryIntBounds.set(pLeft, pRight, pTop, pBottom);
		return this.query(this.mQueryIntBounds, pResult);
	}

	public synchronized List<T> query(final int pLeft, final int pRight, final int pTop, final int pBottom, final IMatcher<T> pMatcher) {
		return this.query(pLeft, pRight, pTop, pBottom, pMatcher, new LinkedList<T>());
	}

	public synchronized List<T> query(final int pLeft, final int pRight, final int pTop, final int pBottom, final IMatcher<T> pMatcher, final List<T> pResult) {
		this.mQueryIntBounds.set(pLeft, pRight, pTop, pBottom);
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

		// ===========================================================
		// Constructors
		// ===========================================================

		public IntQuadTreeNode(final int pLevel, final IntBounds pBounds) {
			super(pLevel, pBounds);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		protected IntQuadTreeNode split(final BoundsSplit pBoundsSplit) {
			return new IntQuadTreeNode(this.mLevel + 1, this.mBounds.split(pBoundsSplit));
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
