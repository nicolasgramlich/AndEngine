package org.anddev.andengine.input.touch.controller;

import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 20:23:33 - 13.07.2010
 */
public class SingleTouchControler implements ITouchController {
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
	public boolean onHandleMotionEvent(final MotionEvent pTouchEvent, final ITouchEventCallback pTouchEventCallback) {
		final TouchEvent touchEvent = TouchEvent.obtain();
		touchEvent.set(pTouchEvent.getX(), pTouchEvent.getY(), pTouchEvent.getAction(), 0, pTouchEvent);
		final boolean handled = pTouchEventCallback.onTouchEvent(touchEvent);
		TouchEvent.recycle(touchEvent);
		return handled;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
