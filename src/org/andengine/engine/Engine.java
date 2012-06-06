package org.andengine.engine;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.andengine.BuildConfig;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.DrawHandlerList;
import org.andengine.engine.handler.IDrawHandler;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.UpdateHandlerList;
import org.andengine.engine.handler.runnable.RunnableHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.input.sensor.SensorDelay;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.AccelerationSensorOptions;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.sensor.location.ILocationListener;
import org.andengine.input.sensor.location.LocationProviderStatus;
import org.andengine.input.sensor.location.LocationSensorOptions;
import org.andengine.input.sensor.orientation.IOrientationListener;
import org.andengine.input.sensor.orientation.OrientationData;
import org.andengine.input.sensor.orientation.OrientationSensorOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.ITouchController;
import org.andengine.input.touch.controller.ITouchEventCallback;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.input.touch.controller.SingleTouchController;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.shader.ShaderProgramManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;
import org.andengine.util.time.TimeConstants;

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
 * The heart of AndEngine. This class holds all the options and functions that
 * make a game work. The Engine makes the game proceed in small discrete steps
 * of time. The Engine manages to synchronize a periodic drawing and updating of
 * the {@link Scene}, which contains all the content that your game is currently
 * handling actively. There usually is one Scene per Engine, except for the
 * SplitScreenEngines. <br>
 * (c) 2010 Nicolas Gramlich <br>
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:21:31 - 08.03.2010
 */
