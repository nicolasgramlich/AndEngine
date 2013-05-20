package org.andengine.util.adt.list;



/**
 * This implementation is particular useful/efficient for enter/poll operations of elements that need to be sorted by natural order instead of the order they are queue.
 * Its {@link java.util.List} like behavior performs better than a plain {@link java.util.ArrayList}, since it automatically shift the contents of its internal Array only when really necessary.
 * Besides sparse allocations to increase the size of the internal Array, {@link SortedList} is allocation free (unlike the {@link java.util.LinkedList} family).
 *
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @author Greg Haynes
 * @since 15:02:40 - 24.02.2012
 */
public class SortedList<T extends Comparable<T>> implements ISortedList<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int INDEX_INVALID = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final IList<T> mList;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SortedList(final IList<T> pList) {
		this.mList = pList;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean isEmpty() {
		return this.mList.isEmpty();
	}

	@Override
	public T get(final int pIndex) throws IndexOutOfBoundsException {
		return this.mList.get(pIndex);
	}

	@Override
	@Deprecated
	public void set(final int pIndex, final T pItem) throws IndexOutOfBoundsException {
		this.mList.set(pIndex, pItem);
	}

	@Override
	public int indexOf(final T pItem) {
		return this.binarySearch(pItem, false);
	}

	@Override
	@Deprecated
	public void add(final int pIndex, final T pItem) {
		this.mList.add(pItem);
	}

	@Override
	public void add(final T pItem) {
		final int index = this.binarySearch(pItem, true);
		if (index < 0) {
			this.mList.add(ListUtils.encodeInsertionIndex(index), pItem);
		} else {
			this.mList.add(index, pItem);
		}
	}

	@Override
	public T removeFirst() {
		return this.mList.removeFirst();
	}

	@Override
	public T removeLast() {
		return this.mList.removeLast();
	}

	@Override
	public boolean remove(final T pItem) {
		if (pItem == null) {
			return this.mList.remove(pItem);
		}

		final int index = this.binarySearch(pItem, false);
		if (index >= 0) {
			this.mList.remove(index);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public T remove(final int pIndex) {
		return this.mList.remove(pIndex);
	}

	@Override
	public int size() {
		return this.mList.size();
	}

	@Override
	public void clear() {
		this.mList.clear();
	}

	@Override
	public String toString() {
		return ListUtils.toString(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private int binarySearch(final T pItem, final boolean pReturnSequenceEndIfNoEqualItemFound) {
		final int size = this.mList.size();
		final int guess = this.binarySearch(0, size, pItem);
		if (guess >= 0) {
			return this.scanForEqualItem(0, size, guess, pItem, pReturnSequenceEndIfNoEqualItemFound);
		} else {
			return guess;
		}
	}

	private int binarySearch(final int pStart, final int pEnd, final T pItem) {
		int low = pStart;
		int high = pEnd - 1;

		while (low <= high) {
			final int mid = (low + high) >>> 1;
			final T midVal = this.mList.get(mid);

			final int diff = pItem.compareTo(midVal);
			if (diff > 0) {
				low = mid + 1;
			} else if (diff < 0) {
				high = mid - 1;
			} else {
				return mid;
			}
		}
		return ListUtils.encodeInsertionIndex(low);
	}

	/**
	 * Scans for items around <code>pGuess</code> that fulfill <code>pItem.compareTo(item) == 0</code> and starting from the leftmost found, it returns the index of the first one that fulfills <code>pItem.equals(item)</code>.
	 *
	 * @param pStart left bound.
	 * @param pEnd right bound.
	 * @param pGuess index to start the search.
	 * @param pItem to perform <code>pItem.compareTo(item) == 0</code> and <code>pItem.equals(item)</code> checks on.
	 * @param pReturnSequenceEndIfNoEqualItemFound
	 * @return
	 */
	private int scanForEqualItem(final int pStart, final int pEnd, final int pGuess, final T pItem, final boolean pReturnSequenceEndIfNoEqualItemFound) {
		/* Quickly move to the beginning of the sequence. */
		int i = pGuess - 1;
		while ((i >= pStart) && (pItem.compareTo(this.mList.get(i)) == 0)) {
			i--;
		}
		i++;

		/* From the beginning of the sequence, advance until the first item equals pItem or the end has been reached. */
		while (i < pEnd) {
			final T item = this.mList.get(i);
			if (i <= pGuess) {
				/* Since the compartTo check has already been performed, only equals needs to be checked. */
				if (pItem.equals(item)) {
					/* Item found. */
					return i;
				}
			} else {
				/* Check if the sequence is still ongoing. */
				if (pItem.compareTo(item) == 0) {
					if (pItem.equals(item)) {
						/* Item found. */
						return i;
					}
				} else {
					/* Return the last known position. */
					return ListUtils.encodeInsertionIndex(i);
				}
			}
			i++;
		}

		if (pReturnSequenceEndIfNoEqualItemFound) {
			return i;
		} else {
			return SortedList.INDEX_INVALID;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
