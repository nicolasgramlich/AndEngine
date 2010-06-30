package org.anddev.andengine.opengl.texture;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.util.Debug;

/**
 * @author Nicolas Gramlich
 * @since 10:56:14 - 30.06.2010
 */
public interface ITextureStateListener {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onLoadedToHardware(final Texture pTexture);
	public void onTextureSourceLoadExeption(final Texture pTexture, final ITextureSource pTextureSource, final Throwable pThrowable);
	public void onUnloadedFromHardware(final Texture pTexture);

	// ===========================================================
	// Methods
	// ===========================================================

	public static class TextureStateAdapter implements ITextureStateListener {
		@Override
		public void onLoadedToHardware(final Texture pTexture) { }

		@Override
		public void onTextureSourceLoadExeption(final Texture pTexture, final ITextureSource pTextureSource, final Throwable pThrowable) { }

		@Override
		public void onUnloadedFromHardware(final Texture pTexture) { }
	}

	public static class DebugTextureStateListener implements ITextureStateListener {
		@Override
		public void onLoadedToHardware(final Texture pTexture) {
			Debug.d("Texture loaded: " + pTexture.toString());
		}

		@Override
		public void onTextureSourceLoadExeption(final Texture pTexture, final ITextureSource pTextureSource, final Throwable pThrowable) {
			Debug.e("Exception loading TextureSource. Texture: " + pTexture.toString() + " TextureSource: " + pTextureSource.toString(), pThrowable);
		}

		@Override
		public void onUnloadedFromHardware(final Texture pTexture) {
			Debug.d("Texture unloaded: " + pTexture.toString());
		}
	}
}