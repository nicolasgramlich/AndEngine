package org.anddev.andengine.opengl.shader.source.criteria;

import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.util.operator.StringOperator;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:02:01 - 10.10.2011
 */
public class GLExtensionsShaderSourceCriteria extends StringShaderSourceCriteria {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public GLExtensionsShaderSourceCriteria(final StringOperator pStringOperator, final String pGLExtensions) {
		super(pStringOperator, pGLExtensions);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected String getActualCriteria() {
		return GLState.getGLExtensions();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
