package org.anddev.andengine.util.spatial.quadtree;

import java.util.LinkedList;
import java.util.List;

import org.anddev.andengine.util.IMatcher;
import org.anddev.andengine.util.ParameterCallable;
import org.anddev.andengine.util.spatial.ISpatialItem;
import org.anddev.andengine.util.spatial.adt.bounds.IBounds;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:16:01 - 07.10.2011
 */
public class QuadTree<B extends IBounds, T extends ISpatialItem<B>> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int LEVEL_ROOT = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	private final QuadTreeNode<B, T> mRoot;
	private final B mBounds;

	// ===========================================================
	// Constructors
	// ===========================================================

	public QuadTree(final B pBounds) {
		this.mBounds = pBounds;
		this.mRoot = new QuadTreeNode<B, T>(QuadTree.LEVEL_ROOT, this.mBounds);
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

	public int getItemCount() {
		// TODO Items could be counted along the add/remove calls.
		return this.mRoot.getItemCount();
	}

	public void add(final T pItem) {
		this.mRoot.add(pItem);
	}

	public void remove(final T pItem) {
		this.mRoot.remove(pItem);
	}

	public List<T> query(final B pBounds) {
		return this.query(pBounds, new LinkedList<T>());
	}

	public List<T> query(final B pBounds, final List<T> pResult) {
		return this.mRoot.query(pBounds, pResult);
	}

	public List<T> query(final B pBounds, final IMatcher<T> pMatcher) {
		return this.query(pBounds, pMatcher, new LinkedList<T>());
	}

	public List<T> query(final B pBounds, final IMatcher<T> pMatcher, final List<T> pResult) {
		return this.mRoot.query(pBounds, pMatcher, pResult);
	}

	public void callItems(final ParameterCallable<T> pParameterCallable) {
		this.mRoot.callItems(pParameterCallable);
	}

	public void callNodes(final ParameterCallable<QuadTreeNode<B, T>> pParameterCallable) {
		this.mRoot.callNodes(pParameterCallable);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
