package org.andengine.util.exception;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:33:53 - 23.04.2012
 */
public class CancelledException extends AndEngineException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = -78123211381435596L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public CancelledException() {
		super();
	}

	public CancelledException(String pMessage) {
		super(pMessage);
	}

	public CancelledException(Throwable pThrowable) {
		super(pThrowable);
	}

	public CancelledException(String pMessage, Throwable pThrowable) {
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
