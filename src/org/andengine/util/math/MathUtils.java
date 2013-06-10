package org.andengine.util.math;

import java.util.Random;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 20:42:15 - 17.12.2009
 */
public final class MathUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final Random RANDOM = new Random(System.nanoTime());

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private MathUtils() {

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

	public static byte min(final byte pA, final byte pB, final byte pC) {
		return (byte) Math.min(Math.min(pA, pB), pC);
	}

	public static byte min(final byte pA, final byte pB, final byte pC, final byte pD) {
		return (byte) Math.min(Math.min(Math.min(pA, pB), pC), pD);
	}

	public static byte min(final byte pA, final byte pB, final byte pC, final byte pD, final byte pE) {
		return (byte) Math.min(Math.min(Math.min(Math.min(pA, pB), pC), pD), pE);
	}

	public static short min(final short pA, final short pB, final short pC) {
		return (short) Math.min(Math.min(pA, pB), pC);
	}

	public static short min(final short pA, final short pB, final short pC, final short pD) {
		return (short) Math.min(Math.min(Math.min(pA, pB), pC), pD);
	}

	public static short min(final short pA, final short pB, final short pC, final short pD, final short pE) {
		return (short) Math.min(Math.min(Math.min(Math.min(pA, pB), pC), pD), pE);
	}

	public static int min(final int pA, final int pB, final int pC) {
		return Math.min(Math.min(pA, pB), pC);
	}

	public static int min(final int pA, final int pB, final int pC, final int pD) {
		return Math.min(Math.min(Math.min(pA, pB), pC), pD);
	}

	public static int min(final int pA, final int pB, final int pC, final int pD, final int pE) {
		return Math.min(Math.min(Math.min(Math.min(pA, pB), pC), pD), pE);
	}

	public static long min(final long pA, final long pB, final long pC) {
		return Math.min(Math.min(pA, pB), pC);
	}

	public static long min(final long pA, final long pB, final long pC, final long pD) {
		return Math.min(Math.min(Math.min(pA, pB), pC), pD);
	}

	public static long min(final long pA, final long pB, final long pC, final long pD, final long pE) {
		return Math.min(Math.min(Math.min(Math.min(pA, pB), pC), pD), pE);
	}

	public static float min(final float pA, final float pB, final float pC) {
		return Math.min(Math.min(pA, pB), pC);
	}

	public static float min(final float pA, final float pB, final float pC, final float pD) {
		return Math.min(Math.min(Math.min(pA, pB), pC), pD);
	}

	public static float min(final float pA, final float pB, final float pC, final float pD, final float pE) {
		return Math.min(Math.min(Math.min(Math.min(pA, pB), pC), pD), pE);
	}

	public static double min(final double pA, final double pB, final double pC) {
		return Math.min(Math.min(pA, pB), pC);
	}

	public static double min(final double pA, final double pB, final double pC, final double pD) {
		return Math.min(Math.min(Math.min(pA, pB), pC), pD);
	}

	public static double min(final double pA, final double pB, final double pC, final double pD, final double pE) {
		return Math.min(Math.min(Math.min(Math.min(pA, pB), pC), pD), pE);
	}

	public static byte max(final byte pA, final byte pB, final byte pC) {
		return (byte) Math.max(Math.max(pA, pB), pC);
	}

	public static byte max(final byte pA, final byte pB, final byte pC, final byte pD) {
		return (byte) Math.max(Math.max(Math.max(pA, pB), pC), pD);
	}

	public static byte max(final byte pA, final byte pB, final byte pC, final byte pD, final byte pE) {
		return (byte) Math.max(Math.max(Math.max(Math.max(pA, pB), pC), pD), pE);
	}

	public static short max(final short pA, final short pB, final short pC) {
		return (short) Math.max(Math.max(pA, pB), pC);
	}

	public static short max(final short pA, final short pB, final short pC, final short pD) {
		return (short) Math.max(Math.max(Math.max(pA, pB), pC), pD);
	}

	public static short max(final short pA, final short pB, final short pC, final short pD, final short pE) {
		return (short) Math.max(Math.max(Math.max(Math.max(pA, pB), pC), pD), pE);
	}

	public static int max(final int pA, final int pB, final int pC) {
		return Math.max(Math.max(pA, pB), pC);
	}

	public static int max(final int pA, final int pB, final int pC, final int pD) {
		return Math.max(Math.max(Math.max(pA, pB), pC), pD);
	}

	public static int max(final int pA, final int pB, final int pC, final int pD, final int pE) {
		return Math.max(Math.max(Math.max(Math.max(pA, pB), pC), pD), pE);
	}

	public static long max(final long pA, final long pB, final long pC) {
		return Math.max(Math.max(pA, pB), pC);
	}

	public static long max(final long pA, final long pB, final long pC, final long pD) {
		return Math.max(Math.max(Math.max(pA, pB), pC), pD);
	}

	public static long max(final long pA, final long pB, final long pC, final long pD, final long pE) {
		return Math.max(Math.max(Math.max(Math.max(pA, pB), pC), pD), pE);
	}

	public static float max(final float pA, final float pB, final float pC) {
		return Math.max(Math.max(pA, pB), pC);
	}

	public static float max(final float pA, final float pB, final float pC, final float pD) {
		return Math.max(Math.max(Math.max(pA, pB), pC), pD);
	}

	public static float max(final float pA, final float pB, final float pC, final float pD, final float pE) {
		return Math.max(Math.max(Math.max(Math.max(pA, pB), pC), pD), pE);
	}

	public static double max(final double pA, final double pB, final double pC) {
		return Math.max(Math.max(pA, pB), pC);
	}

	public static double max(final double pA, final double pB, final double pC, final double pD) {
		return Math.max(Math.max(Math.max(pA, pB), pC), pD);
	}

	public static double max(final double pA, final double pB, final double pC, final double pD, final double pE) {
		return Math.max(Math.max(Math.max(Math.max(pA, pB), pC), pD), pE);
	}

	public static final float atan2(final float dY, final float dX) {
		return (float) Math.atan2(dY, dX);
	}

	public static final float radToDeg(final float pRad) {
		return MathConstants.RAD_TO_DEG * pRad;
	}

	public static final float degToRad(final float pDegree) {
		return MathConstants.DEG_TO_RAD * pDegree;
	}

	public static final int signum(final int n) {
		if (n == 0) {
			return 0;
		} else if (n > 0) {
			return 1;
		} else {
			return -1;
		}
	}

	public static final int randomSign() {
		if (MathUtils.RANDOM.nextBoolean()) {
			return 1;
		} else {
			return -1;
		}
	}

	public static final float random(final float pMin, final float pMax) {
		return pMin + (MathUtils.RANDOM.nextFloat() * (pMax - pMin));
	}

	/**
	 * Interval: <code>[pMin, pMax]</code>
	 *
	 * @param pMin inclusive!
	 * @param pMax inclusive!
	 * @return
	 */
	public static final int random(final int pMin, final int pMax) {
		return pMin + MathUtils.RANDOM.nextInt((pMax - pMin) + 1);
	}

	/**
	 * Interval: <code>[0, pMax)</code>
	 *
	 * @param pMax exclusive!
	 * @return
	 */
	public static final int random(final int pMax) {
		return MathUtils.RANDOM.nextInt(pMax);
	}

	public static final boolean isPowerOfTwo(final int n) {
		return ((n != 0) && ((n & (n - 1)) == 0));
	}

	public static final int nextPowerOfTwo(final float f) {
		return MathUtils.nextPowerOfTwo((int)(Math.ceil(f)));
	}

	public static final int nextPowerOfTwo(final int n) {
		int k = n;

		if (k == 0) {
			return 1;
		}

		k--;

		for (int i = 1; i < 32; i <<= 1) {
			k = k | (k >> i);
		}

		return k + 1;
	}

	public static final int sum(final int[] pValues) {
		int sum = 0;
		for (int i = pValues.length - 1; i >= 0; i--) {
			sum += pValues[i];
		}

		return sum;
	}

	public static float[] rotateAroundCenter(final float[] pVertices, final float pRotation, final float pRotationCenterX, final float pRotationCenterY) {
		if (pRotation != 0) {
			final float rotationRad = MathUtils.degToRad(pRotation);
			final float sinRotationRad = (float) Math.sin(rotationRad);
			final float cosRotationInRad = (float) Math.cos(rotationRad);

			for (int i = pVertices.length - 2; i >= 0; i -= 2) {
				final float pX = pVertices[i];
				final float pY = pVertices[i + 1];
				pVertices[i] = pRotationCenterX + ((cosRotationInRad * (pX - pRotationCenterX)) - (sinRotationRad * (pY - pRotationCenterY)));
				pVertices[i + 1] = pRotationCenterY + ((sinRotationRad * (pX - pRotationCenterX)) + (cosRotationInRad * (pY - pRotationCenterY)));
			}
		}
		return pVertices;
	}

	public static float[] scaleAroundCenter(final float[] pVertices, final float pScaleX, final float pScaleY, final float pScaleCenterX, final float pScaleCenterY) {
		if ((pScaleX != 1) || (pScaleY != 1)) {
			for (int i = pVertices.length - 2; i >= 0; i -= 2) {
				pVertices[i] = pScaleCenterX + ((pVertices[i] - pScaleCenterX) * pScaleX);
				pVertices[i + 1] = pScaleCenterY + ((pVertices[i + 1] - pScaleCenterY) * pScaleY);
			}
		}

		return pVertices;
	}

	public static float[] rotateAndScaleAroundCenter(final float[] pVertices, final float pRotation, final float pRotationCenterX, final float pRotationCenterY, final float pScaleX, final float pScaleY, final float pScaleCenterX, final float pScaleCenterY) {
		MathUtils.rotateAroundCenter(pVertices, pRotation, pRotationCenterX, pRotationCenterY);
		return MathUtils.scaleAroundCenter(pVertices, pScaleX, pScaleY, pScaleCenterX, pScaleCenterY);
	}

	public static float[] revertScaleAroundCenter(final float[] pVertices, final float pScaleX, final float pScaleY, final float pScaleCenterX, final float pScaleCenterY) {
		return MathUtils.scaleAroundCenter(pVertices, 1 / pScaleX, 1 / pScaleY, pScaleCenterX, pScaleCenterY);
	}

	public static float[] revertRotateAroundCenter(final float[] pVertices, final float pRotation, final float pRotationCenterX, final float pRotationCenterY) {
		return MathUtils.rotateAroundCenter(pVertices, -pRotation, pRotationCenterX, pRotationCenterY);
	}

	public static float[] revertRotateAndScaleAroundCenter(final float[] pVertices, final float pRotation, final float pRotationCenterX, final float pRotationCenterY, final float pScaleX, final float pScaleY, final float pScaleCenterX, final float pScaleCenterY) {
		MathUtils.revertScaleAroundCenter(pVertices, pScaleX, pScaleY, pScaleCenterX, pScaleCenterY);
		return MathUtils.revertRotateAroundCenter(pVertices, pRotation, pRotationCenterX, pRotationCenterY);
	}

	public static final boolean isInBounds(final int pMinValue, final int pMaxValue, final int pValue) {
		return (pValue >= pMinValue) && (pValue <= pMaxValue);
	}

	public static final boolean isInBounds(final float pMinValue, final float pMaxValue, final float pValue) {
		return (pValue >= pMinValue) && (pValue <= pMaxValue);
	}

	/**
	 * @param pMinValue inclusive!
	 * @param pMaxValue inclusive!
	 * @param pValue
	 * @return
	 */
	public static final int bringToBounds(final int pMinValue, final int pMaxValue, final int pValue) {
		return Math.max(pMinValue, Math.min(pMaxValue, pValue));
	}

	/**
	 * @param pMinValue inclusive!
	 * @param pMaxValue inclusive!
	 * @param pValue
	 * @return
	 */
	public static final float bringToBounds(final float pMinValue, final float pMaxValue, final float pValue) {
		return Math.max(pMinValue, Math.min(pMaxValue, pValue));
	}

	/**
	 * @return the euclidean distance between the points (pX1, pY1) and (pX2, pY2).
	 */
	public static final float distance(final float pX1, final float pY1, final float pX2, final float pY2) {
		final float dX = pX2 - pX1;
		final float dY = pY2 - pY1;
		return (float) Math.sqrt((dX * dX) + (dY * dY));
	}

	/**
	 * @return the euclidean distance between the origin (0, 0) and (pX, pY).
	 */
	public static final float length(final float pX, final float pY) {
		return (float) Math.sqrt((pX * pX) + (pY * pY));
	}

	/**
	 * @param pX
	 * @param pY
	 * @param pMix [0...1]
	 * @return pX * (1 - pMix) + pY * pMix
	 */
	public static final float mix(final float pX, final float pY, final float pMix) {
		return (pX * (1 - pMix)) + (pY * pMix);
	}

	/**
	 * @param pX
	 * @param pY
	 * @param pMix [0...1]
	 * @return (int)Math.round(pX * (1 - pMix) + pY * pMix)
	 */
	public static final int mix(final int pX, final int pY, final float pMix) {
		return Math.round((pX * (1 - pMix)) + (pY * pMix));
	}

	public static final boolean isEven(final int n) {
		return (n % 2) == 0;
	}

	public static final boolean isOdd(final int n) {
		return (n % 2) == 1;
	}

	public static float dot(final float pXA, final float pYA, final float pXB, final float pYB) {
		return (pXA * pXB) + (pYA * pYB);
	}

	public static float cross(final float pXA, final float pYA, final float pXB, final float pYB) {
		return (pXA * pYB) - (pXB * pYA);
	}

	public static final int factorial(final int n) {
		int result = 1;
		for (int i = 1; i <= n; i++) {
			result *= i;
		}
		return result;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
