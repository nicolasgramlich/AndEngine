package org.andengine.util.experiment.exception;

import org.andengine.util.exception.AndEngineException;

/**
 * (c) 2013 Nicolas Gramlich
 * 
 * @author Nicolas Gramlich
 * @since 00:45:14 - 23.03.2013
 */
public class ExperimentException extends AndEngineException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 8028018956559796977L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ExperimentException() {
		super();
	}

	public ExperimentException(final String pMessage) {
		super(pMessage);
	}

	public ExperimentException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public ExperimentException(final String pMessage, final Throwable pThrowable) {
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
