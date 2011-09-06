package org.anddev.andengine.input.touch.detector;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;

import android.os.SystemClock;
import android.speech.tts.TextToSpeech.Engine;

/**
 * Note: Needs to be registered as an {@link IUpdateHandler} to the {@link Engine} or {@link Scene} to work properly.
 * 
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
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

	private final IContinuousHoldDetectorListener mContinuousHoldDetectorListener;

	private boolean mTriggerOnHoldStarted;
	private boolean mTriggerOnHold;
	private boolean mTriggerOnHoldFinished;

	private final TimerHandler mTimerHandler;


	// ===========================================================
	// Constructors
	// ===========================================================

	public ContinuousHoldDetector(final IContinuousHoldDetectorListener pContinuousHoldDetectorListener) {
		this(HoldDetector.TRIGGER_HOLD_MINIMUM_MILLISECONDS_DEFAULT, HoldDetector.TRIGGER_HOLD_MAXIMUM_DISTANCE_DEFAULT, ContinuousHoldDetector.TIME_BETWEEN_UPDATES_DEFAULT, pContinuousHoldDetectorListener);
	}

	public ContinuousHoldDetector(final long pTriggerHoldMinimumMilliseconds, final float pTriggerHoldMaximumDistance, final float pTimeBetweenUpdates, final IContinuousHoldDetectorListener pContinuousHoldDetectorListener) {
		super(pTriggerHoldMinimumMilliseconds, pTriggerHoldMaximumDistance, null);

		this.mContinuousHoldDetectorListener = pContinuousHoldDetectorListener;

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
	 * When {@link ContinuousHoldDetector#isHolding()} this method will call through to {@link IContinuousHoldDetectorListener#onHoldFinished(ContinuousHoldDetector, long, float, float)}.
	 */
	@Override
	public void reset() {
		if(this.mTriggering) {
			if(this.mContinuousHoldDetectorListener != null) {
				this.mContinuousHoldDetectorListener.onHoldFinished(this, SystemClock.uptimeMillis() - this.mDownTimeMilliseconds, this.mHoldX, this.mHoldY);
			}
		}

		super.reset();

		this.mTriggerOnHoldFinished = false;
		this.mTriggerOnHold = false;
		this.mTriggerOnHoldStarted = false;

		this.mTimerHandler.reset();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	protected void triggerOnHoldStarted(final TouchEvent pSceneTouchEvent) {
		this.mTriggerOnHoldStarted = true;
	}

	@Override
	protected void triggerOnHold(final TouchEvent pSceneTouchEvent, final long pHoldTimeMilliseconds) {
		this.mTriggerOnHold = true;
	}

	@Override
	protected void triggerOnHoldFinished(final TouchEvent pSceneTouchEvent, final long pHoldTimeMilliseconds) {
		this.mTriggerOnHoldFinished = true;
	}

	protected void fireListener() {
		if(this.mTriggerOnHoldStarted) {
			this.mTriggerOnHoldStarted = false;
			this.mOnHoldStartedTriggered = true;

			if(this.mContinuousHoldDetectorListener != null) {
				this.mContinuousHoldDetectorListener.onHoldStarted(this, this.mHoldX, this.mHoldY);
			}
		}

		if(this.mTriggerOnHold) {
			if(this.mContinuousHoldDetectorListener != null) {
				this.mContinuousHoldDetectorListener.onHold(this, SystemClock.uptimeMillis() - this.mDownTimeMilliseconds, this.mHoldX, this.mHoldY);
			}
		}

		if(this.mTriggerOnHoldFinished) {
			this.mTriggering = false;
			this.mTriggerOnHoldFinished = false;
			this.mTriggerOnHold = false;
			this.mTriggerOnHoldStarted = false;
			this.mOnHoldStartedTriggered = false;

			if(this.mContinuousHoldDetectorListener != null) {
				this.mContinuousHoldDetectorListener.onHoldFinished(this, SystemClock.uptimeMillis() - this.mDownTimeMilliseconds, this.mHoldX, this.mHoldY);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IContinuousHoldDetectorListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onHoldStarted(final ContinuousHoldDetector pContinuousHoldDetector, final float pHoldX, final float pHoldY);
		public void onHold(final ContinuousHoldDetector pContinuousHoldDetector, final long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY);
		public void onHoldFinished(final ContinuousHoldDetector pContinuousHoldDetector, final long pHoldTimeMilliseconds, final float pHoldX, final float pHoldY);
	}
}
