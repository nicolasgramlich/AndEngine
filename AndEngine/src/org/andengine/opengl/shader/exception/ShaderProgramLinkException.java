package org.andengine.opengl.shader.exception;
/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 02:31:54 - 07.08.2011
 */
public class ShaderProgramLinkException extends ShaderProgramException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 5418521931032742504L;
	
	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ShaderProgramLinkException(final String pMessage) {
		super(pMessage);
	}

	public ShaderProgramLinkException(final String pMessage, final ShaderProgramException pShaderProgramException) {
		super(pMessage, pShaderProgramException);
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
