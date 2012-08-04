package org.andengine.util.algorithm.path;

import org.andengine.util.algorithm.path.astar.IAStarHeuristic;

/**
 * (c) winniehell (2012)
 *
 * @author <a href="https://github.com/winniehell/">winniehell</a>
 * @since 22:57:13 - 16.08.2010
 */
public interface IBasePathFinder<TEntity, TPath> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public TPath findPath(final IPathFinderMap<TEntity> pPathFinderMap,
	                      final PathFinderOptions pOptions, final TEntity pEntity,
	                      final int pFromX, final int pFromY,
	                      final int pToX, final int pToY,
	                      final IAStarHeuristic<TEntity> pAStarHeuristic,
	                      final ICostFunction<TEntity> pCostFunction);

	public TPath findPath(final IPathFinderMap<TEntity> pPathFinderMap,
	                      final PathFinderOptions pOptions, final TEntity pEntity,
	                      final int pFromX, final int pFromY,
	                      final int pToX, final int pToY,
	                      final IAStarHeuristic<TEntity> pAStarHeuristic,
	                      final ICostFunction<TEntity> pCostFunction,
	                      final IPathFinderListener<TEntity> pPathFinderListener);

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
	
	public interface IPathFinderListener<TEntity> {
		// ===========================================================
		// Constants
		// ===========================================================
	
		// ===========================================================
		// Fields
		// ===========================================================
	
		public void onVisited(final TEntity pEntity, final int pX, final int pY);
	}
}
