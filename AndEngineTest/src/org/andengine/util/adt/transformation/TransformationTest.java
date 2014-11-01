package org.andengine.util.adt.transformation;

import org.andengine.util.AssertUtils;

import android.graphics.Matrix;
import android.test.AndroidTestCase;

/**
 * @author Nicolas Gramlich
 * @since 15:27:27 - 12.05.2010
 */
public class TransformationTest extends AndroidTestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float DELTA = 0.0001f;

	// ===========================================================
	// Fields
	// ===========================================================

	private Matrix mMatrix;
	private Transformation mTransformation;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void setUp() throws Exception {
		this.mMatrix = new Matrix();
		this.mTransformation = new Transformation();
	}

	@Override
	public void tearDown() throws Exception {

	}

	// ===========================================================
	// Test-Methods
	// ===========================================================

	public void testPostRotate1() {
		this.mMatrix.postRotate(3);
		this.mTransformation.postRotate(3);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	public void testPostRotate2() {
		this.mMatrix.postRotate(-3);
		this.mTransformation.postRotate(-3);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	public void testPostRotate3() {
		this.mMatrix.postRotate(135);
		this.mTransformation.postRotate(135);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	public void testPostScale() {
		this.mMatrix.postScale(3, 3);
		this.mTransformation.postScale(3, 3);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	public void testPostTranslate() {
		this.mMatrix.postTranslate(-1, 15);
		this.mTransformation.postTranslate(-1, 15);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	public void testPreRotate1() {
		this.mMatrix.preRotate(3);
		this.mTransformation.preRotate(3);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	public void testPreRotate2() {
		this.mMatrix.preRotate(-3);
		this.mTransformation.preRotate(-3);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	public void testPreRotate3() {
		this.mMatrix.preRotate(135);
		this.mTransformation.preRotate(135);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	public void testPreScale() {
		this.mMatrix.preScale(3, 3);
		this.mTransformation.preScale(3, 3);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	public void testPreTranslate() {
		this.mMatrix.preTranslate(-1, 15);
		this.mTransformation.preTranslate(-1, 15);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	public void testPostComplex() {
		this.mMatrix.postTranslate(10, 2);
		this.mTransformation.postTranslate(10, 2);

		this.mMatrix.postScale(3, 2);
		this.mTransformation.postScale(3, 2);

		this.mMatrix.postRotate(3);
		this.mTransformation.postRotate(3);

		this.mMatrix.postScale(2, 2);
		this.mTransformation.postScale(2, 2);

		this.mMatrix.postTranslate(-10, 10);
		this.mTransformation.postTranslate(-10, 10);

		this.mMatrix.postRotate(-25);
		this.mTransformation.postRotate(-25);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	public void testPreComplex() {
		this.mMatrix.preTranslate(10, 2);
		this.mTransformation.preTranslate(10, 2);

		this.mMatrix.preScale(3, 2);
		this.mTransformation.preScale(3, 2);

		this.mMatrix.preRotate(3);
		this.mTransformation.preRotate(3);

		this.mMatrix.preScale(2, 2);
		this.mTransformation.preScale(2, 2);

		this.mMatrix.preTranslate(-10, 10);
		this.mTransformation.preTranslate(-10, 10);

		this.mMatrix.preRotate(-25);
		this.mTransformation.preRotate(-25);

		final float[] points = { 2, 10 };

		this.testTransformationVsMatrix(points);
	}

	private void testTransformationVsMatrix(final float[] pPoints) {
		final float[] pts1 = pPoints.clone();
		final float[] pts2 = pPoints.clone();

		this.mMatrix.mapPoints(pts1);
		this.mTransformation.transform(pts2);

		AssertUtils.assertArrayEquals(pts1, pts2, TransformationTest.DELTA);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
