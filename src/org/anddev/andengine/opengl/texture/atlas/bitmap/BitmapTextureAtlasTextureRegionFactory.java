package org.anddev.andengine.opengl.texture.atlas.bitmap;


import org.anddev.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.ResourceBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.buildable.BuildableTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
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

	public static void reset() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");
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

	public static ITextureRegion createFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = new AssetBitmapTextureAtlasSource(pContext, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBitmapTextureAtlas, bitmapTextureAtlasSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = new AssetBitmapTextureAtlasSource(pContext, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, bitmapTextureAtlasSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}


	public static ITextureRegion createFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID, final int pTexturePositionX, final int pTexturePositionY) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = new ResourceBitmapTextureAtlasSource(pContext, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBitmapTextureAtlas, bitmapTextureAtlasSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = new ResourceBitmapTextureAtlasSource(pContext, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, bitmapTextureAtlasSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	public static ITextureRegion createFromSource(final BitmapTextureAtlas pBitmapTextureAtlas, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY) {
		return TextureRegionFactory.createFromSource(pBitmapTextureAtlas, pBitmapTextureAtlasSource, pTexturePositionX, pTexturePositionY);
	}

	public static TiledTextureRegion createTiledFromSource(final BitmapTextureAtlas pBitmapTextureAtlas, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY, final int pTileColumns, final int pTileRows) {
		return TextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, pBitmapTextureAtlasSource, pTexturePositionX, pTexturePositionY, pTileColumns, pTileRows);
	}

	// ===========================================================
	// Methods using BuildableTexture
	// ===========================================================

	public static ITextureRegion createFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = new AssetBitmapTextureAtlasSource(pContext, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, bitmapTextureAtlasSource);
	}

	public static TiledTextureRegion createTiledFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = new AssetBitmapTextureAtlasSource(pContext, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, bitmapTextureAtlasSource, pTileColumns, pTileRows);
	}


	public static ITextureRegion createFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = new ResourceBitmapTextureAtlasSource(pContext, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, bitmapTextureAtlasSource);
	}

	public static TiledTextureRegion createTiledFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = new ResourceBitmapTextureAtlasSource(pContext, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, bitmapTextureAtlasSource, pTileColumns, pTileRows);
	}


	public static ITextureRegion createFromSource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource) {
		return BuildableTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, pBitmapTextureAtlasSource);
	}

	public static TiledTextureRegion createTiledFromSource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final int pTileColumns, final int pTileRows) {
		return BuildableTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, pBitmapTextureAtlasSource, pTileColumns, pTileRows);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
