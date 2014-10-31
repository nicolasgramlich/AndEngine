package org.andengine.util;


import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Nicolas Gramlich
 * @since 19:03:16 - 03.04.2010
 */
public class TextUtilsTest extends TestCase {
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

	// ===========================================================
	// Methods
	// ===========================================================

	public void testCountOccurrenceFollowUp() {
		Assert.assertEquals(2, TextUtils.countOccurrences("aabbccdd", 'b'));
	}

	public void testCountOccurrenceEmpty() {
		Assert.assertEquals(0, TextUtils.countOccurrences("", 'b'));
	}

	public void testCountOccurrenceNone() {
		Assert.assertEquals(0, TextUtils.countOccurrences("aaaaaaaa", 'b'));
	}

	public void testCountOccurrenceBeginning() {
		Assert.assertEquals(1, TextUtils.countOccurrences("baaaaa", 'b'));
	}

	public void testCountOccurrenceBeginningAndEnd() {
		Assert.assertEquals(2, TextUtils.countOccurrences("baaaaab", 'b'));
	}

	public void testSplitSimple() {
		final String[] split = TextUtils.split("a\nb", '\n').toArray(new String[2]);
		Assert.assertEquals(2, split.length);
		this.assertArrayEquals(new String[]{"a", "b"}, split);
	}

	public void testSplitSimple2() {
		final String[] split = TextUtils.split("a\nb\nc", '\n').toArray(new String[3]);
		Assert.assertEquals(3, split.length);
		this.assertArrayEquals(new String[]{"a", "b", "c"}, split);
	}

	public void testSplitSimple3() {
		final String[] split = TextUtils.split("a\nb\nc\n", '\n').toArray(new String[4]);
		Assert.assertEquals(4, split.length);
		this.assertArrayEquals(new String[]{"a", "b", "c", ""}, split);
	}

	public void testSplitEmpty() {
		final String[] split = TextUtils.split("", '\n').toArray(new String[1]);
		Assert.assertEquals(1, split.length);
		this.assertArrayEquals(new String[]{""}, split);
	}

	public void testSplitEmpty2() {
		final String[] split = TextUtils.split("\n", '\n').toArray(new String[2]);
		Assert.assertEquals(2, split.length);
		this.assertArrayEquals(new String[]{"", ""}, split);
	}

	public void testSplitEmptyLines() {
		final String[] split = TextUtils.split("\n\na", '\n').toArray(new String[3]);
		Assert.assertEquals(3, split.length);
		this.assertArrayEquals(new String[]{"", "", "a"}, split);
	}

	public void testSplitEmptyLines2() {
		final String[] split = TextUtils.split("\n\n\n", '\n').toArray(new String[4]);
		Assert.assertEquals(4, split.length);
		this.assertArrayEquals(new String[]{"", "", "", ""}, split);
	}

	private void assertArrayEquals(final Object[] pArrayA, final Object[] pArrayB) {
		if(pArrayA == null || pArrayB == null) {
			Assert.fail("One of the arrays was null.");
		}

		if(pArrayA.length != pArrayB.length) {
			Assert.fail("Arrays were not the same lenght.");
		}

		for(int i = 0; i < pArrayA.length; i++) {
			Assert.assertEquals("Index: i", pArrayA[i], pArrayB[i]);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
