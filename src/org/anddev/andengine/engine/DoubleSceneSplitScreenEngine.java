package org.anddev.andengine.engine;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.SplitScreenEngineOptions;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.opengl.GLHelper;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 22:28:34 - 27.03.2010
 */
public class DoubleSceneSplitScreenEngine extends Engine {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Scene mSecondScene;

	// ===========================================================
	// Constructors
	// ===========================================================

	public DoubleSceneSplitScreenEngine(final SplitScreenEngineOptions pSplitScreenEngineOptions) {
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

	@Deprecated
	@Override
	public Scene getScene() {
		return super.getScene();
	}

	public Scene getFirstScene() {
		return super.getScene();
	}

	public Scene getSecondScene() {
		return this.mSecondScene;
	}

	@Deprecated
	@Override
	public void setScene(final Scene pScene) {
		super.setScene(pScene);
	}

	public void setFirstScene(final Scene pScene) {
		super.setScene(pScene);
	}

	public void setSecondScene(final Scene pScene) {
		this.mSecondScene = pScene;
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
		{
			pGL.glScissor(0, 0, surfaceWidthHalf, surfaceHeight);
			pGL.glViewport(0, 0, surfaceWidthHalf, surfaceHeight);

			this.getFirstCamera().onApplyMatrix(pGL);
			GLHelper.setModelViewIdentityMatrix(pGL);

			super.mScene.onDraw(pGL);
			this.getFirstCamera().onDrawHUD(pGL);
		}

		/* Second Screen. With second camera, on the right half of the screens width. */
		{
			pGL.glScissor(surfaceWidthHalf, 0, surfaceWidthHalf, surfaceHeight);
			pGL.glViewport(surfaceWidthHalf, 0, surfaceWidthHalf, surfaceHeight);

			this.getSecondCamera().onApplyMatrix(pGL);
			GLHelper.setModelViewIdentityMatrix(pGL);

			this.mSecondScene.onDraw(pGL);
			this.getSecondCamera().onDrawHUD(pGL);
		}
		
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
	protected Scene getSceneFromSurfaceMotionEvent(MotionEvent pMotionEvent) {
		if(pMotionEvent.getX() <= this.mSurfaceWidth / 2) {
			return this.getFirstScene();
		} else {
			return this.getSecondScene();
		}
	}

	@Override
	protected void onUpdateScene(final float pSecondsElapsed) {
		super.onUpdateScene(pSecondsElapsed);
		if(this.mSecondScene != null) {
			this.mSecondScene.onUpdate(pSecondsElapsed);
		}
	}

	@Override
	protected void onUpdateScenePreFrameHandlers(float pSecondsElapsed) {
		super.onUpdateScenePreFrameHandlers(pSecondsElapsed);
		if(this.mSecondScene != null) {
			this.mSecondScene.updatePreFrameHandlers(pSecondsElapsed);
		}
	}

	@Override
	protected void onUpdateScenePostFrameHandlers(float pSecondsElapsed) {
		super.onUpdateScenePostFrameHandlers(pSecondsElapsed);
		if(this.mSecondScene != null) {
			this.mSecondScene.updatePostFrameHandlers(pSecondsElapsed);
		}
	}

	@Override
	protected void convertSurfaceToSceneMotionEvent(final Camera pCamera, final MotionEvent pSurfaceMotionEvent) {
		final int surfaceWidthHalf = this.mSurfaceWidth / 2;

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
