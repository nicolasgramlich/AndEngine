package org.anddev.andengine.opengl.texture;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

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

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public TextureAtlas(final int pWidth, final int pHeight) {
		this(pWidth, pHeight, new TextureOptions());
	}
	
	public TextureAtlas(final int pWidth, final int pHeight, final TextureOptions pTextureOptions) {
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mTextureOptions = pTextureOptions;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
    public void insert(final Texture pTexture, final int pX, final int pY) {
        this.mTextures.add(pTexture);
        pTexture.setAtlasPosition(pX, pY);
    }
	
	public Texture extract(final int pAtlasPositionX, final int pAtlasPositionY, final int pWidth, final int pHeight) {
		return new Texture(this, pAtlasPositionX, pAtlasPositionY, pWidth, pHeight);
	}
	
	public void loadToHardware(final GL10 pGL) {
		
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class TextureOptions {

		public final int mMagFilter;
		public final int mMinFilter;
		public final int mTextureEnvironment;

		public TextureOptions() {
			this(GL10.GL_NEAREST_MIPMAP_NEAREST, GL10.GL_NEAREST, GL10.GL_MODULATE);
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
