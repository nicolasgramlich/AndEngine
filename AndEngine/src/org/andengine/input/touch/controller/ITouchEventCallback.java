package org.andengine.input.touch.controller;

import org.andengine.input.touch.TouchEvent;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:44:26 - 04.04.2012
 */
public interface ITouchEventCallback {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean onTouchEvent(final TouchEvent pTouchEvent);
}
