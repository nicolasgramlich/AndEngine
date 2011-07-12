package org.anddev.andengine.opengl.texture.bitmap;


import org.anddev.andengine.opengl.texture.bitmap.source.AssetBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.source.ResourceBitmapTextureSource;
import org.anddev.andengine.opengl.texture.buildable.BuildableTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;


/**
 * @author Nicolas Gramlich
 * @since 18:15:14 - 09.03.2010
 */
public class BitmapTextureRegionFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static String sAssetBasePath = "";
	private static boolean sCreateTextureRegionBuffersManaged;

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
			BitmapTextureRegionFactory.sAssetBasePath = pAssetBasePath;
		} else {
			throw new IllegalArgumentException("pAssetBasePath must end with '/' or be lenght zero.");
		}
	}

	public static void setCreateTextureRegionBuffersManaged(final boolean pCreateTextureRegionBuffersManaged) {
		BitmapTextureRegionFactory.sCreateTextureRegionBuffersManaged = pCreateTextureRegionBuffersManaged;
	}

	public static void reset() {
		BitmapTextureRegionFactory.setAssetBasePath("");
		BitmapTextureRegionFactory.setCreateTextureRegionBuffersManaged(false);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Methods using BitmapTexture
	// ===========================================================

	public static TextureRegion createFromAsset(final BitmapTexture pBitmapTexture, final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureSource bitmapTextureSource = new AssetBitmapTextureSource(pContext, BitmapTextureRegionFactory.sAssetBasePath + pAssetPath);
		return TextureRegionFactory.createFromSource(pBitmapTexture, bitmapTextureSource, pTexturePositionX, pTexturePositionY, sCreateTextureRegionBuffersManaged);
	}

	public static TiledTextureRegion createTiledFromAsset(final BitmapTexture pBitmapTexture, final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource bitmapTextureSource = new AssetBitmapTextureSource(pContext, BitmapTextureRegionFactory.sAssetBasePath + pAssetPath);
		return TextureRegionFactory.createTiledFromSource(pBitmapTexture, bitmapTextureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows, sCreateTextureRegionBuffersManaged);
	}


	public static TextureRegion createFromResource(final BitmapTexture pBitmapTexture, final Context pContext, final int pDrawableResourceID, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureSource bitmapTextureSource = new ResourceBitmapTextureSource(pContext, pDrawableResourceID);
		return TextureRegionFactory.createFromSource(pBitmapTexture, bitmapTextureSource, pTexturePositionX, pTexturePositionY, sCreateTextureRegionBuffersManaged);
	}

	public static TiledTextureRegion createTiledFromResource(final BitmapTexture pBitmapTexture, final Context pContext, final int pDrawableResourceID, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource bitmapTextureSource = new ResourceBitmapTextureSource(pContext, pDrawableResourceID);
		return TextureRegionFactory.createTiledFromSource(pBitmapTexture, bitmapTextureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows, sCreateTextureRegionBuffersManaged);
	}

	// ===========================================================
	// Methods using BuildableTexture
	// ===========================================================

	public static TextureRegion createFromAsset(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final String pAssetPath) {
		final IBitmapTextureSource bitmapTextureSource = new AssetBitmapTextureSource(pContext, BitmapTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BuildableTextureRegionFactory.createFromSource(pBuildableBitmapTexture, bitmapTextureSource, sCreateTextureRegionBuffersManaged);
	}

	public static TiledTextureRegion createTiledFromAsset(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final String pAssetPath, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource bitmapTextureSource = new AssetBitmapTextureSource(pContext, BitmapTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BuildableTextureRegionFactory.createTiledFromSource(pBuildableBitmapTexture, bitmapTextureSource, pTileColumns, pTileRows, sCreateTextureRegionBuffersManaged);
	}


	public static TextureRegion createFromResource(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final int pDrawableResourceID) {
		final IBitmapTextureSource bitmapTextureSource = new ResourceBitmapTextureSource(pContext, pDrawableResourceID);
		return BuildableTextureRegionFactory.createFromSource(pBuildableBitmapTexture, bitmapTextureSource, sCreateTextureRegionBuffersManaged);
	}

	public static TiledTextureRegion createTiledFromResource(final BuildableBitmapTexture pBuildableBitmapTexture, final Context pContext, final int pDrawableResourceID, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource bitmapTextureSource = new ResourceBitmapTextureSource(pContext, pDrawableResourceID);
		return BuildableTextureRegionFactory.createTiledFromSource(pBuildableBitmapTexture, bitmapTextureSource, pTileColumns, pTileRows, sCreateTextureRegionBuffersManaged);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
