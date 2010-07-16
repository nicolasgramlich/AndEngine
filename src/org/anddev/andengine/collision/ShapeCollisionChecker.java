package org.anddev.andengine.collision;


/**
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
	
	public static boolean checkCollision(final int pVerticesALength, final int pVerticesBLength, final float[] pVerticesA, final float[] pVerticesB) {		
		/* Check all the lines of A ... */
		for(int a = pVerticesALength - 4; a >= 0; a -= 2) {
			/* ... against all lines in B. */
			if(checkCollisionSub(a, a + 2, pVerticesA, pVerticesB, pVerticesBLength)){
				return true;
			}
		}
		/* Also check the 'around the corner of the array' line of A against all lines in B. */
		if(checkCollisionSub(0, pVerticesALength - 2, pVerticesA, pVerticesB, pVerticesBLength)){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks line specified by pVerticesA[pVertexIndexA1] and pVerticesA[pVertexIndexA2] against all lines in pVerticesB.
	 * */
	private static boolean checkCollisionSub(final int pVertexIndexA1, final int pVertexIndexA2, final float[] pVerticesA, final float[] pVerticesB, final int pVerticesBLength) {
		/* Check against all the lines of B. */
		final float vertexA1X = pVerticesA[pVertexIndexA1];
		final float vertexA1Y = pVerticesA[pVertexIndexA1 + 1];
		final float vertexA2X = pVerticesA[pVertexIndexA2];
		final float vertexA2Y = pVerticesA[pVertexIndexA2 + 1];
		
		for(int b = pVerticesBLength - 4; b >= 0; b -= 2) {
			if(checkLineCollision(vertexA1X, vertexA1Y, vertexA2X, vertexA2Y, pVerticesB[b], pVerticesB[b + 1], pVerticesB[b + 2], pVerticesB[b + 3])){
				return true;
			}
		}
		/* Also check the 'around the corner of the array' line of B. */
		if(checkLineCollision(vertexA1X, vertexA1Y, vertexA2X, vertexA2Y, pVerticesB[pVerticesBLength - 2], pVerticesB[pVerticesBLength - 1], pVerticesB[0], pVerticesB[1])){
			return true;
		}
		return false;
	}
	
	

	public static boolean checkBoxContains(final float[] pVertex1, final float[] pVertex2, final float[] pVertex3, final float[] pVertex4, final float pX, final float pY) {
		final float x1 = pVertex1[0];
		final float y1 = pVertex1[1];
		final float x2 = pVertex2[0];
		final float y2 = pVertex2[1];
		final float x3 = pVertex3[0];
		final float y3 = pVertex3[1];
		final float x4 = pVertex4[0];
		final float x5 = pVertex4[1];
		
		final int resultEdgeA = relativeCCW(x1, y1, x2, y2, pX, pY);
		if(resultEdgeA == 0) {
			return true;
		}
		
		final int resultEdgeB = relativeCCW(x2, y2, x3, y3, pX, pY);
		if(resultEdgeB == 0) {
			return true;
		}
		
		final int resultEdgeC = relativeCCW(x3, y3, x4, x5, pX, pY);
		if(resultEdgeC == 0) {
			return true;
		}
		
		final int resultEdgeD = relativeCCW(x4, x5, x1, y1, pX, pY);
		if(resultEdgeD == 0) {
			return true;
		}
		
		/* Point is not on the edge, so check if the edge is on the same side(left or right) of all edges. */
		final int resultEdgeSum = resultEdgeA + resultEdgeB + resultEdgeC + resultEdgeD;
		return resultEdgeSum == 4 || resultEdgeSum == -4;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
