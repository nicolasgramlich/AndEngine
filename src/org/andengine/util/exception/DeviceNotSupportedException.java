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

	// ===========================================================
	// Constructors
	// ===========================================================

	public DeviceNotSupportedException() {
		super();
	}

	public DeviceNotSupportedException(final String pMessage) {
		super(pMessage);
	}

	public DeviceNotSupportedException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public DeviceNotSupportedException(final String pMessage, final Throwable pThrowable) {
		super(pMessage, pThrowable);
	}

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
