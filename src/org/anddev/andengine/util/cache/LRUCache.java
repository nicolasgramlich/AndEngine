package org.anddev.andengine.util.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.anddev.andengine.util.exception.AndEngineException;
import org.anddev.andengine.util.pool.Pool;
import org.anddev.andengine.util.pool.PoolItem;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 00:24:52 - 02.11.2011
 */
public class LRUCache<K, V> implements Map<K, V> {
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

	private final Pool<LRUCacheValueHolder<K, V>> mLRUCacheValueHolderPool = new Pool<LRUCache.LRUCacheValueHolder<K,V>>() {
		@Override
		protected LRUCacheValueHolder<K, V> onAllocatePoolItem() {
			return new LRUCacheValueHolder<K, V>();
		}
	};

	// ===========================================================
	// Constructors
	// ===========================================================

	public LRUCache(final int maxSize) {
		this.mCapacity = maxSize;
		this.mMap = new HashMap<K, LRUCacheValueHolder<K, V>>();
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

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.mSize == 0;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return this.mSize;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(final Object pKey) {
		return this.mMap.containsKey(pKey);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(final K pKey, final V pValue) {
		if(this.mSize >= this.mCapacity) {
			final K deadKey = this.mLRUCacheQueue.poll();
			this.mMap.remove(deadKey);
			this.mSize--;
		}

		/* Just heat up that item. */
		if(this.mMap.containsKey(pKey)) {
			return this.get(pKey);
		}

		final LRUCacheQueueNode<K> lruCacheQueueNode = this.mLRUCacheQueue.add(pKey);

		final LRUCacheValueHolder<K, V> lruCacheValueHolder = this.mLRUCacheValueHolderPool.obtainPoolItem();
		lruCacheValueHolder.mValue = pValue;
		lruCacheValueHolder.mLRUCacheQueueNode = lruCacheQueueNode;

		this.mMap.put(pKey, lruCacheValueHolder);

		this.mSize++;

		return null;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public V get(final Object pKey) {
		final LRUCacheValueHolder<K, V> lruCacheValueHolder = this.mMap.get(pKey);
		if(lruCacheValueHolder == null) {
			return null;
		}

		this.mLRUCacheQueue.moveToTail(lruCacheValueHolder.mLRUCacheQueueNode);

		return lruCacheValueHolder.mValue;
	}

	@Override
	public void clear() {
		throw new AndEngineException("Not yet implemented.");
	}

	@Override
	public boolean containsValue(final Object pValue) {
		throw new AndEngineException("Not yet implemented.");
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new AndEngineException("Not yet implemented.");
	}

	@Override
	public Set<K> keySet() {
		throw new AndEngineException("Not yet implemented.");
	}

	@Override
	public void putAll(final Map<? extends K, ? extends V> pMap) {
		throw new AndEngineException("Not yet implemented.");
	}

	@Override
	public V remove(final Object pKey) {
		throw new AndEngineException("Not yet implemented.");
	}

	@Override
	public Collection<V> values() {
		throw new AndEngineException("Not yet implemented.");
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class LRUCacheQueueNode<K> extends PoolItem {
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

	private static class LRUCacheValueHolder<K, V> extends PoolItem {
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

	private static class LRUCacheQueue<K> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private LRUCacheQueueNode<K> mHead;
		private LRUCacheQueueNode<K> mTail;

		private final Pool<LRUCacheQueueNode<K>> mLRUCacheQueueNodePool = new Pool<LRUCache.LRUCacheQueueNode<K>>() {
			@Override
			protected LRUCacheQueueNode<K> onAllocatePoolItem() {
				return new LRUCacheQueueNode<K>();
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
			if(this.isEmpty()) {
				return null;
			}

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
				this.mTail = pLRUCacheQueueNode;
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
