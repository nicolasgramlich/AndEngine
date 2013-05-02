package org.andengine.opengl.util.criteria;

import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.data.operator.IntOperator;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:59:32 - 10.10.2011
 */
public abstract class IntGLCriteria implements IGLCriteria {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mCriteria;
	private final IntOperator mIntOperator;

	// ===========================================================
	// Constructors
	// ===========================================================

	public IntGLCriteria(final IntOperator pIntOperator, final int pCriteria) {
		this.mCriteria = pCriteria;
		this.mIntOperator = pIntOperator;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract int getActualCriteria(final GLState pGLState);

	@Override
	public boolean isMet(final GLState pGLState) {
		return this.mIntOperator.check(this.getActualCriteria(pGLState), this.mCriteria);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
