package org.andengine.util.algorithm;


import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:55:22 PM - Nov 10, 2011
 */
public class SprialTest extends TestCase {
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
	// TestMethods
	// ===========================================================

	public void testScaleAroundCenterNonOriginFactor() {
		final Spiral spiral = new Spiral(0, 0);

		Assert.assertEquals(0, spiral.getX());
		Assert.assertEquals(0, spiral.getY());

		spiral.step();
		Assert.assertEquals(1, spiral.getX());
		Assert.assertEquals(0, spiral.getY());

		spiral.step();
		Assert.assertEquals(1, spiral.getX());
		Assert.assertEquals(1, spiral.getY());

		spiral.step();
		Assert.assertEquals(0, spiral.getX());
		Assert.assertEquals(1, spiral.getY());

		spiral.step();
		Assert.assertEquals(-1, spiral.getX());
		Assert.assertEquals(1, spiral.getY());

		spiral.step();
		Assert.assertEquals(-1, spiral.getX());
		Assert.assertEquals(0, spiral.getY());

		spiral.step();
		Assert.assertEquals(-1, spiral.getX());
		Assert.assertEquals(-1, spiral.getY());

		spiral.step();
		Assert.assertEquals(0, spiral.getX());
		Assert.assertEquals(-1, spiral.getY());

		spiral.step();
		Assert.assertEquals(1, spiral.getX());
		Assert.assertEquals(-1, spiral.getY());

		spiral.step();
		Assert.assertEquals(2, spiral.getX());
		Assert.assertEquals(-1, spiral.getY());
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
