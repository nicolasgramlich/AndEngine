package org.anddev.andengine.util.spatial.quadtree;

import org.anddev.andengine.util.spatial.ISpatialItem;
import org.anddev.andengine.util.spatial.adt.bounds.IntBounds;
import org.anddev.andengine.util.spatial.adt.bounds.source.IIntBoundsSource;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:22:18 - 10.10.2011
 */
public class IntQuadTree<T extends ISpatialItem<IIntBoundsSource>> extends QuadTree<IIntBoundsSource, IntBounds, T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public IntQuadTree(final IntBounds pIntBounds) {
		super(pIntBounds);
	}

	public IntQuadTree(final IntBounds pIntBounds, final int pMaxLevel) {
		super(pIntBounds, pMaxLevel);
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
