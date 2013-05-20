package org.andengine.util.exception;

import org.andengine.util.exception.AndEngineRuntimeException;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:33:53 - 01.05.2013
 */
public class IllegalClassNameException extends AndEngineRuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 5615299668631505188L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public IllegalClassNameException() {
		super();
	}

	public IllegalClassNameException(final String pMessage) {
		super(pMessage);
	}

	public IllegalClassNameException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public IllegalClassNameException(final String pMessage, final Throwable pThrowable) {
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
