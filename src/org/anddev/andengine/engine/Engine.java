package org.anddev.andengine.engine;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.IUpdateHandler;
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

	private boolean mRunning = false;
	
	private long mLastTick = System.nanoTime();

	private final EngineOptions mEngineOptions;
	
	private Scene mScene;
	
	private TextureManager mTextureManager = new TextureManager();

	private AccelerometerListener mAccelerometerListener;
	private AccelerometerData mAccelerometerData;

	private ArrayList<IUpdateHandler> mPreFrameHandlers = new ArrayList<IUpdateHandler>();
	private ArrayList<IUpdateHandler> mPostFrameHandlers = new ArrayList<IUpdateHandler>();

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public Engine(final EngineOptions pEngineOptions) {
		this.mEngineOptions = pEngineOptions;		
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

	public int getGameWidth() {
		return this.mEngineOptions.getGameWidth();
	}

	public int getGameHeight() {
		return this.mEngineOptions.getGameHeight();
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
	public void onSensorChanged(SensorEvent pEvent) {
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

	public void onDrawFrame(final GL10 pGL) {
		if(this.mRunning) {
			this.mTextureManager.loadPendingTextureToHardware(pGL);
			final float secondsElapsed = getSecondsElapsed();
			
			updatePreFrameHandlers(secondsElapsed);
			
			if(this.mScene != null){
				this.mScene.onUpdate(secondsElapsed);
			}
			
			if(this.mScene != null){
				this.mScene.onDraw(pGL);
			}
	
			updatePostFrameHandlers(secondsElapsed);
		}
	}

	private void updatePreFrameHandlers(final float pSecondsElapsed) {
		final ArrayList<IUpdateHandler> updateHandlers = this.mPreFrameHandlers;
		updateHandlers(pSecondsElapsed, updateHandlers);
	}

	private void updatePostFrameHandlers(final float pSecondsElapsed) {
		final ArrayList<IUpdateHandler> updateHandlers = this.mPostFrameHandlers;
		updateHandlers(pSecondsElapsed, updateHandlers);
	}

	private void updateHandlers(final float pSecondsElapsed, final ArrayList<IUpdateHandler> pUpdateHandlers) {
		final int layerCount = pUpdateHandlers.size();
		for(int i = 0; i < layerCount; i++)
			pUpdateHandlers.get(i).onUpdate(pSecondsElapsed);
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
