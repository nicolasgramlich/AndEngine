package org.anddev.andengine.opengl.texture.source;


/**
 * @author Nicolas Gramlich
 * @since 11:46:56 - 12.07.2011
 */
public interface ITextureSource extends Cloneable {
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

	public ITextureSource clone();
}