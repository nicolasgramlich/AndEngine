package org.anddev.andengine.opengl.texture.source;

import android.graphics.Bitmap;

/**
 * @author Nicolas Gramlich
 * @since 12:08:52 - 09.03.2010
 */
public interface ITextureSource extends Cloneable {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getWidth();
	public int getHeight();

	public ITextureSource clone();

	public Bitmap onLoadBitmap();
}