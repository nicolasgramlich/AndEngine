package org.andengine.util.algorithm.collision;

import org.andengine.util.Constants;
import org.andengine.util.adt.transformation.Transformation;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:47:42 - 30.08.2011
 */
public final class TriangleCollisionChecker {
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

	private TriangleCollisionChecker() {

	}

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
		final float v0x = pX3 - pX1;
		final float v0y = pY3 - pY1;
		final float v1x = pX2 - pX1;
		final float v1y = pY2 - pY1;
		final float v2x = pX - pX1;
		final float v2y = pY - pY1;

		/* Compute dot products. */
		final float dot00 = v0x * v0x + v0y * v0y;
		final float dot01 = v0x * v1x + v0y * v1y;
		final float dot02 = v0x * v2x + v0y * v2y;
		final float dot11 = v1x * v1x + v1y * v1y;
		final float dot12 = v1x * v2x + v1y * v2y;

		/* Compute barycentric coordinates. */
		final float invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
		final float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		final float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

		/* Check if point is in triangle. */
		return (u > 0) && (v > 0) && (u + v < 1);
	}

	public static boolean checkContains(final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final Transformation pTransformation, final float pX, final float pY) {
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[0 + Constants.VERTEX_INDEX_X] = pX1;
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[0 + Constants.VERTEX_INDEX_Y] = pY1;

		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[2 + Constants.VERTEX_INDEX_X] = pX2;
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[2 + Constants.VERTEX_INDEX_Y] = pY2;

		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[4 + Constants.VERTEX_INDEX_X] = pX3;
		TriangleCollisionChecker.VERTICES_CONTAINS_TMP[4 + Constants.VERTEX_INDEX_Y] = pY3;

		pTransformation.transform(TriangleCollisionChecker.VERTICES_CONTAINS_TMP);

		final float x1 = TriangleCollisionChecker.VERTICES_CONTAINS_TMP[0 + Constants.VERTEX_INDEX_X];
		final float y1 = TriangleCollisionChecker.VERTICES_CONTAINS_TMP[0 + Constants.VERTEX_INDEX_Y];
		final float x2 = TriangleCollisionChecker.VERTICES_CONTAINS_TMP[2 + Constants.VERTEX_INDEX_X];
		final float y2 = TriangleCollisionChecker.VERTICES_CONTAINS_TMP[2 + Constants.VERTEX_INDEX_Y];
		final float x3 = TriangleCollisionChecker.VERTICES_CONTAINS_TMP[4 + Constants.VERTEX_INDEX_X];
		final float y3 = TriangleCollisionChecker.VERTICES_CONTAINS_TMP[4 + Constants.VERTEX_INDEX_Y];

		return TriangleCollisionChecker.checkContains(x1, y1, x2, y2, x3, y3, pX, pY);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}