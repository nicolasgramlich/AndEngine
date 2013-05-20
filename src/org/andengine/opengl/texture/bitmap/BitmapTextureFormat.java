package org.andengine.opengl.texture.bitmap;

import org.andengine.opengl.texture.PixelFormat;

import android.graphics.Bitmap.Config;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:36:45 - 05.04.2012
 */
public enum BitmapTextureFormat {
	// ===========================================================
	// Elements
	// ===========================================================

	RGBA_8888(Config.ARGB_8888, PixelFormat.RGBA_8888),
	RGB_565(Config.RGB_565, PixelFormat.RGB_565),
	RGBA_4444(Config.ARGB_4444, PixelFormat.RGBA_4444),
	A_8(Config.ALPHA_8, PixelFormat.A_8); // TODO

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Config mBitmapConfig;
	private final PixelFormat mPixelFormat;

	// ===========================================================
	// Constructors
	// ===========================================================

	private BitmapTextureFormat(final Config pBitmapConfig, final PixelFormat pPixelFormat) {
		this.mBitmapConfig = pBitmapConfig;
		this.mPixelFormat = pPixelFormat;
	}

	public static BitmapTextureFormat fromPixelFormat(final PixelFormat pPixelFormat) {
		switch (pPixelFormat) {
			case RGBA_8888:
				return RGBA_8888;
			case RGBA_4444:
				return RGBA_4444;
			case RGB_565:
				return RGB_565;
			case A_8:
				return A_8;
			default:
				throw new IllegalArgumentException("Unsupported " + PixelFormat.class.getName() + ": '" + pPixelFormat + "'.");
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Config getBitmapConfig() {
		return this.mBitmapConfig;
	}

	public PixelFormat getPixelFormat() {
		return this.mPixelFormat;
	}

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}