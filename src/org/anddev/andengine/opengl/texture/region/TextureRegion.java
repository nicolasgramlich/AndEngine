package org.anddev.andengine.opengl.texture.region;

import org.anddev.andengine.opengl.texture.ITexture;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:29:59 - 08.03.2010
 */
public class TextureRegion extends BaseTextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegion(final ITexture pTexture, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		super(pTexture, pTexturePositionX, pTexturePositionY, pWidth, pHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public TextureRegion clone() {
		return new TextureRegion(this.mTexture, this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight);
	}

	@Override
	public float getTextureCoordinateX1() {
		return (float) this.mTexturePositionX / this.mTexture.getWidth();
	}

	@Override
	public float getTextureCoordinateY1() {
		return (float) this.mTexturePositionY / this.mTexture.getHeight();
	}

	@Override
	public float getTextureCoordinateX2() {
		return (float) (this.mTexturePositionX + this.mWidth) / this.mTexture.getWidth();
	}

	@Override
	public float getTextureCoordinateY2() {
		return (float) (this.mTexturePositionY + this.mHeight) / this.mTexture.getHeight();
	}

	@Override
	public int getTextureCropLeft() {
		return this.getTexturePositionX();
	}

	@Override
	public int getTextureCropTop() {
		return this.getTexturePositionY();
	}

	@Override
	public int getTextureCropWidth() {
		return this.getWidth();
	}

	@Override
	public int getTextureCropHeight() {
		return this.getHeight();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
