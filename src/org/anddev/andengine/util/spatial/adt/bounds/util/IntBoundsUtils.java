package org.anddev.andengine.util.spatial.adt.bounds.util;

import org.anddev.andengine.util.spatial.adt.bounds.IIntBounds;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:05:32 - 08.10.2011
 */
public class IntBoundsUtils {
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
	
	public static boolean intersects(final IIntBounds pIntBoundsA, final IIntBounds pIntBoundsB) {
		return IntBoundsUtils.intersects(pIntBoundsA.getMinX(), pIntBoundsA.getMinY(), pIntBoundsA.getMaxX(), pIntBoundsA.getMaxY(), pIntBoundsB.getMinX(), pIntBoundsB.getMinY(), pIntBoundsB.getMaxX(), pIntBoundsB.getMaxY());
	}

	public static boolean intersects(final int pMinXA, final int pMinYA, final int pMaxXA, final int pMaxYA, final int pMinXB, final int pMinYB, final int pMaxXB, final int pMaxYB) {
		return (pMinXA < pMaxXB) && (pMinXB < pMaxXA) && (pMinYA < pMaxYB) && (pMinYB < pMaxYA);
	}
	
	public static boolean contains(final IIntBounds pIntBoundsA, final IIntBounds pIntBoundsB) {
		return IntBoundsUtils.contains(pIntBoundsA.getMinX(), pIntBoundsA.getMinY(), pIntBoundsA.getMaxX(), pIntBoundsA.getMaxY(), pIntBoundsB.getMinX(), pIntBoundsB.getMinY(), pIntBoundsB.getMaxX(), pIntBoundsB.getMaxY());
	}

	public static boolean contains(final int pMinXA, final int pMinYA, final int pMaxXA, final int pMaxYA, final int pMinXB, final int pMinYB, final int pMaxXB, final int pMaxYB) {
		return (pMinXA <= pMinXB) && (pMinYA <= pMinYB) && (pMaxXA >= pMaxXB) && (pMaxYA >= pMaxYB);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
