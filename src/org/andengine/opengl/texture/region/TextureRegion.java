package org.andengine.opengl.texture.region;

import org.andengine.opengl.texture.ITexture;

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

	private static final float SCALE_DEFAULT = 1;

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mTextureX;
	protected float mTextureY;
	protected float mTextureWidth;
	protected float mTextureHeight;

	protected float mU;
	protected float mU2;
	protected float mV;
	protected float mV2;

	protected final float mScale;
	protected final boolean mRotated;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegion(final ITexture pTexture, final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight) {
		this(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight, false);
	}

	public TextureRegion(final ITexture pTexture, final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight, final boolean pRotated) {
		this(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight, SCALE_DEFAULT, pRotated);
	}

	public TextureRegion(final ITexture pTexture, final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight, final float pScale) {
		this(pTexture, pTextureX, pTextureY, pTextureWidth, pTextureHeight, pScale, false);
	}

	public TextureRegion(final ITexture pTexture, final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight, final float pScale, final boolean pRotated) {
		super(pTexture);

		this.mTextureX = pTextureX;
		this.mTextureY = pTextureY;

		if(pRotated) {
			this.mRotated = true;

			this.mTextureWidth = pTextureHeight;
			this.mTextureHeight = pTextureWidth;
		} else {
			this.mRotated = false;

			this.mTextureWidth = pTextureWidth;
			this.mTextureHeight = pTextureHeight;
		}

		this.mScale = pScale;

		this.updateUV();
	}

	@Override
	public TextureRegion deepCopy() {
		if(this.mRotated) {
			return new TextureRegion(this.mTexture, this.mTextureX, this.mTextureY, this.mTextureHeight, this.mTextureWidth, this.mScale, this.mRotated);
		} else {
			return new TextureRegion(this.mTexture, this.mTextureX, this.mTextureY, this.mTextureWidth, this.mTextureHeight, this.mScale, this.mRotated);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public float getTextureX() {
		return this.mTextureX;
	}

	@Override
	public float getTextureY() {
		return this.mTextureY;
	}

	@Override
	public void setTextureX(final float pTextureX) {
		this.mTextureX = pTextureX;

		this.updateUV();
	}

	@Override
	public void setTextureY(final float pTextureY) {
		this.mTextureY = pTextureY;

		this.updateUV();
	}

	@Override
	public void setTexturePosition(final float pTextureX, final float pTextureY) {
		this.mTextureX = pTextureX;
		this.mTextureY = pTextureY;

		this.updateUV();
	}

	@Override
	public float getWidth() {
		if(this.mRotated) {
			return this.mTextureHeight * this.mScale;
		} else {
			return this.mTextureWidth * this.mScale;
		}
	}

	@Override
	public float getHeight() {
		if(this.mRotated) {
			return this.mTextureWidth * this.mScale;
		} else {
			return this.mTextureHeight * this.mScale;
		}
	}

	@Override
	public void setTextureWidth(final float pTextureWidth) {
		this.mTextureWidth = pTextureWidth;

		this.updateUV();
	}

	@Override
	public void setTextureHeight(final float pTextureHeight) {
		this.mTextureHeight = pTextureHeight;

		this.updateUV();
	}

	@Override
	public void setTextureSize(final float pTextureWidth, final float pTextureHeight) {
		this.mTextureWidth = pTextureWidth;
		this.mTextureHeight = pTextureHeight;

		this.updateUV();
	}

	@Override
	public void set(final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight) {
		this.mTextureX = pTextureX;
		this.mTextureY = pTextureY;
		this.mTextureWidth = pTextureWidth;
		this.mTextureHeight = pTextureHeight;

		this.updateUV();
	}

	@Override
	public float getU() {
		return this.mU;
	}

	@Override
	public float getU2() {
		return this.mU2;
	}

	@Override
	public float getV() {
		return this.mV;
	}

	@Override
	public float getV2() {
		return this.mV2;
	}

	@Override
	public boolean isScaled() {
		return this.mScale != SCALE_DEFAULT;
	}

	@Override
	public float getScale() {
		return this.mScale;
	}

	@Override
	public boolean isRotated() {
		return this.mRotated;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void updateUV() {
		final ITexture texture = this.mTexture;
		final float textureWidth = texture.getWidth();
		final float textureHeight = texture.getHeight();

		final float x = this.getTextureX();
		final float y = this.getTextureY();

		this.mU = x / textureWidth;
		this.mU2 = (x + this.mTextureWidth) / textureWidth;

		this.mV = y / textureHeight;
		this.mV2 = (y + this.mTextureHeight) / textureHeight;

//		this.mU = (x + 0.5f) / textureWidth;
//		this.mU2 = (x + this.mTextureWidth - 0.5f) / textureWidth;
//
//		this.mV = (y + 0.5f) / textureHeight;
//		this.mV2 = (y + this.mTextureHeight - 0.5f) / textureHeight;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
