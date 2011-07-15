package org.anddev.andengine.sensor.orientation;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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
