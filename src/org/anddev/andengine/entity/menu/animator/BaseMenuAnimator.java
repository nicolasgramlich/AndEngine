package org.anddev.andengine.entity.menu.animator;

import java.util.ArrayList;

import org.anddev.andengine.entity.menu.IMenuAnimator;
import org.anddev.andengine.entity.menu.MenuItem;

/**
 * @author Nicolas Gramlich
 * @since 11:17:32 - 02.04.2010
 */
public abstract class BaseMenuAnimator implements IMenuAnimator {
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

	protected float getMaximumWidth(final ArrayList<MenuItem> pMenuItems) {
		float maximumWidth = Float.MIN_VALUE;
		for(int i = pMenuItems.size() - 1; i >= 0; i--) {
			final MenuItem menuItem = pMenuItems.get(i);
			maximumWidth = Math.max(maximumWidth,menuItem.getWidth());
		}
		return maximumWidth;
	}

	protected float getOverallHeight(final ArrayList<MenuItem> pMenuItems) {
		float overallHeight = 0;
		for(int i = pMenuItems.size() - 1; i >= 0; i--) {
			final MenuItem menuItem = pMenuItems.get(i);
			overallHeight += menuItem.getHeight();
		}
		return overallHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
