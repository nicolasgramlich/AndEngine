package org.anddev.andengine.opengl.texture.factory;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TiledTexture;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.anddev.andengine.opengl.texture.source.ResourceTextureSource;

import android.content.Context;

/**
 * @author Nicolas Gramlich
 * @since 18:15:14 - 09.03.2010
 */
public class TextureFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public static Texture createFromResource(final Context pContext, final int pDrawableResourceID) {
		return new Texture(new ResourceTextureSource(pContext, pDrawableResourceID));
	}
	
	public static Texture createFromAsset(final Context pContext, final String pAssetPath) {
		return new Texture(new AssetTextureSource(pContext, pAssetPath));
	}
	
	public static Texture createTiledFromResource(final Context pContext, final int pDrawableResourceID, final int pTileColumns, final int pTileRows) {
		return new TiledTexture(new ResourceTextureSource(pContext, pDrawableResourceID), pTileColumns, pTileRows);
	}
	
	public static Texture createTiledFromAsset(final Context pContext, final String pAssetPath, final int pTileColumns, final int pTileRows) {
		return new TiledTexture(new AssetTextureSource(pContext, pAssetPath), pTileColumns, pTileRows);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
