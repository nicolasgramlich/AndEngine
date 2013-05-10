package org.andengine.util.adt.list;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.andengine.util.exception.MethodNotYetImplementedException;

import android.util.SparseArray;
import android.util.SparseIntArray;

/**
 * The {@link PriorityAggregatorQueue} is a thread-safe queue that internally holds multiple queues each having their own priority.
 * {@link #peek()}, {@link #poll()} and {@link #take()} always return the head of the highest priority internal queue.
 * The {@link PriorityAggregatorQueue} is i.e. useful in networking situations where different {@link Thread}s (producers) put different messages of different priority and the network {@link Thread} (consumer) should always send the highest priority message first. 
 *
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 21:55:23 - 08.05.2013
 */
public class PriorityAggregatorQueue<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ReentrantLock mLock;

	private final SparseArray<IList<T>> mQueues = new SparseArray<IList<T>>();
	private final SparseIntArray mQueueCapacities = new SparseIntArray();
	private final SparseArray<Condition> mNotFullConditions = new SparseArray<Condition>();

	private final Condition mNotEmptyCondition;

	private int mSize;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PriorityAggregatorQueue(final boolean pFair) {
		this.mLock = new ReentrantLock(pFair);
		this.mNotEmptyCondition = this.mLock.newCondition();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int size() {
		final ReentrantLock lock = this.mLock;
		lock.lock();

		try {
			return this.mSize;
		} finally {
			lock.unlock();
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private void insert(final int pPrority, final T pItem) { // TODO Causes another (potentially unnecessary) lookup for the queue
		final IList<T> queue = this.mQueues.get(pPrority);
		queue.add(pItem);
		this.mSize++;

		this.mNotEmptyCondition.signal();
	}

	private T extract() {
		final SparseArray<IList<T>> queues = this.mQueues; 
		final int queueCount = queues.size();
		for (int i = 0; i < queueCount; i++) {
			final IList<T> queue = this.mQueues.valueAt(i);
			if (queue.size() > 0) {
				final int priority = this.mQueues.keyAt(i);
				return this.extract(priority);
			}
		}
		return null;
	}

	private T extract(final int pPriority) { // TODO Causes another (potentially unnecessary) lookup for the queue and the condition
		final Condition notFullCondition = this.mNotFullConditions.get(pPriority);

		final IList<T> queue = this.mQueues.get(pPriority);
		final T item = queue.remove(0);
		this.mSize--;

		notFullCondition.signal();

		return item;
	}

	public void addQueue(final int pPriority) {
		this.addQueue(pPriority, Integer.MAX_VALUE);
	}

	public void addQueue(final int pPriority, final int pCapacity) {
		final ReentrantLock lock = this.mLock;
		lock.lock();

		try {
			if (this.mQueues.get(pPriority) == null) {
				this.mQueues.put(pPriority, new CircularList<T>());
				this.mQueueCapacities.put(pPriority, pCapacity);
				this.mNotFullConditions.put(pPriority, this.mLock.newCondition());
			} else {
				throw new IllegalArgumentException("Queue with priority: '" + pPriority + "' already exists.");
			}
		} finally {
			lock.unlock();
		}
	}

	public void addQueue(final int pPriority, final int pCapacity, final int pInitialCapacity) {
		final ReentrantLock lock = this.mLock;
		lock.lock();

		try {
			this.mQueues.put(pPriority, new CircularList<T>(pInitialCapacity));
			this.mQueueCapacities.put(pPriority, pCapacity);
			this.mNotFullConditions.put(pPriority, this.mLock.newCondition());
		} finally {
			lock.unlock();
		}
	}

	public T peek() {
		final ReentrantLock lock = this.mLock;
		lock.lock();

		try {
			if (this.mSize == 0) {
				return null;
			} else {
				final SparseArray<IList<T>> queues = this.mQueues; 
				final int queueCount = queues.size();
				for (int i = 0; i < queueCount; i++) {
					final IList<T> queue = this.mQueues.valueAt(i);
					if (queue.size() > 0) {
						return queue.get(0);
					}
				}
				return null;
			}
		} finally {
			lock.unlock();
		}
	}

	public T poll() {
		final ReentrantLock lock = this.mLock;
		lock.lock();

		try {
			if (this.mSize == 0) {
				return null;
			} else {
				return extract();
			}
		} finally {
			lock.unlock();
		}
	}

	public T take() throws InterruptedException {
		final ReentrantLock lock = this.mLock;
		lock.lockInterruptibly();

		try {
			try {
				while (this.mSize == 0) {
					this.mNotEmptyCondition.await();
				}
			} catch (final InterruptedException e) {
				/* Propagate to non-interrupted thread. */
				this.mNotEmptyCondition.signal();
				throw e;
			}
			return this.extract();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Inserts the specified element at the tail of this queue with the given priority, waiting for space to become available if the queue is full.
	 * 
	 * @throws IllegalArgumentException if pItem is <code>null</code>
	 * @throws InterruptedException 
	 */
	public void put(final int pPriority, final T pItem) throws IllegalArgumentException, InterruptedException {
		if (pItem == null) { 
			throw new IllegalArgumentException("pItem must not be null.");
		}

		final ReentrantLock lock = this.mLock;
		final Condition notFullCondition = this.mNotFullConditions.get(pPriority);
		lock.lockInterruptibly();

		try {
			final IList<T> queue = this.mQueues.get(pPriority);
			if (queue == null) {
				throw new IllegalArgumentException("No queue found for pPriority: '" + pPriority + "'.");
			}

			final int queueCapacity = this.mQueueCapacities.get(pPriority);
			try {
				while (queue.size() == queueCapacity) {
					notFullCondition.await();
				}
			} catch (final InterruptedException e) {
				/* Propagate to non-interrupted thread. */
				notFullCondition.signal();
				throw e;
			}
			insert(pPriority, pItem);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Inserts the specified element at the tail of this queue with the given priority, if it is possible without exceeding the capacity of the queue with the given priority.
	 * 
	 * @throws IllegalArgumentException if pItem is <code>null</code>
	 * @return <code>true</code> if the 
	 */
	public boolean offer(final int pPriority, final T pItem) throws IllegalArgumentException {
		if (pItem == null) { 
			throw new IllegalArgumentException("pItem must not be null.");
		}

		final ReentrantLock lock = this.mLock;
		lock.lock();

		try {
			final IList<T> queue = this.mQueues.get(pPriority);
			if (queue == null) {
				throw new IllegalArgumentException("No queue found for pPriority: '" + pPriority + "'.");
			}

			final int queueCapacity = this.mQueueCapacities.get(pPriority);
			if (queue.size() == queueCapacity) {
				return false;
			} else {
				insert(pPriority, pItem);
				return true;
			}
		} finally {
			lock.unlock();
		}
	}

	public void set(final int pPriority, final T pItem) { // TODO Same as put but replaces the first
		throw new MethodNotYetImplementedException();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
