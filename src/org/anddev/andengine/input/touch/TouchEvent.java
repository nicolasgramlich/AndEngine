package org.anddev.andengine.input.touch;

import org.anddev.andengine.util.pool.GenericPool;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 10:17:42 - 13.07.2010
 */
public class TouchEvent {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final GenericPool<TouchEvent> TOUCHEVENT_POOL = new GenericPool<TouchEvent>() {
		@Override
		protected TouchEvent onAllocatePoolItem() {
			return new TouchEvent();
		}
	};
	
	public static final int ACTION_CANCEL = MotionEvent.ACTION_CANCEL;
	public static final int ACTION_DOWN = MotionEvent.ACTION_DOWN;
	public static final int ACTION_MOVE = MotionEvent.ACTION_MOVE;
	public static final int ACTION_OUTSIDE = MotionEvent.ACTION_OUTSIDE;
	public static final int ACTION_UP = MotionEvent.ACTION_UP;

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
		return TOUCHEVENT_POOL.obtainPoolItem();
	}

	public static void recycle(final TouchEvent pTouchEvent) {
		TOUCHEVENT_POOL.recylePoolItem(pTouchEvent);
	}

	public void recycle() {
		TouchEvent.recycle(this);
	}

	public void set(final float pX, final float pY, final int pAction, final int pPointerID, final MotionEvent pMotionEvent) {
		this.mX = pX;
		this.mY = pY;
		this.mAction = pAction;
		this.mPointerID = pPointerID;
		this.mMotionEvent = pMotionEvent;
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

	/**
	 * Provides the raw {@link MotionEvent} that originally caused this {@link TouchEvent}. 
	 * The coordinates of this {@link MotionEvent} are in surface-coordinates! 
	 * @return
	 */
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
