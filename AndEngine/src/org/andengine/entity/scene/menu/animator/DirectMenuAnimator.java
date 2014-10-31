package org.andengine.entity.scene.menu.animator;

import java.util.ArrayList;

import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.util.HorizontalAlign;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:46:34 - 14.05.2010
 */
public class DirectMenuAnimator extends BaseMenuAnimator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DirectMenuAnimator(){
		super();
	}

	public DirectMenuAnimator(final HorizontalAlign pHorizontalAlign) {
		super(pHorizontalAlign);
	}

	public DirectMenuAnimator(final float pMenuItemSpacing) {
		super(pMenuItemSpacing);
	}

	public DirectMenuAnimator(final HorizontalAlign pHorizontalAlign, final float pMenuItemSpacing) {
		super(pHorizontalAlign, pMenuItemSpacing);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void buildAnimations(final ArrayList<IMenuItem> pMenuItems, final float pCameraWidth, final float pCameraHeight) {

	}

	@Override
	public void prepareAnimations(final ArrayList<IMenuItem> pMenuItems, final float pCameraWidth, final float pCameraHeight) {
		final float maximumWidth = this.getMaximumWidth(pMenuItems);
		final float overallHeight = this.getOverallHeight(pMenuItems);

		final float baseX = (pCameraWidth - maximumWidth) * 0.5f;
		final float baseY = (pCameraHeight - overallHeight) * 0.5f;

		final float menuItemSpacing = this.mMenuItemSpacing;

		float offsetY = 0;
		final int menuItemCount = pMenuItems.size();
		for(int i = 0; i < menuItemCount; i++) {
			final IMenuItem menuItem = pMenuItems.get(i);

			final float offsetX;
			switch(this.mHorizontalAlign) {
				case LEFT:
					offsetX = 0;
					break;
				case RIGHT:
					offsetX = maximumWidth - menuItem.getWidthScaled();
					break;
				case CENTER:
				default:
					offsetX = (maximumWidth - menuItem.getWidthScaled()) * 0.5f;
					break;
			}
			menuItem.setPosition(baseX + offsetX , baseY + offsetY);

			offsetY += menuItem.getHeight() + menuItemSpacing;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
