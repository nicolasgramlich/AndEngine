package org.andengine.util;

import org.andengine.util.math.MathUtils;

/**
 * (c) 2013 Nicolas Gramlich
 * 
 * @author Nicolas Gramlich
 * @since 23:09:15 - 01.06.2013
 */
public final class BezierCurveUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int LENGTH_SAMPLES = 10;

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
	 * Calculates the (x|y|z|...)-coordinate for the given control point (x|y|z|...)-coordinates. 
	 *
	 * @param t in the interval <code>[0, 1]</code>.
	 * @param pControlPointCoordinates (x|y|z|...)-coordinates of the bezier curve.
	 * @return
	 */
	public static final float getBezierCurveCoordinate(final float t, final float[] pControlPointCoordinates) {
		float result = 0;
		final int n = pControlPointCoordinates.length - 1;
		for (int i = 0; i <= n; i++) {
			result += pControlPointCoordinates[i] * BezierCurveUtils.bernstein(t, i, n);
		}
		return result;
	}

	/**
	 * Calculates the length of a bezier curve by taking {@link #LENGTH_SAMPLES} samples.
	 */
	public static final float getBezierCurveLength(final float[] pXs, final float[] pYs) {
		return BezierCurveUtils.getBezierCurveLength(pXs, pYs, LENGTH_SAMPLES);
	}

	/**
	 * Calculates the length of a bezier curve by taking <code>pSamples</code> samples.
	 */
	public static final float getBezierCurveLength(final float[] pXs, final float[] pYs, final int pSamples) {
		float length = 0;
		final int n = pXs.length - 1;

		float lastX = pXs[0];
		float lastY = pYs[0];
		for (int k = 1; k <= LENGTH_SAMPLES; k++) {
			final float t = (1f * k) / LENGTH_SAMPLES;

			float x = 0;
			float y = 0;
			for (int i = 0; i <= n; i++) {
				x += pXs[i] * BezierCurveUtils.bernstein(t, i, n);
				y += pYs[i] * BezierCurveUtils.bernstein(t, i, n);
			}

			length += MathUtils.distance(lastX, lastY, x, y);

			lastX = x;
			lastY = y;
		}

		return length;
	}

	/**
	 * @see <a href="http://en.wikipedia.org/wiki/Bernstein_polynomial">wikipedia.org/wiki/Bernstein_polynomial</a> 
	 */
	private static final float bernstein(final float t, final int i, final int n) {
		return ((float) MathUtils.factorial(n) / (float) (MathUtils.factorial(i) * MathUtils.factorial(n - i))) * (float) Math.pow(t, i) * (float) Math.pow(1 - t, n - i);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
