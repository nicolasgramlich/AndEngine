package org.anddev.andengine.collision;

import org.anddev.andengine.util.constants.Constants;
import org.anddev.andengine.util.transformation.Transformation;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:47:42 - 30.08.2011
 */
public class TriangleCollisionChecker {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int TRIANGLE_VERTEX_COUNT = 3;

	private static final float[] VERTICES_CONTAINS_TMP = new float[2 * TriangleCollisionChecker.TRIANGLE_VERTEX_COUNT];

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

	public static boolean checkContains(final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX, final float pY) {
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[0 + Constants.VERTEX_INDEX_X] = pX1;
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[0 + Constants.VERTEX_INDEX_Y] = pY1;

		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[2 + Constants.VERTEX_INDEX_X] = pX2;
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[2 + Constants.VERTEX_INDEX_Y] = pY2;

		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[4 + Constants.VERTEX_INDEX_X] = pX3;
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[4 + Constants.VERTEX_INDEX_Y] = pY3;

		return ShapeCollisionChecker.checkContains(TriangleCollisionChecker.VERTICES_CONTAINS_TMP, TriangleCollisionChecker.TRIANGLE_VERTEX_COUNT * 2, pX, pY);
	}

	public static boolean checkContains(final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final Transformation pTransformation, final float pX, final float pY) {
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[0 + Constants.VERTEX_INDEX_X] = pX1;
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[0 + Constants.VERTEX_INDEX_Y] = pY1;
		
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[2 + Constants.VERTEX_INDEX_X] = pX2;
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[2 + Constants.VERTEX_INDEX_Y] = pY2;
		
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[4 + Constants.VERTEX_INDEX_X] = pX3;
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[4 + Constants.VERTEX_INDEX_Y] = pY3;
		
		pTransformation.transform(TriangleCollisionChecker.VERTICES_CONTAINS_TMP);
		
		return ShapeCollisionChecker.checkContains(TriangleCollisionChecker.VERTICES_CONTAINS_TMP, TriangleCollisionChecker.TRIANGLE_VERTEX_COUNT * 2, pX, pY);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
