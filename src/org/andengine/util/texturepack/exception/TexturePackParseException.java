package org.andengine.util.texturepack.exception;

import org.xml.sax.SAXException;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:29:20 - 29.07.2011
 */
public class TexturePackParseException extends SAXException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 5773816582330137037L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TexturePackParseException() {
		super();
	}

	public TexturePackParseException(final String pDetailMessage) {
		super(pDetailMessage);
	}

	public TexturePackParseException(final Exception pException) {
		super(pException);
	}

	public TexturePackParseException(final String pMessage, final Exception pException) {
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
