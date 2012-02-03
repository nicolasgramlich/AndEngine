package org.andengine.util.adt.list;

import org.andengine.util.adt.list.IQueue;
import org.andengine.util.adt.list.ISortedQueue;




/**
 * This implementation is particular useful/efficient for enter/poll operations of elements that need to be sorted by natural order instead of the order they are queue.
 * Its {@link java.util.Queue} like behavior performs better than a plain {@link java.util.ArrayList}, since it automatically shift the contents of its internal Array only when really necessary.
 * Besides sparse allocations to increase the size of the internal Array, {@link com.zynga.mobileville.path.SortedQueue} is allocation free (unlike the {@link java.util.LinkedList} family).
 *
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @author Greg Haynes
 * @since 15:02:40 - 24.02.2012
 */
public class SortedQueue<T extends Comparable<T>> implements ISortedQueue<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int INDEX_INVALID = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final IQueue<T> mQueue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SortedQueue(final IQueue<T> pQueue) {
		this.mQueue = pQueue;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean isEmpty() {
		return this.mQueue.isEmpty();
	}

	@Override
	public T get(int pIndex) throws IndexOutOfBoundsException {
		return this.mQueue.get(pIndex);
	}

	@Override
	public void enter(int pIndex, T pItem) {
		this.mQueue.enter(pItem);
	}

	@Override
	public void enter(final T pItem) {
		final int index = this.binarySearch(pItem, true);
		if(index < 0) {
			this.mQueue.enter(SortedQueue.encodeInsertionIndex(index), pItem);
		} else {
			this.mQueue.enter(index, pItem);
		}
	}

	@Override
	public boolean remove(final T pItem) {
		if(pItem == null) {
			return this.mQueue.remove(pItem);
		}

		final int index = this.binarySearch(pItem, false);
		if(index >= 0) {
			this.mQueue.remove(index);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public T remove(int pIndex) {
		return this.mQueue.remove(pIndex);
	}

	@Override
	public int size() {
		return this.mQueue.size();
	}

	@Override
	public T peek() {
		return this.mQueue.peek();
	}

	@Override
	public T poll() {
		return this.mQueue.poll();
	}

	@Override
	public void clear() {
		this.mQueue.clear();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public int binarySearch(final T pItem) {
		return this.binarySearch(pItem, false);
	}

	private int binarySearch(final T pItem, final boolean pReturnSequenceEndIfNoEqualItemFound) {
		final int guess = this.binarySearch(0, this.mQueue.size(), pItem);
		if(guess >= 0) {
			return this.scanForEqualItem(0, this.mQueue.size(), guess, pItem, pReturnSequenceEndIfNoEqualItemFound);
		} else {
			return guess;
		}
	}

	private int binarySearch(final int pStart, int pEnd, T pItem) {
		int low = pStart;
		int high = pEnd - 1;

		while (low <= high) {
			final int mid = (low + high) >>> 1;
			final T midVal = this.mQueue.get(mid);

			final int diff = pItem.compareTo(midVal);
			if (diff > 0)
				low = mid + 1;
			else if (diff < 0)
				high = mid - 1;
			else
				return mid;
		}
		return encodeInsertionIndex(low); 
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
		while((i >= pStart) && (pItem.compareTo((T) this.mQueue.get(i)) == 0)) {
			i--;
		}
		i++;

		/* From the beginning of the sequence, advance until the first item equals pItem or the end has been reached. */
		while(i < pEnd) {
			final T item = (T) this.mQueue.get(i);
			if(i <= pGuess) {
				/* Since the compartTo check has already been performed, only equals needs to be checked. */
				if(pItem.equals(item)) {
					/* Item found. */
					return i;
				}
			} else {
				/* Check if the sequence is still ongoing. */
				if(pItem.compareTo(item) == 0) {
					if(pItem.equals(item)) {
						/* Item found. */
						return i;
					}
				} else {
					/* Return the last known position. */
					return SortedQueue.encodeInsertionIndex(i);
				}
			}
			i++;
		}

		if(pReturnSequenceEndIfNoEqualItemFound) {
			return i;
		} else {
			return SortedQueue.INDEX_INVALID;
		}
	}

	private static final int encodeInsertionIndex(final int pIndex) {
		return (-pIndex) - 1;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
