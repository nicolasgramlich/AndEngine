package org.anddev.andengine.entity.scene;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.opengl.util.GLHelper;

import android.view.MotionEvent;

/**
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
	public CameraScene(final int pLayerCount) {
		this(pLayerCount, null);
	}

	public CameraScene(final int pLayerCount, final Camera pCamera) {
		super(pLayerCount);
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
	public boolean onSceneTouchEvent(final MotionEvent pSceneMotionEvent) {
		if(this.mCamera == null) {
			return false;
		} else {
			this.mCamera.convertSceneToHUDMotionEvent(pSceneMotionEvent);

			final boolean handled = super.onSceneTouchEvent(pSceneMotionEvent);

			if(handled) {
				return true;
			} else {
				this.mCamera.convertHUDToSceneMotionEvent(pSceneMotionEvent);
				return false;
			}
		}
	}

	@Override
	protected boolean onChildSceneTouchEvent(final MotionEvent pSceneMotionEvent) {
		final boolean childIsCameraScene = this.mChildScene instanceof CameraScene;
		if(childIsCameraScene) {
			this.mCamera.convertHUDToSceneMotionEvent(pSceneMotionEvent);
			final boolean result = super.onChildSceneTouchEvent(pSceneMotionEvent);
			this.mCamera.convertSceneToHUDMotionEvent(pSceneMotionEvent);
			return result;
		} else {
			return super.onChildSceneTouchEvent(pSceneMotionEvent);
		}
	}

	@Override
	protected void onManagedDraw(final GL10 pGL) {
		if(this.mCamera != null) {
			GLHelper.switchToProjectionMatrix(pGL);
			pGL.glPushMatrix();
			this.mCamera.onApplyPositionIndependentMatrix(pGL);
			{	
				GLHelper.switchToModelViewMatrix(pGL);
				pGL.glPushMatrix();
				pGL.glLoadIdentity();
	
				super.onManagedDraw(pGL);
	
				pGL.glPopMatrix();
			}
			GLHelper.switchToProjectionMatrix(pGL);
			pGL.glPopMatrix();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
