package org.andengine.opengl.shader.source.criteria;

import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.data.operator.StringOperator;

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
	protected String getActualCriteria(final GLState pGLState) {
		return pGLState.getRenderer();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
