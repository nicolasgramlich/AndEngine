package org.andengine.extension.tmx.util.exception;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 17:20:25 - 08.08.2010
 */
public abstract class TMXException extends Exception {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 337819550394833109L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXException() {
		super();
	}

	public TMXException(final String pDetailMessage, final Throwable pThrowable) {
		super(pDetailMessage, pThrowable);
	}

	public TMXException(final String pDetailMessage) {
		super(pDetailMessage);
	}

	public TMXException(final Throwable pThrowable) {
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
