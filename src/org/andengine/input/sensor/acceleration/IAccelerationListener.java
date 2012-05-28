package org.andengine.input.sensor.acceleration;

/**
 * Listener interface that responds to changes in accelerometer data<br>
 * (c) 2010 Nicolas Gramlich <br>
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:58:38 - 10.03.2010
 */
public interface IAccelerationListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Called when the accuracy of the accelerometer changes, for example when you're in a vehicle on a bumpy road
	 * @param pAccelerationData Data from the accelerometer in three dimensions
	 */
	public void onAccelerationAccuracyChanged(final AccelerationData pAccelerationData);
	/**
	 * Called when the data from the accelerometer changes, for example when a user rotates his device
	 * @param pAccelerationData Data from the accelerometer in three dimensions
	 */
	public void onAccelerationChanged(final AccelerationData pAccelerationData);
}
