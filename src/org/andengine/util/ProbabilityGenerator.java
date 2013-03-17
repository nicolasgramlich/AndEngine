package org.andengine.util;

import java.util.ArrayList;

import org.andengine.util.adt.array.ArrayUtils;
import org.andengine.util.math.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 16:54:24 - 07.11.2010
 */
public class ProbabilityGenerator<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mProbabilitySum;
	private final ArrayList<Entry<T>> mEntries = new ArrayList<Entry<T>>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void add(final float pFactor, final T ... pElements) {
		this.mProbabilitySum += pFactor;
		this.mEntries.add(new Entry<T>(pFactor, pElements));
	}

	public T next() {
		float random = MathUtils.random(0, this.mProbabilitySum);

		final ArrayList<Entry<T>> factors = this.mEntries;

		for (int i = factors.size() - 1; i >= 0; i--) {
			final Entry<T> entry = factors.get(i);
			random -= entry.mFactor;
			if (random <= 0) {
				return entry.getReturnValue();
			}
		}

		final Entry<T> lastEntry = factors.get(factors.size() - 1);
		return lastEntry.getReturnValue();
	}

	public void clear() {
		this.mProbabilitySum = 0;
		this.mEntries.clear();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class Entry<T> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		public final float mFactor;
		public final T[] mData;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Entry(final float pFactor, final T ... pData) {
			this.mFactor = pFactor;
			this.mData = pData;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public T getReturnValue() {
			if (this.mData.length == 1) {
				return this.mData[0];
			} else {
				return ArrayUtils.random(this.mData);
			}
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
