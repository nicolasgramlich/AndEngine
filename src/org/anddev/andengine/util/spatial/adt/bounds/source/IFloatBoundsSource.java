package org.anddev.andengine.util.spatial.adt.bounds.source;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 7:42:02 AM - Oct 10, 2011
 */
public interface IFloatBoundsSource extends IBoundsSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public float getLeft();
	public float getRight();
	public float getTop();
	public float getBottom();
}
