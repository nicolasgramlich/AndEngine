package org.anddev.andengine.collision;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.util.constants.Constants;
import org.anddev.andengine.util.math.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:50:19 - 11.03.2010
 */
public class RectangularShapeCollisionChecker extends ShapeCollisionChecker {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int RECTANGULARSHAPE_VERTEX_COUNT = 4;

	private static final float[] VERTICES_CONTAINS_TMP = new float[2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT];
	private static final float[] VERTICES_COLLISION_TMP_A = new float[2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT];
	private static final float[] VERTICES_COLLISION_TMP_B = new float[2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT];

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

	public static boolean checkContains(final RectangularShape<?> pRectangularShape, final float pX, final float pY) {
		RectangularShapeCollisionChecker.fillVertices(pRectangularShape, RectangularShapeCollisionChecker.VERTICES_CONTAINS_TMP);
		return ShapeCollisionChecker.checkContains(RectangularShapeCollisionChecker.VERTICES_CONTAINS_TMP, 2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, pX, pY);
	}

	public static boolean isVisible(final Camera pCamera, final RectangularShape<?> pRectangularShape) {
		RectangularShapeCollisionChecker.fillVertices(pCamera, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A);
		RectangularShapeCollisionChecker.fillVertices(pRectangularShape, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);

		return ShapeCollisionChecker.checkCollision(2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A, 2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);
	}

	public static boolean isVisible(final Camera pCamera, final Line pLine) {
		RectangularShapeCollisionChecker.fillVertices(pCamera, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A);
		LineCollisionChecker.fillVertices(pLine, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);

		return ShapeCollisionChecker.checkCollision(2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A, 2 * LineCollisionChecker.LINE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);
	}

	public static boolean checkCollision(final RectangularShape<?> pRectangularShapeA, final RectangularShape<?> pRectangularShapeB) {
		RectangularShapeCollisionChecker.fillVertices(pRectangularShapeA, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A);
		RectangularShapeCollisionChecker.fillVertices(pRectangularShapeB, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);

		return ShapeCollisionChecker.checkCollision(2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A, 2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);
	}

	public static boolean checkCollision(final RectangularShape<?> pRectangularShape, final Line pLine) {
		RectangularShapeCollisionChecker.fillVertices(pRectangularShape, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A);
		LineCollisionChecker.fillVertices(pLine, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);

		return ShapeCollisionChecker.checkCollision(2 * RectangularShapeCollisionChecker.RECTANGULARSHAPE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_A, 2 * LineCollisionChecker.LINE_VERTEX_COUNT, RectangularShapeCollisionChecker.VERTICES_COLLISION_TMP_B);
	}

	public static void fillVertices(final RectangularShape<?> pRectangularShape, final float[] pVertices) {
		final float left = 0;
		final float top = 0;
		final float right = pRectangularShape.getWidth();
		final float bottom = pRectangularShape.getHeight();

		pVertices[0 + Constants.VERTEX_INDEX_X] = left;
		pVertices[0 + Constants.VERTEX_INDEX_Y] = top;

		pVertices[2 + Constants.VERTEX_INDEX_X] = right;
		pVertices[2 + Constants.VERTEX_INDEX_Y] = top;

		pVertices[4 + Constants.VERTEX_INDEX_X] = right;
		pVertices[4 + Constants.VERTEX_INDEX_Y] = bottom;

		pVertices[6 + Constants.VERTEX_INDEX_X] = left;
		pVertices[6 + Constants.VERTEX_INDEX_Y] = bottom;

		pRectangularShape.getLocalToSceneTransformation().transform(pVertices);
	}

	private static void fillVertices(final Camera pCamera, final float[] pVertices) {
		pVertices[0 + Constants.VERTEX_INDEX_X] = pCamera.getXMin();
		pVertices[0 + Constants.VERTEX_INDEX_Y] = pCamera.getYMin();

		pVertices[2 + Constants.VERTEX_INDEX_X] = pCamera.getXMax();
		pVertices[2 + Constants.VERTEX_INDEX_Y] = pCamera.getYMin();

		pVertices[4 + Constants.VERTEX_INDEX_X] = pCamera.getXMax();
		pVertices[4 + Constants.VERTEX_INDEX_Y] = pCamera.getYMax();

		pVertices[6 + Constants.VERTEX_INDEX_X] = pCamera.getXMin();
		pVertices[6 + Constants.VERTEX_INDEX_Y] = pCamera.getYMax();

		MathUtils.rotateAroundCenter(pVertices, pCamera.getRotation(), pCamera.getCenterX(), pCamera.getCenterY());
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
