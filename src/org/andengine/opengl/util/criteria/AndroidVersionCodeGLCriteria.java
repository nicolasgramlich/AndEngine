package org.andengine.opengl.util.criteria;

import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.data.operator.IntOperator;

import android.os.Build;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:21:13 - 10.10.2011
 */
public class AndroidVersionCodeGLCriteria extends IntGLCriteria {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AndroidVersionCodeGLCriteria(final IntOperator pIntOperator, final int pAndroidVersionCode) {
		super(pIntOperator, pAndroidVersionCode);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected int getActualCriteria(final GLState pGLState) {
		return Build.VERSION.SDK_INT;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
