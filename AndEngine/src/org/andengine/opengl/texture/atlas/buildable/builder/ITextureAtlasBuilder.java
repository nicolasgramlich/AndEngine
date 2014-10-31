package org.andengine.opengl.texture.atlas.buildable.builder;

import java.util.ArrayList;

import org.andengine.opengl.texture.atlas.ITextureAtlas;
import org.andengine.opengl.texture.atlas.buildable.BuildableTextureAtlas.TextureAtlasSourceWithWithLocationCallback;
import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:59:14 - 12.08.2010
 */
public interface ITextureAtlasBuilder<T extends ITextureAtlasSource, A extends ITextureAtlas<T>> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void build(final A pTextureAtlas, final ArrayList<TextureAtlasSourceWithWithLocationCallback<T>> pTextureAtlasSourcesWithLocationCallback) throws TextureAtlasBuilderException;

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class TextureAtlasBuilderException extends Exception {
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

		public TextureAtlasBuilderException(final String pMessage) {
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
