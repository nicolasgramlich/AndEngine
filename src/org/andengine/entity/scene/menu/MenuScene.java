package org.andengine.entity.scene.menu;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.animator.IMenuSceneAnimator;
import org.andengine.entity.scene.menu.animator.InstantMenuSceneAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 20:06:51 - 01.04.2010
 */
public class MenuScene extends CameraScene implements IOnAreaTouchListener, IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ArrayList<IMenuItem> mMenuItems = new ArrayList<IMenuItem>();

	private IOnMenuItemClickListener mOnMenuItemClickListener;

	private IMenuSceneAnimator mMenuSceneAnimator;

	private IMenuItem mSelectedMenuItem;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MenuScene(final Camera pCamera) {
		this(pCamera, new InstantMenuSceneAnimator());
	}

	public MenuScene(final Camera pCamera, final IMenuSceneAnimator pMenuSceneAnimator) {
		this(pCamera, pMenuSceneAnimator, null);
	}

	public MenuScene(final Camera pCamera, final IOnMenuItemClickListener pOnMenuItemClickListener) {
		this(pCamera, new InstantMenuSceneAnimator(), pOnMenuItemClickListener);
	}

	public MenuScene(final Camera pCamera, final IMenuSceneAnimator pMenuSceneAnimator, final IOnMenuItemClickListener pOnMenuItemClickListener) {
		super(pCamera);

		this.mMenuSceneAnimator = pMenuSceneAnimator;
		this.mOnMenuItemClickListener = pOnMenuItemClickListener;
		this.setOnSceneTouchListener(this);
		this.setOnAreaTouchListener(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IOnMenuItemClickListener getOnMenuItemClickListener() {
		return this.mOnMenuItemClickListener;
	}

	public void setOnMenuItemClickListener(final IOnMenuItemClickListener pOnMenuItemClickListener) {
		this.mOnMenuItemClickListener = pOnMenuItemClickListener;
	}

	public ArrayList<IMenuItem> getMenuItems() {
		return this.mMenuItems;
	}

	public IMenuItem getMenuItem(final int pIndex) {
		return this.mMenuItems.get(pIndex);
	}

	public int getMenuItemCount() {
		return this.mMenuItems.size();
	}

	public void addMenuItem(final IMenuItem pMenuItem) {
		this.mMenuItems.add(pMenuItem);
		this.attachChild(pMenuItem);
		this.registerTouchArea(pMenuItem);
	}

	public void clearMenuItems() {
		for (int i = this.mMenuItems.size() - 1; i >= 0; i--) {
			final IMenuItem menuItem = this.mMenuItems.remove(i);
			this.detachChild(menuItem);
			this.unregisterTouchArea(menuItem);
		}
	}

	@Override
	public MenuScene getChildScene() {
		return (MenuScene)super.getChildScene();
	}

	@Override
	public void setChildScene(final Scene pChildScene, final boolean pModalDraw, final boolean pModalUpdate, final boolean pModalTouch) throws IllegalArgumentException {
		if (pChildScene instanceof MenuScene) {
			super.setChildScene(pChildScene, pModalDraw, pModalUpdate, pModalTouch);
		} else {
			throw new IllegalArgumentException("A " + MenuScene.class.getSimpleName() + " accepts only " + MenuScene.class.getSimpleName() + " as a ChildScene.");
		}
	}

	@Override
	public void clearChildScene() {
		if (this.getChildScene() != null) {
			this.getChildScene().reset();
			super.clearChildScene();
		}
	}

	public IMenuSceneAnimator getMenuSceneAnimator() {
		return this.mMenuSceneAnimator;
	}

	public void setMenuSceneAnimator(final IMenuSceneAnimator pMenuSceneAnimator) {
		this.mMenuSceneAnimator = pMenuSceneAnimator;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		final IMenuItem menuItem = ((IMenuItem)pTouchArea);

		switch (pSceneTouchEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				if ((this.mSelectedMenuItem != null) && (this.mSelectedMenuItem != menuItem)) {
					this.mSelectedMenuItem.onUnselected();
				}
				this.mSelectedMenuItem = menuItem;
				this.mSelectedMenuItem.onSelected();
				break;
			case MotionEvent.ACTION_UP:
				if (this.mOnMenuItemClickListener != null) {
					final boolean handled = this.mOnMenuItemClickListener.onMenuItemClicked(this, menuItem, pTouchAreaLocalX, pTouchAreaLocalY);
					menuItem.onUnselected();
					this.mSelectedMenuItem = null;
					return handled;
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				menuItem.onUnselected();
				this.mSelectedMenuItem = null;
				break;
		}
		return true;
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if (this.mSelectedMenuItem != null) {
			this.mSelectedMenuItem.onUnselected();
			this.mSelectedMenuItem = null;
		}
		return false;
	}

	@Override
	public void back() {
		this.back(true, true);
	}

	public void back(final boolean pResetAnimations) {
		this.back(pResetAnimations, true);
	}

	public void back(final boolean pResetAnimations, final boolean pResetParentMenuSceneAnimations) {
		super.back();

		if (pResetAnimations) {
			this.resetAnimations();
		}

		if (pResetParentMenuSceneAnimations) {
			if ((this.mParentScene != null) && (this.mParentScene instanceof MenuScene)) {
				((MenuScene)this.mParentScene).resetAnimations();
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void closeMenuScene() {
		this.back();
	}

	public void buildAnimations() {
		this.mMenuSceneAnimator.buildMenuSceneAnimations(this);
	}

	public void resetAnimations() {
		this.mMenuSceneAnimator.resetMenuSceneAnimations(this);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IOnMenuItemClickListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY);
	}
}
