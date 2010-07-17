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
		if(checkCollisionSub(pVerticesALength - 2, 0, pVerticesA, pVerticesB, pVerticesBLength)){
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
	
	

	public static boolean checkContains(final float[] pVertices, final int pVerticesLength, final float pX, final float pY) {
		int edgeResultSum = 0;

		for(int i = pVerticesLength - 4; i >= 0; i -= 2) {
			final int edgeResult = relativeCCW(pVertices[i], pVertices[i + 1], pVertices[i + 2], pVertices[i + 3], pX, pY);
			if(edgeResult == 0) {
				return true;
			} else {
				edgeResultSum += edgeResult;
			}
		}
		/* Also check the 'around the corner of the array' line. */
		final int edgeResult = relativeCCW(pVertices[pVerticesLength - 2], pVertices[pVerticesLength - 1], pVertices[0], pVertices[1], pX, pY);
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
