package org.andengine.opengl.texture.atlas;

import org.andengine.BuildConfig;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.ITextureStateListener;
import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;
import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:24:29 - 14.07.2011
 */
public interface ITextureAtlas<T extends ITextureAtlasSource> extends ITexture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY) throws IllegalArgumentException;
	public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY, final int pTextureAtlasSourcePadding) throws IllegalArgumentException;
	public void addEmptyTextureAtlasSource(final int pTextureX, final int pTextureY, final int pWidth, final int pHeight);
	public void removeTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY);
	public void clearTextureAtlasSources();

	@Deprecated
	@Override
	public boolean hasTextureStateListener();
	public boolean hasTextureAtlasStateListener();

	@Deprecated
	@Override
	public ITextureAtlasStateListener<T> getTextureStateListener();
	public ITextureAtlasStateListener<T> getTextureAtlasStateListener();

	@Deprecated
	@Override
	public void setTextureStateListener(final ITextureStateListener pTextureStateListener);
	public void setTextureAtlasStateListener(final ITextureAtlasStateListener<T> pTextureAtlasStateListener);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ITextureAtlasStateListener<T extends ITextureAtlasSource> extends ITextureStateListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onTextureAtlasSourceLoaded(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource);
		public void onTextureAtlasSourceLoadExeption(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource, final Throwable pThrowable);

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class TextureAtlasStateAdapter<T extends ITextureAtlasSource> implements ITextureAtlasStateListener<T> {
			@Override
			public void onLoadedToHardware(final ITexture pTexture) { }

			@Override
			public void onTextureAtlasSourceLoaded(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource) { }

			@Override
			public void onTextureAtlasSourceLoadExeption(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource, final Throwable pThrowable) { }

			@Override
			public void onUnloadedFromHardware(final ITexture pTexture) { }
		}

		public static class DebugTextureAtlasStateListener<T extends ITextureAtlasSource> implements ITextureAtlasStateListener<T> {
			@Override
			public void onLoadedToHardware(final ITexture pTexture) {
				if(BuildConfig.DEBUG) {
					Debug.d("Texture loaded: " + pTexture.toString());
				}
			}

			@Override
			public void onTextureAtlasSourceLoaded(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource) {
				Debug.e("Loaded TextureAtlasSource. TextureAtlas: " + pTextureAtlas.toString() + " TextureAtlasSource: " + pTextureAtlasSource.toString());
			}

			@Override
			public void onTextureAtlasSourceLoadExeption(final ITextureAtlas<T> pTextureAtlas, final T pTextureAtlasSource, final Throwable pThrowable) {
				Debug.e("Exception loading TextureAtlasSource. TextureAtlas: " + pTextureAtlas.toString() + " TextureAtlasSource: " + pTextureAtlasSource.toString(), pThrowable);
			}

			@Override
			public void onUnloadedFromHardware(final ITexture pTexture) {
				if(BuildConfig.DEBUG) {
					Debug.d("Texture unloaded: " + pTexture.toString());
				}
			}
		}
	}
}
