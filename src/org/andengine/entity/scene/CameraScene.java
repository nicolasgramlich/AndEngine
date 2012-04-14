package org.andengine.entity.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:35:53 - 29.03.2010
 */
public class CameraScene extends Scene {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected Camera mCamera;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * {@link CameraScene#setCamera(Camera)} needs to be called manually. Otherwise nothing will be drawn.
	 */
	public CameraScene() {
		this(null);
	}

	public CameraScene(final Camera pCamera) {
		this.mCamera = pCamera;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Camera getCamera() {
		return this.mCamera;
	}

	public void setCamera(final Camera pCamera) {
		this.mCamera = pCamera;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		if(this.mCamera == null) {
			return false;
		} else {
			this.mCamera.convertSceneToCameraSceneTouchEvent(pSceneTouchEvent);

			final boolean handled = super.onSceneTouchEvent(pSceneTouchEvent);

			if(handled) {
				return true;
			} else {
				this.mCamera.convertCameraSceneToSceneTouchEvent(pSceneTouchEvent);
				return false;
			}
		}
	}

	@Override
	protected boolean onChildSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		final boolean childIsCameraScene = this.mChildScene instanceof CameraScene;
		if(childIsCameraScene) {
			this.mCamera.convertCameraSceneToSceneTouchEvent(pSceneTouchEvent);
			final boolean result = super.onChildSceneTouchEvent(pSceneTouchEvent);
			this.mCamera.convertSceneToCameraSceneTouchEvent(pSceneTouchEvent);
			return result;
		} else {
			return super.onChildSceneTouchEvent(pSceneTouchEvent);
		}
	}

	@Override
	protected void onApplyMatrix(final GLState pGLState, final Camera pCamera) {
		this.mCamera.onApplyCameraSceneMatrix(pGLState);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void centerEntityInCamera(final IEntity pEntity) {
		final Camera camera = this.mCamera;
		pEntity.setPosition((camera.getWidth() - pEntity.getWidth()) * 0.5f, (camera.getHeight() - pEntity.getHeight()) * 0.5f);
	}

	public void centerEntityInCameraHorizontally(final IEntity pEntity) {
		pEntity.setPosition((this.mCamera.getWidth() - pEntity.getWidth()) * 0.5f, pEntity.getY());
	}

	public void centerEntityInCameraVertically(final IEntity pEntity) {
		pEntity.setPosition(pEntity.getX(), (this.mCamera.getHeight() - pEntity.getHeight()) * 0.5f);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
