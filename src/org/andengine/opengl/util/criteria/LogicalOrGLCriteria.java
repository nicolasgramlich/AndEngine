package org.andengine.opengl.util.criteria;

import org.andengine.opengl.util.GLState;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:10:26 - 12.10.2011
 */
public class LogicalOrGLCriteria implements IGLCriteria {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IGLCriteria[] mGLCriterias;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LogicalOrGLCriteria(final IGLCriteria ... pGLCriterias) {
		this.mGLCriterias = pGLCriterias;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean isMet(final GLState pGLState) {
		for(final IGLCriteria gLCriteria : this.mGLCriterias) {
			if(gLCriteria.isMet(pGLState)) {
				return true;
			}
		}
		return false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
