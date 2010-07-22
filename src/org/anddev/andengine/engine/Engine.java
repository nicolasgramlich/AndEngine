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
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.SplashScene;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.controller.ITouchController;
import org.anddev.andengine.input.touch.controller.SingleTouchControler;
import org.anddev.andengine.input.touch.controller.ITouchController.ITouchEventCallback;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.font.FontManager;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureFactory;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.opengl.util.GLHelper;
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
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


/**
 * @author Nicolas Gramlich
 * @since 12:21:31 - 08.03.2010
 */
public class Engine implements SensorEventListener, OnTouchListener, ITouchEventCallback {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float LOADING_SCREEN_DURATION = 2;

	private static final int SENSOR_DELAY_DEFAULT = SensorManager.SENSOR_DELAY_GAME;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mRunning = false;

	private long mLastTick = -1;
	private float mSecondsElapsedTotal = 0;

	private ITouchController mTouchController = new SingleTouchControler();

	private final EngineOptions mEngineOptions;

	private SoundManager mSoundManager;
	private MusicManager mMusicManager;
	private final TextureManager mTextureManager = new TextureManager();
	private final BufferObjectManager mBufferObjectManager = new BufferObjectManager();
	private final FontManager mFontManager = new FontManager();

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

	private final RunnableHandler mUpdateThreadRunnableHandler = new RunnableHandler();

	private final State mThreadLocker = new State();

	private boolean mIsMethodTracing;

