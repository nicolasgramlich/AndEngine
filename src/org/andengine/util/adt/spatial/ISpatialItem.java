package org.andengine.util.adt.spatial;

import org.andengine.util.adt.bounds.IBounds;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:23:12 - 07.10.2011
 */
public interface ISpatialItem<B extends IBounds> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public B getBounds();
}
