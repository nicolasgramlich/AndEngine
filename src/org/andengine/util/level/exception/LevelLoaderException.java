package org.andengine.util.level.exception;

import org.andengine.util.exception.AndEngineRuntimeException;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:59:01 - 19.04.2012
 */
public class LevelLoaderException extends AndEngineRuntimeException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 1027781104016235328L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public LevelLoaderException() {

	}

	public LevelLoaderException(final String pMessage) {
		super(pMessage);
	}

	public LevelLoaderException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public LevelLoaderException(final String pMessage, final Throwable pThrowable) {
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
