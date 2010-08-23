package org.anddev.andengine.input.touch.detector;

import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 20:49:25 - 23.08.2010
 */
public class HoldDetector {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT = 200;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mEnabled = true;
	private long mTriggerHoldMinimumMilliseconds = TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT;
	private final IHoldDetectorListener mHoldDetectorListener;

	private long mDownTimeMilliseconds = Long.MIN_VALUE;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HoldDetector(final IHoldDetectorListener pClickDetectorListener) {
		this(TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT, pClickDetectorListener);
	}

	public HoldDetector(final long pTriggerHoldMinimumMilliseconds, final IHoldDetectorListener pClickDetectorListener) {
		this.mTriggerHoldMinimumMilliseconds = pTriggerHoldMinimumMilliseconds;
		this.mHoldDetectorListener = pClickDetectorListener;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public long getTriggerHoldMinimumMilliseconds() {
		return this.mTriggerHoldMinimumMilliseconds;
	}

	public void setTriggerHoldMinimumMilliseconds(final long pTriggerHoldMinimumMilliseconds) {
		this.mTriggerHoldMinimumMilliseconds = pTriggerHoldMinimumMilliseconds;
	}

	public boolean isEnabled() {
		return this.mEnabled;
	}

	public void setEnabled(final boolean pEnabled) {
		this.mEnabled = pEnabled;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public boolean onTouchEvent(final TouchEvent pTouchEvent) {
		if(this.mEnabled) {
			switch(pTouchEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					this.mDownTimeMilliseconds = pTouchEvent.getMotionEvent().getDownTime();
					return true;
				case MotionEvent.ACTION_MOVE:
					{
						final long currentTimeMilliseconds = pTouchEvent.getMotionEvent().getEventTime();
	
						final long holdTimeMilliseconds = currentTimeMilliseconds - this.mDownTimeMilliseconds;
						if(holdTimeMilliseconds >= this.mTriggerHoldMinimumMilliseconds) {
							this.mHoldDetectorListener.onHold(this, holdTimeMilliseconds, pTouchEvent);
						}
						return true;
					}
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					{
						final long upTimeMilliseconds = pTouchEvent.getMotionEvent().getEventTime();
	
						final long holdTimeMilliseconds = upTimeMilliseconds - this.mDownTimeMilliseconds;
						if(holdTimeMilliseconds >= this.mTriggerHoldMinimumMilliseconds) {
							this.mHoldDetectorListener.onHoldFinished(this, holdTimeMilliseconds, pTouchEvent);
						}
						return true;
					}
				default:
					return false;
			}
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IHoldDetectorListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onHold(final HoldDetector pHoldDetector, final long pHoldTimeMilliseconds, final TouchEvent pTouchEvent);
		public void onHoldFinished(final HoldDetector pHoldDetector, final long pHoldTimeMilliseconds, final TouchEvent pTouchEvent);
	}
}
