package org.anddev.andengine.collision;

/**
 * @author Nicolas Gramlich
 * @since 11:50:19 - 11.03.2010
 */
public class BaseCollisionChecker {
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

	public static boolean checkAxisAlignedBoxCollision(final float pLeftA, final float pTopA, final float pRightA, final float pBottomA, final float pLeftB, final float pTopB, final float pRightB, final float pBottomB) {
		return (pLeftA < pRightB &&
				pLeftB < pRightA &&
				pTopA < pBottomB &&
				pTopB < pBottomA);
	}

	public static boolean checkLineCollision(final double pX1, final double pY1, final double pX2, final double pY2, final double pX3, final double pY3, final double pX4, final double pY4) {
		return ((relativeCCW(pX1, pY1, pX2, pY2, pX3, pY3) * relativeCCW(pX1, pY1, pX2, pY2, pX4, pY4) <= 0)
				&& (relativeCCW(pX3, pY3, pX4, pY4, pX1, pY1) * relativeCCW(pX3, pY3, pX4, pY4, pX2, pY2) <= 0));
	}

	/**
	 * Returns an indicator of where the specified point (PX,&nbsp;PY) lies with
	 * respect to the line segment from (X1,&nbsp;Y1) to (X2,&nbsp;Y2). The
	 * return value can be either 1, -1, or 0 and indicates in which direction
	 * the specified line must pivot around its first endpoint, (X1,&nbsp;Y1),
	 * in order to point at the specified point (PX,&nbsp;PY).
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
	 * "beyond (X1,&nbsp;Y1)" or 1 if the point lies "beyond (X2,&nbsp;Y2)".
	 * 
	 * @param pX1
	 *            ,&nbsp;Y1 the coordinates of the beginning of the specified
	 *            line segment
	 * @param pX2
	 *            ,&nbsp;Y2 the coordinates of the end of the specified line
	 *            segment
	 * @param pPX
	 *            ,&nbsp;PY the coordinates of the specified point to be
	 *            compared with the specified line segment
	 * @return an integer that indicates the position of the third specified
	 *         coordinates with respect to the line segment formed by the first
	 *         two specified coordinates.
	 */
	public static int relativeCCW(final double pX1, final double pY1, double pX2, double pY2, double pPX, double pPY) {
		// TODO Float instead of double!!! (All test will still run?)
		pX2 -= pX1;
		pY2 -= pY1;
		pPX -= pX1;
		pPY -= pY1;
		double ccw = pPX * pY2 - pPY * pX2;
		if (ccw == 0.0) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on. We can calculate a
			// relative value using the projection of PX,PY onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// endpoint used as the origin for the projection.
			ccw = pPX * pX2 + pPY * pY2;
			if (ccw > 0.0) {
				// Reverse the projection to be relative to the original X2,Y2
				// X2 and Y2 are simply negated.
				// PX and PY need to have (X2 - X1) or (Y2 - Y1) subtracted
				// from them (based on the original values)
				// Since we really want to get a positive answer when the
				// point is "beyond (X2,Y2)", then we want to calculate
				// the inverse anyway - thus we leave X2 & Y2 negated.
				pPX -= pX2;
				pPY -= pY2;
				ccw = pPX * pX2 + pPY * pY2;
				if (ccw < 0.0) {
					ccw = 0.0;
				}
			}
		}
		return (ccw < 0.0) ? -1 : ((ccw > 0.0) ? 1 : 0);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
