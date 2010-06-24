package org.anddev.andengine.entity.menu.animator;

import java.util.ArrayList;

import org.anddev.andengine.entity.HorizontalAlign;
import org.anddev.andengine.entity.menu.MenuItem;
import org.anddev.andengine.entity.shape.modifier.MoveModifier;

/**
 * @author Nicolas Gramlich
 * @since 11:04:35 - 02.04.2010
 */
public class SlideMenuAnimator extends BaseMenuAnimator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SlideMenuAnimator(){
		super();
	}

	public SlideMenuAnimator(final HorizontalAlign pHorizontalAlign) {
		super(pHorizontalAlign);
	}
	
	public SlideMenuAnimator(final float pMenuItemSpacing) {
		super(pMenuItemSpacing);
	}
	
	public SlideMenuAnimator(final HorizontalAlign pHorizontalAlign, final float pMenuItemSpacing) {
		super(pHorizontalAlign, pMenuItemSpacing);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void buildAnimations(final ArrayList<MenuItem> pMenuItems, final float pCameraWidth, final float pCameraHeight) {
		final float maximumWidth = this.getMaximumWidth(pMenuItems);
		final float overallHeight = this.getOverallHeight(pMenuItems);

		final float baseX = pCameraWidth / 2 - maximumWidth / 2;
		final float baseY = pCameraHeight / 2 - overallHeight / 2;

		float offsetY = 0;
		for(int i = 0; i < pMenuItems.size(); i++) {
			final MenuItem menuItem = pMenuItems.get(i);
			
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
					offsetX = (maximumWidth - menuItem.getWidthScaled()) / 2;
					break;
			}
			
			final MoveModifier moveModifier = new MoveModifier(DURATION, -maximumWidth, baseX + offsetX, baseY + offsetY, baseY + offsetY);
			moveModifier.setRemoveWhenFinished(false);
			menuItem.addShapeModifier(moveModifier);

			offsetY += menuItem.getHeight() + this.mMenuItemSpacing;
		}
	}

	@Override
	public void prepareAnimations(final ArrayList<MenuItem> pMenuItems, final float pCameraWidth, final float pCameraHeight) {
		final float maximumWidth = this.getMaximumWidth(pMenuItems);
		final float overallHeight = this.getOverallHeight(pMenuItems);

		final float baseY = pCameraHeight / 2 - overallHeight / 2;

		final float menuItemSpacing = this.mMenuItemSpacing;

		float offsetY = 0;
		for(int i = 0; i < pMenuItems.size(); i++) {
			final MenuItem menuItem = pMenuItems.get(i);

			menuItem.setPosition(-maximumWidth, baseY + offsetY);

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
