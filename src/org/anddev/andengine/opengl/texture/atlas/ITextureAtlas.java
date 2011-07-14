package org.anddev.andengine.opengl.texture.atlas;

import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.source.ITextureAtlasSource;
import org.anddev.andengine.util.Debug;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:24:29 - 14.07.2011
 */
public interface ITextureAtlas<T extends ITextureAtlasSource> extends ITexture {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException;
	public void removeTextureAtlasSource(final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY);
	public void clearTextureAtlasSources();
	
	@Override
	public ITextureAtlasStateListener<T> getTextureStateListener();

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public static interface ITextureAtlasStateListener<T extends ITextureAtlasSource> extends ITextureStateListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onTextureAtlasSourceLoadExeption(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource, final Throwable pThrowable);

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class TextureAtlasStateAdapter<T extends ITextureAtlasSource> implements ITextureAtlasStateListener<T> {
			@Override
			public void onLoadedToHardware(final ITexture pTexture) { }

			@Override
			public void onTextureAtlasSourceLoadExeption(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource, final Throwable pThrowable) { }

			@Override
			public void onUnloadedFromHardware(final ITexture pTexture) { }
		}

		public static class DebugTextureAtlasStateListener<T extends ITextureAtlasSource> implements ITextureAtlasStateListener<T> {
			@Override
			public void onLoadedToHardware(final ITexture pTexture) {
				Debug.d("Texture loaded: " + pTexture.toString());
			}

			@Override
			public void onTextureAtlasSourceLoadExeption(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource, final Throwable pThrowable) {
				Debug.e("Exception loading TextureAtlasSource. TextureAtlas: " + pTextureAtlas.toString() + " TextureAtlasSource: " + pTextureAtlasSource.toString(), pThrowable);
			}

			@Override
			public void onUnloadedFromHardware(final ITexture pTexture) {
				Debug.d("Texture unloaded: " + pTexture.toString());
			}
		}
	}
}
