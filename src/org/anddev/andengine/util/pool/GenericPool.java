package org.anddev.andengine.util.pool;

import java.util.Stack;

import org.anddev.andengine.util.Debug;

/**
 * @author Valentin Milea
 * @author Nicolas Gramlich
 * 
 * @since 22:19:55 - 31.08.2010
 * @param <T>
 */
public abstract class GenericPool<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Stack<T> mAvailableItems = new Stack<T>();
	private int mUnrecycledCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GenericPool() {

	}

	public GenericPool(final int pInitialSize) {
		final Stack<T> availableItems = this.mAvailableItems;
		for(int i = pInitialSize - 1; i >= 0; i--) {
			availableItems.push(this.onAllocatePoolItem());
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract T onAllocatePoolItem();

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized int getUnrecycledCount() {
		return this.mUnrecycledCount;
	}

	public synchronized T obtainPoolItem() {
		final T item;

		if(this.mAvailableItems.size() > 0) {
			item = this.mAvailableItems.pop();
		} else {
			Debug.i("Pool exhausted, with " + this.mUnrecycledCount + " unrecycled items. Allocating one more...");
			item = this.onAllocatePoolItem();
		}
		this.onHandleObtainItem(item);

		this.mUnrecycledCount++;
		return item;
	}

	protected void onHandleObtainItem(final T pItem) {

	}

	public synchronized void recylePoolItem(final T pItem) {
		if(pItem == null) {
			throw new IllegalArgumentException("Cannot recycle null item!");
		}

		this.onHandleRecycleItem(pItem);

		this.mAvailableItems.push(pItem);

		this.mUnrecycledCount--;

		if(this.mUnrecycledCount < 0) {
			Debug.e("More items recycled than obtained!");
		}
	}

	protected void onHandleRecycleItem(final T pItem) {

	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
