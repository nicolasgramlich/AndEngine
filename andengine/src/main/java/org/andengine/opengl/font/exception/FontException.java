package org.andengine.opengl.font.exception;

import org.andengine.util.exception.AndEngineRuntimeException;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:53:31 - 01.11.2011
 */
public class FontException extends AndEngineRuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 2766566088383545102L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public FontException() {
		super();
	}

	public FontException(final String pMessage) {
		super(pMessage);
	}

	public FontException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public FontException(final String pMessage, final Throwable pThrowable) {
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