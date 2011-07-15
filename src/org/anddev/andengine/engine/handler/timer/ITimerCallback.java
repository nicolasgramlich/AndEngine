package org.anddev.andengine.engine.handler.timer;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:23:25 - 12.03.2010
 */
public interface ITimerCallback {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onTimePassed(final TimerHandler pTimerHandler);
}
