package org.andengine.engine.splitscreen;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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
	protected void onDrawScene(final GLState pGLState, final Camera pFirstCamera) {
		if(super.mScene != null) {
			final Camera secondCamera = this.getSecondCamera();
	
			final int surfaceWidth = this.mSurfaceWidth;
			final int surfaceWidthHalf = surfaceWidth >> 1;
	
			final int surfaceHeight = this.mSurfaceHeight;
	
			pGLState.enableScissorTest();
	
			/* First Screen. With first camera, on the left half of the screens width. */
			{
				GLES20.glScissor(0, 0, surfaceWidthHalf, surfaceHeight);
				GLES20.glViewport(0, 0, surfaceWidthHalf, surfaceHeight);
	
				super.mScene.onDraw(pGLState, pFirstCamera);
				pFirstCamera.onDrawHUD(pGLState);
			}
	
			/* Second Screen. With second camera, on the right half of the screens width. */
			{
				GLES20.glScissor(surfaceWidthHalf, 0, surfaceWidthHalf, surfaceHeight);
				GLES20.glViewport(surfaceWidthHalf, 0, surfaceWidthHalf, surfaceHeight);
	
				super.mScene.onDraw(pGLState, secondCamera);
				secondCamera.onDrawHUD(pGLState);
			}
	
			pGLState.disableScissorTest();
		}
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
	protected void onUpdateUpdateHandlers(final float pSecondsElapsed) {
		super.onUpdateUpdateHandlers(pSecondsElapsed);
		this.getSecondCamera().onUpdate(pSecondsElapsed);
	}

	@Override
	protected void onUpdateCameraSurface() {
		final int surfaceWidth = this.mSurfaceWidth;
		final int surfaceWidthHalf = surfaceWidth >> 1;

		this.getFirstCamera().setSurfaceSize(0, 0, surfaceWidthHalf, this.mSurfaceHeight);
		this.getSecondCamera().setSurfaceSize(surfaceWidthHalf, 0, surfaceWidthHalf, this.mSurfaceHeight);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
