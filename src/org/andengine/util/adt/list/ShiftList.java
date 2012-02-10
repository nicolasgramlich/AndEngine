package org.andengine.util.adt.list;

import java.util.Arrays;

import org.andengine.util.adt.queue.IQueue;

/**
 * TODO This class could take some kind of AllocationStrategy object.
 *
 * This implementation is particular useful/efficient for enter/poll operations.
 * Its {@link java.util.Queue} like behavior performs better than a plain {@link java.util.ArrayList}, since it automatically shift the contents of its internal Array only when really necessary.
 * Besides sparse allocations to increase the size of the internal Array, {@link ShiftList} is allocation free (unlike the {@link java.util.LinkedList} family).
 * 
 * Supports <code>null</code> items.
 *
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @author Greg Haynes
 * @since 15:02:40 - 24.02.2012
 */
public class ShiftList<T> implements IQueue<T>, IList<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAPACITY_INITIAL_DEFAULT = 1;
	private static final int INDEX_INVALID = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	protected Object[] mItems;
	protected int mHead;
	protected int mTail;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ShiftList() {
		this(ShiftList.CAPACITY_INITIAL_DEFAULT);
	}

	public ShiftList(final int pInitialCapacity) {
		this.mItems = new Object[pInitialCapacity];
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean isEmpty() {
		return this.mHead == this.mTail;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T get(final int pIndex) throws ArrayIndexOutOfBoundsException {
		return (T) this.mItems[this.mHead + pIndex];
	}

	@Override
	public void set(final int pIndex, final T pItem) throws IndexOutOfBoundsException {
		this.mItems[this.mHead + pIndex] = pItem;
	}

	@Override
	public int indexOf(final T pItem) {
		if(pItem == null) {
			for(int i = this.mHead; i < this.mTail; i++) {
				if(this.mItems[i] == null) {
					return i - this.mHead;
				}
			}
		} else {
			for(int i = this.mHead; i < this.mTail; i++) {
				if(pItem.equals(this.mItems[i])) {
					return i - this.mHead;
				}
			}
		}
		return ShiftList.INDEX_INVALID;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T peek() {
		if(this.mHead == this.mTail) {
			return null;
		} else {
			return (T) this.mItems[this.mHead];
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T poll() {
		if(this.mHead == this.mTail) {
			return null;
		} else {
			final T item = (T) this.mItems[this.mHead];
			this.mItems[this.mHead] = null;
			this.mHead++;
			if(this.mHead == this.mTail) {
				this.mHead = 0;
				this.mTail = 0;
			}
			return item;
		}
	}

	@Override
	public void enter(final T pItem) {
		this.ensureShiftableRight();
		this.mItems[this.mTail] = pItem;
		this.mTail++;
	}

	@Override
	public void enter(final int pIndex, final T pItem) throws ArrayIndexOutOfBoundsException {
		final int size = this.mTail - this.mHead;
		/* Check which side to shift to is more efficient. */
		if(pIndex < (size >> 1)) {
			/* Shift left. */
			this.enterShiftingLeft(pIndex, pItem);
		} else {
			/* Shift right. */
			this.enterShiftingRight(pIndex, pItem, size);
		}
	}

	private void enterShiftingRight(final int pIndex, final T pItem, final int size) {
		this.ensureShiftableRight();

		/* Check if items need to be copied. */
		final int shiftAmount = size - pIndex;
		if(shiftAmount == 0) {
			/* Nothing to shift, we can insert at the tail. */
			this.mItems[this.mTail] = pItem;
		} else {
			/* Shift all items to the right of pIndex one to the right, so there is a free spot at pIndex. */
			final int internalIndex = this.mHead + pIndex;
			System.arraycopy(this.mItems, internalIndex, this.mItems, internalIndex + 1, shiftAmount);
			this.mItems[internalIndex] = pItem;
		}

		this.mTail++;
	}

	private void enterShiftingLeft(final int pIndex, final T pItem) {
		this.ensureShiftableLeft();

		this.mHead--;

		/* Check if items need to be copied. */
		if(pIndex == 0) {
			/* Nothing to shift, we can insert at the head. */
			this.mItems[this.mHead] = pItem;
		} else {
			/* Shift all items to the left if pIndex one to the left, so there is a free spot at pIndex. */
			System.arraycopy(this.mItems, this.mHead + 1, this.mItems, this.mHead, pIndex);
			final int internalIndex = this.mHead + pIndex;
			this.mItems[internalIndex] = pItem;
		}
	}

	@Override
	public void add(final T pItem) {
		this.enter(pItem);
	}

	@Override
	public void add(final int pIndex, final T pItem) throws ArrayIndexOutOfBoundsException {
		this.enter(pIndex, pItem);
	}

	@Override
	public T removeFirst() {
		return this.remove(0);
	}
	
	@Override
	public T removeLast() {
		return this.remove(this.size() - 1);
	}

	@Override
	public boolean remove(final T pItem) {
		final int index = this.indexOf(pItem);
		if(index >= 0) {
			this.remove(index);
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T remove(final int pIndex) throws ArrayIndexOutOfBoundsException {
		final int internalIndex = this.mHead + pIndex;
		final T removed = (T) this.mItems[internalIndex];

		final int size = this.mTail - this.mHead;

		/* Determine which side to shift to makes more sense. */
		final int center = size >> 1;
		if(pIndex < center) {
			/* Shift right. */
			if(internalIndex > this.mHead) {
				System.arraycopy(this.mItems, this.mHead, this.mItems, this.mHead + 1, pIndex);
			}
			this.mItems[this.mHead] = null;
			this.mHead++;
		} else {
			/* Shift left. */
			final int shiftAmount = size - pIndex - 1;
			if(shiftAmount > 0) {
				System.arraycopy(this.mItems, internalIndex + 1, this.mItems, internalIndex, shiftAmount);
			}
			this.mTail--;
			this.mItems[this.mTail] = null;
		}

		return removed;
	}

	@Override
	public int size() {
		return this.mTail - this.mHead;
	}

	@Override
	public void clear() {
		Arrays.fill(this.mItems, this.mHead, this.mTail, null);
		this.mHead = 0;
		this.mTail = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void shift() {
		final int size = this.mTail - this.mHead;
		if(size == 0) {
			this.mHead = 0;
			this.mTail = 0;
		} else {
			/* Copy items to the start of the array. */
			System.arraycopy(this.mItems, this.mHead, this.mItems, 0, size);

			/* Null out old item references, ensuring not to overwrite just copied ones. */
			final int start = Math.max(size, this.mHead);
			final int end = Math.max(start, this.mTail);
			if(start < end) {
				Arrays.fill(this.mItems, start, end, null);
			}

			this.mHead = 0;
			this.mTail = size;
		}
	}

	private void ensureShiftableRight() {
		final int currentCapacity = this.mItems.length;
		/* Check if tail reached the end. */
		if(this.mTail == currentCapacity) {
			final int size = this.mTail - this.mHead;

			/* Check if space problem can be solved by shifting. */
			if(size != currentCapacity) {
				this.shift();
			} else {
				/* Increase array capacity. */
				final int newCapacity = ((currentCapacity * 3) >> 1) + 1;
				final Object newItems[] = new Object[newCapacity];
				System.arraycopy(this.mItems, 0, newItems, 0, currentCapacity);
				this.mItems = newItems;
			}
		}
	}

	private void ensureShiftableLeft() {
		/* Check if there is room at the head. */
		if(this.mHead == 0) {
			final int size = this.mTail - this.mHead;
			final int currentCapacity = this.mItems.length;

			/* Check if space problem can be solved by shifting. */
			if(size < currentCapacity) {
				if(size == 0) {
					this.mHead = 1;
					this.mTail = 1;
				} else {
					/* Shift array items one position to the right. */
					System.arraycopy(this.mItems, this.mHead, this.mItems, this.mHead + 1, size);

					/* Null out old item reference. */
					this.mItems[this.mHead] = null;
					this.mHead++;
					this.mTail++;
				}
			} else {
				/* Increase array capacity. */
				final int newCapacity = ((currentCapacity * 3) >> 1) + 1;
				final Object newItems[] = new Object[newCapacity];
				System.arraycopy(this.mItems, 0, newItems, 1, currentCapacity);
				this.mItems = newItems;
				this.mHead++;
				this.mTail++;
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
