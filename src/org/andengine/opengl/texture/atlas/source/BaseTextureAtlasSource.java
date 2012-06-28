package org.andengine.opengl.texture.atlas.source;

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

	protected int mTextureX;
	protected int mTextureY;
	protected int mTextureWidth;
	protected int mTextureHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTextureAtlasSource(final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight) {
		this.mTextureX = pTextureX;
		this.mTextureY = pTextureY;
		this.mTextureWidth = pTextureWidth;
		this.mTextureHeight = pTextureHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getTextureX() {
		return this.mTextureX;
	}

	@Override
	public int getTextureY() {
		return this.mTextureY;
	}

	@Override
	public void setTextureX(final int pTextureX) {
		this.mTextureX = pTextureX;
	}

	@Override
	public void setTextureY(final int pTextureY) {
		this.mTextureY = pTextureY;
	}

	@Override
	public int getTextureWidth() {
		return this.mTextureWidth;
	}

	@Override
	public int getTextureHeight() {
		return this.mTextureHeight;
	}

	@Override
	public void setTextureWidth(final int pTextureWidth) {
		this.mTextureWidth = pTextureWidth;
	}

	@Override
	public void setTextureHeight(final int pTextureHeight) {
		this.mTextureHeight = pTextureHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "( " + this.getTextureWidth() + "x" + this.getTextureHeight() + " @ "+ this.mTextureX + "/" + this.mTextureY + " )";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}