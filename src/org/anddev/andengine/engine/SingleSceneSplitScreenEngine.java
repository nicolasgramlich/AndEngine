package org.anddev.andengine.engine;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.SplitScreenEngineOptions;
import org.anddev.andengine.opengl.util.GLHelper;

import android.view.MotionEvent;

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

	// ===========================================================
	// Constructors
	// ===========================================================

	public SingleSceneSplitScreenEngine(final SplitScreenEngineOptions pSplitScreenEngineOptions) {
		super(pSplitScreenEngineOptions);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Deprecated
	@Override
	public Camera getCamera() {
		return super.getCamera();
	}

	public Camera getFirstCamera() {
		return super.getCamera();
	}

	public Camera getSecondCamera() {
		return this.getEngineOptions().getSecondCamera();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public SplitScreenEngineOptions getEngineOptions() {
		return (SplitScreenEngineOptions) super.getEngineOptions();
	}

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

			firstCamera.onApplyMatrix(pGL);
			GLHelper.setModelViewIdentityMatrix(pGL);

			super.mScene.onDraw(pGL);
			firstCamera.onDrawHUD(pGL);
		}

		/* Second Screen. With second camera, on the right half of the screens width. */
		{
			pGL.glScissor(surfaceWidthHalf, 0, surfaceWidthHalf, surfaceHeight);
			pGL.glViewport(surfaceWidthHalf, 0, surfaceWidthHalf, surfaceHeight);

			secondCamera.onApplyMatrix(pGL);
			GLHelper.setModelViewIdentityMatrix(pGL);

			super.mScene.onDraw(pGL);
			secondCamera.onDrawHUD(pGL);
		}
		
		pGL.glDisable(GL10.GL_SCISSOR_TEST);
	}

	@Override
	protected Camera getCameraFromSurfaceMotionEvent(final MotionEvent pMotionEvent) {
		if(pMotionEvent.getX() <= this.mSurfaceWidth >> 1) {
			return this.getFirstCamera();
		} else {
			return this.getSecondCamera();
		}
	}

	@Override
	protected void convertSurfaceToSceneMotionEvent(final Camera pCamera, final MotionEvent pSurfaceMotionEvent) {
		final int surfaceWidthHalf = this.mSurfaceWidth >> 1;

		if(pCamera == this.getFirstCamera()) {
			pCamera.convertSurfaceToSceneMotionEvent(pSurfaceMotionEvent, surfaceWidthHalf, this.mSurfaceHeight);
		} else {
			pSurfaceMotionEvent.offsetLocation(-surfaceWidthHalf, 0);
			pCamera.convertSurfaceToSceneMotionEvent(pSurfaceMotionEvent, surfaceWidthHalf, this.mSurfaceHeight);
		}
	}

	@Override
	protected void updatePreFrameHandlers(final float pSecondsElapsed) {
		this.getSecondCamera().onUpdate(pSecondsElapsed);
		super.updatePreFrameHandlers(pSecondsElapsed);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
