package org.anddev.andengine.input.touch.controller;

import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 21:06:40 - 13.07.2010
 */
public abstract class BaseTouchController implements ITouchController  {
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

	protected static boolean fireTouchEvent(float pX, float pY, int pAction, int pPointerID, final MotionEvent pMotionEvent, final ITouchEventCallback pTouchEventCallback) {
		final TouchEvent touchEvent = TouchEvent.obtain();
		touchEvent.set(pX, pY, pAction, pPointerID, pMotionEvent);
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
