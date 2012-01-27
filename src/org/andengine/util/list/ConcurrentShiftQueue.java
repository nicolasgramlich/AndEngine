package org.andengine.util.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This {@link IQueue} implementation is particular useful/efficient for concurrent enter/poll operations without worrying about {@link ConcurrentModificationException} or other threading/deadlock issues.
 * Its {@link Queue} like behavior performs better than a plain {@link ArrayList}, since it automatically shift the contents of its internal Array only when really necessary.
 * Besides sparse allocations to increase the size of the internal Array, {@link ConcurrentShiftQueue} is allocation free (unlike the {@link LinkedList} family).
 * 
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:02:40 - 24.02.2012
 */
public class ConcurrentShiftQueue<T> implements IQueue<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAPACITY_INITIAL_DEFAULT = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	private Object[] mItems;
	private int mHead;
	private int mTail;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ConcurrentShiftQueue() {
		this(ConcurrentShiftQueue.CAPACITY_INITIAL_DEFAULT);
	}

	public ConcurrentShiftQueue(final int pInitialCapacity) {
		this.mItems = new Object[pInitialCapacity];
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public synchronized boolean isEmpty() {
		return this.mHead == this.mTail;
	}

	@Override
	public synchronized void enter(final T pItem) {
		this.ensureCapacity();
		this.mItems[this.mTail] = pItem;
		this.mTail++;
	}

	@Override
	public synchronized int size() {
		return this.mTail - this.mHead;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized T peek() {
		if(this.isEmpty()) {
			return null;
		} else {
			return (T)this.mItems[this.mHead];
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized T poll() {
		if(this.isEmpty()) {
			return null;
		} else {
			final T item = (T)this.mItems[this.mHead];
			this.mItems[this.mHead] = null;
			this.mHead++;
			return item;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized void shift() {
		final int size = this.mTail - this.mHead;
		if(size == 0) {
			this.mHead = 0;
			this.mTail = 0;
		} else {
			System.arraycopy(this.mItems, this.mHead, this.mItems, 0, size);
			
			/* Black out old item references, ensuring not to overwrite just copied ones. */
			final int start = Math.max(size, this.mHead);
			final int end = Math.max(start, this.mTail);
			if(start < end) {
				Arrays.fill(this.mItems, start, end, null);
			}

			this.mHead = 0;
			this.mTail = size;
		}
	}

	private void ensureCapacity() {
		final int currentCapacity = this.mItems.length;
		/* Check if tail reached the end. */
		if(this.mTail == currentCapacity) {
			final int size = this.mTail - this.mHead;

			/* Check if space problem can be solved by shifting. */
			if(size != currentCapacity) {
				this.shift();
			} else {
				/* Increase array size. */
				final int newCapacity = (currentCapacity * 3) / 2 + 1;
				this.mItems = Arrays.copyOf(this.mItems, newCapacity);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
