package org.andengine.opengl.texture.atlas.bitmap;


import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.ResourceBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.BuildableTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;


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

	public static ITextureRegion createFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pTextureX, final int pTextureY) {
		return BitmapTextureAtlasTextureRegionFactory.createFromAsset(pBitmapTextureAtlas, pContext.getAssets(), pAssetPath, pTextureX, pTextureY);
	}

	public static ITextureRegion createFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final AssetManager pAssetManager, final String pAssetPath, final int pTextureX, final int pTextureY) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = AssetBitmapTextureAtlasSource.create(pAssetManager, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBitmapTextureAtlas, bitmapTextureAtlasSource, pTextureX, pTextureY);
	}

	public static TiledTextureRegion createTiledFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pTextureX, final int pTextureY, final int pTileColumns, final int pTileRows) {
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(pBitmapTextureAtlas, pContext.getAssets(), pAssetPath, pTextureX, pTextureY, pTileColumns, pTileRows);
	}

	public static TiledTextureRegion createTiledFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final AssetManager pAssetManager, final String pAssetPath, final int pTextureX, final int pTextureY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = AssetBitmapTextureAtlasSource.create(pAssetManager, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, bitmapTextureAtlasSource, pTextureX, pTextureY, pTileColumns, pTileRows);
	}


	public static ITextureRegion createFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID, final int pTextureX, final int pTextureY) {
		return BitmapTextureAtlasTextureRegionFactory.createFromResource(pBitmapTextureAtlas, pContext.getResources(), pDrawableResourceID, pTextureX, pTextureY);
	}

	public static ITextureRegion createFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Resources pResources, final int pDrawableResourceID, final int pTextureX, final int pTextureY) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = ResourceBitmapTextureAtlasSource.create(pResources, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBitmapTextureAtlas, bitmapTextureAtlasSource, pTextureX, pTextureY);
	}

	public static TiledTextureRegion createTiledFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID, final int pTextureX, final int pTextureY, final int pTileColumns, final int pTileRows) {
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(pBitmapTextureAtlas, pContext.getResources(), pDrawableResourceID, pTextureX, pTextureY, pTileColumns, pTileRows);
	}

	public static TiledTextureRegion createTiledFromResource(final BitmapTextureAtlas pBitmapTextureAtlas, final Resources pResources, final int pDrawableResourceID, final int pTextureX, final int pTextureY, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = ResourceBitmapTextureAtlasSource.create(pResources, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, bitmapTextureAtlasSource, pTextureX, pTextureY, pTileColumns, pTileRows);
	}


	public static ITextureRegion createFromSource(final BitmapTextureAtlas pBitmapTextureAtlas, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final int pTextureX, final int pTextureY) {
		return TextureRegionFactory.createFromSource(pBitmapTextureAtlas, pBitmapTextureAtlasSource, pTextureX, pTextureY);
	}

	public static TiledTextureRegion createTiledFromSource(final BitmapTextureAtlas pBitmapTextureAtlas, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final int pTextureX, final int pTextureY, final int pTileColumns, final int pTileRows) {
		return TextureRegionFactory.createTiledFromSource(pBitmapTextureAtlas, pBitmapTextureAtlasSource, pTextureX, pTextureY, pTileColumns, pTileRows);
	}

	// ===========================================================
	// Methods using BuildableTexture
	// ===========================================================

	public static ITextureRegion createFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath) {
		return BitmapTextureAtlasTextureRegionFactory.createFromAsset(pBuildableBitmapTextureAtlas, pContext.getAssets(), pAssetPath);
	}

	public static ITextureRegion createFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final AssetManager pAssetManager, final String pAssetPath) {
		return BitmapTextureAtlasTextureRegionFactory.createFromAsset(pBuildableBitmapTextureAtlas, pAssetManager, pAssetPath, false);
	}

	public static ITextureRegion createFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath, final boolean pRotated) {
		return BitmapTextureAtlasTextureRegionFactory.createFromAsset(pBuildableBitmapTextureAtlas, pContext.getAssets(), pAssetPath, pRotated);
	}

	public static ITextureRegion createFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final AssetManager pAssetManager, final String pAssetPath, final boolean pRotated) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = AssetBitmapTextureAtlasSource.create(pAssetManager, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, bitmapTextureAtlasSource, pRotated);
	}

	public static TiledTextureRegion createTiledFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pTileColumns, final int pTileRows) {
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(pBuildableBitmapTextureAtlas, pContext.getAssets(), pAssetPath, pTileColumns, pTileRows);
	}

	public static TiledTextureRegion createTiledFromAsset(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final AssetManager pAssetManager, final String pAssetPath, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = AssetBitmapTextureAtlasSource.create(pAssetManager, BitmapTextureAtlasTextureRegionFactory.sAssetBasePath + pAssetPath);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, bitmapTextureAtlasSource, pTileColumns, pTileRows);
	}


	public static ITextureRegion createFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID) {
		return BitmapTextureAtlasTextureRegionFactory.createFromResource(pBuildableBitmapTextureAtlas, pContext.getResources(), pDrawableResourceID);
	}

	public static ITextureRegion createFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Resources pResources, final int pDrawableResourceID) {
		return BitmapTextureAtlasTextureRegionFactory.createFromResource(pBuildableBitmapTextureAtlas, pResources, pDrawableResourceID, false);
	}

	public static ITextureRegion createFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID, final boolean pRotated) {
		return BitmapTextureAtlasTextureRegionFactory.createFromResource(pBuildableBitmapTextureAtlas, pContext.getResources(), pDrawableResourceID, pRotated);
	}

	public static ITextureRegion createFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Resources pResources, final int pDrawableResourceID, final boolean pRotated) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = ResourceBitmapTextureAtlasSource.create(pResources, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, bitmapTextureAtlasSource, pRotated);
	}

	public static TiledTextureRegion createTiledFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Context pContext, final int pDrawableResourceID, final int pTileColumns, final int pTileRows) {
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromResource(pBuildableBitmapTextureAtlas, pContext.getResources(), pDrawableResourceID, pTileColumns, pTileRows);
	}

	public static TiledTextureRegion createTiledFromResource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final Resources pResources, final int pDrawableResourceID, final int pTileColumns, final int pTileRows) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = ResourceBitmapTextureAtlasSource.create(pResources, pDrawableResourceID);
		return BitmapTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, bitmapTextureAtlasSource, pTileColumns, pTileRows);
	}


	public static ITextureRegion createFromSource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource) {
		return BitmapTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, pBitmapTextureAtlasSource, false);
	}

	public static ITextureRegion createFromSource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final boolean pRotated) {
		return BuildableTextureAtlasTextureRegionFactory.createFromSource(pBuildableBitmapTextureAtlas, pBitmapTextureAtlasSource, pRotated);
	}

	public static TiledTextureRegion createTiledFromSource(final BuildableBitmapTextureAtlas pBuildableBitmapTextureAtlas, final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final int pTileColumns, final int pTileRows) {
		return BuildableTextureAtlasTextureRegionFactory.createTiledFromSource(pBuildableBitmapTextureAtlas, pBitmapTextureAtlasSource, pTileColumns, pTileRows);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
