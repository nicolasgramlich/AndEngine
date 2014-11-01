package org.andengine.extension.svg.exception;

/**
 * @author Larva Labs, LLC
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 17:00:21 - 21.05.2011
 */
public class SVGParseException extends RuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 7090913212278249388L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGParseException() {

	}

	public SVGParseException(final String pMessage) {
		super(pMessage);
	}

	public SVGParseException(final String pMessage, final Throwable pThrowable) {
		super(pMessage, pThrowable);
	}

	public SVGParseException(final Throwable pThrowable) {
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
