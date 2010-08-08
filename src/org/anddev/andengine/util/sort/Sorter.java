package org.anddev.andengine.util.sort;

import java.util.Comparator;
import java.util.List;

/**
 * @author Nicolas Gramlich
 * @since 14:14:39 - 06.08.2010
 * @param <T>
 */
public abstract class Sorter<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public abstract void sort(final T[] pArray, final int pStart, final int pEnd, final Comparator<T> pComparator);

	public abstract void sort(final List<T> pList, final int pStart, final int pEnd, final Comparator<T> pComparator);

	// ===========================================================
	// Methods
	// ===========================================================

	public final void sort(final T[] pArray, final Comparator<T> pComparator){
		sort(pArray, 0, pArray.length, pComparator);
	}
	
	public final void sort(final List<T> pList, final Comparator<T> pComparator){
		sort(pList, 0, pList.size(), pComparator);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
