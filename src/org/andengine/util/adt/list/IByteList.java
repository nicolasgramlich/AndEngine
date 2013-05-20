package org.andengine.util.adt.list;

/**
 * (c) 2013 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:18:45 - 19.01.2013
 */
public interface IByteList {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isEmpty();
	public byte get(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public void add(final byte pItem);
	public void add(final int pIndex, final byte pItem) throws ArrayIndexOutOfBoundsException;
	public byte remove(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public int size();
	public void clear();
	public byte[] toArray();
}