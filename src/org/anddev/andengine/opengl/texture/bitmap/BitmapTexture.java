package org.anddev.andengine.opengl.texture.bitmap;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.MathUtils;
import org.anddev.andengine.util.StreamUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:16:25 - 30.07.2011
 */
public abstract class BitmapTexture extends Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;
	private final BitmapTextureFormat mBitmapTextureFormat;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BitmapTexture() throws IOException {
		this(BitmapTextureFormat.RGBA_8888, TextureOptions.DEFAULT, null);
	}

	public BitmapTexture(final BitmapTextureFormat pBitmapTextureFormat) throws IOException {
		this(pBitmapTextureFormat, TextureOptions.DEFAULT, null);
	}

	public BitmapTexture(final TextureOptions pTextureOptions) throws IOException {
		this(BitmapTextureFormat.RGBA_8888, pTextureOptions, null);
	}

	public BitmapTexture(final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions) throws IOException {
		this(pBitmapTextureFormat, pTextureOptions, null);
	}

	public BitmapTexture(final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IOException {
		super(pBitmapTextureFormat.getPixelFormat(), pTextureOptions, pTextureStateListener);
		this.mBitmapTextureFormat = pBitmapTextureFormat;

		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;

		final InputStream in = null;
		try {
			BitmapFactory.decodeStream(this.onGetInputStream(), null, decodeOptions);
		} finally {
			StreamUtils.close(in);
		}

		this.mWidth = decodeOptions.outWidth;
		this.mHeight = decodeOptions.outHeight;
		
		if(!MathUtils.isPowerOfTwo(this.mWidth) || !MathUtils.isPowerOfTwo(this.mHeight)) { // TODO GLHelper.EXTENSIONS_NON_POWER_OF_TWO
			throw new IllegalArgumentException("pWidth and pHeight must be a power of 2!");
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mWidth;
	}

	@Override
	public int getHeight() {
		return this.mHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract InputStream onGetInputStream() throws IOException;

	@Override
	protected void writeTextureToHardware(final GL10 pGL) throws IOException {
		final Config bitmapConfig = this.mBitmapTextureFormat.getBitmapConfig();
		final boolean preMultipyAlpha = this.mTextureOptions.mPreMultipyAlpha;

		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inPreferredConfig = bitmapConfig;

		final Bitmap bitmap = BitmapFactory.decodeStream(this.onGetInputStream(), null, decodeOptions);

		if(preMultipyAlpha) {
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		} else {
			GLHelper.glTexImage2D(pGL, GL10.GL_TEXTURE_2D, 0, bitmap, 0, this.mPixelFormat);
		}

		bitmap.recycle();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum BitmapTextureFormat {
		// ===========================================================
		// Elements
		// ===========================================================

		RGBA_8888(Config.ARGB_8888, PixelFormat.RGBA_8888),
		RGB_565(Config.RGB_565, PixelFormat.RGB_565),
		RGBA_4444(Config.ARGB_4444, PixelFormat.RGBA_4444), // TODO
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
			switch(pPixelFormat) {
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
}
