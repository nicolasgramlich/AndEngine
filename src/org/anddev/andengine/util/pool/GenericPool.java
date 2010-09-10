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
	private final int mGrowth;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GenericPool() {
		this(0);
	}

	public GenericPool(final int pInitialSize) {
		this(pInitialSize, 1);
	}

	public GenericPool(final int pInitialSize, final int pGrowth) {
		if(pGrowth < 0) {
			throw new IllegalArgumentException("pGrowth must be at least 0!");
		}

		this.mGrowth = pGrowth;

		if(pInitialSize > 0) {
			this.batchAllocate(pInitialSize);
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

	public synchronized void batchAllocate(final int pCount) {
		final Stack<T> availableItems = this.mAvailableItems;
		for(int i = pCount - 1; i >= 0; i--) {
			availableItems.push(this.onAllocatePoolItem());
		}
	}

	public synchronized int getUnrecycledCount() {
		return this.mUnrecycledCount;
	}

	public synchronized T obtainPoolItem() {
		final T item;

		if(this.mAvailableItems.size() > 0) {
			item = this.mAvailableItems.pop();
		} else {
			if(this.mGrowth == 1) {
				item = this.onAllocatePoolItem();
			} else {
				this.batchAllocate(this.mGrowth);
				item = this.mAvailableItems.pop();
			}
			Debug.i(this.getClass().getName() + "<" + item.getClass().getSimpleName() +"> was exhausted, with " + this.mUnrecycledCount + " item not yet recycled. Allocated " + this.mGrowth + " more.");
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
