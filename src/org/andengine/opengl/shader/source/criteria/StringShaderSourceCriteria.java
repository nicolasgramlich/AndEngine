package org.andengine.opengl.shader.source.criteria;

import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.data.operator.StringOperator;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:59:32 - 10.10.2011
 */
public abstract class StringShaderSourceCriteria implements IShaderSourceCriteria {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mCriteria;
	private final StringOperator mStringOperator;

	// ===========================================================
	// Constructors
	// ===========================================================

	public StringShaderSourceCriteria(final StringOperator pStringOperator, final String pCriteria) {
		this.mCriteria = pCriteria;
		this.mStringOperator = pStringOperator;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract String getActualCriteria(final GLState pGLState);

	@Override
	public boolean isMet(final GLState pGLState) {
		return this.mStringOperator.check(this.getActualCriteria(pGLState), this.mCriteria);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
