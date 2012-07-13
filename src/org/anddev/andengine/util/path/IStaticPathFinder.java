package org.anddev.andengine.util.path;

import org.anddev.andengine.util.path.floyd_warshall.NegativeCycleException;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:57:13 - 16.08.2010
 */
public interface IStaticPathFinder<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public WeightedPath findPath(
		final float pMaxCost,
		final int pFromTileColumn, final int pFromTileRow,
		final int pToTileColumn, final int pToTileRow
	);
	
	public void initialize(IStaticTiledMap pMap) 
	            throws NegativeCycleException;
}
