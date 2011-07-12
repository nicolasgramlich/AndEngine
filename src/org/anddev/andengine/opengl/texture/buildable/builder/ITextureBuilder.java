package org.anddev.andengine.opengl.texture.buildable.builder;

import java.util.ArrayList;

import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.buildable.BuildableTexture.TextureSourceWithWithLocationCallback;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * @author Nicolas Gramlich
 * @since 15:59:14 - 12.08.2010
 */
public interface ITextureBuilder<T extends ITextureSource, K extends ITexture<T>> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void pack(final K pTexture, final ArrayList<TextureSourceWithWithLocationCallback<T>> pTextureSourcesWithLocationCallback) throws TextureSourcePackingException;

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class TextureSourcePackingException extends Exception {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = 4700734424214372671L;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public TextureSourcePackingException(final String pMessage) {
			super(pMessage);
		}

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
		// Inner and Anonymous Classes
		// ===========================================================
	}

}
