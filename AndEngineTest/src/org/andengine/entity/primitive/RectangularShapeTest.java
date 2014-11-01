package org.andengine.entity.primitive;


import junit.framework.Assert;
import junit.framework.TestCase;

import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.util.AssertUtils;

import android.util.FloatMath;

/**
 * @author Nicolas Gramlich
 * @since 19:03:16 - 03.04.2010
 */
public class RectangularShapeTest extends TestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float DELTA = 0.0001f;

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

	public void testContainsSimple() {
		final TestRectangularShape shape = new TestRectangularShape(0, 0, 2, 2);

		/* Center */
		Assert.assertTrue(shape.contains(1, 1));

		/* Sides */
		Assert.assertTrue(shape.contains(1, 0 + DELTA));
		Assert.assertTrue(shape.contains(0 + DELTA, 1));
		Assert.assertTrue(shape.contains(2 - DELTA, 1));
		Assert.assertTrue(shape.contains(1, 2 - DELTA));

		/* Edges */
		Assert.assertTrue(shape.contains(0 + DELTA, 0 + DELTA));
		Assert.assertTrue(shape.contains(2 - DELTA, 2 - DELTA));
		Assert.assertTrue(shape.contains(0 + DELTA, 2 - DELTA));
		Assert.assertTrue(shape.contains(2 - DELTA, 0 + DELTA));

		/* Outside */
		Assert.assertFalse(shape.contains(0 - DELTA, 0 - DELTA));
		Assert.assertFalse(shape.contains(2 + DELTA, 2 + DELTA));
		Assert.assertFalse(shape.contains(2 + DELTA, 0 - DELTA));
		Assert.assertFalse(shape.contains(0 - DELTA, 2 + DELTA));
	}

	public void testContainsScaled() {
		final TestRectangularShape shape = new TestRectangularShape(0.5f, 0.5f, 1, 1);
		shape.setScale(2);

		/* Center */
		Assert.assertTrue(shape.contains(1, 1));

		/* Sides */
		Assert.assertTrue(shape.contains(1, 0 + DELTA));
		Assert.assertTrue(shape.contains(0 + DELTA, 1));
		Assert.assertTrue(shape.contains(2 - DELTA, 1));
		Assert.assertTrue(shape.contains(1, 2 - DELTA));

		/* Edges */
		Assert.assertTrue(shape.contains(0 + DELTA, 0 +  DELTA));
		Assert.assertTrue(shape.contains(2 - DELTA, 2 - DELTA));
		Assert.assertTrue(shape.contains(0 + DELTA, 2 - DELTA));
		Assert.assertTrue(shape.contains(2 - DELTA, 0 + DELTA));

		/* Outside */
		Assert.assertFalse(shape.contains(0 - DELTA, 0 - DELTA));
		Assert.assertFalse(shape.contains(2 + DELTA, 2 + DELTA));
		Assert.assertFalse(shape.contains(2 + DELTA, 0 - DELTA));
		Assert.assertFalse(shape.contains(0 - DELTA, 2 + DELTA));
	}

	public void testContainsRotated() {
		final TestRectangularShape shape = new TestRectangularShape(0, 0, 2, 2);
		shape.setRotation(45);

		/* Center */
		Assert.assertTrue(shape.contains(1, 1));

		/* (Old)Sides */
		Assert.assertTrue(shape.contains(1, 0));
		Assert.assertTrue(shape.contains(0, 1));
		Assert.assertTrue(shape.contains(2, 1));
		Assert.assertTrue(shape.contains(1, 2));

		/* (Old)Edges */
		Assert.assertFalse(shape.contains(0, 0));
		Assert.assertFalse(shape.contains(2, 2));
		Assert.assertFalse(shape.contains(0, 2));
		Assert.assertFalse(shape.contains(2, 0));
	}

	public void testContainsRotatedAndScaled() {
		final TestRectangularShape shape = new TestRectangularShape(0, 0, 2, 2);
		shape.setRotation(45);
		shape.setScale(2 + DELTA / FloatMath.sqrt(2f));

		/* Center */
		Assert.assertTrue(shape.contains(1, 1));

		/* (Old)Sides */
		Assert.assertTrue(shape.contains(1, 0));
		Assert.assertTrue(shape.contains(0, 1));
		Assert.assertTrue(shape.contains(2, 1));
		Assert.assertTrue(shape.contains(1, 2));

		/* (Old)Edges */
		Assert.assertTrue(shape.contains(0, 0));
		Assert.assertTrue(shape.contains(2, 2));
		Assert.assertTrue(shape.contains(0, 2));
		Assert.assertTrue(shape.contains(2, 0));

		/* (New)Edges */
		Assert.assertTrue(shape.contains(-1, 1));
		Assert.assertTrue(shape.contains(1, -1));
		Assert.assertTrue(shape.contains(1, 3));
		Assert.assertTrue(shape.contains(3, 1));
	}

	public void testCollidesWithSimple() {
		final TestRectangularShape shapeA = new TestRectangularShape(0, 0, 2, 2);
		final TestRectangularShape shapeB = new TestRectangularShape(1, 1, 2, 2);
		Assert.assertTrue(shapeA.collidesWith(shapeB));
	}

	public void testCollidesWithSimpleNot() {
		final TestRectangularShape shapeA = new TestRectangularShape(0, 0, 2, 2);
		final TestRectangularShape shapeB = new TestRectangularShape(3, 0, 2, 2);
		Assert.assertFalse(shapeA.collidesWith(shapeB));
	}

	public void testCollidesWithScaled() {
		final TestRectangularShape shapeA = new TestRectangularShape(0, 0, 2, 2);
		final TestRectangularShape shapeB = new TestRectangularShape(3, 3, 2, 2);
		Assert.assertFalse(shapeA.collidesWith(shapeB));

		shapeB.setScale(3);
		Assert.assertTrue(shapeA.collidesWith(shapeB));

		shapeB.setScaleCenter(0, 0);
		Assert.assertFalse(shapeA.collidesWith(shapeB));

		shapeB.setScale(2);
		shapeB.setScaleCenter(2, 2);
		Assert.assertTrue(shapeA.collidesWith(shapeB));
	}

	public void testCollidesWithScaledUneven() {
		final TestRectangularShape shapeA = new TestRectangularShape(0, 0, 2, 2);
		final TestRectangularShape shapeB = new TestRectangularShape(3, 0, 2, 2);
		Assert.assertFalse(shapeA.collidesWith(shapeB));

		shapeB.setScaleX(2.1f);
		Assert.assertTrue(shapeA.collidesWith(shapeB));

		shapeB.setScaleX(1);
		shapeB.setScaleY(2.1f);
		Assert.assertFalse(shapeA.collidesWith(shapeB));
	}

	public void testCollidesWithRotated() {
		final TestRectangularShape shapeA = new TestRectangularShape(0, 0, 4, 4);
		final TestRectangularShape shapeB = new TestRectangularShape(5, 0, 4, 4);
		Assert.assertFalse(shapeA.collidesWith(shapeB));

		shapeA.setRotation(45f);
		shapeB.setRotation(45f);
		Assert.assertTrue(shapeA.collidesWith(shapeB));

		shapeB.setRotation(90f);
		Assert.assertFalse(shapeA.collidesWith(shapeB));
	}

	public void testCollidesWithRotatedAroundCenter() {
		final TestRectangularShape shapeA = new TestRectangularShape(0, 0, 2, 2);
		final TestRectangularShape shapeB = new TestRectangularShape(3, 0, 2, 2);
		Assert.assertFalse(shapeA.collidesWith(shapeB));

		shapeA.setRotationCenter(2, 2);
		shapeA.setRotation(45f);
		Assert.assertTrue(shapeA.collidesWith(shapeB));

		shapeA.setRotation(90f);
		Assert.assertTrue(shapeA.collidesWith(shapeB));

		shapeA.setRotation(179.9f);
		Assert.assertTrue(shapeA.collidesWith(shapeB));

		shapeA.setRotation(180.1f);
		Assert.assertFalse(shapeA.collidesWith(shapeB));
	}

	public void testCollidesWithRotatedAndScaled() {
		final TestRectangularShape shapeA = new TestRectangularShape(0, 0, 2, 2);
		final TestRectangularShape shapeB = new TestRectangularShape(3, 0, 2, 2);
		Assert.assertFalse(shapeA.collidesWith(shapeB));

		shapeB.setRotation(45f);
		Assert.assertFalse(shapeA.collidesWith(shapeB));

		shapeB.setScale(2f / FloatMath.sqrt(2f) + DELTA);
		Assert.assertTrue(shapeA.collidesWith(shapeB));

		shapeB.setScale(2f / FloatMath.sqrt(2f) - DELTA);
		Assert.assertFalse(shapeA.collidesWith(shapeB));
	}


	public void testGetLocalCoordinatesSimple() {
		final TestRectangularShape shape = new TestRectangularShape(0, 0, 2, 2);
		AssertUtils.assertArrayEquals(new float[]{1, 1}, shape.convertSceneToLocalCoordinates(1, 1), DELTA);
	}

	public void testGetLocalCoordinatesNonOrigin() {
		final TestRectangularShape shape = new TestRectangularShape(10, 10, 2, 2);
		AssertUtils.assertArrayEquals(new float[]{1, 1}, shape.convertSceneToLocalCoordinates(11, 11), DELTA);
	}

	public void testGetLocalCoordinatesNonOriginRotated() {
		final TestRectangularShape shape = new TestRectangularShape(10, 10, 2, 2);
		shape.setRotation(90);
		AssertUtils.assertArrayEquals(new float[]{0, 2}, shape.convertSceneToLocalCoordinates(10, 10), DELTA);
	}

	public void testGetLocalCoordinatesNonOriginScaled() {
		final TestRectangularShape shape = new TestRectangularShape(10, 10, 2, 2);
		shape.setScale(0.5f);
		AssertUtils.assertArrayEquals(new float[]{0, 0}, shape.convertSceneToLocalCoordinates(10.5f, 10.5f), DELTA);
	}


	public void testGetSceneCenterCoordinatesSimple() {
		final TestRectangularShape shape = new TestRectangularShape(0, 0, 2, 2);
		AssertUtils.assertArrayEquals(new float[]{1, 1}, shape.getSceneCenterCoordinates(), DELTA);
	}

	public void testGetSceneCenterCoordinatesNonOrigin() {
		final TestRectangularShape shape = new TestRectangularShape(10, 10, 2, 2);
		AssertUtils.assertArrayEquals(new float[]{11, 11}, shape.getSceneCenterCoordinates(), DELTA);
	}

	public void testGetSceneCenterCoordinatesScaled() {
		final TestRectangularShape shape = new TestRectangularShape(10, 10, 2, 2);
		shape.setScale(2);
		AssertUtils.assertArrayEquals(new float[]{11, 11}, shape.getSceneCenterCoordinates(), DELTA);
	}

	public void testGetSceneCenterCoordinatesRotated() {
		final TestRectangularShape shape = new TestRectangularShape(10, 10, 2, 2);
		shape.setRotation(123);
		AssertUtils.assertArrayEquals(new float[]{11, 11}, shape.getSceneCenterCoordinates(), DELTA);
	}

	public void testGetSceneCenterCoordinatesScaledUneven() {
		final TestRectangularShape shape = new TestRectangularShape(10, 10, 2, 2);
		shape.setScale(2);
		shape.setScaleCenter(0, 0);
		AssertUtils.assertArrayEquals(new float[]{12, 12}, shape.getSceneCenterCoordinates(), DELTA);
	}

	public void testGetSceneCenterCoordinatesRotatedUneven() {
		final TestRectangularShape shape = new TestRectangularShape(10, 10, 2, 2);
		shape.setRotation(90);
		shape.setRotationCenter(0, 0);
		AssertUtils.assertArrayEquals(new float[]{9, 11}, shape.getSceneCenterCoordinates(), DELTA);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private class TestRectangularShape extends RectangularShape {
		public TestRectangularShape(final float pX, final float pY, final float pWidth, final float pHeight) {
			super(pX, pY, pWidth, pHeight, null);
		}

		@Override
		protected void onUpdateVertices() {
		}

		@Override
		public IVertexBufferObject getVertexBufferObject() {
			return null;
		}
	}
}
