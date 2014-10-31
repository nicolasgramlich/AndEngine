package org.andengine.util.adt.list;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Nicolas Gramlich
 * @since 22:31:38 - 16.09.2010
 */
public class UniqueListTest extends TestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private UniqueList<UniqueInteger> mUniqueIntegerList;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	protected void setUp() throws Exception {
		this.mUniqueIntegerList = new UniqueList<UniqueInteger>(new ShiftList<UniqueInteger>(1));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void testUniqueDuplicateAdding() {
		UniqueInteger zero = new UniqueInteger(0, "ZERO");
		UniqueInteger one_1 = new UniqueInteger(1, "ONE(1)");
		UniqueInteger one_2 = new UniqueInteger(1, "ONE(2)");
		
		this.mUniqueIntegerList.add(zero);
		this.mUniqueIntegerList.add(one_1);
		this.mUniqueIntegerList.add(one_2);

		Assert.assertEquals(3, this.mUniqueIntegerList.size());
		this.mUniqueIntegerList.add(one_2);
		Assert.assertEquals(3, this.mUniqueIntegerList.size());
		this.mUniqueIntegerList.add(one_1);
		Assert.assertEquals(3, this.mUniqueIntegerList.size());
		this.mUniqueIntegerList.add(zero);
		Assert.assertEquals(3, this.mUniqueIntegerList.size());

		Assert.assertTrue(this.mUniqueIntegerList.remove(one_2));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class UniqueInteger implements Comparable<UniqueInteger> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mValue;
		private final String pName;

		// ===========================================================
		// Constructors
		// ===========================================================

		public UniqueInteger(final int mValue, final String pName) {
			this.mValue = mValue;
			this.pName = pName;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public int compareTo(final UniqueInteger pUniqueInteger) {
			return this.mValue - pUniqueInteger.mValue;
		}

		@Override
		public boolean equals(final Object pObject) {
			return this == pObject;
		}

		@Override
		public String toString() {
			return pName;
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
