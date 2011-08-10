package org.anddev.andengine.opengl.texture.region;

import org.anddev.andengine.opengl.texture.ITexture;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:07:17 - 07.08.2011
 */
public interface ITextureRegion {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getWidth();
	public int getHeight();

	public void setWidth(final int pWidth);
	public void setHeight(final int pHeight);
	public void setSize(final int pWidth, final int pHeight);

	public int getX();
	public int getY();

	public void setX(final int pX);
	public void setY(final int pY);
	public void setPosition(final int pX, final int pY);

	public void set(final int pX, final int pY, final int pWidth, final int pHeight);

	// TODO Potentially the U/U2/V/V2 could be provided/stored with Float.floatToRawIntBits(..) so that they don't need to be permanently converted in the Sprite class, potentially improving performance of updating texturecoordinates by a factor of 10. 
	public float getU();
	public float getU2();
	public float getV();
	public float getV2();

	public ITexture getTexture();

	public ITextureRegion clone();
}