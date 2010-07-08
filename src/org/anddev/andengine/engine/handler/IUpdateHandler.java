package org.anddev.andengine.engine.handler;

/**
 * @author Nicolas Gramlich
 * @since 12:24:09 - 11.03.2010
 */
public interface IUpdateHandler {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onUpdate(final float pSecondsElapsed);
	public void reset();
}
