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
public class CameraTest  extends AndroidTestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 100;
	private static final int CAMERA_HEIGHT = 100;

	private static final float DELTA = 0.0001f;

	// ===========================================================
	// Fields
	// ===========================================================

	private Engine mEngine;
	private Camera mCamera;
	private Scene mScene;
	private CameraScene mCameraScene;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void setUp() throws Exception {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mEngine = new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), this.mCamera));
		this.mEngine.setSurfaceSize(CAMERA_WIDTH, CAMERA_HEIGHT);

		this.mScene = new Scene();

		this.mCameraScene = new CameraScene();
		this.mCameraScene.setCamera(this.mCamera);

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

	public void testSceneTouchCenter() throws Exception {
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = 0;
		final float expectedY = 0;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}
	
	public void testLikeLevelEditorActivity() throws Exception {
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = 0;
		final float expectedY = 0;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testSceneTouchCenterRotated() throws Exception {
		this.mCamera.setRotation(90);
		
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = 0;
		final float expectedY = 100;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testSceneTouchCenterRotated2() throws Exception {
		this.mCamera.setRotation(90);
		this.mCamera.setCameraSceneRotation(-90);
		
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = 0;
		final float expectedY = 100;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}
	
	public void testSceneTouchCenterUncentered() throws Exception {
		this.mCamera.setCenter(0, 0);
		
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = -50;
		final float expectedY = -50;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}
	
	public void testSceneTouchCenterUncenteredRotated() throws Exception {
		this.mCamera.setRotation(90);
		this.mCamera.setCenter(45, 45);
		
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = -5;
		final float expectedY = 95;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	
	public void testCameraSceneTouchCenter() throws Exception {
		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final float expectedX = 50;
		final float expectedY = 50;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}
	
	public void testCameraSceneTouchCenterRotated() throws Exception {
		this.mCamera.setRotation(90);
		this.mCamera.setCameraSceneRotation(-90);
		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final float expectedX = 50;
		final float expectedY = 50;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}
	
	public void testCameraSceneTouchRotated() throws Exception {
		this.mCamera.setRotation(90);
		this.mCamera.setCameraSceneRotation(-90);
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = 100;
		final float expectedY = 0;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}
	
	public void testCameraSceneTouchCenterUncenteredRotated() throws Exception {
		this.mCamera.setCameraSceneRotation(180);
		this.mCamera.setCenter(0, 0);
		
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = 100;
		final float expectedY = 100;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}
	
	public void testCameraSceneTouchCenterUncenteredRotated4() throws Exception {
		this.mCamera.setCameraSceneRotation(90);
		this.mCamera.setCenter(0, 0);
		
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 0;

		final float expectedX = 0;
		final float expectedY = 100;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testCameraSceneTouchCenterUncenteredRotated2() throws Exception {
		this.mCamera.setCameraSceneRotation(180);
		this.mCamera.setCenter(0, 0);
		
		final int surfaceTouchX = 100;
		final int surfaceTouchY = 100;

		final float expectedX = 0;
		final float expectedY = 0;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}


	public void testCameraSceneTouchCenterUncenteredRotated3() throws Exception {
		this.mCamera.setCameraSceneRotation(180);
		this.mCamera.setCenter(100, 100);
		
		final int surfaceTouchX = 100;
		final int surfaceTouchY = 100;

		final float expectedX = 0;
		final float expectedY = 0;

		this.testSceneTouchWorker(this.mCameraScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}


	public void testConvertSceneToSurfaceTouchEventCenter() {
		this.mCamera.setCenter(0, 0);

		final TouchEvent touchEvent = TouchEvent.obtain(0, 0, TouchEvent.ACTION_DOWN, 0, null);

		final int surfaceWidth = 100;
		final int surfaceHeight = 100;

		this.mCamera.convertSceneToSurfaceTouchEvent(touchEvent, surfaceWidth, surfaceHeight);

		Assert.assertEquals(50, touchEvent.getX(), DELTA);
		Assert.assertEquals(50, touchEvent.getY(), DELTA);
	}

	public void testConvertSceneToSurfaceTouchEventCenterRotated180() {
		this.mCamera.setCenter(0, 0);
		this.mCamera.setRotation(180);
		
		final TouchEvent touchEvent = TouchEvent.obtain(0, 0, TouchEvent.ACTION_DOWN, 0, null);
		
		final int surfaceWidth = 100;
		final int surfaceHeight = 100;
		
		this.mCamera.convertSceneToSurfaceTouchEvent(touchEvent, surfaceWidth, surfaceHeight);
		
		Assert.assertEquals(50, touchEvent.getX(), DELTA);
		Assert.assertEquals(50, touchEvent.getY(), DELTA);
	}

	public void testConvertSceneToSurfaceTouchEventNonCenter() {
		this.mCamera.setCenter(0, 0);
		
		final TouchEvent touchEvent = TouchEvent.obtain(-50, -50, TouchEvent.ACTION_DOWN, 0, null);
		
		final int surfaceWidth = 100;
		final int surfaceHeight = 100;
		
		this.mCamera.convertSceneToSurfaceTouchEvent(touchEvent, surfaceWidth, surfaceHeight);
		
		Assert.assertEquals(0, touchEvent.getX(), DELTA);
		Assert.assertEquals(0, touchEvent.getY(), DELTA);
	}

	public void testConvertSceneToSurfaceTouchEventNonCenterRotated180() {
		this.mCamera.setCenter(0, 0);
		this.mCamera.setRotation(180);
		
		final TouchEvent touchEvent = TouchEvent.obtain(-50, -50, TouchEvent.ACTION_DOWN, 0, null);
		
		final int surfaceWidth = 100;
		final int surfaceHeight = 100;
		
		this.mCamera.convertSceneToSurfaceTouchEvent(touchEvent, surfaceWidth, surfaceHeight);
		
		Assert.assertEquals(100, touchEvent.getX(), DELTA);
		Assert.assertEquals(100, touchEvent.getY(), DELTA);
	}
	
	public void testConvertSceneToSurfaceTouchEventNonCenterRotated90() {
		this.mCamera.setCenter(0, 0);
		this.mCamera.setRotation(90);
		
		final TouchEvent touchEvent = TouchEvent.obtain(-50, -50, TouchEvent.ACTION_DOWN, 0, null);
		
		final int surfaceWidth = 100;
		final int surfaceHeight = 100;
		
		this.mCamera.convertSceneToSurfaceTouchEvent(touchEvent, surfaceWidth, surfaceHeight);
		
		Assert.assertEquals(0, touchEvent.getX(), DELTA);
		Assert.assertEquals(100, touchEvent.getY(), DELTA);
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
