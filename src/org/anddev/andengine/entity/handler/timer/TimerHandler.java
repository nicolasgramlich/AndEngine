package org.anddev.andengine.entity.handler.timer;

import org.anddev.andengine.entity.IUpdateHandler;

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

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public TimerHandler(final float pTimerSeconds, final ITimerCallback pTimerCallback) {
		this.mTimerSeconds = pTimerSeconds;
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
		if(!this.mCallbackTriggered) {
			this.mSecondsPassed += pSecondsElapsed;
			if(this.mSecondsPassed >= this.mTimerSeconds) {
				this.mCallbackTriggered = true;
				this.mTimerCallback.onTimePassed();
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
