package org.anddev.andengine.engine;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.SplitScreenEngineOptions;
import org.anddev.andengine.opengl.GLHelper;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 22:28:34 - 27.03.2010
 */
public class SplitScreenEngine extends Engine {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SplitScreenEngine(final SplitScreenEngineOptions pSplitScreenEngineOptions) {
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
		final int surfaceWidth = this.mSurfaceWidth;
		final int surfaceWidthHalf = surfaceWidth / 2;

		final int surfaceHeight = this.mSurfaceHeight;

		pGL.glEnable(GL10.GL_SCISSOR_TEST); // TODO --> GLHelper

		/* First Screen. With first camera, on the left half of the screens width. */
		pGL.glScissor(0, 0, surfaceWidthHalf, surfaceHeight);
		pGL.glViewport(0, 0, surfaceWidthHalf, surfaceHeight);
		super.mScene.onDraw(pGL);
		this.getFirstCamera().onDrawHUD(pGL);


		/* Second Screen. With second camera, on the right half of the screens width. */
		pGL.glScissor(surfaceWidthHalf, 0, surfaceWidthHalf, surfaceHeight);
		pGL.glViewport(surfaceWidthHalf, 0, surfaceWidthHalf, surfaceHeight);

		this.getSecondCamera().onApplyMatrix(pGL);
		GLHelper.setModelViewIdentityMatrix(pGL);

		super.mScene.onDraw(pGL);
		this.getSecondCamera().onDrawHUD(pGL);

		pGL.glDisable(GL10.GL_SCISSOR_TEST);
	}

	@Override
	protected Camera getCameraFromSurfaceMotionEvent(final MotionEvent pMotionEvent) {
		if(pMotionEvent.getX() <= this.mSurfaceWidth / 2) {
			return this.getFirstCamera();
		} else {
			return this.getSecondCamera();
		}
	}

	@Override
	protected MotionEvent convertSurfaceToSceneMotionEvent(final Camera pCamera, final MotionEvent pSurfaceMotionEvent) {
		final int surfaceWidthHalf = this.mSurfaceWidth / 2;

		if(pCamera == this.getFirstCamera()) {
			pCamera.convertSurfaceToSceneMotionEvent(pSurfaceMotionEvent, surfaceWidthHalf, this.mSurfaceHeight);
		} else {
			pSurfaceMotionEvent.offsetLocation(-surfaceWidthHalf, 0);
			pCamera.convertSurfaceToSceneMotionEvent(pSurfaceMotionEvent, surfaceWidthHalf, this.mSurfaceHeight);
		}
		return pSurfaceMotionEvent;
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
