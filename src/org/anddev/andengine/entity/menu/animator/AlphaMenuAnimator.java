package org.anddev.andengine.entity.menu.animator;

import java.util.ArrayList;

import org.anddev.andengine.entity.menu.MenuItem;
import org.anddev.andengine.entity.sprite.modifier.AlphaModifier;

/**
 * @author Nicolas Gramlich
 * @since 11:04:35 - 02.04.2010
 */
public class AlphaMenuAnimator extends BaseMenuAnimator {
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
	public void buildAnimations(final ArrayList<MenuItem> pMenuItems, final float pCameraWidth, final float pCameraHeight) {
		for(int i = 0; i < pMenuItems.size(); i++) {
			final MenuItem menuItem = pMenuItems.get(i);
			
			menuItem.addSpriteModifier(new AlphaModifier(2, 0, 1));
		}
	}

	@Override
	public void prepareAnimations(final ArrayList<MenuItem> pMenuItems, final float pCameraWidth, final float pCameraHeight) {
		final float maximumWidth = getMaximumWidth(pMenuItems);
		final float overallHeight = getOverallHeight(pMenuItems);
		
		final float baseX = pCameraWidth / 2 - maximumWidth / 2;
		final float baseY = pCameraHeight / 2 - overallHeight / 2;
		
		float offsetY = 0;
		for(int i = 0; i < pMenuItems.size(); i++) {
			final MenuItem menuItem = pMenuItems.get(i);
			
			menuItem.setPosition(baseX, baseY + offsetY);
			
			offsetY += menuItem.getHeight();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
