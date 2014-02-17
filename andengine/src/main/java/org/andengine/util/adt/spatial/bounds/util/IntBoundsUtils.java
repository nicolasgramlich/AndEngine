package org.andengine.util.adt.spatial.bounds.util;

import org.andengine.util.adt.bounds.IIntBounds;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:05:32 - 08.10.2011
 */
public final class IntBoundsUtils {
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
	
	public static final boolean intersects(final IIntBounds pIntBoundsA, final IIntBounds pIntBoundsB) {
		return IntBoundsUtils.intersects(pIntBoundsA.getXMin(), pIntBoundsA.getYMin(), pIntBoundsA.getXMax(), pIntBoundsA.getYMax(), pIntBoundsB.getXMin(), pIntBoundsB.getYMin(), pIntBoundsB.getXMax(), pIntBoundsB.getYMax());
	}

	public static final boolean intersects(final int pXMinA, final int pYMinA, final int pXMaxA, final int pYMaxA, final int pXMinB, final int pYMinB, final int pXMaxB, final int pYMaxB) {
		return ((pXMinA < pXMaxB) && (pXMinB < pXMaxA) && (pYMinA < pYMaxB) && (pYMinB < pYMaxA))
				|| IntBoundsUtils.contains(pXMinA, pYMinA, pXMaxA, pYMaxA, pXMinB, pYMinB, pXMaxB, pYMaxB)
				|| IntBoundsUtils.contains(pXMinB, pYMinB, pXMaxB, pYMaxB, pXMinA, pYMinA, pXMaxA, pYMaxA);
	}
	
	public static final boolean contains(final IIntBounds pIntBoundsA, final IIntBounds pIntBoundsB) {
		return IntBoundsUtils.contains(pIntBoundsA.getXMin(), pIntBoundsA.getYMin(), pIntBoundsA.getXMax(), pIntBoundsA.getYMax(), pIntBoundsB.getXMin(), pIntBoundsB.getYMin(), pIntBoundsB.getXMax(), pIntBoundsB.getYMax());
	}
	
	public static final boolean contains(final IIntBounds pIntBounds, final int pX, final int pY) {
		return IntBoundsUtils.contains(pIntBounds.getXMin(), pIntBounds.getYMin(), pIntBounds.getXMax(), pIntBounds.getYMax(), pX, pY);
	}
	
	public static final boolean contains(final IIntBounds pIntBounds, final int pXMin, final int pYMin, final int pXMax, final int pYMax) {
		return IntBoundsUtils.contains(pIntBounds.getXMin(), pIntBounds.getYMin(), pIntBounds.getXMax(), pIntBounds.getYMax(), pXMin, pYMin, pXMax, pYMax);
	}

	public static final boolean contains(final int pXMin, final int pYMin, final int pXMax, final int pYMax, final int pX, final int pY) {
		return (pXMin <= pX) && (pYMin <= pY) && (pXMax >= pX) && (pYMax >= pY);
	}

	public static final boolean contains(final int pXMinA, final int pYMinA, final int pXMaxA, final int pYMaxA, final int pXMinB, final int pYMinB, final int pXMaxB, final int pYMaxB) {
		return (pXMinA <= pXMinB) && (pYMinA <= pYMinB) && (pXMaxA >= pXMaxB) && (pYMaxA >= pYMaxB);
	}

	public static final boolean adjacent(final IIntBounds pIntBoundsA, final IIntBounds pIntBoundsB) {
		return IntBoundsUtils.adjacent(pIntBoundsA.getXMin(), pIntBoundsA.getYMin(), pIntBoundsA.getXMax(), pIntBoundsA.getYMax(), pIntBoundsB.getXMin(), pIntBoundsB.getYMin(), pIntBoundsB.getXMax(), pIntBoundsB.getYMax());
	}

	public static final boolean adjacent(final int pXMinA, final int pYMinA, final int pXMaxA, final int pYMaxA, final int pXMinB, final int pYMinB, final int pXMaxB, final int pYMaxB) {
		final int width = Math.min(pXMaxA, pXMaxB) - Math.max(pXMinA, pXMinB);
		final int height = Math.min(pYMaxA, pYMaxB) - Math.max(pYMinA, pYMinB);

		return (width == 0 && height > 0) || (height == 0 && width > 0);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
