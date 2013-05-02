package org.andengine.util.adt.list;

/**
 * (c) 2013 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:13:13 - 10.01.2013
 */
public interface ICharList {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isEmpty();
	public char get(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public void add(final char pItem);
	public void add(final int pIndex, final char pItem) throws ArrayIndexOutOfBoundsException;
	public char remove(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public int size();
	public void clear();
	public char[] toArray();
}