package org.anddev.andengine.opengl.texture;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.MathUtils;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * @author Nicolas Gramlich
 * @since 14:55:02 - 08.03.2010
 */
public class Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] HARDWARETEXTUREID_FETCHER = new int[1];

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;

	private boolean mLoadedToHardware;
	private int mHardwareTextureID = -1;
	private final TextureOptions mTextureOptions;

	private final ArrayList<TextureSourceWithLocation> mTextureSources = new ArrayList<TextureSourceWithLocation>();

	private final ITextureStateListener mTextureStateListener;

	protected boolean mUpdateOnHardwareNeeded = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 */
	public Texture(final int pWidth, final int pHeight) {
		this(pWidth, pHeight, TextureOptions.DEFAULT, null);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureStateListener to be informed when this {@link Texture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public Texture(final int pWidth, final int pHeight, final ITextureStateListener pTextureStateListener) {
		this(pWidth, pHeight, TextureOptions.DEFAULT, pTextureStateListener);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the Texture.
	 */
	public Texture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pWidth, pHeight, pTextureOptions, null);
	}

	/**
	 * @param pWidth must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pHeight must be a power of 2 (i.e. 32, 64, 128, 256, 512, 1024).
	 * @param pTextureOptions the (quality) settings of the Texture.
	 * @param pTextureStateListener to be informed when this {@link Texture} is loaded, unloaded or a {@link ITextureSource} failed to load.
	 */
	public Texture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException {
		if (!MathUtils.isPowerOfTwo(pWidth) || !MathUtils.isPowerOfTwo(pHeight)){
			throw new IllegalArgumentException("pWidth and pHeight must be a power of 2!");
		}

		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mTextureOptions = pTextureOptions;
		this.mTextureStateListener = pTextureStateListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getHardwareTextureID() {
		return this.mHardwareTextureID;
	}

	public boolean isLoadedToHardware() {
		return this.mLoadedToHardware;
	}

	public boolean isUpdateOnHardwareNeeded() {
		return this.mUpdateOnHardwareNeeded;
	}

	void setLoadedToHardware(final boolean pLoadedToHardware) {
		this.mLoadedToHardware = pLoadedToHardware;
	}

	public int getWidth() {
		return this.mWidth;
	}

	public int getHeight() {
		return this.mHeight;
	}

	public TextureOptions getTextureOptions() {
		return this.mTextureOptions;
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

	public void loadToHardware(final GL10 pGL) {
		GLHelper.enableTextures(pGL);

		this.mHardwareTextureID = Texture.generateHardwareTextureID(pGL);

		this.allocateAndBindTextureOnHardware(pGL);

		this.applyTextureOptions(pGL);

		this.writeTextureToHardware(pGL);

		this.mUpdateOnHardwareNeeded = false;
		this.mLoadedToHardware = true;

		if(this.mTextureStateListener != null) {
			this.mTextureStateListener.onLoadedToHardware(this);
		}
	}

	public void unloadFromHardware(final GL10 pGL) {
		GLHelper.enableTextures(pGL);

		this.deleteTextureOnHardware(pGL);

		this.mHardwareTextureID = -1;

		this.mLoadedToHardware = false;

		if(this.mTextureStateListener != null) {
			this.mTextureStateListener.onUnloadedFromHardware(this);
		}
	}

	private void writeTextureToHardware(final GL10 pGL) {
		final boolean preMultipyAlpha = this.mTextureOptions.mPreMultipyAlpha;

		final ArrayList<TextureSourceWithLocation> textureSources = this.mTextureSources;
		final int textureSourceCount = textureSources.size();

		for(int j = 0; j < textureSourceCount; j++) {
			final TextureSourceWithLocation textureSourceWithLocation = textureSources.get(j);
			if(textureSourceWithLocation != null) {
				final Bitmap bmp = textureSourceWithLocation.onLoadBitmap();
				try{
					if(bmp == null) {
						throw new IllegalArgumentException("TextureSource: " + textureSourceWithLocation.toString() + " returned a null Bitmap.");
					}
					if(preMultipyAlpha) {
						GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, textureSourceWithLocation.getTexturePositionX(), textureSourceWithLocation.getTexturePositionY(), bmp, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE);
					} else {
						GLHelper.glTexSubImage2D(pGL, GL10.GL_TEXTURE_2D, 0, textureSourceWithLocation.getTexturePositionX(), textureSourceWithLocation.getTexturePositionY(), bmp, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE);
					}

					bmp.recycle();
				} catch (final IllegalArgumentException iae) {
					// TODO Load some static checkerboard or so to visualize that loading the texture has failed.
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

	private void applyTextureOptions(final GL10 pGL) {
		final TextureOptions textureOptions = this.mTextureOptions;
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, textureOptions.mMinFilter);
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, textureOptions.mMagFilter);
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, textureOptions.mWrapS);
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, textureOptions.mWrapT);
		pGL.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, textureOptions.mTextureEnvironment);
	}

	private void allocateAndBindTextureOnHardware(final GL10 pGL) {
		GLHelper.bindTexture(pGL, this.mHardwareTextureID);

		Texture.sendPlaceholderBitmapToHardware(this.mWidth, this.mHeight);
	}

	private void deleteTextureOnHardware(final GL10 pGL) {
		GLHelper.deleteTexture(pGL, this.mHardwareTextureID);
	}

	private static int generateHardwareTextureID(final GL10 pGL) {
		pGL.glGenTextures(1, Texture.HARDWARETEXTUREID_FETCHER, 0);

		return Texture.HARDWARETEXTUREID_FETCHER[0];
	}

	private static void sendPlaceholderBitmapToHardware(final int pWidth, final int pHeight) {
		final Bitmap textureBitmap = Bitmap.createBitmap(pWidth, pHeight, Bitmap.Config.ARGB_8888);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textureBitmap, 0);

		textureBitmap.recycle();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ITextureStateListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onLoadedToHardware(final Texture pTexture);
		public void onTextureSourceLoadExeption(final Texture pTexture, final ITextureSource pTextureSource, final Throwable pThrowable);
		public void onUnloadedFromHardware(final Texture pTexture);

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class TextureStateAdapter implements ITextureStateListener {
			@Override
			public void onLoadedToHardware(final Texture pTexture) { }

			@Override
			public void onTextureSourceLoadExeption(final Texture pTexture, final ITextureSource pTextureSource, final Throwable pThrowable) { }

			@Override
			public void onUnloadedFromHardware(final Texture pTexture) { }
		}

		public static class DebugTextureStateListener implements ITextureStateListener {
			@Override
			public void onLoadedToHardware(final Texture pTexture) {
				Debug.d("Texture loaded: " + pTexture.toString());
			}

			@Override
			public void onTextureSourceLoadExeption(final Texture pTexture, final ITextureSource pTextureSource, final Throwable pThrowable) {
				Debug.e("Exception loading TextureSource. Texture: " + pTexture.toString() + " TextureSource: " + pTextureSource.toString(), pThrowable);
			}

			@Override
			public void onUnloadedFromHardware(final Texture pTexture) {
				Debug.d("Texture unloaded: " + pTexture.toString());
			}
		}
	}

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

		public Bitmap onLoadBitmap() {
			return this.mTextureSource.onLoadBitmap();
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
}
