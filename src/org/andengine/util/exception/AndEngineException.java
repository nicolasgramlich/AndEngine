package org.andengine.util.exception;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 02:40:26 - 07.08.2011
 */
public class AndEngineException extends RuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final long serialVersionUID = -4325207483842883006L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================


	public AndEngineException() {
		super();
	}

	public AndEngineException(final String pMessage, final Throwable pThrowable) {
		super(pMessage, pThrowable);
	}

	public AndEngineException(final String pMessage) {
		super(pMessage);
	}

	public AndEngineException(final Throwable pThrowable) {
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
