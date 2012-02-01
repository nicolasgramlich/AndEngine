package org.andengine.util.list;

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

	public SynchronizedQueue(final IQueue<T> mQueue) {
		this.mQueue = mQueue;
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
	public synchronized void enter(final T pItem) {
		this.mQueue.enter(pItem);
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

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
