package org.andengine.util;

import org.andengine.util.math.MathUtils;

/**
 * (c) 2013 Nicolas Gramlich
 * 
 * @author Nicolas Gramlich
 * @since 23:09:15 - 01.06.2013
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Bernstein_polynomial">Bézier curve (Wikipedia)</a> 
 * @see <a href="http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/spline/Bezier/bezier-der.html">Derivatives of a Bézier Curve (Michigan Tech University)</a>
 * @see <a href="http://www.iaeng.org/IJAM/issues_v40/issue_2/IJAM_40_2_07.pdf">Continuous Curvature Path Generation Based on Bezier Curves for Autonomous Vehicles (Choi, Curry, Elkaim)</a>
 * @see <a href="http://www.whizkidtech.redprince.net/bezier/">Bézier Curves (Stanislav)</a>
 * @see <a href="http://www.cad.zju.edu.cn/home/hwlin/Curvature-of-singular-Bezier-curves-and-surfaces.pdf">Curvature of singular Bézier curves and surfaces (Zhejiang University)</a>
 * @see <a href="http://en.wikipedia.org/wiki/Bernstein_polynomial">Bernstein polynomial (Wikipedia)</a>
 */
public final class BezierCurveUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int LENGTH_SAMPLES_DEFAULT = 10;

	private static final float[] COORDINATES_TMP = new float[2];

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private BezierCurveUtils() {

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

	/**
	 * Calculates the (x|y|z|...)-coordinate for the given control point (x|y|z|...)-coordinates at value <code>t</code>.
	 *
	 * @param t in the interval <code>[0, 1]</code>.
	 * @param pControlPointCoordinates (x|y|z|...)-coordinates of control points of the Bézier curve.
	 * @return
	 */
	public static final float getBezierCurveCoordinate(final float t, final float[] pControlPointCoordinates) {
		float result = 0;
		final int n = pControlPointCoordinates.length - 1;
		for (int i = 0; i <= n; i++) {
			result += pControlPointCoordinates[i] * BezierCurveUtils.getBernsteinPolynomial(t, i, n);
		}
		return result;
	}

	/**
	 * Calculates the x/y-coordinate for the given control point x/y-coordinates at value <code>t</code>.
	 *
	 * @param t in the interval <code>[0, 1]</code>.
	 * @param pXs x-coordinates of the control points of the Bézier curve.
	 * @param pYs y-coordinates of the control points of the Bézier curve.
	 * @return a shared(!) float[] of length 2.
	 */
	public static final float[] getBezierCurveCoordinates(final float t, final float[] pXs, final float[] pYs) {
		return BezierCurveUtils.getBezierCurveCoordinates(t, pXs, pYs, COORDINATES_TMP);
	}

	/**
	 * Calculates the x/y-coordinate for the given control point x/y-coordinates at value <code>t</code>.
	 *
	 * @param t in the interval <code>[0, 1]</code>.
	 * @param pXs x-coordinates of the control points of the Bézier curve.
	 * @param pYs y-coordinates of the control points of the Bézier curve.
	 * @param pReuse must be of length 2.
	 * @return <code>pReuse</code> as a convenience.
	 */
	public static final float[] getBezierCurveCoordinates(final float t, final float[] pXs, final float[] pYs, final float[] pReuse) {
		final int n = pXs.length - 1;
		pReuse[Constants.VERTEX_INDEX_X] = 0;
		pReuse[Constants.VERTEX_INDEX_Y] = 0;
		for (int i = 0; i <= n; i++) {
			final float bernsteinPolynomial = BezierCurveUtils.getBernsteinPolynomial(t, i, n);
			pReuse[Constants.VERTEX_INDEX_X] += pXs[i] * bernsteinPolynomial;
			pReuse[Constants.VERTEX_INDEX_Y] += pYs[i] * bernsteinPolynomial;
		}
		return pReuse;
	}

	/**
	 * Calculates the length of a Bézier curve by taking {@link #LENGTH_SAMPLES_DEFAULT} samples.
	 *
	 * @param pXs x-coordinates of the control points of the Bézier curve.
	 * @param pYs y-coordinates of the control points of the Bézier curve.
	 */
	public static final float getBezierCurveLength(final float[] pXs, final float[] pYs) {
		return BezierCurveUtils.getBezierCurveLength(pXs, pYs, LENGTH_SAMPLES_DEFAULT);
	}

	/**
	 * Calculates the length of a Bézier curve by taking <code>pSamples</code> samples.
	 *
	 * @param pXs x-coordinates of the control points of the Bézier curve.
	 * @param pYs y-coordinates of the control points of the Bézier curve.
	 * @param pSamples the number of samples to take. The higher the more accurate.
	 * @return
	 */
	public static final float getBezierCurveLength(final float[] pXs, final float[] pYs, final int pSamples) {
		float length = 0;
		final int n = pXs.length - 1;

		float lastX = pXs[0];
		float lastY = pYs[0];
		for (int k = 1; k <= LENGTH_SAMPLES_DEFAULT; k++) {
			final float t = (1f * k) / LENGTH_SAMPLES_DEFAULT;

			float x = 0;
			float y = 0;
			for (int i = 0; i <= n; i++) {
				final float bernstein = BezierCurveUtils.getBernsteinPolynomial(t, i, n);
				x += pXs[i] * bernstein;
				y += pYs[i] * bernstein;
			}

			length += MathUtils.distance(lastX, lastY, x, y);

			lastX = x;
			lastY = y;
		}

		return length;
	}

	/**
	 * Calculates the curvature for the given control points of a Bézier curve at value <code>t</code>.
	 *
	 * @param t in the interval <code>[0, 1]</code>.
	 * @param pXs x-coordinates of the control points of the Bézier curve.
	 * @param pYs y-coordinates of the control points of the Bézier curve.
	 */
	public static final float getBezierCurveCurvature(final float t, final float[] pXs, final float[] pYs) {
		final float dxt = BezierCurveUtils.getBezierCurveDerivativeValue(t, 1, pXs);
		final float dyt = BezierCurveUtils.getBezierCurveDerivativeValue(t, 1, pYs);
		final float ddxt = BezierCurveUtils.getBezierCurveDerivativeValue(t, 2, pXs);
		final float ddyt = BezierCurveUtils.getBezierCurveDerivativeValue(t, 2, pYs);

		return BezierCurveUtils.getCurvature(dxt, dyt, ddxt, ddyt);
	}

	/**
	 * Calculates the value of the <code>k</code>th derivative of given control point (x|y|z|...)-coordinates of a Bézier curve at value <code>t</code>.
	 *
	 * @param t in the interval <code>[0, 1]</code>.
	 * @param k the <code>k</code>th derivative.
	 * @param pControlPointCoordinates (x|y|z|...)-coordinates of the Bézier curve.
	 * @return
	 */
	public static final float getBezierCurveDerivativeValue(final float t, final int k, final float[] pControlPointCoordinates) {
		final int n = pControlPointCoordinates.length - 1;
		float result = 1;
		for (int i = n; i > n - k; i--) {
			result *= i;
		}

		float sum = 0;
		for (int i = 0; i <= n - k; i++) {
			final float bernstein = BezierCurveUtils.getBernsteinPolynomial(t, i, n - k);
			final float d = BezierCurveUtils.getLevelDifference(i, k, pControlPointCoordinates);
			sum += bernstein * d;
		}

		result *= sum;

		return result;
	}


	private static float getCurvature(final float dxt, final float dyt, final float ddxt, final float ddyt) {
		return Math.abs(dxt * ddyt - dyt * ddxt) / (float) Math.pow(dxt * dxt + dyt * dyt, 3f / 2f);
	}

	private static final float getBernsteinPolynomial(final float x, final int i, final int n) {
		return ((float) MathUtils.factorial(n) / (float) (MathUtils.factorial(i) * MathUtils.factorial(n - i))) * (float) Math.pow(x, i) * (float) Math.pow(1 - x, n - i);
	}

	private static float getLevelDifference(final int i, final int level, final float[] pControlPointCoordinates) {
		if (level == 0) {
			return pControlPointCoordinates[i];
		} else {
			return BezierCurveUtils.getLevelDifference(i + 1, level - 1, pControlPointCoordinates) - BezierCurveUtils.getLevelDifference(i, level - 1, pControlPointCoordinates);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
