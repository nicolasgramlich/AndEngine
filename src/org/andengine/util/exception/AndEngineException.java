package org.andengine.util.exception;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 02:40:26 - 07.08.2011
 */
public class AndEngineException extends Exception {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 6577340337732194722L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AndEngineException() {
		super();
	}

	public AndEngineException(final String pMessage) {
		super(pMessage);
	}

	public AndEngineException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public AndEngineException(final String pMessage, final Throwable pThrowable) {
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
