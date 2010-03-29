package org.anddev.andengine.engine.options.resolutionpolicy;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import android.util.DisplayMetrics;
import android.widget.FrameLayout.LayoutParams;

/**
 * @author Nicolas Gramlich
 * @since 11:22:48 - 29.03.2010
 */
public class FillResolutionPolicy implements IResolutionPolicy {
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

	@Override
	public LayoutParams createLayoutParams(final DisplayMetrics pDisplayMetrics) {
		return new LayoutParams(FILL_PARENT, FILL_PARENT);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
