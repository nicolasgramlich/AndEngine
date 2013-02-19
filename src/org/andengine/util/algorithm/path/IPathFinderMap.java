package org.andengine.util.algorithm.path;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 23:00:24 - 16.08.2010
 */
public interface IPathFinderMap<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public boolean isBlocked(final int pX, final int pY, final T pEntity);
}
