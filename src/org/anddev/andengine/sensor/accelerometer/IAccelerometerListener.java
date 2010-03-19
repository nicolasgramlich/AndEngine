package org.anddev.andengine.sensor.accelerometer;

/**
 * @author Nicolas Gramlich
 * @since 16:58:38 - 10.03.2010
 */
public interface IAccelerometerListener {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onAccelerometerChanged(final AccelerometerData pAccelerometerData);
}
