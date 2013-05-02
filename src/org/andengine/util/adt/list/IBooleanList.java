package org.andengine.util.adt.list;

/**
 * (c) 2013 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:05:14 - 19.01.2013
 */
public interface IBooleanList {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isEmpty();
	public boolean get(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public void add(final boolean pItem);
	public void add(final int pIndex, final boolean pItem) throws ArrayIndexOutOfBoundsException;
	public boolean remove(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public int size();
	public void clear();
	public int[] toArray();
}