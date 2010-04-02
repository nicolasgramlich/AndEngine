package org.anddev.andengine.entity.menu;

import java.util.ArrayList;

import org.anddev.andengine.entity.menu.animator.AlphaMenuAnimator;

/**
 * @author Nicolas Gramlich
 * @since 10:50:36 - 02.04.2010
 */
public interface IMenuAnimator {
	// ===========================================================
	// Constants
	// ===========================================================
	
	public static final IMenuAnimator DEFAULT = new AlphaMenuAnimator();

	// ===========================================================
	// Methods
	// ===========================================================

	public void prepareAnimations(final ArrayList<MenuItem> pMenuItems, final float pCameraWidth, final float pCameraHeight);
	public void buildAnimations(final ArrayList<MenuItem> pMenuItems, final float pCameraWidth, final float pCameraHeight);
}
