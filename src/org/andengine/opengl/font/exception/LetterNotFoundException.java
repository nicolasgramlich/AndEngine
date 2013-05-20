package org.andengine.opengl.font.exception;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:19:24 - 03.11.2011
 */
public class LetterNotFoundException extends FontException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 5260601170771253529L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public LetterNotFoundException() {
		super();
	}

	public LetterNotFoundException(final String pMessage) {
		super(pMessage);
	}

	public LetterNotFoundException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public LetterNotFoundException(final String pMessage, final Throwable pThrowable) {
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
