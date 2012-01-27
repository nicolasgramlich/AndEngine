package org.andengine.util.list;

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
	public void enter(final T pItem);
	public int size();
	public T peek();
	public T poll();
}
