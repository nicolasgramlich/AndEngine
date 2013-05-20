package org.andengine.util.adt.list;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 19:21:53 - 03.05.2012
 */
public interface IIntList {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isEmpty();
	public int get(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public void add(final int pItem);
	public void add(final int pIndex, final int pItem) throws ArrayIndexOutOfBoundsException;
	public int remove(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public int size();
	public void clear();
	public int[] toArray();
}