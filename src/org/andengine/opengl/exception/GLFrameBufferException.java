package org.andengine.opengl.exception;


/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:47:22 - 13.02.2012
 */
public class GLFrameBufferException extends GLException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = -8910272713633644676L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public GLFrameBufferException(final int pError) {
		super(pError);
	}

	public GLFrameBufferException(final int pError, final String pString) {
		super(pError, pString);
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
