package org.anddev.andengine.input.touch.controller;

import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 20:23:45 - 13.07.2010
 */
public interface ITouchController {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public boolean onHandleMotionEvent(final MotionEvent pMotionEvent, final ITouchEventCallback pTouchEventCallback);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	static interface ITouchEventCallback {
		public boolean onTouchEvent(final TouchEvent pTouchEvent);
	}
}
