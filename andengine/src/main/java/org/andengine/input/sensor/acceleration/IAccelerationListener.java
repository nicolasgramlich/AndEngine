package org.andengine.input.sensor.acceleration;

/**
 * (c) 2010 Nicolas Gramlich 
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

	public void onAccelerationAccuracyChanged(final AccelerationData pAccelerationData);
	public void onAccelerationChanged(final AccelerationData pAccelerationData);
}
