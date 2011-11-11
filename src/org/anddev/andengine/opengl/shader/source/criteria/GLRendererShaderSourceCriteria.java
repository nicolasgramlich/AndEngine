package org.anddev.andengine.opengl.shader.source.criteria;

import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.util.operator.StringOperator;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:04:27 - 10.10.2011
 */
public class GLRendererShaderSourceCriteria extends StringShaderSourceCriteria {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public GLRendererShaderSourceCriteria(final StringOperator pStringOperator, final String pGLRenderer) {
		super(pStringOperator, pGLRenderer);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected String getActualCriteria() {
		return GLState.getGLRenderer();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
