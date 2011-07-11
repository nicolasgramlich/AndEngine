package org.anddev.andengine.opengl.texture;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.MathUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLUtils;

/**
 * @author Nicolas Gramlich
 * @since 14:55:02 - 08.03.2010
 */
public class Texture extends BaseTexture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;
	private final TextureFormat mTextureFormat;

	private final ArrayList<TextureSourceWithLocation> mTextureSources = new ArrayList<TextureSourceWithLocation>();

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses {@link TextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 */
	public Texture(final int pWidth, final int pHeight) {
		this(pWidth, pHeight, TextureFormat.RGBA_8888);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureFormat use {@link TextureFormat#RGBA_8888} for {@link Texture}s with transparency and {@link TextureFormat#RGB_565} for {@link Texture}s without transparency.
	 */
	public Texture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat) {
		this(pWidth, pHeight, pTextureFormat, TextureOptions.DEFAULT, null);
	}

	/**
	 * Uses {@link TextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureStateListener to be informed when this {@link Texture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public Texture(final int pWidth, final int pHeight, final ITextureStateListener pTextureStateListener) {
		this(pWidth, pHeight, TextureFormat.RGBA_8888, TextureOptions.DEFAULT, pTextureStateListener);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureFormat use {@link TextureFormat#RGBA_8888} for {@link Texture}s with transparency and {@link TextureFormat#RGB_565} for {@link Texture}s without transparency.
	 * @param pTextureStateListener to be informed when this {@link Texture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public Texture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat, final ITextureStateListener pTextureStateListener) {
		this(pWidth, pHeight, pTextureFormat, TextureOptions.DEFAULT, pTextureStateListener);
	}

	/**
	 * Uses {@link TextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the Texture.
	 */
	public Texture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pWidth, pHeight, TextureFormat.RGBA_8888, pTextureOptions, null);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureFormat use {@link TextureFormat#RGBA_8888} for {@link Texture}s with transparency and {@link TextureFormat#RGB_565} for {@link Texture}s without transparency.
	 * @param pTextureOptions the (quality) settings of the Texture.
	 */
	public Texture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pWidth, pHeight, pTextureFormat, pTextureOptions, null);
	}

	/**
	 * Uses {@link TextureFormat#RGBA_8888}.
	 *
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the Texture.
	 * @param pTextureStateListener to be informed when this {@link Texture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public Texture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException {
		this(pWidth, pHeight, TextureFormat.RGBA_8888, pTextureOptions, pTextureStateListener);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureFormat use {@link TextureFormat#RGBA_8888} for {@link Texture}s with transparency and {@link TextureFormat#RGB_565} for {@link Texture}s without transparency.
	 * @param pTextureOptions the (quality) settings of the Texture.
	 * @param pTextureStateListener to be informed when this {@link Texture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public Texture(final int pWidth, final int pHeight, final TextureFormat pTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException {
		super(pTextureOptions, pTextureStateListener);
		if (!MathUtils.isPowerOfTwo(pWidth) || !MathUtils.isPowerOfTwo(pHeight)){
			throw new IllegalArgumentException("pWidth and pHeight must be a power of 2!");
		}
		this.mTextureFormat = pTextureFormat;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public TextureFormat getTextureFormat() {
		return this.mTextureFormat;
	}

	public int getWidth() {
		return this.mWidth;
	}

	public int getHeight() {
		return this.mHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public TextureSourceWithLocation addTextureSource(final ITextureSource pTextureSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException {
		this.checkTextureSourcePosition(pTextureSource, pTexturePositionX, pTexturePositionY);

		final TextureSourceWithLocation textureSourceWithLocation = new TextureSourceWithLocation(pTextureSource, pTexturePositionX, pTexturePositionY);
		this.mTextureSources.add(textureSourceWithLocation);
		this.mUpdateOnHardwareNeeded = true;
		return textureSourceWithLocation;
	}

	private void checkTextureSourcePosition(final ITextureSource pTextureSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException {
		if(pTexturePositionX < 0) {
			throw new IllegalArgumentException("Illegal negative pTexturePositionX supplied: '" + pTexturePositionX + "'");
		} else if(pTexturePositionY < 0) {
			throw new IllegalArgumentException("Illegal negative pTexturePositionY supplied: '" + pTexturePositionY + "'");
		} else if(pTexturePositionX + pTextureSource.getWidth() > this.mWidth || pTexturePositionY + pTextureSource.getHeight() > this.mHeight) {
			throw new IllegalArgumentException("Supplied pTextureSource must not exceed bounds of Texture.");
		}
	}

	public void removeTextureSource(final ITextureSource pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		final ArrayList<TextureSourceWithLocation> textureSources = this.mTextureSources;
		for(int i = textureSources.size() - 1; i >= 0; i--) {
			final TextureSourceWithLocation textureSourceWithLocation = textureSources.get(i);
			if(textureSourceWithLocation.mTextureSource == pTextureSource && textureSourceWithLocation.mTexturePositionX == pTexturePositionX && textureSourceWithLocation.mTexturePositionY == pTexturePositionY) {
				textureSources.remove(i);
				this.mUpdateOnHardwareNeeded = true;
				return;
			}
		}
	}

	public void clearTextureSources() {
		this.mTextureSources.clear();
		this.mUpdateOnHardwareNeeded = true;
	}

	@Override
	protected void writeTextureToHardware(final GL10 pGL) {
		final Config bitmapConfig = this.mTextureFormat.getBitmapConfig();
		final int glFormat = this.mTextureFormat.getGLFormat();
		final int glDataType = this.mTextureFormat.getGLDataType();
		final boolean preMultipyAlpha = this.mTextureOptions.mPreMultipyAlpha;

		final ArrayList<TextureSourceWithLocation> textureSources = this.mTextureSources;
		final int textureSourceCount = textureSources.size();

		for(int j = 0; j < textureSourceCount; j++) {
			final TextureSourceWithLocation textureSourceWithLocation = textureSources.get(j);
			if(textureSourceWithLocation != null) {
				// TODO Add support for compressed Textures (ETC1).
				// GLHelper.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);

				final Bitmap bmp = textureSourceWithLocation.onLoadBitmap(bitmapConfig);
				try {
					if(bmp == null) {
						throw new IllegalArgumentException("TextureSource: " + textureSourceWithLocation.toString() + " returned a null Bitmap.");
					}
					if(preMultipyAlpha) {
						GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, textureSourceWithLocation.getTexturePositionX(), textureSourceWithLocation.getTexturePositionY(), bmp, glFormat, glDataType);
					} else {
						GLHelper.glTexSubImage2D(pGL, GL10.GL_TEXTURE_2D, 0, textureSourceWithLocation.getTexturePositionX(), textureSourceWithLocation.getTexturePositionY(), bmp, glFormat, glDataType, this.mTextureFormat);
					}

					bmp.recycle();
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

					Debug.e("Error loading: " + textureSourceWithLocation.toString(), iae);
					if(this.mTextureStateListener != null) {
						this.mTextureStateListener.onTextureSourceLoadExeption(this, textureSourceWithLocation.mTextureSource, iae);
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

		this.sendPlaceholderBitmapToHardware(pGL, this.mWidth, this.mHeight);
	}

	private void sendPlaceholderBitmapToHardware(final GL10 pGL, final int pWidth, final int pHeight) {
		final Bitmap textureBitmap = Bitmap.createBitmap(pWidth, pHeight, this.mTextureFormat.getBitmapConfig());
		// TODO Check if there is an easier/faster method to create a white placeholder bitmap.

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textureBitmap, 0);

		textureBitmap.recycle();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class TextureSourceWithLocation {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final ITextureSource mTextureSource;
		private final int mTexturePositionX;
		private final int mTexturePositionY;

		// ===========================================================
		// Constructors
		// ===========================================================

		public TextureSourceWithLocation(final ITextureSource pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
			this.mTextureSource = pTextureSource;
			this.mTexturePositionX = pTexturePositionX;
			this.mTexturePositionY = pTexturePositionY;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getTexturePositionX() {
			return this.mTexturePositionX;
		}

		public int getTexturePositionY() {
			return this.mTexturePositionY;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		public int getWidth() {
			return this.mTextureSource.getWidth();
		}

		public int getHeight() {
			return this.mTextureSource.getHeight();
		}

		public Bitmap onLoadBitmap(final Config pBitmapConfig) {
			return this.mTextureSource.onLoadBitmap(pBitmapConfig);
		}

		@Override
		public String toString() {
			return this.mTextureSource.toString();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public enum TextureFormat {
		// ===========================================================
		// Elements
		// ===========================================================

		RGBA_8888(GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, Config.ARGB_8888),
		RGB_565(GL10.GL_RGB, GL10.GL_UNSIGNED_SHORT_5_6_5, Config.RGB_565);

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mGLFormat;
		private final int mGLDataType;
		private final Config mBitmapConfig;

		// ===========================================================
		// Constructors
		// ===========================================================

		private TextureFormat(final int pGLFormat, final int pGLDataType, final Config pBitmapConfig) {
			this.mGLFormat = pGLFormat;
			this.mGLDataType = pGLDataType;
			this.mBitmapConfig = pBitmapConfig;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getGLFormat() {
			return this.mGLFormat;
		}

		public int getGLDataType() {
			return this.mGLDataType;
		}

		public Config getBitmapConfig() {
			return this.mBitmapConfig;
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
