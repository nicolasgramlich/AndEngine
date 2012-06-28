package org.andengine.util.adt.pool;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Valentin Milea
 * @author Nicolas Gramlich
 * 
 * @since 23:00:21 - 21.08.2010
 */
public abstract class Pool<T extends PoolItem> extends GenericPool<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public Pool() {
		super();
	}

	public Pool(final int pInitialSize) {
		super(pInitialSize);
	}

	public Pool(final int pInitialSize, final int pGrowth) {
		super(pInitialSize, pGrowth);
	}

	public Pool(final int pInitialSize, final int pGrowth, final int pAvailableItemCountMaximum) {
		super(pInitialSize, pGrowth, pAvailableItemCountMaximum);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected T onHandleAllocatePoolItem() {
		final T poolItem = super.onHandleAllocatePoolItem();
		poolItem.mParent = this;
		return poolItem;
	}

	@Override
	protected void onHandleObtainItem(final T pPoolItem) {
		pPoolItem.mRecycled = false;
		pPoolItem.onObtain();
	}

	@Override
	protected void onHandleRecycleItem(final T pPoolItem) {
		pPoolItem.onRecycle();
		pPoolItem.mRecycled = true;
	}

	@Override
	public synchronized void recyclePoolItem(final T pPoolItem) {
		if(pPoolItem.mParent == null) {
			throw new IllegalArgumentException("PoolItem not assigned to a pool!");
		} else if(!pPoolItem.isFromPool(this)) {
			throw new IllegalArgumentException("PoolItem from another pool!");
		} else if(pPoolItem.isRecycled()) {
			throw new IllegalArgumentException("PoolItem already recycled!");
		}

		super.recyclePoolItem(pPoolItem);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized boolean ownsPoolItem(final T pPoolItem) {
		return pPoolItem.mParent == this;
	}

	@SuppressWarnings("unchecked")
	void recycle(final PoolItem pPoolItem) {
		this.recyclePoolItem((T) pPoolItem);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
