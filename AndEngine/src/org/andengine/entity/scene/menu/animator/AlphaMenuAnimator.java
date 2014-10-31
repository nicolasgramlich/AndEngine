package org.andengine.entity.scene.menu.animator;

import java.util.ArrayList;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:04:35 - 02.04.2010
 */
public class AlphaMenuAnimator extends BaseMenuAnimator {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float ALPHA_FROM = 0.0f;
	private static final float ALPHA_TO = 1.0f;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AlphaMenuAnimator(){
		super();
	}

	public AlphaMenuAnimator(final IEaseFunction pEaseFunction) {
		super(pEaseFunction);
	}

	public AlphaMenuAnimator(final HorizontalAlign pHorizontalAlign) {
		super(pHorizontalAlign);
	}

	public AlphaMenuAnimator(final HorizontalAlign pHorizontalAlign, final IEaseFunction pEaseFunction) {
		super(pHorizontalAlign, pEaseFunction);
	}

	public AlphaMenuAnimator(final float pMenuItemSpacing) {
		super(pMenuItemSpacing);
	}

	public AlphaMenuAnimator(final float pMenuItemSpacing, final IEaseFunction pEaseFunction) {
		super(pMenuItemSpacing, pEaseFunction);
	}

	public AlphaMenuAnimator(final HorizontalAlign pHorizontalAlign, final float pMenuItemSpacing) {
		super(pHorizontalAlign, pMenuItemSpacing);
	}

	public AlphaMenuAnimator(final HorizontalAlign pHorizontalAlign, final float pMenuItemSpacing, final IEaseFunction pEaseFunction) {
		super(pHorizontalAlign, pMenuItemSpacing, pEaseFunction);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void buildAnimations(final ArrayList<IMenuItem> pMenuItems, final float pCameraWidth, final float pCameraHeight) {
		final IEaseFunction easeFunction = this.mEaseFunction;
		final int menuItemCount = pMenuItems.size();
		for(int i = menuItemCount - 1; i >= 0; i--) {
			final AlphaModifier alphaModifier = new AlphaModifier(DURATION, ALPHA_FROM, ALPHA_TO, easeFunction);
			alphaModifier.setAutoUnregisterWhenFinished(false);
			pMenuItems.get(i).registerEntityModifier(alphaModifier);
		}
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

			menuItem.setAlpha(ALPHA_FROM);

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
