package org.anddev.andengine.entity.menu;

import java.util.ArrayList;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.CameraScene;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.input.touch.IOnAreaTouchListener;
import org.anddev.andengine.input.touch.ITouchArea;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 20:06:51 - 01.04.2010
 */
public class MenuScene extends CameraScene implements IOnAreaTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<MenuItem> mMenuItems = new ArrayList<MenuItem>();

	private IOnMenuItemClickerListener mOnMenuItemClickerListener;
	
	private IMenuAnimator mMenuAnimator = IMenuAnimator.DEFAULT;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MenuScene() {
		this(null, null);
	}

	public MenuScene(final IOnMenuItemClickerListener pOnMenuItemClickerListener) {
		this(pOnMenuItemClickerListener, null);
	}

	public MenuScene(final Camera pCamera) {
		this(null, pCamera);
	}

	public MenuScene(final IOnMenuItemClickerListener pOnMenuItemClickerListener, final Camera pCamera) {
		super(1, pCamera);
		this.mOnMenuItemClickerListener = pOnMenuItemClickerListener;
		this.setOnAreaTouchListener(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IOnMenuItemClickerListener getOnMenuItemClickerListener() {
		return this.mOnMenuItemClickerListener;
	}

	public void setOnMenuItemClickerListener(final IOnMenuItemClickerListener pOnMenuItemClickerListener) {
		this.mOnMenuItemClickerListener = pOnMenuItemClickerListener;
	}

	public int getMenuItemCount() {
		return this.mMenuItems.size();
	}

	public void addMenuItem(final MenuItem pMenuItem) {
		this.mMenuItems.add(pMenuItem);
		this.getBottomLayer().addEntity(pMenuItem);
		this.registerTouchArea(pMenuItem);
	}
	
	@Override
	public MenuScene getChildScene() {
		return (MenuScene)super.getChildScene();
	}
	
	@Override
	public void setChildScene(final Scene pChildScene, final boolean pModalDraw, final boolean pModalUpdate) {
		if(pChildScene instanceof MenuScene) {
			super.setChildScene(pChildScene, pModalDraw, pModalUpdate);
		} else {
			throw new IllegalArgumentException("MenuScene accepts only MenuScenes as a ChildScene.");
		}
	}

	public void clearChildScene() {
		if(this.getChildScene() != null) {
			this.getChildScene().reset();
			super.clearChildScene();
		}
	}
	
	public void setMenuAnimator(final IMenuAnimator pMenuAnimator) {
		this.mMenuAnimator = pMenuAnimator;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onAreaTouched(final ITouchArea pTouchArea, final MotionEvent pSceneMotionEvent) {
		if(this.mOnMenuItemClickerListener != null) {
			if(pSceneMotionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				this.mOnMenuItemClickerListener.onMenuItemClicked(this, (MenuItem)pTouchArea);
			}
		}
		return true;
	}
	
	@Override
	public void back() {
		super.back();
		
		this.reset();
	}

	@Override
	public void reset() {
		super.reset();
		
		final ArrayList<MenuItem> menuItems = this.mMenuItems;
		for(int i = menuItems.size() - 1; i >= 0; i--) {
			menuItems.get(i).reset();
		}
		
		this.prepareAnimations();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void buildAnimations() {
		this.prepareAnimations();
		
		final float cameraHeight = this.mCamera.getHeight();
		final float cameraWidth = this.mCamera.getWidth();
		this.mMenuAnimator.buildAnimations(this.mMenuItems, cameraWidth, cameraHeight);
	}
	
	public void prepareAnimations() {
		final float cameraHeight = this.mCamera.getHeight();
		final float cameraWidth = this.mCamera.getWidth();
		this.mMenuAnimator.prepareAnimations(this.mMenuItems, cameraWidth, cameraHeight);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
