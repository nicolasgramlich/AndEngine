package org.andengine.util.adt.data.operator;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:24:15 - 02.02.2012
 */
public enum CharOperator {
	// ===========================================================
	// Elements
	// ===========================================================

	EQUALS() {
		@Override
		public boolean check(final char pCharA, final char pCharB) {
			return pCharA == pCharB;
		}
	},
	NOT_EQUALS()  {
		@Override
		public boolean check(final char pCharA, final char pCharB) {
			return pCharA != pCharB;
		}
	},
	LESS_THAN()  {
		@Override
		public boolean check(final char pCharA, final char pCharB) {
			return pCharA < pCharB;
		}
	},
	LESS_OR_EQUAL_THAN()  {
		@Override
		public boolean check(final char pCharA, final char pCharB) {
			return pCharA <= pCharB;
		}
	},
	MORE_THAN()  {
		@Override
		public boolean check(final char pCharA, final char pCharB) {
			return pCharA > pCharB;
		}
	},
	MORE_OR_EQUAL_THAN()  {
		@Override
		public boolean check(final char pCharA, final char pCharB) {
			return pCharA >= pCharB;
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

	public abstract boolean check(final char pCharA, final char pCharB);

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}