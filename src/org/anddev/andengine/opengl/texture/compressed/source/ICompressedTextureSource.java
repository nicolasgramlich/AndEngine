package org.anddev.andengine.opengl.texture.compressed.source;

import java.io.InputStream;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * @author Nicolas Gramlich
 * @since 11:12:13 - 12.07.2011
 */
public interface ICompressedTextureSource extends ITextureSource, Cloneable {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public ICompressedTextureSource clone();

	public InputStream onGetInputStream();
}