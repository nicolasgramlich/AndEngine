package org.anddev.andengine.entity.layer.tiled.tmx;

import java.util.ArrayList;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureFactory;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.anddev.andengine.util.SAXUtils;
import org.xml.sax.Attributes;

import android.content.Context;
import android.util.SparseArray;

/**
 * @author Nicolas Gramlich
 * @since 19:03:24 - 20.07.2010
 */
public class TMXTileSet implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mFirstGlobalTileID;
	private final String mName;
	private final int mTileWidth;
	private final int mTileHeight;

	private String mImageSource;
	private Texture mTexture;

	private int mTilesHorizontal;
	@SuppressWarnings("unused")
	private int mTilesVertical;

	private final int mSpacing;
	private final int mMargin;

	private final SparseArray<ArrayList<TMXTileProperty>> mTMXTileProperties = new SparseArray<ArrayList<TMXTileProperty>>();
	private final TextureOptions mTextureOptions;

	// ===========================================================
	// Constructors
	// ===========================================================

	TMXTileSet(final Attributes pAttributes, final TextureOptions pTextureOptions) {
		this.mFirstGlobalTileID = SAXUtils.getIntAttribute(pAttributes, TAG_TILESET_ATTRIBUTE_FIRSTGID, 1);
		this.mName = pAttributes.getValue("", TAG_TILESET_ATTRIBUTE_NAME);
		this.mTileWidth = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_TILESET_ATTRIBUTE_TILEWIDTH);
		this.mTileHeight = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_TILESET_ATTRIBUTE_TILEHEIGHT);
		this.mSpacing = SAXUtils.getIntAttribute(pAttributes, TAG_TILESET_ATTRIBUTE_SPACING, 0);
		this.mMargin = SAXUtils.getIntAttribute(pAttributes, TAG_TILESET_ATTRIBUTE_MARGIN, 0);

		this.mTextureOptions = pTextureOptions;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final int getFirstGlobalTileID() {
		return this.mFirstGlobalTileID;
	}

	public final String getName() {
		return this.mName;
	}

	public final int getTileWidth() {
		return this.mTileWidth;
	}

	public final int getTileHeight() {
		return this.mTileHeight;
	}

	public Texture getTexture() {
		return this.mTexture;
	}

	public void setImageSource(final Context pContext, final TextureManager pTextureManager, final Attributes pAttributes) {
		this.mImageSource = pAttributes.getValue("", TAG_IMAGE_ATTRIBUTE_SOURCE);

		final AssetTextureSource assetTextureSource = new AssetTextureSource(pContext, this.mImageSource);
		this.mTilesHorizontal = assetTextureSource.getWidth() / this.mTileWidth;
		this.mTilesVertical = assetTextureSource.getHeight() / this.mTileHeight;
		this.mTexture = TextureFactory.createForTextureSourceSize(assetTextureSource, this.mTextureOptions);
		TextureRegionFactory.createFromSource(this.mTexture, assetTextureSource, 0, 0);
		pTextureManager.loadTexture(this.mTexture);
	}

	public String getImageSource() {
		return this.mImageSource;
	}
	
	public SparseArray<ArrayList<TMXTileProperty>> getTMXTileProperties() {
		return this.mTMXTileProperties;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public ArrayList<TMXTileProperty> getTMXTilePropertiesFromGlobalTileID(final int pGlobalTileID) {
		final int localTileID = pGlobalTileID - this.mFirstGlobalTileID;
		return this.mTMXTileProperties.get(localTileID);
	}

	public void addTMXTileProperty(final int pLocalTileID, final TMXTileProperty pTMXTileProperty) {
		final ArrayList<TMXTileProperty> existingProperties = this.mTMXTileProperties.get(pLocalTileID);
		if(existingProperties != null) {
			existingProperties.add(pTMXTileProperty);
		} else {
			final ArrayList<TMXTileProperty> newProperties = new ArrayList<TMXTileProperty>();
			newProperties.add(pTMXTileProperty);
			this.mTMXTileProperties.put(pLocalTileID, newProperties);
		}
	}

	public TextureRegion getTextureRegionFromGlobalTileID(final int pGlobalTileID) {
		final int localTileID = pGlobalTileID - this.mFirstGlobalTileID;
		final int tileColumn = localTileID % this.mTilesHorizontal;
		final int tileRow = localTileID / this.mTilesHorizontal;

		final int texturePositionX = this.mMargin + (this.mSpacing + this.mTileWidth) * tileColumn;
		final int texturePositionY = this.mMargin + (this.mSpacing + this.mTileHeight) * tileRow;

		return new TextureRegion(this.mTexture, texturePositionX, texturePositionY, this.mTileWidth, this.mTileHeight);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
