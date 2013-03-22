package org.andengine.util.adt.pool;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.util.adt.list.ShiftList;
import org.andengine.util.adt.queue.IQueue;
import org.andengine.util.adt.queue.concurrent.SynchronizedQueue;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Valentin Milea
 * @author Nicolas Gramlich
 *
 * @since 23:02:58 - 21.08.2010
 */
public abstract class PoolUpdateHandler<T extends PoolItem> implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Pool<T> mPool;
	private final IQueue<T> mScheduledPoolItemQueue = new SynchronizedQueue<T>(new ShiftList<T>());

	// ===========================================================
	// Constructors
	// ===========================================================

	public PoolUpdateHandler() {
		this.mPool = new Pool<T>() {
			@Override
			protected T onAllocatePoolItem() {
				return PoolUpdateHandler.this.onAllocatePoolItem();
			}
		};
	}

	public PoolUpdateHandler(final int pInitialPoolSize) {
		this.mPool = new Pool<T>(pInitialPoolSize) {
			@Override
			protected T onAllocatePoolItem() {
				return PoolUpdateHandler.this.onAllocatePoolItem();
			}
		};
	}

	public PoolUpdateHandler(final int pInitialPoolSize, final int pGrowth) {
		this.mPool = new Pool<T>(pInitialPoolSize, pGrowth) {
			@Override
			protected T onAllocatePoolItem() {
				return PoolUpdateHandler.this.onAllocatePoolItem();
			}
		};
	}

	public PoolUpdateHandler(final int pInitialPoolSize, final int pGrowth, final int pAvailableItemCountMaximum) {
		this.mPool = new Pool<T>(pInitialPoolSize, pGrowth, pAvailableItemCountMaximum) {
			@Override
			protected T onAllocatePoolItem() {
				return PoolUpdateHandler.this.onAllocatePoolItem();
			}
		};
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract T onAllocatePoolItem();

	protected abstract void onHandlePoolItem(final T pPoolItem);

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		final IQueue<T> scheduledPoolItemQueue = this.mScheduledPoolItemQueue;
		final Pool<T> pool = this.mPool;

		T item;
		while ((item = scheduledPoolItemQueue.poll()) != null) {
			this.onHandlePoolItem(item);
			pool.recyclePoolItem(item);
		}
	}

	@Override
	public void reset() {
		final IQueue<T> scheduledPoolItemQueue = this.mScheduledPoolItemQueue;
		final Pool<T> pool = this.mPool;

		T item;
		while ((item = scheduledPoolItemQueue.poll()) != null) {
			pool.recyclePoolItem(item);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public T obtainPoolItem() {
		return this.mPool.obtainPoolItem();
	}

	public void postPoolItem(final T pPoolItem) {
		if (pPoolItem == null) {
			throw new IllegalArgumentException("PoolItem already recycled!");
		} else if (!this.mPool.ownsPoolItem(pPoolItem)) {
			throw new IllegalArgumentException("PoolItem from another pool!");
		}

		this.mScheduledPoolItemQueue.enter(pPoolItem);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
