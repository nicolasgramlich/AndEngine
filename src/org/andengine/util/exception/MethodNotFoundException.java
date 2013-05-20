package org.andengine.util.exception;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:13:32 - 26.12.2011
 */
public class MethodNotFoundException extends AndEngineRuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = -6878670836555401945L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MethodNotFoundException() {

	}

	public MethodNotFoundException(final String pMessage) {
		super(pMessage);
	}

	public MethodNotFoundException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public MethodNotFoundException(final String pMessage, final Throwable pThrowable) {
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
