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
		return IntBoundsUtils.intersects(pIntBoundsA.getXMin(), pIntBoundsA.getYMin(), pIntBoundsA.getXMax(), pIntBoundsA.getYMax(), pIntBoundsB.getXMin(), pIntBoundsB.getYMin(), pIntBoundsB.getXMax(), pIntBoundsB.getYMax());
	}

	public static boolean intersects(final int pXMinA, final int pYMinA, final int pXMaxA, final int pYMaxA, final int pXMinB, final int pYMinB, final int pXMaxB, final int pYMaxB) {
		return (pXMinA < pXMaxB) && (pXMinB < pXMaxA) && (pYMinA < pYMaxB) && (pYMinB < pYMaxA);
	}
	
	public static boolean contains(final IIntBounds pIntBoundsA, final IIntBounds pIntBoundsB) {
		return IntBoundsUtils.contains(pIntBoundsA.getXMin(), pIntBoundsA.getYMin(), pIntBoundsA.getXMax(), pIntBoundsA.getYMax(), pIntBoundsB.getXMin(), pIntBoundsB.getYMin(), pIntBoundsB.getXMax(), pIntBoundsB.getYMax());
	}

	public static boolean contains(final int pXMinA, final int pYMinA, final int pXMaxA, final int pYMaxA, final int pXMinB, final int pYMinB, final int pXMaxB, final int pYMaxB) {
		return (pXMinA <= pXMinB) && (pYMinA <= pYMinB) && (pXMaxA >= pXMaxB) && (pYMaxA >= pYMaxB);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
