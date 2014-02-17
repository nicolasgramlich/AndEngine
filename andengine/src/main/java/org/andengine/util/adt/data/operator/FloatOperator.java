package org.andengine.util.adt.data.operator;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:24:15 - 02.02.2012
 */
public enum FloatOperator {
	// ===========================================================
	// Elements
	// ===========================================================

	EQUALS() {
		@Override
		public boolean check(final float pFloatA, final float pFloatB) {
			return pFloatA == pFloatB;
		}
	},
	NOT_EQUALS()  {
		@Override
		public boolean check(final float pFloatA, final float pFloatB) {
			return pFloatA != pFloatB;
		}
	},
	LESS_THAN()  {
		@Override
		public boolean check(final float pFloatA, final float pFloatB) {
			return pFloatA < pFloatB;
		}
	},
	LESS_OR_EQUAL_THAN()  {
		@Override
		public boolean check(final float pFloatA, final float pFloatB) {
			return pFloatA <= pFloatB;
		}
	},
	MORE_THAN()  {
		@Override
		public boolean check(final float pFloatA, final float pFloatB) {
			return pFloatA > pFloatB;
		}
	},
	MORE_OR_EQUAL_THAN()  {
		@Override
		public boolean check(final float pFloatA, final float pFloatB) {
			return pFloatA >= pFloatB;
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

	public abstract boolean check(final float pFloatA, final float pFloatB);

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}