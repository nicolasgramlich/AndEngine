package org.anddev.andengine.input.touch;

import org.anddev.andengine.entity.Scene;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 10:59:10 - 31.03.2010
 */
public interface IOnSceneTouchListener {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean onSceneTouchEvent(final Scene pScene, final MotionEvent pSceneMotionEvent);
}