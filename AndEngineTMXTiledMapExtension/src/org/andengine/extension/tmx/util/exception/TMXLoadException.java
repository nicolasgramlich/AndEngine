package org.andengine.extension.tmx.util.exception;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 00:10:02 - 28.07.2010
 */
public class TMXLoadException extends TMXException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = -8295358631698809883L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXLoadException() {
		super();
	}

	public TMXLoadException(final String pDetailMessage, final Throwable pThrowable) {
		super(pDetailMessage, pThrowable);
	}

	public TMXLoadException(final String pDetailMessage) {
		super(pDetailMessage);
	}

	public TMXLoadException(final Throwable pThrowable) {
		super(pThrowable);
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
