package org.anddev.andengine.util.cache;

import java.util.HashMap;
import java.util.LinkedList;

import org.anddev.andengine.util.exception.AndEngineException;

/**
 * Implemented as an extended {@link HashMap} with a maximum capacity and an aggregated {@link LinkedList} used as LRU queue.
 *
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 22:33:34 - 01.11.2011
 */
public class LRUCache<K, V> extends HashMap<K, V> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 3345124753192560741L;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mCapacity;
	private final LinkedList<K> mCache;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LRUCache(final int pCapacity) {
		super(pCapacity);

		if(pCapacity <= 0) {
			throw new AndEngineException("pCapacity must be > 0.");
		}

		this.mCapacity = pCapacity;
		this.mCache = new LinkedList<K>();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void clear() {
		super.clear();
		this.mCache.clear();
	}

	@Override
	public V put(final K pKey, final V pValue) {
		/* If the key isn't in the cache and the cache is full, drop the last entry. */
		if (!super.containsKey(pKey) && !this.mCache.isEmpty() && this.mCache.size() + 1 > this.mCapacity) {
			final K deadKey = this.mCache.removeLast();
			super.remove(deadKey);
		}

		this.updateKey(pKey);
		return super.put(pKey, pValue);
	}

	@Override
	public V get(final Object pKey) {
		final V value = super.get(pKey);
		if (value != null) {
			this.updateKey(pKey);
		}
		return value;
	}

	@Override
	public V remove(final Object pKey) {
		this.mCache.remove(pKey);
		return super.remove(pKey);
	}

	@SuppressWarnings("unchecked")
	private void updateKey(final Object pKey) {
		this.mCache.remove(pKey);
		this.mCache.addFirst((K) pKey);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
