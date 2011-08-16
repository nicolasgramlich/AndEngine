package org.anddev.andengine.opengl.texture.region;

import org.anddev.andengine.opengl.texture.ITexture;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:29:59 - 08.03.2010
 */
public class TextureRegion extends BaseTextureRegion implements ITextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mX;
	protected int mY;
	protected int mWidth;
	protected int mHeight;

	protected float mU;
	protected float mU2;
	protected float mV;
	protected float mV2;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegion(final ITexture pTexture, final int pX, final int pY, final int pWidth, final int pHeight) {
		super(pTexture);

		this.mX = pX;
		this.mY = pY;

		this.mWidth = pWidth;
		this.mHeight = pHeight;

		this.updateUV();
	}
	
	@Override
	public TextureRegion clone() {
		return new TextureRegion(mTexture, mX, mY, mWidth, mHeight);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getX() {
		return this.mX;
	}

	@Override
	public int getY() {
		return this.mY;
	}

	@Override
	public void setX(final int pX) {
		this.mX = pX;
		this.updateUV();
	}

	@Override
	public void setY(final int pY) {
		this.mY = pY;
		this.updateUV();
	}

	@Override
	public void setPosition(final int pX, final int pY) {
		this.mX = pX;
		this.mY = pY;
		this.updateUV();
	}

	@Override
	public int getWidth() {
		return this.mWidth;
	}

	@Override
	public int getHeight() {
		return this.mHeight;
	}

	@Override
	public void setWidth(final int pWidth) {
		this.mWidth = pWidth;
		this.updateUV();
	}

	@Override
	public void setHeight(final int pHeight) {
		this.mHeight = pHeight;
		this.updateUV();
	}

	@Override
	public void setSize(final int pWidth, final int pHeight) {
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.updateUV();
	}

	@Override
	public void set(final int pX, final int pY, final int pWidth, final int pHeight) {
		this.mX = pX;
		this.mY = pY;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void updateUV() {
		final ITexture texture = this.mTexture;
		final int textureWidth = texture.getWidth();
		final int textureHeight = texture.getHeight();

		final int x = this.getX();
		final int y = this.getY();

		this.mU = (float) x / textureWidth;
		this.mU2 = (float) (x + this.getWidth()) / textureWidth;

		this.mV = (float) y / textureHeight;
		this.mV2 = (float) (y + this.getHeight()) / textureHeight;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
