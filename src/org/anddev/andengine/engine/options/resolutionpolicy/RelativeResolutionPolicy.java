package org.anddev.andengine.engine.options.resolutionpolicy;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout.LayoutParams;

/**
 * @author Nicolas Gramlich
 * @since 11:23:00 - 29.03.2010
 */
public class RelativeResolutionPolicy implements IResolutionPolicy {
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
		this.mWidthScale = pScale;
		this.mHeightScale = pScale;
	}

	public RelativeResolutionPolicy(final int pWidthScale, final int pHeightScale) {
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
	public LayoutParams createLayoutParams(final DisplayMetrics pDisplayMetrics) {
		final int width = Math.round(pDisplayMetrics.widthPixels * this.mWidthScale);
		final int height = Math.round(pDisplayMetrics.heightPixels * this.mHeightScale);

		final LayoutParams layoutParams = new LayoutParams(width, height);

		layoutParams.gravity = Gravity.CENTER;
		return layoutParams;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
