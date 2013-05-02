package org.andengine.util.adt.queue;

import org.andengine.util.adt.list.UniqueList;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:17:33 PM - 03.022012
 */
public class UniqueQueue<T extends Comparable<T>> extends UniqueList<T> implements IUniqueQueue<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public UniqueQueue(final IQueue<T> pQueue) {
		super(pQueue);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public T peek() {
		if (this.isEmpty()) {
			return null;
		} else {
			return this.get(0);
		}
	}

	@Override
	public T poll() {
		if (this.isEmpty()) {
			return null;
		} else {
			return this.remove(0);
		}
	}

	@Override
	public void enter(final T pItem) {
		this.add(pItem);
	}

	@Deprecated
	@Override
	public void enter(final int pIndex, final T pItem) throws IndexOutOfBoundsException {
		this.add(pIndex, pItem);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
