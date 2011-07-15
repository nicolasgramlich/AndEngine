package org.anddev.andengine.collision;

import org.anddev.andengine.util.constants.Constants;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:50:19 - 11.03.2010
 */
public class ShapeCollisionChecker extends BaseCollisionChecker {
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

	public static boolean checkCollision(final int pVerticesALength, final float[] pVerticesA, final int pVerticesBLength, final float[] pVerticesB) {
		/* Check all the lines of A ... */
		for(int a = pVerticesALength - 4; a >= 0; a -= 2) {
			/* ... against all lines in B. */
			if(ShapeCollisionChecker.checkCollisionSub(a, a + 2, pVerticesA, pVerticesB, pVerticesBLength)){
				return true;
			}
		}
		/* Also check the 'around the corner of the array' line of A against all lines in B. */
		if(ShapeCollisionChecker.checkCollisionSub(pVerticesALength - 2, 0, pVerticesA, pVerticesB, pVerticesBLength)){
			return true;
		} else {
			/* At last check if one polygon 'contains' the other one by checking
			 * if one vertex of the one vertices is contained by all of the other vertices. */
			if(ShapeCollisionChecker.checkContains(pVerticesA, pVerticesALength, pVerticesB[Constants.VERTEX_INDEX_X], pVerticesB[Constants.VERTEX_INDEX_Y])) {
				return true;
			} else if(ShapeCollisionChecker.checkContains(pVerticesB, pVerticesBLength, pVerticesA[Constants.VERTEX_INDEX_X], pVerticesA[Constants.VERTEX_INDEX_Y])) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Checks line specified by pVerticesA[pVertexIndexA1] and pVerticesA[pVertexIndexA2] against all lines in pVerticesB.
	 */
	private static boolean checkCollisionSub(final int pVertexIndexA1, final int pVertexIndexA2, final float[] pVerticesA, final float[] pVerticesB, final int pVerticesBLength) {
		/* Check against all the lines of B. */
		final float vertexA1X = pVerticesA[pVertexIndexA1 + Constants.VERTEX_INDEX_X];
		final float vertexA1Y = pVerticesA[pVertexIndexA1 + Constants.VERTEX_INDEX_Y];
		final float vertexA2X = pVerticesA[pVertexIndexA2 + Constants.VERTEX_INDEX_X];
		final float vertexA2Y = pVerticesA[pVertexIndexA2 + Constants.VERTEX_INDEX_Y];

		for(int b = pVerticesBLength - 4; b >= 0; b -= 2) {
			if(LineCollisionChecker.checkLineCollision(vertexA1X, vertexA1Y, vertexA2X, vertexA2Y, pVerticesB[b + Constants.VERTEX_INDEX_X], pVerticesB[b + Constants.VERTEX_INDEX_Y], pVerticesB[b + 2 + Constants.VERTEX_INDEX_X], pVerticesB[b + 2 + Constants.VERTEX_INDEX_Y])){
				return true;
			}
		}
		/* Also check the 'around the corner of the array' line of B. */
		if(LineCollisionChecker.checkLineCollision(vertexA1X, vertexA1Y, vertexA2X, vertexA2Y, pVerticesB[pVerticesBLength - 2], pVerticesB[pVerticesBLength - 1], pVerticesB[Constants.VERTEX_INDEX_X], pVerticesB[Constants.VERTEX_INDEX_Y])){
			return true;
		}
		return false;
	}

	public static boolean checkContains(final float[] pVertices, final int pVerticesLength, final float pX, final float pY) {
		int edgeResultSum = 0;

		for(int i = pVerticesLength - 4; i >= 0; i -= 2) {
			final int edgeResult = BaseCollisionChecker.relativeCCW(pVertices[i], pVertices[i + 1], pVertices[i + 2], pVertices[i + 3], pX, pY);
			if(edgeResult == 0) {
				return true;
			} else {
				edgeResultSum += edgeResult;
			}
		}
		/* Also check the 'around the corner of the array' line. */
		final int edgeResult = BaseCollisionChecker.relativeCCW(pVertices[pVerticesLength - 2], pVertices[pVerticesLength - 1], pVertices[Constants.VERTEX_INDEX_X], pVertices[Constants.VERTEX_INDEX_Y], pX, pY);
		if(edgeResult == 0){
			return true;
		} else {
			edgeResultSum += edgeResult;
		}

		final int vertexCount = pVerticesLength / 2;
		/* Point is not on the edge, so check if the edge is on the same side(left or right) of all edges. */
		return edgeResultSum == vertexCount || edgeResultSum == -vertexCount ;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
