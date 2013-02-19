package org.andengine.entity.scene.menu.animator;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.align.VerticalAlign;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 10:50:36 - 02.04.2010
 */
public interface IMenuSceneAnimator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public float getOffsetX();
	public void setOffsetX(final float pOffsetX);
	public float getOffsetY();
	public void setOffsetY(final float pOffsetY);
	public float getMenuItemSpacing();
	public void setMenuItemSpacing(final float pMenuItemSpacing);
	public HorizontalAlign getHorizontalAlign();
	public void setHorizontalAlign(final HorizontalAlign pHorizontalAlign);
	public VerticalAlign getVerticalAlign();
	public void setVerticalAlign(final VerticalAlign pVerticalAlign);

	public void buildMenuSceneAnimations(final MenuScene pMenuScene);
	public void resetMenuSceneAnimations(final MenuScene pMenuScene);
}
