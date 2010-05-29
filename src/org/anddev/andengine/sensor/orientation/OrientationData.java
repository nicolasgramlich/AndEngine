package org.anddev.andengine.sensor.orientation;

import java.util.Arrays;

import org.anddev.andengine.sensor.BaseSensorData;

import android.hardware.SensorManager;

/**
 * @author Nicolas Gramlich
 * @since 11:30:33 - 25.05.2010
 */
public class OrientationData extends BaseSensorData {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public OrientationData() {
		super(3);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public float getRoll() {
		return super.mValues[SensorManager.DATA_Z];
	}
	
	public float getPitch() {
		return super.mValues[SensorManager.DATA_Y];
	}
	
	public float getYaw() {
		return super.mValues[SensorManager.DATA_X];
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString() {
		return "Orientation: " + Arrays.toString(this.mValues);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
