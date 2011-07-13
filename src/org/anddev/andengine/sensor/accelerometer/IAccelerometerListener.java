package org.anddev.andengine.sensor.accelerometer;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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
