package org.anddev.andengine.opengl.texture.source.packing;

import java.util.ArrayList;

import org.anddev.andengine.opengl.texture.PackableTexture;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * @author Nicolas Gramlich
 * @since 15:59:14 - 12.08.2010
 */
public interface ITextureSourcePackingAlgorithm {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void pack(final PackableTexture pPackableTexture, final ArrayList<ITextureSource> pTextureSources) throws TextureSourcePackingException;

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
