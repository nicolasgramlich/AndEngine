package org.anddev.andengine.opengl.texture;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.util.Debug;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:24:29 - 14.07.2011
 */
public interface ITextureAtlas<T extends ITextureSource> extends ITexture {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void addTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException;
	public void removeTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY);
	public void clearTextureSources();
	
	@Override
	public ITextureAtlasStateListener<T> getTextureStateListener();

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public static interface ITextureAtlasStateListener<T extends ITextureSource> extends ITextureStateListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onTextureSourceLoadExeption(final ITextureAtlas<T> pTextureAtlas, final T pTextureSource, final Throwable pThrowable);

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class TextureAtlasStateAdapter<T extends ITextureSource> implements ITextureAtlasStateListener<T> {
			@Override
			public void onLoadedToHardware(final ITexture pTexture) { }

			@Override
			public void onTextureSourceLoadExeption(final ITextureAtlas<T> pTextureAtlas, final T pTextureSource, final Throwable pThrowable) { }

			@Override
			public void onUnloadedFromHardware(final ITexture pTexture) { }
		}

		public static class DebugTextureAtlasStateListener<T extends ITextureSource> implements ITextureAtlasStateListener<T> {
			@Override
			public void onLoadedToHardware(final ITexture pTexture) {
				Debug.d("Texture loaded: " + pTexture.toString());
			}

			@Override
			public void onTextureSourceLoadExeption(final ITextureAtlas<T> pTextureAtlas, final T pTextureSource, final Throwable pThrowable) {
				Debug.e("Exception loading TextureSource. TextureAtlas: " + pTextureAtlas.toString() + " TextureSource: " + pTextureSource.toString(), pThrowable);
			}

			@Override
			public void onUnloadedFromHardware(final ITexture pTexture) {
				Debug.d("Texture unloaded: " + pTexture.toString());
			}
		}
	}
}
