package org.anddev.andengine.util.spatial.quadtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.anddev.andengine.util.IMatcher;
import org.anddev.andengine.util.ParameterCallable;
import org.anddev.andengine.util.exception.AndEngineException;
import org.anddev.andengine.util.spatial.ISpatialItem;
import org.anddev.andengine.util.spatial.adt.bounds.BoundsSplit;
import org.anddev.andengine.util.spatial.adt.bounds.BoundsSplit.BoundsSplitException;
import org.anddev.andengine.util.spatial.adt.bounds.IBounds;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:16:01 - 07.10.2011
 */
public abstract class QuadTree<B extends IBounds, T extends ISpatialItem<B>> implements IBounds {
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final int LEVEL_ROOT = 0;
	protected static final int LEVEL_MAX_DEFAULT = 8;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final QuadTreeNode mRoot;
	protected final int mMaxLevel;

	// ===========================================================
	// Constructors
	// ===========================================================

	public QuadTree(final B pBounds) {
		this(pBounds, QuadTree.LEVEL_MAX_DEFAULT);
	}

	protected QuadTree(final B pBounds, final int pMaxLevel) {
		this.mMaxLevel = pMaxLevel;
		this.mRoot = this.initRoot(pBounds);
	}

	protected abstract QuadTreeNode initRoot(final B pBounds);

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getMaxLevel() {
		return this.mMaxLevel;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract QuadTreeNode getRoot();

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder()
			.append('[')
			.append(" Class: ")
			.append(this.getClass().getSimpleName())
			.append('\n')
			.append('\t')
			.append("MaxLevel: ")
			.append(this.mMaxLevel)
			.append(',')
			.append('\n')
			.append('\t')
			.append("Bounds: ");

		this.mRoot.appendBoundsToString(sb);

		sb.append(',')
			.append('\n')
			.append('\t')
			.append("Root:")
			.append('\n')
			.append(this.mRoot.toString(2))
			.append('\n')
			.append(']');
		
		return sb.toString();
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
		this.mRoot.add(pItem, pItem.getBounds());
	}
	
	public synchronized void addAll(final T ... pItems) {
		for(T item : pItems) {
			this.mRoot.add(item, item.getBounds());
		}
	}
	
	public synchronized void addAll(final List<T> pItems) {
		for(T item : pItems) {
			this.mRoot.add(item, item.getBounds());
		}
	}
	
	public synchronized void addAll(final Collection<T> pItems) {
		for(T item : pItems) {
			this.mRoot.add(item, item.getBounds());
		}
	}

	@Deprecated
	public synchronized void add(final T pItem, final B pBounds) {
		this.mRoot.add(pItem, pBounds);
	}

	/**
	 * Shorthand for <code>remove(pItem, pBounds)</code> followed by a <code>add(pItem)</code>.
	 *
	 * @param pItem to be freshly added.
	 * @param pBounds to remove pItem with.
	 */
	public synchronized void move(final T pItem, final B pBounds) throws AndEngineException {
		final boolean success = this.remove(pItem, pBounds);
		if(!success) {
			throw new AndEngineException("Failed to remove item: '" + pItem.toString() + " from old bounds: '" + pBounds.toString() + "'.");
		}
		this.add(pItem);
	}

	/**
	 * Shorthand for <code>remove(pItem, pOldBounds)</code> followed by a <code>add(pItem, pNewBounds)</code>.
	 *
	 * @param pItem to be freshly added.
	 * @param pOldBounds to remove pItem with.
	 * @param pNewBounds to add pItem with.
	 */
	@Deprecated
	public synchronized void move(final T pItem, final B pOldBounds, final B pNewBounds) throws AndEngineException {
		final boolean success = this.remove(pItem, pOldBounds);
		if(!success) {
			throw new AndEngineException("Failed to remove item: '" + pItem.toString() + " from old bounds: '" + pOldBounds.toString() + "'.");
		}
		this.add(pItem, pNewBounds);
	}

	public synchronized boolean remove(final T pItem) {
		return this.remove(pItem, pItem.getBounds());
	}

	public synchronized boolean remove(final T pItem, final B pBounds) {
		return this.mRoot.remove(pItem, pBounds);
	}

	public synchronized List<T> query(final B pBounds) {
		return this.query(pBounds, new ArrayList<T>());
	}

	public synchronized List<T> query(final B pBounds, final List<T> pResult) {
		return this.mRoot.query(pBounds, pResult);
	}

	public synchronized List<T> query(final B pBounds, final IMatcher<T> pMatcher) {
		return this.query(pBounds, pMatcher, new ArrayList<T>());
	}

	public synchronized List<T> query(final B pBounds, final IMatcher<T> pMatcher, final List<T> pResult) {
		return this.mRoot.query(pBounds, pMatcher, pResult);
	}

