package org.andengine.input.touch.detector;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import android.speech.tts.TextToSpeech.Engine;
import android.view.MotionEvent;

/**
 * Note: Needs to be registered as an {@link IUpdateHandler} to the {@link Engine} or {@link Scene} to work properly.
 * 
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @author Greg Haynes
 * @since 20:49:25 - 23.08.2010
 */
public class ContinuousHoldDetector extends HoldDetector implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float TIME_BETWEEN_UPDATES_DEFAULT = 0.1f;

	// ===========================================================
	// Fields
	// ===========================================================

	private final TimerHandler mTimerHandler;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ContinuousHoldDetector(final IHoldDetectorListener pHoldDetectorListener) {
		this(HoldDetector.TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT, HoldDetector.TRIGGER_HOLD_MAXIMUM_DISTANCE_DEFAULT, ContinuousHoldDetector.TIME_BETWEEN_UPDATES_DEFAULT, pHoldDetectorListener);
	}

	public ContinuousHoldDetector(final long pTriggerHoldMinimumMilliseconds, final float pTriggerHoldMaximumDistance, final float pTimeBetweenUpdates, final IHoldDetectorListener pHoldDetectorListener) {
		super(pTriggerHoldMinimumMilliseconds, pTriggerHoldMaximumDistance, pHoldDetectorListener);

		this.mTimerHandler = new TimerHandler(pTimeBetweenUpdates, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				ContinuousHoldDetector.this.fireListener();
			}
		});
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mTimerHandler.onUpdate(pSecondsElapsed);
	}

	/**
	 * When {@link ContinuousHoldDetector#isHolding()} this method will call through to {@link IHoldDetectorListener#onHoldFinished(HoldDetector, long, int, float, float)}.
	 */
	@Override
	public void reset() {
		super.reset();
		this.mTimerHandler.reset();
	}

	@Override
	public boolean onManagedTouchEvent(final TouchEvent pSceneTouchEvent) {
		final MotionEvent motionEvent = pSceneTouchEvent.getMotionEvent();

		switch(pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				if(this.mPointerID == TouchEvent.INVALID_POINTER_ID) {
					this.prepareHold(pSceneTouchEvent);
					return true;
				} else {
					return false;
				}
			case TouchEvent.ACTION_MOVE:
			{
				if(this.mPointerID == pSceneTouchEvent.getPointerID()) {
					this.mHoldX = pSceneTouchEvent.getX();
					this.mHoldY = pSceneTouchEvent.getY();

					this.mMaximumDistanceExceeded = this.mMaximumDistanceExceeded || Math.abs(this.mDownX - motionEvent.getX()) > this.mTriggerHoldMaximumDistance || Math.abs(this.mDownY - motionEvent.getY()) > this.mTriggerHoldMaximumDistance;
					return true;
				} else {
					return false;
				}
			}
			case TouchEvent.ACTION_UP:
			case TouchEvent.ACTION_CANCEL:
			{
				if(this.mPointerID == pSceneTouchEvent.getPointerID()) {
					this.mHoldX = pSceneTouchEvent.getX();
					this.mHoldY = pSceneTouchEvent.getY();

					if(this.mTriggering) {
						this.triggerOnHoldFinished(motionEvent.getEventTime() - this.mDownTimeMilliseconds);
					}

					this.mPointerID = TouchEvent.INVALID_POINTER_ID;
					return true;
				} else {
					return false;
				}
			}
			default:
				return false;
		}
	}

	@Override
	protected void prepareHold(final TouchEvent pSceneTouchEvent) {
		super.prepareHold(pSceneTouchEvent);
		this.mTimerHandler.reset();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	void fireListener() {
		if(this.mPointerID != TouchEvent.INVALID_POINTER_ID) {
			final long holdTimeMilliseconds = System.currentTimeMillis() - this.mDownTimeMilliseconds;
			if(holdTimeMilliseconds >= this.mTriggerHoldMinimumMilliseconds) {
				if(this.mTriggering) {
					this.triggerOnHold(holdTimeMilliseconds);
				} else if(!this.mMaximumDistanceExceeded) {
					this.triggerOnHoldStarted();
				}
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
