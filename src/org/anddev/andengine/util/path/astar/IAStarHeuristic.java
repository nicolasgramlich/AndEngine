package org.anddev.andengine.util.path.astar;

import org.anddev.andengine.util.path.ITiledMap;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:59:20 - 16.08.2010
 */
public interface IAStarHeuristic<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public float getExpectedRestCost(final ITiledMap<T> pTiledMap, final T pEntity, final int pFromTileColumn, final int pFromTileRow, final int pToTileColumn, final int pToTileRow);
}
