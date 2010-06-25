package org.anddev.andengine.entity.menu.animator;

import java.util.ArrayList;

import org.anddev.andengine.entity.menu.IMenuAnimator;
import org.anddev.andengine.entity.menu.MenuItem;
import org.anddev.andengine.util.HorizontalAlign;

/**
 * @author Nicolas Gramlich
 * @since 11:17:32 - 02.04.2010
 */
public abstract class BaseMenuAnimator implements IMenuAnimator {
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final float DURATION = 1.0f;
	
	private static final float DEFAULT_MENUITEMSPACING = 1.0f;
	
	private static final HorizontalAlign DEFAULT_HORIZONTALALIGN = HorizontalAlign.CENTER;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final float mMenuItemSpacing;
	protected final HorizontalAlign mHorizontalAlign;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseMenuAnimator() {
		this(DEFAULT_MENUITEMSPACING);
	}

	public BaseMenuAnimator(final float pMenuItemSpacing) {
		this(DEFAULT_HORIZONTALALIGN, pMenuItemSpacing);
	}

	public BaseMenuAnimator(HorizontalAlign pHorizontalAlign) {
		this(pHorizontalAlign, DEFAULT_MENUITEMSPACING);
	}

	public BaseMenuAnimator(final HorizontalAlign pHorizontalAlign, final float pMenuItemSpacing) {
		this.mHorizontalAlign = pHorizontalAlign;
		this.mMenuItemSpacing = pMenuItemSpacing;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	protected float getMaximumWidth(final ArrayList<MenuItem> pMenuItems) {
		float maximumWidth = Float.MIN_VALUE;
		for(int i = pMenuItems.size() - 1; i >= 0; i--) {
			final MenuItem menuItem = pMenuItems.get(i);
			maximumWidth = Math.max(maximumWidth, menuItem.getWidthScaled());
		}
		return maximumWidth;
	}

	protected float getOverallHeight(final ArrayList<MenuItem> pMenuItems) {
		float overallHeight = 0;
		for(int i = pMenuItems.size() - 1; i >= 0; i--) {
			final MenuItem menuItem = pMenuItems.get(i);
			overallHeight += menuItem.getHeight();
		}

		overallHeight += (pMenuItems.size() - 1) * this.mMenuItemSpacing;
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
