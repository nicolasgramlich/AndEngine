package org.anddev.andengine.util.spatial.adt.bounds;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 7:42:02 AM - Oct 10, 2011
 */
public interface IFloatBounds extends IBounds {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public float getMinX();
	public float getMinY();
	public float getMaxX();
	public float getMaxY();
}
