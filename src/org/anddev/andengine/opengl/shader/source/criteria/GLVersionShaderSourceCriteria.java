package org.anddev.andengine.opengl.shader.source.criteria;

import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.util.operator.StringOperator;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:52:33 PM - 10.10.2011
 */
public class GLVersionShaderSourceCriteria extends StringShaderSourceCriteria {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public GLVersionShaderSourceCriteria(final StringOperator pStringOperator, final String pExpectedGLVersion) {
		super(pStringOperator, pExpectedGLVersion);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected String getActualCriteria() {
		return GLState.getGLVersion();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
