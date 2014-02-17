package org.andengine.util.adt.spatial.bounds.util;

import org.andengine.util.adt.bounds.IFloatBounds;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:12:02 - 07.10.2011
 */
public final class FloatBoundsUtils {
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
	
	public static final boolean intersects(final IFloatBounds pFloatBoundsA, final IFloatBounds pFloatBoundsB) {
		return FloatBoundsUtils.intersects(pFloatBoundsA.getXMin(), pFloatBoundsA.getYMin(), pFloatBoundsA.getXMax(), pFloatBoundsA.getYMax(), pFloatBoundsB.getXMin(), pFloatBoundsB.getYMin(), pFloatBoundsB.getXMax(), pFloatBoundsB.getYMax());
	}

	public static final boolean intersects(final float pXMinA, final float pYMinA, final float pXMaxA, final float pYMaxA, final float pXMinB, final float pYMinB, final float pXMaxB, final float pYMaxB) {
		return (pXMinA < pXMaxB) && (pXMinB < pXMaxA) && (pYMinA < pYMaxB) && (pYMinB < pYMaxA)
				|| FloatBoundsUtils.contains(pXMinA, pYMinA, pXMaxA, pYMaxA, pXMinB, pYMinB, pXMaxB, pYMaxB)
				|| FloatBoundsUtils.contains(pXMinB, pYMinB, pXMaxB, pYMaxB, pXMinA, pYMinA, pXMaxA, pYMaxA);
	}

	public static final boolean contains(final IFloatBounds pFloatBounds, final float pX, final float pY) {
		return FloatBoundsUtils.contains(pFloatBounds.getXMin(), pFloatBounds.getYMin(), pFloatBounds.getXMax(), pFloatBounds.getYMax(), pX, pY);
	}
	
	public static final boolean contains(final IFloatBounds pFloatBoundsA, final IFloatBounds pFloatBoundsB) {
		return FloatBoundsUtils.contains(pFloatBoundsA.getXMin(), pFloatBoundsA.getYMin(), pFloatBoundsA.getXMax(), pFloatBoundsA.getYMax(), pFloatBoundsB.getXMin(), pFloatBoundsB.getYMin(), pFloatBoundsB.getXMax(), pFloatBoundsB.getYMax());
	}

	public static final boolean contains(final IFloatBounds pFloatBounds, final float pXMin, final float pYMin, final float pXMax, final float pYMax) {
		return FloatBoundsUtils.contains(pFloatBounds.getXMin(), pFloatBounds.getYMin(), pFloatBounds.getXMax(), pFloatBounds.getYMax(), pXMin, pYMin, pXMax, pYMax);
	}

	public static final boolean contains(final float pXMin, final float pYMin, final float pXMax, final float pYMax, final float pX, final float pY) {
		return (pXMin <= pX) && (pYMin <= pY) && (pXMax >= pX) && (pYMax >= pY);
	}

	public static final boolean contains(final float pXMinA, final float pYMinA, final float pXMaxA, final float pYMaxA, final float pXMinB, final float pYMinB, final float pXMaxB, final float pYMaxB) {
		return (pXMinA <= pXMinB) && (pYMinA <= pYMinB) && (pXMaxA >= pXMaxB) && (pYMaxA >= pYMaxB);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
