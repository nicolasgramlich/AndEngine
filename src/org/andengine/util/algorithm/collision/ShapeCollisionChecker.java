package org.andengine.util.algorithm.collision;

import org.andengine.opengl.util.VertexUtils;
import org.andengine.util.Constants;


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

	/**
	 * Calls through to {@link ShapeCollisionChecker#checkCollisionSub(float[], int, int, int, int, int, float[], int, int, int, int)} with the default parameters internally used by different AndEngine primitives.
	 * @param pVerticesA
	 * @param pVertexCountA the number of vertices in pVerticesA
	 * @param pVerticesB
	 * @param pVertexCountB the number of vertices in pVerticesB
	 * @return
	 */
	public static boolean checkCollision(final float[] pVerticesA, final int pVertexCountA, final float[] pVerticesB, final int pVertexCountB) {
		return ShapeCollisionChecker.checkCollision(pVerticesA, pVertexCountA, Constants.VERTEX_INDEX_X, Constants.VERTEX_INDEX_Y, 2, pVerticesB, pVertexCountB, Constants.VERTEX_INDEX_X, Constants.VERTEX_INDEX_Y, 2);
	}

	/**
	 * @param pVerticesA
	 * @param pVertexCountA the number of vertices in pVerticesA
	 * @param pVertexOffsetXA
	 * @param pVertexOffsetYA
	 * @param pVertexStrideA
	 * @param pVerticesB
	 * @param pVertexCountB the number of vertices in pVerticesB
	 * @param pVertexOffsetXB
	 * @param pVertexOffsetYB
	 * @param pVertexStrideB
	 * @return
	 */
	public static boolean checkCollision(final float[] pVerticesA, final int pVertexCountA, final int pVertexOffsetXA, final int pVertexOffsetYA, final int pVertexStrideA, final float[] pVerticesB, final int pVertexCountB, final int pVertexOffsetXB, final int pVertexOffsetYB, final int pVertexStrideB) {
		/* Check all the lines of A ... */
		for(int a = pVertexCountA - 2; a >= 0; a--) {
			/* ... against all lines in B. */
			if(ShapeCollisionChecker.checkCollisionSub(pVerticesA, pVertexOffsetXA, pVertexOffsetYA, pVertexStrideA, a, a + 1, pVerticesB, pVertexCountB, pVertexOffsetXB, pVertexOffsetYB, pVertexStrideB)){
				return true;
			}
		}
		/* Also check the 'around the corner of the array' line of A against all lines in B. */
		if(ShapeCollisionChecker.checkCollisionSub(pVerticesA, pVertexOffsetXA, pVertexOffsetYA, pVertexStrideA, pVertexCountA - 1, 0, pVerticesB, pVertexCountB, pVertexOffsetXB, pVertexOffsetYB, pVertexStrideB)){
			return true;
		} else {
			/* At last check if one polygon 'contains' the other one by checking
			 * if one vertex of the one vertices is contained by all of the other vertices. */
			if(ShapeCollisionChecker.checkContains(pVerticesA, pVertexCountA, VertexUtils.getVertex(pVerticesB, pVertexOffsetXB, pVertexStrideB, 0), VertexUtils.getVertex(pVerticesB, pVertexOffsetYB, pVertexStrideB, 0))) {
				return true;
			} else if(ShapeCollisionChecker.checkContains(pVerticesB, pVertexCountB, VertexUtils.getVertex(pVerticesA, pVertexOffsetXA, pVertexStrideA, 0), VertexUtils.getVertex(pVerticesA, pVertexOffsetYA, pVertexStrideA, 0))) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Checks line specified by <code>pVertexIndexA1</code> and <code>pVertexIndexA2</code> in <code>pVerticesA</code> against all lines in <code>pVerticesB</code>.
	 * 
	 * @param pVerticesA
	 * @param pVertexOffsetXA
	 * @param pVertexOffsetYA
	 * @param pVertexStrideA
	 * @param pVertexIndexA1 the first point of the line in pVerticesA
	 * @param pVertexIndexA2 the second point of the line in pVerticesA
	 * @param pVerticesB
	 * @param pVertexCountB the number of vertices in pVerticesB
	 * @param pVertexOffsetXB
	 * @param pVertexOffsetYB
	 * @param pVertexStrideB
	 * @return
	 */
	private static boolean checkCollisionSub(final float[] pVerticesA, final int pVertexOffsetXA, final int pVertexOffsetYA, final int pVertexStrideA, final int pVertexIndexA1, final int pVertexIndexA2, final float[] pVerticesB, final int pVertexCountB, final int pVertexOffsetXB, final int pVertexOffsetYB, final int pVertexStrideB) {
		/* Check against all the lines of B. */
		final float vertexA1X = VertexUtils.getVertex(pVerticesA, pVertexOffsetXA, pVertexStrideA, pVertexIndexA1);
		final float vertexA1Y = VertexUtils.getVertex(pVerticesA, pVertexOffsetYA, pVertexStrideA, pVertexIndexA1);
		final float vertexA2X = VertexUtils.getVertex(pVerticesA, pVertexOffsetXA, pVertexStrideA, pVertexIndexA2);
		final float vertexA2Y = VertexUtils.getVertex(pVerticesA, pVertexOffsetYA, pVertexStrideA, pVertexIndexA2);

		for(int b = pVertexCountB - 2; b >= 0; b--) {
			final float vertexB1X = VertexUtils.getVertex(pVerticesB, pVertexOffsetXB, pVertexStrideB, b);
			final float vertexB1Y = VertexUtils.getVertex(pVerticesB, pVertexOffsetYB, pVertexStrideB, b);
			final float vertexB2X = VertexUtils.getVertex(pVerticesB, pVertexOffsetXB, pVertexStrideB, b + 1);
			final float vertexB2Y = VertexUtils.getVertex(pVerticesB, pVertexOffsetYB, pVertexStrideB, b + 1);
			if(LineCollisionChecker.checkLineCollision(vertexA1X, vertexA1Y, vertexA2X, vertexA2Y, vertexB1X, vertexB1Y, vertexB2X, vertexB2Y)){
				return true;
			}
		}
		/* Also check the 'around the corner of the array' line of B. */
		final float vertexB1X = VertexUtils.getVertex(pVerticesB, pVertexOffsetXB, pVertexStrideB, pVertexCountB - 1);
		final float vertexB1Y = VertexUtils.getVertex(pVerticesB, pVertexOffsetYB, pVertexStrideB, pVertexCountB - 1);
		final float vertexB2X = VertexUtils.getVertex(pVerticesB, pVertexOffsetXB, pVertexStrideB, 0);
		final float vertexB2Y = VertexUtils.getVertex(pVerticesB, pVertexOffsetYB, pVertexStrideB, 0);
		if(LineCollisionChecker.checkLineCollision(vertexA1X, vertexA1Y, vertexA2X, vertexA2Y, vertexB1X, vertexB1Y, vertexB2X, vertexB2Y)){
			return true;
		}
		return false;
	}

	/**
	 * Calls through to {@link ShapeCollisionChecker#checkContains(float[], int, int, int, int, float, float)} with the default parameters internally used by different AndEngine primitives.
	 *
	 * @param pVertices
	 * @param pVertexCount the number of vertices in pVertices
	 * @param pX
	 * @param pY
	 * @return
	 */
	public static boolean checkContains(final float[] pVertices, final int pVertexCount, final float pX, final float pY) {
		return ShapeCollisionChecker.checkContains(pVertices, pVertexCount, Constants.VERTEX_INDEX_X, Constants.VERTEX_INDEX_Y, 2, pX, pY);
	}

	public static boolean checkContains(final float[] pVertices, final int pVertexCount, final int pVertexOffsetX, final int pVertexOffsetY, final int pVertexStride, final float pX, final float pY) {
		int edgeResultSum = 0;

		for(int i = pVertexCount - 2; i >= 0; i--) {
			final float x1 = VertexUtils.getVertex(pVertices, pVertexOffsetX, pVertexStride, i);
			final float y1 = VertexUtils.getVertex(pVertices, pVertexOffsetY, pVertexStride, i);
			final float x2 = VertexUtils.getVertex(pVertices, pVertexOffsetX, pVertexStride, i + 1);
			final float y2 = VertexUtils.getVertex(pVertices, pVertexOffsetY, pVertexStride, i + 1);
			final int edgeResult = BaseCollisionChecker.relativeCCW(x1, y1, x2, y2, pX, pY);
			if(edgeResult == 0) {
				return true;
			} else {
				edgeResultSum += edgeResult;
			}
		}
		/* Also check the 'around the corner of the array' line. */
		final float x1 = VertexUtils.getVertex(pVertices, pVertexOffsetX, pVertexStride, pVertexCount - 1);
		final float y1 = VertexUtils.getVertex(pVertices, pVertexOffsetY, pVertexStride, pVertexCount - 1);
		final float x2 = VertexUtils.getVertex(pVertices, pVertexOffsetX, pVertexStride, 0);
		final float y2 = VertexUtils.getVertex(pVertices, pVertexOffsetY, pVertexStride, 0);
		final int edgeResult = BaseCollisionChecker.relativeCCW(x1, y1, x2, y2, pX, pY);
		if(edgeResult == 0){
			return true;
		} else {
			edgeResultSum += edgeResult;
		}

		/* Point is not on the edge, so check if the edge is on the same side(left or right) of all edges. */
		return (edgeResultSum == pVertexCount) || (edgeResultSum == -pVertexCount) ;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
