package org.anddev.andengine.util.path;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 23:00:24 - 16.08.2010
 */
public interface ITiledMap<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public int getTileColumns();
	public int getTileRows();

	public void onTileVisitedByPathFinder(final int pTileColumn, int pTileRow);

	public boolean isTileBlocked(final T pEntity, final int pTileColumn, final int pTileRow);

	public float getStepCost(final T pEntity, final int pFromTileColumn, final int pFromTileRow, final int pToTileColumn, final int pToTileRow);
}
