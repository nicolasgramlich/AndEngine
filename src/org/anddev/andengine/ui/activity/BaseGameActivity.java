package org.anddev.andengine.ui.activity;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.EngineOptions;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.sensor.accelerometer.AccelerometerListener;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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

	private void applyEngineOptions(EngineOptions pEngineOptions) {
		if(pEngineOptions.mFullscreen) {
			applyFullscreen();
		}

		switch(pEngineOptions.mScreenOrientation){
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
