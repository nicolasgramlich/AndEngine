package org.andengine.util.adt.data.operator;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:24:15 - 02.02.2012
 */
public enum DoubleOperator {
	// ===========================================================
	// Elements
	// ===========================================================

	EQUALS() {
		@Override
		public boolean check(final double pDoubleA, final double pDoubleB) {
			return pDoubleA == pDoubleB;
		}
	},
	NOT_EQUALS() {
		@Override
		public boolean check(final double pDoubleA, final double pDoubleB) {
			return pDoubleA != pDoubleB;
		}
	},
	LESS_THAN() {
		@Override
		public boolean check(final double pDoubleA, final double pDoubleB) {
			return pDoubleA < pDoubleB;
		}
	},
	LESS_OR_EQUAL_THAN() {
		@Override
		public boolean check(final double pDoubleA, final double pDoubleB) {
			return pDoubleA <= pDoubleB;
		}
	},
	MORE_THAN() {
		@Override
		public boolean check(final double pDoubleA, final double pDoubleB) {
			return pDoubleA > pDoubleB;
		}
	},
	MORE_OR_EQUAL_THAN() {
		@Override
		public boolean check(final double pDoubleA, final double pDoubleB) {
			return pDoubleA >= pDoubleB;
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

	public abstract boolean check(final double pDoubleA, final double pDoubleB);

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}