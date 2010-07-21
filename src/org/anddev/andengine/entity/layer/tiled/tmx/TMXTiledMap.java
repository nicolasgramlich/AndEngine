package org.anddev.andengine.entity.layer.tiled.tmx;

import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;
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
	private final int mTilesHorizontal;
	private final int mTilesVertical;
	private final int mTileWidth;
	private final int mTileHeight;
	
	private final ArrayList<TMXTileSet> mTMXTileSets = new ArrayList<TMXTileSet>();
	private final ArrayList<TMXLayer> mTMXLayers = new ArrayList<TMXLayer>();

	private final RectangleVertexBuffer mSharedVertexBuffer;

	private HashMap<Integer, TextureRegion> mTextureRegionFromGLobalTileIDCache = new HashMap<Integer, TextureRegion>();

	// ===========================================================
	// Constructors
	// ===========================================================

	TMXTiledMap(final Attributes pAttributes) {
		this.mOrientation = pAttributes.getValue("", TAG_MAP_ATTRIBUTE_ORIENTATION);
		if(this.mOrientation.equals(TAG_MAP_ATTRIBUTE_ORIENTATION_VALUE_ORTHOGONAL) == false) {
			throw new IllegalArgumentException(TAG_MAP_ATTRIBUTE_ORIENTATION + ": '" + this.mOrientation + "' is not supported.");
		}
		this.mTilesHorizontal = Integer.parseInt(pAttributes.getValue("", TAG_MAP_ATTRIBUTE_WIDTH));
		this.mTilesVertical = Integer.parseInt(pAttributes.getValue("", TAG_MAP_ATTRIBUTE_HEIGHT));
		this.mTileWidth = Integer.parseInt(pAttributes.getValue("", TAG_MAP_ATTRIBUTE_TILEWIDTH));
		this.mTileHeight = Integer.parseInt(pAttributes.getValue("", TAG_MAP_ATTRIBUTE_TILEHEIGHT));

		this.mSharedVertexBuffer = new RectangleVertexBuffer(GL11.GL_STATIC_DRAW);
		BufferObjectManager.getActiveInstance().loadBufferObject(this.mSharedVertexBuffer);
		this.mSharedVertexBuffer.onUpdate(0, 0, this.mTileWidth, this.mTileHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final String getOrientation() {
		return this.mOrientation;
	}

	public final int getWidth() {
		return this.mTilesHorizontal;
	}

	public final int getHeight() {
		return this.mTilesVertical;
	}

	public final int getTileWidth() {
		return this.mTileWidth;
	}

	public final int getTileHeight() {
		return this.mTileHeight;
	}

	public RectangleVertexBuffer getSharedVertexBuffer() {
		return this.mSharedVertexBuffer;
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

	public TextureRegion getTextureRegionFromGlobalTileID(final int pGlobalTileID) {
		final HashMap<Integer, TextureRegion> textureRegionFromGLobalTileIDCache = this.mTextureRegionFromGLobalTileIDCache;
		
		final TextureRegion cachedTextureRegion = textureRegionFromGLobalTileIDCache.get(pGlobalTileID);
		if(cachedTextureRegion != null) {
			return cachedTextureRegion;
		} else {
			final ArrayList<TMXTileSet> tmxTileSets = this.mTMXTileSets;
			
			for(int i = tmxTileSets.size() - 1; i >= 0; i--) {
				final TMXTileSet tmxTileSet = tmxTileSets.get(i);
				if(pGlobalTileID >= tmxTileSet.getFirstGID()) {
					final TextureRegion textureRegion = tmxTileSet.getTextureRegionFromGlobalTileID(pGlobalTileID);
					/* Add to cache for the all future pGlobalTileIDs with the same value. */
					textureRegionFromGLobalTileIDCache.put(pGlobalTileID, textureRegion);
					return textureRegion;
				}
			}
			throw new IllegalArgumentException("No TextureRegion found for pGlobalTileID=" + pGlobalTileID);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
