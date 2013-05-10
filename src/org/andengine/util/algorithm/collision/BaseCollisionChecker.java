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
public final class BaseCollisionChecker {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private BaseCollisionChecker() {

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

	public static boolean checkAxisAlignedRectangleCollision(final float pLeftA, final float pTopA, final float pRightA, final float pBottomA, final float pLeftB, final float pTopB, final float pRightB, final float pBottomB) {
		return (pLeftA < pRightB) &&
				(pLeftB < pRightA) &&
				(pTopA < pBottomB) &&
				(pTopB < pBottomA);
	}

	public static boolean checkAxisAlignedRectangleContains(final float pLeft, final float pTop, final float pRight, final float pBottom, final float pX, final float pY) {
		return (pX > pLeft) &&
				(pX < pRight) &&
				(pY > pTop) &&
				(pY < pBottom);
	}

	/**
	 * Returns an indicator of where the specified point (PX, PY) lies with
	 * respect to the line segment from (X1, Y1) to (X2, Y2). The
	 * return value can be either 1, -1, or 0 and indicates in which direction
	 * the specified line must pivot around its first endpoint, (X1, Y1),
	 * in order to point at the specified point (PX, PY).
	 * <p>
	 * A return value of 1 indicates that the line segment must turn in the
	 * direction that takes the positive X axis towards the negative Y axis. In
	 * the default coordinate system used by Java 2D, this direction is
	 * counterclockwise.
	 * <p>
	 * A return value of -1 indicates that the line segment must turn in the
	 * direction that takes the positive X axis towards the positive Y axis. In
	 * the default coordinate system, this direction is clockwise.
	 * <p>
	 * A return value of 0 indicates that the point lies exactly on the line
	 * segment. Note that an indicator value of 0 is rare and not useful for
	 * determining colinearity because of floating point rounding issues.
	 * <p>
	 * If the point is colinear with the line segment, but not between the
	 * endpoints, then the value will be -1 if the point lies
	 * "beyond (X1, Y1)" or 1 if the point lies "beyond (X2, Y2)".
	 *
	 * @param pX1 ,
	 * @param pY1 the coordinates of the beginning of the specified
	 *            line segment
	 * @param pX2 ,
	 * @param pY2 the coordinates of the end of the specified line
	 *            segment
	 * @param pPX ,
	 * @param pPY the coordinates of the specified point to be
	 *            compared with the specified line segment
	 * @return an integer that indicates the position of the third specified
	 *         coordinates with respect to the line segment formed by the first
	 *         two specified coordinates.
	 */
	public static int relativeCCW(final float pX1, final float pY1, float pX2, float pY2, float pPX, float pPY) {
		pX2 -= pX1;
		pY2 -= pY1;
		pPX -= pX1;
		pPY -= pY1;
		float ccw = (pPX * pY2) - (pPY * pX2);
		if (ccw == 0.0f) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on. We can calculate a
			// relative value using the projection of PX,PY onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// endpoint used as the origin for the projection.
			ccw = (pPX * pX2) + (pPY * pY2);
			if (ccw > 0.0f) {
				// Reverse the projection to be relative to the original X2,Y2
				// X2 and Y2 are simply negated.
				// PX and PY need to have (X2 - X1) or (Y2 - Y1) subtracted
				// from them (based on the original values)
				// Since we really want to get a positive answer when the
				// point is "beyond (X2,Y2)", then we want to calculate
				// the inverse anyway - thus we leave X2 & Y2 negated.
				pPX -= pX2;
				pPY -= pY2;
				ccw = (pPX * pX2) + (pPY * pY2);
				if (ccw < 0.0f) {
					ccw = 0.0f;
				}
			}
		}
		return (ccw < 0.0f) ? -1 : ((ccw > 0.0f) ? 1 : 0);
	}

