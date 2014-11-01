package org.andengine.extension.tmx.util.exception;

import org.xml.sax.SAXException;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 00:10:02 - 28.07.2010
 */
public class TMXParseException extends SAXException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 2213964295487921492L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXParseException() {
		super();
	}

	public TMXParseException(final String pDetailMessage) {
		super(pDetailMessage);
	}

	public TMXParseException(final Exception pException) {
		super(pException);
	}

	public TMXParseException(final String pMessage, final Exception pException) {
		super(pMessage, pException);
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
