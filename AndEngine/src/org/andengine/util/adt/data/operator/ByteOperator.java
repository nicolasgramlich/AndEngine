package org.andengine.util.adt.data.operator;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:24:15 - 02.02.2012
 */
public enum ByteOperator {
	// ===========================================================
	// Elements
	// ===========================================================

	EQUALS() {
		@Override
		public boolean check(final byte pByteA, final byte pByteB) {
			return pByteA == pByteB;
		}
	},
	NOT_EQUALS()  {
		@Override
		public boolean check(final byte pByteA, final byte pByteB) {
			return pByteA != pByteB;
		}
	},
	LESS_THAN()  {
		@Override
		public boolean check(final byte pByteA, final byte pByteB) {
			return pByteA < pByteB;
		}
	},
	LESS_OR_EQUAL_THAN()  {
		@Override
		public boolean check(final byte pByteA, final byte pByteB) {
			return pByteA <= pByteB;
		}
	},
	MORE_THAN()  {
		@Override
		public boolean check(final byte pByteA, final byte pByteB) {
			return pByteA > pByteB;
		}
	},
	MORE_OR_EQUAL_THAN()  {
		@Override
		public boolean check(final byte pByteA, final byte pByteB) {
			return pByteA >= pByteB;
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

	public abstract boolean check(final byte pByteA, final byte pByteB);

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}