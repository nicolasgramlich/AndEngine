package org.andengine.util.experiment.exception;

/**
 * (c) 2013 Nicolas Gramlich
 * 
 * @author Nicolas Gramlich
 * @since 08:25:34 - 22.03.2013
 */
public class ExperimentNotFoundException extends ExperimentException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 8872925957672647007L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ExperimentNotFoundException() {
		super();
	}

	public ExperimentNotFoundException(final String pMessage) {
		super(pMessage);
	}

	public ExperimentNotFoundException(final Throwable pThrowable) {
		super(pThrowable);
	}

	public ExperimentNotFoundException(final String pMessage, final Throwable pThrowable) {
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
