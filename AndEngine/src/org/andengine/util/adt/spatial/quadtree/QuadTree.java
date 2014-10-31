package org.andengine.util.adt.spatial.quadtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.andengine.util.IMatcher;
import org.andengine.util.adt.bounds.BoundsSplit;
import org.andengine.util.adt.bounds.BoundsSplit.BoundsSplitException;
import org.andengine.util.adt.bounds.IBounds;
import org.andengine.util.adt.spatial.ISpatialItem;
import org.andengine.util.call.ParameterCallable;
import org.andengine.util.debug.Debug;
import org.andengine.util.exception.AndEngineRuntimeException;

/**
 * TODO Make all methods non-synchronized and add a SynchronizedXZYQuadTree subclasses. 
 *
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

	protected final B mBounds;
	protected final QuadTreeNode mRoot;
	protected final int mMaxLevel;

	// ===========================================================
	// Constructors
	// ===========================================================

	public QuadTree(final B pBounds) {
		this(pBounds, QuadTree.LEVEL_MAX_DEFAULT);
	}

	protected QuadTree(final B pBounds, final int pMaxLevel) {
		this.mBounds = pBounds;
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

	public B getBounds() {
		return this.mBounds;
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

	@SuppressWarnings("deprecation")
	public synchronized void add(final T pItem) {
		this.add(pItem, pItem.getBounds());
	}

	public synchronized void addAll(final T ... pItems) {
		for(final T item : pItems) {
			this.add(item);
		}
	}

	public synchronized void addAll(final ArrayList<T> pItems) {
		final int itemCount = pItems.size();
		for(int i = 0; i < itemCount; i++) {
			this.add(pItems.get(i));
		}
	}

	public synchronized void addAll(final Collection<T> pItems) {
		for(final T item : pItems) {
			this.add(item);
		}
	}

	@Deprecated
	public synchronized void add(final T pItem, final B pBounds) {
		if(!this.mRoot.contains(pBounds)) {
			Debug.w("pBounds are out of the bounds of this " + this.getClass().getSimpleName() + ".");
			this.mRoot.addItemSafe(pItem);
			return;
		}
		this.mRoot.add(pItem, pBounds);
	}

	/**
	 * Shorthand for <code>remove(pItem, pBounds)</code> followed by a <code>add(pItem)</code>.
	 *
	 * @param pItem to be freshly added.
	 * @param pBounds to remove pItem with.
	 */
	public synchronized void move(final T pItem, final B pBounds) throws AndEngineRuntimeException {
		final boolean success = this.remove(pItem, pBounds);
		if(!success) {
			throw new AndEngineRuntimeException("Failed to remove item: '" + pItem.toString() + " from old bounds: '" + pBounds.toString() + "'.");
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
	public synchronized void move(final T pItem, final B pOldBounds, final B pNewBounds) throws AndEngineRuntimeException {
		final boolean success = this.remove(pItem, pOldBounds);
		if(!success) {
			throw new AndEngineRuntimeException("Failed to remove item: '" + pItem.toString() + " from old bounds: '" + pOldBounds.toString() + "'.");
		}
		this.add(pItem, pNewBounds);
	}

	public synchronized boolean remove(final T pItem) {
		return this.remove(pItem, pItem.getBounds());
	}

	public synchronized boolean remove(final T pItem, final B pBounds) {
		return this.mRoot.remove(pItem, pBounds);
	}

	public synchronized ArrayList<T> query(final B pBounds) {
		return this.query(pBounds, new ArrayList<T>());
	}

	public synchronized <L extends List<T>> L query(final B pBounds, final L pResult) {
		return this.mRoot.query(pBounds, pResult);
	}

	public synchronized ArrayList<T> query(final B pBounds, final IMatcher<T> pMatcher) {
		return this.query(pBounds, pMatcher, new ArrayList<T>());
	}

	public synchronized <L extends List<T>> L query(final B pBounds, final IMatcher<T> pMatcher, final L pResult) {
		return this.mRoot.query(pBounds, pMatcher, pResult);
	}

	/**
	 * @param pBounds
	 * @param pMatcher must only {@link IMatcher#matches(T)} when the item is <code>instanceof</code> S, otherwise it will an {@link ClassCastException}.
	 * @param pResult
	 * @return
	 * @throws ClassCastException when pMatcher matched an item that was not <code>instanceof</code> S.
	 */
	public synchronized <L extends List<S>, S extends T> L queryForSubclass(final B pBounds, final IMatcher<T> pMatcher, final L pResult) throws ClassCastException {
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

	public synchronized void clear() {
		this.mRoot.clear();
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

		public ArrayList<T> getItemsAndItemsBelow() {
			return this.getItemsAndItemsBelow(new ArrayList<T>());
		}

		/**
		 * @return the items of this {@link QuadTreeNode} and all children (recursively).
		 */
		public <L extends List<T>> L getItemsAndItemsBelow(final L pResult) {
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

		public ArrayList<T> getItemsAndItemsBelow(final IMatcher<T> pMatcher) {
			return this.getItemsAndItemsBelow(pMatcher, new ArrayList<T>());
		}

		public <L extends List<T>> L getItemsAndItemsBelow(final IMatcher<T> pMatcher, final L pResult) {
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
		public <L extends List<S>, S extends T> L getItemsAndItemsBelowForSubclass(final IMatcher<T> pMatcher, final L pResult) throws ClassCastException {
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

		public ArrayList<T> query(final B pBounds) {
			return this.query(pBounds, new ArrayList<T>());
		}

		public <L extends List<T>> L query(final B pBounds, final L pResult) {
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

		public <L extends List<T>> L query(final B pBounds, final IMatcher<T> pMatcher, final L pResult) {
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
		public <L extends List<S>, S extends T> L queryForSubclass(final B pBounds, final IMatcher<T> pMatcher, final L pResult) throws ClassCastException {
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
		private <L extends List<T>> boolean queryChild(final B pBounds, final L pResult, final QuadTreeNode pChild) {
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
		private <L extends List<T>> boolean queryChild(final B pBounds, final IMatcher<T> pMatcher, final L pResult, final QuadTreeNode pChild) {
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
		private <L extends List<S>, S extends T> boolean queryChildForSubclass(final B pBounds, final IMatcher<T> pMatcher, final L pResult, final QuadTreeNode pChild) throws ClassCastException {
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

		public void add(final T pItem, final B pBounds) throws IllegalArgumentException {
			/* Check if this node is in the maximum level. */
			if(this.mLevel >= QuadTree.this.mMaxLevel) {
				/* No more levels allowed, so this node has to take the item. */
				this.addItemSafe(pItem);
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
						this.addItemSafe(pItem);
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
						this.addItemSafe(pItem);
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
						this.addItemSafe(pItem);
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
						this.addItemSafe(pItem);
					}
					return;
				}
			}

			/* None of the children completely contained the item. */
			this.addItemSafe(pItem);
		}

		public boolean remove(final T pItem) throws IllegalArgumentException {
			return this.remove(pItem, pItem.getBounds());
		}

		public boolean remove(final T pItem, final B pBounds) throws IllegalArgumentException {
			if(!this.contains(pBounds)) { // TODO Perform this check only for the root?
				throw new IllegalArgumentException("pItem (" + pItem.toString() + ") is out of the bounds of this " + this.getClass().getSimpleName() + ".");
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

		private void addItemSafe(final T pItem) {
			if(this.mItems == null) {
				this.mItems = new ArrayList<T>(1);
			}
			this.mItems.add(pItem);
		}

		protected void clear() {
			if(this.mBottomLeftChild != null) {
				this.mBottomLeftChild.clear();
				this.mBottomLeftChild = null;
			}
			if(this.mBottomRightChild != null) {
				this.mBottomRightChild.clear();
				this.mBottomRightChild = null;
			}
			if(this.mTopLeftChild != null) {
				this.mTopLeftChild.clear();
				this.mTopLeftChild = null;
			}
			if(this.mTopRightChild != null) {
				this.mTopRightChild.clear();
				this.mTopRightChild = null;
			}

			if(this.mItems != null) {
				this.mItems.clear();
				this.mItems = null;
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
