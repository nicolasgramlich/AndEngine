package org.anddev.andengine.engine;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.entity.IUpdateHandler;
import org.anddev.andengine.entity.Scene;
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


/**
 * @author Nicolas Gramlich
 * @since 12:21:31 - 08.03.2010
 */
public class Engine implements SensorEventListener {
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

	private final ArrayList<IUpdateHandler> mPreFrameHandlers = new ArrayList<IUpdateHandler>();
	private final ArrayList<IUpdateHandler> mPostFrameHandlers = new ArrayList<IUpdateHandler>();
	
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
		switch(pSensor.getType()){
			case Sensor.TYPE_ACCELEROMETER:
				this.mAccelerometerData.setAccuracy(pAccuracy);
				if(this.mRunning){
					this.mAccelerometerListener.onAccelerometerChanged(this.mAccelerometerData);
				}
				break;
		}
	}

	@Override
	public void onSensorChanged(final SensorEvent pEvent) {
		switch(pEvent.sensor.getType()){
			case Sensor.TYPE_ACCELEROMETER:
				this.mAccelerometerData.setValues(pEvent.values);
				if(this.mRunning){
					this.mAccelerometerListener.onAccelerometerChanged(this.mAccelerometerData);
				}
				break;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public MotionEvent surfaceToSceneMotionEvent(final MotionEvent pMotionEvent) {
		final Camera camera = this.getCamera();

		final float x = camera.relativeToAbsoluteX(pMotionEvent.getX() / this.mSurfaceWidth);
		final float y = camera.relativeToAbsoluteY(pMotionEvent.getY() / this.mSurfaceHeight);
		
		pMotionEvent.setLocation(x, y);
		return pMotionEvent;
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
		// TODO Pre/PostFrame Handlers already react!
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
				this.mScene.onUpdate(secondsElapsed);

				this.onDrawScene(pGL);
			}

			this.updatePostFrameHandlers(secondsElapsed);
		}
	}

	protected void onDrawScene(final GL10 pGL) {
		this.mScene.onDraw(pGL);
		this.getCamera().onDrawHUD(pGL);
	}

	protected void updatePreFrameHandlers(final float pSecondsElapsed) {
		this.getCamera().onUpdate(pSecondsElapsed);
		final ArrayList<IUpdateHandler> updateHandlers = this.mPreFrameHandlers;
		this.updateHandlers(pSecondsElapsed, updateHandlers);
	}

	protected void updatePostFrameHandlers(final float pSecondsElapsed) {
		final ArrayList<IUpdateHandler> updateHandlers = this.mPostFrameHandlers;
		this.updateHandlers(pSecondsElapsed, updateHandlers);
	}

	private void updateHandlers(final float pSecondsElapsed, final ArrayList<IUpdateHandler> pUpdateHandlers) {
		final int handlerCount = pUpdateHandlers.size();
		for(int i = 0; i < handlerCount; i++) {
			pUpdateHandlers.get(i).onUpdate(pSecondsElapsed);
		}
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
