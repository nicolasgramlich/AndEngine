package org.andengine.input.sensor;

import android.hardware.SensorManager;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:14:38 - 31.10.2010
 */
public enum SensorDelay {
	// ===========================================================
	// Elements
	// ===========================================================

	NORMAL(SensorManager.SENSOR_DELAY_NORMAL),
	UI(SensorManager.SENSOR_DELAY_UI),
	GAME(SensorManager.SENSOR_DELAY_GAME),
	FASTEST(SensorManager.SENSOR_DELAY_FASTEST);

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mDelay;

	// ===========================================================
	// Constructors
	// ===========================================================

	private SensorDelay(final int pDelay) {
		this.mDelay = pDelay;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getDelay() {
		return this.mDelay;
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
