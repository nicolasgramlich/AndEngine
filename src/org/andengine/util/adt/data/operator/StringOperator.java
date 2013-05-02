package org.andengine.util.adt.data.operator;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:57:05 - 10.10.2011
 */
public enum StringOperator {
	// ===========================================================
	// Elements
	// ===========================================================

	EQUALS() {
		@Override
		public boolean check(final String pStringA, final String pStringB) {
			return pStringA.equals(pStringB);
		}
	},
	EQUALS_IGNORE_CASE() {
		@Override
		public boolean check(final String pStringA, final String pStringB) {
			return pStringA.equalsIgnoreCase(pStringB);
		}
	},
	NOT_EQUALS() {
		@Override
		public boolean check(final String pStringA, final String pStringB) {
			return !pStringA.equals(pStringB);
		}
	},
	NOT_EQUALS_IGNORE_CASE() {
		@Override
		public boolean check(final String pStringA, final String pStringB) {
			return !pStringA.equalsIgnoreCase(pStringB);
		}
	},
	CONTAINS() {
		@Override
		public boolean check(final String pStringA, final String pStringB) {
			return pStringA.contains(pStringB);
		}
	},
	NOT_CONTAINS() {
		@Override
		public boolean check(final String pStringA, final String pStringB) {
			return !pStringA.contains(pStringB);
		}
	},
	STARTS_WITH() {
		@Override
		public boolean check(final String pStringA, final String pStringB) {
			return pStringA.startsWith(pStringB);
		}
	},
	NOT_STARTS_WITH() {
		@Override
		public boolean check(final String pStringA, final String pStringB) {
			return !pStringA.startsWith(pStringB);
		}
	},
	ENDS_WITH() {
		@Override
		public boolean check(final String pStringA, final String pStringB) {
			return pStringA.endsWith(pStringB);
		}
	},
	NOT_ENDS_WITH() {
		@Override
		public boolean check(final String pStringA, final String pStringB) {
			return !pStringA.endsWith(pStringB);
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

	public abstract boolean check(final String pStringA, final String pStringB);

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}