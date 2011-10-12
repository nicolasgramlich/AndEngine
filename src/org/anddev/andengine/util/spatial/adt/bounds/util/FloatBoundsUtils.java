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
	
	public static boolean intersects(final IFloatBounds pFloatBoundsA, final IFloatBounds pFloatBoundsB) {
		return FloatBoundsUtils.intersects(pFloatBoundsA.getMinX(), pFloatBoundsA.getMinY(), pFloatBoundsA.getMaxX(), pFloatBoundsA.getMaxY(), pFloatBoundsB.getMinX(), pFloatBoundsB.getMinY(), pFloatBoundsB.getMaxX(), pFloatBoundsB.getMaxY());
	}

	public static boolean intersects(final float pMinXA, final float pMinYA, final float pMaxXA, final float pMaxYA, final float pMinXB, final float pMinYB, final float pMaxXB, final float pMaxYB) {
		return (pMinXA < pMaxXB) && (pMinXB < pMaxXA) && (pMinYA < pMaxYB) && (pMinYB < pMaxYA);
	}

	public static boolean contains(final IFloatBounds pFloatBoundsA, final IFloatBounds pFloatBoundsB) {
		return FloatBoundsUtils.intersects(pFloatBoundsA.getMinX(), pFloatBoundsA.getMinY(), pFloatBoundsA.getMaxX(), pFloatBoundsA.getMaxY(), pFloatBoundsB.getMinX(), pFloatBoundsB.getMinY(), pFloatBoundsB.getMaxX(), pFloatBoundsB.getMaxY());
	}

	public static boolean contains(final float pMinXA, final float pMinYA, final float pMaxXA, final float pMaxYA, final float pMinXB, final float pMinYB, final float pMaxXB, final float pMaxYB) {
		return (pMinXA <= pMinXB) && (pMinYA <= pMinYB) && (pMaxXA >= pMaxXB) && (pMaxYA >= pMaxYB);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
