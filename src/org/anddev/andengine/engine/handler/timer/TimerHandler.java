package org.anddev.andengine.engine.handler.timer;

import org.anddev.andengine.engine.handler.IUpdateHandler;

/**
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

	private final float mTimerSeconds;
	private float mSecondsPassed;
	private boolean mCallbackTriggered = false;
	private final ITimerCallback mTimerCallback;
	private final boolean mAutoReset;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TimerHandler(final float pTimerSeconds, final ITimerCallback pTimerCallback) {
		this(pTimerSeconds, false, pTimerCallback);
	}

	public TimerHandler(final float pTimerSeconds, final boolean pAutoReset, final ITimerCallback pTimerCallback) {
		this.mTimerSeconds = pTimerSeconds;
		this.mAutoReset = pAutoReset;
		this.mTimerCallback = pTimerCallback;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		if(this.mAutoReset) {
			this.mSecondsPassed += pSecondsElapsed;
			while(this.mSecondsPassed >= this.mTimerSeconds) {
				this.mSecondsPassed -= this.mTimerSeconds;
				this.mTimerCallback.onTimePassed(this);
			}
		} else {
			if(!this.mCallbackTriggered) {
				this.mSecondsPassed += pSecondsElapsed;
				if(this.mSecondsPassed >= this.mTimerSeconds) {
					this.mCallbackTriggered = true;
					this.mTimerCallback.onTimePassed(this);
				}
			}
		}
	}

	@Override
	public void reset() {
		this.mCallbackTriggered = false;
		this.mSecondsPassed = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
