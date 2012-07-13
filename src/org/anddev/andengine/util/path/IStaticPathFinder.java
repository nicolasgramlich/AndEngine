package org.anddev.andengine.util.path;

import org.anddev.andengine.util.path.floyd_warshall.NegativeCycleException;

/**
 * interface for static (i.e. no cost or blocking information changes) path finder
 * 
 * @author <a href="https://github.com/winniehell/">winniehell</a>
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
