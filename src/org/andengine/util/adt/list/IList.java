package org.andengine.util.adt.list;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:27:16 - 01.02.2012
 */
public interface IList<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isEmpty();
	public T get(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public void add(final T pItem);
	public void add(final int pIndex, final T pItem) throws ArrayIndexOutOfBoundsException;
	public boolean remove(final T pItem);
	public T remove(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public int size();
	public void clear();
}
