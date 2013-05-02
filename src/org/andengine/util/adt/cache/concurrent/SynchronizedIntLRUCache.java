package org.andengine.util.adt.cache.concurrent;

import org.andengine.util.adt.cache.IntLRUCache;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:26:47 - 16.01.2012
 */
public class SynchronizedIntLRUCache<V> extends IntLRUCache<V> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SynchronizedIntLRUCache(final int pCapacity) {
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
	public synchronized V put(final int pKey, final V pValue) {
		return super.put(pKey, pValue);
	}

	@Override
	public synchronized V get(final int pKey) {
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
