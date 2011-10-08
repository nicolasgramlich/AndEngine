package org.anddev.andengine.util.spatial.quadtree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.anddev.andengine.util.IMatcher;
import org.anddev.andengine.util.ParameterCallable;
import org.anddev.andengine.util.spatial.ISpatialItem;
import org.anddev.andengine.util.spatial.adt.bounds.IBounds;
import org.anddev.andengine.util.spatial.adt.bounds.IBounds.BoundsSplit;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:24:49 - 07.10.2011
 */
public class QuadTreeNode<B extends IBounds, T extends ISpatialItem<B>> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int LEVEL_MAX = 8;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mLevel;
	private final B mBounds;
	private final List<T> mItems = new ArrayList<T>(); // TODO Benchmark various list types

	private QuadTreeNode<B, T> mTopLeftChild;
	private QuadTreeNode<B, T> mTopRightChild;
	private QuadTreeNode<B, T> mBottomLeftChild;
	private QuadTreeNode<B, T> mBottomRightChild;

	// ===========================================================
	// Constructors
	// ===========================================================

	public QuadTreeNode(final int pLevel, final B pSpatialBounds) {
		this.mLevel = pLevel;
		this.mBounds = pSpatialBounds;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isEmpty() {
		return this.mTopLeftChild == null;
	}

	public B getBounds() {
		return this.mBounds;
	}

	public List<T> getItems() {
		return this.mItems;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getItemCount() {
		int count = this.mItems.size();

		if(this.mTopLeftChild != null) {
			count += this.mTopLeftChild.getItemCount();
			count += this.mTopRightChild.getItemCount();
			count += this.mBottomLeftChild.getItemCount();
			count += this.mBottomRightChild.getItemCount();
		}

		return count;
	}

	public void callItems(final ParameterCallable<T> pParameterCallable) {
		for(final T item : this.mItems) {
			pParameterCallable.call(item);
		}

		if(this.mTopLeftChild != null) {
			this.mTopLeftChild.callItems(pParameterCallable);
			this.mTopRightChild.callItems(pParameterCallable);
			this.mBottomLeftChild.callItems(pParameterCallable);
			this.mBottomRightChild.callItems(pParameterCallable);
		}
	}

	public void callNodes(final ParameterCallable<QuadTreeNode<B, T>> pParameterCallable) {
		pParameterCallable.call(this);

		if(this.mTopLeftChild != null) {
			this.mTopLeftChild.callNodes(pParameterCallable);
			this.mTopRightChild.callNodes(pParameterCallable);
			this.mBottomLeftChild.callNodes(pParameterCallable);
			this.mBottomRightChild.callNodes(pParameterCallable);
		}
	}

	public List<T> getItemsAndItemsBelow() {
		return this.getItemsAndItemsBelow(new LinkedList<T>());
	}

	/**
	 * @return the items of this {@link QuadTreeNode} and all children (recursively).
	 */
	public List<T> getItemsAndItemsBelow(final List<T> pResult) {
		pResult.addAll(this.mItems);

		if(this.mTopLeftChild != null) {
			this.mTopLeftChild.getItemsAndItemsBelow(pResult);
			this.mTopRightChild.getItemsAndItemsBelow(pResult);
			this.mBottomLeftChild.getItemsAndItemsBelow(pResult);
			this.mBottomRightChild.getItemsAndItemsBelow(pResult);
		}

		return pResult;
	}

	public List<T> getItemsAndItemsBelow(final IMatcher<T> pMatcher) {
		return this.getItemsAndItemsBelow(pMatcher, new LinkedList<T>());
	}

	public List<T> getItemsAndItemsBelow(final IMatcher<T> pMatcher, final List<T> pResult) {
		for(final T item : this.mItems) {
			if(pMatcher.matches(item)) {
				pResult.add(item);
			}
		}

		if(this.mTopLeftChild != null) {
			this.mTopLeftChild.getItemsAndItemsBelow(pMatcher, pResult);
			this.mTopRightChild.getItemsAndItemsBelow(pMatcher, pResult);
			this.mBottomLeftChild.getItemsAndItemsBelow(pMatcher, pResult);
			this.mBottomRightChild.getItemsAndItemsBelow(pMatcher, pResult);
		}

		return pResult;
	}

	public List<T> query(final B pSpatialBounds) {
		return this.query(pSpatialBounds, new LinkedList<T>());
	}

	public List<T> query(final B pSpatialBounds, final List<T> pResult) {
		/* Test against all items in this node. */
		for(final T item : this.mItems) {
			if(pSpatialBounds.intersects(item.getBounds())) {
				pResult.add(item);
			}
		}

		/* Early exit when there are no children. */
		if(this.mTopLeftChild == null) {
			return pResult;
		}

		/* Check children. */
		if(this.queryChild(pSpatialBounds, pResult, this.mTopLeftChild)) {
			return pResult;
		} else if(this.queryChild(pSpatialBounds, pResult, this.mTopRightChild)) {
			return pResult;
		} else if(this.queryChild(pSpatialBounds, pResult, this.mBottomLeftChild)) {
			return pResult;
		} else if(this.queryChild(pSpatialBounds, pResult, this.mBottomRightChild)) {
			return pResult;
		} else {
			return pResult;
		}
	}

	public List<T> query(final B pSpatialBounds, final IMatcher<T> pMatcher, final List<T> pResult) {
		/* Test against all items in this node. */
		for(final T item : this.mItems) {
			if(pSpatialBounds.intersects(item.getBounds()) && pMatcher.matches(item)) {
				pResult.add(item);
			}
		}

		/* Early exit when there are no children. */
		if(this.mTopLeftChild == null) {
			return pResult;
		}

		/* Check children. */
		if(this.queryChild(pSpatialBounds, pMatcher, pResult, this.mTopLeftChild)) {
			return pResult;
		} else if(this.queryChild(pSpatialBounds, pMatcher, pResult, this.mTopRightChild)) {
			return pResult;
		} else if(this.queryChild(pSpatialBounds, pMatcher, pResult, this.mBottomLeftChild)) {
			return pResult;
		} else if(this.queryChild(pSpatialBounds, pMatcher, pResult, this.mBottomRightChild)) {
			return pResult;
		} else {
			return pResult;
		}
	}

	/**
	 * @param pSpatialBounds
	 * @param pResult
	 * @param pChild
	 * @return <code>true</code> when the child contains pSpatialBounds, <code>false</code> otherwise.
	 */
	private boolean queryChild(final B pSpatialBounds, final List<T> pResult, final QuadTreeNode<B, T> pChild) {
		if(!pChild.isEmpty()) {
			if(pChild.mBounds.contains(pSpatialBounds)) {
				pResult.addAll(pChild.query(pSpatialBounds, pResult));
				return true;
			}

			if(pSpatialBounds.contains(pChild.mBounds)) {
				pResult.addAll(pChild.getItemsAndItemsBelow(pResult));
			} else if(pChild.mBounds.intersects(pSpatialBounds)) {
				pResult.addAll(pChild.query(pSpatialBounds, pResult));
			}
		}
		return false;
	}

	/**
	 * @param pSpatialBounds
	 * @param pMatcher
	 * @param pResult
	 * @param pChild
	 * @return <code>true</code> when the child contains pSpatialBounds, <code>false</code> otherwise.
	 */
	private boolean queryChild(final B pSpatialBounds, final IMatcher<T> pMatcher, final List<T> pResult, final QuadTreeNode<B, T> pChild) {
		if(!pChild.isEmpty()) {
			if(pChild.mBounds.contains(pSpatialBounds)) {
				pResult.addAll(pChild.query(pSpatialBounds, pMatcher, pResult));
				return true;
			}

			if(pSpatialBounds.contains(pChild.mBounds)) {
				pResult.addAll(pChild.getItemsAndItemsBelow(pMatcher, pResult));
			} else if(pChild.mBounds.intersects(pSpatialBounds)) {
				pResult.addAll(pChild.query(pSpatialBounds, pMatcher, pResult));
			}
		}
		return false;
	}

	public void add(final T pItem) throws IllegalArgumentException {
		final B itemBounds = pItem.getBounds();

		// if the item is not contained in this quad, there's a problem
		if(!this.mBounds.contains(itemBounds)) { // TODO Perform this check only for the root?
			throw new IllegalArgumentException("pItem is out of the bounds of this " + this.getClass().getSimpleName() + ".");
		}

		// if the subnodes are null create them. may not be sucessfull: see below
		// we may be at the smallest allowed size in which case the subnodes will not be created
		if(this.mTopLeftChild == null) {
			if(this.mLevel > QuadTreeNode.LEVEL_MAX) {
				/* No more levels allowed, so this node has to take the item. */
				this.mItems.add(pItem);
				return;
			} else {
				this.allocateChildren();
			}
		}

		/* If the node contains the item, add the item to that node. */
		if(this.mTopLeftChild.mBounds.contains(itemBounds)) {
			this.mTopLeftChild.add(pItem);
		} else if(this.mTopRightChild.mBounds.contains(itemBounds)) {
			this.mTopRightChild.add(pItem);
		} else if(this.mBottomLeftChild.mBounds.contains(itemBounds)) {
			this.mBottomLeftChild.add(pItem);
		} else if(this.mBottomRightChild.mBounds.contains(itemBounds)) {
			this.mBottomRightChild.add(pItem);
		} else {
			/* None of the children completely contained the item. */
			this.mItems.add(pItem);
		}
	}

	public boolean remove(final T pItem) throws IllegalArgumentException {
		// TODO Implement...
		// TODO Shrink tree where possible.
		return true;
	}

	@SuppressWarnings("unchecked")
	private void allocateChildren() {
		final int nextLevel = this.mLevel + 1;

		this.mTopLeftChild = new QuadTreeNode<B, T>(nextLevel, (B) this.mBounds.split(BoundsSplit.TOP_LEFT));
		this.mTopRightChild = new QuadTreeNode<B, T>(nextLevel, (B) this.mBounds.split(BoundsSplit.TOP_RIGHT));
		this.mBottomLeftChild = new QuadTreeNode<B, T>(nextLevel, (B) this.mBounds.split(BoundsSplit.BOTTOM_LEFT));
		this.mBottomRightChild = new QuadTreeNode<B, T>(nextLevel, (B) this.mBounds.split(BoundsSplit.BOTTOM_RIGHT));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
