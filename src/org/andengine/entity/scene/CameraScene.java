package org.andengine.entity.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.exception.MethodNotSupportedException;

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
	 * {@link #setCamera(Camera)} needs to be called manually. Otherwise nothing will be drawn.
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

	@Override
	public float getWidth() {
		return this.mCamera.getCameraSceneWidth();
	}

	@Override
	public float getHeight() {
		return this.mCamera.getCameraSceneHeight();
	}

	@Override
	public void setWidth(final float pWidth) {
		throw new MethodNotSupportedException();
	}

	@Override
	public void setHeight(final float pHeight) {
		throw new MethodNotSupportedException();
	}

	@Override
	public void setSize(final float pWidth, final float pHeight) {
		throw new MethodNotSupportedException();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		if (this.mCamera == null) {
			return false;
		} else {
			this.mCamera.convertSceneTouchEventToCameraSceneTouchEvent(pSceneTouchEvent);

			final boolean handled = super.onSceneTouchEvent(pSceneTouchEvent);

			if (handled) {
				return true;
			} else {
				this.mCamera.convertCameraSceneTouchEventToSceneTouchEvent(pSceneTouchEvent);
				return false;
			}
		}
	}

	@Override
	protected boolean onChildSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		final boolean childIsCameraScene = this.mChildScene instanceof CameraScene;
		if (childIsCameraScene) {
			this.mCamera.convertCameraSceneTouchEventToSceneTouchEvent(pSceneTouchEvent);
			final boolean result = super.onChildSceneTouchEvent(pSceneTouchEvent);
			this.mCamera.convertSceneTouchEventToCameraSceneTouchEvent(pSceneTouchEvent);
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
		pEntity.setPosition(this.mCamera.getCenterX(), this.mCamera.getCenterY());
	}

	public void centerEntityInCameraHorizontally(final IEntity pEntity) {
		pEntity.setX(this.mCamera.getCenterX());
	}

	public void centerEntityInCameraVertically(final IEntity pEntity) {
		pEntity.setY(this.mCamera.getCenterY());
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
