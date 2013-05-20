package org.andengine.util.adt.color;

import org.andengine.util.adt.pool.GenericPool;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 2:25:20 - 12.08.2011
 */
public class ColorPool extends GenericPool<Color> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected Color onAllocatePoolItem() {
		return new Color(Color.WHITE);
	}

	@Override
	protected void onHandleRecycleItem(final Color pColor) {
		pColor.setChecking(Color.WHITE);

		super.onHandleRecycleItem(pColor);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
