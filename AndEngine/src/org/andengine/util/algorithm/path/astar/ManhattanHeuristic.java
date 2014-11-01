package org.andengine.util.algorithm.path.astar;

import org.andengine.util.algorithm.path.IPathFinderMap;

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
	public float getExpectedRestCost(final IPathFinderMap<T> pPathFinderMap, final T pEntity, final int pFromX, final int pFromY, final int pToX, final int pToY) {
		return Math.abs(pFromX - pToX) + Math.abs(pFromY - pToY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
