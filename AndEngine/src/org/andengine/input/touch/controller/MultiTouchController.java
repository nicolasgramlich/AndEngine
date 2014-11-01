package org.andengine.input.touch.controller;

import android.view.MotionEvent;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:23:33 - 13.07.2010
 */
public class MultiTouchController extends BaseTouchController {
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
	public void onHandleMotionEvent(final MotionEvent pMotionEvent) {
		final int action = pMotionEvent.getAction() & MotionEvent.ACTION_MASK;
		switch(action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				this.onHandleTouchAction(MotionEvent.ACTION_DOWN, pMotionEvent);
				return;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				this.onHandleTouchAction(MotionEvent.ACTION_UP, pMotionEvent);
				return;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_OUTSIDE:
				this.onHandleTouchAction(action, pMotionEvent);
				return;
			case MotionEvent.ACTION_MOVE:
				this.onHandleTouchMove(pMotionEvent);
				return;
			default:
				throw new IllegalArgumentException("Invalid Action detected: " + action);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void onHandleTouchMove(final MotionEvent pMotionEvent) {
		for(int i = pMotionEvent.getPointerCount() - 1; i >= 0; i--) {
			final int pointerIndex = i;
			final int pointerID = pMotionEvent.getPointerId(pointerIndex);
			this.fireTouchEvent(pMotionEvent.getX(pointerIndex), pMotionEvent.getY(pointerIndex), MotionEvent.ACTION_MOVE, pointerID, pMotionEvent);
		}
	}

	private void onHandleTouchAction(final int pAction, final MotionEvent pMotionEvent) {
		final int pointerIndex = MultiTouchController.getPointerIndex(pMotionEvent);
		final int pointerID = pMotionEvent.getPointerId(pointerIndex);
		this.fireTouchEvent(pMotionEvent.getX(pointerIndex), pMotionEvent.getY(pointerIndex), pAction, pointerID, pMotionEvent);
	}

	private static int getPointerIndex(final MotionEvent pMotionEvent) {
		return (pMotionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
