package org.anddev.andengine.sensor.orientation;

import org.anddev.andengine.sensor.SensorDelay;

/**
 * @author Nicolas Gramlich
 * @since 11:12:36 - 31.10.2010
 */
public class OrientationSensorOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	final SensorDelay mSensorDelay;

	// ===========================================================
	// Constructors
	// ===========================================================

	public OrientationSensorOptions(final SensorDelay pSensorDelay) {
		this.mSensorDelay = pSensorDelay;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public SensorDelay getSensorDelay() {
		return this.mSensorDelay;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
