package org.anddev.andengine.opengl.shader.source.criteria;

import org.anddev.andengine.util.operator.IntOperator;
import org.anddev.andengine.util.system.SystemUtils;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:21:13 - 10.10.2011
 */
public class AndroidVersionCodeShaderSourceCriteria extends IntShaderSourceCriteria {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AndroidVersionCodeShaderSourceCriteria(final IntOperator pIntOperator, final int pAndroidVersionCode) {
		super(pIntOperator, pAndroidVersionCode);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected int getActualCriteria() {
		return SystemUtils.BUILD_VERSION_SDK;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
