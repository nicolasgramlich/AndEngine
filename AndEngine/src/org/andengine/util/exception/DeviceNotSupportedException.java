package org.andengine.util.exception;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:05:09 - 19.03.2012
 */
public class DeviceNotSupportedException extends AndEngineException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 2640523490821876076L;

	// ===========================================================
	// Fields
	// ===========================================================

	private final DeviceNotSupportedCause mDeviceNotSupportedCause;

	// ===========================================================
	// Constructors
	// ===========================================================

	public DeviceNotSupportedException(final DeviceNotSupportedCause pDeviceNotSupportedCause) {
		super();

		this.mDeviceNotSupportedCause = pDeviceNotSupportedCause;
	}

	public DeviceNotSupportedException(final DeviceNotSupportedCause pDeviceNotSupportedCause, final Throwable pThrowable) {
		super(pThrowable);

		this.mDeviceNotSupportedCause = pDeviceNotSupportedCause;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public DeviceNotSupportedCause getDeviceNotSupportedCause() {
		return this.mDeviceNotSupportedCause;
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

	public static enum DeviceNotSupportedCause {
		// ===========================================================
		// Elements
		// ===========================================================

		CODEPATH_INCOMPLETE,
		EGLCONFIG_NOT_FOUND;

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
}
