package org.anddev.andengine.util.spatial.adt.bounds.util;

import org.anddev.andengine.util.spatial.adt.bounds.IFloatBounds;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:12:02 - 07.10.2011
 */
public class FloatBoundsUtils {
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

	public static boolean intersects(final float pLeftA, final float pRightA, final float pTopA, final float pBottomA, final float pLeftB, final float pRightB, final float pTopB, final float pBottomB) {
		return (pLeftA < pRightB) && (pLeftB < pRightA) && (pTopA < pBottomB) && (pTopB < pBottomA);
	}

	public static boolean intersects(final IFloatBounds pFloatBoundsA, final IFloatBounds pFloatBoundsB) {
		return FloatBoundsUtils.intersects(pFloatBoundsA.getLeft(), pFloatBoundsA.getRight(), pFloatBoundsA.getTop(), pFloatBoundsA.getBottom(), pFloatBoundsB.getLeft(), pFloatBoundsB.getRight(), pFloatBoundsB.getTop(), pFloatBoundsB.getBottom());
	}

	public static boolean contains(final float pLeftA, final float pRightA, final float pTopA, final float pBottomA, final float pLeftB, final float pRightB, final float pTopB, final float pBottomB) {
		return (pLeftA <= pLeftB) && (pTopA <= pTopB) && (pRightA >= pRightB) && (pBottomA >= pBottomB);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
