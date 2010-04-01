package org.anddev.andengine.entity.menu;

import java.util.ArrayList;

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

	private ArrayList<MenuItem> mMenuItems = new ArrayList<MenuItem>();
	private IOnMenuItemClickerListener mOnMenuItemClickerListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MenuScene(final IOnMenuItemClickerListener pOnMenuItemClickerListener) {
		this(pOnMenuItemClickerListener, null);
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
	
	public void setOnMenuItemClickerListener(IOnMenuItemClickerListener pOnMenuItemClickerListener) {
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
