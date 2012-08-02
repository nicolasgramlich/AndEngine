package org.andengine.util.algorithm.path;

import org.andengine.util.adt.spatial.bounds.util.IntBoundsUtils;

/**
 * (c) winniehell (2012)
 *
 * @author <a href="https://github.com/winniehell/">winniehell</a>
 * @since 2012-07-{27}  19:54
 */
public class PathFinderOptions {
  // ===========================================================
  // Constants
  // ===========================================================

  // ===========================================================
  // Fields
  // ===========================================================

	private int mXMin;
	private int mYMin;
	private int mXMax;
	private int mYMax;

	private boolean mAllowDiagonal;
	private float mMaxCost;

  // ===========================================================
  // Constructors
  // ===========================================================

	/**
	 * default constructor
	 *
	 * @param pXMin lower x-bound
	 * @param pYMin lower y-bound
	 * @param pXMax upper x-bound
	 * @param pYMax upper y-bound
	 * @param pAllowDiagonal allow diagonal movement
	 * @param pMaxCost maximum path cost
	 */
	public PathFinderOptions(final int pXMin, final int pYMin, final int pXMax, final int pYMax,
	                         final boolean pAllowDiagonal, final float pMaxCost) {
		this.mXMin = pXMin;
		this.mYMin = pYMin;
		this.mXMax = pXMax;
		this.mYMax = pYMax;

		this.mAllowDiagonal = pAllowDiagonal;
		this.mMaxCost = pMaxCost;
	}

	/**
	 * set maximum path cost to infinity
	 */
	public PathFinderOptions(final int pXMin, final int pYMin, final int pXMax, final int pYMax,
	                         final boolean pAllowDiagonal) {
		this(pXMin, pYMin, pXMax, pYMax, pAllowDiagonal, Float.POSITIVE_INFINITY);
	}

	/**
	 * set no bounds
	 */
	public PathFinderOptions(final boolean pAllowDiagonal, final float pMaxCost) {
		this(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, pAllowDiagonal, pMaxCost);
	}

	/**
	 * set no bounds and set maximum path cost to infinity
	 */
	public PathFinderOptions(final boolean pAllowDiagonal) {
		this(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, pAllowDiagonal, Float.POSITIVE_INFINITY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean allowDiagonalMovement() {
		return mAllowDiagonal;
	}

	public float getMaxCost() {
		return mMaxCost;
	}

	public int getXMin() {
		return mXMin;
	}

	public int getYMin() {
		return mYMin;
	}

	public int getXMax() {
		return mXMax;
	}

	public int getYMax() {
		return mYMax;
	}

	public void setBounds(final int pXMin, final int pYMin, final int pXMax, final int pYMax) {
		this.mXMin = pXMin;
		this.mYMin = pYMin;
		this.mXMax = pXMax;
		this.mYMax = pYMax;
	}

	public void setDiagonalMovement(final boolean pAllow) {
		this.mAllowDiagonal = pAllow;
	}

	public void setMaxCost(float mMaxCost) {
		this.mMaxCost = mMaxCost;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean inBounds(final int pX, final int pY) {
		return IntBoundsUtils.contains(mXMin, mYMin, mXMax, mYMax, pX, pY);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
