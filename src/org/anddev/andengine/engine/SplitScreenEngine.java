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
		pGL.glEnable(GL10.GL_SCISSOR_TEST);
		final int surfaceWidth = this.mSurfaceWidth;
		final int surfaceHeight = this.mSurfaceHeight;

		/* First Screen. Simply with half the width. */
		pGL.glScissor(0, 0, surfaceWidth / 2, surfaceHeight);
		pGL.glViewport(0, 0, surfaceWidth / 2, surfaceHeight);
		super.mScene.onDraw(pGL);
		this.getFirstCamera().onDrawHUD(pGL);


		/* Second Screen. With second Camera, */
		pGL.glScissor(surfaceWidth / 2, 0, surfaceWidth / 2, surfaceHeight);
		pGL.glViewport(surfaceWidth / 2, 0, surfaceWidth / 2, surfaceHeight);

		this.getEngineOptions().getSecondCamera().onApplyMatrix(pGL);
		GLHelper.setModelViewIdentityMatrix(pGL);

		super.mScene.onDraw(pGL);
		this.getSecondCamera().onDrawHUD(pGL);

		pGL.glDisable(GL10.GL_SCISSOR_TEST);
	}

	@Override
	public MotionEvent surfaceToSceneMotionEvent(final MotionEvent pMotionEvent) {
		final int surfaceWidthHalf = this.mSurfaceWidth / 2;

		final Camera camera;
		final float relativeX;
		final float relativeY = pMotionEvent.getY() / this.mSurfaceHeight;

		if(pMotionEvent.getX() < surfaceWidthHalf) {
			camera = this.getFirstCamera();

			relativeX = pMotionEvent.getX() / surfaceWidthHalf;
		} else {
			camera = this.getSecondCamera();

			relativeX = (pMotionEvent.getX() - surfaceWidthHalf) / surfaceWidthHalf;
		}

		final float x = camera.relativeToAbsoluteX(relativeX);
		final float y = camera.relativeToAbsoluteY(relativeY);

		pMotionEvent.setLocation(x, y);
		return pMotionEvent;
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
