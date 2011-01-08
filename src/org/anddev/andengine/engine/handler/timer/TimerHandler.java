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

	private float mTimerSeconds;
	private float mTimerSecondsElapsed;
	private boolean mCallbackTriggered = false;
	protected final ITimerCallback mTimerCallback;
	private boolean mAutoReset;

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

	public boolean isAutoReset() {
		return this.mAutoReset;
	}

	public void setAutoReset(final boolean pAutoReset) {
		this.mAutoReset = pAutoReset;
	}

	/**
	 * You probably want to use this in conjunction with {@link TimerHandler#reset()}.
	 * @param pTimerSeconds
	 */
	public void setTimerSeconds(final float pTimerSeconds) {
		this.mTimerSeconds = pTimerSeconds;
	}

	public float getTimerSeconds() {
		return this.mTimerSeconds;
	}

	public float getTimerSecondsElapsed() {
		return this.mTimerSecondsElapsed;
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
			if(!this.mCallbackTriggered) {
				this.mTimerSecondsElapsed += pSecondsElapsed;
				if(this.mTimerSecondsElapsed >= this.mTimerSeconds) {
					this.mCallbackTriggered = true;
					this.mTimerCallback.onTimePassed(this);
				}
			}
		}
	}

	@Override
	public void reset() {
		this.mCallbackTriggered = false;
		this.mTimerSecondsElapsed = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
