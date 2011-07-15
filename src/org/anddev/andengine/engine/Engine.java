package org.anddev.andengine.engine;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.music.MusicManager;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.audio.sound.SoundManager;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.UpdateHandlerList;
import org.anddev.andengine.engine.handler.runnable.RunnableHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.controller.ITouchController;
import org.anddev.andengine.input.touch.controller.ITouchController.ITouchEventCallback;
import org.anddev.andengine.input.touch.controller.SingleTouchControler;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.font.FontManager;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.sensor.SensorDelay;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.AccelerometerSensorOptions;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.sensor.location.ILocationListener;
import org.anddev.andengine.sensor.location.LocationProviderStatus;
import org.anddev.andengine.sensor.location.LocationSensorOptions;
import org.anddev.andengine.sensor.orientation.IOrientationListener;
import org.anddev.andengine.sensor.orientation.OrientationData;
import org.anddev.andengine.sensor.orientation.OrientationSensorOptions;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.constants.TimeConstants;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:21:31 - 08.03.2010
 */
public class Engine implements SensorEventListener, OnTouchListener, ITouchEventCallback, TimeConstants, LocationListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final SensorDelay SENSORDELAY_DEFAULT = SensorDelay.GAME;
	private static final int UPDATEHANDLERS_CAPACITY_DEFAULT = 32;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mRunning = false;

	private long mLastTick = -1;
	private float mSecondsElapsedTotal = 0;

	private final State mThreadLocker = new State();

	private final UpdateThread mUpdateThread = new UpdateThread();

	private final RunnableHandler mUpdateThreadRunnableHandler = new RunnableHandler();

	private final EngineOptions mEngineOptions;
	protected final Camera mCamera;

	private ITouchController mTouchController;

	private SoundManager mSoundManager;
	private MusicManager mMusicManager;
	private final TextureManager mTextureManager = new TextureManager();
	private final BufferObjectManager mBufferObjectManager = new BufferObjectManager();
	private final FontManager mFontManager = new FontManager();

	protected Scene mScene;

	private Vibrator mVibrator;

	private ILocationListener mLocationListener;
	private Location mLocation;

	private IAccelerometerListener mAccelerometerListener;
	private AccelerometerData mAccelerometerData;

	private IOrientationListener mOrientationListener;
	private OrientationData mOrientationData;

	private final UpdateHandlerList mUpdateHandlers = new UpdateHandlerList(UPDATEHANDLERS_CAPACITY_DEFAULT);

	protected int mSurfaceWidth = 1; // 1 to prevent accidental DIV/0
	protected int mSurfaceHeight = 1; // 1 to prevent accidental DIV/0

	private boolean mIsMethodTracing;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Engine(final EngineOptions pEngineOptions) {
		BitmapTextureAtlasTextureRegionFactory.reset();
		SoundFactory.reset();
		MusicFactory.reset();
		FontFactory.reset();

		BufferObjectManager.setActiveInstance(this.mBufferObjectManager);

		this.mEngineOptions = pEngineOptions;
		this.setTouchController(new SingleTouchControler());
		this.mCamera = pEngineOptions.getCamera();

		if(this.mEngineOptions.needsSound()) {
			this.mSoundManager = new SoundManager();
		}

		if(this.mEngineOptions.needsMusic()) {
			this.mMusicManager = new MusicManager();
		}

		this.mUpdateThread.start();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isRunning() {
		return this.mRunning;
	}

	public synchronized void start() {
		if(!this.mRunning) {
			this.mLastTick = System.nanoTime();
			this.mRunning = true;
		}
	}

	public synchronized void stop() {
		if(this.mRunning) {
			this.mRunning = false;
		}
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
		return this.mCamera;
	}

	public float getSecondsElapsedTotal() {
		return this.mSecondsElapsedTotal;
	}

	public void setSurfaceSize(final int pSurfaceWidth, final int pSurfaceHeight) {
		//		Debug.w("SurfaceView size changed to (width x height): " + pSurfaceWidth + " x " + pSurfaceHeight, new Exception());
		this.mSurfaceWidth = pSurfaceWidth;
		this.mSurfaceHeight = pSurfaceHeight;
		this.onUpdateCameraSurface();
	}

	protected void onUpdateCameraSurface() {
		this.mCamera.setSurfaceSize(0, 0, this.mSurfaceWidth, this.mSurfaceHeight);
	}

	public int getSurfaceWidth() {
		return this.mSurfaceWidth;
	}

	public int getSurfaceHeight() {
		return this.mSurfaceHeight;
	}

	public ITouchController getTouchController() {
		return this.mTouchController;
	}

	public void setTouchController(final ITouchController pTouchController) {
		this.mTouchController = pTouchController;
		this.mTouchController.applyTouchOptions(this.mEngineOptions.getTouchOptions());
		this.mTouchController.setTouchEventCallback(this);
	}

	public AccelerometerData getAccelerometerData() {
		return this.mAccelerometerData;
	}

	public OrientationData getOrientationData() {
		return this.mOrientationData;
	}

	public SoundManager getSoundManager() throws IllegalStateException {
		if(this.mSoundManager != null) {
			return this.mSoundManager;
		} else {
			throw new IllegalStateException("To enable the SoundManager, check the EngineOptions!");
		}
	}

	public MusicManager getMusicManager() throws IllegalStateException {
		if(this.mMusicManager != null) {
			return this.mMusicManager;
		} else {
			throw new IllegalStateException("To enable the MusicManager, check the EngineOptions!");
		}
	}

	public TextureManager getTextureManager() {
		return this.mTextureManager;
	}

	public FontManager getFontManager() {
		return this.mFontManager;
	}

	public void clearUpdateHandlers() {
		this.mUpdateHandlers.clear();
	}

	public void registerUpdateHandler(final IUpdateHandler pUpdateHandler) {
		this.mUpdateHandlers.add(pUpdateHandler);
	}

	public void unregisterUpdateHandler(final IUpdateHandler pUpdateHandler) {
		this.mUpdateHandlers.remove(pUpdateHandler);
	}

	public boolean isMethodTracing() {
		return this.mIsMethodTracing;
	}

	public void startMethodTracing(final String pTraceFileName) {
		if(!this.mIsMethodTracing) {
			this.mIsMethodTracing = true;
			android.os.Debug.startMethodTracing(pTraceFileName);
		}
	}

	public void stopMethodTracing() {
		if(this.mIsMethodTracing) {
			android.os.Debug.stopMethodTracing();
			this.mIsMethodTracing = false;
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onAccuracyChanged(final Sensor pSensor, final int pAccuracy) {
		if(this.mRunning) {
			switch(pSensor.getType()) {
				case Sensor.TYPE_ACCELEROMETER:
					if(this.mAccelerometerData != null) {
						this.mAccelerometerData.setAccuracy(pAccuracy);
						this.mAccelerometerListener.onAccelerometerChanged(this.mAccelerometerData);
					} else if(this.mOrientationData != null) {
						this.mOrientationData.setAccelerometerAccuracy(pAccuracy);
						this.mOrientationListener.onOrientationChanged(this.mOrientationData);
					}
					break;
				case Sensor.TYPE_MAGNETIC_FIELD:
					this.mOrientationData.setMagneticFieldAccuracy(pAccuracy);
					this.mOrientationListener.onOrientationChanged(this.mOrientationData);
					break;
			}
		}
	}

	@Override
	public void onSensorChanged(final SensorEvent pEvent) {
		if(this.mRunning) {
			switch(pEvent.sensor.getType()) {
				case Sensor.TYPE_ACCELEROMETER:
					if(this.mAccelerometerData != null) {
						this.mAccelerometerData.setValues(pEvent.values);
						this.mAccelerometerListener.onAccelerometerChanged(this.mAccelerometerData);
					} else if(this.mOrientationData != null) {
						this.mOrientationData.setAccelerometerValues(pEvent.values);
						this.mOrientationListener.onOrientationChanged(this.mOrientationData);
					}
					break;
				case Sensor.TYPE_MAGNETIC_FIELD:
					this.mOrientationData.setMagneticFieldValues(pEvent.values);
					this.mOrientationListener.onOrientationChanged(this.mOrientationData);
					break;
			}
		}
	}

	@Override
	public void onLocationChanged(final Location pLocation) {
		if(this.mLocation == null) {
			this.mLocation = pLocation;
		} else {
			if(pLocation == null) {
				this.mLocationListener.onLocationLost();
			} else {
				this.mLocation = pLocation;
				this.mLocationListener.onLocationChanged(pLocation);
			}
		}
	}

	@Override
	public void onProviderDisabled(final String pProvider) {
		this.mLocationListener.onLocationProviderDisabled();
	}

	@Override
	public void onProviderEnabled(final String pProvider) {
		this.mLocationListener.onLocationProviderEnabled();
	}

	@Override
	public void onStatusChanged(final String pProvider, final int pStatus, final Bundle pExtras) {
		switch(pStatus) {
			case LocationProvider.AVAILABLE:
				this.mLocationListener.onLocationProviderStatusChanged(LocationProviderStatus.AVAILABLE, pExtras);
				break;
			case LocationProvider.OUT_OF_SERVICE:
				this.mLocationListener.onLocationProviderStatusChanged(LocationProviderStatus.OUT_OF_SERVICE, pExtras);
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				this.mLocationListener.onLocationProviderStatusChanged(LocationProviderStatus.TEMPORARILY_UNAVAILABLE, pExtras);
				break;
		}
	}

	@Override
	public boolean onTouch(final View pView, final MotionEvent pSurfaceMotionEvent) {
		if(this.mRunning) {
			final boolean handled = this.mTouchController.onHandleMotionEvent(pSurfaceMotionEvent);
			try {
				/*
				 * As a human cannot interact 1000x per second, we pause the
				 * UI-Thread for a little.
				 */
				Thread.sleep(20); // TODO Maybe this can be removed, when TouchEvents are handled on the UpdateThread!
			} catch (final InterruptedException e) {
				Debug.e(e);
			}
			return handled;
		} else {
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(final TouchEvent pSurfaceTouchEvent) {
		/*
		 * Let the engine determine which scene and camera this event should be
		 * handled by.
		 */
		final Scene scene = this.getSceneFromSurfaceTouchEvent(pSurfaceTouchEvent);
		final Camera camera = this.getCameraFromSurfaceTouchEvent(pSurfaceTouchEvent);

		this.convertSurfaceToSceneTouchEvent(camera, pSurfaceTouchEvent);

		if(this.onTouchHUD(camera, pSurfaceTouchEvent)) {
			return true;
		} else {
			/* If HUD didn't handle it, Scene may handle it. */
			return this.onTouchScene(scene, pSurfaceTouchEvent);
		}
	}

	protected boolean onTouchHUD(final Camera pCamera, final TouchEvent pSceneTouchEvent) {
		if(pCamera.hasHUD()) {
			return pCamera.getHUD().onSceneTouchEvent(pSceneTouchEvent);
		} else {
			return false;
		}
	}

	protected boolean onTouchScene(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		if(pScene != null) {
			return pScene.onSceneTouchEvent(pSceneTouchEvent);
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void runOnUpdateThread(final Runnable pRunnable) {
		this.mUpdateThreadRunnableHandler.postRunnable(pRunnable);
	}

	public void interruptUpdateThread(){
		this.mUpdateThread.interrupt();
	}

	public void onResume() {
		// TODO GLHelper.reset(pGL); ?
		this.mTextureManager.reloadTextures();
		this.mFontManager.reloadFonts();
		BufferObjectManager.setActiveInstance(this.mBufferObjectManager);
		this.mBufferObjectManager.reloadBufferObjects();
	}

	public void onPause() {

	}

	protected Camera getCameraFromSurfaceTouchEvent(@SuppressWarnings("unused") final TouchEvent pTouchEvent) {
		return this.getCamera();
	}

	protected Scene getSceneFromSurfaceTouchEvent(@SuppressWarnings("unused") final TouchEvent pTouchEvent) {
		return this.mScene;
	}

	protected void convertSurfaceToSceneTouchEvent(final Camera pCamera, final TouchEvent pSurfaceTouchEvent) {
		pCamera.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent, this.mSurfaceWidth, this.mSurfaceHeight);
	}

	public void onLoadComplete(final Scene pScene) {
		this.setScene(pScene);
	}

	void onTickUpdate() throws InterruptedException {
		if(this.mRunning) {
			final long secondsElapsed = this.getNanosecondsElapsed();

			this.onUpdate(secondsElapsed);

			this.yieldDraw();
		} else {
			this.yieldDraw();

			Thread.sleep(16);
		}
	}

	private void yieldDraw() throws InterruptedException {
		final State threadLocker = this.mThreadLocker;
		threadLocker.notifyCanDraw();
		threadLocker.waitUntilCanUpdate();
	}

	protected void onUpdate(final long pNanosecondsElapsed) throws InterruptedException {
		final float pSecondsElapsed = (float)pNanosecondsElapsed / TimeConstants.NANOSECONDSPERSECOND;

		this.mSecondsElapsedTotal += pSecondsElapsed;
		this.mLastTick += pNanosecondsElapsed;

		this.mTouchController.onUpdate(pSecondsElapsed);
		this.updateUpdateHandlers(pSecondsElapsed);
		this.onUpdateScene(pSecondsElapsed);
	}

	protected void onUpdateScene(final float pSecondsElapsed) {
		if(this.mScene != null) {
			this.mScene.onUpdate(pSecondsElapsed);
		}
	}

	protected void updateUpdateHandlers(final float pSecondsElapsed) {
		this.mUpdateThreadRunnableHandler.onUpdate(pSecondsElapsed);
		this.mUpdateHandlers.onUpdate(pSecondsElapsed);
		this.getCamera().onUpdate(pSecondsElapsed);
	}

	public void onDrawFrame(final GL10 pGL) throws InterruptedException {
		final State threadLocker = this.mThreadLocker;

		threadLocker.waitUntilCanDraw();

		this.mTextureManager.updateTextures(pGL);
		this.mFontManager.updateFonts(pGL);
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			this.mBufferObjectManager.updateBufferObjects((GL11) pGL);
		}

		this.onDrawScene(pGL);

		threadLocker.notifyCanUpdate();
	}

	protected void onDrawScene(final GL10 pGL) {
		final Camera camera = this.getCamera();

		this.mScene.onDraw(pGL, camera);

		camera.onDrawHUD(pGL);
	}

	private long getNanosecondsElapsed() {
		final long now = System.nanoTime();

		return this.calculateNanosecondsElapsed(now, this.mLastTick);
	}

	protected long calculateNanosecondsElapsed(final long pNow, final long pLastTick) {
		return pNow - pLastTick;
	}

	public boolean enableVibrator(final Context pContext) {
		this.mVibrator = (Vibrator) pContext.getSystemService(Context.VIBRATOR_SERVICE);
		return this.mVibrator != null;
	}

	public void vibrate(final long pMilliseconds) throws IllegalStateException {
		if(this.mVibrator != null) {
			this.mVibrator.vibrate(pMilliseconds);
		} else {
			throw new IllegalStateException("You need to enable the Vibrator before you can use it!");
		}
	}

	public void vibrate(final long[] pPattern, final int pRepeat) throws IllegalStateException {
		if(this.mVibrator != null) {
			this.mVibrator.vibrate(pPattern, pRepeat);
		} else {
			throw new IllegalStateException("You need to enable the Vibrator before you can use it!");
		}
	}

	public void enableLocationSensor(final Context pContext, final ILocationListener pLocationListener, final LocationSensorOptions pLocationSensorOptions) {
		this.mLocationListener = pLocationListener;

		final LocationManager locationManager = (LocationManager) pContext.getSystemService(Context.LOCATION_SERVICE);
		final String locationProvider = locationManager.getBestProvider(pLocationSensorOptions, pLocationSensorOptions.isEnabledOnly());
		// TODO locationProvider can be null, in that case return false. Successful case should return true.
		locationManager.requestLocationUpdates(locationProvider, pLocationSensorOptions.getMinimumTriggerTime(), pLocationSensorOptions.getMinimumTriggerDistance(), this);

		this.onLocationChanged(locationManager.getLastKnownLocation(locationProvider));
	}

	public void disableLocationSensor(final Context pContext) {
		final LocationManager locationManager = (LocationManager) pContext.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);
	}

	/**
	 * @see {@link Engine#enableAccelerometerSensor(Context, IAccelerometerListener, AccelerometerSensorOptions)}
	 */
	public boolean enableAccelerometerSensor(final Context pContext, final IAccelerometerListener pAccelerometerListener) {
		return this.enableAccelerometerSensor(pContext, pAccelerometerListener, new AccelerometerSensorOptions(SENSORDELAY_DEFAULT));
	}

	/**
	 * @return <code>true</code> when the sensor was successfully enabled, <code>false</code> otherwise.
	 */
	public boolean enableAccelerometerSensor(final Context pContext, final IAccelerometerListener pAccelerometerListener, final AccelerometerSensorOptions pAccelerometerSensorOptions) {
		final SensorManager sensorManager = (SensorManager) pContext.getSystemService(Context.SENSOR_SERVICE);
		if(this.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER)) {
			this.mAccelerometerListener = pAccelerometerListener;

			if(this.mAccelerometerData == null) {
				final Display display = ((WindowManager) pContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
				final int displayRotation = display.getOrientation();
				this.mAccelerometerData = new AccelerometerData(displayRotation);
			}

			this.registerSelfAsSensorListener(sensorManager, Sensor.TYPE_ACCELEROMETER, pAccelerometerSensorOptions.getSensorDelay());

			return true;
		} else {
			return false;
		}
	}


	/**
	 * @return <code>true</code> when the sensor was successfully disabled, <code>false</code> otherwise.
	 */
	public boolean disableAccelerometerSensor(final Context pContext) {
		final SensorManager sensorManager = (SensorManager) pContext.getSystemService(Context.SENSOR_SERVICE);
		if(this.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER)) {
			this.unregisterSelfAsSensorListener(sensorManager, Sensor.TYPE_ACCELEROMETER);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see {@link Engine#enableOrientationSensor(Context, IOrientationListener, OrientationSensorOptions)}
	 */
	public boolean enableOrientationSensor(final Context pContext, final IOrientationListener pOrientationListener) {
		return this.enableOrientationSensor(pContext, pOrientationListener, new OrientationSensorOptions(SENSORDELAY_DEFAULT));
	}

	/**
	 * @return <code>true</code> when the sensor was successfully enabled, <code>false</code> otherwise.
	 */
	public boolean enableOrientationSensor(final Context pContext, final IOrientationListener pOrientationListener, final OrientationSensorOptions pOrientationSensorOptions) {
		final SensorManager sensorManager = (SensorManager) pContext.getSystemService(Context.SENSOR_SERVICE);
		if(this.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER) && this.isSensorSupported(sensorManager, Sensor.TYPE_MAGNETIC_FIELD)) {
			this.mOrientationListener = pOrientationListener;

			if(this.mOrientationData == null) {
				final Display display = ((WindowManager) pContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
				final int displayRotation = display.getOrientation();
				this.mOrientationData = new OrientationData(displayRotation);
			}

			this.registerSelfAsSensorListener(sensorManager, Sensor.TYPE_ACCELEROMETER, pOrientationSensorOptions.getSensorDelay());
			this.registerSelfAsSensorListener(sensorManager, Sensor.TYPE_MAGNETIC_FIELD, pOrientationSensorOptions.getSensorDelay());

			return true;
		} else {
			return false;
		}
	}


	/**
	 * @return <code>true</code> when the sensor was successfully disabled, <code>false</code> otherwise.
	 */
	public boolean disableOrientationSensor(final Context pContext) {
		final SensorManager sensorManager = (SensorManager) pContext.getSystemService(Context.SENSOR_SERVICE);
		if(this.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER) && this.isSensorSupported(sensorManager, Sensor.TYPE_MAGNETIC_FIELD)) {
			this.unregisterSelfAsSensorListener(sensorManager, Sensor.TYPE_ACCELEROMETER);
			this.unregisterSelfAsSensorListener(sensorManager, Sensor.TYPE_MAGNETIC_FIELD);
			return true;
		} else {
			return false;
		}
	}

	private boolean isSensorSupported(final SensorManager pSensorManager, final int pType) {
		return pSensorManager.getSensorList(pType).size() > 0;
	}

	private void registerSelfAsSensorListener(final SensorManager pSensorManager, final int pType, final SensorDelay pSensorDelay) {
		final Sensor sensor = pSensorManager.getSensorList(pType).get(0);
		pSensorManager.registerListener(this, sensor, pSensorDelay.getDelay());
	}

	private void unregisterSelfAsSensorListener(final SensorManager pSensorManager, final int pType) {
		final Sensor sensor = pSensorManager.getSensorList(pType).get(0);
		pSensorManager.unregisterListener(this, sensor);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private class UpdateThread extends Thread {
		public UpdateThread() {
			super("UpdateThread");
		}

		@Override
		public void run() {
			android.os.Process.setThreadPriority(Engine.this.mEngineOptions.getUpdateThreadPriority());
			try {
				while(true) {
					Engine.this.onTickUpdate();
				}
			} catch (final InterruptedException e) {
				Debug.d("UpdateThread interrupted. Don't worry - this Exception is most likely expected!", e);
				this.interrupt();
			}
		}
	}

	private static class State {
		boolean mDrawing = false;

		public synchronized void notifyCanDraw() {
			// Debug.d(">>> notifyCanDraw");
			this.mDrawing = true;
			this.notifyAll();
			// Debug.d("<<< notifyCanDraw");
		}

		public synchronized void notifyCanUpdate() {
			// Debug.d(">>> notifyCanUpdate");
			this.mDrawing = false;
			this.notifyAll();
			// Debug.d("<<< notifyCanUpdate");
		}

		public synchronized void waitUntilCanDraw() throws InterruptedException {
			// Debug.d(">>> waitUntilCanDraw");
			while(!this.mDrawing) {
				this.wait();
			}
			// Debug.d("<<< waitUntilCanDraw");
		}

		public synchronized void waitUntilCanUpdate() throws InterruptedException {
			// Debug.d(">>> waitUntilCanUpdate");
			while(this.mDrawing) {
				this.wait();
			}
			// Debug.d("<<< waitUntilCanUpdate");
		}
	}
}