	/**
	 * @param pBounds
	 * @param pMatcher must only {@link IMatcher#matches(T)} when the item is <code>instanceof</code> S, otherwise it will an {@link ClassCastException}.
	 * @param pResult
	 * @return
	 * @throws ClassCastException when pMatcher matched an item that was not <code>instanceof</code> S.
	 */
	public synchronized <S> List<S> queryForSubclass(final B pBounds, final IMatcher<T> pMatcher, final List<S> pResult) throws ClassCastException {
		return this.mRoot.queryForSubclass(pBounds, pMatcher, pResult);
	}

	public synchronized boolean containsAny(final B pBounds) {
		return this.mRoot.containsAny(pBounds);
	}

	public synchronized boolean containsAny(final B pBounds, final IMatcher<T> pMatcher) {
		return this.mRoot.containsAny(pBounds, pMatcher);
	}

	public synchronized void callItems(final ParameterCallable<T> pParameterCallable) {
		this.mRoot.callItems(pParameterCallable);
	}

	public synchronized void callNodes(final ParameterCallable<QuadTreeNode> pParameterCallable) {
		this.mRoot.callNodes(pParameterCallable);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public abstract class QuadTreeNode implements IBounds {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		protected final int mLevel;
		protected List<T> mItems;

		protected QuadTreeNode mTopLeftChild;
		protected QuadTreeNode mTopRightChild;
		protected QuadTreeNode mBottomLeftChild;
		protected QuadTreeNode mBottomRightChild;

		// ===========================================================
		// Constructors
		// ===========================================================

		protected QuadTreeNode(final int pLevel) {
			this.mLevel = pLevel;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public boolean hasChildren() {
			return this.mTopLeftChild == null && this.mTopRightChild == null && this.mBottomLeftChild != null && this.mBottomRightChild != null;
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
		
		protected abstract boolean contains(final B pBounds);
		protected abstract boolean contains(final BoundsSplit pBoundsSplit, final B pBounds);
		protected abstract boolean containedBy(final B pBounds);
		protected abstract boolean intersects(final B pBounds);
		protected abstract boolean intersects(final B pBoundsA, final B pBoundsB);
		protected abstract QuadTreeNode split(final BoundsSplit pBoundsSplit);
		protected abstract void appendBoundsToString(final StringBuilder pStringBuilder);

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
				.append("Level: ")
				.append(this.mLevel)
				.append(',' )
				.append('\n')
				.append(indents)
				.append('\t')
				.append("Bounds: ");

			this.appendBoundsToString(sb); 

			sb.append(',' )
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
				final int itemCount = this.mItems.size();
				for(int i = 0; i < itemCount; i++) {
					final T item = this.mItems.get(i);
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

		public void callNodes(final ParameterCallable<QuadTreeNode> pParameterCallable) {
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
			return this.getItemsAndItemsBelow(new ArrayList<T>());
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
			return this.getItemsAndItemsBelow(pMatcher, new ArrayList<T>());
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

		@SuppressWarnings("unchecked")
		public <S> List<S> getItemsAndItemsBelowForSubclass(final IMatcher<T> pMatcher, final List<S> pResult) throws ClassCastException {
			if(this.mItems != null) {
				final int itemCount = this.mItems.size();
				for(int i = 0; i < itemCount; i++) {
					final T item = this.mItems.get(i);
					if(pMatcher.matches(item)) {
						pResult.add((S)item);
					}
				}
			}

			if(this.mTopLeftChild != null) {
				this.mTopLeftChild.getItemsAndItemsBelowForSubclass(pMatcher, pResult);
			}
			if(this.mTopRightChild != null) {
				this.mTopRightChild.getItemsAndItemsBelowForSubclass(pMatcher, pResult);
			}
			if(this.mBottomLeftChild != null) {
				this.mBottomLeftChild.getItemsAndItemsBelowForSubclass(pMatcher, pResult);
			}
			if(this.mBottomRightChild != null) {
				this.mBottomRightChild.getItemsAndItemsBelowForSubclass(pMatcher, pResult);
			}

			return pResult;
		}

		public List<T> query(final B pBounds) {
			return this.query(pBounds, new ArrayList<T>());
		}

		public List<T> query(final B pBounds, final List<T> pResult) {
			/* Test against all items in this node. */
			if(this.mItems != null) {
				final int itemCount = this.mItems.size();
				for(int i = 0; i < itemCount; i++) {
					final T item = this.mItems.get(i);
					if(this.intersects(pBounds, item.getBounds())) {
						pResult.add(item);
					}
				}
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
			if(this.mItems != null) {
				for(final T item : this.mItems) {
					if(this.intersects(pBounds, item.getBounds()) && pMatcher.matches(item)) {
						pResult.add(item);
					}
				}
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

		@SuppressWarnings("unchecked")
		public <S> List<S> queryForSubclass(final B pBounds, final IMatcher<T> pMatcher, final List<S> pResult) throws ClassCastException {
			/* Test against all items in this node. */
			if(this.mItems != null) {
				for(final T item : this.mItems) {
					if(this.intersects(pBounds, item.getBounds()) && pMatcher.matches(item)) {
						pResult.add((S)item);
					}
				}
			}

			/* Check children. */
			if(this.queryChildForSubclass(pBounds, pMatcher, pResult, this.mTopLeftChild)) {
				return pResult;
			} else if(this.queryChildForSubclass(pBounds, pMatcher, pResult, this.mTopRightChild)) {
				return pResult;
			} else if(this.queryChildForSubclass(pBounds, pMatcher, pResult, this.mBottomLeftChild)) {
				return pResult;
			} else if(this.queryChildForSubclass(pBounds, pMatcher, pResult, this.mBottomRightChild)) {
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
		private boolean queryChild(final B pBounds, final List<T> pResult, final QuadTreeNode pChild) {
			if(pChild == null) {
				return false;
			}

			if(pChild.contains(pBounds)) {
				pChild.query(pBounds, pResult);
				return true;
			}

			if(pChild.containedBy(pBounds)) {
				pChild.getItemsAndItemsBelow(pResult);
			} else if(pChild.intersects(pBounds)) {
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
		private boolean queryChild(final B pBounds, final IMatcher<T> pMatcher, final List<T> pResult, final QuadTreeNode pChild) {
			if(pChild == null) {
				return false;
			}

			if(pChild.contains(pBounds)) {
				pChild.query(pBounds, pMatcher, pResult);
				return true;
			}

			if(pChild.containedBy(pBounds)) {
				pChild.getItemsAndItemsBelow(pMatcher, pResult);
			} else if(pChild.intersects(pBounds)) {
				pChild.query(pBounds, pMatcher, pResult);
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
		private <S> boolean queryChildForSubclass(final B pBounds, final IMatcher<T> pMatcher, final List<S> pResult, final QuadTreeNode pChild) throws ClassCastException {
			if(pChild == null) {
				return false;
			}
			
			if(pChild.contains(pBounds)) {
				pChild.queryForSubclass(pBounds, pMatcher, pResult);
				return true;
			}
			
			if(pChild.containedBy(pBounds)) {
				pChild.getItemsAndItemsBelowForSubclass(pMatcher, pResult);
			} else if(pChild.intersects(pBounds)) {
				pChild.queryForSubclass(pBounds, pMatcher, pResult);
			}
			
			return false;
		}

		public boolean containsAny(final B pBounds, final IMatcher<T> pMatcher) {
			/* Test against all items in this node. */
			if(this.mItems != null) {
				final int itemCount = this.mItems.size();
				for(int i = 0; i < itemCount; i++) {
					final T item = this.mItems.get(i);
					if(this.intersects(pBounds, item.getBounds()) && pMatcher.matches(item)) {
						return true;
					}
				}
			}

			/* Check children. */
			if(this.containsAnyChild(pBounds, pMatcher, this.mTopLeftChild)) {
				return true;
			} else if(this.containsAnyChild(pBounds, pMatcher, this.mTopRightChild)) {
				return true;
			} else if(this.containsAnyChild(pBounds, pMatcher, this.mBottomLeftChild)) {
				return true;
			} else if(this.containsAnyChild(pBounds, pMatcher, this.mBottomRightChild)) {
				return true;
			} else {
				return false;
			}
		}

		public boolean containsAny(final B pBounds) {
			/* Test against all items in this node. */
			if(this.mItems != null) {
				final int itemCount = this.mItems.size();
				for(int i = 0; i < itemCount; i++) {
					final T item = this.mItems.get(i);
					if(this.intersects(pBounds, item.getBounds())) {
						return true;
					}
				}
			}

			/* Check children. */
			if(this.containsAnyChild(pBounds, this.mTopLeftChild)) {
				return true;
			} else if(this.containsAnyChild(pBounds, this.mTopRightChild)) {
				return true;
			} else if(this.containsAnyChild(pBounds, this.mBottomLeftChild)) {
				return true;
			} else if(this.containsAnyChild(pBounds, this.mBottomRightChild)) {
				return true;
			} else {
				return false;
			}
		}

		private boolean containsAnyChild(final B pBounds, final IMatcher<T> pMatcher, final QuadTreeNode pChild) {
			if(pChild == null) {
				return false;
			}

			if(pChild.intersects(pBounds) && pChild.containsAny(pBounds, pMatcher)) {
				return true;
			}

			return false;
		}

		private boolean containsAnyChild(final B pBounds, final QuadTreeNode pChild) {
			if(pChild == null) {
				return false;
			}

			if(pChild.intersects(pBounds) && pChild.containsAny(pBounds)) {
				return true;
			}

			return false;
		}

		public void add(final T pItem) throws IllegalArgumentException {
			this.add(pItem, pItem.getBounds());
		}

		public void add(final T pItem, final B pBounds) throws IllegalArgumentException {
			// if the item is not contained in this quad, there's a problem
			if(!this.contains(pBounds)) { // TODO Perform this check only for the root?
				throw new IllegalArgumentException("pItem is out of the bounds of this " + this.getClass().getSimpleName() + ".");
			}

			/* Check if this node is in the maximum level. */
			if(this.mLevel >= QuadTree.this.mMaxLevel) {
				/* No more levels allowed, so this node has to take the item. */
				this.ensureItemsAllocated();
				this.mItems.add(pItem);
				return;
			}

			/* If the node contains the item, add the item to that node. */
			if(this.mTopLeftChild != null && this.mTopLeftChild.contains(pBounds)) {
				this.mTopLeftChild.add(pItem, pBounds);
				return;
			} else if(this.contains(BoundsSplit.TOP_LEFT, pBounds)) {
				if(this.mTopLeftChild == null) {
					try {
						this.mTopLeftChild = this.split(BoundsSplit.TOP_LEFT);
						this.mTopLeftChild.add(pItem, pBounds);
					} catch (final BoundsSplitException e) {
						this.ensureItemsAllocated();
						this.mItems.add(pItem);
					}
					return;
				}
			}

			if(this.mTopRightChild != null && this.mTopRightChild.contains(pBounds)) {
				this.mTopRightChild.add(pItem, pBounds);
				return;
			} else if(this.contains(BoundsSplit.TOP_RIGHT, pBounds)) {
				if(this.mTopRightChild == null) {
					try {
						this.mTopRightChild = this.split(BoundsSplit.TOP_RIGHT);
						this.mTopRightChild.add(pItem, pBounds);
					} catch (final BoundsSplitException e) {
						this.ensureItemsAllocated();
						this.mItems.add(pItem);
					}
					return;
				}
			}

			if(this.mBottomLeftChild != null && this.mBottomLeftChild.contains(pBounds)) {
				this.mBottomLeftChild.add(pItem, pBounds);
				return;
			} else if(this.contains(BoundsSplit.BOTTOM_LEFT, pBounds)) {
				if(this.mBottomLeftChild == null) {
					try {
						this.mBottomLeftChild = this.split(BoundsSplit.BOTTOM_LEFT);
						this.mBottomLeftChild.add(pItem, pBounds);
					} catch (final BoundsSplitException e) {
						this.ensureItemsAllocated();
						this.mItems.add(pItem);
					}
					return;
				}
			}

			if(this.mBottomRightChild != null && this.mBottomRightChild.contains(pBounds)) {
				this.mBottomRightChild.add(pItem, pBounds);
				return;
			} else if(this.contains(BoundsSplit.BOTTOM_RIGHT, pBounds)) {
				if(this.mBottomRightChild == null) {
					try {
						this.mBottomRightChild = this.split(BoundsSplit.BOTTOM_RIGHT);
						this.mBottomRightChild.add(pItem, pBounds);
					} catch (final BoundsSplitException e) {
						this.ensureItemsAllocated();
						this.mItems.add(pItem);
					}
					return;
				}
			}

			/* None of the children completely contained the item. */
			this.ensureItemsAllocated();
			this.mItems.add(pItem);
		}

		public boolean remove(final T pItem) throws IllegalArgumentException {
			return this.remove(pItem, pItem.getBounds());
		}

		public boolean remove(final T pItem, final B pBounds) throws IllegalArgumentException {
			if(!this.contains(pBounds)) { // TODO Perform this check only for the root?
				throw new IllegalArgumentException("pItem is out of the bounds of this " + this.getClass().getSimpleName() + ".");
			}

			/* If there are no children, try to remove from self. */
			if(this.mTopLeftChild != null && this.mTopLeftChild.contains(pBounds)) {
				return this.mTopLeftChild.remove(pItem, pBounds);
			} else if(this.mTopRightChild != null && this.mTopRightChild.contains(pBounds)) {
				return this.mTopRightChild.remove(pItem, pBounds);
			} else if(this.mBottomLeftChild != null && this.mBottomLeftChild.contains(pBounds)) {
				return this.mBottomLeftChild.remove(pItem, pBounds);
			} else if(this.mBottomRightChild != null && this.mBottomRightChild.contains(pBounds)) {
				return this.mBottomRightChild.remove(pItem, pBounds);
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
