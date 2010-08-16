package org.anddev.andengine.opengl.texture.builder;

import java.util.ArrayList;

import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.BuildableTexture.TextureSourceWithWithLocationCallback;

/**
 * @author Nicolas Gramlich
 * @since 15:59:14 - 12.08.2010
 */
public interface ITextureBuilder {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void pack(final BuildableTexture pBuildableTexture, final ArrayList<TextureSourceWithWithLocationCallback> pTextureSourcesWithLocationCallback) throws TextureSourcePackingException;

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
