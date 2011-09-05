package org.anddev.andengine.input.touch.detector;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;

import android.os.SystemClock;
import android.speech.tts.TextToSpeech.Engine;
import android.view.MotionEvent;

/**
 * Note: Needs to be registered as an {@link IUpdateHandler} to the {@link Engine} or {@link Scene} to work properly.
 * 
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:49:25 - 23.08.2010
 */
public class HoldDetector extends BaseDetector implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT = 200;
	private static final float TRIGGER_HOLD_MAXIMUM_DISTANCE_DEFAULT = 10;

	private static final float TIME_BETWEEN_UPDATES_DEFAULT = 0.1f;

	// ===========================================================
	// Fields
	// ===========================================================

	private long mTriggerHoldMinimumMilliseconds;
	private float mTriggerHoldMaximumDistance;
	private final IHoldDetectorListener mHoldDetectorListener;

	private long mDownTimeMilliseconds = Long.MIN_VALUE;

	private float mDownX;
	private float mDownY;

	private float mHoldX;
	private float mHoldY;

	private boolean mMaximumDistanceExceeded;

	private boolean mTriggering;
	private boolean mOnHoldStartedTriggered;
	private boolean mTriggerOnHoldStarted;
	private boolean mTriggerOnHold;
	private boolean mTriggerOnHoldFinished;

	private final TimerHandler mTimerHandler;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HoldDetector(final IHoldDetectorListener pClickDetectorListener) {
		this(HoldDetector.TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT, HoldDetector.TRIGGER_HOLD_MAXIMUM_DISTANCE_DEFAULT, HoldDetector.TIME_BETWEEN_UPDATES_DEFAULT, pClickDetectorListener);
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

	public boolean isHolding() {
		return this.mTriggering;
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

		this.mTriggering = false;
		this.mTriggerOnHoldFinished = false;
		this.mTriggerOnHold = false;
		this.mTriggerOnHoldStarted = false;
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
						this.mTriggerOnHold = true;
					} else {
						final float triggerHoldMaximumDistance = this.mTriggerHoldMaximumDistance;
						this.mMaximumDistanceExceeded = this.mMaximumDistanceExceeded || Math.abs(this.mDownX - motionEvent.getX()) > triggerHoldMaximumDistance  || Math.abs(this.mDownY - motionEvent.getY()) > triggerHoldMaximumDistance;

						if(!this.mMaximumDistanceExceeded) {
							this.mTriggering = true;

							if(!this.mOnHoldStartedTriggered) {
								this.mTriggerOnHoldStarted = true;
							} else {
								this.mTriggerOnHold = true;
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
						this.mTriggerOnHoldFinished = true;
					} else {
						final float triggerHoldMaximumDistance = this.mTriggerHoldMaximumDistance;
						this.mMaximumDistanceExceeded = this.mMaximumDistanceExceeded || Math.abs(this.mDownX - motionEvent.getX()) > triggerHoldMaximumDistance  || Math.abs(this.mDownY - motionEvent.getY()) > triggerHoldMaximumDistance;

						if(!this.mMaximumDistanceExceeded) {
							this.mTriggerOnHoldFinished = true;
						}
					}
				}
				return true;
			}
			default:
				return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void fireListener() {
		if(this.mTriggerOnHoldStarted) {
			this.mTriggerOnHoldStarted = false;
			this.mOnHoldStartedTriggered = true;
			this.mHoldDetectorListener.onHoldStarted(this, this.mHoldX, this.mHoldY);
		}

		if(this.mTriggerOnHold) {
			this.mHoldDetectorListener.onHold(this, SystemClock.uptimeMillis() - this.mDownTimeMilliseconds, this.mHoldX, this.mHoldY);
		}

		if(this.mTriggerOnHoldFinished) {
			this.mTriggering = false;
			this.mTriggerOnHoldFinished = false;
			this.mTriggerOnHold = false;
			this.mTriggerOnHoldStarted = false;
			this.mOnHoldStartedTriggered = false;
			this.mHoldDetectorListener.onHoldFinished(this, SystemClock.uptimeMillis() - this.mDownTimeMilliseconds, this.mHoldX, this.mHoldY);
		}
	}

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

		public void onHoldStarted(final HoldDetector pHoldDetector, final float pHoldX, final float pHoldY);
		public void onHold(final HoldDetector pHoldDetector, final long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY);
		public void onHoldFinished(final HoldDetector pHoldDetector, final long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY);
	}
}
