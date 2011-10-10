package org.anddev.andengine.util.spatial.adt.bounds.source;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 7:42:02 AM - Oct 10, 2011
 */
public interface IIntBoundsSource extends IBoundsSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getLeft();
	public int getRight();
	public int getTop();
	public int getBottom();
}
