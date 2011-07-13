package org.anddev.andengine.util.pool;

import java.util.ArrayList;

import org.anddev.andengine.engine.handler.IUpdateHandler;

/**
 * @author Valentin Milea
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * 
 * @since 23:02:58 - 21.08.2010
 * @param <T>
 */
public abstract class PoolUpdateHandler<T extends PoolItem> implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Pool<T> mPool;
	private final ArrayList<T> mScheduledPoolItems = new ArrayList<T>();

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
		final ArrayList<T> scheduledPoolItems = this.mScheduledPoolItems;

		synchronized (scheduledPoolItems) {
			final int count = scheduledPoolItems.size();

			if(count > 0) {
				final Pool<T> pool = this.mPool;
				T item;

				for(int i = 0; i < count; i++) {
					item = scheduledPoolItems.get(i);
					this.onHandlePoolItem(item);
					pool.recyclePoolItem(item);
				}

				scheduledPoolItems.clear();
			}
		}
	}

	@Override
	public void reset() {
		final ArrayList<T> scheduledPoolItems = this.mScheduledPoolItems;
		synchronized (scheduledPoolItems) {
			final int count = scheduledPoolItems.size();

			final Pool<T> pool = this.mPool;
			for(int i = count - 1; i >= 0; i--) {
				pool.recyclePoolItem(scheduledPoolItems.get(i));
			}

			scheduledPoolItems.clear();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public T obtainPoolItem() {
		return this.mPool.obtainPoolItem();
	}

	public void postPoolItem(final T pPoolItem) {
		synchronized (this.mScheduledPoolItems) {
			if(pPoolItem == null) {
				throw new IllegalArgumentException("PoolItem already recycled!");
			} else if(!this.mPool.ownsPoolItem(pPoolItem)) {
				throw new IllegalArgumentException("PoolItem from another pool!");
			}

			this.mScheduledPoolItems.add(pPoolItem);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
