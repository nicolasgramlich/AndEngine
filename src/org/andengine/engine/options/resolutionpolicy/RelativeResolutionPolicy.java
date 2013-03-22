package org.andengine.engine.options.resolutionpolicy;

import android.view.View.MeasureSpec;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:23:00 - 29.03.2010
 */
public class RelativeResolutionPolicy extends BaseResolutionPolicy {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mWidthScale;
	private final float mHeightScale;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RelativeResolutionPolicy(final float pScale) {
		this(pScale, pScale);
	}

	public RelativeResolutionPolicy(final float pWidthScale, final float pHeightScale) {
		this.mWidthScale = pWidthScale;
		this.mHeightScale = pHeightScale;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onMeasure(final IResolutionPolicy.Callback pResolutionPolicyCallback, final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
		BaseResolutionPolicy.throwOnNotMeasureSpecEXACTLY(pWidthMeasureSpec, pHeightMeasureSpec);

		final int measuredWidth = (int) (MeasureSpec.getSize(pWidthMeasureSpec) * this.mWidthScale);
		final int measuredHeight = (int) (MeasureSpec.getSize(pHeightMeasureSpec) * this.mHeightScale);

		pResolutionPolicyCallback.onResolutionChanged(measuredWidth, measuredHeight);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
