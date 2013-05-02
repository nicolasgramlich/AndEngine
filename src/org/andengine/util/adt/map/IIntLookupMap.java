package org.andengine.util.adt.map;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:31:34 - 26.04.2012
 */
public interface IIntLookupMap<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void add(final T pItem, final int pValue);
	public T item(final int pValue);
	public int value(final T pItem);
}
