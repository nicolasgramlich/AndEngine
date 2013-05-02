package org.andengine.util.adt.cache;

import org.andengine.util.adt.pool.GenericPool;

import android.util.SparseArray;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:19:22 - 08.12.2011
 */
public class IntLRUCache<V> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mCapacity;
	private int mSize;

	private final SparseArray<IntLRUCacheValueHolder<V>> mSparseArray;
	private final IntLRUCacheQueue mIntLRUCacheQueue;

	private final GenericPool<IntLRUCacheValueHolder<V>> mIntLRUCacheValueHolderPool = new GenericPool<IntLRUCache.IntLRUCacheValueHolder<V>>() {
		@Override
		protected IntLRUCacheValueHolder<V> onAllocatePoolItem() {
			return new IntLRUCacheValueHolder<V>();
		}

		@Override
		protected void onHandleRecycleItem(final IntLRUCacheValueHolder<V> pIntLRUCacheValueHolder) {
			pIntLRUCacheValueHolder.mIntLRUCacheQueueNode = null;
			pIntLRUCacheValueHolder.mValue = null;
		}
	};

	// ===========================================================
	// Constructors
	// ===========================================================

	public IntLRUCache(final int pCapacity) {
		this.mCapacity = pCapacity;
		this.mSparseArray = new SparseArray<IntLRUCacheValueHolder<V>>(pCapacity);
		this.mIntLRUCacheQueue = new IntLRUCacheQueue();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getCapacity() {
		return this.mCapacity;
	}

	public int getSize() {
		return this.mSize;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public boolean isEmpty() {
		return this.mSize == 0;
	}

	public V put(final int pKey, final V pValue) {
		final IntLRUCacheValueHolder<V> existingIntLRUCacheValueHolder = this.mSparseArray.get(pKey);
		if (existingIntLRUCacheValueHolder != null) {
			/* Just heat up that item. */
			this.mIntLRUCacheQueue.moveToTail(existingIntLRUCacheValueHolder.mIntLRUCacheQueueNode);

			return existingIntLRUCacheValueHolder.mValue;
		}

		if (this.mSize >= this.mCapacity) {
			final int deadKey = this.mIntLRUCacheQueue.poll();
			this.mSparseArray.remove(deadKey);
			this.mSize--;
		}

		final IntLRUCacheQueueNode intLRUCacheQueueNode = this.mIntLRUCacheQueue.add(pKey);

		final IntLRUCacheValueHolder<V> intLRUCacheValueHolder = this.mIntLRUCacheValueHolderPool.obtainPoolItem();
//		final IntLRUCacheValueHolder<V> intLRUCacheValueHolder = new IntLRUCacheValueHolder<V>();
		intLRUCacheValueHolder.mValue = pValue;
		intLRUCacheValueHolder.mIntLRUCacheQueueNode = intLRUCacheQueueNode;

		this.mSparseArray.put(pKey, intLRUCacheValueHolder);

		this.mSize++;

		return null;
	}

	public V get(final int pKey) {
		final IntLRUCacheValueHolder<V> intLRUCacheValueHolder = this.mSparseArray.get(pKey);
		if (intLRUCacheValueHolder == null) {
			return null;
		}

		this.mIntLRUCacheQueue.moveToTail(intLRUCacheValueHolder.mIntLRUCacheQueueNode);

		return intLRUCacheValueHolder.mValue;
	}

	public void clear() {
		while (!this.mIntLRUCacheQueue.isEmpty()) {
			final int key = this.mIntLRUCacheQueue.poll();
			final IntLRUCacheValueHolder<V> lruCacheValueHolder = this.mSparseArray.get(key);
			if (lruCacheValueHolder == null) {
				throw new IllegalArgumentException();
			}
			this.mSparseArray.remove(key);
			this.mIntLRUCacheValueHolderPool.recyclePoolItem(lruCacheValueHolder);
		}
		this.mSize = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	static class IntLRUCacheQueueNode {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		int mKey;
		IntLRUCacheQueueNode mPrevious;
		IntLRUCacheQueueNode mNext;

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	static class IntLRUCacheValueHolder<V> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		V mValue;
		IntLRUCacheQueueNode mIntLRUCacheQueueNode;

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	static class IntLRUCacheQueue {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private IntLRUCacheQueueNode mHead;
		private IntLRUCacheQueueNode mTail;

		private final GenericPool<IntLRUCacheQueueNode> mIntLRUCacheQueueNodePool = new GenericPool<IntLRUCache.IntLRUCacheQueueNode>() {
			@Override
			protected IntLRUCacheQueueNode onAllocatePoolItem() {
				return new IntLRUCacheQueueNode();
			}

			@Override
			protected void onHandleRecycleItem(final IntLRUCacheQueueNode pIntLRUCacheQueueNode) {
				pIntLRUCacheQueueNode.mKey = 0;
				pIntLRUCacheQueueNode.mPrevious = null;
				pIntLRUCacheQueueNode.mNext = null;
			}
		};

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public boolean isEmpty() {
			return this.mHead == null;
		}

		public IntLRUCacheQueueNode add(final int pKey) {
			final IntLRUCacheQueueNode intLRUCacheQueueNode = this.mIntLRUCacheQueueNodePool.obtainPoolItem();
//			final IntLRUCacheQueueNode intLRUCacheQueueNode = new IntLRUCacheQueueNode();
			intLRUCacheQueueNode.mKey = pKey;

			return this.add(intLRUCacheQueueNode);
		}

		private IntLRUCacheQueueNode add(final IntLRUCacheQueueNode pIntLRUCacheQueueNode) {
			if (this.isEmpty()) {
				this.mHead = pIntLRUCacheQueueNode;
				this.mTail = this.mHead;
			} else {
				this.mTail.mNext = pIntLRUCacheQueueNode;
				pIntLRUCacheQueueNode.mPrevious = this.mTail;
				this.mTail = pIntLRUCacheQueueNode;
			}

			return this.mTail;
		}

		public int poll() {
			final IntLRUCacheQueueNode head = this.mHead;
			final int key = this.mHead.mKey;
			if (key == 0) {
				throw new IllegalStateException();
			}

			/* Check if item to poll is the tail. */
			if (this.mHead.mNext == null) {
				this.mHead = null;
				this.mTail = null;
			} else {
				this.mHead = this.mHead.mNext;
				this.mHead.mPrevious = null;
			}

			this.mIntLRUCacheQueueNodePool.recyclePoolItem(head);
			return key;
		}

		public void moveToTail(final IntLRUCacheQueueNode pIntLRUCacheQueueNode) {
			final IntLRUCacheQueueNode next = pIntLRUCacheQueueNode.mNext;

			/* Check if the node already is the tail. */
			if (next == null) {
				return;
			} else {
				final IntLRUCacheQueueNode previous = pIntLRUCacheQueueNode.mPrevious;
				next.mPrevious = previous;

				/* Check if item to bump is the head. */
				if (previous == null) {
					this.mHead = next;
				} else {
					previous.mNext = next;
				}

				this.mTail.mNext = pIntLRUCacheQueueNode;
				pIntLRUCacheQueueNode.mPrevious = this.mTail;
				pIntLRUCacheQueueNode.mNext = null;
				this.mTail = pIntLRUCacheQueueNode;
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
