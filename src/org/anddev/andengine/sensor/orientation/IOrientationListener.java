package org.anddev.andengine.sensor.orientation;


/**
 * @author Nicolas Gramlich
 * @since 11:30:42 - 25.05.2010
 */
public interface IOrientationListener {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onOrientationChanged(final OrientationData pOrientationData);
}
