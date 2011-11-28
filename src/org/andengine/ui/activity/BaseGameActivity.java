package org.andengine.ui.activity;

import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.SoundManager;
import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.opengl.view.RenderSurfaceView.IRendererListener;
import org.andengine.sensor.accelerometer.AccelerometerSensorOptions;
import org.andengine.sensor.accelerometer.IAccelerometerListener;
import org.andengine.sensor.location.ILocationListener;
import org.andengine.sensor.location.LocationSensorOptions;
import org.andengine.sensor.orientation.IOrientationListener;
import org.andengine.sensor.orientation.OrientationSensorOptions;
import org.andengine.ui.IGameInterface;
import org.andengine.util.ActivityUtils;
import org.andengine.util.constants.Constants;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Gravity;
import android.widget.FrameLayout.LayoutParams;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:27:06 - 08.03.2010
 */
public abstract class BaseGameActivity extends BaseActivity implements IGameInterface, IRendererListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected Engine mEngine;
	private WakeLock mWakeLock;
	protected RenderSurfaceView mRenderSurfaceView;
	private boolean mGamePaused;
	private boolean mGameCreated;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);

		this.mGamePaused = true;

		this.mEngine = this.onCreateEngine(this.onCreateEngineOptions());

		this.applyEngineOptions(this.mEngine.getEngineOptions());

		this.onSetContentView();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if(!this.mGamePaused) {
			this.onPauseGame();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		this.onDestroyGame();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Engine getEngine() {
		return this.mEngine;
	}

	public boolean isGamePaused() {
		return this.mGamePaused;
	}

	public boolean isGameRunning() {
		return !this.mGamePaused;
	}

	public boolean isGameLoaded() {
		return this.mGameCreated;
	}

	public SoundManager getSoundManager() {
		return this.mEngine.getSoundManager();
	}

	public MusicManager getMusicManager() {
		return this.mEngine.getMusicManager();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onSurfaceCreated() {
		Debug.d("Surface created.");

		if(!this.mGameCreated) {
			this.onCreateGame();
		} else {
			this.onReloadResources();
		}

		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				BaseGameActivity.this.onResumeGame();
			}
		});
	}

	@Override
	public void onSurfaceChanged(final int pWidth, final int pHeight) {
		Debug.d("Surface changed: Width=" + pWidth + "  Height=" + pHeight);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public Engine onCreateEngine(final EngineOptions pEngineOptions) {
		return new Engine(pEngineOptions);
	}

	protected void onCreateGame() {
		this.onCreateResources();

		Debug.d("Resources created.");
	
		final Scene scene = this.onCreateScene();
		this.mEngine.onLoadComplete(scene);
	
		this.mGameCreated = true;
		this.onGameCreated();

		Debug.d("Game created.");
	}

	public void onResumeGame() {
		this.mGamePaused = false;

		this.acquireWakeLock();

		BaseGameActivity.this.mRenderSurfaceView.onResume();

		BaseGameActivity.this.mEngine.start();

		Debug.d("Game resumed.");
	}

	protected void onReloadResources() {
		this.mEngine.onReloadResources();

		Debug.d("Resources reloaded.");
	}

	public void onPauseGame() {
		this.mGamePaused = true;
		this.releaseWakeLock();

		this.mEngine.stop();
		this.mRenderSurfaceView.onPause();

		Debug.d("Game paused.");
	}

	protected void onDestroyGame() {
		this.mEngine.onDestroy();

		this.onDestroyResources();

		Debug.d("Game destroyed.");
	}

	public void onDestroyResources() {
		if(this.mEngine.getEngineOptions().getAudioOptions().needsMusic()) {
			this.getMusicManager().releaseAll();
		}
		if(this.mEngine.getEngineOptions().getAudioOptions().needsSound()) {
			this.getSoundManager().releaseAll();
		}

		Debug.d("Resources destroyed.");
	}

	public void runOnUpdateThread(final Runnable pRunnable) {
		this.mEngine.runOnUpdateThread(pRunnable);
	}

	protected void onSetContentView() {
		this.mRenderSurfaceView = new RenderSurfaceView(this);
		this.mRenderSurfaceView.setRenderer(this.mEngine, this);

		this.setContentView(this.mRenderSurfaceView, BaseGameActivity.createSurfaceViewLayoutParams());
	}

	private void acquireWakeLock() {
		this.acquireWakeLock(this.mEngine.getEngineOptions().getWakeLockOptions());
	}

	private void acquireWakeLock(final WakeLockOptions pWakeLockOptions) {
		if(pWakeLockOptions == WakeLockOptions.SCREEN_ON) {
			ActivityUtils.keepScreenOn(this);
		} else {
			final PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
			this.mWakeLock = pm.newWakeLock(pWakeLockOptions.getFlag() | PowerManager.ON_AFTER_RELEASE, Constants.DEBUGTAG);
			try {
				this.mWakeLock.acquire();
			} catch (final SecurityException e) {
				Debug.e("You have to add\n\t<uses-permission android:name=\"android.permission.WAKE_LOCK\"/>\nto your AndroidManifest.xml !", e);
			}
		}
	}

	private void releaseWakeLock() {
		if(this.mWakeLock != null && this.mWakeLock.isHeld()) {
			this.mWakeLock.release();
		}
	}

	private void applyEngineOptions(final EngineOptions pEngineOptions) {
		if(pEngineOptions.isFullscreen()) {
			ActivityUtils.requestFullscreen(this);
		}

		if(pEngineOptions.getAudioOptions().needsMusic() || pEngineOptions.getAudioOptions().needsSound()) {
			this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		}

		switch(pEngineOptions.getScreenOrientation()) {
			case LANDSCAPE:
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				break;
			case PORTRAIT:
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;
		}
	}

	protected static LayoutParams createSurfaceViewLayoutParams() {
		final LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layoutParams.gravity = Gravity.CENTER;
		return layoutParams;
	}

	protected void enableVibrator() {
		this.mEngine.enableVibrator(this);
	}

	/**
	 * @see {@link Engine#enableLocationSensor(Context, ILocationListener, LocationSensorOptions)}
	 */
	protected void enableLocationSensor(final ILocationListener pLocationListener, final LocationSensorOptions pLocationSensorOptions) {
		this.mEngine.enableLocationSensor(this, pLocationListener, pLocationSensorOptions);
	}

	/**
	 * @see {@link Engine#disableLocationSensor(Context)}
	 */
	protected void disableLocationSensor() {
		this.mEngine.disableLocationSensor(this);
	}

	/**
	 * @see {@link Engine#enableAccelerometerSensor(Context, IAccelerometerListener)}
	 */
	protected boolean enableAccelerometerSensor(final IAccelerometerListener pAccelerometerListener) {
		return this.mEngine.enableAccelerometerSensor(this, pAccelerometerListener);
	}

	/**
	 * @see {@link Engine#enableAccelerometerSensor(Context, IAccelerometerListener, AccelerometerSensorOptions)}
	 */
	protected boolean enableAccelerometerSensor(final IAccelerometerListener pAccelerometerListener, final AccelerometerSensorOptions pAccelerometerSensorOptions) {
		return this.mEngine.enableAccelerometerSensor(this, pAccelerometerListener, pAccelerometerSensorOptions);
	}

	/**
	 * @see {@link Engine#disableAccelerometerSensor(Context)}
	 */
	protected boolean disableAccelerometerSensor() {
		return this.mEngine.disableAccelerometerSensor(this);
	}

	/**
	 * @see {@link Engine#enableOrientationSensor(Context, IOrientationListener)}
	 */
	protected boolean enableOrientationSensor(final IOrientationListener pOrientationListener) {
		return this.mEngine.enableOrientationSensor(this, pOrientationListener);
	}

	/**
	 * @see {@link Engine#enableOrientationSensor(Context, IOrientationListener, OrientationSensorOptions)}
	 */
	protected boolean enableOrientationSensor(final IOrientationListener pOrientationListener, final OrientationSensorOptions pLocationSensorOptions) {
		return this.mEngine.enableOrientationSensor(this, pOrientationListener, pLocationSensorOptions);
	}

	/**
	 * @see {@link Engine#disableOrientationSensor(Context)}
	 */
	protected boolean disableOrientationSensor() {
		return this.mEngine.disableOrientationSensor(this);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
