package org.andengine.entity.scene.menu.animator;

import java.util.ArrayList;

import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:17:32 - 02.04.2010
 */
public abstract class BaseMenuAnimator implements IMenuAnimator {
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final float DURATION = 1.0f;

	private static final float MENUITEMSPACING_DEFAULT = 1.0f;

	private static final HorizontalAlign HORIZONTALALIGN_DEFAULT = HorizontalAlign.CENTER;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final float mMenuItemSpacing;
	protected final HorizontalAlign mHorizontalAlign;
	protected final IEaseFunction mEaseFunction;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseMenuAnimator() {
		this(MENUITEMSPACING_DEFAULT);
	}

	public BaseMenuAnimator(final IEaseFunction pEaseFunction) {
		this(MENUITEMSPACING_DEFAULT, pEaseFunction);
	}

	public BaseMenuAnimator(final float pMenuItemSpacing) {
		this(HORIZONTALALIGN_DEFAULT, pMenuItemSpacing);
	}

	public BaseMenuAnimator(final float pMenuItemSpacing, final IEaseFunction pEaseFunction) {
		this(HORIZONTALALIGN_DEFAULT, pMenuItemSpacing, pEaseFunction);
	}

	public BaseMenuAnimator(final HorizontalAlign pHorizontalAlign) {
		this(pHorizontalAlign, MENUITEMSPACING_DEFAULT);
	}

	public BaseMenuAnimator(final HorizontalAlign pHorizontalAlign, final IEaseFunction pEaseFunction) {
		this(pHorizontalAlign, MENUITEMSPACING_DEFAULT, pEaseFunction);
	}

	public BaseMenuAnimator(final HorizontalAlign pHorizontalAlign, final float pMenuItemSpacing) {
		this(pHorizontalAlign, pMenuItemSpacing, EaseLinear.getInstance());
	}

	public BaseMenuAnimator(final HorizontalAlign pHorizontalAlign, final float pMenuItemSpacing, final IEaseFunction pEaseFunction) {
		this.mHorizontalAlign = pHorizontalAlign;
		this.mMenuItemSpacing = pMenuItemSpacing;
		this.mEaseFunction = pEaseFunction;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	protected float getMaximumWidth(final ArrayList<IMenuItem> pMenuItems) {
		float maximumWidth = Float.MIN_VALUE;
		for(int i = pMenuItems.size() - 1; i >= 0; i--) {
			final IMenuItem menuItem = pMenuItems.get(i);
			maximumWidth = Math.max(maximumWidth, menuItem.getWidthScaled());
		}
		return maximumWidth;
	}

	protected float getOverallHeight(final ArrayList<IMenuItem> pMenuItems) {
		float overallHeight = 0;
		for(int i = pMenuItems.size() - 1; i >= 0; i--) {
			final IMenuItem menuItem = pMenuItems.get(i);
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
