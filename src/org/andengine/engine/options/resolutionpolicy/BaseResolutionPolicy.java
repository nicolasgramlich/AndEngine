package org.andengine.engine.options.resolutionpolicy;

import android.view.View.MeasureSpec;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 22:46:43 - 06.10.2010
 */
public abstract class BaseResolutionPolicy implements IResolutionPolicy {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected static void throwOnNotMeasureSpecEXACTLY(final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
		final int specWidthMode = MeasureSpec.getMode(pWidthMeasureSpec);
		final int specHeightMode = MeasureSpec.getMode(pHeightMeasureSpec);

		if (specWidthMode != MeasureSpec.EXACTLY || specHeightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("This IResolutionPolicy requires MeasureSpec.EXACTLY ! That means ");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
