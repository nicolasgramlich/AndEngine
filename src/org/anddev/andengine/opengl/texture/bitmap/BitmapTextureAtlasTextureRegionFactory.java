package org.anddev.andengine.opengl.texture.bitmap;


import org.anddev.andengine.opengl.texture.bitmap.source.AssetBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.bitmap.source.ResourceBitmapTextureSource;
import org.anddev.andengine.opengl.texture.buildable.BuildableTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 18:15:14 - 09.03.2010
 */
public class BitmapTextureAtlasTextureRegionFactory {
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
			BitmapTextureAtlasTextureRegionFactory.sAssetBasePath = pAssetBasePath;
		} else {
			throw new IllegalArgumentException("pAssetBasePath must end with '/' or be lenght zero.");
		}
	}

	public static void setCreateTextureRegionBuffersManaged(final boolean pCreateTextureRegionBuffersManaged) {
		BitmapTextureAtlasTextureRegionFactory.sCreateTextureRegionBuffersManaged = pCreateTextureRegionBuffersManaged;
	}

	public static void reset() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");
		BitmapTextureAtlasTextureRegionFactory.setCreateTextureRegionBuffersManaged(false);
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

	public static TextureRegion createFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureSource bitmapTextureSource = new AssetBitmapTextureSource(pContext, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBitmapTextureAtlas, bitmapTextureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource bitmapTextureSource = new AssetBitmapTextureSource(pContext, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, bitmapTextureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureSource bitmapTextureSource = new ResourceBitmapTextureSource(pContext, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBitmapTextureAtlas, bitmapTextureSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource bitmapTextureSource = new ResourceBitmapTextureSource(pContext, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, bitmapTextureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	public static TextureRegion createFromSource(final BitmapTextureAtlas pBitmapTextureAtlas, final IBitmapTextureSource pBitmapTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		return TextureRegionFactory.createFromSource(pBitmapTextureAtlas, pBitmapTextureSource, pTexturePositionX, pTexturePositionY, sCreateTextureRegionBuffersManaged);
	}

	public static TiledTextureRegion createTiledFromSource(final BitmapTextureAtlas pBitmapTextureAtlas, final IBitmapTextureSource pBitmapTextureSource, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		return TextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, pBitmapTextureSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows, sCreateTextureRegionBuffersManaged);
	}

	// ===========================================================
	// Methods using BuildableTexture
	// ===========================================================

	public static TextureRegion createFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath) {
		final IBitmapTextureSource bitmapTextureSource = new AssetBitmapTextureSource(pContext, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, bitmapTextureSource);
	}

	public static TiledTextureRegion createTiledFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource bitmapTextureSource = new AssetBitmapTextureSource(pContext, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, bitmapTextureSource, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID) {
		final IBitmapTextureSource bitmapTextureSource = new ResourceBitmapTextureSource(pContext, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, bitmapTextureSource);
	}

	public static TiledTextureRegion createTiledFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureSource bitmapTextureSource = new ResourceBitmapTextureSource(pContext, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, bitmapTextureSource, pTileColumns, pTileRows);
	}


	public static TextureRegion createFromSource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final IBitmapTextureSource pBitmapTextureSource) {
		return BuildableTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, pBitmapTextureSource, sCreateTextureRegionBuffersManaged);
	}

	public static TiledTextureRegion createTiledFromSource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final IBitmapTextureSource pBitmapTextureSource, final int pTileColumns, final int pTileRows) {
		return BuildableTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, pBitmapTextureSource, pTileColumns, pTileRows, sCreateTextureRegionBuffersManaged);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
