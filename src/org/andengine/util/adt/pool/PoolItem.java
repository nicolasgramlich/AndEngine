package org.andengine.util.adt.pool;

/**
 * An item intended to be used in a {@link Pool}, that contains data about the pool in which it resides.<br>
 * (c) 2010 Nicolas Gramlich <br>
 * (c) 2011 Zynga Inc.
 * 
 * @author Valentin Milea
 * @author Nicolas Gramlich
 * 
 * @since 23:02:47 - 21.08.2010
 */
public abstract class PoolItem {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	Pool<? extends PoolItem> mParent;
	boolean mRecycled = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @return The {@link Pool} this item is in
	 */
	public Pool<? extends PoolItem> getParent() {
		return this.mParent;
	}

	/**
	 * @return Whether this item is available from the {@link Pool}
	 */
	public boolean isRecycled() {
		return this.mRecycled;
	}

	/**
	 * @param pPool The {@link Pool} to be checked
	 * @return Whether this item is in the {@link Pool} to be checked
	 */
	public boolean isFromPool(final Pool<? extends PoolItem> pPool) {
		return pPool == this.mParent;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * What happens when this item is going to be recycled
	 * @see Pool#onHandleRecycleItem(PoolItem)
	 */
	protected void onRecycle() {

	}

	/**
	 * What happens when this item is going to be obtained
	 * @see Pool#onHandleObtainItem(PoolItem)
	 */
	protected void onObtain() {

	}

	/**
	 * Recycle this item
	 * @see Pool#recycle(PoolItem)
	 */
	public void recycle() {
		if(this.mParent == null) {
			throw new IllegalStateException("Item already recycled!");
		}

		this.mParent.recycle(this);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}