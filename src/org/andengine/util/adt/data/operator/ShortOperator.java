package org.andengine.util.adt.data.operator;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:24:15 - 02.02.2012
 */
public enum ShortOperator {
	// ===========================================================
	// Elements
	// ===========================================================

	EQUALS() {
		@Override
		public boolean check(final short pShortA, final short pShortB) {
			return pShortA == pShortB;
		}
	},
	NOT_EQUALS() {
		@Override
		public boolean check(final short pShortA, final short pShortB) {
			return pShortA != pShortB;
		}
	},
	LESS_THAN() {
		@Override
		public boolean check(final short pShortA, final short pShortB) {
			return pShortA < pShortB;
		}
	},
	LESS_OR_EQUAL_THAN() {
		@Override
		public boolean check(final short pShortA, final short pShortB) {
			return pShortA <= pShortB;
		}
	},
	MORE_THAN() {
		@Override
		public boolean check(final short pShortA, final short pShortB) {
			return pShortA > pShortB;
		}
	},
	MORE_OR_EQUAL_THAN() {
		@Override
		public boolean check(final short pShortA, final short pShortB) {
			return pShortA >= pShortB;
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

	public abstract boolean check(final short pShortA, final short pShortB);

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}