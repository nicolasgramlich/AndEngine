package org.anddev.andengine.util.spatial.quadtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.anddev.andengine.util.IMatcher;
import org.anddev.andengine.util.ParameterCallable;
import org.anddev.andengine.util.exception.AndEngineException;
import org.anddev.andengine.util.spatial.ISpatialItem;
import org.anddev.andengine.util.spatial.adt.bounds.IBounds;
import org.anddev.andengine.util.spatial.adt.bounds.IBounds.BoundsSplit;
import org.anddev.andengine.util.spatial.adt.bounds.IBounds.BoundsSplitException;
import org.anddev.andengine.util.spatial.adt.bounds.source.IBoundsSource;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:16:01 - 07.10.2011
 */
public abstract class QuadTree<S extends IBoundsSource, B extends IBounds<S>, T extends ISpatialItem<S>> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int LEVEL_ROOT = 0;
	private static final int LEVEL_MAX_DEFAULT = 8;

	// ===========================================================
	// Fields
	// ===========================================================

	private final QuadTreeNode<S, B, T> mRoot;
	private final B mBounds;
	private final int mMaxLevel;

	// ===========================================================
	// Constructors
	// ===========================================================

	public QuadTree(final B pBounds) {
		this(pBounds, QuadTree.LEVEL_MAX_DEFAULT);
	}

	public QuadTree(final B pBounds, final int pMaxLevel) {
		this.mBounds = pBounds;
		this.mMaxLevel = pMaxLevel;
		this.mRoot = new QuadTreeNode<S, B, T>(QuadTree.LEVEL_ROOT, this.mBounds);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getMaxLevel() {
		return this.mMaxLevel;
	}

	public B getBounds() {
		return this.mBounds;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString() {
		return new StringBuilder()
			.append('[')
			.append(" Class: ")
			.append(this.getClass().getSimpleName())
			.append('\n')
			.append('\t')
			.append("MaxLevel: ")
			.append(this.mMaxLevel)
			.append('\n')
			.append('\t')
			.append("Bounds: ")
			.append(this.mBounds.toString())
			.append(',')
			.append('\n')
			.append('\t')
			.append("Root:")
			.append('\n')
			.append(this.mRoot.toString(2))
			.append('\n')
			.append(']')
			.toString();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized int getItemCount() {
		// TODO Items could be counted along the add/remove calls.
		return this.mRoot.getItemCount();
	}

	public synchronized boolean isEmpty() {
		return this.getItemCount() == 0;
	}

	public synchronized void add(final T pItem) {
		this.mRoot.add(pItem, pItem.getBoundsSource());
	}

	@Deprecated
	public synchronized void add(final T pItem, final S pBoundsSource) {
		this.mRoot.add(pItem, pBoundsSource);
	}

	/**
	 * Shorthand for <code>remove(pItem, pBounds)</code> followed by a <code>add(pItem)</code>.
	 *
	 * @param pItem to be freshly added.
	 * @param pBoundsSource to remove pItem with.
	 */
	public synchronized void move(final T pItem, final S pBoundsSource) throws AndEngineException {
		final boolean success = this.remove(pItem, pBoundsSource);
		if(!success) {
			throw new AndEngineException("Failed to remove item: '" + pItem.toString() + " from old bounds: '" + pBoundsSource.toString() + "'.");
		}
		this.add(pItem);
	}

	/**
	 * Shorthand for <code>remove(pItem, pOldBoundsSource)</code> followed by a <code>add(pItem, pNewBoundsSource)</code>.
	 *
	 * @param pItem to be freshly added.
	 * @param pOldBoundsSource to remove pItem with.
	 * @param pNewBoundsSource to add pItem with.
	 */
	@Deprecated
	public synchronized void move(final T pItem, final S pOldBoundsSource, final S pNewBoundsSource) throws AndEngineException {
		final boolean success = this.remove(pItem, pOldBoundsSource);
		if(!success) {
			throw new AndEngineException("Failed to remove item: '" + pItem.toString() + " from old bounds: '" + pOldBoundsSource.toString() + "'.");
		}
		this.add(pItem, pNewBoundsSource);
	}

	public synchronized boolean remove(final T pItem) {
		return this.remove(pItem, pItem.getBoundsSource());
	}

	public synchronized boolean remove(final T pItem, final S pBoundsSource) {
		return this.mRoot.remove(pItem, pBoundsSource);
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

	public synchronized void callNodes(final ParameterCallable<QuadTreeNode<S, B, T>> pParameterCallable) {
		this.mRoot.callNodes(pParameterCallable);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	@SuppressWarnings("hiding")
	public class QuadTreeNode<S extends IBoundsSource, B extends IBounds<S>, T extends ISpatialItem<S>> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mLevel;
		private final B mBounds;
		private List<T> mItems;

		private QuadTreeNode<S, B, T> mTopLeftChild;
		private QuadTreeNode<S, B, T> mTopRightChild;
		private QuadTreeNode<S, B, T> mBottomLeftChild;
		private QuadTreeNode<S, B, T> mBottomRightChild;
		private boolean mChildrenAllocated;

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

		public boolean hasChildren() {
			return this.mTopLeftChild == null && this.mTopRightChild == null && this.mBottomLeftChild != null && this.mBottomRightChild != null;
		}

		public B getBounds() {
			return this.mBounds;
		}

		/**
		 * @return the list of items. Can be <code>null</code>.
		 */
		public List<T> getItems() {
			return this.mItems;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public String toString() {
			return this.toString(0);
		}

		public String toString(final int pIndent) {
			final char[] indents = new char[pIndent];
			Arrays.fill(indents, '\t');
			
			final StringBuilder sb = new StringBuilder()
				.append(indents)
				.append('[')
				.append(" Class: ")
				.append(this.getClass().getSimpleName())
				.append('\n')
				.append(indents)
				.append('\t')
				.append("Bounds: ")
				.append(this.mBounds.toString())
				.append(',' )
				.append('\n')
				.append(indents)
				.append("\tItems: ");

			if(this.mItems != null) {
				sb.append(this.mItems.toString());
			} else {
				sb.append("[]");
			}
	
			sb.append('\n')
				.append(indents)
				.append('\t')
				.append("Children: [");

			/* Children */
			if(this.mTopLeftChild == null && this.mTopRightChild == null && this.mBottomLeftChild == null && this.mBottomRightChild == null) {
				sb.append(']');
			} else {
				if(this.mTopLeftChild != null) {
					sb.append('\n');
					sb.append(this.mTopLeftChild.toString(pIndent + 2));
					if(this.mTopRightChild != null || this.mBottomLeftChild != null || this.mBottomRightChild != null) {
						sb.append(',');
					}
				}
				if(this.mTopRightChild != null) {
					sb.append('\n');
					sb.append(this.mTopRightChild.toString(pIndent + 2));
					if(this.mBottomLeftChild != null || this.mBottomRightChild != null) {
						sb.append(',');
					}
				}
				if(this.mBottomLeftChild != null) {
					sb.append('\n');
					sb.append(this.mBottomLeftChild.toString(pIndent + 2));
					if(this.mBottomRightChild != null) {
						sb.append(',');
					}
				}
				if(this.mBottomRightChild != null) {
					sb.append('\n');
					sb.append(this.mBottomRightChild.toString(pIndent + 2));
				}
				sb.append('\n')
					.append(indents)
					.append('\t')
					.append(']');
			}

			sb.append('\n')
				.append(indents)
				.append(']');
			
			return sb.toString();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		public int getItemCount() {
			int count;
			if(this.mItems == null) {
				count = 0;
			} else {
				count = this.mItems.size();
			}

			if(this.mTopLeftChild != null) {
				count += this.mTopLeftChild.getItemCount();
			}
			if(this.mTopRightChild != null) {
				count += this.mTopRightChild.getItemCount();
			}
			if(this.mBottomLeftChild != null) {
				count += this.mBottomLeftChild.getItemCount();
			}
			if(this.mBottomRightChild != null) {
				count += this.mBottomRightChild.getItemCount();
			}

			return count;
		}

		public void callItems(final ParameterCallable<T> pParameterCallable) {
			if(this.mItems != null) {
				for(final T item : this.mItems) { // TODO Check if iteration is allocation free.
					pParameterCallable.call(item);
				}
			}

			if(this.mTopLeftChild != null) {
				this.mTopLeftChild.callItems(pParameterCallable);
			}
			if(this.mTopRightChild != null) {
				this.mTopRightChild.callItems(pParameterCallable);
			}
			if(this.mBottomLeftChild != null) {
				this.mBottomLeftChild.callItems(pParameterCallable);
			}
			if(this.mBottomRightChild != null) {
				this.mBottomRightChild.callItems(pParameterCallable);
			}
		}

		public void callNodes(final ParameterCallable<QuadTreeNode<S, B, T>> pParameterCallable) {
			pParameterCallable.call(this);

			if(this.mTopLeftChild != null) {
				this.mTopLeftChild.callNodes(pParameterCallable);
			}
			if(this.mTopRightChild != null) {
				this.mTopRightChild.callNodes(pParameterCallable);
			}
			if(this.mBottomLeftChild != null) {
				this.mBottomLeftChild.callNodes(pParameterCallable);
			}
			if(this.mBottomRightChild != null) {
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
			if(this.mItems != null) {
				pResult.addAll(this.mItems); // TODO Does addAll use an iterator internally?
			}

			if(this.mTopLeftChild != null) {
				this.mTopLeftChild.getItemsAndItemsBelow(pResult);
			}
			if(this.mTopRightChild != null) {
				this.mTopRightChild.getItemsAndItemsBelow(pResult);
			}
			if(this.mBottomLeftChild != null) {
				this.mBottomLeftChild.getItemsAndItemsBelow(pResult);
			}
			if(this.mBottomRightChild != null) {
				this.mBottomRightChild.getItemsAndItemsBelow(pResult);
			}

			return pResult;
		}

		public List<T> getItemsAndItemsBelow(final IMatcher<T> pMatcher) {
			return this.getItemsAndItemsBelow(pMatcher, new LinkedList<T>());
		}

		public List<T> getItemsAndItemsBelow(final IMatcher<T> pMatcher, final List<T> pResult) {
			if(this.mItems != null) {
				for(final T item : this.mItems) { // TODO Check if iteration is allocation free.
					if(pMatcher.matches(item)) {
						pResult.add(item);
					}
				}
			}

			if(this.mTopLeftChild != null) {
				this.mTopLeftChild.getItemsAndItemsBelow(pMatcher, pResult);
			}
			if(this.mTopRightChild != null) {
				this.mTopRightChild.getItemsAndItemsBelow(pMatcher, pResult);
			}
			if(this.mBottomLeftChild != null) {
				this.mBottomLeftChild.getItemsAndItemsBelow(pMatcher, pResult);
			}
			if(this.mBottomRightChild != null) {
				this.mBottomRightChild.getItemsAndItemsBelow(pMatcher, pResult);
			}

			return pResult;
		}

		public List<T> query(final B pBounds) {
			return this.query(pBounds, new LinkedList<T>());
		}

		public List<T> query(final B pBounds, final List<T> pResult) {
			/* Test against all items in this node. */
			if(this.mItems != null) {
				for(final T item : this.mItems) { // TODO Check if iteration is allocation free.
					if(pBounds.intersects(item.getBoundsSource())) {
						pResult.add(item);
					}
				}
			}

			/* Check children. */
			if(this.mTopLeftChild != null && this.queryChild(pBounds, pResult, this.mTopLeftChild)) {
				return pResult;
			} else if(this.mTopRightChild != null && this.queryChild(pBounds, pResult, this.mTopRightChild)) {
				return pResult;
			} else if(this.mBottomLeftChild != null && this.queryChild(pBounds, pResult, this.mBottomLeftChild)) {
				return pResult;
			} else if(this.mBottomRightChild != null && this.queryChild(pBounds, pResult, this.mBottomRightChild)) {
				return pResult;
			} else {
				return pResult;
			}
		}

		public List<T> query(final B pBounds, final IMatcher<T> pMatcher, final List<T> pResult) {
			/* Test against all items in this node. */
			if(this.mItems != null) {
				for(final T item : this.mItems) {
					if(pBounds.intersects(item.getBoundsSource()) && pMatcher.matches(item)) {
						pResult.add(item);
					}
				}
			}

			/* Check children. */
			if(this.mTopLeftChild != null && this.queryChild(pBounds, pMatcher, pResult, this.mTopLeftChild)) {
				return pResult;
			} else if(this.mTopRightChild != null && this.queryChild(pBounds, pMatcher, pResult, this.mTopRightChild)) {
				return pResult;
			} else if(this.mBottomLeftChild != null && this.queryChild(pBounds, pMatcher, pResult, this.mBottomLeftChild)) {
				return pResult;
			} else if(this.mBottomRightChild != null && this.queryChild(pBounds, pMatcher, pResult, this.mBottomRightChild)) {
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
		private boolean queryChild(final B pBounds, final List<T> pResult, final QuadTreeNode<S, B, T> pChild) {
			if(pChild.mBounds.contains(pBounds)) {
				pChild.query(pBounds, pResult);
				return true;
			}

			if(pBounds.contains(pChild.mBounds)) {
				pChild.getItemsAndItemsBelow(pResult);
			} else if(pChild.mBounds.intersects(pBounds)) {
				pChild.query(pBounds, pResult);
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
		private boolean queryChild(final B pBounds, final IMatcher<T> pMatcher, final List<T> pResult, final QuadTreeNode<S, B, T> pChild) {
			if(pChild.mBounds.contains(pBounds)) {
				pChild.query(pBounds, pMatcher, pResult);
				return true;
			}

			if(pBounds.contains(pChild.mBounds)) {
				pChild.getItemsAndItemsBelow(pMatcher, pResult);
			} else if(pChild.mBounds.intersects(pBounds)) {
				pChild.query(pBounds, pMatcher, pResult);
			}
			return false;
		}

		public void add(final T pItem) throws IllegalArgumentException {
			this.add(pItem, pItem.getBoundsSource());
		}

		@SuppressWarnings("unchecked")
		public void add(final T pItem, final S pBoundsSource) throws IllegalArgumentException {
			// if the item is not contained in this quad, there's a problem
			if(!this.mBounds.contains(pBoundsSource)) { // TODO Perform this check only for the root?
				throw new IllegalArgumentException("pItem is out of the bounds of this " + this.getClass().getSimpleName() + ".");
			}

			/* Check if this node has children and eventually create them.*/
			if(!this.mChildrenAllocated) {
				if(this.mLevel > QuadTree.this.mMaxLevel) {
					/* No more levels allowed, so this node has to take the item. */
					this.ensureItemsAllocated();
					this.mItems.add(pItem);
					return;
				}
			}

			/* If the node contains the item, add the item to that node. */
			if(this.mTopLeftChild != null && this.mTopLeftChild.mBounds.contains(pBoundsSource)) {
				this.mTopLeftChild.add(pItem, pBoundsSource);
			} else if(this.mBounds.contains(BoundsSplit.TOP_LEFT, pBoundsSource)) {
				if(this.mTopLeftChild == null) {
					try {
						this.mTopLeftChild = new QuadTreeNode<S, B, T>(this.mLevel + 1, (B) this.mBounds.split(BoundsSplit.TOP_LEFT));
						this.mTopLeftChild.add(pItem, pBoundsSource);
						return;
					} catch (final BoundsSplitException e) {
						/* Nothing. */
					}
				}
			}

			if(this.mTopRightChild != null && this.mTopRightChild.mBounds.contains(pBoundsSource)) {
				this.mTopRightChild.add(pItem, pBoundsSource);
			} else if(this.mBounds.contains(BoundsSplit.TOP_RIGHT, pBoundsSource)) {
				if(this.mTopRightChild == null) {
					try {
						this.mTopRightChild = new QuadTreeNode<S, B, T>(this.mLevel + 1, (B) this.mBounds.split(BoundsSplit.TOP_RIGHT));
						this.mTopRightChild.add(pItem, pBoundsSource);
						return;
					} catch (final BoundsSplitException e) {
						/* Nothing. */
					}
				}
			}
			
			if(this.mBottomLeftChild != null && this.mBottomLeftChild.mBounds.contains(pBoundsSource)) {
				this.mBottomLeftChild.add(pItem, pBoundsSource);
			} else if(this.mBounds.contains(BoundsSplit.BOTTOM_LEFT, pBoundsSource)) {
				if(this.mBottomLeftChild == null) {
					try {
						this.mBottomLeftChild = new QuadTreeNode<S, B, T>(this.mLevel + 1, (B) this.mBounds.split(BoundsSplit.BOTTOM_LEFT));
						this.mBottomLeftChild.add(pItem, pBoundsSource);
						return;
					} catch (final BoundsSplitException e) {
						/* Nothing. */
					}
				}
			}
			
			if(this.mBottomRightChild != null && this.mBottomRightChild.mBounds.contains(pBoundsSource)) {
				this.mBottomRightChild.add(pItem, pBoundsSource);
			} else if(this.mBounds.contains(BoundsSplit.BOTTOM_RIGHT, pBoundsSource)) {
				if(this.mBottomRightChild == null) {
					try {
						this.mBottomRightChild = new QuadTreeNode<S, B, T>(this.mLevel + 1, (B) this.mBounds.split(BoundsSplit.BOTTOM_RIGHT));
						this.mBottomRightChild.add(pItem, pBoundsSource);
						return;
					} catch (final BoundsSplitException e) {
						/* Nothing. */
					}
				}
			}
			
			/* None of the children completely contained the item. */
			this.ensureItemsAllocated();
			this.mItems.add(pItem);
		}

		public boolean remove(final T pItem) throws IllegalArgumentException {
			return this.remove(pItem, pItem.getBoundsSource());
		}

		public boolean remove(final T pItem, final S pBoundsSource) throws IllegalArgumentException {
			if(!this.mBounds.contains(pBoundsSource)) { // TODO Perform this check only for the root?
				throw new IllegalArgumentException("pItem is out of the bounds of this " + this.getClass().getSimpleName() + ".");
			}

			/* If there are no children, try to remove from self. */
			if(this.mTopLeftChild != null && this.mTopLeftChild.mBounds.contains(pBoundsSource)) {
				return this.mTopLeftChild.remove(pItem, pBoundsSource);
			} else if(this.mTopRightChild != null && this.mTopRightChild.mBounds.contains(pBoundsSource)) {
				return this.mTopRightChild.remove(pItem, pBoundsSource);
			} else if(this.mBottomLeftChild != null && this.mBottomLeftChild.mBounds.contains(pBoundsSource)) {
				return this.mBottomLeftChild.remove(pItem, pBoundsSource);
			} else if(this.mBottomRightChild != null && this.mBottomRightChild.mBounds.contains(pBoundsSource)) {
				return this.mBottomRightChild.remove(pItem, pBoundsSource);
			} else {
				/* None of the children completely contained the item. */
				if(this.mItems == null) {
					return false;
				} else {
					// TODO Potentially mItems could be set to null when its size is 0.
					return this.mItems.remove(pItem);
				}
			}
		}

		private void ensureItemsAllocated() {
			if(this.mItems == null) {
				this.mItems = new ArrayList<T>(1);
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
