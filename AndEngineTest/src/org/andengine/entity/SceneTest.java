package org.andengine.entity;

import junit.framework.Assert;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.os.SystemClock;
import android.test.AndroidTestCase;
import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 15:27:27 - 12.05.2010
 */
public class SceneTest extends AndroidTestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Engine mEngine;
	private VertexBufferObjectManager mVertexBufferObjectManager;
	private Camera mCamera;
	private Scene mScene;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void setUp() throws Exception {
		this.mCamera = new Camera(0, 0, 100, 100);
		this.mEngine = new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), this.mCamera));
		this.mEngine.setSurfaceSize(100, 100);

		this.mVertexBufferObjectManager = this.mEngine.getVertexBufferObjectManager();

		this.mScene = new Scene();

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
		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final float expectedX = 50;
		final float expectedY = 50;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testSceneTouchEdge() throws Exception {
		final int surfaceTouchX = 0;
		final int surfaceTouchY = 100;

		final float expectedX = 0;
		final float expectedY = 100;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testSceneTouchOffsetCamera() throws Exception {
		this.mCamera.setCenter(0, 0);

		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final float expectedX = 0;
		final float expectedY = 0;

		this.testSceneTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testChildSceneTouch() throws Exception {
		final Scene childScene = new Scene();

		this.mScene.setChildSceneModal(childScene);

		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final float expectedX = 50;
		final float expectedY = 50;

		this.testSceneTouchWorker(childScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testChildSceneTouchOffsetCamera() throws Exception {
		this.mCamera.setCenter(0, 0);

		final Scene childScene = new Scene();

		this.mScene.setChildSceneModal(childScene);

		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final float expectedX = 0;
		final float expectedY = 0;

		this.testSceneTouchWorker(childScene, surfaceTouchX, surfaceTouchY, expectedX, expectedY);
	}

	public void testAreaTouchSimple() throws Exception {
		final int surfaceTouchX = 50;
		final int surfaceTouchY = 50;

		final ITouchArea touchArea = new Rectangle(0, 0, 50, 50, this.mVertexBufferObjectManager);

		this.testAreaTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, touchArea);
	}

	public void testAreaTouchOutside() throws Exception {
		final int surfaceTouchX = 51;
		final int surfaceTouchY = 51;

		final ITouchArea touchArea = new Rectangle(0, 0, 50, 50, this.mVertexBufferObjectManager);

		this.testAreaTouchWorker(this.mScene, surfaceTouchX, surfaceTouchY, touchArea);
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
				Assert.assertEquals(pExpectedX, actualX);
				Assert.assertEquals(pExpectedY, actualY);
				return true;
			}
		});

		final long uptimeMillis = SystemClock.uptimeMillis();

		final boolean result = this.mEngine.onTouch(null, MotionEvent.obtain(uptimeMillis, uptimeMillis, MotionEvent.ACTION_DOWN, pSurfaceTouchX, pSurfaceTouchY, 0));

		Assert.assertTrue(result);
	}

	private void testAreaTouchWorker(final Scene pScene, final int pSurfaceTouchX, final int pSurfaceTouchY, final ITouchArea pExpectedTouchArea) throws InterruptedException {
		pScene.registerTouchArea(pExpectedTouchArea);

		pScene.setOnAreaTouchListener(new IOnAreaTouchListener() {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pActualTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				Assert.assertSame(pExpectedTouchArea, pActualTouchArea);
				return true;
			}
		});

		final long uptimeMillis = SystemClock.uptimeMillis();

		this.mEngine.onTouch(null, MotionEvent.obtain(uptimeMillis, uptimeMillis, MotionEvent.ACTION_DOWN, pSurfaceTouchX, pSurfaceTouchY, 0));
		this.mEngine.onUpdate(1);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
