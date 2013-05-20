package org.andengine.util.exception;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:25:52 - 03.11.2011
 */
public class MethodNotYetImplementedException extends AndEngineRuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = -4308430823868086531L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MethodNotYetImplementedException() {

	}

	public MethodNotYetImplementedException(final String pMessage) {
		super(pMessage);
	}

	public MethodNotYetImplementedException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public MethodNotYetImplementedException(final String pMessage, final Throwable pThrowable) {
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
