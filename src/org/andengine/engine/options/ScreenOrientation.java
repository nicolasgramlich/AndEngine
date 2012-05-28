package org.andengine.engine.options;

/**
 * Whether the app should be in portrait mode or landscape mode, and whether it should react to tilting the device.
 * Enum contains the following values:<ul>
 * <li><b><u>LANDSCAPE_FIXED</b></u>: The app will be fixed in its default Landscape mode
 * <li><b><u>LANDSCAPE_SENSOR</b></u>: The app will automatically rotate between the Landscape modes, depending on the orientation of the device
 * <li><b><u>PORTRAIT_FIXED</b></u>: The app will be fixed in its default Portrait mode 
 * <li><b><u>PORTRAIT_SENSOR</b></u>: The app will automatically rotate between the Portrait modes, depending on the orientation of the device.
 * </ul>
 * <br>
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:48:03 - 04.04.2012
 */
public enum ScreenOrientation {
	// ===========================================================
	// Elements
	// ===========================================================

	/** The app will be fixed in its default Landscape mode. */
	LANDSCAPE_FIXED,
	/** The app will automatically rotate between the Landscape modes, depending on the orientation of the device. */
	LANDSCAPE_SENSOR,
	/** The app will be fixed in its default Portrait mode. */
	PORTRAIT_FIXED,
	/** The app will automatically rotate between the Portrait modes, depending on the orientation of the device. */
	PORTRAIT_SENSOR;

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

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
