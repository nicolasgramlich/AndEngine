package org.andengine.util;


import junit.framework.TestCase;

import org.andengine.util.math.MathUtils;

/**
 * @author Nicolas Gramlich
 * @since 19:03:16 - 03.04.2010
 */
public class MathUtilsTest extends TestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float DELTA = 0.0001f;

	private static float[] TEMP_ARRAY = new float[2];

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

	// ===========================================================
	// Rotate around center
	// ===========================================================

	public void testRotateAroundCenterOriginAngleZero() {
		AssertUtils.assertArrayEquals(new float[]{1, 1}, MathUtils.rotateAroundCenter(new float[]{1, 1}, 0, 0, 0), DELTA);
	}

	public void testRotateAroundCenterOriginAngle90() {
		AssertUtils.assertArrayEquals(new float[]{-1, 1}, MathUtils.rotateAroundCenter(new float[]{1, 1}, 90, 0, 0), DELTA);
	}

	public void testRotateAroundCenterNonOriginAngle0() {
		AssertUtils.assertArrayEquals(new float[]{1, 1}, MathUtils.rotateAroundCenter(new float[]{1, 1}, 0, 2, 2), DELTA);
	}

	public void testRotateAroundCenterNonOriginAngle180() {
		AssertUtils.assertArrayEquals(new float[]{3, 3}, MathUtils.rotateAroundCenter(new float[]{1, 1}, 180, 2, 2), DELTA);
	}

	// ===========================================================
	// Scale around center
	// ===========================================================

	public void testScaleAroundCenterOriginFactor1() {
		AssertUtils.assertArrayEquals(new float[]{1, 1}, MathUtils.scaleAroundCenter(new float[]{1, 1}, 1, 1, 0, 0), DELTA);
	}

	public void testScaleAroundCenterOriginFactor2() {
		AssertUtils.assertArrayEquals(new float[]{2, 2}, MathUtils.scaleAroundCenter(new float[]{1, 1}, 2, 2, 0, 0), DELTA);
	}

	public void testScaleAroundCenterOriginFactor0_5() {
		AssertUtils.assertArrayEquals(new float[]{-0.5f, 0.5f}, MathUtils.scaleAroundCenter(new float[]{-1, 1}, 0.5f, 0.5f, 0, 0), DELTA);
	}

	public void testScaleAroundCenterNonOriginFactor1() {
		AssertUtils.assertArrayEquals(new float[]{1, 1}, MathUtils.scaleAroundCenter(new float[]{1, 1}, 1, 1, 2, 2), DELTA);
	}

	public void testScaleAroundCenterNonOriginFactor() {
		AssertUtils.assertArrayEquals(new float[]{0, 0}, MathUtils.scaleAroundCenter(new float[]{1, 1}, 2, 2, 2, 2), DELTA);
	}

	// ===========================================================
	// Rotate and Scale around Center
	// ===========================================================

	public void testRotateAndScaleAroundCenterOriginAngle0Factor1() {
		TEMP_ARRAY = new float[]{1, 1};
		MathUtils.rotateAroundCenter(TEMP_ARRAY, 0, 0, 0);
		MathUtils.scaleAroundCenter(TEMP_ARRAY, 1, 1, 0, 0);

		AssertUtils.assertArrayEquals(new float[]{1, 1}, TEMP_ARRAY, DELTA);
	}

	public void testRotateAndScaleAroundCenterOriginAngle90Factor2() {
		TEMP_ARRAY = new float[]{1, 1};
		MathUtils.rotateAndScaleAroundCenter(TEMP_ARRAY, 90, 0, 0, 2, 2, 0, 0);

		AssertUtils.assertArrayEquals(new float[]{-2, 2}, TEMP_ARRAY, DELTA);
	}

	// ===========================================================
	// Revert Functions
	// ===========================================================

	public void testRotateAndScaleAroundCenterOriginAngle90Factor2Revert() {
		TEMP_ARRAY = new float[]{-2, 2};
		MathUtils.revertRotateAndScaleAroundCenter(TEMP_ARRAY, 90, 0, 0, 2, 2, 0, 0);

		AssertUtils.assertArrayEquals(new float[]{1, 1}, TEMP_ARRAY, DELTA);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
