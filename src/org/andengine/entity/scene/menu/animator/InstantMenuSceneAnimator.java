package org.andengine.entity.scene.menu.animator;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.align.VerticalAlign;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:04:35 - 02.04.2010
 */
public class InstantMenuSceneAnimator extends MenuSceneAnimator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public InstantMenuSceneAnimator() {
		super();
	}

	public InstantMenuSceneAnimator(final HorizontalAlign pHorizontalAlign, final VerticalAlign pVerticalAlign) {
		super(pHorizontalAlign, pVerticalAlign);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onMenuItemPositionBuilt(final MenuScene pMenuScene, final int pIndex, final IMenuItem pMenuItem, final float pX, final float pY) {
		pMenuItem.setPosition(pX, pY);
	}

	@Override
	protected void onMenuItemPositionReset(final MenuScene pMenuScene, final int pIndex, final IMenuItem pMenuItem, final float pX, final float pY) {
		pMenuItem.setPosition(pX, pY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
