package org.anddev.andengine.util.pool;


/**
 * @author Valentin Milea
 * @author Nicolas Gramlich
 * 
 * @since 23:00:21 - 21.08.2010
 * @param <T>
 */
public abstract class Pool<T extends PoolItem> extends GenericPool<T>{
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

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onHandleObtainItem(final T pPoolItem) {
		pPoolItem.mParent = this;
	}

	@Override
	protected void onHandleRecycleItem(final T pPoolItem) {
		pPoolItem.onRecycle();

		pPoolItem.mParent = null;
	}

	@Override
	public synchronized void recylePoolItem(final T pPoolItem) {
		if(pPoolItem.mParent == null) {
			throw new IllegalArgumentException("PoolItem already recycled. Maybe it's from another pool?");
		} else if(!pPoolItem.isFromPool(this)) {
			throw new IllegalArgumentException("PoolItem from another pool!");
		}
		super.recylePoolItem(pPoolItem);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized boolean ownsPoolItem(final T pPoolItem) {
		return pPoolItem.mParent == this;
	}

	@SuppressWarnings("unchecked")
	void recycle(final PoolItem pPoolItem) {
		this.recylePoolItem((T) pPoolItem);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
