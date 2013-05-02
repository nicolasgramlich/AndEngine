package org.andengine.util.adt.list;

/**
 * (c) 2013 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:18:46 - 19.01.2013
 */
public interface IDoubleList {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isEmpty();
	public double get(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public void add(final double pItem);
	public void add(final int pIndex, final double pItem) throws ArrayIndexOutOfBoundsException;
	public double remove(final int pIndex) throws ArrayIndexOutOfBoundsException;
	public int size();
	public void clear();
	public double[] toArray();
}