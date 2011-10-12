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
		return IntBoundsUtils.intersects(pIntBoundsA.getLeft(), pIntBoundsA.getTop(), pIntBoundsA.getRight(), pIntBoundsA.getBottom(), pIntBoundsB.getLeft(), pIntBoundsB.getTop(), pIntBoundsB.getRight(), pIntBoundsB.getBottom());
	}

	public static boolean intersects(final int pLeftA, final int pTopA, final int pRightA, final int pBottomA, final int pLeftB, final int pTopB, final int pRightB, final int pBottomB) {
		return (pLeftA < pRightB) && (pLeftB < pRightA) && (pTopA < pBottomB) && (pTopB < pBottomA);
	}
	
	public static boolean contains(final IIntBounds pIntBoundsA, final IIntBounds pIntBoundsB) {
		return IntBoundsUtils.contains(pIntBoundsA.getLeft(), pIntBoundsA.getTop(), pIntBoundsA.getRight(), pIntBoundsA.getBottom(), pIntBoundsB.getLeft(), pIntBoundsB.getTop(), pIntBoundsB.getRight(), pIntBoundsB.getBottom());
	}

	public static boolean contains(final int pLeftA, final int pTopA, final int pRightA, final int pBottomA, final int pLeftB, final int pTopB, final int pRightB, final int pBottomB) {
		return (pLeftA <= pLeftB) && (pTopA <= pTopB) && (pRightA >= pRightB) && (pBottomA >= pBottomB);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
