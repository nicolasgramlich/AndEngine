package org.anddev.andengine.util.spatial.adt.bounds;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 7:42:02 AM - Oct 10, 2011
 */
public interface IIntBounds extends IBounds {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getLeft();
	public int getTop();
	public int getRight();
	public int getBottom();
}
