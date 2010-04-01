package org.anddev.andengine.engine;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.entity.IUpdateHandler;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.UpdateHandlerList;
import org.anddev.andengine.entity.handler.timer.ITimerCallback;
import org.anddev.andengine.entity.handler.timer.TimerHandler;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureRegion;
import org.anddev.andengine.opengl.texture.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.util.MathUtils;
import org.anddev.andengine.util.constants.TimeConstants;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Debug;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


/**
 * @author Nicolas Gramlich
 * @since 12:21:31 - 08.03.2010
 */
public class Engine implements SensorEventListener, OnTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mRunning = false;

	private long mLastTick = System.nanoTime();

	private final EngineOptions mEngineOptions;

	protected Scene mScene;

	private final TextureManager mTextureManager = new TextureManager();

	private IAccelerometerListener mAccelerometerListener;
	private AccelerometerData mAccelerometerData;

	private final UpdateHandlerList mPreFrameHandlers = new UpdateHandlerList();
	private final UpdateHandlerList mPostFrameHandlers = new UpdateHandlerList();

	protected int mSurfaceWidth = 1; // 1 to prevent accidental DIV/0
	protected int mSurfaceHeight = 1; // 1 to prevent accidental DIV/0

	// ===========================================================
	// Constructors
	// ===========================================================

	public Engine(final EngineOptions pEngineOptions) {
		this.mEngineOptions = pEngineOptions;
		if(this.mEngineOptions.hasLoadingScreen()) {
			this.initLoadingScreen();
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isRunning() {
		return this.mRunning;
	}

	public void start() {
		if(!this.mRunning){
			this.mLastTick = System.nanoTime();
		}
		this.mRunning = true;
	}

	public void stop() {
		this.mRunning = false;
	}

	public Scene getScene() {
		return this.mScene;
	}

	public void setScene(final Scene pScene) {
		this.mScene = pScene;
	}

	public EngineOptions getEngineOptions() {
		return this.mEngineOptions;
	}

	public Camera getCamera() {
		return this.mEngineOptions.getCamera();
	}

	public void setSurfaceSize(final int pSurfaceWidth, final int pSurfaceHeight) {
		this.mSurfaceWidth = pSurfaceWidth;
		this.mSurfaceHeight = pSurfaceHeight;
	}

	public int getSurfaceWidth() {
		return this.mSurfaceWidth;
	}

	public int getSurfaceHeight() {
		return this.mSurfaceHeight;
	}

	public AccelerometerData getAccelerometerData() {
		return this.mAccelerometerData;
	}

	public void clearPreFrameHandlers() {
		this.mPreFrameHandlers.clear();
	}

	public void clearPostFrameHandlers() {
		this.mPostFrameHandlers.clear();
	}

	public void registerPreFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPreFrameHandlers.add(pUpdateHandler);
	}

	public void registerPostFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPostFrameHandlers.add(pUpdateHandler);
	}

	public void unregisterPreFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPreFrameHandlers.remove(pUpdateHandler);
	}

	public void unregisterPostFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPostFrameHandlers.remove(pUpdateHandler);
	}

	public void startPerformanceTracing(final String pTraceFileName) {
		Debug.startMethodTracing("AndEngine/" + pTraceFileName);
	}

	public void stopPerformanceTracing() {
		Debug.stopMethodTracing();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onAccuracyChanged(final Sensor pSensor, final int pAccuracy) {
		if(this.mRunning){
			switch(pSensor.getType()){
				case Sensor.TYPE_ACCELEROMETER:
					this.mAccelerometerData.setAccuracy(pAccuracy);
					this.mAccelerometerListener.onAccelerometerChanged(this.mAccelerometerData);
					break;
			}
		}
	}

	@Override
	public void onSensorChanged(final SensorEvent pEvent) {
		if(this.mRunning){
			switch(pEvent.sensor.getType()){
				case Sensor.TYPE_ACCELEROMETER:
					this.mAccelerometerData.setValues(pEvent.values);
					this.mAccelerometerListener.onAccelerometerChanged(this.mAccelerometerData);
					break;
			}
		}
	}

	@Override
	public boolean onTouch(final View pView, final MotionEvent pMotionEvent) {
		if(this.mRunning) {
			final Camera camera = this.getCameraFromSurfaceMotionEvent(pMotionEvent);
			final MotionEvent sceneMotionEvent = this.convertSurfaceToSceneMotionEvent(camera, pMotionEvent);

			if(this.onTouchHUD(camera, pMotionEvent)) {
				return true;
			} else {
				/* If HUD didn't handle it, Scene may handle it. */
				return this.onTouchScene(sceneMotionEvent, pMotionEvent);
			}
		} else {
			return false;
		}
	}

	protected boolean onTouchHUD(final Camera pCamera, final MotionEvent pSceneMotionEvent) {
		if(pCamera.hasHUD()) {
			return pCamera.getHUD().onSceneTouchEvent(pSceneMotionEvent);
		} else {
			return false;
		}
	}

	protected boolean onTouchScene(final MotionEvent pSceneMotionEvent, final MotionEvent pRawMotionEvent) {
		if(this.mScene != null) {
			return this.mScene.onSceneTouchEvent(pSceneMotionEvent);
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected Camera getCameraFromSurfaceMotionEvent(final MotionEvent pMotionEvent) {
		return this.getCamera();
	}

	protected MotionEvent convertSurfaceToSceneMotionEvent(final Camera pCamera, final MotionEvent pSurfaceMotionEvent) {
		pCamera.convertSurfaceToSceneMotionEvent(pSurfaceMotionEvent, this.mSurfaceWidth, this.mSurfaceHeight);
		return pSurfaceMotionEvent;
	}

	private void initLoadingScreen() {
		final ITextureSource loadingScreenTextureSource = this.mEngineOptions.getLoadingScreenTextureSource();
		final int loadingScreenWidth = loadingScreenTextureSource.getWidth();
		final int loadingScreenHeight = loadingScreenTextureSource.getHeight();
		final Texture loadingScreenTexture = new Texture(MathUtils.nextPowerOfTwo(loadingScreenWidth), MathUtils.nextPowerOfTwo(loadingScreenHeight));
		final TextureRegion loadingScreenTextureRegion = TextureRegionFactory.createFromSource(loadingScreenTexture, loadingScreenTextureSource, 0, 0);

		final Camera cam = this.getCamera();
		final Sprite loadingScreenSprite = new Sprite(cam.getMinX(), cam.getMinY(), cam.getWidth(), cam.getHeight(), loadingScreenTextureRegion);

		this.loadTexture(loadingScreenTexture);

		final Scene loadingScene = new Scene(1);
		loadingScene.getLayer(0).addEntity(loadingScreenSprite);
		this.setScene(loadingScene);
	}

	public void onLoadComplete(final Scene pScene) {
		// final Scene loadingScene = this.mScene; // TODO Free texture from loading-screen.
		if(this.mEngineOptions.hasLoadingScreen()){
			this.registerPreFrameHandler(new TimerHandler(2, new ITimerCallback() {
				@Override
				public void onTimePassed() {
					Engine.this.setScene(pScene);
				}
			}));
		} else {
			this.setScene(pScene);
		}
	}

	public void onDrawFrame(final GL10 pGL) {
		final float secondsElapsed = this.getSecondsElapsed();
		this.mTextureManager.loadPendingTextureToHardware(pGL);

		if(this.mRunning) {
			this.updatePreFrameHandlers(secondsElapsed);

			if(this.mScene != null){
				this.mScene.updatePreFrameHandlers(secondsElapsed);

				this.mScene.onUpdate(secondsElapsed);

				this.onDrawScene(pGL);

				this.mScene.updatePostFrameHandlers(secondsElapsed);
			}

			this.updatePostFrameHandlers(secondsElapsed);
		} else {
			if(this.mScene != null){
				this.onDrawScene(pGL);
			}
		}
	}

	protected void updatePreFrameHandlers(final float pSecondsElapsed) {
		this.getCamera().onUpdate(pSecondsElapsed);
		this.mPreFrameHandlers.onUpdate(pSecondsElapsed);
	}

	protected void updatePostFrameHandlers(final float pSecondsElapsed) {
		this.mPostFrameHandlers.onUpdate(pSecondsElapsed);
	}

	protected void onDrawScene(final GL10 pGL) {
		this.mScene.onDraw(pGL);
		this.getCamera().onDrawHUD(pGL);
	}

	private float getSecondsElapsed() {
		final long now = System.nanoTime();
		final float secondsElapsed = (float)(now  - this.mLastTick) / TimeConstants.NANOSECONDSPERSECOND;
		this.mLastTick = now;
		return secondsElapsed;
	}

	public void reloadTextures() {
		this.mTextureManager.reloadLoadedToPendingTextures();
	}

	public void loadTexture(final Texture pTexture) {
		this.mTextureManager.addTexturePendingForBeingLoadedToHardware(pTexture);
	}

	public void loadTextures(final Texture ... pTextures) {
		for(int i = pTextures.length - 1; i >= 0; i--) {
			this.mTextureManager.addTexturePendingForBeingLoadedToHardware(pTextures[i]);
		}
	}

	public boolean enableAccelerometer(final Context pContext, final IAccelerometerListener pAccelerometerListener) {
		final SensorManager sensorManager = (SensorManager) pContext.getSystemService(Context.SENSOR_SERVICE);
		if (this.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER)) {
			this.registerSelfAsSensorListener(sensorManager, Sensor.TYPE_ACCELEROMETER);

			this.mAccelerometerListener = pAccelerometerListener;
			if(this.mAccelerometerData == null) {
				this.mAccelerometerData = new AccelerometerData();
			}

			return true;
		} else {
			return false;
		}
	}

	private void registerSelfAsSensorListener(final SensorManager pSensorManager, final int pType) {
		final Sensor accelerometer = pSensorManager.getSensorList(pType).get(0);
		pSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
	}

	private boolean isSensorSupported(final SensorManager pSensorManager, final int pType) {
		return pSensorManager.getSensorList(pType).size() > 0;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
