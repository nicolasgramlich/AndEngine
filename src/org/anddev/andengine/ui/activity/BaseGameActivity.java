package org.anddev.andengine.ui.activity;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.EngineOptions;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.sensor.accelerometer.AccelerometerListener;

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
public abstract class BaseGameActivity extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Engine mEngine;
	private WakeLock mWakeLock;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);

		this.mEngine = this.onLoadEngine();
		applyEngineOptions(this.mEngine.getEngineOptions());

		this.setContentView(new RenderSurfaceView(this, this.mEngine));

		this.onLoadResources();
		this.mEngine.setScene(this.onLoadScene());
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract Scene onLoadScene();

	protected abstract void onLoadResources();

	protected abstract void onLoadComplete();

	protected abstract Engine onLoadEngine();

	// ===========================================================
	// Methods
	// ===========================================================

	private void acquireWakeLock() {
		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "AndEngine");
		this.mWakeLock.acquire();
	}
	
	private void releaseWakeLock() {
		if(this.mWakeLock != null)
			this.mWakeLock.release();
	}

	private void applyEngineOptions(EngineOptions pEngineOptions) {
		if(pEngineOptions.isFullscreen()) {
			applyFullscreen();
		}

		switch(pEngineOptions.getScreenOrientation()){
			case LANDSCAPE:
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				break;
			case PORTRAIT:
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;
		}
	}

	private void applyFullscreen() {
		final Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.requestFeature(Window.FEATURE_NO_TITLE);
	}
	
	protected void enableAccelerometer(final AccelerometerListener pAccelerometerListener) {
		this.mEngine.enableAccelerometer(this, pAccelerometerListener);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
