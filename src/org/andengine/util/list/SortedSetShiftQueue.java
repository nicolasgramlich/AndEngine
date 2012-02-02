package org.andengine.util.list;

import java.util.Arrays;



/**
 * This implementation is particular useful/efficient for enter/poll operations of elements that need to be sorted by natural order instead of the order they are queue.
 * Its {@link java.util.Queue} like behavior performs better than a plain {@link java.util.ArrayList}, since it automatically shift the contents of its internal Array only when really necessary.
 * Besides sparse allocations to increase the size of the internal Array, {@link com.zynga.mobileville.path.SortedSetShiftQueue} is allocation free (unlike the {@link java.util.LinkedList} family).
 *
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @author Greg Haynes
 * @since 15:02:40 - 24.02.2012
 */
public class SortedSetShiftQueue<T extends Comparable<T>> extends ShiftQueue<T> implements ISortedList<T>, ISortedQueue<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int INDEX_INVALID = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SortedSetShiftQueue() {
		super();
	}

	public SortedSetShiftQueue(final int pInitialCapacity) {
		super(pInitialCapacity);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void enter(final T pItem) {
		final int internalIndex = this.binarySearchInternal(pItem, true);
		if(internalIndex < 0) {
			this.addInternal(SortedSetShiftQueue.encodeInsertionIndex(internalIndex), pItem);
		} else {
			this.addInternal(internalIndex, pItem);
		}
	}

	@Override
	public boolean remove(final T pItem) {
		if(pItem == null) {
			super.remove(pItem);
		}

		final int internalIndex = this.binarySearchInternal(pItem, false);
		if(internalIndex >= 0) {
			this.removeInternal(internalIndex);
			return true;
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public int binarySearch(final T pItem) {
		final int indexInernal = this.binarySearchInternal(pItem, false);
		if(indexInernal == SortedSetShiftQueue.INDEX_INVALID) {
			return SortedSetShiftQueue.INDEX_INVALID;
		} else {
			return indexInernal - this.mHead;
		}
	}

	private int binarySearchInternal(final T pItem, final boolean pReturnSequenceEndIfNoEqualItemFound) {
		final int guess = Arrays.binarySearch(this.mItems, this.mHead, this.mTail, pItem);
		if(guess >= 0) {
			return this.scanForEqualItem(this.mHead, this.mTail, guess, pItem, pReturnSequenceEndIfNoEqualItemFound);
		} else {
			return guess;
		}
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
	@SuppressWarnings("unchecked")
	private int scanForEqualItem(final int pStart, final int pEnd, final int pGuess, final T pItem, final boolean pReturnSequenceEndIfNoEqualItemFound) {
		/* Quickly move to the beginning of the sequence. */
		int i = pGuess - 1;
		while((i >= pStart) && (pItem.compareTo((T) this.mItems[i]) == 0)) {
			i--;
		}
		i++;

		/* From the beginning of the sequence, advance until the first item equals pItem or the end has been reached. */
		while(i < pEnd) {
			final T item = (T) this.mItems[i];
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
					return SortedSetShiftQueue.encodeInsertionIndex(i);
				}
			}
			i++;
		}

		if(pReturnSequenceEndIfNoEqualItemFound) {
			return i;
		} else {
			return SortedSetShiftQueue.INDEX_INVALID;
		}
	}

	private static final int encodeInsertionIndex(final int pIndex) {
		return (-pIndex) - 1;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
