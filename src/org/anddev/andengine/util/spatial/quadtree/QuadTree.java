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
 * @since 20:16:01 - 07.10.2011
 */
public class QuadTree<B extends IBounds, T extends ISpatialItem<B>> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int LEVEL_ROOT = 0;
	private static final int LEVEL_MAX_DEFAULT = 8;

	// ===========================================================
	// Fields
	// ===========================================================

	private final QuadTreeNode<B, T> mRoot;
	private final B mBounds;
	private final int mMaxLevel;

	// ===========================================================
	// Constructors
	// ===========================================================

	public QuadTree(final B pBounds) {
		this(pBounds, LEVEL_MAX_DEFAULT);
	}

	public QuadTree(final B pBounds, final int pMaxLevel) {
		this.mBounds = pBounds;
		this.mMaxLevel = pMaxLevel;
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

	public synchronized int getItemCount() {
		// TODO Items could be counted along the add/remove calls.
		return this.mRoot.getItemCount();
	}

	public synchronized void add(final T pItem) {
		this.mRoot.add(pItem, pItem.getBounds());
	}
	
	@Deprecated
	public synchronized void add(final T pItem, final B pBounds) {
		this.mRoot.add(pItem, pBounds);
	}
	
	public synchronized void move(final T pItem, final B pBounds) {
		this.remove(pItem, pBounds);
		this.add(pItem);
	}

	public synchronized void remove(final T pItem) {
		this.remove(pItem, pItem.getBounds());
	}

	public synchronized void remove(final T pItem, final B pBounds) {
		this.mRoot.remove(pItem, pBounds);
	}

	public synchronized List<T> query(final B pBounds) {
		return this.query(pBounds, new LinkedList<T>());
	}

	public synchronized List<T> query(final B pBounds, final List<T> pResult) {
		return this.mRoot.query(pBounds, pResult);
	}

	public synchronized List<T> query(final B pBounds, final IMatcher<T> pMatcher) {
		return this.query(pBounds, pMatcher, new LinkedList<T>());
	}

	public synchronized List<T> query(final B pBounds, final IMatcher<T> pMatcher, final List<T> pResult) {
		return this.mRoot.query(pBounds, pMatcher, pResult);
	}

	public synchronized void callItems(final ParameterCallable<T> pParameterCallable) {
		this.mRoot.callItems(pParameterCallable);
	}

	public synchronized void callNodes(final ParameterCallable<QuadTreeNode<B, T>> pParameterCallable) {
		this.mRoot.callNodes(pParameterCallable);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	@SuppressWarnings("hiding")
	public class QuadTreeNode<B extends IBounds, T extends ISpatialItem<B>> {
		// ===========================================================
		// Constants
		// ===========================================================

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

		public QuadTreeNode(final int pLevel, final B pBounds) {
			this.mLevel = pLevel;
			this.mBounds = pBounds;
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
			pResult.addAll(this.mItems); // TODO Does addAll use an iterator internally?

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

		public List<T> query(final B pBounds) {
			return this.query(pBounds, new LinkedList<T>());
		}

		public List<T> query(final B pBounds, final List<T> pResult) {
			/* Test against all items in this node. */
			for(final T item : this.mItems) {
				if(pBounds.intersects(item.getBounds())) {
					pResult.add(item);
				}
			}

			/* Early exit when there are no children. */
			if(this.mTopLeftChild == null) {
				return pResult;
			}

			/* Check children. */
			if(this.queryChild(pBounds, pResult, this.mTopLeftChild)) {
				return pResult;
			} else if(this.queryChild(pBounds, pResult, this.mTopRightChild)) {
				return pResult;
			} else if(this.queryChild(pBounds, pResult, this.mBottomLeftChild)) {
				return pResult;
			} else if(this.queryChild(pBounds, pResult, this.mBottomRightChild)) {
				return pResult;
			} else {
				return pResult;
			}
		}

		public List<T> query(final B pBounds, final IMatcher<T> pMatcher, final List<T> pResult) {
			/* Test against all items in this node. */
			for(final T item : this.mItems) {
				if(pBounds.intersects(item.getBounds()) && pMatcher.matches(item)) {
					pResult.add(item);
				}
			}

			/* Early exit when there are no children. */
			if(this.mTopLeftChild == null) {
				return pResult;
			}

			/* Check children. */
			if(this.queryChild(pBounds, pMatcher, pResult, this.mTopLeftChild)) {
				return pResult;
			} else if(this.queryChild(pBounds, pMatcher, pResult, this.mTopRightChild)) {
				return pResult;
			} else if(this.queryChild(pBounds, pMatcher, pResult, this.mBottomLeftChild)) {
				return pResult;
			} else if(this.queryChild(pBounds, pMatcher, pResult, this.mBottomRightChild)) {
				return pResult;
			} else {
				return pResult;
			}
		}

		/**
		 * @param pBounds
		 * @param pResult
		 * @param pChild
		 * @return <code>true</code> when the child contains pBounds, <code>false</code> otherwise.
		 */
		private boolean queryChild(final B pBounds, final List<T> pResult, final QuadTreeNode<B, T> pChild) {
			if(!pChild.isEmpty()) {
				if(pChild.mBounds.contains(pBounds)) {
					pChild.query(pBounds, pResult);
					return true;
				}

				if(pBounds.contains(pChild.mBounds)) {
					pChild.getItemsAndItemsBelow(pResult);
				} else if(pChild.mBounds.intersects(pBounds)) {
					pChild.query(pBounds, pResult);
				}
			}
			return false;
		}

		/**
		 * @param pBounds
		 * @param pMatcher
		 * @param pResult
		 * @param pChild
		 * @return <code>true</code> when the child contains pBounds, <code>false</code> otherwise.
		 */
		private boolean queryChild(final B pBounds, final IMatcher<T> pMatcher, final List<T> pResult, final QuadTreeNode<B, T> pChild) {
			if(!pChild.isEmpty()) {
				if(pChild.mBounds.contains(pBounds)) {
					pChild.query(pBounds, pMatcher, pResult);
					return true;
				}

				if(pBounds.contains(pChild.mBounds)) {
					pChild.getItemsAndItemsBelow(pMatcher, pResult);
				} else if(pChild.mBounds.intersects(pBounds)) {
					pChild.query(pBounds, pMatcher, pResult);
				}
			}
			return false;
		}

		public void add(final T pItem) throws IllegalArgumentException {
			this.add(pItem, pItem.getBounds());
		}

		public void add(final T pItem, final B pBounds) throws IllegalArgumentException {
			// if the item is not contained in this quad, there's a problem
			if(!this.mBounds.contains(pBounds)) { // TODO Perform this check only for the root?
				throw new IllegalArgumentException("pItem is out of the bounds of this " + this.getClass().getSimpleName() + ".");
			}

			/* Check if this node has children and eventually create them.*/
			if(this.mTopLeftChild == null) {
				if(this.mLevel > QuadTree.this.mMaxLevel) {
					/* No more levels allowed, so this node has to take the item. */
					this.mItems.add(pItem);
					return;
				} else {
					this.allocateChildren();
				}
			}

			/* If the node contains the item, add the item to that node. */
			if(this.mTopLeftChild.mBounds.contains(pBounds)) {
				this.mTopLeftChild.add(pItem, pBounds);
			} else if(this.mTopRightChild.mBounds.contains(pBounds)) {
				this.mTopRightChild.add(pItem, pBounds);
			} else if(this.mBottomLeftChild.mBounds.contains(pBounds)) {
				this.mBottomLeftChild.add(pItem, pBounds);
			} else if(this.mBottomRightChild.mBounds.contains(pBounds)) {
				this.mBottomRightChild.add(pItem, pBounds);
			} else {
				/* None of the children completely contained the item. */
				this.mItems.add(pItem);
			}
		}

		public boolean remove(final T pItem) throws IllegalArgumentException {
			return this.remove(pItem, pItem.getBounds());
		}

		public boolean remove(final T pItem, final B pBounds) throws IllegalArgumentException {
			if(!this.mBounds.contains(pBounds)) { // TODO Perform this check only for the root?
				throw new IllegalArgumentException("pItem is out of the bounds of this " + this.getClass().getSimpleName() + ".");
			}

			/* If there are no children, try to remove from self. */
			if(this.mTopLeftChild == null) {
				return this.mItems.remove(pItem);
			}

			if(this.mTopLeftChild.mBounds.contains(pBounds)) {
				return this.mTopLeftChild.remove(pItem, pBounds);
			} else if(this.mTopRightChild.mBounds.contains(pBounds)) {
				return this.mTopRightChild.remove(pItem, pBounds);
			} else if(this.mBottomLeftChild.mBounds.contains(pBounds)) {
				return this.mBottomLeftChild.remove(pItem, pBounds);
			} else if(this.mBottomRightChild.mBounds.contains(pBounds)) {
				return this.mBottomRightChild.remove(pItem, pBounds);
			} else {
				/* None of the children completely contained the item. */
				return this.mItems.remove(pItem);
			}
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
}
