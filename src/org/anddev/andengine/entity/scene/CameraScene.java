package org.anddev.andengine.entity.scene;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.input.touch.TouchEvent;

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
	public boolean onSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		if(this.mCamera == null) {
			return false;
		} else {
			this.mCamera.convertSceneToHUDTouchEvent(pSceneTouchEvent);

			final boolean handled = super.onSceneTouchEvent(pSceneTouchEvent);

			if(handled) {
				return true;
			} else {
				this.mCamera.convertHUDToSceneTouchEvent(pSceneTouchEvent);
				return false;
			}
		}
	}

	@Override
	protected boolean onChildSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		final boolean childIsCameraScene = this.mChildScene instanceof CameraScene;
		if(childIsCameraScene) {
			this.mCamera.convertHUDToSceneTouchEvent(pSceneTouchEvent);
			final boolean result = super.onChildSceneTouchEvent(pSceneTouchEvent);
			this.mCamera.convertSceneToHUDTouchEvent(pSceneTouchEvent);
			return result;
		} else {
			return super.onChildSceneTouchEvent(pSceneTouchEvent);
		}
	}

	@Override
	protected void onManagedDraw(final GL10 pGL, final Camera pCamera) {
		if(this.mCamera != null) {
			pGL.glMatrixMode(GL10.GL_PROJECTION);
			this.mCamera.onApplyPositionIndependentMatrix(pGL);
			{
				pGL.glMatrixMode(GL10.GL_MODELVIEW);
				pGL.glPushMatrix();
				pGL.glLoadIdentity();

				super.onManagedDraw(pGL, pCamera);

				pGL.glPopMatrix();
			}
			pGL.glMatrixMode(GL10.GL_PROJECTION);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
