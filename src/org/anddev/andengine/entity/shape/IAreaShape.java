package org.anddev.andengine.entity.shape;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 19:01:16 - 07.08.2011
 */
public interface IAreaShape extends IShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public float getWidth();
	public float getHeight();

	public float getBaseWidth();
	public float getBaseHeight();

	public float getWidthScaled();
	public float getHeightScaled();
}
