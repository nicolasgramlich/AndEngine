package org.andengine.util.path;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:57:13 - 16.08.2010
 */
public interface IPathFinder<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public Path findPath(final int pFromX, final int pFromY, final int pToX, final int pToY, final T pEntity, final IPathFinderMap<T> pPathFinderMap, final ICostFunction<T> pCostFunction);
	public Path findPath(final int pFromX, final int pFromY, final int pToX, final int pToY, final T pEntity, final IPathFinderMap<T> pPathFinderMap, final ICostFunction<T> pCostFunction, final float pMaxCost);
	public Path findPath(final int pFromX, final int pFromY, final int pToX, final int pToY, final T pEntity, final IPathFinderMap<T> pPathFinderMap, final ICostFunction<T> pCostFunction, final float pMaxCost, final IPathFinderListener<T> pPathFinderListener);

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
	
	public interface IPathFinderListener<T> {
		// ===========================================================
		// Constants
		// ===========================================================
	
		// ===========================================================
		// Fields
		// ===========================================================
	
		public void onVisited(final T pEntity, final int pX, final int pY);
	}
}
