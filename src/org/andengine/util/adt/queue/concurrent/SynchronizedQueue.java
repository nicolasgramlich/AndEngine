package org.andengine.util.adt.queue.concurrent;

import org.andengine.util.adt.list.ListUtils;
import org.andengine.util.adt.queue.IQueue;

/**
 * (c) 2012 Zynga Inc.
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
	public synchronized T get(final int pIndex) throws IndexOutOfBoundsException {
		return this.mQueue.get(pIndex);
	}

	@Override
	public synchronized void set(final int pIndex, final T pItem) throws IndexOutOfBoundsException {
		this.mQueue.set(pIndex, pItem);
	}

	@Override
	public synchronized int indexOf(final T pItem) {
		return this.mQueue.indexOf(pItem);
	}

	@Override
	public synchronized void add(final T pItem) {
		this.mQueue.add(pItem);
	}

	@Override
	public synchronized void add(final int pIndex, final T pItem) throws IndexOutOfBoundsException {
		this.mQueue.add(pIndex, pItem);
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
	public synchronized void enter(final T pItem) {
		this.mQueue.enter(pItem);
	}

	@Override
	public synchronized void enter(final int pIndex, final T pItem) throws IndexOutOfBoundsException {
		this.mQueue.enter(pIndex, pItem);
	}

	@Override
	public synchronized T removeFirst() {
		return this.mQueue.removeFirst();
	}

	@Override
	public synchronized T removeLast() {
		return this.mQueue.removeLast();
	}

	@Override
	public synchronized boolean remove(final T pItem) {
		return this.mQueue.remove(pItem);
	}

	@Override
	public synchronized T remove(final int pIndex) throws IndexOutOfBoundsException {
		return this.mQueue.remove(pIndex);
	}

	@Override
	public synchronized int size() {
		return this.mQueue.size();
	}

	@Override
	public synchronized void clear() {
		this.mQueue.clear();
	}

	@Override
	public synchronized String toString() {
		return ListUtils.toString(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
