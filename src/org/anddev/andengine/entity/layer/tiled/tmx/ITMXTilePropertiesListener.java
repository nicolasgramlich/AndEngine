package org.anddev.andengine.entity.layer.tiled.tmx;

import java.util.ArrayList;

/**
 * @author Nicolas Gramlich
 * @since 10:11:41 - 27.07.2010
 */
public interface ITMXTilePropertiesListener {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final ArrayList<TMXTileProperty> pTMXProperties, final int pTileRow, final int pTileColumn, final int pTileWidth, final int pTileHeight);
}
