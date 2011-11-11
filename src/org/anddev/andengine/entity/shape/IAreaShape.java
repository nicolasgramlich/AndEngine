package org.anddev.andengine.entity.shape;

import org.anddev.andengine.opengl.Mesh;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 19:01:16 - 07.08.2011
 */
public interface IAreaShape<T extends Mesh> extends IShape<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public float getWidth();
	public float getBaseWidth();
	public float getHeight();
	public float getBaseHeight();

	public float getWidthScaled();
	public float getHeightScaled();

	public void setHeight(final float pHeight);
	public void setWidth(final float pWidth);
	public void setSize(final float pWidth, final float pHeight);
}
