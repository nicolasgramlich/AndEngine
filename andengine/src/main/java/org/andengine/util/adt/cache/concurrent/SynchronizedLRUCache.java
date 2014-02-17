package org.andengine.util.adt.cache.concurrent;

import org.andengine.util.adt.cache.LRUCache;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:26:47 - 16.01.2012
 */
public class SynchronizedLRUCache<K, V> extends LRUCache<K, V> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SynchronizedLRUCache(final int pCapacity) {
		super(pCapacity);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public synchronized int getSize() {
		return super.getSize();
	}

	@Override
	public synchronized boolean isEmpty() {
		return super.isEmpty();
	}

	@Override
	public synchronized V put(final K pKey, final V pValue) {
		return super.put(pKey, pValue);
	}

	@Override
	public synchronized V get(final K pKey) {
		return super.get(pKey);
	}

	@Override
	public synchronized void clear() {
		super.clear();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
