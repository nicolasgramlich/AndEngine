package org.anddev.andengine.opengl.texture;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
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

	private int mHardwareTextureID = -1;

	private final ArrayList<TextureSourceWithLocation> mTextureSources = new ArrayList<TextureSourceWithLocation>();
	private final TextureOptions mTextureOptions;
	private boolean mLoadedToHardware;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Texture(final int pWidth, final int pHeight) {
		this(pWidth, pHeight, TextureOptions.DEFAULT);
	}

	public Texture(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) {
		assert(MathUtils.isPowerOfTwo(pWidth) && MathUtils.isPowerOfTwo(pHeight));

		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mTextureOptions = pTextureOptions;
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

	void setLoadedToHardware(final boolean pLoadedToHardware) {
		this.mLoadedToHardware = pLoadedToHardware;
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

	public void addTextureSource(final ITextureSource pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
		this.mTextureSources.add(new TextureSourceWithLocation(pTextureSource, pTexturePositionX, pTexturePositionY));
	}

	public void loadToHardware(final GL10 pGL) {
		GLHelper.enableTextures(pGL);

		this.mHardwareTextureID = this.generateHardwareTextureID(pGL);

		this.allocateAndBindTextureOnHardware(pGL);

		this.applyTextureOptions(pGL);

		this.writeTextureToHardware();

		this.mLoadedToHardware = true;
	}

	private void writeTextureToHardware() {
		final ArrayList<TextureSourceWithLocation> textureSources = this.mTextureSources;
		final int textureSourceCount = textureSources.size();
		for(int j = 0; j < textureSourceCount; j++) {
			final TextureSourceWithLocation textureSource = textureSources.get(j);
			if(textureSource != null) {
				final Bitmap bmp = textureSource.getBitmap();
				GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, textureSource.getTexturePositionX(), textureSource.getTexturePositionY(), bmp);
				bmp.recycle();
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

		this.sendPlaceholderBitmapToHardware(this.mWidth, this.mHeight);
	}

	private int generateHardwareTextureID(final GL10 pGL) {
		pGL.glGenTextures(1, Texture.HARDWARETEXTUREID_FETCHER, 0);

		return Texture.HARDWARETEXTUREID_FETCHER[0];
	}

	private void sendPlaceholderBitmapToHardware(final int pWidth, final int pHeight) {
		final Bitmap textureBitmap = Bitmap.createBitmap(pWidth, pHeight, Bitmap.Config.ARGB_8888);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textureBitmap, 0);

		textureBitmap.recycle();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class TextureSourceWithLocation implements ITextureSource {

		private final ITextureSource mTextureSource;
		private final int mTexturePositionX;
		private final int mTexturePositionY;

		public TextureSourceWithLocation(final ITextureSource pTextureSource, final int pTexturePositionX, final int pTexturePositionY) {
			this.mTextureSource = pTextureSource;
			this.mTexturePositionX = pTexturePositionX;
			this.mTexturePositionY = pTexturePositionY;
		}

		public int getTexturePositionX() {
			return this.mTexturePositionX;
		}

		public int getTexturePositionY() {
			return this.mTexturePositionY;
		}

		@Override
		public int getWidth() {
			return this.mTextureSource.getWidth();
		}

		@Override
		public int getHeight() {
			return this.mTextureSource.getHeight();
		}

		@Override
		public Bitmap getBitmap() {
			return this.mTextureSource.getBitmap();
		}
	}
}
