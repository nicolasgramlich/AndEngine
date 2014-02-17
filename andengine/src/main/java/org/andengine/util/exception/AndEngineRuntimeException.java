package org.andengine.util.exception;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 02:40:26 - 07.08.2011
 */
public class AndEngineRuntimeException extends RuntimeException {
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


	public AndEngineRuntimeException() {
		super();
	}

	public AndEngineRuntimeException(final String pMessage) {
		super(pMessage);
	}

	public AndEngineRuntimeException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public AndEngineRuntimeException(final String pMessage, final Throwable pThrowable) {
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
