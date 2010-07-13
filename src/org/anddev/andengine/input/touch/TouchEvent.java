package org.anddev.andengine.input.touch;

import java.util.Stack;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 10:17:42 - 13.07.2010
 */
public class TouchEvent {
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final Stack<TouchEvent> TOUCHEVENT_STACK = new Stack<TouchEvent>();   
	private static final Object TOUCHEVENT_RECYCLE_LOCK = new Object();

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mPointerID;

	protected float mX;
	protected float mY;

	protected int mAction;

	protected MotionEvent mMotionEvent;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public static TouchEvent obtain() {
		synchronized (TOUCHEVENT_RECYCLE_LOCK) {
			if(TOUCHEVENT_STACK.isEmpty()){
				return new TouchEvent();
			} else {
				return TOUCHEVENT_STACK.pop();
			}
		}
	}
	
	public static void recycle(final TouchEvent pTouchEvent) {
		synchronized (TOUCHEVENT_RECYCLE_LOCK) {
			TOUCHEVENT_STACK.push(pTouchEvent);
		}
	}
	
	public void recycle() {
		TouchEvent.recycle(this);
	}

	public void set(final float pX, final float pY, final int pAction, final int pPointerID, final MotionEvent pTouchEvent) {
		this.mX = pX;
		this.mY = pY;
		this.mAction = pAction;
		this.mPointerID = pPointerID;
		this.mMotionEvent = pTouchEvent;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getX() {
		return this.mX;
	}

	public float getY() {
		return this.mY;
	}

	public void set(final float pX, final float pY) {
		this.mX = pX;
		this.mY = pY;
	}

	public void offset(final float pDeltaX, final float pDeltaY) {
		this.mX += pDeltaX;
		this.mY += pDeltaY;
	}

	public int getPointerID() {
		return this.mPointerID;
	}

	public int getAction() {
		return this.mAction;
	}

	public MotionEvent getMotionEvent() {
		return this.mMotionEvent;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
