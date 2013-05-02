package org.andengine.util.adt.list;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:14:45 - 27.01.2012
 */
public interface IFloatList {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isEmpty();
	public float get(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public void add(final float pItem);
	public void add(final int pIndex, final float pItem) throws ArrayIndexOutOfBoundsException;
	public float remove(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public int size();
	public void clear();
	public float[] toArray();
}