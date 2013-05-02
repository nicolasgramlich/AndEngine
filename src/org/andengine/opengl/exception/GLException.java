package org.andengine.opengl.exception;

import android.opengl.GLU;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:44:27 - 13.02.2012
 */
public class GLException extends RuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = -7494923307858371890L;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mError;

	// ===========================================================
	// Constructors
	// ===========================================================

	public GLException(final int pError) {
		this(pError, GLException.getErrorString(pError));
	}

	public GLException(final int pError, final String pString) {
		super(pString);

		this.mError = pError;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getError() {
		return this.mError;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private static String getErrorString(final int pError) {
		String errorString = GLU.gluErrorString(pError);
		if (errorString == null) {
			errorString = "Unknown error '0x" + Integer.toHexString(pError) + "'.";
		}
		return errorString;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
