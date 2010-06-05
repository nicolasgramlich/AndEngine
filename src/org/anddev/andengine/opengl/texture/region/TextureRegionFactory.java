package org.anddev.andengine.opengl.texture.region;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.ResourceTextureSource;

import android.content.Context;


/**
 * @author Nicolas Gramlich
 * @since 18:15:14 - 09.03.2010
 */
public class TextureRegionFactory {
	// ===========================================================
	// Constants
	// ===========================================================
	
	public static TextureRegion extractFromTexture(final Texture pTexture, int pTexturePositionX, int pTexturePositionY, int pWidth, int pHeight) {
		return new TextureRegion(pTexture, pTexturePositionX, pTexturePositionY, pWidth, pHeight);
	}

	// ===========================================================
	// From Asset
	// ===========================================================

	public static TextureRegion createFromAsset(final Texture pTexture, final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY) {
		final ITextureSource textureSource = new AssetTextureSource(pContext, pAssetPath);
		return createFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromAsset(final Texture pTexture, final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new AssetTextureSource(pContext, pAssetPath);
		return createTiledFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	// ===========================================================
	// From Resource
	// ===========================================================

	public static TextureRegion createFromResource(final Texture pTexture, final Context pContext, final int pDrawableResourceID, final int pTexturePositionX, final int pTexturePositionY) {
		final ITextureSource textureSource = new ResourceTextureSource(pContext, pDrawableResourceID);
		return createFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromResource(final Texture pTexture, final Context pContext, final int pDrawableResourceID, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new ResourceTextureSource(pContext, pDrawableResourceID);
		return createTiledFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	// ===========================================================
	// Worker-Methods
	// ===========================================================

	public static TextureRegion createFromSource(final Texture pTexture, final ITextureSource pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		final TextureRegion textureRegion = new TextureRegion(pTexture, pTexturePositionX, pTexturePositionY, pTextureSource.getWidth(), pTextureSource.getHeight());
		pTexture.addTextureSource(pTextureSource, textureRegion.getTexturePositionX(), textureRegion.getTexturePositionY());
		return textureRegion;
	}

	public static TiledTextureRegion createTiledFromSource(final Texture pTexture, final ITextureSource pTextureSource, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final TiledTextureRegion tiledTextureRegion = new TiledTextureRegion(pTexture, pTexturePositionX, pTexturePositionY, pTextureSource.getWidth(), pTextureSource.getHeight(), pTileColumns, pTileRows);
		pTexture.addTextureSource(pTextureSource, tiledTextureRegion.getTexturePositionX(), tiledTextureRegion.getTexturePositionY());
		return tiledTextureRegion;
	}
}
