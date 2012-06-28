package org.andengine.engine.handler.timer;

import org.andengine.engine.handler.IUpdateHandler;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:23:58 - 12.03.2010
 */
public class TimerHandler implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mTimerSeconds;
	private float mTimerSecondsElapsed;
	private boolean mTimerCallbackTriggered;
	protected final ITimerCallback mTimerCallback;
	private boolean mAutoReset;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TimerHandler(final float pTimerSeconds, final ITimerCallback pTimerCallback) {
		this(pTimerSeconds, false, pTimerCallback);
	}

	public TimerHandler(final float pTimerSeconds, final boolean pAutoReset, final ITimerCallback pTimerCallback) {
		if(pTimerSeconds <= 0){
			throw new IllegalStateException("pTimerSeconds must be >= 0!");
		}

		this.mTimerSeconds = pTimerSeconds;
		this.mAutoReset = pAutoReset;
		this.mTimerCallback = pTimerCallback;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isAutoReset() {
		return this.mAutoReset;
	}

	public void setAutoReset(final boolean pAutoReset) {
		this.mAutoReset = pAutoReset;
	}

	public void setTimerSeconds(final float pTimerSeconds) {
		this.mTimerSeconds = pTimerSeconds;
	}

	public float getTimerSeconds() {
		return this.mTimerSeconds;
	}

	public float getTimerSecondsElapsed() {
		return this.mTimerSecondsElapsed;
	}
	
	public boolean isTimerCallbackTriggered() {
		return this.mTimerCallbackTriggered;
	}

	public void setTimerCallbackTriggered(boolean pTimerCallbackTriggered) {
		this.mTimerCallbackTriggered = pTimerCallbackTriggered;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		if(this.mAutoReset) {
			this.mTimerSecondsElapsed += pSecondsElapsed;
			while(this.mTimerSecondsElapsed >= this.mTimerSeconds) {
				this.mTimerSecondsElapsed -= this.mTimerSeconds;
				this.mTimerCallback.onTimePassed(this);
			}
		} else {
			if(!this.mTimerCallbackTriggered) {
				this.mTimerSecondsElapsed += pSecondsElapsed;
				if(this.mTimerSecondsElapsed >= this.mTimerSeconds) {
					this.mTimerCallbackTriggered = true;
					this.mTimerCallback.onTimePassed(this);
				}
			}
		}
	}

	@Override
	public void reset() {
		this.mTimerCallbackTriggered = false;
		this.mTimerSecondsElapsed = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
