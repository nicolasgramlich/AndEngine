package org.anddev.andengine.opengl.texture.atlas.buildable;


import org.anddev.andengine.opengl.texture.atlas.ITextureAtlas;
import org.anddev.andengine.opengl.texture.region.ITextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.texture.source.ITextureAtlasSource;
import org.anddev.andengine.util.Callback;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:42:08 - 12.07.2011
 */
public class BuildableTextureAtlasTextureRegionFactory {
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

	// ===========================================================
	// Methods using BuildableBitmapTexture
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static <T extends ITextureAtlasSource, A extends ITextureAtlas<T>> ITextureRegion createFromSource(final BuildableTextureAtlas<T, A> pBuildableTextureAtlas, final T pTextureAtlasSource) {
		final TextureRegion textureRegion = new TextureRegion(pBuildableTextureAtlas, 0, 0, pTextureAtlasSource.getWidth(), pTextureAtlasSource.getHeight());
		pBuildableTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, new Callback<T>() {
			@Override
			public void onCallback(final T pCallbackValue) {
				textureRegion.setPosition(pCallbackValue.getTexturePositionX(), pCallbackValue.getTexturePositionY());
			}
		});
		return textureRegion;
	}

	public static <T extends ITextureAtlasSource, A extends ITextureAtlas<T>> TiledTextureRegion createTiledFromSource(final BuildableTextureAtlas<T, A> pBuildableTextureAtlas, final T pTextureAtlasSource, final int pTileColumns, final int pTileRows) {
		final TiledTextureRegion tiledTextureRegion = TiledTextureRegion.create(pBuildableTextureAtlas, 0, 0, pTextureAtlasSource.getWidth(), pTextureAtlasSource.getHeight(), pTileColumns, pTileRows);
		pBuildableTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, new Callback<T>() {
			@Override
			public void onCallback(final T pCallbackValue) {
				tiledTextureRegion.setPosition(pCallbackValue.getTexturePositionX(), pCallbackValue.getTexturePositionY());
			}
		});
		return tiledTextureRegion;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
