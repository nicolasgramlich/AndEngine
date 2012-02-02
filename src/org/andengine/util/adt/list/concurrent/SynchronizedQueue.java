package org.andengine.util.adt.list.concurrent;

import org.andengine.util.adt.list.IQueue;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:23:50 - 01.02.2012
 */
public class SynchronizedQueue<T> implements IQueue<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IQueue<T> mQueue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SynchronizedQueue(final IQueue<T> pQueue) {
		this.mQueue = pQueue;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public synchronized boolean isEmpty() {
		return this.mQueue.isEmpty();
	}

	@Override
	public synchronized T get(final int pIndex) {
		return this.mQueue.get(pIndex);
	}

	@Override
	public synchronized void enter(final T pItem) {
		this.mQueue.enter(pItem);
	}

	@Override
	public synchronized void enter(final int pIndex, final T pItem) {
		this.mQueue.enter(pIndex, pItem);
	}

	@Override
	public synchronized T remove(final int pIndex) {
		return this.mQueue.remove(pIndex);
	}

	@Override
	public synchronized boolean remove(final T pItem) {
		return this.mQueue.remove(pItem);
	}

	@Override
	public synchronized int size() {
		return this.mQueue.size();
	}

	@Override
	public synchronized T peek() {
		return this.mQueue.peek();
	}

	@Override
	public synchronized T poll() {
		return this.mQueue.poll();
	}

	@Override
	public synchronized void clear() {
		this.mQueue.clear();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
