package org.anddev.andengine.collision;

import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_X;
import static org.anddev.andengine.util.constants.Constants.VERTEX_INDEX_Y;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.util.MathUtils;

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

	private static final int RECTANGULARSHAPE_VERTEX_COUNT = 4;
	private static final int LINE_VERTEX_COUNT = 2;

	private static final float[] VERTICES_CONTAINS_TMP = new float[2 * RECTANGULARSHAPE_VERTEX_COUNT];
	private static final float[] VERTICES_COLLISION_TMP_A = new float[2 * RECTANGULARSHAPE_VERTEX_COUNT];
	private static final float[] VERTICES_COLLISION_TMP_B = new float[2 * RECTANGULARSHAPE_VERTEX_COUNT];

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

	public static boolean checkContains(final RectangularShape pRectangularShape, final float pX, final float pY) {
		RectangularShapeCollisionChecker.fillVertices(pRectangularShape, VERTICES_CONTAINS_TMP);
		return ShapeCollisionChecker.checkContains(VERTICES_CONTAINS_TMP, 2 * RECTANGULARSHAPE_VERTEX_COUNT, pX, pY);
	}

	public static boolean isVisible(final Camera pCamera, final RectangularShape pRectangularShape) {
		RectangularShapeCollisionChecker.fillVertices(pCamera, VERTICES_COLLISION_TMP_A);
		RectangularShapeCollisionChecker.fillVertices(pRectangularShape, VERTICES_COLLISION_TMP_B);

		return ShapeCollisionChecker.checkCollision(2 * RECTANGULARSHAPE_VERTEX_COUNT, VERTICES_COLLISION_TMP_A, 2 * RECTANGULARSHAPE_VERTEX_COUNT, VERTICES_COLLISION_TMP_B);
	}

	public static boolean isVisible(final Camera pCamera, final Line pLine) {
		RectangularShapeCollisionChecker.fillVertices(pCamera, VERTICES_COLLISION_TMP_A);
		LineCollisionChecker.fillVertices(pLine, VERTICES_COLLISION_TMP_B);

		return ShapeCollisionChecker.checkCollision(2 * RECTANGULARSHAPE_VERTEX_COUNT, VERTICES_COLLISION_TMP_A, 2 * LINE_VERTEX_COUNT, VERTICES_COLLISION_TMP_B);
	}

	public static boolean checkCollision(final RectangularShape pRectangularShapeA, final RectangularShape pRectangularShapeB) {
		RectangularShapeCollisionChecker.fillVertices(pRectangularShapeA, VERTICES_COLLISION_TMP_A);
		RectangularShapeCollisionChecker.fillVertices(pRectangularShapeB, VERTICES_COLLISION_TMP_B);

		return ShapeCollisionChecker.checkCollision(2 * RECTANGULARSHAPE_VERTEX_COUNT, VERTICES_COLLISION_TMP_A, 2 * RECTANGULARSHAPE_VERTEX_COUNT, VERTICES_COLLISION_TMP_B);
	}

	public static boolean checkCollision(final RectangularShape pRectangularShape, final Line pLine) {
		RectangularShapeCollisionChecker.fillVertices(pRectangularShape, VERTICES_COLLISION_TMP_A);
		LineCollisionChecker.fillVertices(pLine, VERTICES_COLLISION_TMP_B);

		return ShapeCollisionChecker.checkCollision(2 * RECTANGULARSHAPE_VERTEX_COUNT, VERTICES_COLLISION_TMP_A, 2 * LINE_VERTEX_COUNT, VERTICES_COLLISION_TMP_B);
	}

	public static void fillVertices(final RectangularShape pRectangularShape, final float[] pVertices) {
		final float left = 0;
		final float top = 0;
		final float right = pRectangularShape.getWidth();
		final float bottom = pRectangularShape.getHeight();

		pVertices[0 + VERTEX_INDEX_X] = left;
		pVertices[0 + VERTEX_INDEX_Y] = top;

		pVertices[2 + VERTEX_INDEX_X] = right;
		pVertices[2 + VERTEX_INDEX_Y] = top;

		pVertices[4 + VERTEX_INDEX_X] = right;
		pVertices[4 + VERTEX_INDEX_Y] = bottom;

		pVertices[6 + VERTEX_INDEX_X] = left;
		pVertices[6 + VERTEX_INDEX_Y] = bottom;

		pRectangularShape.getLocalToSceneTransformation().transform(pVertices);
	}

	private static void fillVertices(final Camera pCamera, final float[] pVertices) {
		pVertices[0 + VERTEX_INDEX_X] = pCamera.getMinX();
		pVertices[0 + VERTEX_INDEX_Y] = pCamera.getMinY();

		pVertices[2 + VERTEX_INDEX_X] = pCamera.getMaxX();
		pVertices[2 + VERTEX_INDEX_Y] = pCamera.getMinY();

		pVertices[4 + VERTEX_INDEX_X] = pCamera.getMaxX();
		pVertices[4 + VERTEX_INDEX_Y] = pCamera.getMaxY();

		pVertices[6 + VERTEX_INDEX_X] = pCamera.getMinX();
		pVertices[6 + VERTEX_INDEX_Y] = pCamera.getMaxY();

		MathUtils.rotateAroundCenter(pVertices, pCamera.getRotation(), pCamera.getCenterX(), pCamera.getCenterY());
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
