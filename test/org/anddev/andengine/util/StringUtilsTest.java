package org.anddev.andengine.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Nicolas Gramlich
 * @since 19:03:16 - 03.04.2010
 */
public class StringUtilsTest {
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
	
	@Test
	public void testCountOccurrenceFollowUp() {
		assertEquals(2, StringUtils.countOccurences("aabbccdd", "b"));
	}
	
	@Test
	public void testCountOccurrenceEmpty() {
		assertEquals(0, StringUtils.countOccurences("", "b"));
	}
	
	@Test
	public void testCountOccurrenceNone() {
		assertEquals(0, StringUtils.countOccurences("aaaaaaaa", "b"));
	}
	
	@Test
	public void testCountOccurrenceBeginning() {
		assertEquals(1, StringUtils.countOccurences("baaaaa", "b"));
	}
	
	@Test
	public void testCountOccurrenceBeginningAndEnd() {
		assertEquals(2, StringUtils.countOccurences("baaaaab", "b"));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
