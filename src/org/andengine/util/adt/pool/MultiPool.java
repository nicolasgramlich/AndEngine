package org.andengine.util.adt.pool;

import android.util.SparseArray;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 10:13:26 - 02.03.2011
 */
public class MultiPool<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SparseArray<GenericPool<T>> mPools = new SparseArray<GenericPool<T>>();

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

	public void registerPool(final int pID, final GenericPool<T> pPool) {
		this.mPools.put(pID, pPool);
	}

	public T obtainPoolItem(final int pID) {
		final GenericPool<T> pool = this.mPools.get(pID);
		if (pool == null) {
			return null;
		} else {
			return pool.obtainPoolItem();
		}
	}

	public void recyclePoolItem(final int pID, final T pItem) {
		final GenericPool<T> pool = this.mPools.get(pID);
		if (pool != null) {
			pool.recyclePoolItem(pItem);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