	private Vibrator mVibrator;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Engine(final EngineOptions pEngineOptions) {
		TextureRegionFactory.setAssetBasePath("");
		SoundFactory.setAssetBasePath("");
		MusicFactory.setAssetBasePath("");
		FontFactory.setAssetBasePath("");

		BufferObjectManager.setActiveInstance(this.mBufferObjectManager);

		this.mEngineOptions = pEngineOptions;

		if(this.mEngineOptions.needsSound()) {
			this.mSoundManager = new SoundManager();
		}

		if(this.mEngineOptions.needsMusic()) {
			this.mMusicManager = new MusicManager();
		}

		if(this.mEngineOptions.hasLoadingScreen()) {
			this.initLoadingScreen();
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

	public ITouchController getTouchController() {
		return this.mTouchController;
	}

	public void setTouchController(final ITouchController pTouchController) {
		this.mTouchController = pTouchController;
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
			final boolean handled = this.mTouchController.onHandleMotionEvent(pSurfaceMotionEvent, this);
			try {
				/* As a human cannot interact 1000x per second, we pause the UI-Thread for a little.*/
				Thread.sleep(20);
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
		/* Let the engine determine which scene and camera this event should be handled by. */
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

	private void initLoadingScreen() {
		final ITextureSource loadingScreenTextureSource = this.getEngineOptions().getLoadingScreenTextureSource();
		final Texture loadingScreenTexture = TextureFactory.createForTextureSourceSize(loadingScreenTextureSource);
		final TextureRegion loadingScreenTextureRegion = TextureRegionFactory.createFromSource(loadingScreenTexture, loadingScreenTextureSource, 0, 0);
		this.setScene(new SplashScene(this.getCamera(), loadingScreenTextureRegion));
	}

	public void onResume() {
		this.mTextureManager.reloadTextures();
		BufferObjectManager.setActiveInstance(this.mBufferObjectManager);
		this.mBufferObjectManager.reloadBufferObjects();
	}

	public void onPause() {
		this.stop();
	}

	protected Camera getCameraFromSurfaceTouchEvent(final TouchEvent pTouchEvent) {
		return this.getCamera();
	}

	protected Scene getSceneFromSurfaceTouchEvent(final TouchEvent pTouchEvent) {
		return this.mScene;
	}

	protected void convertSurfaceToSceneTouchEvent(final Camera pCamera, final TouchEvent pSurfaceTouchEvent) {
		pCamera.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent, this.mSurfaceWidth, this.mSurfaceHeight);
	}

	public void onLoadComplete(final Scene pScene) {
		// final Scene loadingScene = this.mScene; // TODO Free texture from loading-screen.
		if(this.mEngineOptions.hasLoadingScreen()){
			this.registerPreFrameHandler(new TimerHandler(LOADING_SCREEN_DURATION, new ITimerCallback() {
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {
					Engine.this.unregisterPreFrameHandler(pTimerHandler);
					Engine.this.setScene(pScene);
				}
			}));
		} else {
			this.setScene(pScene);
		}
	}

	protected void onUpdate() {
		if(this.mRunning) {
			final float secondsElapsed = this.getSecondsElapsed();

			this.updatePreFrameHandlers(secondsElapsed);

			if(this.mScene != null){
				this.onUpdateScenePreFrameHandlers(secondsElapsed);

				this.mUpdateThreadRunnableHandler.onUpdate(secondsElapsed);
				this.onUpdateScene(secondsElapsed);

				this.mThreadLocker.notifyCanDraw();
				this.mThreadLocker.waitUntilCanUpdate();

				this.onUpdateScenePostFrameHandlers(secondsElapsed);
			} else {
				this.mThreadLocker.notifyCanDraw();
				this.mThreadLocker.waitUntilCanUpdate();
			}

			this.updatePostFrameHandlers(secondsElapsed);
			//			if(secondsElapsed < 0.033f) {
			//				try {
			//					final int sleepTimeMilliseconds = (int)((0.033f - secondsElapsed) * 1000);
			//					Thread.sleep(sleepTimeMilliseconds);
			//				} catch (InterruptedException e) {
			//					Debug.e("UpdateThread interrupted from sleep.", e);
			//				}
			//			}
		} else {
			this.mThreadLocker.notifyCanDraw();
			this.mThreadLocker.waitUntilCanUpdate();

			try {
				Thread.sleep(16);
			} catch (final InterruptedException e) {
				Debug.e("UpdateThread interrupted from sleep.", e);
			}
		}
	}

	public void onDrawFrame(final GL10 pGL) {
		this.mThreadLocker.waitUntilCanDraw();

		this.mTextureManager.updateTextures(pGL);
		this.mFontManager.updateFonts(pGL);
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			this.mBufferObjectManager.updateBufferObjects((GL11)pGL);
		}

		this.onDrawScene(pGL);

		this.mThreadLocker.notifyCanUpdate();
	}

	protected void onUpdateScene(final float pSecondsElapsed) {
		this.mScene.onUpdate(pSecondsElapsed);
	}

	protected void onUpdateScenePostFrameHandlers(final float pSecondsElapsed) {
		this.mScene.updatePostFrameHandlers(pSecondsElapsed);
	}

	protected void onUpdateScenePreFrameHandlers(final float pSecondsElapsed) {
		this.mScene.updatePreFrameHandlers(pSecondsElapsed);
	}

	protected void updatePreFrameHandlers(final float pSecondsElapsed) {
		this.getCamera().onUpdate(pSecondsElapsed);
		this.mPreFrameHandlers.onUpdate(pSecondsElapsed);
	}

	protected void updatePostFrameHandlers(final float pSecondsElapsed) {
		this.mPostFrameHandlers.onUpdate(pSecondsElapsed);
	}

	protected void onDrawScene(final GL10 pGL) {
		final Camera camera = this.getCamera();

		this.mScene.onDraw(pGL, camera);
		
		camera.onDrawHUD(pGL);
	}

	private float getSecondsElapsed() {
		final long now = System.nanoTime();
		if(this.mLastTick == -1) {
			this.mLastTick = now - TimeConstants.NANOSECONDSPERMILLISECOND;
		}

		final long nanosecondsElapsed = this.calculateNanoSecondsElapsed(now, this.mLastTick);

		final float secondsElapsed = (float)nanosecondsElapsed / TimeConstants.NANOSECONDSPERSECOND;
		this.mLastTick = now;

		this.mSecondsElapsedTotal += secondsElapsed;

		return secondsElapsed;
	}

	protected long calculateNanoSecondsElapsed(final long pNow, final long pLastTick) {
		return pNow - pLastTick;
	}

	public boolean enableVibrator(final Context pContext) {
		this.mVibrator = (Vibrator)pContext.getSystemService(Context.VIBRATOR_SERVICE);
		return this.mVibrator != null;
	}

	public void vibrate(final long pMilliseconds) throws IllegalStateException {
		if(this.mVibrator != null) {
			this.mVibrator.vibrate(pMilliseconds);
		} else {
			throw new IllegalStateException("You need to enable the Vibrator before you can use it!");
		}
	}

	public boolean enableAccelerometerSensor(final Context pContext, final IAccelerometerListener pAccelerometerListener) {
		return this.enableAccelerometerSensor(pContext, pAccelerometerListener, SENSOR_DELAY_DEFAULT);
	}

	public boolean enableAccelerometerSensor(final Context pContext, final IAccelerometerListener pAccelerometerListener, final int pRate) {
		final SensorManager sensorManager = (SensorManager) pContext.getSystemService(Context.SENSOR_SERVICE);
		if (this.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER)) {
			this.registerSelfAsSensorListener(sensorManager, Sensor.TYPE_ACCELEROMETER, pRate);

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
		return this.enableOrientationSensor(pContext, pOrientationListener, SENSOR_DELAY_DEFAULT);
	}

	public boolean enableOrientationSensor(final Context pContext, final IOrientationListener pOrientationListener, final int pRate) {
		final SensorManager sensorManager = (SensorManager) pContext.getSystemService(Context.SENSOR_SERVICE);
		if (this.isSensorSupported(sensorManager, Sensor.TYPE_ORIENTATION)) {
			this.registerSelfAsSensorListener(sensorManager, Sensor.TYPE_ORIENTATION, pRate);

			this.mOrientationListener = pOrientationListener;
			if(this.mOrientationData == null) {
				this.mOrientationData = new OrientationData();
			}

			return true;
		} else {
			return false;
		}
	}

	private void registerSelfAsSensorListener(final SensorManager pSensorManager, final int pType, final int pRate) {
		final Sensor accelerometer = pSensorManager.getSensorList(pType).get(0);
		pSensorManager.registerListener(this, accelerometer, pRate);
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
