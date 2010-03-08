package org.anddev.andengine.opengl.texture;

import android.graphics.Bitmap;

/**
 * @author Nicolas Gramlich
 * @since 14:29:59 - 08.03.2010
 */
public class Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mWidth;
	protected final int mHeight;
	protected int mAtlasPositionX;
	protected int mAtlasPositionY;
	protected TextureAtlas mTextureAtlas;

	// ===========================================================
	// Constructors
	// ===========================================================

	Texture(final TextureAtlas pTextureAtlas, final int pAtlasPositionX, final int pAtlasPositionY, final int pWidth, final int pHeight) {
		this.mTextureAtlas = pTextureAtlas;
		this.mAtlasPositionX = pAtlasPositionX;
		this.mAtlasPositionY = pAtlasPositionY;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	public Texture(final ITextureSource pTextureSource) {
		this.mWidth = pTextureSource.getWidth();
		this.mHeight = pTextureSource.getHeight();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getWidth() {
		return this.mWidth;
	}

	public int getHeight() {
		return this.mHeight;
	}

	public void setAtlasPosition(final int pX, final int pY) {
		this.mAtlasPositionX = pX;
		this.mAtlasPositionY = pY;
	}

	public int getAtlasPositionX() {
		return this.mAtlasPositionX;
	}

	public int getAtlasPositionY() {
		return this.mAtlasPositionY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	protected static interface ITextureSource {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public int getWidth();
		public int getHeight();
		public Bitmap getBitmap();
	}
}
