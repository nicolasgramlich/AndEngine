package org.anddev.andengine.opengl.texture.source;



/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:55:12 - 12.07.2011
 */
public abstract class BaseTextureAtlasSource implements ITextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mTexturePositionX;
	protected int mTexturePositionY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTextureAtlasSource(final int pTexturePositionX, final int pTexturePositionY) {
		this.mTexturePositionX = pTexturePositionX;
		this.mTexturePositionY = pTexturePositionY;
	}

	@Override
	public abstract BaseTextureAtlasSource clone();

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getTexturePositionX() {
		return this.mTexturePositionX;
	}

	@Override
	public int getTexturePositionY() {
		return this.mTexturePositionY;
	}

	@Override
	public void setTexturePositionX(final int pTexturePositionX) {
		this.mTexturePositionX = pTexturePositionX;
	}

	@Override
	public void setTexturePositionY(final int pTexturePositionY) {
		this.mTexturePositionY = pTexturePositionY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "( " + this.getWidth() + "x" + this.getHeight() + " @ "+ this.mTexturePositionX + "/" + this.mTexturePositionY + " )";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}