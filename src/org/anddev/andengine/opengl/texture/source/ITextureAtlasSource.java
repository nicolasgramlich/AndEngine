package org.anddev.andengine.opengl.texture.source;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:46:56 - 12.07.2011
 */
public interface ITextureAtlasSource extends Cloneable {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getTexturePositionX();
	public int getTexturePositionY();
	public void setTexturePositionX(final int pTexturePositionX);
	public void setTexturePositionY(final int pTexturePositionY);

	public int getWidth();
	public int getHeight();

	public ITextureAtlasSource clone();
}