package org.anddev.andengine.opengl.texture;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.util.MathUtils;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * @author Nicolas Gramlich
 * @since 14:55:02 - 08.03.2010
 */
public class TextureAtlas {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;

	private int mHardwareTextureID = -1;

	private final ArrayList<Texture> mTextures = new ArrayList<Texture>();
	private final TextureOptions mTextureOptions;
	private boolean mLoadedToHardware;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureAtlas(final int pWidth, final int pHeight) {
		this(pWidth, pHeight, new TextureOptions());
	}

	public TextureAtlas(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) {
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

	public void insert(final Texture pTexture, final int pX, final int pY) {
		this.mTextures.add(pTexture);
		pTexture.setTextureAtlas(this);
		pTexture.setAtlasPosition(pX, pY);
	}

	public Texture extract(final int pAtlasPositionX, final int pAtlasPositionY, final int pWidth, final int pHeight) {
		return new Texture(this, pAtlasPositionX, pAtlasPositionY, pWidth, pHeight);
	}

	public void loadToHardware(final GL10 pGL) {
		GLHelper.enableTextures(pGL);

		this.mHardwareTextureID = allocateAndBindTextureOnHardware(pGL, this.mWidth, this.mHeight);

		applyTextureOptions(pGL, this.mTextureOptions);

		writeTexturesToHardware(this.mTextures);

		this.mLoadedToHardware = true;     
	}

	private static void writeTexturesToHardware(final ArrayList<Texture> pTextures) {
		final int textureCount = pTextures.size();
		for(int j = 0; j < textureCount; j++) {
			final Texture texture = pTextures.get(j);
			if(texture != null) {
				final Bitmap bmp = texture.getBitmap();
				GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, texture.getAtlasPositionX(), texture.getAtlasPositionY(), bmp);
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
		final Bitmap atlasBitmap = Bitmap.createBitmap(pWidth, pHeight, Bitmap.Config.ARGB_8888);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, atlasBitmap, 0);
		atlasBitmap.recycle();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

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
