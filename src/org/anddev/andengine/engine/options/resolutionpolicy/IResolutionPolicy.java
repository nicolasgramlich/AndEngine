package org.anddev.andengine.engine.options.resolutionpolicy;

import android.util.DisplayMetrics;
import android.widget.FrameLayout.LayoutParams;

/**
 * @author Nicolas Gramlich
 * @since 11:02:35 - 29.03.2010
 */
public interface IResolutionPolicy {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public LayoutParams createLayoutParams(final DisplayMetrics pDisplayMetrics);
}
