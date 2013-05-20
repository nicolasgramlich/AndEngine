package org.andengine.util.exception;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:13:32 - 26.12.2011
 */
public class MethodNotSupportedException extends AndEngineRuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 1248621152476879759L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MethodNotSupportedException() {

	}

	public MethodNotSupportedException(final String pMessage) {
		super(pMessage);
	}

	public MethodNotSupportedException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public MethodNotSupportedException(final String pMessage, final Throwable pThrowable) {
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
