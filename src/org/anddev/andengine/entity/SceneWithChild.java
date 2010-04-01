package org.anddev.andengine.entity;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 12:47:39 - 08.03.2010
 */
public class SceneWithChild extends Scene {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Scene mChildScene;
	private boolean mChildSceneModalDraw;
	private boolean mChildSceneModalUpdate;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SceneWithChild(final int pLayerCount) {
		super(pLayerCount);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean hasChildScene() {
		return this.mChildScene != null;
	}

	public Scene getChildScene() {
		return this.mChildScene;
	}

	public void setChildScene(final Scene pChildScene) {
		this.setChildSceneModal(pChildScene, true, true);
	}

	public void setChildSceneModal(final Scene pChildScene, final boolean pModalDraw, final boolean pModalUpdate) {
		this.mChildScene = pChildScene;
		this.mChildSceneModalDraw = pModalDraw;
		this.mChildSceneModalUpdate = pModalUpdate;
	}

	public void clearChildScene() {
		this.mChildScene = null;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onSceneTouchEvent(final MotionEvent pSceneMotionEvent) {
		if(this.mChildScene == null) {
			return super.onSceneTouchEvent(pSceneMotionEvent);
		} else {
			return this.mChildScene.onSceneTouchEvent(pSceneMotionEvent);
		}
	}

	@Override
	protected void onManagedDraw(final GL10 pGL) {
		if(this.mChildScene == null || !this.mChildSceneModalDraw) {
			super.onManagedDraw(pGL);
		}
		if(this.mChildScene != null) {
			this.mChildScene.onDraw(pGL);
		}
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(this.mChildScene == null || !this.mChildSceneModalUpdate) {
			super.onManagedUpdate(pSecondsElapsed);
		}

		if(this.mChildScene != null) {
			this.mChildScene.onUpdate(pSecondsElapsed);
		}
	}

	@Override
	public void updatePreFrameHandlers(final float pSecondsElapsed) {
		if(this.mChildScene == null && !this.mChildSceneModalUpdate) {
			super.updatePreFrameHandlers(pSecondsElapsed);
		}

		if (this.mChildScene != null) {
			this.mChildScene.updatePreFrameHandlers(pSecondsElapsed);
		}
	}

	@Override
	public void updatePostFrameHandlers(final float pSecondsElapsed) {
		if(this.mChildScene == null  && !this.mChildSceneModalUpdate) {
			super.updatePostFrameHandlers(pSecondsElapsed);
		}

		if (this.mChildScene != null) {
			this.mChildScene.updatePostFrameHandlers(pSecondsElapsed);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
