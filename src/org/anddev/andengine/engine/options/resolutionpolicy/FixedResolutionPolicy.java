package org.anddev.andengine.engine.options.resolutionpolicy;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout.LayoutParams;

/**
 * @author Nicolas Gramlich
 * @since 11:23:00 - 29.03.2010
 */
public class FixedResolutionPolicy implements IResolutionPolicy {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FixedResolutionPolicy(final int pWidth, final int pHeight) {
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public LayoutParams createLayoutParams(final DisplayMetrics pDisplayMetrics) {
		final LayoutParams layoutParams = new LayoutParams(this.mWidth, this.mHeight);
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
