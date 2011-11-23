package org.andengine.util.path.astar;

import org.andengine.util.path.IPathFinderMap;

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
	
	private final float mFactor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ManhattanHeuristic() {
		this(1);
	}

	public ManhattanHeuristic(final float pFactor) {
		this.mFactor = pFactor;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getExpectedRestCost(final IPathFinderMap<T> pPathFinderMap, final T pEntity, final int pFromX, final int pFromY, final int pToX, final int pToY) {
		return this.mFactor * (Math.abs(pFromX - pToX) + Math.abs(pToX - pToY));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
