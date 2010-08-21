package org.anddev.andengine.util;

import android.util.SparseArray;

/**
 * @author Nicolas Gramlich
 * @since 11:51:29 - 20.08.2010
 * @param <T>
 */
public class Library<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	protected final SparseArray<T> mItems;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public Library() {
		this.mItems = new SparseArray<T>();
	}
	
	public Library(final int pInitialCapacity) {
		this.mItems = new SparseArray<T>(pInitialCapacity);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public void put(final int pID, final T pItem) {
		this.mItems.put(pID, pItem);
	}
	
	public T get(final int pID) {
		return this.mItems.get(pID);
	}

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
