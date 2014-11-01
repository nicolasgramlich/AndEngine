package org.andengine.engine.camera;

import junit.framework.Assert;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import android.os.SystemClock;
import android.test.AndroidTestCase;
import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 00:07:21 - 14.05.2010
 */
public class ZoomCameraTest  extends AndroidTestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 20;
	private static final int CAMERA_HEIGHT = 20;

	private static final float DELTA = 0.0001f;

	// ===========================================================
	// Fields
	// ===========================================================

	private Engine mEngine;
	private ZoomCamera mZoomCamera;
	private Scene mScene;
	private CameraScene mCameraScene;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void setUp() throws Exception {
		this.mZoomCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mEngine = new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), this.mZoomCamera));
		this.mEngine.setSurfaceSize(CAMERA_WIDTH, CAMERA_HEIGHT);

		this.mScene = new Scene();

		this.mCameraScene = new CameraScene();
		this.mCameraScene.setCamera(this.mZoomCamera);

		this.mScene.setChildScene(this.mCameraScene, true, true, false);

		this.mEngine.setScene(this.mScene);

		this.mEngine.start();
	}

	@Override
	public void tearDown() throws Exception {
		this.mEngine.stop();
	}

	// ===========================================================
	// Test-Methods
	// ===========================================================
	
	public void testCameraSceneTouchCenterUncenteredRotated() throws Exception {
		this.mZoomCamera.setCameraSceneRotation(180);
		this.mZoomCamera.setZoomFactor(2);
		this.mZoomCamera.setCenter(0, 0);
		
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = 20;
		final float expectedY = 20;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}
	
	public void testCameraSceneTouchCenterUncenteredRotated2() throws Exception {
		this.mZoomCamera.setCameraSceneRotation(180);
		this.mZoomCamera.setZoomFactor(4);
		this.mZoomCamera.setCenter(0, 0);
		
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = 20;
		final float expectedY = 20;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}
	
	public void testCameraSceneTouchCenterUncenteredRotated3() throws Exception {
		this.mZoomCamera.setCameraSceneRotation(180);
		this.mZoomCamera.setZoomFactor(0.5f);
		this.mZoomCamera.setCenter(0, 0);
		
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = 20;
		final float expectedY = 20;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}
	
	public void testCameraSceneTouchCenterUncenteredRotated4() throws Exception {
		this.mZoomCamera.setCameraSceneRotation(180);
		this.mZoomCamera.setZoomFactor(0.5f);
		this.mZoomCamera.setCenter(100, 100);
		
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = 20;
		final float expectedY = 20;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}
	
	public void testCameraSceneTouchCenterUncenteredRotated5() throws Exception {
		this.mZoomCamera.setCameraSceneRotation(180);
		this.mZoomCamera.setZoomFactor(2);
		this.mZoomCamera.setCenter(15, 15);
		
		final int surfaceTouchX = 20;
		final int surfaceTouchY = 20;

		final float expectedX = 0;
		final float expectedY = 0;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void testSceneTouchWorker(final Scene pScene, final int pSurfaceTouchX, final int pSurfaceTouchY, final float pExpectedX, final float pExpectedY) {
		pScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
				final float actualX = pSceneTouchEvent.getX();
				final float actualY = pSceneTouchEvent.getY();
				Assert.assertEquals(pExpectedX, actualX, DELTA);
				Assert.assertEquals(pExpectedY, actualY, DELTA);
				return true;
			}
		});

		final long uptimeMillis = SystemClock.uptimeMillis();

		final boolean result = this.mEngine.onTouch(null, MotionEvent.obtain(uptimeMillis, uptimeMillis, MotionEvent.ACTION_DOWN, pSurfaceTouchX, pSurfaceTouchY, 0));

		Assert.assertTrue(result);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
