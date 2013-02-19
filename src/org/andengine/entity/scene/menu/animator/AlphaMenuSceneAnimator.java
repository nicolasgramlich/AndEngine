package org.andengine.entity.scene.menu.animator;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.align.VerticalAlign;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:04:35 - 02.04.2010
 */
public class AlphaMenuSceneAnimator extends MenuSceneAnimator {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float DURATION = 1.0f;
	private static final float ALPHA_FROM = 0.0f;
	private static final float ALPHA_TO = 1.0f;

	private static final IEaseFunction EASEFUNCTION_DEFAULT = EaseLinear.getInstance();

	// ===========================================================
	// Fields
	// ===========================================================

	private IEaseFunction mEaseFunction;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AlphaMenuSceneAnimator() {
		this(AlphaMenuSceneAnimator.EASEFUNCTION_DEFAULT);
	}

	public AlphaMenuSceneAnimator(final IEaseFunction pEaseFunction) {
		super();

		this.mEaseFunction = pEaseFunction;
	}

	public AlphaMenuSceneAnimator(final HorizontalAlign pHorizontalAlign, final VerticalAlign pVerticalAlign) {
		this(pHorizontalAlign, pVerticalAlign, AlphaMenuSceneAnimator.EASEFUNCTION_DEFAULT);
	}

	public AlphaMenuSceneAnimator(final HorizontalAlign pHorizontalAlign, final VerticalAlign pVerticalAlign, final IEaseFunction pEaseFunction) {
		super(pHorizontalAlign, pVerticalAlign);

		this.mEaseFunction = pEaseFunction;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IEaseFunction getEaseFunction() {
		return this.mEaseFunction;
	}

	public void setEaseFunction(final IEaseFunction pEaseFunction) {
		this.mEaseFunction = pEaseFunction;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onMenuItemPositionBuilt(final MenuScene pMenuScene, final int pIndex, final IMenuItem pMenuItem, final float pX, final float pY) {
		pMenuItem.setPosition(pX, pY);

		final AlphaModifier alphaModifier = new AlphaModifier(AlphaMenuSceneAnimator.DURATION, AlphaMenuSceneAnimator.ALPHA_FROM, AlphaMenuSceneAnimator.ALPHA_TO, this.mEaseFunction);
		alphaModifier.setAutoUnregisterWhenFinished(false);
		pMenuItem.registerEntityModifier(alphaModifier);
	}

	@Override
	protected void onMenuItemPositionReset(final MenuScene pMenuScene, final int pIndex, final IMenuItem pMenuItem, final float pX, final float pY) {
		pMenuItem.setPosition(pX, pY);

		pMenuItem.resetEntityModifiers();
		pMenuItem.setAlpha(AlphaMenuSceneAnimator.ALPHA_FROM);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
