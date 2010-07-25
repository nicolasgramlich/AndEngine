package org.anddev.andengine.entity.scene.menu.animator;

import java.util.ArrayList;

import org.anddev.andengine.engine.easying.Easing;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.shape.modifier.AlphaModifier;
import org.anddev.andengine.util.HorizontalAlign;

/**
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

	public AlphaMenuAnimator(final HorizontalAlign pHorizontalAlign) {
		super(pHorizontalAlign);
	}
	
	public AlphaMenuAnimator(final float pMenuItemSpacing) {
		super(pMenuItemSpacing);
	}

	public AlphaMenuAnimator(final HorizontalAlign pHorizontalAlign, final float pMenuItemSpacing) {
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
		final int menuItemCount = pMenuItems.size();
		for(int i = 0; i < menuItemCount; i++) {
			final AlphaModifier alphaModifier = new AlphaModifier(DURATION, ALPHA_FROM, ALPHA_TO, Easing.LINEAR);
			alphaModifier.setRemoveWhenFinished(false);
			pMenuItems.get(i).addShapeModifier(alphaModifier);
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
