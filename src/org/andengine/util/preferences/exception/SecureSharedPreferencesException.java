package org.andengine.util.preferences.exception;

import org.andengine.util.exception.AndEngineRuntimeException;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:09:38 - 13.04.2013
 */
public class SecureSharedPreferencesException extends AndEngineRuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 5683854473467471982L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SecureSharedPreferencesException() {

	}

	public SecureSharedPreferencesException(final String pMessage) {
		super(pMessage);
	}

	public SecureSharedPreferencesException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public SecureSharedPreferencesException(final String pMessage, final Throwable pThrowable) {
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
