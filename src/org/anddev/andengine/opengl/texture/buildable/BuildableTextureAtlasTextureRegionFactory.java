package org.anddev.andengine.opengl.texture.buildable;


import org.anddev.andengine.opengl.texture.atlas.ITextureAtlas;
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

	public static <T extends ITextureAtlasSource, A extends ITextureAtlas<T>> TextureRegion createFromSource(final BuildableTextureAtlas<T, A> pBuildableTextureAtlas, final T pTextureAtlasSource, final boolean pTextureRegionBufferManaged) {
		final TextureRegion textureRegion = new TextureRegion(pBuildableTextureAtlas, 0, 0, pTextureAtlasSource.getWidth(), pTextureAtlasSource.getHeight());
		pBuildableTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, new Callback<T>() {
			@Override
			public void onCallback(final T pCallbackValue) {
				textureRegion.setTexturePosition(pCallbackValue.getTexturePositionX(), pCallbackValue.getTexturePositionY());
			}
		});
		textureRegion.setTextureRegionBufferManaged(pTextureRegionBufferManaged);
		return textureRegion;
	}

	public static <T extends ITextureAtlasSource, A extends ITextureAtlas<T>> TiledTextureRegion createTiledFromSource(final BuildableTextureAtlas<T, A> pBuildableTextureAtlas, final T pTextureAtlasSource, final int pTileColumns, final int pTileRows, final boolean pTextureRegionBufferManaged) {
		final TiledTextureRegion tiledTextureRegion = new TiledTextureRegion(pBuildableTextureAtlas, 0, 0, pTextureAtlasSource.getWidth(), pTextureAtlasSource.getHeight(), pTileColumns, pTileRows);
		pBuildableTextureAtlas.addTextureAtlasSource(pTextureAtlasSource, new Callback<T>() {
			@Override
			public void onCallback(final T pCallbackValue) {
				tiledTextureRegion.setTexturePosition(pCallbackValue.getTexturePositionX(), pCallbackValue.getTexturePositionY());
			}
		});
		tiledTextureRegion.setTextureRegionBufferManaged(pTextureRegionBufferManaged);
		return tiledTextureRegion;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
