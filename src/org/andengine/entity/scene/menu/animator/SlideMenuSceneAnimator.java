package org.andengine.entity.scene.menu.animator;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.align.VerticalAlign;
import org.andengine.util.adt.spatial.Direction;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:04:35 - 02.04.2010
 */
public class SlideMenuSceneAnimator extends MenuSceneAnimator {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float DURATION = 1.0f;

	private static final IEaseFunction EASEFUNCTION_DEFAULT = EaseLinear.getInstance();

	private static final Direction DIRECTION_DEFAULT = Direction.RIGHT;

	// ===========================================================
	// Fields
	// ===========================================================

	private IEaseFunction mEaseFunction;
	private Direction mDirection;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SlideMenuSceneAnimator() {
		this(SlideMenuSceneAnimator.DIRECTION_DEFAULT, SlideMenuSceneAnimator.EASEFUNCTION_DEFAULT);
	}

	public SlideMenuSceneAnimator(final IEaseFunction pEaseFunction) {
		this(SlideMenuSceneAnimator.DIRECTION_DEFAULT, pEaseFunction);
	}

	public SlideMenuSceneAnimator(final Direction pDirection) {
		this(pDirection, SlideMenuSceneAnimator.EASEFUNCTION_DEFAULT);
	}

	public SlideMenuSceneAnimator(final Direction pDirection, final IEaseFunction pEaseFunction) {
		super();

		this.mDirection = pDirection;
		this.mEaseFunction = pEaseFunction;
	}

	public SlideMenuSceneAnimator(final HorizontalAlign pHorizontalAlign, final VerticalAlign pVerticalAlign) {
		this(pHorizontalAlign, pVerticalAlign, SlideMenuSceneAnimator.DIRECTION_DEFAULT, SlideMenuSceneAnimator.EASEFUNCTION_DEFAULT);
	}

	public SlideMenuSceneAnimator(final HorizontalAlign pHorizontalAlign, final VerticalAlign pVerticalAlign, final IEaseFunction pEaseFunction) {
		this(pHorizontalAlign, pVerticalAlign, SlideMenuSceneAnimator.DIRECTION_DEFAULT, pEaseFunction);
	}

	public SlideMenuSceneAnimator(final HorizontalAlign pHorizontalAlign, final VerticalAlign pVerticalAlign, final Direction pDirection) {
		this(pHorizontalAlign, pVerticalAlign, pDirection, SlideMenuSceneAnimator.EASEFUNCTION_DEFAULT);
	}

	public SlideMenuSceneAnimator(final HorizontalAlign pHorizontalAlign, final VerticalAlign pVerticalAlign, final Direction pDirection, final IEaseFunction pEaseFunction) {
		super(pHorizontalAlign, pVerticalAlign);

		this.mDirection = pDirection;
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

	public Direction getDirection() {
		return this.mDirection;
	}

	public void setDirection(final Direction pDirection) {
		this.mDirection = pDirection;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onMenuItemPositionBuilt(final MenuScene pMenuScene, final int pIndex, final IMenuItem pMenuItem, final float pX, final float pY) {
		final float fromX = getFromX(pMenuScene, pMenuItem, pX);
		final float fromY = getFromY(pMenuScene, pMenuItem, pY);

		pMenuItem.setPosition(fromX, fromY);

		final MoveModifier moveModifier = new MoveModifier(SlideMenuSceneAnimator.DURATION, fromX, fromY, pX, pY, this.mEaseFunction);
		moveModifier.setAutoUnregisterWhenFinished(false);
		pMenuItem.registerEntityModifier(moveModifier);
	}

	@Override
	protected void onMenuItemPositionReset(final MenuScene pMenuScene, final int pIndex, final IMenuItem pMenuItem, final float pX, final float pY) {
		final float fromX = getFromX(pMenuScene, pMenuItem, pX);
		final float fromY = getFromY(pMenuScene, pMenuItem, pY);

		pMenuItem.setPosition(fromX, fromY);

		pMenuItem.resetEntityModifiers();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected float getFromX(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pX) {
		switch (this.mDirection) {
			case UP:
			case DOWN:
				return pX;
			case DOWN_LEFT:
			case UP_LEFT:
			case LEFT:
				return pMenuScene.getWidth() + (pMenuItem.getWidth() * 0.5f);
			case DOWN_RIGHT:
			case UP_RIGHT:
			case RIGHT:
				return -(pMenuItem.getWidth() * 0.5f);
			default:
				throw new IllegalArgumentException();
		}
	}

	protected float getFromY(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pY) {
		switch (this.mDirection) {
			case DOWN_LEFT:
			case DOWN:
			case DOWN_RIGHT:
				return pMenuScene.getHeight() + (pMenuItem.getHeight() * 0.5f);
			case LEFT:
			case RIGHT:
				return pY;
			case UP_LEFT:
			case UP:
			case UP_RIGHT:
				return -(pMenuItem.getHeight() * 0.5f);
			default:
				throw new IllegalArgumentException();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
