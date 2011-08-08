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

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegion(final ITexture pTexture, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		super(pTexture);

		this.mX = pTexturePositionX;
		this.mY = pTexturePositionY;

		this.mWidth = pWidth;
		this.mHeight = pHeight;

		this.updateUV();
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
