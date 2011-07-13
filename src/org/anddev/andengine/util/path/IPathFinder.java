package org.anddev.andengine.util.path;

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

	public Path findPath(final T pEntity, final int pMaxCost, final int pFromTileColumn, final int pFromTileRow, final int pToTileColumn, final int pToTileRow);
}
