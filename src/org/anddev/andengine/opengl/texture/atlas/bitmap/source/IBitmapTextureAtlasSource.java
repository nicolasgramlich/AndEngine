package org.anddev.andengine.opengl.texture.atlas.bitmap.source;

import org.anddev.andengine.opengl.texture.source.ITextureAtlasSource;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:08:52 - 09.03.2010
 */
public interface IBitmapTextureAtlasSource extends ITextureAtlasSource {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public IBitmapTextureAtlasSource clone();

	public Bitmap onLoadBitmap(final Config pBitmapConfig);
}