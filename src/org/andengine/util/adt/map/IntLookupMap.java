package org.andengine.util.adt.map;

import java.util.HashMap;

import android.util.SparseArray;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:30:35 - 26.04.2012
 */
public class IntLookupMap<T> implements IIntLookupMap<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final HashMap<T, Integer> mItemToValueMapping = new HashMap<T, Integer>();
	private final SparseArray<T> mValueToItemMapping = new SparseArray<T>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void add(final T pName, final int pValue) {
		this.mItemToValueMapping.put(pName, Integer.valueOf(pValue));
		this.mValueToItemMapping.put(pValue, pName);
	}

	@Override
	public T item(final int pValue) {
		return this.mValueToItemMapping.get(pValue);
	}

	@Override
	public int value(final T pName) {
		final Integer value = this.mItemToValueMapping.get(pName);
		if (value == null) {
			return -1;
		} else {
			return value.intValue();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}