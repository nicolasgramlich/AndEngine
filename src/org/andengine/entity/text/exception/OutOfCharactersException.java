package org.andengine.entity.text.exception;


/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:53:31 - 01.11.2011
 */
public class OutOfCharactersException extends TextException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 3076821980884912905L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public OutOfCharactersException() {
		super();
	}

	public OutOfCharactersException(final String pMessage) {
		super(pMessage);
	}

	public OutOfCharactersException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public OutOfCharactersException(final String pMessage, final Throwable pThrowable) {
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
