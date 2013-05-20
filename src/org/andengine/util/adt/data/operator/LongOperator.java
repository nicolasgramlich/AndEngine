package org.andengine.util.adt.data.operator;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:24:15 - 02.02.2012
 */
public enum LongOperator {
	// ===========================================================
	// Elements
	// ===========================================================

	EQUALS() {
		@Override
		public boolean check(final long pLongA, final long pLongB) {
			return pLongA == pLongB;
		}
	},
	NOT_EQUALS() {
		@Override
		public boolean check(final long pLongA, final long pLongB) {
			return pLongA != pLongB;
		}
	},
	LESS_THAN() {
		@Override
		public boolean check(final long pLongA, final long pLongB) {
			return pLongA < pLongB;
		}
	},
	LESS_OR_EQUAL_THAN() {
		@Override
		public boolean check(final long pLongA, final long pLongB) {
			return pLongA <= pLongB;
		}
	},
	MORE_THAN() {
		@Override
		public boolean check(final long pLongA, final long pLongB) {
			return pLongA > pLongB;
		}
	},
	MORE_OR_EQUAL_THAN() {
		@Override
		public boolean check(final long pLongA, final long pLongB) {
			return pLongA >= pLongB;
		}
	};

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public abstract boolean check(final long pLongA, final long pLongB);

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}