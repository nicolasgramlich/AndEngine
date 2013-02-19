package org.andengine.engine.handler;

import org.andengine.util.IMatcher;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 12:24:09 - 11.03.2010
 */
public interface IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onUpdate(final float pSecondsElapsed);
	public void reset();

	// TODO Maybe add onRegister and onUnregister. (Maybe add SimpleUpdateHandler that implements all methods, but onUpdate)

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface IUpdateHandlerMatcher extends IMatcher<IUpdateHandler> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
