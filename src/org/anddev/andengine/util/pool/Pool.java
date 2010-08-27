package org.anddev.andengine.util.pool;

import java.util.Stack;

import org.anddev.andengine.util.Debug;

/**
 * @author Valentin Milea
 * @author Nicolas Gramlich
 * 
 * @since 23:00:21 - 21.08.2010
 * @param <T>
 */
public abstract class Pool<T extends PoolItem> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Stack<T> mAvailablePoolItems = new Stack<T>();
	private int mUnrecycledCount;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public Pool() {
		
	}

	public Pool(final int pInitialSize) {
		final Stack<T> availablePoolItems = this.mAvailablePoolItems;
		for(int i = pInitialSize - 1; i >= 0; i--) {
			availablePoolItems.push(this.allocatePoolItem());
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract T allocatePoolItem();

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized int getUnrecycledCount() {
		return this.mUnrecycledCount;
	}

	public synchronized T obtainPoolItem() {
		T poolItem;

		if(this.mAvailablePoolItems.size() > 0) {
			poolItem = this.mAvailablePoolItems.pop();
		} else {
			Debug.d("Pool exhausted, with " + this.mUnrecycledCount + " unrecycled items. Allocating one more...");
			poolItem = this.allocatePoolItem();
		}

		// assert: item.mParent == null
		poolItem.mParent = this;
		this.mUnrecycledCount++;
		return poolItem;
	}

	public synchronized void recylePoolItem(final T pPoolItem) {
		if(pPoolItem.mParent != this) {
			if(pPoolItem.mParent == null) {
				throw new IllegalArgumentException("Item already recycled. Maybe it's from another pool?");
			} else {
				throw new IllegalArgumentException("Item from another pool!");
			}
		}

		pPoolItem.mParent = null;
		this.mAvailablePoolItems.push(pPoolItem);

		// assert: mUnrecycledCount >= 0
		this.mUnrecycledCount--;
	}

	public synchronized boolean ownsPoolItem(final T pPoolItem) {
		return pPoolItem.mParent == this;
	}

	@SuppressWarnings("unchecked")
	void recyclePoolItem(final PoolItem pPoolItem) {// TODO Why not "T" ?
		this.recylePoolItem((T) pPoolItem);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
