package org.anddev.andengine.input.touch.detector;

import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:49:25 - 23.08.2010
 */
public class HoldDetector extends BaseDetector {
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final long TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT = 200;
	protected static final float TRIGGER_HOLD_MAXIMUM_DISTANCE_DEFAULT = 10;

	// ===========================================================
	// Fields
	// ===========================================================

	protected long mTriggerHoldMinimumMilliseconds;
	protected float mTriggerHoldMaximumDistance;
	protected final IHoldDetectorListener mHoldDetectorListener;

	protected long mDownTimeMilliseconds = Long.MIN_VALUE;

	protected float mDownX;
	protected float mDownY;

	protected float mHoldX;
	protected float mHoldY;

	protected boolean mMaximumDistanceExceeded;

	protected boolean mTriggering;
	protected boolean mOnHoldStartedTriggered;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HoldDetector(final IHoldDetectorListener pHoldDetectorListener) {
		this(HoldDetector.TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT, HoldDetector.TRIGGER_HOLD_MAXIMUM_DISTANCE_DEFAULT, pHoldDetectorListener);
	}

	public HoldDetector(final long pTriggerHoldMinimumMilliseconds, final float pTriggerHoldMaximumDistance, final IHoldDetectorListener pHoldDetectorListener) {
		this.mTriggerHoldMinimumMilliseconds = pTriggerHoldMinimumMilliseconds;
		this.mTriggerHoldMaximumDistance = pTriggerHoldMaximumDistance;
		this.mHoldDetectorListener = pHoldDetectorListener;
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

	public boolean isHolding() {
		return this.mTriggering;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * When {@link HoldDetector#isHolding()} this method will call through to {@link IHoldDetectorListener#onHoldFinished(HoldDetector, TouchEvent, long, float, float)}, with the supplied {@link TouchEvent} being <code>null</code>.
	 */
	@Override
	public void reset() {
		if(this.mTriggering) {
			if(this.mHoldDetectorListener != null) {
				this.mHoldDetectorListener.onHoldFinished(this, null, System.currentTimeMillis() - this.mDownTimeMilliseconds, this.mHoldX, this.mHoldY);
			}
		}

		this.mTriggering = false;
		this.mOnHoldStartedTriggered = false;
		this.mMaximumDistanceExceeded = false;
		this.mDownTimeMilliseconds = Long.MIN_VALUE;
	}

	@Override
	public boolean onManagedTouchEvent(final TouchEvent pSceneTouchEvent) {
		final MotionEvent motionEvent = pSceneTouchEvent.getMotionEvent();

		this.mHoldX = pSceneTouchEvent.getX();
		this.mHoldY = pSceneTouchEvent.getY();

		switch(pSceneTouchEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				this.mDownTimeMilliseconds = motionEvent.getDownTime();
				this.mDownX = motionEvent.getX();
				this.mDownY = motionEvent.getY();
				this.mMaximumDistanceExceeded = false;
				return true;
			case MotionEvent.ACTION_MOVE:
			{
				final long currentTimeMilliseconds = motionEvent.getEventTime();

				final long holdTimeMilliseconds = currentTimeMilliseconds - this.mDownTimeMilliseconds;
				if(holdTimeMilliseconds >= this.mTriggerHoldMinimumMilliseconds) {
					if(this.mTriggering) {
						this.triggerOnHold(pSceneTouchEvent, holdTimeMilliseconds);
					} else {
						final float triggerHoldMaximumDistance = this.mTriggerHoldMaximumDistance;
						this.mMaximumDistanceExceeded = this.mMaximumDistanceExceeded || Math.abs(this.mDownX - motionEvent.getX()) > triggerHoldMaximumDistance  || Math.abs(this.mDownY - motionEvent.getY()) > triggerHoldMaximumDistance;

						if(!this.mMaximumDistanceExceeded) {
							this.mTriggering = true;

							if(!this.mOnHoldStartedTriggered) {
								this.triggerOnHoldStarted(pSceneTouchEvent);
							} else {
								this.triggerOnHold(pSceneTouchEvent, holdTimeMilliseconds);
							}
						}
					}
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			{
				final long upTimeMilliseconds = motionEvent.getEventTime();

				final long holdTimeMilliseconds = upTimeMilliseconds - this.mDownTimeMilliseconds;
				if(holdTimeMilliseconds >= this.mTriggerHoldMinimumMilliseconds) {
					if(this.mTriggering) {
						this.triggerOnHoldFinished(pSceneTouchEvent, holdTimeMilliseconds);
					} else {
						final float triggerHoldMaximumDistance = this.mTriggerHoldMaximumDistance;
						this.mMaximumDistanceExceeded = this.mMaximumDistanceExceeded || Math.abs(this.mDownX - motionEvent.getX()) > triggerHoldMaximumDistance  || Math.abs(this.mDownY - motionEvent.getY()) > triggerHoldMaximumDistance;

						if(!this.mMaximumDistanceExceeded) {
							this.triggerOnHoldFinished(pSceneTouchEvent, holdTimeMilliseconds);
						}
					}
				}
				return true;
			}
			default:
				return false;
		}
	}

	protected void triggerOnHoldStarted(final TouchEvent pSceneTouchEvent) {
		this.mOnHoldStartedTriggered = true;

		if(this.mHoldDetectorListener != null) {
			this.mHoldDetectorListener.onHoldStarted(this, pSceneTouchEvent, this.mHoldX, this.mHoldY);
		}
	}

	protected void triggerOnHold(final TouchEvent pSceneTouchEvent, final long pHoldTimeMilliseconds) {
		if(this.mHoldDetectorListener != null) {
			this.mHoldDetectorListener.onHold(this, pSceneTouchEvent, pHoldTimeMilliseconds, this.mHoldX, this.mHoldY);
		}
	}

	protected void triggerOnHoldFinished(final TouchEvent pSceneTouchEvent, final long pHoldTimeMilliseconds) {
		this.mTriggering = false;
		this.mOnHoldStartedTriggered = false;

		if(this.mHoldDetectorListener != null) {
			this.mHoldDetectorListener.onHoldFinished(this, pSceneTouchEvent, pHoldTimeMilliseconds, this.mHoldX, this.mHoldY);
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

		public void onHoldStarted(final HoldDetector pHoldDetector, final TouchEvent pSceneTouchEvent, final float pHoldX, final float pHoldY);
		public void onHold(final HoldDetector pHoldDetector, final TouchEvent pSceneTouchEvent, final long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY);
		public void onHoldFinished(final HoldDetector pHoldDetector, final TouchEvent pSceneTouchEvent, final long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY);
	}
}
