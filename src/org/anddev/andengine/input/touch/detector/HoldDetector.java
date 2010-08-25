package org.anddev.andengine.input.touch.detector;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.input.touch.TouchEvent;

import android.os.SystemClock;
import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 20:49:25 - 23.08.2010
 */
public class HoldDetector implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT = 200;
	private static final float TRIGGER_HOLD_MAXIMUM_DISTANCE_DEFAULT = 10;

	private static final float TIME_BETWEEN_UPDATES_DEFAULT = 0.1f;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mEnabled = true;
	private long mTriggerHoldMinimumMilliseconds;
	private float mTriggerHoldMaximumDistance;
	private final IHoldDetectorListener mHoldDetectorListener;

	private long mDownTimeMilliseconds = Long.MIN_VALUE;

	private float mDownX;
	private float mDownY;
	
	private float mHoldX;
	private float mHoldY;

	private boolean mMaximumDistanceExceeded = false;

	private boolean mTriggerOnHold = false;
	private boolean mTriggerOnHoldFinished = false;

	private final TimerHandler mTimerHandler;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HoldDetector(final IHoldDetectorListener pClickDetectorListener) {
		this(TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT, TRIGGER_HOLD_MAXIMUM_DISTANCE_DEFAULT, TIME_BETWEEN_UPDATES_DEFAULT, pClickDetectorListener);
	}

	public HoldDetector(final long pTriggerHoldMinimumMilliseconds, final float pTriggerHoldMaximumDistance, final float pTimeBetweenUpdates, final IHoldDetectorListener pClickDetectorListener) {
		this.mTriggerHoldMinimumMilliseconds = pTriggerHoldMinimumMilliseconds;
		this.mTriggerHoldMaximumDistance = pTriggerHoldMaximumDistance;
		this.mHoldDetectorListener = pClickDetectorListener;

		this.mTimerHandler = new TimerHandler(pTimeBetweenUpdates, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				HoldDetector.this.fireListener();
			}
		});
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

	public float getTriggerHoldMaximumDistance() {
		return this.mTriggerHoldMaximumDistance;
	}

	public void setTriggerHoldMaximumDistance(final float pTriggerHoldMaximumDistance) {
		this.mTriggerHoldMaximumDistance = pTriggerHoldMaximumDistance;
	}

	public boolean isEnabled() {
		return this.mEnabled;
	}

	public void setEnabled(final boolean pEnabled) {
		this.mEnabled = pEnabled;
	}

	public boolean isHolding() {
		return this.mTriggerOnHold;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mTimerHandler.onUpdate(pSecondsElapsed);
	}
	
	@Override
	public void reset() {
		this.mTimerHandler.reset();
	}
	
	protected void fireListener() {
		if(this.mTriggerOnHoldFinished) {
			this.mHoldDetectorListener.onHoldFinished(this, SystemClock.uptimeMillis() - this.mDownTimeMilliseconds, this.mHoldX, this.mHoldY);
			this.mTriggerOnHoldFinished = false;
			this.mTriggerOnHold = false;
		} else if(this.mTriggerOnHold) {
			this.mHoldDetectorListener.onHold(this, SystemClock.uptimeMillis() - this.mDownTimeMilliseconds, this.mHoldX, this.mHoldY);
		}
		//		this.mHoldDetectorListener.onHold(this, pHoldTimeMilliseconds)
		//		CheckPassed().onControlChange(BaseOnScreenControl.this, BaseOnScreenControl.this.mControlValueX, BaseOnScreenControl.this.mControlValueY);
	}

	public boolean onTouchEvent(final TouchEvent pTouchEvent) {
		if(this.mEnabled) {
			final MotionEvent motionEvent = pTouchEvent.getMotionEvent();

			this.mHoldX = pTouchEvent.getX();
			this.mHoldY = pTouchEvent.getY();

			switch(pTouchEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					this.mDownTimeMilliseconds = motionEvent.getDownTime();
					this.mDownX = motionEvent.getX();
					this.mDownY = motionEvent.getY();
					this.mMaximumDistanceExceeded = false;
					return true;
				case MotionEvent.ACTION_MOVE:
				{
					final long currentTimeMilliseconds = motionEvent.getEventTime();

					final float triggerHoldMaximumDistance = this.mTriggerHoldMaximumDistance;
					this.mMaximumDistanceExceeded = this.mMaximumDistanceExceeded || Math.abs(this.mDownX - motionEvent.getX()) > triggerHoldMaximumDistance  || Math.abs(this.mDownY - motionEvent.getY()) > triggerHoldMaximumDistance;
					if(this.mTriggerOnHold || !this.mMaximumDistanceExceeded) {
						final long holdTimeMilliseconds = currentTimeMilliseconds - this.mDownTimeMilliseconds;
						if(holdTimeMilliseconds >= this.mTriggerHoldMinimumMilliseconds) {
							this.mTriggerOnHold = true;
						}
					}
					return true;
				}
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
				{
					final long upTimeMilliseconds = motionEvent.getEventTime();

					final float triggerHoldMaximumDistance = this.mTriggerHoldMaximumDistance;
					this.mMaximumDistanceExceeded = this.mMaximumDistanceExceeded || Math.abs(this.mDownX - motionEvent.getX()) > triggerHoldMaximumDistance  || Math.abs(this.mDownY - motionEvent.getY()) > triggerHoldMaximumDistance;
					if(this.mTriggerOnHold || !this.mMaximumDistanceExceeded) {
						final long holdTimeMilliseconds = upTimeMilliseconds - this.mDownTimeMilliseconds;
						if(holdTimeMilliseconds >= this.mTriggerHoldMinimumMilliseconds) {
							this.mTriggerOnHoldFinished = true;
						}
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

		public void onHold(final HoldDetector pHoldDetector, final long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY);
		public void onHoldFinished(final HoldDetector pHoldDetector, final long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY);
	}
}
