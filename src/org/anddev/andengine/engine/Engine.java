package org.anddev.andengine.engine;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.AccelerometerListener;
import org.anddev.andengine.util.constants.TimeConstants;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


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

	private final EngineOptions mEngineOptions;
	
	private Scene mScene;
	
	private TextureManager mTextureManager = new TextureManager();

	private long mLastTick = System.nanoTime();

	private AccelerometerListener mAccelerometerListener;

	private AccelerometerData mAccelerometerData;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public Engine(final EngineOptions pEngineOptions) {
		this.mEngineOptions = pEngineOptions;		
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setScene(final Scene pScene) {
		this.mScene = pScene;
	}
	
	public EngineOptions getEngineOptions() {
		return this.mEngineOptions;
	}

	public int getGameWidth() {
		return this.mEngineOptions.mGameWidth;
	}

	public int getGameHeight() {
		return this.mEngineOptions.mGameHeight;
	}	
	
	public AccelerometerData getAccelerometerData() {
		return this.mAccelerometerData;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onAccuracyChanged(final Sensor pSensor, final int pAccuracy) {
		switch(pSensor.getType()){
			case Sensor.TYPE_ACCELEROMETER:
				this.mAccelerometerData.setAccuracy(pAccuracy);
				this.mAccelerometerListener.onAccelerometerChanged(this.mAccelerometerData);
				break;
		}
	}

	@Override
	public void onSensorChanged(SensorEvent pEvent) {
		switch(pEvent.sensor.getType()){
			case Sensor.TYPE_ACCELEROMETER:
				this.mAccelerometerData.setValues(pEvent.values);
				this.mAccelerometerListener.onAccelerometerChanged(this.mAccelerometerData);
				break;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void onDrawFrame(final GL10 pGL) {
		this.mTextureManager.loadPendingTextureToHardware(pGL);
		final float secondsElapsed = getSecondsElapsed();
		
		if(this.mScene != null){
			this.mScene.onUpdate(secondsElapsed);
		}
		
		if(this.mScene != null){
			this.mScene.onDraw(pGL);
		}
	}

	private float getSecondsElapsed() {
		final long now = System.nanoTime();
		final float secondsElapsed = (float)(now  - this.mLastTick) / TimeConstants.NANOSECONDSPERSECOND;
		this.mLastTick = now;
		return secondsElapsed;
	}
	
	public void loadTexture(final Texture pTexture) {
		this.mTextureManager.addTexturePendingForBeingLoadedToHardware(pTexture);
	}
	
	public boolean enableAccelerometer(final Context pContext, final AccelerometerListener pAccelerometerListener) {		
		final SensorManager sensorManager = (SensorManager) pContext.getSystemService(Context.SENSOR_SERVICE);
		if (isSensorSupported(sensorManager, Sensor.TYPE_ACCELEROMETER)) {
			registerSelfAsSensorListener(sensorManager, Sensor.TYPE_ACCELEROMETER);
			
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
