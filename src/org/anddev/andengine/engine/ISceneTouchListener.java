package org.anddev.andengine.engine;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 09:21:14 - 31.03.2010
 */
public interface ISceneTouchListener { // TODO Move to a better place ...
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean onSceneTouchEvent(final MotionEvent pSceneMotionEvent);
}