	/**
	 * Calls through to {@link #checkCollisionSub(float[], int, int, int, int, int, float[], int, int, int, int)} with the default parameters internally used by different AndEngine primitives.
	 * @param pVerticesA
	 * @param pVertexCountA the number of vertices in pVerticesA
	 * @param pVerticesB
	 * @param pVertexCountB the number of vertices in pVerticesB
	 * @return
	 */
	public static boolean checkCollision(final float[] pVerticesA, final int pVertexCountA, final float[] pVerticesB, final int pVertexCountB) {
		return BaseCollisionChecker.checkCollision(pVerticesA, pVertexCountA, Constants.VERTEX_INDEX_X, Constants.VERTEX_INDEX_Y, 2, pVerticesB, pVertexCountB, Constants.VERTEX_INDEX_X, Constants.VERTEX_INDEX_Y, 2);
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
		for (int a = pVertexCountA - 2; a >= 0; a--) {
			/* ... against all lines in B. */
			if (BaseCollisionChecker.checkCollisionSub(pVerticesA, pVertexOffsetXA, pVertexOffsetYA, pVertexStrideA, a, a + 1, pVerticesB, pVertexCountB, pVertexOffsetXB, pVertexOffsetYB, pVertexStrideB)) {
				return true;
			}
		}
		/* Also check the 'around the corner of the array' line of A against all lines in B. */
		if (BaseCollisionChecker.checkCollisionSub(pVerticesA, pVertexOffsetXA, pVertexOffsetYA, pVertexStrideA, pVertexCountA - 1, 0, pVerticesB, pVertexCountB, pVertexOffsetXB, pVertexOffsetYB, pVertexStrideB)) {
			return true;
		} else {
			/* At last check if one polygon 'contains' the other one by checking
			 * if one vertex of the one vertices is contained by all of the other vertices. */
			if (BaseCollisionChecker.checkContains(pVerticesA, pVertexCountA, VertexUtils.getVertex(pVerticesB, pVertexOffsetXB, pVertexStrideB, 0), VertexUtils.getVertex(pVerticesB, pVertexOffsetYB, pVertexStrideB, 0))) {
				return true;
			} else if (BaseCollisionChecker.checkContains(pVerticesB, pVertexCountB, VertexUtils.getVertex(pVerticesA, pVertexOffsetXA, pVertexStrideA, 0), VertexUtils.getVertex(pVerticesA, pVertexOffsetYA, pVertexStrideA, 0))) {
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
	static boolean checkCollisionSub(final float[] pVerticesA, final int pVertexOffsetXA, final int pVertexOffsetYA, final int pVertexStrideA, final int pVertexIndexA1, final int pVertexIndexA2, final float[] pVerticesB, final int pVertexCountB, final int pVertexOffsetXB, final int pVertexOffsetYB, final int pVertexStrideB) {
		/* Check against all the lines of B. */
		final float vertexA1X = VertexUtils.getVertex(pVerticesA, pVertexOffsetXA, pVertexStrideA, pVertexIndexA1);
		final float vertexA1Y = VertexUtils.getVertex(pVerticesA, pVertexOffsetYA, pVertexStrideA, pVertexIndexA1);
		final float vertexA2X = VertexUtils.getVertex(pVerticesA, pVertexOffsetXA, pVertexStrideA, pVertexIndexA2);
		final float vertexA2Y = VertexUtils.getVertex(pVerticesA, pVertexOffsetYA, pVertexStrideA, pVertexIndexA2);

		for (int b = pVertexCountB - 2; b >= 0; b--) {
			final float vertexB1X = VertexUtils.getVertex(pVerticesB, pVertexOffsetXB, pVertexStrideB, b);
			final float vertexB1Y = VertexUtils.getVertex(pVerticesB, pVertexOffsetYB, pVertexStrideB, b);
			final float vertexB2X = VertexUtils.getVertex(pVerticesB, pVertexOffsetXB, pVertexStrideB, b + 1);
			final float vertexB2Y = VertexUtils.getVertex(pVerticesB, pVertexOffsetYB, pVertexStrideB, b + 1);
			if (LineCollisionChecker.checkLineCollision(vertexA1X, vertexA1Y, vertexA2X, vertexA2Y, vertexB1X, vertexB1Y, vertexB2X, vertexB2Y)) {
				return true;
			}
		}
		/* Also check the 'around the corner of the array' line of B. */
		final float vertexB1X = VertexUtils.getVertex(pVerticesB, pVertexOffsetXB, pVertexStrideB, pVertexCountB - 1);
		final float vertexB1Y = VertexUtils.getVertex(pVerticesB, pVertexOffsetYB, pVertexStrideB, pVertexCountB - 1);
		final float vertexB2X = VertexUtils.getVertex(pVerticesB, pVertexOffsetXB, pVertexStrideB, 0);
		final float vertexB2Y = VertexUtils.getVertex(pVerticesB, pVertexOffsetYB, pVertexStrideB, 0);
		if (LineCollisionChecker.checkLineCollision(vertexA1X, vertexA1Y, vertexA2X, vertexA2Y, vertexB1X, vertexB1Y, vertexB2X, vertexB2Y)) {
			return true;
		}
		return false;
	}

	/**
	 * Calls through to {@link #checkContains(float[], int, int, int, int, float, float)} with the default parameters internally used by different AndEngine primitives.
	 *
	 * @param pVertices
	 * @param pVertexCount the number of vertices in pVertices
	 * @param pX
	 * @param pY
	 * @return
	 */
	public static boolean checkContains(final float[] pVertices, final int pVertexCount, final float pX, final float pY) {
		return BaseCollisionChecker.checkContains(pVertices, pVertexCount, Constants.VERTEX_INDEX_X, Constants.VERTEX_INDEX_Y, 2, pX, pY);
	}

	/**
	 * Works with complex polygons!
	 *
	 * @see <a href="http://alienryderflex.com/polygon/">alienryderflex.com/polygon/</a>
	 *
	 * @param pVertices
	 * @param pVertexCount the number of vertices in pVertices
	 * @param pVertexOffsetX
	 * @param pVertexOffsetY
	 * @param pVertexStride
	 * @param pX
	 * @param pY
	 * @return <code>true</code> when the point defined by <code>(pX, pY)</code> is inside the polygon defined by <code>pVertices</code>, <code>false</code>. If the point is exactly on the edge of the polygon, the result can be <code>true</code> or <code>false</code>.
	 */
	public static boolean checkContains(final float[] pVertices, final int pVertexCount, final int pVertexOffsetX, final int pVertexOffsetY, final int pVertexStride, final float pX, final float pY) {
		boolean odd = false;

		int j = pVertexCount - 1;
		for (int i = 0; i < pVertexCount; i++) {
			final float vertexXI = VertexUtils.getVertex(pVertices, pVertexOffsetX, pVertexStride, i);
			final float vertexYI = VertexUtils.getVertex(pVertices, pVertexOffsetY, pVertexStride, i);
			final float vertexXJ = VertexUtils.getVertex(pVertices, pVertexOffsetX, pVertexStride, j);
			final float vertexYJ = VertexUtils.getVertex(pVertices, pVertexOffsetY, pVertexStride, j);

			if ((((vertexYI < pY) && (vertexYJ >= pY)) || ((vertexYJ < pY) && (vertexYI >= pY))) && ((vertexXI <= pX) || (vertexXJ <= pX))) {
				odd ^= ((vertexXI + (((pY - vertexYI) / (vertexYJ - vertexYI)) * (vertexXJ - vertexXI))) < pX);
			}
			j = i;
		}

		return odd;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
