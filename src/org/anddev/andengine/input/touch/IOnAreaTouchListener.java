package org.anddev.andengine.input.touch;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 17:23:50 - 31.03.2010
 */
public interface IOnAreaTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean onAreaTouched(final ITouchArea pTouchArea, final MotionEvent pSceneMotionEvent);
}
