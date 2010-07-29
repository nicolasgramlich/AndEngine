package org.anddev.andengine.entity.layer.tiled.tmx;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;
import org.anddev.andengine.util.SAXUtils;
import org.xml.sax.Attributes;

import android.util.SparseArray;

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
	private final ArrayList<TMXObjectGroup> mTMXObjectGroups = new ArrayList<TMXObjectGroup>();

	private final RectangleVertexBuffer mSharedVertexBuffer;

	private SparseArray<TextureRegion> mGlobalTileIDToTextureRegionCache = new SparseArray<TextureRegion>();
	private SparseArray<ArrayList<TMXTileProperty>> mGlobalTileIDToTMXTilePropertiesCache = new SparseArray<ArrayList<TMXTileProperty>>();

	// ===========================================================
	// Constructors
	// ===========================================================

	TMXTiledMap(final Attributes pAttributes) {
		this.mOrientation = pAttributes.getValue("", TAG_MAP_ATTRIBUTE_ORIENTATION);
		if(this.mOrientation.equals(TAG_MAP_ATTRIBUTE_ORIENTATION_VALUE_ORTHOGONAL) == false) {
			throw new IllegalArgumentException(TAG_MAP_ATTRIBUTE_ORIENTATION + ": '" + this.mOrientation + "' is not supported.");
		}
		this.mTilesHorizontal = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_MAP_ATTRIBUTE_WIDTH);
		this.mTilesVertical = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_MAP_ATTRIBUTE_HEIGHT);
		this.mTileWidth = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_MAP_ATTRIBUTE_TILEWIDTH);
		this.mTileHeight = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_MAP_ATTRIBUTE_TILEHEIGHT);

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

	void addTMXTileSet(final TMXTileSet pTMXTileSet) {
		this.mTMXTileSets.add(pTMXTileSet);
	}

	public ArrayList<TMXTileSet> getTMXTileSets() {
		return this.mTMXTileSets;
	}

	void addTMXLayer(final TMXLayer pTMXLayer) {
		this.mTMXLayers.add(pTMXLayer);
	}

	public ArrayList<TMXLayer> getTMXLayers() {
		return this.mTMXLayers;
	}

	void addTMXObjectGroup(final TMXObjectGroup pTMXObjectGroup) {
		this.mTMXObjectGroups.add(pTMXObjectGroup);
	}

	public ArrayList<TMXObjectGroup> getTMXObjectGroups() {
		return this.mTMXObjectGroups;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public ArrayList<TMXTileProperty> getTMXTileProperties(final int pGlobalTileID) {
		final SparseArray<ArrayList<TMXTileProperty>> globalTileIDToTMXTilePropertiesCache = this.mGlobalTileIDToTMXTilePropertiesCache;

		final ArrayList<TMXTileProperty> cachedTMXTileProperties = globalTileIDToTMXTilePropertiesCache.get(pGlobalTileID);
		if(cachedTMXTileProperties != null) {
			return cachedTMXTileProperties;
		} else {
			final ArrayList<TMXTileSet> tmxTileSets = this.mTMXTileSets;

			for(int i = tmxTileSets.size() - 1; i >= 0; i--) {
				final TMXTileSet tmxTileSet = tmxTileSets.get(i);
				if(pGlobalTileID >= tmxTileSet.getFirstGlobalTileID()) {
					return tmxTileSet.getTMXTilePropertiesFromGlobalTileID(pGlobalTileID);
				}
			}
			throw new IllegalArgumentException("No TMXTileProperties found for pGlobalTileID=" + pGlobalTileID);
		}
	}

	public TextureRegion getTextureRegionFromGlobalTileID(final int pGlobalTileID) {
		final SparseArray<TextureRegion> globalTileIDToTextureRegionCache = this.mGlobalTileIDToTextureRegionCache;

		final TextureRegion cachedTextureRegion = globalTileIDToTextureRegionCache.get(pGlobalTileID);
		if(cachedTextureRegion != null) {
			return cachedTextureRegion;
		} else {
			final ArrayList<TMXTileSet> tmxTileSets = this.mTMXTileSets;

			for(int i = tmxTileSets.size() - 1; i >= 0; i--) {
				final TMXTileSet tmxTileSet = tmxTileSets.get(i);
				if(pGlobalTileID >= tmxTileSet.getFirstGlobalTileID()) {
					final TextureRegion textureRegion = tmxTileSet.getTextureRegionFromGlobalTileID(pGlobalTileID);
					/* Add to cache for the all future pGlobalTileIDs with the same value. */
					globalTileIDToTextureRegionCache.put(pGlobalTileID, textureRegion);
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
