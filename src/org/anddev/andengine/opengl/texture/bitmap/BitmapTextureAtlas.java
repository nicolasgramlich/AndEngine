package org.anddev.andengine.opengl.texture.bitmap;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.TextureAtlas;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.bitmap.source.IBitmapTextureSource;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.Debug;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:55:02 - 08.03.2010
 */
public class BitmapTextureAtlas extends TextureAtlas<IBitmapTextureSource> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final BitmapTextureFormat mBitmapTextureFormat;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses {@link BitmapTextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight) {
		this(pWidth, pHeight, BitmapTextureFormat.RGBA_8888);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} for {@link BitmapTextureAtlas}s with transparency and {@link BitmapTextureFormat#RGB_565} for {@link BitmapTextureAtlas}s without transparency.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat) {
		this(pWidth, pHeight, pBitmapTextureFormat, TextureOptions.DEFAULT, null);
	}

	/**
	 * Uses {@link BitmapTextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final ITextureAtlasStateListener<IBitmapTextureSource> pTextureAtlasStateListener) {
		this(pWidth, pHeight, BitmapTextureFormat.RGBA_8888, TextureOptions.DEFAULT, pTextureAtlasStateListener);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} for {@link BitmapTextureAtlas}s with transparency and {@link BitmapTextureFormat#RGB_565} for {@link BitmapTextureAtlas}s without transparency.
	 * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final ITextureAtlasStateListener<IBitmapTextureSource> pTextureAtlasStateListener) {
		this(pWidth, pHeight, pBitmapTextureFormat, TextureOptions.DEFAULT, pTextureAtlasStateListener);
	}

	/**
	 * Uses {@link BitmapTextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the BitmapTexture.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pWidth, pHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, null);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} for {@link BitmapTextureAtlas}s with transparency and {@link BitmapTextureFormat#RGB_565} for {@link BitmapTextureAtlas}s without transparency.
	 * @param pTextureOptions the (quality) settings of the BitmapTexture.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pWidth, pHeight, pBitmapTextureFormat, pTextureOptions, null);
	}

	/**
	 * Uses {@link BitmapTextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the BitmapTexture.
	 * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureAtlasStateListener<IBitmapTextureSource> pTextureAtlasStateListener) throws IllegalArgumentException {
		this(pWidth, pHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, pTextureAtlasStateListener);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} for {@link BitmapTextureAtlas}s with transparency and {@link BitmapTextureFormat#RGB_565} for {@link BitmapTextureAtlas}s without transparency.
	 * @param pTextureOptions the (quality) settings of the BitmapTexture.
	 * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final ITextureAtlasStateListener<IBitmapTextureSource> pTextureAtlasStateListener) throws IllegalArgumentException {
		super(pWidth, pHeight, pBitmapTextureFormat.getTextureFormat(), pTextureOptions, pTextureAtlasStateListener);

		this.mBitmapTextureFormat = pBitmapTextureFormat;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public BitmapTextureFormat getBitmapTextureFormat() {
		return this.mBitmapTextureFormat;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	protected void writeTextureToHardware(final GL10 pGL) {
		final Config bitmapConfig = this.mBitmapTextureFormat.getBitmapConfig();
		final int glFormat = this.mTextureFormat.getGLFormat();
		final int glDataType = this.mTextureFormat.getGLDataType();
		final boolean preMultipyAlpha = this.mTextureOptions.mPreMultipyAlpha;

		final ArrayList<IBitmapTextureSource> textureSources = this.mTextureSources;
		final int textureSourceCount = textureSources.size();

		for(int j = 0; j < textureSourceCount; j++) {
			final IBitmapTextureSource bitmapTextureSource = textureSources.get(j);
			if(bitmapTextureSource != null) {
				final Bitmap bitmap = bitmapTextureSource.onLoadBitmap(bitmapConfig);
				try {
					if(bitmap == null) {
						throw new IllegalArgumentException(bitmapTextureSource.getClass().getSimpleName() + ": " + bitmapTextureSource.toString() + " returned a null Bitmap.");
					}
					if(preMultipyAlpha) {
						GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTextureSource.getTexturePositionX(), bitmapTextureSource.getTexturePositionY(), bitmap, glFormat, glDataType);
					} else {
						GLHelper.glTexSubImage2D(pGL, GL10.GL_TEXTURE_2D, 0, bitmapTextureSource.getTexturePositionX(), bitmapTextureSource.getTexturePositionY(), bitmap, glFormat, glDataType, this.mTextureFormat);
					}

					bitmap.recycle();
				} catch (final IllegalArgumentException iae) {
					// TODO Load some static checkerboard or so to visualize that loading the texture has failed.
					//private Buffer createImage(final int width, final int height) {
					//	final int stride = 3 * width;
					//	final ByteBuffer image = ByteBuffer.allocateDirect(height * stride)
					//			.order(ByteOrder.nativeOrder());
					//
					//	// Fill with a pretty "munching squares" pattern:
					//	for (int t = 0; t < height; t++) {
					//		final byte red = (byte) (255 - 2 * t);
					//		final byte green = (byte) (2 * t);
					//		final byte blue = 0;
					//		for (int x = 0; x < width; x++) {
					//			final int y = x ^ t;
					//			image.position(stride * y + x * 3);
					//			image.put(red);
					//			image.put(green);
					//			image.put(blue);
					//		}
					//	}
					//	image.position(0);
					//	return image;
					//}

					Debug.e("Error loading: " + bitmapTextureSource.toString(), iae);
					if(this.getTextureStateListener() != null) {
						this.getTextureStateListener().onTextureSourceLoadExeption(this, bitmapTextureSource, iae);
					} else {
						throw iae;
					}
				}
			}
		}
	}

	@Override
	protected void bindTextureOnHardware(final GL10 pGL) {
		super.bindTextureOnHardware(pGL);

		this.sendPlaceholderBitmapToHardware();
	}

	private void sendPlaceholderBitmapToHardware() {
		final Bitmap textureBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, this.mBitmapTextureFormat.getBitmapConfig());
		// TODO Check if there is an easier/faster method to create a white placeholder bitmap.

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textureBitmap, 0);

		textureBitmap.recycle();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum BitmapTextureFormat {
		// ===========================================================
		// Elements
		// ===========================================================

		RGBA_4444( Config.ARGB_4444, TextureFormat.RGBA_4444),
		RGBA_8888(Config.ARGB_8888, TextureFormat.RGBA_8888),
		RGB_565( Config.RGB_565, TextureFormat.RGB_565),
		A_8( Config.ALPHA_8, TextureFormat.A_8);

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final Config mBitmapConfig;
		private final TextureFormat mTextureFormat;

		// ===========================================================
		// Constructors
		// ===========================================================

		private BitmapTextureFormat(final Config pBitmapConfig, final TextureFormat pTextureFormat) {
			this.mBitmapConfig = pBitmapConfig;
			this.mTextureFormat = pTextureFormat;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public Config getBitmapConfig() {
			return this.mBitmapConfig;
		}

		public TextureFormat getTextureFormat() {
			return this.mTextureFormat;
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
