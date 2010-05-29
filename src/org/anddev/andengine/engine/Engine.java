package org.anddev.andengine.engine;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.entity.IUpdateHandler;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.entity.SplashScene;
import org.anddev.andengine.entity.UpdateHandlerList;
import org.anddev.andengine.entity.handler.timer.ITimerCallback;
import org.anddev.andengine.entity.handler.timer.TimerHandler;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.font.FontManager;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureFactory;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.sensor.orientation.IOrientationListener;
import org.anddev.andengine.sensor.orientation.OrientationData;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.constants.TimeConstants;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
	
	private static final float LOADING_SCREEN_DURATION = 2;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mRunning = false;

	private long mLastTick = System.nanoTime();
	private float mSecondsElapsedTotal = 0;

	private final EngineOptions mEngineOptions;

	private TextureManager mTextureManager = new TextureManager();
	private FontManager mFontManager = new FontManager();

	protected Scene mScene;

	private IAccelerometerListener mAccelerometerListener;
	private AccelerometerData mAccelerometerData;
	
	private IOrientationListener mOrientationListener;
	private OrientationData mOrientationData ;

	private final UpdateHandlerList mPreFrameHandlers = new UpdateHandlerList();
	private final UpdateHandlerList mPostFrameHandlers = new UpdateHandlerList();

	protected int mSurfaceWidth = 1; // 1 to prevent accidental DIV/0
	protected int mSurfaceHeight = 1; // 1 to prevent accidental DIV/0

	private final Thread mUpdateThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while(true) {
				Engine.this.onUpdate();
			}
		}
	}, "UpdateThread");

	private final State mThreadLocker = new State();

	private boolean mIsMethodTracing;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Engine(final EngineOptions pEngineOptions) {
		this.mEngineOptions = pEngineOptions;

		BufferObjectManager.clear();
		
		if(this.mEngineOptions.hasLoadingScreen()) {
			initLoadingScreen();
		}
		
		this.mUpdateThread.start();
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
	
	public float getSecondsElapsedTotal() {
		return this.mSecondsElapsedTotal;
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

	public TextureManager getTextureManager() {
		return this.mTextureManager;
	}

	public FontManager getFontManager() {
		return this.mFontManager;
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
				case Sensor.TYPE_ORIENTATION:
					this.mOrientationData.setValues(pEvent.values);
					this.mOrientationListener.onOrientationChanged(this.mOrientationData);
					break;
			}
		}
	}

	@Override
	public boolean onTouch(final View pView, final MotionEvent pSurfaceMotionEvent) {
		if(this.mRunning) {
			/* Let the engine determine which scene and camera this event should be handled by. */
			final Scene scene = this.getSceneFromSurfaceMotionEvent(pSurfaceMotionEvent);
			final Camera camera = this.getCameraFromSurfaceMotionEvent(pSurfaceMotionEvent);
			
			this.convertSurfaceToSceneMotionEvent(camera, pSurfaceMotionEvent);

			if(this.onTouchHUD(camera, pSurfaceMotionEvent)) {
				return true;
			} else {
				/* If HUD didn't handle it, Scene may handle it. */
				return this.onTouchScene(scene, pSurfaceMotionEvent);
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

	protected boolean onTouchScene(final Scene pScene, final MotionEvent pSceneMotionEvent) {
		if(pScene != null) {
			return pScene.onSceneTouchEvent(pSceneMotionEvent);
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void initLoadingScreen() {
		final ITextureSource loadingScreenTextureSource = this.getEngineOptions().getLoadingScreenTextureSource();
		final Texture loadingScreenTexture = TextureFactory.createForTextureSourceSize(loadingScreenTextureSource);
		final TextureRegion loadingScreenTextureRegion = TextureRegionFactory.createFromSource(loadingScreenTexture, loadingScreenTextureSource, 0, 0);
		this.setScene(new SplashScene(this.getCamera(), loadingScreenTextureRegion));
	}

	public void onResume() {
		this.mTextureManager.reloadTextures();
		BufferObjectManager.reloadBufferObjects();
	}

	public void onPause() {
		this.stop();
	}

	protected Camera getCameraFromSurfaceMotionEvent(final MotionEvent pMotionEvent) {
		return this.getCamera();
	}
	
	protected Scene getSceneFromSurfaceMotionEvent(final MotionEvent pMotionEvent) {
		return this.mScene;
	}

	protected void convertSurfaceToSceneMotionEvent(final Camera pCamera, final MotionEvent pSurfaceMotionEvent) {
		pCamera.convertSurfaceToSceneMotionEvent(pSurfaceMotionEvent, this.mSurfaceWidth, this.mSurfaceHeight);
	}

	public void onLoadComplete(final Scene pScene) {
		// final Scene loadingScene = this.mScene; // TODO Free texture from loading-screen.
		if(this.mEngineOptions.hasLoadingScreen()){
			this.registerPreFrameHandler(new TimerHandler(LOADING_SCREEN_DURATION, new ITimerCallback() {
				@Override
				public void onTimePassed() {
					Engine.this.setScene(pScene);
				}
			}));
		} else {
			this.setScene(pScene);
		}
	}

	protected void onUpdate() {
		final float secondsElapsed = getSecondsElapsed();

		if(this.mRunning) {
			this.updatePreFrameHandlers(secondsElapsed);

			if(this.mScene != null){
				onUpdateScenePreFrameHandlers(secondsElapsed);

				onUpdateScene(secondsElapsed);

				this.mThreadLocker.notifyCanDraw();
				this.mThreadLocker.waitUntilCanUpdate();

				onUpdateScenePostFrameHandlers(secondsElapsed);
			} else {
				this.mThreadLocker.notifyCanDraw();
				this.mThreadLocker.waitUntilCanUpdate();
			}

			this.updatePostFrameHandlers(secondsElapsed);
		} else {
			this.mThreadLocker.notifyCanDraw();
			this.mThreadLocker.waitUntilCanUpdate();
			
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				Debug.e("UpdateThread interrupted from sleep.", e);
			}
		}
	}

	public void onDrawFrame(final GL10 pGL) {
		this.mThreadLocker.waitUntilCanDraw();

		this.mTextureManager.ensureTexturesLoadedToHardware(pGL);
		this.mFontManager.ensureFontsLoadedToHardware(pGL);
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			BufferObjectManager.ensureBufferObjectsLoadedToHardware((GL11)pGL);
		}

		this.onDrawScene(pGL);

		this.mThreadLocker.notifyCanUpdate();
	}

	protected void onUpdateScene(final float secondsElapsed) {
		this.mScene.onUpdate(secondsElapsed);
	}

	protected void onUpdateScenePostFrameHandlers(final float secondsElapsed) {
		this.mScene.updatePostFrameHandlers(secondsElapsed);
	}

	protected void onUpdateScenePreFrameHandlers(final float secondsElapsed) {
		this.mScene.updatePreFrameHandlers(secondsElapsed);
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
		
		this.mSecondsElapsedTotal += secondsElapsed;
		
		return secondsElapsed;
	}

	public boolean enableAccelerometerSensor(final Context pContext, final IAccelerometerListener pAccelerometerListener) {
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
	
	public boolean enableOrientationSensor(final Context pContext, final IOrientationListener pOrientationListener) {
		final SensorManager sensorManager = (SensorManager) pContext.getSystemService(Context.SENSOR_SERVICE);
		if (this.isSensorSupported(sensorManager, Sensor.TYPE_ORIENTATION)) {
			this.registerSelfAsSensorListener(sensorManager, Sensor.TYPE_ORIENTATION);

			this.mOrientationListener = pOrientationListener;
			if(this.mOrientationData == null) {
				this.mOrientationData = new OrientationData();
			}

			return true;
		} else {
			return false;
		}
	}

	private void registerSelfAsSensorListener(final SensorManager pSensorManager, final int pType) {
		final Sensor accelerometer = pSensorManager.getSensorList(pType).get(0);
		pSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
	}

	private boolean isSensorSupported(final SensorManager pSensorManager, final int pType) {
		return pSensorManager.getSensorList(pType).size() > 0;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class State {
		private boolean mDrawing = false;

		public synchronized void notifyCanDraw() {
			//			Debug.d(">>> notifyCanDraw");
			this.mDrawing = true;
			this.notifyAll();
			//			Debug.d("<<< notifyCanDraw");
		}

		public synchronized void notifyCanUpdate() {
			//			Debug.d(">>> notifyCanUpdate");
			this.mDrawing = false;
			this.notifyAll();
			//			Debug.d("<<< notifyCanUpdate");
		}

		public synchronized void waitUntilCanDraw() {
			//			Debug.d(">>> waitUntilCanDraw");
			while (this.mDrawing == false) {
				try {
					this.wait();
				} catch (final InterruptedException e) { }
			}
			//			Debug.d("<<< waitUntilCanDraw");
		}

		public synchronized void waitUntilCanUpdate() {
			//			Debug.d(">>> waitUntilCanUpdate");
			while (this.mDrawing == true) {
				try {
					this.wait();
				} catch (final InterruptedException e) { }
			}
			//			Debug.d("<<< waitUntilCanUpdate");
		}		
	}
}
