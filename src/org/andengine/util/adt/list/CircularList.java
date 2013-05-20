package org.andengine.util.adt.list;

import java.util.Arrays;

/**
 * TODO This class could take some kind of AllocationStrategy object.
 *
 * This implementation is particular useful/efficient for enter/poll operations.
 * Its {@link java.util.Queue} like behavior performs better than a plain {@link java.util.ArrayList}, since it automatically shift the contents of its internal Array only when really necessary.
 * Besides sparse allocations to increase the size of the internal Array, {@link CircularList} is allocation free (unlike the {@link java.util.LinkedList} family).
 *
 * (c) 2012 Zynga Inc.
 *
 * @author Greg Haynes
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:02:40 - 24.02.2012
 */
public class CircularList<T> implements IList<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAPACITY_INITIAL_DEFAULT = 1;
	private static final int INDEX_INVALID = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	private Object[] mItems;
	private int mHead;
	private int mSize;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CircularList() {
		this(CircularList.CAPACITY_INITIAL_DEFAULT);
	}

	public CircularList(final int pInitialCapacity) {
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
		return this.mSize == 0;
	}

	@Override
	public void add(final T pItem) {
		this.ensureCapacity();
		this.mItems[this.encodeToInternalIndex(this.mSize)] = pItem;
		this.mSize++;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T get(final int pIndex) throws ArrayIndexOutOfBoundsException {
		return (T) this.mItems[this.encodeToInternalIndex(pIndex)];
	}

	@Override
	public void set(final int pIndex, final T pItem) throws IndexOutOfBoundsException {
		this.mItems[this.encodeToInternalIndex(pIndex)] = pItem;
	}

	@Override
	public int indexOf(final T pItem) {
		final int size = this.size();
		if (pItem == null) {
			for (int i = 0; i < size; i++) {
				if (this.get(i) == null) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (pItem.equals(this.get(i))) {
					return i;
				}
			}
		}
		return CircularList.INDEX_INVALID;
	}

	@Override
	public void add(final int pIndex, final T pItem) {
		int internalIndex = this.encodeToInternalIndex(pIndex);

		this.ensureCapacity();

		final int internalTail = this.encodeToInternalIndex(this.mSize);
		if (internalIndex == internalTail) {
			// nothing to shift, tail is free
		} else if (internalIndex == this.mHead) {
			this.mHead--;
			if (this.mHead == -1) {
				this.mHead = this.mItems.length - 1;
			}
			internalIndex--;
			if (internalIndex == -1) {
				internalIndex = this.mItems.length - 1;
			}
		} else if ((internalIndex < this.mHead) || (this.mHead == 0)) {
			System.arraycopy(this.mItems, internalIndex, this.mItems, internalIndex + 1, internalTail - internalIndex);
		} else if (internalIndex > internalTail) {
			System.arraycopy(this.mItems, this.mHead, this.mItems, this.mHead - 1, pIndex);
			this.mHead--;
			if (this.mHead == -1) {
				this.mHead = this.mItems.length - 1;
			}
			internalIndex--;
			if (internalIndex == -1) {
				internalIndex = this.mItems.length - 1;
			}
		} else if (pIndex < (this.mSize >> 1)) {
			System.arraycopy(this.mItems, this.mHead, this.mItems, this.mHead - 1, pIndex);
			this.mHead--;
			if (this.mHead == -1) {
				this.mHead = this.mItems.length - 1;
			}
			internalIndex--;
			if (internalIndex == -1) {
				internalIndex = this.mItems.length - 1;
			}
		} else {
			System.arraycopy(this.mItems, internalIndex, this.mItems, internalIndex + 1, internalTail - internalIndex);
		}
		this.mItems[internalIndex] = pItem;
		this.mSize++;
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
		if (index >= 0) {
			this.remove(index);
			return true;
		} else {
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public T remove(final int pIndex) {
		final int internalIndex = this.encodeToInternalIndex(pIndex);
		final T removed = (T) this.mItems[internalIndex];

		final int internalTail = this.encodeToInternalIndex(this.mSize - 1);

		if (internalIndex == internalTail) {
			this.mItems[internalTail] = null;
		} else if (internalIndex == this.mHead) {
			this.mItems[this.mHead] = null;
			this.mHead++;
			if (this.mHead == this.mItems.length) {
				this.mHead = 0;
			}
		} else if (internalIndex < this.mHead) {
			System.arraycopy(this.mItems, internalIndex + 1, this.mItems, internalIndex, internalTail - internalIndex);
			this.mItems[internalTail] = null;
		} else if (internalIndex > internalTail) {
			System.arraycopy(this.mItems, this.mHead, this.mItems, this.mHead + 1, pIndex);
			this.mItems[this.mHead] = null;
			this.mHead++;
			if (this.mHead == this.mItems.length) {
				this.mHead = 0;
			}
		} else if (pIndex < (this.mSize >> 1)) {
			System.arraycopy(this.mItems, this.mHead, this.mItems, this.mHead + 1, pIndex);
			this.mItems[this.mHead] = null;
			this.mHead++;
			if (this.mHead == this.mItems.length) {
				this.mHead = 0;
			}
		} else {
			System.arraycopy(this.mItems, internalIndex + 1, this.mItems, internalIndex, internalTail - internalIndex);
			this.mItems[internalTail] = null;
		}
		this.mSize--;

		return removed;
	}

	@Override
	public int size() {
		return this.mSize;
	}

	@Override
	public void clear() {
		final int tail = this.mHead + this.mSize;
		final int capacity = this.mItems.length;
		/* Check if items can be blacked out in one or two calls. */
		if (tail <= capacity) {
			Arrays.fill(this.mItems, this.mHead, tail, null);
		} else {
			final int headToCapacity = capacity - this.mHead;
			/* Black out items from head to the end of the array. */
			Arrays.fill(this.mItems, this.mHead, capacity, null);
			/* Black out items from the beginning of the array to the tail. */
			Arrays.fill(this.mItems, 0, this.mSize - headToCapacity, null);
		}
		this.mHead = 0;
		this.mSize = 0;
	}

	@Override
	public String toString() {
		return ListUtils.toString(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void ensureCapacity() {
		final int currentCapacity = this.mItems.length;
		if (this.mSize == currentCapacity) {
			final int newCapacity = ((currentCapacity * 3) >> 1) + 1;
			final Object newItems[] = new Object[newCapacity];

			System.arraycopy(this.mItems, this.mHead, newItems, 0, this.mSize - this.mHead);
			System.arraycopy(this.mItems, 0, newItems, this.mSize - this.mHead, this.mHead);

			this.mItems = newItems;
			this.mHead = 0;
		}
	}

	private int encodeToInternalIndex(final int pIndex) {
		int internalIndex = this.mHead + pIndex;
		if (internalIndex >= this.mItems.length) {
			internalIndex -= this.mItems.length;
		}
		return internalIndex;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
