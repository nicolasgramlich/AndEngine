package org.andengine.util.list;

import java.util.Arrays;

/**
 * This implementation is particular useful/efficient for enter/poll operations.
 * Its {@link java.util.Queue} like behavior performs better than a plain {@link java.util.ArrayList}, since it automatically shift the contents of its internal Array only when really necessary.
 * Besides sparse allocations to increase the size of the internal Array, {@link ShiftQueue} is allocation free (unlike the {@link java.util.LinkedList} family).
 * 
 * Supports <code>null</code> items.
 *
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @author Greg Haynes
 * @since 15:02:40 - 24.02.2012
 */
public class ShiftQueue<T> implements IQueue<T>, IList<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAPACITY_INITIAL_DEFAULT = 1;

	// ===========================================================
	// Fields
	// ===========================================================

	protected Object[] mItems;
	protected int mHead;
	protected int mTail;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ShiftQueue() {
		this(ShiftQueue.CAPACITY_INITIAL_DEFAULT);
	}

	public ShiftQueue(final int pInitialCapacity) {
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

	@SuppressWarnings("unchecked")
	@Override
	public T peek() {
		if(this.isEmpty()) {
			return null;
		} else {
			return (T) this.mItems[this.mHead];
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T poll() {
		if(this.isEmpty()) {
			return null;
		} else {
			final T item = (T) this.mItems[this.mHead];
			this.mItems[this.mHead] = null;
			this.mHead++;
			return item;
		}
	}

	@Override
	public void enter(final T pItem) {
		this.ensureCapacityRight();
		this.mItems[this.mTail] = pItem;
		this.mTail++;
	}

	@Override
	public void add(final T pItem) {
		this.enter(pItem);
	}

	@Override
	public void add(final int pIndex, final T pItem) throws ArrayIndexOutOfBoundsException {
		this.addInternal(pIndex + this.mHead, pItem);
	}

	protected void addInternal(final int pIndex, final T pItem) throws ArrayIndexOutOfBoundsException {
		final int size = this.mTail - this.mHead;
		if(pIndex < (size >> 1)) {
			// shift left
			this.ensureCapacityLeft();
			System.arraycopy(this.mItems, this.mHead - 1, this.mItems, this.mHead, pIndex);
			this.mItems[(this.mHead + pIndex) - 1] = pItem;
			this.mHead--;
		} else {
			// shift right
			this.ensureCapacityRight();
			final int absoluteIndex = this.mHead + pIndex;
			System.arraycopy(this.mItems, absoluteIndex, this.mItems, absoluteIndex + 1, size - pIndex);
			this.mItems[absoluteIndex] = pItem;
			this.mTail++;
		}
	}

	@Override
	public boolean remove(final T pItem) {
		if (pItem == null) {
			for (int index = this.mHead; index < this.mTail; index++) {
				if (this.mItems[index] == null) {
					this.remove(index);
					return true;
				}
			}
		} else {
			for (int index = this.mHead; index < this.mTail; index++) {
				if (pItem.equals(this.mItems[index])) {
					this.remove(index);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean remove(final int pIndex) throws ArrayIndexOutOfBoundsException {
		return this.removeInternal(pIndex + this.mHead);
	}

	protected boolean removeInternal(final int pIndex) throws ArrayIndexOutOfBoundsException {
		final int size = this.mTail - this.mHead;

		/* Determine which side to shift to makes more sense. */
		final int center = (this.mHead + this.mTail) >> 1;
		if(pIndex < center) {
			/* Shift right. */
			if(pIndex > this.mHead) {
				System.arraycopy(this.mItems, this.mHead, this.mItems, this.mHead + 1, pIndex);
			}
			this.mItems[this.mHead] = null;
			this.mHead++;
		} else {
			/* Shift left. */
			final int shiftAmount = size - pIndex - 1;
			if(shiftAmount > 0) {
				System.arraycopy(this.mItems, pIndex + 1, this.mItems, pIndex, shiftAmount);
			}
			this.mTail--;
			this.mItems[this.mTail] = null;
		}
		return true;
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

	private void ensureCapacityLeft() {
		/* Check if there is room at the head. */
		if(this.mHead == 0) {
			final int size = this.mTail - this.mHead;
			final int currentCapacity = this.mItems.length;

			/* Check if space problem can be solved by shifting. */
			if(size != currentCapacity) {
				this.shiftRightByOne();
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

	private void ensureCapacityRight() {
		final int currentCapacity = this.mItems.length;
		/* Check if tail reached the end. */
		if(this.mTail == currentCapacity) {
			final int size = this.mTail - this.mHead;

			/* Check if space problem can be solved by shifting. */
			if(size != currentCapacity) {
				this.shiftLeft();
			} else {
				/* Increase array capacity. */
				final int newCapacity = ((currentCapacity * 3) >> 1) + 1;
				final Object newItems[] = new Object[newCapacity];
				System.arraycopy(this.mItems, 0, newItems, 0, currentCapacity);
				this.mItems = newItems;
			}
		}
	}

	private void shiftLeft() {
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

	private void shiftRightByOne() {
		final int size = this.mTail - this.mHead;
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
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
