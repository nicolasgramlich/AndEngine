package org.anddev.andengine.opengl.texture.region;


import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.Texture.TextureSourceWithLocation;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.texture.source.ResourceTextureSource;
import org.anddev.andengine.util.Callback;

import android.content.Context;


/**
 * @author Nicolas Gramlich
 * @since 18:15:14 - 09.03.2010
 */
public class TextureRegionFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static String sAssetBasePath = "";

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @param pAssetBasePath must end with '<code>/</code>' or have <code>.length() == 0</code>.
	 */
	public static void setAssetBasePath(final String pAssetBasePath) {
		if(pAssetBasePath.endsWith("/") || pAssetBasePath.length() == 0) {
			TextureRegionFactory.sAssetBasePath = pAssetBasePath;
		} else {
			throw new IllegalStateException("pAssetBasePath must end with '/' or be lenght zero.");
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static TextureRegion extractFromTexture(final Texture pTexture, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		return new TextureRegion(pTexture, pTexturePositionX, pTexturePositionY, pWidth, pHeight);
	}

	// ===========================================================
	// Methods using Texture
	// ===========================================================

	public static TextureRegion createFromAsset(final Texture pTexture, final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY) {
		final ITextureSource textureSource = new AssetTextureSource(pContext, TextureRegionFactory.sAssetBasePath + pAssetPath);
		return TextureRegionFactory.createFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromAsset(final Texture pTexture, final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new AssetTextureSource(pContext, TextureRegionFactory.sAssetBasePath + pAssetPath);
		return TextureRegionFactory.createTiledFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromResource(final Texture pTexture, final Context pContext, final int pDrawableResourceID, final int pTexturePositionX, final int pTexturePositionY) {
		final ITextureSource textureSource = new ResourceTextureSource(pContext, pDrawableResourceID);
		return TextureRegionFactory.createFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromResource(final Texture pTexture, final Context pContext, final int pDrawableResourceID, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new ResourceTextureSource(pContext, pDrawableResourceID);
		return TextureRegionFactory.createTiledFromSource(pTexture, textureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}


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

	// ===========================================================
	// Methods using BuildableTexture
	// ===========================================================

	public static TextureRegion createFromAsset(final BuildableTexture pBuildableTexture, final Context pContext, final String pAssetPath) {
		final ITextureSource textureSource = new AssetTextureSource(pContext, TextureRegionFactory.sAssetBasePath + pAssetPath);
		return TextureRegionFactory.createFromSource(pBuildableTexture, textureSource);
	}

	public static TiledTextureRegion createTiledFromAsset(final BuildableTexture pBuildableTexture, final Context pContext, final String pAssetPath, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new AssetTextureSource(pContext, TextureRegionFactory.sAssetBasePath + pAssetPath);
		return TextureRegionFactory.createTiledFromSource(pBuildableTexture, textureSource, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromResource(final BuildableTexture pBuildableTexture, final Context pContext, final int pDrawableResourceID) {
		final ITextureSource textureSource = new ResourceTextureSource(pContext, pDrawableResourceID);
		return TextureRegionFactory.createFromSource(pBuildableTexture, textureSource);
	}

	public static TiledTextureRegion createTiledFromResource(final BuildableTexture pBuildableTexture, final Context pContext, final int pDrawableResourceID, final int pTileColumns, final int pTileRows) {
		final ITextureSource textureSource = new ResourceTextureSource(pContext, pDrawableResourceID);
		return TextureRegionFactory.createTiledFromSource(pBuildableTexture, textureSource, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromSource(final BuildableTexture pBuildableTexture, final ITextureSource pTextureSource) {
		final TextureRegion textureRegion = new TextureRegion(pBuildableTexture, 0, 0, pTextureSource.getWidth(), pTextureSource.getHeight());
		pBuildableTexture.addTextureSource(pTextureSource, new Callback<TextureSourceWithLocation>() {
			@Override
			public void onCallback(final TextureSourceWithLocation pCallbackValue) {
				textureRegion.setTexturePosition(pCallbackValue.getTexturePositionX(), pCallbackValue.getTexturePositionY());
			}
		});
		return textureRegion;
	}

	public static TiledTextureRegion createTiledFromSource(final BuildableTexture pBuildableTexture, final ITextureSource pTextureSource, final int pTileColumns, final int pTileRows) {
		final TiledTextureRegion tiledTextureRegion = new TiledTextureRegion(pBuildableTexture, 0, 0, pTextureSource.getWidth(), pTextureSource.getHeight(), pTileColumns, pTileRows);
		pBuildableTexture.addTextureSource(pTextureSource, new Callback<TextureSourceWithLocation>() {
			@Override
			public void onCallback(final TextureSourceWithLocation pCallbackValue) {
				tiledTextureRegion.setTexturePosition(pCallbackValue.getTexturePositionX(), pCallbackValue.getTexturePositionY());
			}
		});
		return tiledTextureRegion;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
