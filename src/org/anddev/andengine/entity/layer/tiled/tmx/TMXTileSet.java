package org.anddev.andengine.entity.layer.tiled.tmx;

import org.anddev.andengine.entity.layer.tiled.tmx.util.constants.TMXConstants;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureFactory;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.xml.sax.Attributes;

import android.content.Context;

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

	private final int mFirstGID;
	private final String mName;
	private final int mTileWidth;
	private final int mTileHeight;
	
	private String mImageSource;
	private Texture mTexture;
	
	private int mTilesHorizontal;
	@SuppressWarnings("unused")
	private int mTilesVertical;

	// ===========================================================
	// Constructors
	// ===========================================================

	TMXTileSet(final Attributes pAttributes) {
		this.mFirstGID = Integer.parseInt(pAttributes.getValue("", TAG_TILESET_ATTRIBUTE_FIRSTGID));
		this.mName = pAttributes.getValue("", TAG_TILESET_ATTRIBUTE_NAME);
		this.mTileWidth = Integer.parseInt(pAttributes.getValue("", TAG_TILESET_ATTRIBUTE_TILEWIDTH));
		this.mTileHeight = Integer.parseInt(pAttributes.getValue("", TAG_TILESET_ATTRIBUTE_TILEHEIGHT));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final int getFirstGID() {
		return this.mFirstGID;
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
		this.mTexture = TextureFactory.createForTextureSourceSize(assetTextureSource);
		TextureRegionFactory.createFromSource(this.mTexture, assetTextureSource, 0, 0);
		pTextureManager.loadTexture(this.mTexture);
	}

	public String getImageSource() {
		return this.mImageSource;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public TextureRegion getTextureRegionFromGlobalTileID(final int pGlobalTileID) {
		final int localTileID = pGlobalTileID - this.mFirstGID;
		final int tileIndexX = localTileID % this.mTilesHorizontal;
		final int tileIndexY = localTileID / this.mTilesHorizontal;
		return new TextureRegion(this.mTexture, this.mTileWidth * tileIndexX, this.mTileHeight * tileIndexY, this.mTileWidth, this.mTileHeight);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
