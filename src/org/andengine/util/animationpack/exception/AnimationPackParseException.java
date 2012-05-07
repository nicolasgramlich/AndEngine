package org.andengine.util.animationpack.exception;

import org.xml.sax.SAXException;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:03:45 - 03.06.2012
 */
public class AnimationPackParseException extends SAXException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 1136010869754861664L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnimationPackParseException() {
		super();
	}

	public AnimationPackParseException(final String pDetailMessage) {
		super(pDetailMessage);
	}

	public AnimationPackParseException(final Exception pException) {
		super(pException);
	}

	public AnimationPackParseException(final String pMessage, final Exception pException) {
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
