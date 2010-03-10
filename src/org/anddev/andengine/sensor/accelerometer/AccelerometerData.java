package org.anddev.andengine.sensor.accelerometer;

import java.util.Arrays;

import android.hardware.SensorManager;

/**
 * @author Nicolas Gramlich
 * @since 16:50:44 - 10.03.2010
 */
public class AccelerometerData {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final float[] mValues = new float[3];
	private int mAccuracy;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public void setValues(final float[] pValues) {
		this.mValues[SensorManager.DATA_X] = pValues[SensorManager.DATA_X];
		this.mValues[SensorManager.DATA_Y] = pValues[SensorManager.DATA_Y];
		this.mValues[SensorManager.DATA_Z] = pValues[SensorManager.DATA_Z];
	}
	
	public float getX() {
		return this.mValues[SensorManager.DATA_X];
	}
	
	public float getY() {
		return this.mValues[SensorManager.DATA_Y];
	}
	
	public float getZ() {
		return this.mValues[SensorManager.DATA_Z];
	}

	public void setAccuracy(final int pAccuracy) {
		this.mAccuracy = pAccuracy;		
	}
	
	public int getAccuracy() {
		return this.mAccuracy;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public String toString() {
		return "Values: " + Arrays.toString(this.mValues);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
