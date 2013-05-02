package org.andengine.util.adt.list;

/**
 * TODO This class could take some kind of AllocationStrategy object.
 *
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 19:35:59 - 03.05.2012
 */
public class LongArrayList implements ILongList {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAPACITY_INITIAL_DEFAULT = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	private long[] mItems;
	private int mSize;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LongArrayList() {
		this(LongArrayList.CAPACITY_INITIAL_DEFAULT);
	}

	public LongArrayList(final int pInitialCapacity) {
		this.mItems = new long[pInitialCapacity];
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean isEmpty() {
		return this.mSize == 0;
	}

	@Override
	public long get(final int pIndex) throws ArrayIndexOutOfBoundsException {
		return this.mItems[pIndex];
	}

	@Override
	public void add(final long pItem) {
		this.ensureCapacity(this.mSize + 1);

		this.mItems[this.mSize] = pItem;
		this.mSize++;
	}

	@Override
	public void add(final int pIndex, final long pItem) throws ArrayIndexOutOfBoundsException {
		this.ensureCapacity(this.mSize + 1);

		System.arraycopy(this.mItems, pIndex, this.mItems, pIndex + 1, this.mSize - pIndex);

		this.mItems[pIndex] = pItem;
		this.mSize++;
	}

	@Override
	public long remove(final int pIndex) throws ArrayIndexOutOfBoundsException {
		final long oldValue = this.mItems[pIndex];

		final int numMoved = this.mSize - pIndex - 1;
		if (numMoved > 0) {
			System.arraycopy(this.mItems, pIndex + 1, this.mItems, pIndex, numMoved);
		}

		this.mSize--;

		return oldValue;
	}

	@Override
	public int size() {
		return this.mSize;
	}

	@Override
	public void clear() {
		this.mSize = 0;
	}

	@Override
	public long[] toArray() {
		final long[] array = new long[this.mSize];
		System.arraycopy(this.mItems, 0, array, 0, this.mSize);
		return array;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void ensureCapacity(final int pCapacity) {
		final int currentCapacity = this.mItems.length;
		if (currentCapacity < pCapacity) {
			/* Increase array size. */
			final int newCapacity = ((currentCapacity * 3) >> 1) + 1;
			final long[] newItems = new long[newCapacity];
			System.arraycopy(this.mItems, 0, newItems, 0, currentCapacity);
			this.mItems = newItems;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
