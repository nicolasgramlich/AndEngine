package org.anddev.andengine.util.pool;

import java.util.Collections;
import java.util.Stack;

import org.anddev.andengine.util.Debug;

/**
 * @author Valentin Milea
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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
			this.batchAllocatePoolItems(pInitialSize);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public synchronized int getUnrecycledCount() {
		return this.mUnrecycledCount;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract T onAllocatePoolItem();

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pItem every item passes this method just before it gets recycled.
	 */
	protected void onHandleRecycleItem(final T pItem) {

	}

	protected T onHandleAllocatePoolItem() {
		return this.onAllocatePoolItem();
	}

	/**
	 * @param pItem every item that was just obtained from the pool, passes this method. 
	 */
	protected void onHandleObtainItem(final T pItem) {

	}

	public synchronized void batchAllocatePoolItems(final int pCount) {
		final Stack<T> availableItems = this.mAvailableItems;
		for(int i = pCount - 1; i >= 0; i--) {
			availableItems.push(this.onHandleAllocatePoolItem());
		}
	}

	public synchronized T obtainPoolItem() {
		final T item;

		if(this.mAvailableItems.size() > 0) {
			item = this.mAvailableItems.pop();
		} else {
			if(this.mGrowth == 1) {
				item = this.onHandleAllocatePoolItem();
			} else {
				this.batchAllocatePoolItems(this.mGrowth);
				item = this.mAvailableItems.pop();
			}
			Debug.i(this.getClass().getName() + "<" + item.getClass().getSimpleName() +"> was exhausted, with " + this.mUnrecycledCount + " item not yet recycled. Allocated " + this.mGrowth + " more.");
		}
		this.onHandleObtainItem(item);

		this.mUnrecycledCount++;
		return item;
	}

	public synchronized void recyclePoolItem(final T pItem) {
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

	public synchronized void shufflePoolItems() {
		Collections.shuffle(this.mAvailableItems);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