public class Engine implements SensorEventListener, OnTouchListener,
		ITouchEventCallback, LocationListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final SensorDelay SENSORDELAY_DEFAULT = SensorDelay.GAME;
	private static final int UPDATEHANDLERS_CAPACITY_DEFAULT = 8;
	private static final int DRAWHANDLERS_CAPACITY_DEFAULT = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mRunning;
	private boolean mDestroyed;

	private long mLastTick;
	private float mSecondsElapsedTotal;

	private final EngineLock mEngineLock;

	private final UpdateThread mUpdateThread;
	private final RunnableHandler mUpdateThreadRunnableHandler = new RunnableHandler();

	private final EngineOptions mEngineOptions;
	protected final Camera mCamera;

	private ITouchController mTouchController;

	private final VertexBufferObjectManager mVertexBufferObjectManager = new VertexBufferObjectManager();
	private final TextureManager mTextureManager = new TextureManager();
	private final FontManager mFontManager = new FontManager();
	private final ShaderProgramManager mShaderProgramManager = new ShaderProgramManager();

	private final SoundManager mSoundManager;
	private final MusicManager mMusicManager;

	protected Scene mScene;

	private Vibrator mVibrator;

	private ILocationListener mLocationListener;
	private Location mLocation;

	private IAccelerationListener mAccelerationListener;
	private AccelerationData mAccelerationData;

	private IOrientationListener mOrientationListener;
	private OrientationData mOrientationData;

	private final UpdateHandlerList mUpdateHandlers = new UpdateHandlerList(
			Engine.UPDATEHANDLERS_CAPACITY_DEFAULT);
	private final DrawHandlerList mDrawHandlers = new DrawHandlerList(
			Engine.DRAWHANDLERS_CAPACITY_DEFAULT);

	protected int mSurfaceWidth = 1; // 1 to prevent accidental DIV/0
	protected int mSurfaceHeight = 1; // 1 to prevent accidental DIV/0

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Initializes a new Engine object with contents set according to the given
	 * {@link EngineOptions}
	 * 
	 * @param pEngineOptions settings for Engine object
	 */
	public Engine(final EngineOptions pEngineOptions) {
		/* Initialize Factory and Manager classes. */
		BitmapTextureAtlasTextureRegionFactory.reset();
		SoundFactory.onCreate();
		MusicFactory.onCreate();
		FontFactory.onCreate();
		this.mVertexBufferObjectManager.onCreate();
		this.mTextureManager.onCreate();
		this.mFontManager.onCreate();
		this.mShaderProgramManager.onCreate();

		/* Apply EngineOptions. */
		this.mEngineOptions = pEngineOptions;
		if (this.mEngineOptions.hasEngineLock()) {
			this.mEngineLock = pEngineOptions.getEngineLock();
		} else {
			this.mEngineLock = new EngineLock(false);
		}
		this.mCamera = pEngineOptions.getCamera();

		/* Touch. */
		if (this.mEngineOptions.getTouchOptions().needsMultiTouch()) {
			this.setTouchController(new MultiTouchController());
		} else {
			this.setTouchController(new SingleTouchController());
		}

		/* Audio. */
		if (this.mEngineOptions.getAudioOptions().needsSound()) {
			this.mSoundManager = new SoundManager(this.mEngineOptions
					.getAudioOptions().getSoundOptions()
					.getMaxSimultaneousStreams());
		} else {
			this.mSoundManager = null;
		}
		if (this.mEngineOptions.getAudioOptions().needsMusic()) {
			this.mMusicManager = new MusicManager();
		} else {
			this.mMusicManager = null;
		}

		/* Start the UpdateThread. */
		if (this.mEngineOptions.hasUpdateThread()) {
			this.mUpdateThread = this.mEngineOptions.getUpdateThread();
		} else {
			this.mUpdateThread = new UpdateThread();
		}
		this.mUpdateThread.setEngine(this);
	}

	/**
	 * Starts the {@link UpdateThread}
	 * 
	 * @throws IllegalThreadStateException
	 */
	public void startUpdateThread() throws IllegalThreadStateException {
		this.mUpdateThread.start();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Returns whether the Engine is currently running
	 * 
	 * @return Engine is running
	 */
	public synchronized boolean isRunning() {
		return this.mRunning;
	}

	/**
	 * Start calling the {@link #onUpdate(long)} method when the engine ticks:
	 * starts the game.
	 */
	public synchronized void start() {
		if (!this.mRunning) {
			this.mLastTick = System.nanoTime();
			this.mRunning = true;
		}
	}

	/**
	 * Stops calling the {@link #onUpdate(long)} method when the engine ticks:
	 * freezes the game.
	 */
	public synchronized void stop() {
		if (this.mRunning) {
			this.mRunning = false;
		}
	}

	/**
	 * The {@link EngineLock} can be used to {@link EngineLock#lock()}/
	 * {@link EngineLock#unlock()} on, to ensure the code in between runs
	 * mutually exclusive to the {@link UpdateThread} and the GL{@link Thread}.
	 * When the caller already is on the {@link UpdateThread} or the GL-
	 * {@link Thread}, that code is executed immediately.
	 * 
	 * @return the {@link EngineLock} the {@link Engine} locks on to ensure
	 *         mutually exclusivity to the {@link UpdateThread} and the GL
	 *         {@link Thread}.
	 */
	public EngineLock getEngineLock() {
		return this.mEngineLock;
	}

	/**
	 * @return The {@link Scene} that is associated with this Engine
	 */
	public Scene getScene() {
		return this.mScene;
	}

	/**
	 * @param pScene
	 *            The {@link Scene} that is associated with this Engine
	 */
	public void setScene(final Scene pScene) {
		this.mScene = pScene;
	}

	/**
	 * 
	 * @return the {@link EngineOptions} that are associated with this Engine
	 */
	public EngineOptions getEngineOptions() {
		return this.mEngineOptions;
	}

	/**
	 * 
	 * @return the {@link Camera} that is associated with this Engine
	 */
	public Camera getCamera() {
		return this.mCamera;
	}

	/**
	 * 
	 * @return the number of seconds that has elapsed since starting this Engine
	 */
	public float getSecondsElapsedTotal() {
		return this.mSecondsElapsedTotal;
	}

	/**
	 * Sets the surface dimensions of the Engine's {@link Camera}
	 * @param pSurfaceWidth new width
	 * @param pSurfaceHeight new height
	 */
	public void setSurfaceSize(final int pSurfaceWidth, final int pSurfaceHeight) {
		this.mSurfaceWidth = pSurfaceWidth;
		this.mSurfaceHeight = pSurfaceHeight;
		this.onUpdateCameraSurface();
	}

	/**
	 * Sets the surface size of the Engine's {@link Camera} to that of the
	 * Engine
	 */
	protected void onUpdateCameraSurface() {
		this.mCamera.setSurfaceSize(0, 0, this.mSurfaceWidth,
				this.mSurfaceHeight);
	}

	/**
	 * Gets the Engine's surface width
	 * @return surface width
	 */
	public int getSurfaceWidth() {
		return this.mSurfaceWidth;
	}

	/**
	 * Gets the Engine's surface height
	 * @return surface height
	 */
	public int getSurfaceHeight() {
		return this.mSurfaceHeight;
	}

	/**
	 * Retrieves the Engine's {@link ITouchController}
	 * @return Engine's {@link ITouchController}
	 */
	public ITouchController getTouchController() {
		return this.mTouchController;
	}

	/**
	 * Sets the Engine's {@link ITouchController} to the given
	 * {@link ITouchController}
	 * @param pTouchController given {@link ITouchController}
	 */
	public void setTouchController(final ITouchController pTouchController) {
		this.mTouchController = pTouchController;
		this.mTouchController.setTouchEventCallback(this);
	}

	/**
	 * Gets the current {@link AccelerationData}
	 * @return Engine's {@link AccelerationData}
	 */
	public AccelerationData getAccelerationData() {
		return this.mAccelerationData;
	}

	/**
	 * Gets the current {@link OrientationData}
	 * @return Engine's {@link OrientationData}
	 */
	public OrientationData getOrientationData() {
		return this.mOrientationData;
	}

	/**
	 * Gets the Engine's {@link VertexBufferObjectManager}
	 * @return Engine's {@link VertexBufferObjectManager}
	 */
	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return this.mVertexBufferObjectManager;
	}

	/**
	 * Gets the Engine's {@link TextureManager}
	 * @return Engine's {@link TextureManager}
	 */
	public TextureManager getTextureManager() {
		return this.mTextureManager;
	}

	/**
	 * Gets the Engine's {@link FontManager}
	 * @return Engine's {@link FontManager}
	 */
	public FontManager getFontManager() {
		return this.mFontManager;
	}

	/**
	 * Gets the Engine's {@link ShaderProgramManager}
	 * @return Engine's {@link ShaderProgramManager}
	 */
	public ShaderProgramManager getShaderProgramManager() {
		return this.mShaderProgramManager;
	}

	/**
	 * Gets the Engine's {@link SoundManager}.  If a {@link SoundManager} has
	 * not been set through the {@link EngineOptions} and thus does not exist,
	 * getSoundManager() will throw an {@link IllegalStateException}.
	 * 
	 * @return Engine's {@link SoundManager}
	 * @throws IllegalStateException Engine's {@link SoundManager} is 
	 * 								 {@code null}
	 */
	public SoundManager getSoundManager() throws IllegalStateException {
		if (this.mSoundManager != null) {
			return this.mSoundManager;
		} else {
			throw new IllegalStateException(
					"To enable the SoundManager, check the EngineOptions!");
		}
	}

	/**
	 * Gets the Engine's {@link MusicManager}.  If a {@link MusicManager} has
	 * not been set through the {@link EngineOptions} and thus does not exist,
	 * getMusicManager() will throw an {@link IllegalStateException}.
	 * 
	 * @return Engine's {@link MusicManager}
	 * @throws IllegalStateException Engine's {@link MusicManager} is
	 * 								 {@code null} 
	 */
	public MusicManager getMusicManager() throws IllegalStateException {
		if (this.mMusicManager != null) {
			return this.mMusicManager;
		} else {
			throw new IllegalStateException(
					"To enable the MusicManager, check the EngineOptions!");
		}
	}

	/**
	 * Registers a given {@link IUpdateHandler} with the Engine.  This will not
	 * affect any other registered {@link IUpdateHandler} objects.
	 * 
	 * @param pUpdateHandler new {@link IUpdateHandler}
	 */
	public void registerUpdateHandler(final IUpdateHandler pUpdateHandler) {
		this.mUpdateHandlers.add(pUpdateHandler);
	}

	/**
	 * Unregisters a given {@link IUpdateHandler}.  If the
	 * {@link IUpdateHandler} does not exist within the Engine, this will do
	 * nothing.
	 * 
	 * @param pUpdateHandler {@link IUpdateHandler} to remove
	 */
	public void unregisterUpdateHandler(final IUpdateHandler pUpdateHandler) {
		this.mUpdateHandlers.remove(pUpdateHandler);
	}

	/**
	 * Removes all links to {@link IUpdateHandler} objects tracked in the Engine
	 */
	public void clearUpdateHandlers() {
		this.mUpdateHandlers.clear();
	}

	/**
	 * Registers a given {@link IDrawHandler} with the Engine.  This will not
	 * affect any other registered {@link IDrawHandler} objects
	 * 
	 * @param pDrawHandler new {@link IDrawHandler}
	 */
	public void registerDrawHandler(final IDrawHandler pDrawHandler) {
		this.mDrawHandlers.add(pDrawHandler);
	}

	/**
	 * Unregisters a given {@link IDrawHandler}.  If the {@link IDrawHandler}
	 * does not exist within the Engine, this will do nothing.
	 * 
	 * @param pDrawHandler {@link IDrawHandler} to remove
	 */
	public void unregisterDrawHandler(final IDrawHandler pDrawHandler) {
		this.mDrawHandlers.remove(pDrawHandler);
	}

	/**
	 * Removes all links to {@link IDrawHandler} objects tracked in the Engine
	 */
	public void clearDrawHandlers() {
		this.mDrawHandlers.clear();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onAccuracyChanged(final Sensor pSensor, final int pAccuracy) {
		if (this.mRunning) {
			switch (pSensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				if (this.mAccelerationData != null) {
					this.mAccelerationData.setAccuracy(pAccuracy);
					this.mAccelerationListener
							.onAccelerationAccuracyChanged(this.mAccelerationData);
				} else if (this.mOrientationData != null) {
					this.mOrientationData.setAccelerationAccuracy(pAccuracy);
					this.mOrientationListener
							.onOrientationAccuracyChanged(this.mOrientationData);
				}
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				this.mOrientationData.setMagneticFieldAccuracy(pAccuracy);
				this.mOrientationListener
						.onOrientationAccuracyChanged(this.mOrientationData);
				break;
			}
		}
	}

	@Override
	public void onSensorChanged(final SensorEvent pEvent) {
		if (this.mRunning) {
			switch (pEvent.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				if (this.mAccelerationData != null) {
					this.mAccelerationData.setValues(pEvent.values);
					this.mAccelerationListener
							.onAccelerationChanged(this.mAccelerationData);
				} else if (this.mOrientationData != null) {
					this.mOrientationData.setAccelerationValues(pEvent.values);
					this.mOrientationListener
							.onOrientationChanged(this.mOrientationData);
				}
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				this.mOrientationData.setMagneticFieldValues(pEvent.values);
				this.mOrientationListener
						.onOrientationChanged(this.mOrientationData);
				break;
			}
		}
	}

	@Override
	public void onLocationChanged(final Location pLocation) {
		if (this.mLocation == null) {
			this.mLocation = pLocation;
		} else {
			if (pLocation == null) {
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
	public void onStatusChanged(final String pProvider, final int pStatus,
			final Bundle pExtras) {
		switch (pStatus) {
		case LocationProvider.AVAILABLE:
			this.mLocationListener.onLocationProviderStatusChanged(
					LocationProviderStatus.AVAILABLE, pExtras);
			break;
		case LocationProvider.OUT_OF_SERVICE:
			this.mLocationListener.onLocationProviderStatusChanged(
					LocationProviderStatus.OUT_OF_SERVICE, pExtras);
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			this.mLocationListener.onLocationProviderStatusChanged(
					LocationProviderStatus.TEMPORARILY_UNAVAILABLE, pExtras);
			break;
		}
	}

	@Override
	public boolean onTouch(final View pView,
			final MotionEvent pSurfaceMotionEvent) {
		if (this.mRunning) {
			this.mTouchController.onHandleMotionEvent(pSurfaceMotionEvent);
			try {
				/*
				 * Because a human cannot interact 1000x per second, we pause
				 * the UI-Thread for a little.
				 */
				Thread.sleep(this.mEngineOptions.getTouchOptions()
						.getTouchEventIntervalMilliseconds());
			} catch (final InterruptedException e) {
				Debug.e(e);
			}
			return true;
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
		final Scene scene = this
				.getSceneFromSurfaceTouchEvent(pSurfaceTouchEvent);
		final Camera camera = this
				.getCameraFromSurfaceTouchEvent(pSurfaceTouchEvent);

		this.convertSurfaceToSceneTouchEvent(camera, pSurfaceTouchEvent);

		if (this.onTouchHUD(camera, pSurfaceTouchEvent)) {
			return true;
		} else {
			/* If HUD didn't handle it, Scene may handle it. */
			return this.onTouchScene(scene, pSurfaceTouchEvent);
		}
	}

	protected boolean onTouchHUD(final Camera pCamera,
			final TouchEvent pSceneTouchEvent) {
		if (pCamera.hasHUD()) {
			return pCamera.getHUD().onSceneTouchEvent(pSceneTouchEvent);
		} else {
			return false;
		}
	}

	protected boolean onTouchScene(final Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		if (pScene != null) {
			return pScene.onSceneTouchEvent(pSceneTouchEvent);
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void runOnUpdateThread(final Runnable pRunnable) {
		this.runOnUpdateThread(pRunnable, true);
	}

	/**
	 * This method is useful when you want to execute code on the
	 * {@link UpdateThread}, even though the Engine is paused.
	 * 
	 * @param pRunnable
	 *            the {@link Runnable} to be run on the {@link UpdateThread}.
	 * @param pOnlyWhenEngineRunning
	 *            if <code>true</code>, the execution of the {@link Runnable}
	 *            will be delayed until the next time
	 *            {@link Engine#onUpdateUpdateHandlers(float)} is picked up,
	 *            which is when {@link Engine#isRunning()} is <code>true</code>.
	 *            if <code>false</code>, the execution of the {@link Runnable}
	 *            will happen as soon as possible on the {@link UpdateThread},
	 *            no matter what {@link Engine#isRunning()} is.
	 */
	public void runOnUpdateThread(final Runnable pRunnable,
			final boolean pOnlyWhenEngineRunning) {
		if (pOnlyWhenEngineRunning) {
			this.mUpdateThreadRunnableHandler.postRunnable(pRunnable);
		} else {
			this.mUpdateThread.postRunnable(pRunnable);
		}
	}

	/**
	 * @param pRunnable
	 *            the {@link Runnable} to run mutually exclusive to the
	 *            {@link UpdateThread} and the GL-{@link Thread}. When the
	 *            caller already is on the {@link UpdateThread} or the GL-
	 *            {@link Thread}, the {@link Runnable} is executed immediately.
	 * @see {@link Engine#getEngineLock()} to manually {@link EngineLock#lock()}
	 *      /{@link EngineLock#unlock()} on, while avoiding creating a
	 *      {@link Runnable}.
	 */
	public void runSafely(final Runnable pRunnable) {
		this.mEngineLock.lock();
		try {
			pRunnable.run();
		} finally {
			this.mEngineLock.unlock();
		}
	}

	public void onDestroy() {
		this.mEngineLock.lock();
		try {
			this.mDestroyed = true;
			this.mEngineLock.notifyCanUpdate();
		} finally {
			this.mEngineLock.unlock();
		}
		try {
			this.mUpdateThread.join();
		} catch (final InterruptedException e) {
			Debug.e("Could not join UpdateThread.", e);
			Debug.w("Trying to manually interrupt UpdateThread.");
			this.mUpdateThread.interrupt();
		}

		this.mVertexBufferObjectManager.onDestroy();
		this.mTextureManager.onDestroy();
		this.mFontManager.onDestroy();
		this.mShaderProgramManager.onDestroy();
	}

	public void onReloadResources() {
		this.mVertexBufferObjectManager.onReload();
		this.mTextureManager.onReload();
		this.mFontManager.onReload();
		this.mShaderProgramManager.onReload();
	}

	protected Camera getCameraFromSurfaceTouchEvent(final TouchEvent pTouchEvent) {
		return this.getCamera();
	}

	protected Scene getSceneFromSurfaceTouchEvent(final TouchEvent pTouchEvent) {
		return this.mScene;
	}

	protected void convertSurfaceToSceneTouchEvent(final Camera pCamera,
			final TouchEvent pSurfaceTouchEvent) {
		pCamera.convertSurfaceToSceneTouchEvent(pSurfaceTouchEvent,
				this.mSurfaceWidth, this.mSurfaceHeight);
	}

	protected void convertSceneToSurfaceTouchEvent(final Camera pCamera,
			final TouchEvent pSurfaceTouchEvent) {
		pCamera.convertSceneToSurfaceTouchEvent(pSurfaceTouchEvent,
				this.mSurfaceWidth, this.mSurfaceHeight);
	}

	void onTickUpdate() throws InterruptedException {
		if (this.mRunning) {
			final long secondsElapsed = this.getNanosecondsElapsed();

			this.mEngineLock.lock();
			try {
				this.throwOnDestroyed();

				this.onUpdate(secondsElapsed);

				this.throwOnDestroyed();

				this.mEngineLock.notifyCanDraw();
				this.mEngineLock.waitUntilCanUpdate();
			} finally {
				this.mEngineLock.unlock();
			}
		} else {
			this.mEngineLock.lock();
			try {
				this.throwOnDestroyed();

				this.mEngineLock.notifyCanDraw();
				this.mEngineLock.waitUntilCanUpdate();
			} finally {
				this.mEngineLock.unlock();
			}

			Thread.sleep(16);
		}
	}

	private void throwOnDestroyed() throws EngineDestroyedException {
		if (this.mDestroyed) {
			throw new EngineDestroyedException();
		}
	}

	public void onUpdate(final long pNanosecondsElapsed)
			throws InterruptedException {
		final float pSecondsElapsed = pNanosecondsElapsed
				* TimeConstants.SECONDS_PER_NANOSECOND;

		this.mSecondsElapsedTotal += pSecondsElapsed;
		this.mLastTick += pNanosecondsElapsed;

		this.mTouchController.onUpdate(pSecondsElapsed);
		this.onUpdateUpdateHandlers(pSecondsElapsed);
		this.onUpdateScene(pSecondsElapsed);
	}

	protected void onUpdateScene(final float pSecondsElapsed) {
		if (this.mScene != null) {
			this.mScene.onUpdate(pSecondsElapsed);
		}
	}

	protected void onUpdateUpdateHandlers(final float pSecondsElapsed) {
		this.mUpdateThreadRunnableHandler.onUpdate(pSecondsElapsed);
		this.mUpdateHandlers.onUpdate(pSecondsElapsed);
		this.getCamera().onUpdate(pSecondsElapsed);
	}

	protected void onUpdateDrawHandlers(final GLState pGLState,
			final Camera pCamera) {
		this.mDrawHandlers.onDraw(pGLState, pCamera);
	}

	public void onDrawFrame(final GLState pGLState) throws InterruptedException {
		final EngineLock engineLock = this.mEngineLock;

		engineLock.lock();
		try {
			engineLock.waitUntilCanDraw();

			this.mVertexBufferObjectManager.updateVertexBufferObjects(pGLState);
			this.mTextureManager.updateTextures(pGLState);
			this.mFontManager.updateFonts(pGLState);

			this.onUpdateDrawHandlers(pGLState, this.mCamera);
			this.onDrawScene(pGLState, this.mCamera);

			engineLock.notifyCanUpdate();
		} finally {
			engineLock.unlock();
		}
	}

	protected void onDrawScene(final GLState pGLState, final Camera pCamera) {
		if (this.mScene != null) {
			this.mScene.onDraw(pGLState, pCamera);
		}

		pCamera.onDrawHUD(pGLState);
	}

	private long getNanosecondsElapsed() {
		final long now = System.nanoTime();

		return now - this.mLastTick;
	}

	public boolean enableVibrator(final Context pContext) {
		this.mVibrator = (Vibrator) pContext
				.getSystemService(Context.VIBRATOR_SERVICE);
		return this.mVibrator != null;
	}

	public void vibrate(final long pMilliseconds) throws IllegalStateException {
		if (this.mVibrator != null) {
			this.mVibrator.vibrate(pMilliseconds);
		} else {
			throw new IllegalStateException(
					"You need to enable the Vibrator before you can use it!");
		}
	}

	public void vibrate(final long[] pPattern, final int pRepeat)
			throws IllegalStateException {
		if (this.mVibrator != null) {
			this.mVibrator.vibrate(pPattern, pRepeat);
		} else {
			throw new IllegalStateException(
					"You need to enable the Vibrator before you can use it!");
		}
	}

	public void enableLocationSensor(final Context pContext,
			final ILocationListener pLocationListener,
			final LocationSensorOptions pLocationSensorOptions) {
		this.mLocationListener = pLocationListener;

		final LocationManager locationManager = (LocationManager) pContext
				.getSystemService(Context.LOCATION_SERVICE);
		final String locationProvider = locationManager.getBestProvider(
				pLocationSensorOptions, pLocationSensorOptions.isEnabledOnly());
		// TODO locationProvider can be null, in that case return false.
		// Successful case should return true.
		locationManager.requestLocationUpdates(locationProvider,
				pLocationSensorOptions.getMinimumTriggerTime(),
				pLocationSensorOptions.getMinimumTriggerDistance(), this);

		this.onLocationChanged(locationManager
				.getLastKnownLocation(locationProvider));
	}

	public void disableLocationSensor(final Context pContext) {
		final LocationManager locationManager = (LocationManager) pContext
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);
	}

	/**
	 * @see {@link Engine#enableAccelerationSensor(Context, IAccelerationListener, AccelerationSensorOptions)}
	 */
	public boolean enableAccelerationSensor(final Context pContext,
			final IAccelerationListener pAccelerationListener) {
		return this.enableAccelerationSensor(pContext, pAccelerationListener,
				new AccelerationSensorOptions(Engine.SENSORDELAY_DEFAULT));
	}

	/**
	 * @return <code>true</code> when the sensor was successfully enabled,
	 *         <code>false</code> otherwise.
	 */
	public boolean enableAccelerationSensor(final Context pContext,
			final IAccelerationListener pAccelerationListener,
			final AccelerationSensorOptions pAccelerationSensorOptions) {
		final SensorManager sensorManager = (SensorManager) pContext
				.getSystemService(Context.SENSOR_SERVICE);
		if (this.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER)) {
			this.mAccelerationListener = pAccelerationListener;

			if (this.mAccelerationData == null) {
				final Display display = ((WindowManager) pContext
						.getSystemService(Context.WINDOW_SERVICE))
						.getDefaultDisplay();
				final int displayRotation = display.getOrientation();
				this.mAccelerationData = new AccelerationData(displayRotation);
			}

			this.registerSelfAsSensorListener(sensorManager,
					Sensor.TYPE_ACCELEROMETER,
					pAccelerationSensorOptions.getSensorDelay());

			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return <code>true</code> when the sensor was successfully disabled,
	 *         <code>false</code> otherwise.
	 */
	public boolean disableAccelerationSensor(final Context pContext) {
		final SensorManager sensorManager = (SensorManager) pContext
				.getSystemService(Context.SENSOR_SERVICE);
		if (this.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER)) {
			this.unregisterSelfAsSensorListener(sensorManager,
					Sensor.TYPE_ACCELEROMETER);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see {@link Engine#enableOrientationSensor(Context, IOrientationListener, OrientationSensorOptions)}
	 */
	public boolean enableOrientationSensor(final Context pContext,
			final IOrientationListener pOrientationListener) {
		return this.enableOrientationSensor(pContext, pOrientationListener,
				new OrientationSensorOptions(Engine.SENSORDELAY_DEFAULT));
	}

	/**
	 * @return <code>true</code> when the sensor was successfully enabled,
	 *         <code>false</code> otherwise.
	 */
	public boolean enableOrientationSensor(final Context pContext,
			final IOrientationListener pOrientationListener,
			final OrientationSensorOptions pOrientationSensorOptions) {
		final SensorManager sensorManager = (SensorManager) pContext
				.getSystemService(Context.SENSOR_SERVICE);
		if (this.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER)
				&& this.isSensorSupported(sensorManager,
						Sensor.TYPE_MAGNETIC_FIELD)) {
			this.mOrientationListener = pOrientationListener;

			if (this.mOrientationData == null) {
				final Display display = ((WindowManager) pContext
						.getSystemService(Context.WINDOW_SERVICE))
						.getDefaultDisplay();
				final int displayRotation = display.getOrientation();
				this.mOrientationData = new OrientationData(displayRotation);
			}

			this.registerSelfAsSensorListener(sensorManager,
					Sensor.TYPE_ACCELEROMETER,
					pOrientationSensorOptions.getSensorDelay());
			this.registerSelfAsSensorListener(sensorManager,
					Sensor.TYPE_MAGNETIC_FIELD,
					pOrientationSensorOptions.getSensorDelay());

			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return <code>true</code> when the sensor was successfully disabled,
	 *         <code>false</code> otherwise.
	 */
	public boolean disableOrientationSensor(final Context pContext) {
		final SensorManager sensorManager = (SensorManager) pContext
				.getSystemService(Context.SENSOR_SERVICE);
		if (this.isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER)
				&& this.isSensorSupported(sensorManager,
						Sensor.TYPE_MAGNETIC_FIELD)) {
			this.unregisterSelfAsSensorListener(sensorManager,
					Sensor.TYPE_ACCELEROMETER);
			this.unregisterSelfAsSensorListener(sensorManager,
					Sensor.TYPE_MAGNETIC_FIELD);
			return true;
		} else {
			return false;
		}
	}

	private boolean isSensorSupported(final SensorManager pSensorManager,
			final int pType) {
		return pSensorManager.getSensorList(pType).size() > 0;
	}

	private void registerSelfAsSensorListener(
			final SensorManager pSensorManager, final int pType,
			final SensorDelay pSensorDelay) {
		final Sensor sensor = pSensorManager.getSensorList(pType).get(0);
		pSensorManager.registerListener(this, sensor, pSensorDelay.getDelay());
	}

	private void unregisterSelfAsSensorListener(
			final SensorManager pSensorManager, final int pType) {
		final Sensor sensor = pSensorManager.getSensorList(pType).get(0);
		pSensorManager.unregisterListener(this, sensor);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class UpdateThread extends Thread {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private Engine mEngine;
		private final RunnableHandler mRunnableHandler = new RunnableHandler();

		// ===========================================================
		// Constructors
		// ===========================================================

		public UpdateThread() {
			super(UpdateThread.class.getSimpleName());
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public void setEngine(final Engine pEngine) {
			this.mEngine = pEngine;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void run() {
			android.os.Process.setThreadPriority(this.mEngine
					.getEngineOptions().getUpdateThreadPriority());
			try {
				while (true) {
					this.mRunnableHandler.onUpdate(0);
					this.mEngine.onTickUpdate();
				}
			} catch (final InterruptedException e) {
				if (BuildConfig.DEBUG) {
					Debug.d(this.getClass().getSimpleName()
							+ " interrupted. Don't worry - this "
							+ e.getClass().getSimpleName()
							+ " is most likely expected!", e);
				}
				this.interrupt();
			}
		}

		// ===========================================================
		// Methods
		// ===========================================================

		public void postRunnable(final Runnable pRunnable) {
			this.mRunnableHandler.postRunnable(pRunnable);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public class EngineDestroyedException extends InterruptedException {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = -4691263961728972560L;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	/**
	 * The {@link EngineLock EngineLock} can be used to lock on, to ensure the
	 * code in between runs mutually exclusive to the {@link UpdateThread} and
	 * the GL{@link Thread}. When the caller already is on the
	 * {@link UpdateThread} or the GL-{@link Thread}, that code is executed
	 * immediately.
	 **/
	public static class EngineLock extends ReentrantLock {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = 671220941302523934L;

		// ===========================================================
		// Fields
		// ===========================================================

		final Condition mDrawingCondition = this.newCondition();
		final AtomicBoolean mDrawing = new AtomicBoolean(false);

		// ===========================================================
		// Constructors
		// ===========================================================

		public EngineLock(final boolean pFair) {
			super(pFair);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		void notifyCanDraw() {
			this.mDrawing.set(true);
			this.mDrawingCondition.signalAll();
		}

		void notifyCanUpdate() {
			this.mDrawing.set(false);
			this.mDrawingCondition.signalAll();
		}

		void waitUntilCanDraw() throws InterruptedException {
			while (!this.mDrawing.get()) {
				this.mDrawingCondition.await();
			}
		}

		void waitUntilCanUpdate() throws InterruptedException {
			while (this.mDrawing.get()) {
				this.mDrawingCondition.await();
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
