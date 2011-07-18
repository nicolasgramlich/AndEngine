package org.anddev.andengine.opengl.texture.atlas.bitmap;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.TextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.source.ITextureAtlasSource;
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
public class BitmapTextureAtlas extends TextureAtlas<IBitmapTextureAtlasSource> {
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
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} for {@link BitmapTextureAtlas}MAGIC_CONSTANT with transparency and {@link BitmapTextureFormat#RGB_565} for {@link BitmapTextureAtlas}MAGIC_CONSTANT without transparency.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat) {
		this(pWidth, pHeight, pBitmapTextureFormat, TextureOptions.DEFAULT, null);
	}

	/**
	 * Uses {@link BitmapTextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureAtlasSource} failed to load.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final ITextureAtlasStateListener<IBitmapTextureAtlasSource> pTextureAtlasStateListener) {
		this(pWidth, pHeight, BitmapTextureFormat.RGBA_8888, TextureOptions.DEFAULT, pTextureAtlasStateListener);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} for {@link BitmapTextureAtlas}MAGIC_CONSTANT with transparency and {@link BitmapTextureFormat#RGB_565} for {@link BitmapTextureAtlas}MAGIC_CONSTANT without transparency.
	 * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureAtlasSource} failed to load.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final ITextureAtlasStateListener<IBitmapTextureAtlasSource> pTextureAtlasStateListener) {
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
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} for {@link BitmapTextureAtlas}MAGIC_CONSTANT with transparency and {@link BitmapTextureFormat#RGB_565} for {@link BitmapTextureAtlas}MAGIC_CONSTANT without transparency.
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
	 * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureAtlasSource} failed to load.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureAtlasStateListener<IBitmapTextureAtlasSource> pTextureAtlasStateListener) throws IllegalArgumentException {
		this(pWidth, pHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, pTextureAtlasStateListener);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pBitmapTextureFormat use {@link BitmapTextureFormat#RGBA_8888} for {@link BitmapTextureAtlas}MAGIC_CONSTANT with transparency and {@link BitmapTextureFormat#RGB_565} for {@link BitmapTextureAtlas}MAGIC_CONSTANT without transparency.
	 * @param pTextureOptions the (quality) settings of the BitmapTexture.
	 * @param pTextureAtlasStateListener to be informed when this {@link BitmapTextureAtlas} is loaded, unloaded or a {@link ITextureAtlasSource} failed to load.
	 */
	public BitmapTextureAtlas(final int pWidth, final int pHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final ITextureAtlasStateListener<IBitmapTextureAtlasSource> pTextureAtlasStateListener) throws IllegalArgumentException {
		super(pWidth, pHeight, pBitmapTextureFormat.getPixelFormat(), pTextureOptions, pTextureAtlasStateListener);

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
		final int glFormat = this.mPixelFormat.getGLFormat();
		final int glType = this.mPixelFormat.getGLType();
		final boolean preMultipyAlpha = this.mTextureOptions.mPreMultipyAlpha;

		final ArrayList<IBitmapTextureAtlasSource> textureSources = this.mTextureAtlasSources;
		final int textureSourceCount = textureSources.size();

		for(int j = 0; j < textureSourceCount; j++) {
			final IBitmapTextureAtlasSource bitmapTextureAtlasSource = textureSources.get(j);
			if(bitmapTextureAtlasSource != null) {
				final Bitmap bitmap = bitmapTextureAtlasSource.onLoadBitmap(bitmapConfig);
				try {
					if(bitmap == null) {
						throw new IllegalArgumentException(bitmapTextureAtlasSource.getClass().getSimpleName() + ": " + bitmapTextureAtlasSource.toString() + " returned a null Bitmap.");
					}
					if(preMultipyAlpha) {
						GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTextureAtlasSource.getTexturePositionX(), bitmapTextureAtlasSource.getTexturePositionY(), bitmap, glFormat, glType);
					} else {
						GLHelper.glTexSubImage2D(pGL, GL10.GL_TEXTURE_2D, 0, bitmapTextureAtlasSource.getTexturePositionX(), bitmapTextureAtlasSource.getTexturePositionY(), bitmap, this.mPixelFormat);
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

					Debug.e("Error loading: " + bitmapTextureAtlasSource.toString(), iae);
					if(this.getTextureStateListener() != null) {
						this.getTextureStateListener().onTextureAtlasSourceLoadExeption(this, bitmapTextureAtlasSource, iae);
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

		RGBA_4444( Config.ARGB_4444, PixelFormat.RGBA_4444), // TODO
		RGBA_8888(Config.ARGB_8888, PixelFormat.RGBA_8888),
		RGB_565( Config.RGB_565, PixelFormat.RGB_565),
		A_8( Config.ALPHA_8, PixelFormat.A_8); // TODO

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
