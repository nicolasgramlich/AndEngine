package org.anddev.andengine.entity.layer.tiled.tmx;

import java.util.ArrayList;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.xml.sax.Attributes;

/**
 * @author Nicolas Gramlich
 * @since 19:38:11 - 20.07.2010
 */
public class TMXTiledMap implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mOrientation;
	private final int mWidth;
	private final int mHeight;
	private final int mTileWidth;
	private final int mTileHeight;
	private final ArrayList<TMXTileSet> mTMXTileSets = new ArrayList<TMXTileSet>();
	private final ArrayList<TMXLayer> mTMXLayers = new ArrayList<TMXLayer>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXTiledMap(final Attributes pAttributes) {
		this.mOrientation = pAttributes.getValue("", TAG_MAP_ATTRIBUTE_ORIENTATION);
		if(this.mOrientation.equals(TAG_MAP_ATTRIBUTE_ORIENTATION_VALUE_ORTHOGONAL) == false) {
			throw new IllegalArgumentException(TAG_MAP_ATTRIBUTE_ORIENTATION + ": '" + this.mOrientation + "' is not supported.");
		}
		this.mWidth = Integer.parseInt(pAttributes.getValue("", TAG_MAP_ATTRIBUTE_WIDTH));
		this.mHeight = Integer.parseInt(pAttributes.getValue("", TAG_MAP_ATTRIBUTE_HEIGHT));
		this.mTileWidth = Integer.parseInt(pAttributes.getValue("", TAG_MAP_ATTRIBUTE_TILEWIDTH));
		this.mTileHeight = Integer.parseInt(pAttributes.getValue("", TAG_MAP_ATTRIBUTE_TILEHEIGHT));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final String getOrientation() {
		return this.mOrientation;
	}

	public final int getWidth() {
		return this.mWidth;
	}

	public final int getHeight() {
		return this.mHeight;
	}

	public final int getTileWidth() {
		return this.mTileWidth;
	}

	public final int getTileHeight() {
		return this.mTileHeight;
	}

	public ArrayList<TMXTileSet> getTMXTileSets() {
		return this.mTMXTileSets;
	}

	void addTMXTileSet(final TMXTileSet pTMXTileSet) {
		this.mTMXTileSets.add(pTMXTileSet);
	}
	
	public ArrayList<TMXLayer> getTMXLayers() {
		return this.mTMXLayers;
	}

	void addTMXLayer(final TMXLayer pTMXLayer) {
		this.mTMXLayers.add(pTMXLayer);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public Object build(final TextureManager pTextureManager) {
		
		return null;
	}

	public TextureRegion getTextureRegionFromGlobalTileID(final int pGlobalTileID) {
		return null;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
