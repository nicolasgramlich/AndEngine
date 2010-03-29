package org.anddev.andengine.engine.options.resolutionpolicy;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout.LayoutParams;

/**
 * @author Nicolas Gramlich
 * @since 11:23:00 - 29.03.2010
 */
public class RatioResolutionPolicy implements IResolutionPolicy {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mRatio;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RatioResolutionPolicy(final float pRatio) {
		this.mRatio = pRatio;
	}

	public RatioResolutionPolicy(final int pWidthRatio, final int pHeightRatio) {
		this.mRatio = (float)pWidthRatio / pHeightRatio;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public LayoutParams createLayoutParams(final DisplayMetrics pDisplayMetrics) {
		final float realRatio = (float)pDisplayMetrics.widthPixels / pDisplayMetrics.heightPixels;

		final int width;
		final int height;
		if(realRatio < this.mRatio) {
			width = pDisplayMetrics.widthPixels;
			height = Math.round(width / this.mRatio);
		} else {
			height = pDisplayMetrics.heightPixels;
			width = Math.round(height * this.mRatio);
		}

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
