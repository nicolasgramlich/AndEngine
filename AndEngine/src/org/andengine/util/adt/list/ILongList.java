package org.andengine.util.adt.list;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 19:36:57 - 03.05.2012
 */
public interface ILongList {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isEmpty();
	public float get(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public void add(final long pItem);
	public void add(final int pIndex, final long pItem) throws ArrayIndexOutOfBoundsException;
	public float remove(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public int size();
	public void clear();
	public long[] toArray();
}