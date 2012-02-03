package org.andengine.util.adt.list;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:00:30 - 24.01.2012
 */
public interface IQueue<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isEmpty();
	public T get(final int pIndex) throws IndexOutOfBoundsException;
	public int indexOf(final T pItem);
	public T peek();
	public T poll();
	public void enter(final T pItem);
	public void enter(final int pIndex, final T pItem) throws IndexOutOfBoundsException;
	public boolean remove(final T pItem);
	public T remove(final int pIndex) throws IndexOutOfBoundsException;
	public int size();
	public void clear();
}
