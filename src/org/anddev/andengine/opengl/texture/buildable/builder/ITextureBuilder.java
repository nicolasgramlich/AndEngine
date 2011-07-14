package org.anddev.andengine.opengl.texture.buildable.builder;

import java.util.ArrayList;

import org.anddev.andengine.opengl.texture.ITextureAtlas;
import org.anddev.andengine.opengl.texture.buildable.BuildableTextureAtlas.TextureSourceWithWithLocationCallback;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:59:14 - 12.08.2010
 */
public interface ITextureBuilder<T extends ITextureSource, A extends ITextureAtlas<T>> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void pack(final A pTextureAtlas, final ArrayList<TextureSourceWithWithLocationCallback<T>> pTextureSourcesWithLocationCallback) throws TextureSourcePackingException;

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
