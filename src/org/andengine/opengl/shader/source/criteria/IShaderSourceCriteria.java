package org.andengine.opengl.shader.source.criteria;

import org.andengine.opengl.util.GLState;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:43:34 - 10.10.2011
 */
public interface IShaderSourceCriteria {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public boolean isMet(final GLState pGLState);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}