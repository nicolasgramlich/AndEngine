package org.andengine.util.adt.pool;

import java.util.ArrayList;
import java.util.Collections;

import org.andengine.BuildConfig;
import org.andengine.entity.Entity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.ColorPool;
import org.andengine.util.debug.Debug;

/**
 * A pool represents a list of objects that can be reused. For example, if you
 * have a lot of bullets in your game, you should use an object pool to reuse
 * the bullet objects once they are off screen so you don't have to instantiate
 * a whole new bullet object, which is expensive.<br>
 * 
 * <br>
 * (c) 2010 Nicolas Gramlich<br>
 * (c) 2011 Zynga Inc.<br>
 * 
 * @author Valentin Milea
 * @author Nicolas Gramlich
 * 
 * @since 22:19:55 - 31.08.2010
 * @see ColorPool
 * @see Pool
 */
public abstract class GenericPool<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<T> mAvailableItems;
	private final int mGrowth;
	private final int mAvailableItemCountMaximum;

	private int mUnrecycledItemCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Creates an empty pool, which starts out growing by 1 object every time it
	 * runs out of objects and has the maximum number of available slots in the
	 * pool (2<sup>31</sup>-1).
	 */
	public GenericPool() {
		this(0);
	}

	/**
	 * Creates a pool with which grows by 1 object every time it runs out of
	 * objects and has the maximum number of available slots in the pool
	 * (2<sup>31</sup>-1).
	 * 
	 * @param pInitialSize
	 *            How many objects should be available from the onset
	 */
	public GenericPool(final int pInitialSize) {
		this(pInitialSize, 1);
	}

	/**
	 * Creates a pool with the maximum number of available slots in the pool,
	 * 2<sup>31</sup>-1.
	 * 
	 * @param pInitialSize
	 *            How many objects should be available from the onset
	 * @param pGrowth
	 *            Specifies with how much objects the pool should grow, whenever
	 *            it runs out of available objects. Must be > 0.
	 */
	public GenericPool(final int pInitialSize, final int pGrowth) {
		this(pInitialSize, pGrowth, Integer.MAX_VALUE);
	}

	/**
	 * Creates a pool
	 * 
	 * @param pInitialSize
	 *            How many objects should be available from the onset
	 * @param pGrowth
	 *            Specifies with how much objects the pool should grow, whenever
	 *            it runs out of available objects. Must be > 0.
	 * @param pAvailableItemsMaximum
	 *            Specifies how many objects the pool can hold maximally. Must
	 *            be > 0.
	 */
	public GenericPool(final int pInitialSize, final int pGrowth,
			final int pAvailableItemsMaximum) {
		if (pGrowth <= 0) {
			throw new IllegalArgumentException(
					"pGrowth must be greater than 0!");
		}
		if (pAvailableItemsMaximum < 0) {
			throw new IllegalArgumentException(
					"pAvailableItemsMaximum must be at least 0!");
		}

		this.mGrowth = pGrowth;
		this.mAvailableItemCountMaximum = pAvailableItemsMaximum;
		this.mAvailableItems = new ArrayList<T>(pInitialSize);

		if (pInitialSize > 0) {
			this.batchAllocatePoolItems(pInitialSize);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @return How many items are in this pool that are currently 'in use' and
	 *         not ready for recycling.
	 */
	public synchronized int getUnrecycledItemCount() {
		return this.mUnrecycledItemCount;
	}

	/**
	 * @return How many items are in this pool that are available for recycling
	 */
	public synchronized int getAvailableItemCount() {
		return this.mAvailableItems.size();
	}

	/**
	 * 
	 * @return How many objects the pool can hold maximally
	 */
	public int getAvailableItemCountMaximum() {
		return this.mAvailableItemCountMaximum;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * Called when an object is required but it can't be recycled from the pool
	 * 
	 * @return A new instantiation of the object
	 * @see #onHandleRecycleItem(Object)
	 * @see #onHandleObtainItem(Object)
	 */
	protected abstract T onAllocatePoolItem();

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Called when an item is sent to the pool for recycling. Typically you'll
	 * want to call methods like {@link Entity#setIgnoreUpdate(boolean)
	 * T.setIgnoreUpdate(true)} and {@link Entity#setVisible(boolean)
	 * T.setVisible(false)}
	 * 
	 * @param pItem
	 *            The item that is about to get recycled
	 * @see #onHandleObtainItem(Object)
	 * @see #onAllocatePoolItem()
	 */
	protected void onHandleRecycleItem(final T pItem) {

	}

	/**
	 * @see #onAllocatePoolItem()
	 * @return A fresh instance of an object
	 */
	protected T onHandleAllocatePoolItem() {
		return this.onAllocatePoolItem();
	}

	/**
	 * Called just before an object is returned to the caller, this is where you
	 * write your initialization code, i.e.: set location, rotation, etc.
	 * 
	 * @param pItem
	 *            The item that was just obtained from the pool
	 * @see #onAllocatePoolItem()
	 * @see #onHandleRecycleItem(Object)
	 */
	protected void onHandleObtainItem(final T pItem) {

	}

	/**
	 * Obtains more than one item from the pool and adds them to the list of
	 * available items
	 * 
	 * @param pCount
	 *            The number of items to be obtained
	 * @see #onHandleAllocatePoolItem()
	 */
	public synchronized void batchAllocatePoolItems(final int pCount) {
		final ArrayList<T> availableItems = this.mAvailableItems;

		int allocationCount = this.mAvailableItemCountMaximum
				- availableItems.size();
		if (pCount < allocationCount) {
			allocationCount = pCount;
		}

		for (int i = allocationCount - 1; i >= 0; i--) {
			availableItems.add(this.onHandleAllocatePoolItem());
		}
	}

	/**
	 * Called when you wish to obtain an item
	 * 
	 * @return A recycled or new item, depending on if there were any recycled
	 *         items available from the list
	 * @see #onHandleObtainItem(Object)
	 */
	public synchronized T obtainPoolItem() {
		final T item;

		if (this.mAvailableItems.size() > 0) {
			item = this.mAvailableItems.remove(this.mAvailableItems.size() - 1);
		} else {
			if (this.mGrowth == 1 || this.mAvailableItemCountMaximum == 0) {
				item = this.onHandleAllocatePoolItem();
			} else {
				this.batchAllocatePoolItems(this.mGrowth);
				item = this.mAvailableItems
						.remove(this.mAvailableItems.size() - 1);
			}
			if (BuildConfig.DEBUG) {
				Debug.v(this.getClass().getName() + "<"
						+ item.getClass().getSimpleName()
						+ "> was exhausted, with " + this.mUnrecycledItemCount
						+ " item not yet recycled. Allocated " + this.mGrowth
						+ " more.");
			}
		}
		this.onHandleObtainItem(item);

		this.mUnrecycledItemCount++;
		return item;
	}

	/**
	 * Called when you wish to recycle an item
	 * 
	 * @param pItem
	 *            The item that is about to be recycled
	 * @see #onHandleRecycleItem(Object)
	 */
	public synchronized void recyclePoolItem(final T pItem) {
		if (pItem == null) {
			throw new IllegalArgumentException("Cannot recycle null item!");
		}

		this.onHandleRecycleItem(pItem);

		if (this.mAvailableItems.size() < this.mAvailableItemCountMaximum) {
			this.mAvailableItems.add(pItem);
		}

		this.mUnrecycledItemCount--;

		if (this.mUnrecycledItemCount < 0) {
			Debug.e("More items recycled than obtained!");
		}
	}

	/**
	 * Randomizes the order of the items in the list of available items
	 */
	public synchronized void shufflePoolItems() {
		Collections.shuffle(this.mAvailableItems);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
