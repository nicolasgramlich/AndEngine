package org.andengine.opengl.shader.source.criteria;

import org.andengine.opengl.util.GLState;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:15:55 - 12.10.2011
 */
public class LogicalAndShaderSourceCriteria implements IShaderSourceCriteria {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final IShaderSourceCriteria[] mShaderSourceCriterias;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public LogicalAndShaderSourceCriteria(final IShaderSourceCriteria ... pShaderSourceCriterias) {
		this.mShaderSourceCriterias = pShaderSourceCriterias;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean isMet(final GLState pGLState) {
		for(IShaderSourceCriteria shaderSourceCriteria : this.mShaderSourceCriterias) {
			if(!shaderSourceCriteria.isMet(pGLState)) {
				return false;
			}
		}
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
