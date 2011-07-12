package org.anddev.andengine.opengl.texture.buildable;


import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.util.Callback;


/**
 * @author Nicolas Gramlich
 * @since 16:42:08 - 12.07.2011
 */
public class BuildableTextureRegionFactory {
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

	public static <T extends ITextureSource, K extends ITexture<T>> TextureRegion createFromSource(final BuildableTexture<T, K> pBuildableTexture, final T pTextureSource, final boolean pTextureRegionBufferManaged) {
		final TextureRegion textureRegion = new TextureRegion(pBuildableTexture, 0, 0, pTextureSource.getWidth(), pTextureSource.getHeight());
		pBuildableTexture.addTextureSource(pTextureSource, new Callback<T>() {
			@Override
			public void onCallback(final T pCallbackValue) {
				textureRegion.setTexturePosition(pCallbackValue.getTexturePositionX(), pCallbackValue.getTexturePositionY());
			}
		});
		textureRegion.setTextureRegionBufferManaged(pTextureRegionBufferManaged);
		return textureRegion;
	}

	public static <T extends ITextureSource, K extends ITexture<T>> TiledTextureRegion createTiledFromSource(final BuildableTexture<T, K> pBuildableTexture, final T pTextureSource, final int pTileColumns, final int pTileRows, final boolean pTextureRegionBufferManaged) {
		final TiledTextureRegion tiledTextureRegion = new TiledTextureRegion(pBuildableTexture, 0, 0, pTextureSource.getWidth(), pTextureSource.getHeight(), pTileColumns, pTileRows);
		pBuildableTexture.addTextureSource(pTextureSource, new Callback<T>() {
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
