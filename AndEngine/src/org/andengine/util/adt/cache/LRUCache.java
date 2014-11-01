package org.andengine.util.adt.cache;

import java.util.HashMap;

import org.andengine.util.adt.pool.GenericPool;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 00:24:52 - 02.11.2011
 */
public class LRUCache<K, V> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mCapacity;
	private int mSize;

	private final HashMap<K, LRUCacheValueHolder<K, V>> mMap;
	private final LRUCacheQueue<K> mLRUCacheQueue;

	private final GenericPool<LRUCacheValueHolder<K, V>> mLRUCacheValueHolderPool = new GenericPool<LRUCache.LRUCacheValueHolder<K, V>>() {
		@Override
		protected LRUCacheValueHolder<K, V> onAllocatePoolItem() {
			return new LRUCacheValueHolder<K, V>();
		}

		@Override
		protected void onHandleRecycleItem(final LRUCacheValueHolder<K, V> pLRUCacheValueHolder) {
			pLRUCacheValueHolder.mLRUCacheQueueNode = null;
			pLRUCacheValueHolder.mValue = null;
		}
	};

	// ===========================================================
	// Constructors
	// ===========================================================

	public LRUCache(final int pCapacity) {
		this.mCapacity = pCapacity;
		this.mMap = new HashMap<K, LRUCacheValueHolder<K, V>>(pCapacity);
		this.mLRUCacheQueue = new LRUCacheQueue<K>();
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

	public V put(final K pKey, final V pValue) {
		final LRUCacheValueHolder<K, V> existingLRUCacheValueHolder = this.mMap.get(pKey);
		if(existingLRUCacheValueHolder != null) {
			/* Just heat up that item. */
			this.mLRUCacheQueue.moveToTail(existingLRUCacheValueHolder.mLRUCacheQueueNode);

			return existingLRUCacheValueHolder.mValue;
		}

		if(this.mSize >= this.mCapacity) {
			final K deadKey = this.mLRUCacheQueue.poll();
			this.mMap.remove(deadKey);
			this.mSize--;
		}

		final LRUCacheQueueNode<K> lruCacheQueueNode = this.mLRUCacheQueue.add(pKey);

		final LRUCacheValueHolder<K, V> lruCacheValueHolder = this.mLRUCacheValueHolderPool.obtainPoolItem();
//		final LRUCacheValueHolder<K, V> lruCacheValueHolder = new LRUCacheValueHolder<K, V>();
		lruCacheValueHolder.mValue = pValue;
		lruCacheValueHolder.mLRUCacheQueueNode = lruCacheQueueNode;

		this.mMap.put(pKey, lruCacheValueHolder);

		this.mSize++;

		return null;
	}

	public V get(final K pKey) {
		final LRUCacheValueHolder<K, V> lruCacheValueHolder = this.mMap.get(pKey);
		if(lruCacheValueHolder == null) {
			return null;
		}

		this.mLRUCacheQueue.moveToTail(lruCacheValueHolder.mLRUCacheQueueNode);

		return lruCacheValueHolder.mValue;
	}

	public void clear() {
		while(!this.mLRUCacheQueue.isEmpty()) {
			final K key = this.mLRUCacheQueue.poll();
			final LRUCacheValueHolder<K, V> lruCacheValueHolder = this.mMap.remove(key);
			this.mLRUCacheValueHolderPool.recyclePoolItem(lruCacheValueHolder);
		}

		this.mSize = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	static class LRUCacheQueueNode<K> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		K mKey;
		LRUCacheQueueNode<K> mPrevious;
		LRUCacheQueueNode<K> mNext;

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

	static class LRUCacheValueHolder<K, V> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		V mValue;
		LRUCacheQueueNode<K> mLRUCacheQueueNode;

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

	static class LRUCacheQueue<K> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private LRUCacheQueueNode<K> mHead;
		private LRUCacheQueueNode<K> mTail;

		private final GenericPool<LRUCacheQueueNode<K>> mLRUCacheQueueNodePool = new GenericPool<LRUCache.LRUCacheQueueNode<K>>() {
			@Override
			protected LRUCacheQueueNode<K> onAllocatePoolItem() {
				return new LRUCacheQueueNode<K>();
			}

			@Override
			protected void onHandleRecycleItem(final LRUCacheQueueNode<K> pLRUCacheQueueNode) {
				pLRUCacheQueueNode.mKey = null;
				pLRUCacheQueueNode.mPrevious = null;
				pLRUCacheQueueNode.mNext = null;
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

		public LRUCacheQueueNode<K> add(final K pKey) {
			final LRUCacheQueueNode<K> lruCacheQueueNode = this.mLRUCacheQueueNodePool.obtainPoolItem();
//			final LRUCacheQueueNode<K> lruCacheQueueNode = new LRUCacheQueueNode<K>();
			lruCacheQueueNode.mKey = pKey;

			return this.add(lruCacheQueueNode);
		}

		private LRUCacheQueueNode<K> add(final LRUCacheQueueNode<K> pLRUCacheQueueNode) {
			if(this.isEmpty()) {
				this.mHead = pLRUCacheQueueNode;
				this.mTail = this.mHead;
			} else {
				this.mTail.mNext = pLRUCacheQueueNode;
				pLRUCacheQueueNode.mPrevious = this.mTail;
				this.mTail = pLRUCacheQueueNode;
			}

			return this.mTail;
		}

		public K poll() {
			final LRUCacheQueueNode<K> head = this.mHead;
			final K key = this.mHead.mKey;

			/* Check if item to poll is the tail. */
			if(this.mHead.mNext == null) {
				this.mHead = null;
				this.mTail = null;
			} else {
				this.mHead = this.mHead.mNext;
				this.mHead.mPrevious = null;
			}

			this.mLRUCacheQueueNodePool.recyclePoolItem(head);
			return key;
		}

		public void moveToTail(final LRUCacheQueueNode<K> pLRUCacheQueueNode) {
			final LRUCacheQueueNode<K> next = pLRUCacheQueueNode.mNext;

			/* Check if the node already is the tail. */
			if(next == null) {
				return;
			} else {
				final LRUCacheQueueNode<K> previous = pLRUCacheQueueNode.mPrevious;
				next.mPrevious = previous;

				/* Check if item to bump is the head. */
				if(previous == null) {
					this.mHead = next;
				} else {
					previous.mNext = next;
				}

				this.mTail.mNext = pLRUCacheQueueNode;
				pLRUCacheQueueNode.mPrevious = this.mTail;
				pLRUCacheQueueNode.mNext = null;
				this.mTail = pLRUCacheQueueNode;
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
