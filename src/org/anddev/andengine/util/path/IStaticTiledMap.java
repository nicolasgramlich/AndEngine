package org.anddev.andengine.util.path;

/**
 * interface for static (i.e. no cost or blocking information changes) tiled maps
 * 
 * @author <a href="https://github.com/winniehell/">winniehell</a>
 */
public interface IStaticTiledMap {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public int getTileColumns();
	public int getTileRows();

	public void onTileVisitedByPathFinder(final int pTileColumn, int pTileRow);

	public boolean isTileBlocked(final int pTileColumn, final int pTileRow);

	public float getStepCost(final int pFromTileColumn, final int pFromTileRow, final int pToTileColumn, final int pToTileRow);
}
