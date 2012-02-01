package org.andengine.util.list;


/**
 * (c) Zynga 2012
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
	public void add(final float pItem) {
		this.ensureCapacity(this.mSize + 1);

		this.mItems[this.mSize] = pItem;
		this.mSize++;
	}

	@Override
	public void add(final int pIndex, final float pItem) throws ArrayIndexOutOfBoundsException {
		this.rangeCheckForAdd(pIndex);

		this.ensureCapacity(this.mSize + 1);

		System.arraycopy(this.mItems, pIndex, this.mItems, pIndex + 1, this.mSize - pIndex);

		this.mItems[pIndex] = pItem;
		this.mSize++;
	}

	@Override
	public float get(final int pIndex) throws ArrayIndexOutOfBoundsException {
		this.rangeCheck(pIndex);

		return this.mItems[pIndex];
	}

	@Override
	public float remove(final int pIndex) throws ArrayIndexOutOfBoundsException {
		this.rangeCheck(pIndex);

		final float oldValue = this.mItems[pIndex];

		final int numMoved = this.mSize - pIndex - 1;
		if(numMoved > 0) {
			System.arraycopy(this.mItems, pIndex + 1, this.mItems, pIndex, numMoved);
		}

		this.mSize--;

		return oldValue;
	}

	@Override
	public void clear() {
		this.mSize = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void rangeCheck(final int pIndex) throws ArrayIndexOutOfBoundsException {
		if(pIndex >= this.mSize) {
			throw new ArrayIndexOutOfBoundsException(pIndex);
		}
	}

	private void rangeCheckForAdd(final int pIndex) throws ArrayIndexOutOfBoundsException {
		if(pIndex > this.mSize || pIndex < 0) {
			throw new ArrayIndexOutOfBoundsException(pIndex);
		}
	}

	private void ensureCapacity(final int pCapacity) {
		final int currentCapacity = this.mItems.length;
		if(currentCapacity < pCapacity) {
			/* Increase array size. */
			final int newCapacity = (currentCapacity * 3) / 2 + 1;
			final float newItems[] = new float[newCapacity];
			System.arraycopy(this.mItems, 0, newItems, 0, currentCapacity);
			this.mItems = newItems;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
