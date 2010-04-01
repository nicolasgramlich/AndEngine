package org.anddev.andengine.entity.menu;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.CameraScene;
import org.anddev.andengine.entity.sprite.modifier.MoveModifier;
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

	private MenuScene mSubMenuScene;
	private boolean mSubMenuSceneModalDraw;
	private boolean mSubMenuSceneModalUpdate;

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

	public boolean hasSubMenuScene() {
		return this.mSubMenuScene != null;
	}

	public MenuScene getSubMenuScene() {
		return this.mSubMenuScene;
	}

	public void setSubMenuScene(final MenuScene pSubMenuScene) {
		this.mSubMenuScene = pSubMenuScene;
	}

	public void setSubMenuSceneModal(final MenuScene pSubMenuScene, final boolean pModalDraw, final boolean pModalUpdate) {
		this.mSubMenuScene = pSubMenuScene;
		this.mSubMenuSceneModalDraw = pModalDraw;
		this.mSubMenuSceneModalUpdate = pModalUpdate;
	}

	public void clearSubMenu() {
		this.mSubMenuScene = null;
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
	public boolean onSceneTouchEvent(final MotionEvent pSceneMotionEvent) {
		if(this.mSubMenuScene == null) {
			return super.onSceneTouchEvent(pSceneMotionEvent);
		} else {
			return this.mSubMenuScene.onSceneTouchEvent(pSceneMotionEvent);
		}
	}

	@Override
	protected void onManagedDraw(final GL10 pGL) {
		if(this.mSubMenuScene == null || !this.mSubMenuSceneModalDraw) {
			super.onManagedDraw(pGL);
		}
		if(this.mSubMenuScene != null) {
			this.mSubMenuScene.onDraw(pGL);
		}
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(this.mSubMenuScene == null || !this.mSubMenuSceneModalUpdate) {
			super.onManagedUpdate(pSecondsElapsed);
		}

		if(this.mSubMenuScene != null) {
			this.mSubMenuScene.onUpdate(pSecondsElapsed);
		}
	}

	@Override
	public void updatePreFrameHandlers(final float pSecondsElapsed) {
		if(this.mSubMenuScene == null && !this.mSubMenuSceneModalUpdate) {
			super.updatePreFrameHandlers(pSecondsElapsed);
		}

		if (this.mSubMenuScene != null) {
			this.mSubMenuScene.updatePreFrameHandlers(pSecondsElapsed);
		}
	}

	@Override
	public void updatePostFrameHandlers(final float pSecondsElapsed) {
		if(this.mSubMenuScene == null  && !this.mSubMenuSceneModalUpdate) {
			super.updatePostFrameHandlers(pSecondsElapsed);
		}

		if (this.mSubMenuScene != null) {
			this.mSubMenuScene.updatePostFrameHandlers(pSecondsElapsed);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void build() {
		//		final float cameraHeight = this.mCamera.getHeight();
		//		final float cameraWidth = this.mCamera.getWidth();

		final ArrayList<MenuItem> menuItems = this.mMenuItems;
		for(int i = menuItems.size() - 1; i >= 0; i--) {
			menuItems.get(i).addSpriteModifier(new MoveModifier(3, 0, 150, 0, 50 + i * 100)); // TODO Vernünftige Formel
		}
	}

	@Override
	public void reset() {
		final ArrayList<MenuItem> menuItems = this.mMenuItems;
		for(int i = menuItems.size() - 1; i >= 0; i--) {
			menuItems.get(i).reset();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
