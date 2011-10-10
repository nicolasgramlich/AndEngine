package org.anddev.andengine.util.spatial.quadtree;

import org.anddev.andengine.util.spatial.ISpatialItem;
import org.anddev.andengine.util.spatial.adt.bounds.FloatBounds;
import org.anddev.andengine.util.spatial.adt.bounds.source.IFloatBoundsSource;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:15:21 - 10.10.2011
 */
public class FloatQuadTree<T extends ISpatialItem<IFloatBoundsSource>> extends QuadTree<IFloatBoundsSource, FloatBounds, T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public FloatQuadTree(final FloatBounds pFloatBounds) {
		super(pFloatBounds);
	}

	public FloatQuadTree(final FloatBounds pFloatBounds, final int pMaxLevel) {
		super(pFloatBounds, pMaxLevel);
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
