package org.andengine.opengl.texture.atlas.source;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:46:56 - 12.07.2011
 */
public interface ITextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getTextureX();
	public int getTextureY();
	public void setTextureX(final int pTextureX);
	public void setTextureY(final int pTextureY);

	public int getTextureWidth();
	public int getTextureHeight();
	public void setTextureWidth(final int pTextureWidth);
	public void setTextureHeight(final int pTextureHeight);

	public ITextureAtlasSource deepCopy();
}