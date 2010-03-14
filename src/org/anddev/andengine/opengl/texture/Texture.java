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
		this(pWidth, pHeight, new TextureOptions());
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

	public void insertTextureRegion(final TextureRegion pTextureRegion, final ITextureSource pTextureSource) {
		this.mTextureSources.add(new TextureSourceWithLocation(pTextureSource, pTextureRegion.getTexturePositionX(), pTextureRegion.getTexturePositionY()));
		pTextureRegion.setTexture(this);
	}

	public void associateTextureRegion(final TextureRegion pTextureRegion) {
		pTextureRegion.setTexture(this);
	}

	public void loadToHardware(final GL10 pGL) {
		GLHelper.enableTextures(pGL);

		this.mHardwareTextureID = allocateAndBindTextureOnHardware(pGL, this.mWidth, this.mHeight);

		applyTextureOptions(pGL, this.mTextureOptions);

		writeTextureToHardware(this.mTextureSources);

		this.mLoadedToHardware = true;     
	}

	private static void writeTextureToHardware(final ArrayList<TextureSourceWithLocation> pTextureSources) {
		final int textureSourceCount = pTextureSources.size();
		for(int j = 0; j < textureSourceCount; j++) {
			final TextureSourceWithLocation textureSource = pTextureSources.get(j);
			if(textureSource != null) {
				final Bitmap bmp = textureSource.getBitmap();
				GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, textureSource.getTexturePositionX(), textureSource.getTexturePositionY(), bmp);
				bmp.recycle();
			}
		}
	}

	private static void applyTextureOptions(final GL10 pGL, final TextureOptions pTextureOptions) {
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, pTextureOptions.mMinFilter);
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, pTextureOptions.mMagFilter);
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		pGL.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		pGL.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, pTextureOptions.mTextureEnvironment);
	}

	private static int allocateAndBindTextureOnHardware(final GL10 pGL, final int pWidth, final int pHeight) {
		final int hardwareTextureID = generateHardwareTextureID(pGL);
		
		pGL.glBindTexture(GL10.GL_TEXTURE_2D, hardwareTextureID);

		sendPlaceholderBitmapToHardware(pWidth, pHeight);
		
		return hardwareTextureID;
	}

	private static int generateHardwareTextureID(final GL10 pGL) {
		final int[] hardwareTextureIDFether = new int[1];
		pGL.glGenTextures(1, hardwareTextureIDFether, 0);
		
		return hardwareTextureIDFether[0];
	}

	private static void sendPlaceholderBitmapToHardware(final int pWidth, final int pHeight) {
		final Bitmap textureBitmap = Bitmap.createBitmap(pWidth, pHeight, Bitmap.Config.ARGB_8888);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textureBitmap, 0);
		textureBitmap.recycle();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public class TextureSourceWithLocation implements ITextureSource {
		
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

	public static class TextureOptions {
		public final int mMagFilter;
		public final int mMinFilter;
		public final int mTextureEnvironment;

		public TextureOptions() {
			this(GL10.GL_NEAREST, GL10.GL_LINEAR, GL10.GL_MODULATE);
		}

		public TextureOptions(final int pMinFilter, final int pMagFilter) {
			this(pMinFilter, pMagFilter, GL10.GL_MODULATE);
		}

		public TextureOptions(final int pMinFilter, final int pMagFilter, final int pTextureEnvironment) {
			this.mMinFilter = pMinFilter;
			this.mMagFilter = pMagFilter;
			this.mTextureEnvironment = pTextureEnvironment;
		}
	}
}
