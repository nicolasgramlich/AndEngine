package org.anddev.andengine.util.pool;

import java.util.ArrayList;

import org.anddev.andengine.engine.handler.IUpdateHandler;

/**
 * @author Valentin Milea
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
	private final ArrayList<T> mScheduled = new ArrayList<T>();

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public PoolUpdateHandler() {
		this(0);
	}

	public PoolUpdateHandler(final int pInitialPoolSize) {
		this.mPool = new Pool<T>(pInitialPoolSize) {
			@Override
			protected T allocatePoolItem() {
				return PoolUpdateHandler.this.allocatePoolItem();
			}
		};
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract T allocatePoolItem();

	protected abstract void handlePoolItem(final T pPoolItem);

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		final ArrayList<T> scheduled = this.mScheduled;

		synchronized (scheduled) {
			final int count = scheduled.size();

			if(count > 0) {
				final Pool<T> pool = this.mPool;
				T item;

				for(int i = 0; i < count; i++) {
					item = scheduled.get(i);
					this.handlePoolItem(item);
					pool.recylePoolItem(item);
				}

				scheduled.clear();
			}
		}
	}

	@Override
	public void reset() {
		final ArrayList<T> scheduled = this.mScheduled;
		synchronized (scheduled) {
			final int count = scheduled.size();

			final Pool<T> pool = this.mPool;
			for(int i = count - 1; i >= 0; i--) {
				pool.recylePoolItem(scheduled.get(i));
			}

			scheduled.clear();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public T obtainPoolItem() {
		return this.mPool.obtainPoolItem();
	}

	public void postPoolItem(final T pPoolItem) {
		synchronized (this.mScheduled) {
			if(!this.mPool.ownsPoolItem(pPoolItem)) {
				throw new IllegalArgumentException("Runnable from another pool or already recycled!");
			}

			this.mScheduled.add(pPoolItem);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
