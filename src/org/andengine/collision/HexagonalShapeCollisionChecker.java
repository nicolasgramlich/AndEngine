package org.andengine.collision;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.shape.HexagonalShape;
import org.andengine.util.constants.Constants;
import org.andengine.util.math.MathUtils;
import org.andengine.util.transformation.Transformation;

/**
 * 
 * @author Stefan Hagdahl
 *
 */
public class HexagonalShapeCollisionChecker extends ShapeCollisionChecker {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int HEXAGONALSHAPE_VERTEX_COUNT = 8;
	private static final int RECTANGULARSHAPE_VERTEX_COUNT = 4;
	private static final int LINE_VERTEX_COUNT = 2;

	private static final float[] VERTICES_CONTAINS_TMP = new float[2 * HexagonalShapeCollisionChecker.HEXAGONALSHAPE_VERTEX_COUNT];
	private static final float[] VERTICES_COLLISION_TMP_A = new float[2 * HexagonalShapeCollisionChecker.HEXAGONALSHAPE_VERTEX_COUNT];
	private static final float[] VERTICES_COLLISION_TMP_B = new float[2 * HexagonalShapeCollisionChecker.HEXAGONALSHAPE_VERTEX_COUNT];
	private static final float[] VERTICES_COLLISION_TMP_C = new float[2 * RECTANGULARSHAPE_VERTEX_COUNT];

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
	
	public static boolean checkContains(final HexagonalShape pHexagonalShape, final float pX, final float pY) {
		HexagonalShapeCollisionChecker.fillVertices(pHexagonalShape, VERTICES_CONTAINS_TMP);
		return ShapeCollisionChecker.checkContains(VERTICES_CONTAINS_TMP, 2 * HEXAGONALSHAPE_VERTEX_COUNT, pX, pY);
	}

	public static boolean isVisible(final Camera pCamera, final HexagonalShape pHexagonalShape) {
		HexagonalShapeCollisionChecker.fillVertices(pCamera, VERTICES_COLLISION_TMP_C);
		HexagonalShapeCollisionChecker.fillVertices(pHexagonalShape, VERTICES_COLLISION_TMP_B);

		return ShapeCollisionChecker.checkCollision(2 * RECTANGULARSHAPE_VERTEX_COUNT, VERTICES_COLLISION_TMP_C, 2 * HEXAGONALSHAPE_VERTEX_COUNT, VERTICES_COLLISION_TMP_B);
	}

	public static boolean checkCollision(final HexagonalShape pHexagonalShapeA, final HexagonalShape pHexagonalShapeB) {
		HexagonalShapeCollisionChecker.fillVertices(pHexagonalShapeA, VERTICES_COLLISION_TMP_A);
		HexagonalShapeCollisionChecker.fillVertices(pHexagonalShapeB, VERTICES_COLLISION_TMP_B);

		return ShapeCollisionChecker.checkCollision(2 * HEXAGONALSHAPE_VERTEX_COUNT, VERTICES_COLLISION_TMP_A, 2 * HEXAGONALSHAPE_VERTEX_COUNT, VERTICES_COLLISION_TMP_B);
	}

	public static boolean checkCollision(final HexagonalShape pHexagonalShape, final Line pLine) {
		HexagonalShapeCollisionChecker.fillVertices(pHexagonalShape, VERTICES_COLLISION_TMP_A);
		LineCollisionChecker.fillVertices(pLine, VERTICES_COLLISION_TMP_B);

		return ShapeCollisionChecker.checkCollision(2 * HEXAGONALSHAPE_VERTEX_COUNT, VERTICES_COLLISION_TMP_A, 2 * LINE_VERTEX_COUNT, VERTICES_COLLISION_TMP_B);
	}

	public static void fillVertices(final HexagonalShape pHexagonalShape, final float[] pVertices) {
		HexagonalShapeCollisionChecker.fillVertices(0, 0, pHexagonalShape, pHexagonalShape.getLocalToSceneTransformation(), pVertices);
	}

	public static void fillVertices(final float pLocalX, final float pLocalY, final HexagonalShape pHexagonalShape, final Transformation pLocalToSceneTransformation, final float[] pVertices) {
		final float x = pLocalX;
		final float y = pLocalY;
		final float r = pHexagonalShape.getR();
		final float h = pHexagonalShape.getH();
		final float side = pHexagonalShape.getSide();
		
		//point 0
		pVertices[0 + Constants.VERTEX_INDEX_X] = x;
		pVertices[0 + Constants.VERTEX_INDEX_Y] = y;
		//point 1
		pVertices[2 + Constants.VERTEX_INDEX_X] = x + side;
		pVertices[2 + Constants.VERTEX_INDEX_Y] = y;
		//point 2
		pVertices[4 + Constants.VERTEX_INDEX_X] = x + side + h;
		pVertices[4 + Constants.VERTEX_INDEX_Y] = y + r;
		//point 3
		pVertices[6 + Constants.VERTEX_INDEX_X] = x + side;
		pVertices[6 + Constants.VERTEX_INDEX_Y] = y + r + r;
		//point 4
		pVertices[8 + Constants.VERTEX_INDEX_X] = x;
		pVertices[8 + Constants.VERTEX_INDEX_Y] = y + r + r;
		//point 5
		pVertices[10 + Constants.VERTEX_INDEX_X] = x - h;
		pVertices[10 + Constants.VERTEX_INDEX_Y] = y + x;

		pLocalToSceneTransformation.transform(pVertices);
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
