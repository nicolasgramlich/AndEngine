package org.anddev.andengine.opengl.texture.source.packing;

import org.anddev.andengine.opengl.texture.Texture;
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

	public void pack(final Texture pTexture, final ITextureSource[] pTextureSources);
}
