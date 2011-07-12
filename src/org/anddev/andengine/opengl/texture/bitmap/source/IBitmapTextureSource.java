package org.anddev.andengine.opengl.texture.bitmap.source;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * @author Nicolas Gramlich
 * @since 12:08:52 - 09.03.2010
 */
public interface IBitmapTextureSource extends ITextureSource {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public IBitmapTextureSource clone();

	public Bitmap onLoadBitmap(final Config pBitmapConfig);
}