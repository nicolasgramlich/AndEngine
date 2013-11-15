package org.anddev.andengine.util.path.astar;

import org.anddev.andengine.util.path.ITiledMap;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:58:01 - 16.08.2010
 */
public class ManhattanHeuristic<T> implements IAStarHeuristic<T> {
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

	@Override
	public float getExpectedRestCost(final ITiledMap<T> pTiledMap, final T pEntity, final int pTileFromX, final int pTileFromY, final int pTileToX, final int pTileToY) {
		return Math.abs(pTileFromX - pTileToX) + Math.abs(pTileFromY - pTileToY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
