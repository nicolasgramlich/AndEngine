package org.andengine.util.adt.queue.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.andengine.util.adt.list.CircularList;
import org.andengine.util.adt.list.IList;

import android.util.SparseArray;
import android.util.SparseIntArray;

/**
 * The {@link PriorityBlockingAggregatorQueue} is a thread-safe queue that internally holds multiple queues each having their own priority.
 * {@link #peek()}, {@link #poll()} and {@link #take()} always return the head of the highest priority internal queue.
 * The {@link PriorityBlockingAggregatorQueue} is i.e. useful in networking situations where different {@link Thread}s (producers) put different messages of different priority and the network {@link Thread} (consumer) should always send the highest priority message first.
 *
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 21:55:23 - 08.05.2013
 */
public class PriorityBlockingAggregatorQueue<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int QUEUE_INITIAL_CAPACITY_DEFAULT = 10;

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

	public PriorityBlockingAggregatorQueue(final boolean pFair) {
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

	@Override
	public String toString() {
		final ReentrantLock lock = this.mLock;
		lock.lock();

		try {
			final StringBuilder stringBuilder = new StringBuilder();

			if (this.mQueues.size() > 0) {
				final SparseArray<IList<T>> queues = this.mQueues;
				final SparseIntArray queueCapacities = this.mQueueCapacities;

				stringBuilder.append(" [\n");

				final int queueCount = queues.size();
				for (int i = 0; i < queueCount; i++) {
					final int priority = queues.keyAt(i);
					final IList<T> queue = queues.valueAt(i);
					final int queueCapacity = queueCapacities.valueAt(i);
					final int queueSize = queue.size();

					stringBuilder.append("\tPriority: ").append(priority).append(" (Capacity: ").append(queueSize).append('/').append(queueCapacity).append("): ");
					stringBuilder.append(queue.toString());

					if (i < (queueCount - 1)) {
						stringBuilder.append(',');
					}
					stringBuilder.append('\n');
				}

				stringBuilder.append(']');
			}

			return stringBuilder.toString();
		} finally {
			lock.unlock();
		}
	}


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
		this.addQueue(pPriority, pCapacity, QUEUE_INITIAL_CAPACITY_DEFAULT);
	}

	public void addQueue(final int pPriority, final int pCapacity, final int pInitialCapacity) {
		if (pCapacity <= 0) {
			throw new IllegalArgumentException("pCapacity must be greater than 0.");
		}

		if (pInitialCapacity <= 0) {
			throw new IllegalArgumentException("pInitialCapacity must be greater than 0.");
		}

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

//	public T peek(final int pPriority) {
//		final ReentrantLock lock = this.mLock;
//		lock.lock();
//
//		try {
//			final IList<T> queue = this.mQueues.get(pPriority);
//			if (queue == null) {
//				throw new IllegalArgumentException("No queue found for pPriority: '" + pPriority + "'.");
//			}
//			final int queueCapacity = this.mQueueCapacities.get(pPriority);
//
//			if (queueCapacity == 0) {
//				return null;
//			} else {
//				return queue.get(0);
//			}
//		} finally {
//			lock.unlock();
//		}
//	}

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

//	public T poll(final int pPriority) {
//		final ReentrantLock lock = this.mLock;
//		lock.lock();
//
//		try {
//			final int queueCapacity = this.mQueueCapacities.get(pPriority, -1);
//			if (queueCapacity == 0) {
//				return null;
//			} else {
//				return this.extract(pPriority);
//			}
//		} finally {
//			lock.unlock();
//		}
//	}

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

//	public T take(final int pPriority) throws InterruptedException {
//		final ReentrantLock lock = this.mLock;
//		lock.lockInterruptibly();
//
//		try {
//			final IList<T> queue = this.mQueues.get(pPriority);
//			if (queue == null) {
//				throw new IllegalArgumentException("No queue found for pPriority: '" + pPriority + "'.");
//			}
//
//			try {
//				while (queue.size() == 0) {
//					this.mNotEmptyCondition.await();
//				}
//			} catch (final InterruptedException e) {
//				/* Propagate to non-interrupted thread. */
//				this.mNotEmptyCondition.signal();
//				throw e;
//			}
//			return this.extract(pPriority);
//		} finally {
//			lock.unlock();
//		}
//	}

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

	public void clear() {
		final ReentrantLock lock = this.mLock;
		lock.lock();

		try {
			if (this.mSize > 0) {
				final SparseArray<IList<T>> queues = this.mQueues; 
				final int queueCount = queues.size();
				for (int i = 0; i < queueCount; i++) {
					final int priority = this.mQueues.keyAt(i);

					final IList<T> queue = this.mQueues.valueAt(i);
					queue.clear();

					final Condition notFullCondition = this.mNotFullConditions.get(priority);
					notFullCondition.signal();
				}
				this.mSize = 0;
			}
		} finally {
			lock.unlock();
		}
	}

	public void clear(final int pPriority) {
		final ReentrantLock lock = this.mLock;
		lock.lock();

		try {
			final IList<T> queue = this.mQueues.get(pPriority);
			if (queue == null) {
				throw new IllegalArgumentException("No queue found for pPriority: '" + pPriority + "'.");
			}
			final int queueSize = queue.size();

			queue.clear();

			final Condition notFullCondition = this.mNotFullConditions.get(pPriority);
			notFullCondition.signal();

			this.mSize -= queueSize;
		} finally {
			lock.unlock();
		}
	}

	public void clearAndPut(final int pPriority, final T pItem) throws IllegalArgumentException, InterruptedException {
		final ReentrantLock lock = this.mLock;
		lock.lock();

		try {
			this.clear(pPriority);
			this.put(pPriority, pItem);
		} finally {
			lock.unlock();
		}
	}

	public boolean clearAndOffer(final int pPriority, final T pItem) {
		final ReentrantLock lock = this.mLock;
		lock.lock();

		try {
			this.clear(pPriority);
			return this.offer(pPriority, pItem);
		} finally {
			lock.unlock();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
