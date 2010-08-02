package org.anddev.andengine.engine;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.input.touch.TouchEvent;

/**
 * @author Nicolas Gramlich
 * @since 22:28:34 - 27.03.2010
 */
public class SingleSceneSplitScreenEngine extends Engine {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Camera mSecondCamera;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SingleSceneSplitScreenEngine(final EngineOptions pEngineOptions, final Camera pSecondCamera) {
		super(pEngineOptions);
		this.mSecondCamera = pSecondCamera;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Deprecated
	@Override
	public Camera getCamera() {
		return super.mCamera;
	}

	public Camera getFirstCamera() {
		return super.mCamera;
	}

	public Camera getSecondCamera() {
		return this.mSecondCamera;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onDrawScene(final GL10 pGL) {
		final Camera firstCamera = this.getFirstCamera();
		final Camera secondCamera = this.getSecondCamera();

		final int surfaceWidth = this.mSurfaceWidth;
		final int surfaceWidthHalf = surfaceWidth >> 1;

		final int surfaceHeight = this.mSurfaceHeight;

		pGL.glEnable(GL10.GL_SCISSOR_TEST); // TODO --> GLHelper

		/* First Screen. With first camera, on the left half of the screens width. */
		{
			pGL.glScissor(0, 0, surfaceWidthHalf, surfaceHeight);
			pGL.glViewport(0, 0, surfaceWidthHalf, surfaceHeight);

			super.mScene.onDraw(pGL, firstCamera);
			firstCamera.onDrawHUD(pGL);
		}

		/* Second Screen. With second camera, on the right half of the screens width. */
		{
			pGL.glScissor(surfaceWidthHalf, 0, surfaceWidthHalf, surfaceHeight);
			pGL.glViewport(surfaceWidthHalf, 0, surfaceWidthHalf, surfaceHeight);

			super.mScene.onDraw(pGL, secondCamera);
			secondCamera.onDrawHUD(pGL);
		}

		pGL.glDisable(GL10.GL_SCISSOR_TEST);
	}

	@Override
	protected Camera getCameraFromSurfaceTouchEvent(final TouchEvent pTouchEvent) {
		if(pTouchEvent.getX() <= this.mSurfaceWidth >> 1) {
			return this.getFirstCamera();
		} else {
			return this.getSecondCamera();
		}
	}

	@Override
	protected void convertSurfaceToSceneTouchEvent(final Camera pCamera, final TouchEvent pSurfaceTouchEvent) {
		final int surfaceWidthHalf = this.mSurfaceWidth >> 1;

		if(pCamera == this.getFirstCamera()) {
			pCamera.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent, surfaceWidthHalf, this.mSurfaceHeight);
		} else {
			pSurfaceTouchEvent.offset(-surfaceWidthHalf, 0);
			pCamera.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent, surfaceWidthHalf, this.mSurfaceHeight);
		}
	}

	@Override
	protected void updateUpdateHandlers(final float pSecondsElapsed) {
		super.updateUpdateHandlers(pSecondsElapsed);
		this.getSecondCamera().onUpdate(pSecondsElapsed);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
