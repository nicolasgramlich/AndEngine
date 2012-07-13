package org.anddev.andengine.util.path;

import org.anddev.andengine.util.path.floyd_warshall.NegativeCycleException;

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
