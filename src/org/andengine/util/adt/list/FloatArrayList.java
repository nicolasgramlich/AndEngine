package org.andengine.util.adt.list;

/**
 * TODO This class could take some kind of AllocationStrategy object.
 *
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:07:43 - 26.01.2012
 */
public class FloatArrayList implements IFloatList {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAPACITY_INITIAL_DEFAULT = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	private float[] mItems;
	private int mSize;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FloatArrayList() {
		this(FloatArrayList.CAPACITY_INITIAL_DEFAULT);
	}

	public FloatArrayList(final int pInitialCapacity) {
		this.mItems = new float[pInitialCapacity];
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
	public float get(final int pIndex) throws ArrayIndexOutOfBoundsException {
		return this.mItems[pIndex];
	}

	@Override
	public void add(final float pItem) {
		this.ensureCapacity(this.mSize + 1);

		this.mItems[this.mSize] = pItem;
		this.mSize++;
	}

	@Override
	public void add(final int pIndex, final float pItem) throws ArrayIndexOutOfBoundsException {
		this.ensureCapacity(this.mSize + 1);

		System.arraycopy(this.mItems, pIndex, this.mItems, pIndex + 1, this.mSize - pIndex);

		this.mItems[pIndex] = pItem;
		this.mSize++;
	}

	@Override
	public float remove(final int pIndex) throws ArrayIndexOutOfBoundsException {
		final float oldValue = this.mItems[pIndex];

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
	public float[] toArray() {
		final float[] array = new float[this.mSize];
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
			final float[] newItems = new float[newCapacity];
			System.arraycopy(this.mItems, 0, newItems, 0, currentCapacity);
			this.mItems = newItems;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
