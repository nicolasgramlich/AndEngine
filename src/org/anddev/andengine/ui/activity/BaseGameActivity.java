package org.anddev.andengine.ui.activity;

import org.anddev.andengine.audio.sound.SoundManager;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.EngineOptions;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.IGameInterface;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Nicolas Gramlich
 * @since 11:27:06 - 08.03.2010
 */
public abstract class BaseGameActivity extends Activity implements IGameInterface {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Engine mEngine;
	private WakeLock mWakeLock;
	private final SoundManager mSoundManager = new SoundManager();

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);

		this.mEngine = this.onLoadEngine();
		this.applyEngineOptions(this.mEngine.getEngineOptions());

		this.setContentView(new RenderSurfaceView(this, this.mEngine));

		this.onLoadResources();
		final Scene scene = this.onLoadScene();
		this.mEngine.onLoadComplete(scene);
		this.onLoadComplete();
		this.mEngine.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.acquireWakeLock();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.releaseWakeLock();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Engine getEngine() {
		return this.mEngine;
	}

	public SoundManager getSoundManager() {
		return this.mSoundManager;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private void acquireWakeLock() {
		final PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "AndEngine");
		this.mWakeLock.acquire();
	}

	private void releaseWakeLock() {
		if(this.mWakeLock != null) {
			this.mWakeLock.release();
		}
	}

	private void applyEngineOptions(final EngineOptions pEngineOptions) {
		if(pEngineOptions.isFullscreen()) {
			this.applyFullscreen();
		}

		switch(pEngineOptions.getScreenOrientation()){
			case LANDSCAPE:
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				break;
			case PORTRAIT:
				this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;
		}
	}

	private void applyFullscreen() {
		final Window window = this.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.requestFeature(Window.FEATURE_NO_TITLE);
	}

	protected void enableAccelerometer(final IAccelerometerListener pAccelerometerListener) {
		this.mEngine.enableAccelerometer(this, pAccelerometerListener);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
