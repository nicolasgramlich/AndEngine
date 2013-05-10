package org.andengine.opengl.texture.region;

import org.andengine.opengl.texture.ITexture;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:07:17 - 07.08.2011
 */
public interface ITextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public float getTextureX();
	public float getTextureY();

	public void setTextureX(final float pTextureX);
	public void setTextureY(final float pTextureY);
	public void setTexturePosition(final float pTextureX, final float pTextureY);

	/**
	 * Note: Takes {@link #getScale()} into account!
	 */
	public float getWidth();
	/**
	 * Note: Takes {@link #getScale()} into account!
	 */
	public float getHeight();

	public void setTextureWidth(final float pTextureWidth);
	public void setTextureHeight(final float pTextureHeight);
	public void setTextureSize(final float pTextureWidth, final float pTextureHeight);

	public void set(final float pTextureX, final float pTextureY, final float pTextureWidth, final float pTextureHeight);

	public float getU();
	public float getU2();
	public float getV();
	public float getV2();

	public boolean isScaled();
	public float getScale();
	public boolean isRotated();

	public ITexture getTexture();

	public ITextureRegion deepCopy() throws DeepCopyNotSupportedException;
}